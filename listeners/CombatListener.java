package dev.enco.greatcombat.core.listeners;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import dev.enco.greatcombat.api.events.CombatContinueEvent;
import dev.enco.greatcombat.api.events.CombatEndEvent;
import dev.enco.greatcombat.api.events.CombatJoinEvent;
import dev.enco.greatcombat.api.events.CombatMergeEvent;
import dev.enco.greatcombat.api.events.CombatPreStartEvent;
import dev.enco.greatcombat.api.events.CombatStartEvent;
import dev.enco.greatcombat.api.events.CombatTickEvent;
import dev.enco.greatcombat.api.events.PlayerKickInCombatEvent;
import dev.enco.greatcombat.api.events.PlayerLeaveInCombatEvent;
import dev.enco.greatcombat.api.managers.ICombatManager;
import dev.enco.greatcombat.api.managers.ICooldownManager;
import dev.enco.greatcombat.api.managers.IPowerupsManager;
import dev.enco.greatcombat.api.managers.IScoreboardManager;
import dev.enco.greatcombat.api.models.IUser;
import dev.enco.greatcombat.api.models.WrappedTask;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Messages;
import dev.enco.greatcombat.core.config.settings.Powerups;
import dev.enco.greatcombat.core.config.settings.Settings;
import dev.enco.greatcombat.core.utils.Time;
import lombok.Generated;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class CombatListener
implements Listener {
    private final ICombatManager combatManager;
    private final ConfigManager configManager;
    private final IPowerupsManager powerupsManager;
    private final IScoreboardManager scoreboardManager;
    private final ICooldownManager cooldownManager;

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void onPreStart(CombatPreStartEvent e) {
        Player damager = e.getDamager();
        Player target = e.getTarget();
        Settings settings = this.configManager.getSettings();
        if (settings.ignoredWorlds().contains(damager.getWorld().getName())) {
            e.setCancelled(true);
            return;
        }
        Powerups powerups = this.configManager.getPowerups();
        if (this.powerupsManager.hasPowerups(damager, powerups.preventableDamagerPowerups()) || this.powerupsManager.hasPowerups(target, powerups.preventableTargetPowerups())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onContinue(CombatContinueEvent e) {
        long now = System.currentTimeMillis();
        e.getTarget().refresh(now);
        e.getDamager().refresh(now);
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onJoin(CombatJoinEvent e) {
        IUser user = e.isDamagerJoiner() ? e.getTarget() : e.getDamager();
        IUser joiner = e.isDamagerJoiner() ? e.getDamager() : e.getTarget();
        this.addBoth(joiner, user);
        Player userPlayer = user.asPlayer();
        long now = System.currentTimeMillis();
        user.refresh(now);
        Messages messages = this.configManager.getMessages();
        messages.onJoin().execute(joiner.asPlayer(), userPlayer.getName());
        Powerups powerups = this.configManager.getPowerups();
        this.processStart(joiner, powerups, now, e.getDamager().getPlayerUUID().equals(joiner.getPlayerUUID()));
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onStart(CombatStartEvent e) {
        IUser damager = e.getDamager();
        IUser target = e.getTarget();
        this.addBoth(damager, target);
        long now = System.currentTimeMillis();
        Powerups powerups = this.configManager.getPowerups();
        this.processStart(damager, powerups, now, true);
        this.processStart(target, powerups, now, false);
        Messages messages = this.configManager.getMessages();
        Player damagerPlayer = damager.asPlayer();
        Player targetPlayer = target.asPlayer();
        messages.onStartDamager().execute(damagerPlayer, targetPlayer.getName());
        messages.onStartTarget().execute(targetPlayer, damagerPlayer.getName());
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onMerge(CombatMergeEvent e) {
        IUser damager = e.getDamager();
        IUser target = e.getTarget();
        this.addBoth(damager, target);
        long now = System.currentTimeMillis();
        damager.refresh(now);
        target.refresh(now);
        Player damagerPlayer = damager.asPlayer();
        Player targetPlayer = target.asPlayer();
        Messages messages = this.configManager.getMessages();
        messages.onMerge().execute(damagerPlayer, targetPlayer.getName());
        messages.onMerge().execute(targetPlayer, damagerPlayer.getName());
    }

    private void processStart(IUser user, Powerups powerups, long now, boolean damager) {
        Player player = user.asPlayer();
        this.powerupsManager.disablePowerups(player, damager ? powerups.disablingDamagerPowerups() : powerups.disablingTargetPowerups());
        user.setStartPvpTime(now);
        user.startTimer();
    }

    private void addBoth(IUser user1, IUser user2) {
        user1.addOpponent(user2);
        user2.addOpponent(user1);
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onEndCombat(CombatEndEvent e) {
        IUser user = e.getUser();
        WrappedTask<?> runnable = user.getRunnable();
        if (runnable != null) {
            runnable.cancel();
        }
        user.removeFromOpponentsMaps();
        user.deleteBossbar();
        this.scoreboardManager.resetScoreboard(user);
        this.combatManager.removeFromCombatMap(user);
        Player player = user.asPlayer();
        Messages messages = this.configManager.getMessages();
        messages.onStop().execute(player, new String[0]);
        this.cooldownManager.clearPlayerCooldowns(player);
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onCombatQuit(PlayerLeaveInCombatEvent e) {
        IUser user = e.getUser();
        this.combatManager.stopCombat(user);
        Player player = user.asPlayer();
        Messages messages = this.configManager.getMessages();
        Settings settings = this.configManager.getSettings();
        if (settings.killOnLeave() && !player.hasPermission("greatcombat.kill.bypass")) {
            player.setHealth(0.0);
            messages.onPvpLeave().execute(player, player.getName());
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onCombatKick(PlayerKickInCombatEvent e) {
        ImmutableSet<String> kickmessages;
        IUser user = e.getUser();
        this.combatManager.stopCombat(user);
        Player player = user.asPlayer();
        Messages messages = this.configManager.getMessages();
        Settings settings = this.configManager.getSettings();
        if (settings.killOnKick() && !player.hasPermission("greatcombat.kill.bypass") && ((kickmessages = settings.kickMessages()).isEmpty() || kickmessages.contains(e.getReason()))) {
            player.setHealth(0.0);
            messages.onPvpLeave().execute(player, player.getName());
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onCombatTick(CombatTickEvent e) {
        IUser user = e.getUser();
        long remainingTime = user.getRemaining();
        Messages messages = this.configManager.getMessages();
        Settings settings = this.configManager.getSettings();
        if (remainingTime < settings.minTime()) {
            this.combatManager.stopCombat(user);
        } else {
            messages.onTick().execute(user.asPlayer(), Time.format((int)(remainingTime / 1000L)));
            user.updateBoardAndBar(remainingTime);
        }
    }

    @Inject
    @Generated
    public CombatListener(ICombatManager combatManager, ConfigManager configManager, IPowerupsManager powerupsManager, IScoreboardManager scoreboardManager, ICooldownManager cooldownManager) {
        this.combatManager = combatManager;
        this.configManager = configManager;
        this.powerupsManager = powerupsManager;
        this.scoreboardManager = scoreboardManager;
        this.cooldownManager = cooldownManager;
    }
}
