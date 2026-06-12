package dev.enco.greatcombat.core.regions.impl;

import com.google.inject.Inject;
import dev.enco.greatcombat.api.managers.ICombatManager;
import dev.enco.greatcombat.api.models.IRegionListener;
import dev.enco.greatcombat.core.config.ConfigManager;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.event.WrappedDisallowedPVPEvent;
import org.jetbrains.annotations.NotNull;

public class WorldGuardListener
implements IRegionListener {
    private final ICombatManager combatManager;
    private final ConfigManager configManager;

    @Override
    public void registerListener(@NotNull JavaPlugin plugin) {
        WorldGuardWrapper.getInstance().registerEvents(plugin);
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }

    @EventHandler
    public void listen(WrappedDisallowedPVPEvent e) {
        if (!this.configManager.getRegionWorlds().contains(e.getAttacker().getWorld().getName())) {
            return;
        }
        boolean damagerInCombat = this.combatManager.isInCombat(e.getAttacker().getUniqueId());
        boolean targetInCombat = this.combatManager.isInCombat(e.getDefender().getUniqueId());
        if (damagerInCombat && targetInCombat) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
        }
    }

    @Inject
    @Generated
    public WorldGuardListener(ICombatManager combatManager, ConfigManager configManager) {
        this.combatManager = combatManager;
        this.configManager = configManager;
    }
}
