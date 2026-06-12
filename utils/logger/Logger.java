package dev.enco.greatcombat.core.utils.logger;

import dev.enco.greatcombat.core.utils.logger.ILogger;
import dev.enco.greatcombat.core.utils.logger.impl.ComponentLogger;
import dev.enco.greatcombat.core.utils.logger.impl.LegacyLogger;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Logger {
    private static final String INFO_PREFIX = "\u00a77(\u00a7aGreatCombat\u00a77) \u00a7aINFO \u00a7f";
    private static final String WARN_PREFIX = "\u00a77(\u00a7eGreatCombat\u00a77) \u00a76WARN \u00a7e";
    private static final String ERROR_PREFIX = "\u00a77(\u00a7cGreatCombat\u00a77) \u00a74ERROR \u00a7c";
    private static ILogger logger;

    public static void setup(JavaPlugin plugin) {
        logger = Integer.parseInt(Bukkit.getBukkitVersion().split("-")[0].split("\\.")[1]) >= 19 ? new ComponentLogger(plugin) : new LegacyLogger();
    }

    public static void warn(String message) {
        logger.warn(WARN_PREFIX + message);
    }

    public static void info(String message) {
        logger.info(INFO_PREFIX + message);
    }

    public static void error(String message) {
        logger.error(ERROR_PREFIX + message);
    }

    @Generated
    private Logger() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
