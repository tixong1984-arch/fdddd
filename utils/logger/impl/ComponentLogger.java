package dev.enco.greatcombat.core.utils.logger.impl;

import dev.enco.greatcombat.core.utils.logger.ILogger;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.plugin.java.JavaPlugin;

public class ComponentLogger
implements ILogger {
    private final net.kyori.adventure.text.logger.slf4j.ComponentLogger logger;
    private final LegacyComponentSerializer legacySection;

    public ComponentLogger(JavaPlugin plugin) {
        this.logger = plugin.getComponentLogger();
        this.legacySection = LegacyComponentSerializer.legacySection();
    }

    @Override
    public void info(String s) {
        this.logger.info((Component)this.deserialize(s));
    }

    @Override
    public void warn(String s) {
        this.logger.warn((Component)this.deserialize(s));
    }

    @Override
    public void error(String s) {
        this.logger.error((Component)this.deserialize(s));
    }

    private TextComponent deserialize(String s) {
        return this.legacySection.deserialize(s);
    }
}
