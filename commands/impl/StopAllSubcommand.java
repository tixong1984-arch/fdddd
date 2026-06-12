package dev.enco.greatcombat.core.commands.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.ICombatManager;
import dev.enco.greatcombat.core.commands.Subcommand;
import dev.enco.greatcombat.core.config.ConfigManager;
import java.util.List;
import lombok.Generated;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class StopAllSubcommand
implements Subcommand {
    private final ICombatManager combatManager;
    private final ConfigManager configManager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        this.combatManager.stop();
        sender.sendMessage(this.configManager.getLocale().stopAllSuccess());
        return true;
    }

    @Override
    @Nullable
    public List<String> onTab() {
        return List.of();
    }

    @Inject
    @Generated
    public StopAllSubcommand(ICombatManager combatManager, ConfigManager configManager) {
        this.combatManager = combatManager;
        this.configManager = configManager;
    }
}
