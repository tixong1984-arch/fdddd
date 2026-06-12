package dev.enco.greatcombat.core.listeners;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import dev.enco.greatcombat.api.events.PlayerKickInCombatEvent;
import dev.enco.greatcombat.api.events.PlayerLeaveInCombatEvent;
import dev.enco.greatcombat.api.managers.ICombatManager;
import dev.enco.greatcombat.api.managers.ICooldownManager;
import dev.enco.greatcombat.api.models.ICooldownItem;
import dev.enco.greatcombat.api.models.IUser;
import dev.enco.greatcombat.core.actions.ActionMap;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Commands;
import dev.enco.greatcombat.core.config.settings.Messages;
import dev.enco.greatcombat.core.config.settings.Settings;
import dev.enco.greatcombat.core.utils.Time;
import java.util.Set;
import java.util.UUID;
import lombok.Generated;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;

public class PlayerListener
implements Listener {
    private final JavaPlugin plugin;
    private final ICombatManager combatManager;
    private final ConfigManager configManager;
    private final ICooldownManager cooldownManager;
    private final PluginManager pm = Bukkit.getPluginManager();

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onDamage(EntityDamageByEntityEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player) {
            Player target = (Player)entity;
            damager = this.getDamager(e.getDamager());
            if (damager != null) {
                this.combatManager.startCombat((Player)damager, target);
            }
        } else {
            damager = e.getEntity();
            if (damager instanceof EnderCrystal) {
                EnderCrystal crystal = (EnderCrystal)damager;
                Player exploder = this.getDamager(e.getDamager());
                if (exploder != null) {
                    crystal.setMetadata("exploder", (MetadataValue)new FixedMetadataValue((Plugin)this.plugin, (Object)exploder.getUniqueId()));
                }
            }
        }
    }

    private Player getDamager(Entity damager) {
        EnderCrystal crystal;
        TNTPrimed tnt;
        AreaEffectCloud cl;
        Player pl;
        Projectile pr;
        ProjectileSource projectileSource;
        Settings settings = this.configManager.getSettings();
        if (damager instanceof Player) {
            Player pl2 = (Player)damager;
            return pl2;
        }
        if (damager instanceof Projectile && (projectileSource = (pr = (Projectile)damager).getShooter()) instanceof Player) {
            pl = (Player)projectileSource;
            if (!settings.ignoredProjectile().contains(damager.getType())) {
                return pl;
            }
        }
        if (damager instanceof AreaEffectCloud && (projectileSource = (cl = (AreaEffectCloud)damager).getSource()) instanceof Player) {
            pl = (Player)projectileSource;
            return pl;
        }
        if (damager instanceof TNTPrimed && (projectileSource = (tnt = (TNTPrimed)damager).getSource()) instanceof Player) {
            pl = (Player)projectileSource;
            return pl;
        }
        if (damager instanceof EnderCrystal && (crystal = (EnderCrystal)damager).hasMetadata("exploder")) {
            return Bukkit.getPlayer((UUID)((UUID)((MetadataValue)crystal.getMetadata("exploder").get(0)).value()));
        }
        return null;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        IUser user = this.combatManager.getUser(uuid);
        if (user != null) {
            this.pm.callEvent((Event)new PlayerLeaveInCombatEvent(user));
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        IUser user = this.combatManager.getUser(uuid);
        if (user != null) {
            this.pm.callEvent((Event)new PlayerKickInCombatEvent(user, ChatColor.stripColor((String)e.getReason())));
        }
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPreprocess(PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        if (player.hasPermission("greatcombat.commands.bypass")) {
            return;
        }
        this.handlePlayerCommand(player, e.getMessage(), (Cancellable)e);
        UUID uuid = player.getUniqueId();
        Commands commands = this.configManager.getCommands();
        Messages messages = this.configManager.getMessages();
        if (this.combatManager.isInCombat(uuid) && !commands.commands().isEmpty()) {
            String[] args = e.getMessage().split(" ");
            String command = args[0].substring(1).toLowerCase();
            String subCommand = args.length > 1 ? args[1].toLowerCase() : null;
            boolean cancel = false;
            boolean match = this.matchSubcommands(command, subCommand, commands);
            switch (commands.changeType()) {
                case BLACKLIST: {
                    if (!match) break;
                    cancel = true;
                    break;
                }
                case WHITELIST: {
                    if (match) break;
                    cancel = true;
                }
            }
            if (cancel) {
                messages.onPvpCommand().execute(player, new String[0]);
                e.setCancelled(true);
            }
        }
    }

    private boolean matchSubcommands(String command, String subCommand, Commands commands) {
        ImmutableMap<String, Set<String>> cmds = commands.commands();
        Set<String> subCmds = cmds.get(command);
        if (subCmds == null) {
            return false;
        }
        if (subCmds.isEmpty()) {
            return true;
        }
        if (subCommand == null) {
            return false;
        }
        return subCmds.contains(subCommand);
    }

    private void handlePlayerCommand(Player sender, String command, Cancellable e) {
        Commands commands = this.configManager.getCommands();
        Messages messages = this.configManager.getMessages();
        for (String s : commands.playerCommands()) {
            if (!command.startsWith(s)) continue;
            try {
                String targetName = command.replace(s, "").split(" ")[1];
                Player player = Bukkit.getPlayer((String)targetName);
                if (player == null || !this.combatManager.isInCombat(player.getUniqueId())) continue;
                e.setCancelled(true);
                messages.onPlayerCommand().execute(sender, targetName);
                break;
            }
            catch (IndexOutOfBoundsException indexOutOfBoundsException) {
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onTeleport(PlayerTeleportEvent e) {
        Settings settings = this.configManager.getSettings();
        if (settings.allowedTpCause().contains(e.getCause())) {
            return;
        }
        Player player = e.getPlayer();
        if (player.hasPermission("greatcombat.teleports.bypass")) {
            return;
        }
        if (!this.combatManager.isInCombat(player.getUniqueId())) {
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler(priority=EventPriority.LOWEST)
    public void onSend(PlayerCommandSendEvent e) {
        ImmutableMap<String, Set<String>> cmds;
        Commands commands;
        Player player = e.getPlayer();
        if (player.hasPermission("greatcombat.commands.bypass")) {
            return;
        }
        UUID uuid = player.getUniqueId();
        if (this.combatManager.isInCombat(uuid) && (commands = this.configManager.getCommands()).changeComplete() && !(cmds = commands.commands()).isEmpty()) {
            switch (commands.changeType()) {
                case WHITELIST: {
                    e.getCommands().clear();
                    e.getCommands().addAll(cmds.keySet());
                    break;
                }
                case BLACKLIST: {
                    e.getCommands().removeAll(cmds.keySet());
                }
            }
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled=true)
    public void onDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        UUID uuid = player.getUniqueId();
        if (this.combatManager.isInCombat(uuid)) {
            this.combatManager.stopCombat(this.combatManager.getUser(uuid));
        }
    }

    @EventHandler(ignoreCancelled=true)
    public void onHeld(PlayerItemHeldEvent e) {
        ActionMap actionMap = this.configManager.getMessages().onItemHeld();
        if (actionMap.isEmpty()) {
            return;
        }
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();
        if (this.combatManager.isInCombat(uuid)) {
            ItemStack is = player.getInventory().getItem(e.getNewSlot());
            if (is == null) {
                return;
            }
            ICooldownItem item = this.cooldownManager.getCooldownItem(is);
            if (item == null) {
                return;
            }
            if (this.cooldownManager.hasCooldown(uuid, item)) {
                actionMap.execute(player, item.translation(), Time.format(this.cooldownManager.getCooldownTime(uuid, item)));
            }
        }
    }

    @EventHandler
    public void onGlide(EntityToggleGlideEvent e) {
        if (this.configManager.isElytraGlideAllowed()) {
            return;
        }
        Entity entity = e.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }
        Player player = (Player)entity;
        if (player.hasPermission("greatcombat.glide.bypass")) {
            return;
        }
        ItemStack chestplate = player.getInventory().getChestplate();
        if (chestplate == null || chestplate.getType() != Material.ELYTRA) {
            return;
        }
        if (this.combatManager.isInCombat(player.getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @Inject
    @Generated
    public PlayerListener(JavaPlugin plugin, ICombatManager combatManager, ConfigManager configManager, ICooldownManager cooldownManager) {
        this.plugin = plugin;
        this.combatManager = combatManager;
        this.configManager = configManager;
        this.cooldownManager = cooldownManager;
    }
}
