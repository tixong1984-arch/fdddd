package dev.enco.greatcombat.core.utils.logger.impl;

import dev.enco.greatcombat.core.utils.logger.ILogger;
import java.util.logging.Logger;
import org.bukkit.Bukkit;

public class LegacyLogger
implements ILogger {
    private final Logger logger = Bukkit.getLogger();

    @Override
    public void info(String s) {
        this.logger.info(s);
    }

    @Override
    public void warn(String s) {
        this.logger.warning(s);
    }

    @Override
    public void error(String s) {
        this.logger.severe(s);
    }
}
