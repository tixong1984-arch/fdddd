package dev.enco.greatcombat.core.commands.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.core.commands.Subcommand;
import dev.enco.greatcombat.core.config.ConfigManager;
import java.util.List;
import lombok.Generated;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class ReloadSubcommand
implements Subcommand {
    private final ConfigManager configManager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        this.configManager.reload();
        sender.sendMessage(this.configManager.getLocale().reload());
        return true;
    }

    @Override
    @Nullable
    public List<String> onTab() {
        return List.of();
    }

    @Inject
    @Generated
    public ReloadSubcommand(ConfigManager configManager) {
        this.configManager = configManager;
    }
}
