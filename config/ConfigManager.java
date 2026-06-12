package dev.enco.greatcombat.core.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.IRegionManager;
import dev.enco.greatcombat.api.models.PowerupType;
import dev.enco.greatcombat.core.actions.ActionFactory;
import dev.enco.greatcombat.core.config.ConfigFile;
import dev.enco.greatcombat.core.config.FilesHandler;
import dev.enco.greatcombat.core.config.settings.Bossbar;
import dev.enco.greatcombat.core.config.settings.Commands;
import dev.enco.greatcombat.core.config.settings.Expansion;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.config.settings.Messages;
import dev.enco.greatcombat.core.config.settings.Powerups;
import dev.enco.greatcombat.core.config.settings.Scoreboard;
import dev.enco.greatcombat.core.config.settings.Settings;
import dev.enco.greatcombat.core.config.settings.TimeFormats;
import dev.enco.greatcombat.core.listeners.CommandsType;
import dev.enco.greatcombat.core.utils.EnumUtils;
import dev.enco.greatcombat.core.utils.LangUtils;
import dev.enco.greatcombat.core.utils.Placeholders;
import dev.enco.greatcombat.core.utils.Time;
import dev.enco.greatcombat.core.utils.colorizer.Colorizer;
import dev.enco.greatcombat.core.utils.logger.Logger;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import lombok.Generated;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@Singleton
public class ConfigManager {
    private final IRegionManager regionManager;
    private final JavaPlugin plugin;
    private boolean checkUpdates;
    private boolean metricsEnable;
    private boolean elytraGlideAllowed;
    private boolean usingPapi;
    private Commands commands;
    private Scoreboard scoreboard;
    private Powerups powerups;
    private Messages messages;
    private TimeFormats secondsFormats;
    private TimeFormats minutesFormats;
    private TimeFormats hoursFormats;
    private Bossbar bossbar;
    private Settings settings;
    private String serverManager;
    private Locale locale;
    private FileConfiguration mainConfig;
    private Expansion expansion;
    private List<String> regionWorlds;
    private List<String> checkers;

    public void reload() {
        FilesHandler.reloadAll();
        this.load();
    }

    public void load() {
        long start = System.currentTimeMillis();
        this.mainConfig = FilesHandler.getConfigFile("config").get();
        this.metricsEnable = this.mainConfig.getBoolean("metrics");
        this.setupLogger();
        LangUtils.setup(this);
        Time.setConfigManager(this);
        ConfigurationSection colorizerSection = this.mainConfig.getConfigurationSection("colorizer");
        if (colorizerSection == null) {
            this.mainConfig.createSection("colorizer");
        }
        Colorizer.setColorizer(this.mainConfig.getString("colorizer", "LEGACY"));
        this.setupScoreboard();
        FileConfiguration messagesConfig = FilesHandler.getConfigFile("messages").get();
        this.setupActions(messagesConfig);
        this.setupBossbar(messagesConfig);
        this.setupTimeFormats(this.mainConfig);
        this.setupPowerups(this.mainConfig);
        this.setupSettings(this.mainConfig);
        this.setupCommands(this.mainConfig);
        this.checkUpdates = this.mainConfig.getBoolean("update-checker");
        this.usingPapi = this.mainConfig.getBoolean("use-papi");
        Placeholders.setUsingPapi(this.usingPapi);
        ConfigurationSection expansionSec = this.mainConfig.getConfigurationSection("expansion");
        this.expansion = new Expansion(expansionSec.getString("error"), expansionSec.getString("true"), expansionSec.getString("false"), expansionSec.getString("delimiter"));
        this.regionWorlds = this.mainConfig.getStringList("region-worlds");
        LangUtils.shutdown(this.mainConfig.getBoolean("disable-lang"));
        this.elytraGlideAllowed = this.mainConfig.getBoolean("allow-elytra-gliding");
        this.checkers = this.mainConfig.getStringList("api.custom-checkers-ids");
        Logger.info(this.locale.configLoaded() + (System.currentTimeMillis() - start) + " ms.");
    }

    public boolean checkAndCreateLangFiles() {
        File languageFile = new File(this.plugin.getDataFolder(), "locale.yml");
        if (!languageFile.exists()) {
            this.plugin.saveResource("locale.yml", false);
            Logger.error("\u0412\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u044f\u0437\u044b\u043a \u0432 \u0444\u0430\u0439\u043b\u0435 locale.yml \u0438 \u043f\u0435\u0440\u0435\u0437\u0430\u043f\u0443\u0441\u0442\u0438\u0442\u0435 \u0441\u0435\u0440\u0432\u0435\u0440");
            Logger.error("Change language in file locale.yml and reboot server");
            this.plugin.getServer().getPluginManager().disablePlugin((Plugin)this.plugin);
            return false;
        }
        YamlConfiguration landConfig = YamlConfiguration.loadConfiguration((File)languageFile);
        String lang = landConfig.getString("locale");
        boolean safe = landConfig.getBoolean("replace");
        File folder = this.plugin.getDataFolder();
        FilesHandler.addConfigFile2List(new ConfigFile("messages", folder, lang, safe));
        FilesHandler.addConfigFile2List(new ConfigFile("config", folder, lang, safe));
        FilesHandler.addConfigFile2List(new ConfigFile("scoreboard", folder, lang, safe));
        FilesHandler.addConfigFile2List(new ConfigFile("logger", folder, lang, safe));
        return true;
    }

    private void setupLogger() {
        FileConfiguration lang = FilesHandler.getConfigFile("logger").get();
        this.locale = new Locale(lang.getString("on-enable"), lang.getString("on-disable"), lang.getString("author-ver"), lang.getString("config-loaded"), lang.getStringList("updates-found"), lang.getString("updates-not-found"), lang.getString("updates-error"), lang.getStringList("outdated-core"), lang.getString("tab-discarded-instance"), lang.getString("scoreboard-provider"), lang.getString("scoreboard-error"), lang.getString("handler-does-not-exist"), lang.getString("meta-does-not-exist"), lang.getString("blocker-does-not-exist"), lang.getString("powerup-does-not-exist"), lang.getString("server-manager-loading"), lang.getString("server-manager-loaded"), lang.getString("server-manager-error"), lang.getString("bar-color-error"), lang.getString("bar-style-error"), lang.getString("projectile-error"), lang.getStringList("command-help"), lang.getString("player-not-found"), lang.getString("specify-player"), lang.getString("not-in-combat"), lang.getString("stop-success"), lang.getString("stopall-success"), lang.getString("empty-item"), lang.getString("click-to-copy"), lang.getString("specify-2-players"), lang.getString("combat-started"), lang.getString("illegal-action-pattern"), lang.getString("action-does-not-exist"), lang.getString("sound-does-not-exist"), lang.getString("volume-and-pitch-error"), lang.getString("null-sound"), lang.getString("reload"), lang.getString("updated"), lang.getString("material-null"), lang.getString("material-error"), lang.getString("lang-error"), lang.getString("lang-success"));
    }

    private void setupScoreboard() {
        FileConfiguration scoreboardConfig = FilesHandler.getConfigFile("scoreboard").get();
        this.scoreboard = new Scoreboard(Colorizer.colorize(scoreboardConfig.getString("title")), ImmutableList.copyOf(Colorizer.colorizeAll(scoreboardConfig.getStringList("lines"))), Colorizer.colorize(scoreboardConfig.getString("opponent")), Colorizer.colorize(scoreboardConfig.getString("empty")), scoreboardConfig.getBoolean("enable"));
    }

    private void setupSettings(FileConfiguration config) {
        ArrayList<EntityType> ignored = new ArrayList<EntityType>();
        for (String type : config.getStringList("ignored-projectile")) {
            try {
                ignored.add(EntityType.valueOf((String)type));
            }
            catch (IllegalArgumentException e) {
                Logger.warn(MessageFormat.format(this.locale.projectileError(), type));
            }
        }
        this.settings = new Settings(config.getInt("pvp-time"), EnumUtils.toEnumSet(config.getStringList("allowed-teleportations-cause"), PlayerTeleportEvent.TeleportCause.class, c -> Logger.warn("Unknown teleport cause " + c)), ImmutableSet.copyOf(config.getStringList("ignored-worlds")), config.getBoolean("kill-on-leave"), config.getBoolean("kill-on-kick"), ImmutableSet.copyOf(config.getStringList("kick-messages")), config.getLong("tick-interval"), config.getLong("time-to-stop"), ImmutableSet.copyOf(ignored));
    }

    private void setupPowerups(FileConfiguration config) {
        this.serverManager = config.getString("server-manager");
        this.powerups = new Powerups(EnumUtils.toEnumSet(config.getStringList("prevent-start-if-damager"), PowerupType.class, s -> Logger.warn(this.locale.powerupTypeDoesNotExist())), EnumUtils.toEnumSet(config.getStringList("prevent-start-if-target"), PowerupType.class, s -> Logger.warn(this.locale.powerupTypeDoesNotExist())), EnumUtils.toEnumSet(config.getStringList("disable-for-damager"), PowerupType.class, s -> Logger.warn(this.locale.powerupTypeDoesNotExist())), EnumUtils.toEnumSet(config.getStringList("disable-for-target"), PowerupType.class, s -> Logger.warn(this.locale.powerupTypeDoesNotExist())));
    }

    private void setupCommands(FileConfiguration config) {
        ConfigurationSection section = config.getConfigurationSection("commands");
        ImmutableMap.Builder mapBuilder = ImmutableMap.builder();
        List commandMaps = section.getMapList("commands");
        for (Map map : commandMaps) {
            for (Map.Entry entry : map.entrySet()) {
                String cmd = entry.getKey().toString();
                List subCmds = (List)entry.getValue();
                mapBuilder.put(cmd, new HashSet(subCmds));
            }
        }
        this.commands = new Commands(CommandsType.valueOf(section.getString("type")), section.getBoolean("change-tabcomplete"), mapBuilder.build(), ImmutableSet.copyOf(section.getStringList("player-commands")));
    }

    private void setupBossbar(FileConfiguration config) {
        ConfigurationSection section = config.getConfigurationSection("bossbar");
        BarStyle style = BarStyle.SOLID;
        String st = section.getString("style");
        try {
            style = BarStyle.valueOf((String)st);
        }
        catch (IllegalArgumentException e) {
            Logger.warn(MessageFormat.format(this.locale.barStyleError(), st));
        }
        BarColor color = BarColor.RED;
        String c = section.getString("color");
        try {
            color = BarColor.valueOf((String)c);
        }
        catch (IllegalArgumentException e) {
            Logger.warn(MessageFormat.format(this.locale.barColorError(), c));
        }
        this.bossbar = new Bossbar(style, color, Colorizer.colorize(section.getString("title")), section.getBoolean("progress"), section.getBoolean("enable"));
    }

    private void setupActions(FileConfiguration config) {
        ConfigurationSection section = config.getConfigurationSection("actions");
        this.messages = new Messages(ActionFactory.from(this.locale, section.getStringList("on-start-damager")), ActionFactory.from(this.locale, section.getStringList("on-start-target")), ActionFactory.from(this.locale, section.getStringList("on-stop")), ActionFactory.from(this.locale, section.getStringList("on-item-cooldown")), ActionFactory.from(this.locale, section.getStringList("on-pvp-leave")), ActionFactory.from(this.locale, section.getStringList("on-pvp-command")), ActionFactory.from(this.locale, section.getStringList("on-interact-prevention")), ActionFactory.from(this.locale, section.getStringList("on-tick")), ActionFactory.from(this.locale, section.getStringList("on-player-command")), ActionFactory.from(this.locale, section.getStringList("on-join")), ActionFactory.from(this.locale, section.getStringList("on-merge")), ActionFactory.from(this.locale, section.getStringList("on-cooldown-expired")), ActionFactory.from(this.locale, section.getStringList("on-held")));
    }

    private void setupTimeFormats(FileConfiguration config) {
        ConfigurationSection section = config.getConfigurationSection("time");
        ConfigurationSection seconds = section.getConfigurationSection("seconds");
        ConfigurationSection minutes = section.getConfigurationSection("minutes");
        ConfigurationSection hours = section.getConfigurationSection("hours");
        this.secondsFormats = new TimeFormats(seconds.getString("form1"), seconds.getString("form3"), seconds.getString("form5"));
        this.minutesFormats = new TimeFormats(minutes.getString("form1"), minutes.getString("form3"), minutes.getString("form5"));
        this.hoursFormats = new TimeFormats(hours.getString("form1"), hours.getString("form3"), hours.getString("form5"));
    }

    @Inject
    @Generated
    public ConfigManager(IRegionManager regionManager, JavaPlugin plugin) {
        this.regionManager = regionManager;
        this.plugin = plugin;
    }

    @Generated
    public IRegionManager getRegionManager() {
        return this.regionManager;
    }

    @Generated
    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    @Generated
    public boolean isCheckUpdates() {
        return this.checkUpdates;
    }

    @Generated
    public boolean isMetricsEnable() {
        return this.metricsEnable;
    }

    @Generated
    public boolean isElytraGlideAllowed() {
        return this.elytraGlideAllowed;
    }

    @Generated
    public boolean isUsingPapi() {
        return this.usingPapi;
    }

    @Generated
    public Commands getCommands() {
        return this.commands;
    }

    @Generated
    public Scoreboard getScoreboard() {
        return this.scoreboard;
    }

    @Generated
    public Powerups getPowerups() {
        return this.powerups;
    }

    @Generated
    public Messages getMessages() {
        return this.messages;
    }

    @Generated
    public TimeFormats getSecondsFormats() {
        return this.secondsFormats;
    }

    @Generated
    public TimeFormats getMinutesFormats() {
        return this.minutesFormats;
    }

    @Generated
    public TimeFormats getHoursFormats() {
        return this.hoursFormats;
    }

    @Generated
    public Bossbar getBossbar() {
        return this.bossbar;
    }

    @Generated
    public Settings getSettings() {
        return this.settings;
    }

    @Generated
    public String getServerManager() {
        return this.serverManager;
    }

    @Generated
    public Locale getLocale() {
        return this.locale;
    }

    @Generated
    public FileConfiguration getMainConfig() {
        return this.mainConfig;
    }

    @Generated
    public Expansion getExpansion() {
        return this.expansion;
    }

    @Generated
    public List<String> getRegionWorlds() {
        return this.regionWorlds;
    }

    @Generated
    public List<String> getCheckers() {
        return this.checkers;
    }
}
