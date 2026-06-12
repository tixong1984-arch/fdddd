package dev.enco.greatcombat.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import dev.enco.greatcombat.api.GreatCombatPlugin;
import dev.enco.greatcombat.api.GreatCombatProvider;
import dev.enco.greatcombat.api.managers.ICombatManager;
import dev.enco.greatcombat.api.managers.IInteractionManager;
import dev.enco.greatcombat.api.managers.IManager;
import dev.enco.greatcombat.api.managers.IRegionManager;
import dev.enco.greatcombat.api.managers.ITaskManager;
import dev.enco.greatcombat.api.models.IRegionListener;
import dev.enco.greatcombat.core.GreatCombatModule;
import dev.enco.greatcombat.core.commands.MainCommand;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.listeners.CombatListener;
import dev.enco.greatcombat.core.listeners.PlayerListener;
import dev.enco.greatcombat.core.regions.RegionProvider;
import dev.enco.greatcombat.core.utils.PapiExpansion;
import dev.enco.greatcombat.core.utils.UpdateUtils;
import dev.enco.greatcombat.core.utils.logger.Logger;
import dev.enco.greatcombat.core.utils.metrics.bukkit.Metrics;
import lombok.Generated;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class GreatCombat
extends JavaPlugin
implements GreatCombatPlugin {
    private ICombatManager combatManager;
    private IRegionManager regionManager;
    private ConfigManager configManager;
    private Injector injector;

    @Override
    @NotNull
    public <T extends IManager> T getManager(@NotNull Class<T> clazz) {
        return (T)((IManager)this.injector.getInstance(clazz));
    }

    public void onEnable() {
        Logger.setup(this);
        this.injector = Guice.createInjector((Module[])new Module[]{new GreatCombatModule(this)});
        this.configManager = (ConfigManager)this.injector.getInstance(ConfigManager.class);
        if (!this.configManager.checkAndCreateLangFiles()) {
            return;
        }
        this.configManager.load();
        this.combatManager = this.getManager(ICombatManager.class);
        PlayerListener playerListener = (PlayerListener)this.injector.getInstance(PlayerListener.class);
        CombatListener combatListener = (CombatListener)this.injector.getInstance(CombatListener.class);
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents((Listener)playerListener, (Plugin)this);
        pm.registerEvents((Listener)combatListener, (Plugin)this);
        PluginCommand command = this.getCommand("greatcombat");
        MainCommand cmd = new MainCommand(this.configManager, this.injector);
        command.setExecutor((CommandExecutor)cmd);
        command.setTabCompleter((TabCompleter)cmd);
        if (this.configManager.isMetricsEnable()) {
            new Metrics(this, 25444);
        }
        if (this.configManager.isUsingPapi()) {
            ((PapiExpansion)((Object)this.injector.getInstance(PapiExpansion.class))).register();
        }
        this.regionManager = this.getManager(IRegionManager.class);
        try {
            String rm = this.configManager.getMainConfig().getString("region-manager");
            RegionProvider provider = RegionProvider.valueOf(rm.toUpperCase());
            this.regionManager.setListener((IRegionListener)this.injector.getInstance(provider.getClazz()));
        }
        catch (IllegalArgumentException rm) {
            // empty catch block
        }
        this.getManager(IInteractionManager.class).registerDefaults();
        Locale locale = this.configManager.getLocale();
        Logger.info(locale.onEnable());
        Logger.info(locale.authorVersion() + this.getDescription().getVersion());
        if (this.configManager.isCheckUpdates()) {
            this.getManager(ITaskManager.class).getGlobalScheduler().runLaterAsync(() -> UpdateUtils.check(locale, version -> {
                if (this.getDescription().getVersion().equals(version)) {
                    Logger.info(locale.updatesNotFound());
                } else {
                    for (String s : locale.updatesFound()) {
                        Logger.info(s);
                    }
                }
            }), 30L);
        }
        GreatCombatProvider.setPlugin(this);
    }

    public void onDisable() {
        Locale locale;
        if (this.combatManager != null) {
            this.combatManager.stop();
        }
        if ((locale = this.configManager.getLocale()) != null) {
            Logger.info(locale.onDisable());
            Logger.info(locale.authorVersion() + this.getDescription().getVersion());
        }
    }

    @Generated
    public ICombatManager getCombatManager() {
        return this.combatManager;
    }

    @Generated
    public IRegionManager getRegionManager() {
        return this.regionManager;
    }

    @Generated
    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    @Generated
    public Injector getInjector() {
        return this.injector;
    }
}
