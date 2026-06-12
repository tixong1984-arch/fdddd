package dev.enco.greatcombat.core.commands.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.ITaskManager;
import dev.enco.greatcombat.core.commands.Subcommand;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.utils.UpdateUtils;
import java.util.List;
import lombok.Generated;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class UpdateSubcommand
implements Subcommand {
    private final ITaskManager taskManager;
    private final JavaPlugin plugin;
    private final ConfigManager configManager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        this.taskManager.getGlobalScheduler().runAsync(() -> UpdateUtils.check(this.configManager.getLocale(), ver -> {
            String current = this.plugin.getDescription().getVersion();
            Locale locale = this.configManager.getLocale();
            if (ver.equals(current)) {
                sender.sendMessage(locale.updatesNotFound());
                return;
            }
            UpdateUtils.update(this.plugin, ver, sender);
            sender.sendMessage(locale.updated());
        }));
        return true;
    }

    @Override
    @Nullable
    public List<String> onTab() {
        return List.of();
    }

    @Inject
    @Generated
    public UpdateSubcommand(ITaskManager taskManager, JavaPlugin plugin, ConfigManager configManager) {
        this.taskManager = taskManager;
        this.plugin = plugin;
        this.configManager = configManager;
    }
}
