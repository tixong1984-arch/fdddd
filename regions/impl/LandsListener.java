package dev.enco.greatcombat.core.regions.impl;

import com.google.inject.Inject;
import dev.enco.greatcombat.api.managers.ICombatManager;
import dev.enco.greatcombat.api.models.IRegionListener;
import dev.enco.greatcombat.api.models.IUser;
import dev.enco.greatcombat.core.config.ConfigManager;
import java.util.UUID;
import lombok.Generated;
import me.angeschossen.lands.api.LandsIntegration;
import me.angeschossen.lands.api.events.player.area.PlayerAreaEnterEvent;
import me.angeschossen.lands.api.land.Area;
import me.angeschossen.lands.api.player.LandPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class LandsListener
implements IRegionListener {
    private final ICombatManager combatManager;
    private final ConfigManager configManager;
    private LandsIntegration api;

    @Override
    public void registerListener(@NotNull JavaPlugin plugin) {
        this.api = LandsIntegration.of((Plugin)plugin);
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }

    @EventHandler
    public void listenEnter(PlayerAreaEnterEvent e) {
        if (!this.configManager.getRegionWorlds().contains(e.getLandPlayer().getPlayer().getWorld().getName())) {
            return;
        }
        Area area = e.getArea();
        LandPlayer landPlayer = e.getLandPlayer();
        Player player = landPlayer.getPlayer();
        UUID uuid = player.getUniqueId();
        if (this.combatManager.isInCombat(uuid)) {
            IUser user = this.combatManager.getUser(uuid);
            for (IUser opponent : user.getOpponents()) {
                LandPlayer lPlayer = this.api.getLandPlayer(opponent.getPlayerUUID());
                if (area.canPvP(landPlayer, lPlayer, false)) continue;
                e.setCancelled(true);
                break;
            }
        }
    }

    @Inject
    @Generated
    public LandsListener(ICombatManager combatManager, ConfigManager configManager) {
        this.combatManager = combatManager;
        this.configManager = configManager;
    }
}
