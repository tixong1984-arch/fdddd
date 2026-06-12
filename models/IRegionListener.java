package dev.enco.greatcombat.api.models;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public interface IRegionListener
extends Listener {
    default public void registerListener(@NotNull JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)plugin);
    }

    default public void unregisterListener() {
        HandlerList.unregisterAll((Listener)this);
    }
}
