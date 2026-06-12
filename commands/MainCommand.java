package dev.enco.greatcombat.core.commands;

import com.google.inject.Injector;
import dev.enco.greatcombat.core.commands.CommandArg;
import dev.enco.greatcombat.core.commands.Subcommand;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Locale;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Generated;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MainCommand
implements TabExecutor {
    private final ConfigManager configManager;
    private final Injector injector;

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("greatcombat.admin")) {
            return true;
        }
        if (args.length < 1) {
            this.sendHelpMessage(sender);
            return true;
        }
        try {
            CommandArg arg = CommandArg.valueOf(args[0].toUpperCase());
            ((Subcommand)this.injector.getInstance(arg.getClazz())).onCommand(sender, Arrays.copyOfRange(args, 1, args.length));
        }
        catch (IllegalArgumentException e) {
            this.sendHelpMessage(sender);
        }
        return true;
    }

    private void sendHelpMessage(CommandSender sender) {
        Locale locale = this.configManager.getLocale();
        for (String s : locale.commandHelp()) {
            sender.sendMessage(s);
        }
    }

    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!sender.hasPermission("greatcombat.admin")) {
            return null;
        }
        if (args.length == 1) {
            ArrayList<String> result = new ArrayList<String>();
            for (CommandArg cmd : CommandArg.values()) {
                result.add(cmd.name().toLowerCase());
            }
            return result;
        }
        try {
            CommandArg arg = CommandArg.valueOf(args[0].toUpperCase());
            return ((Subcommand)this.injector.getInstance(arg.getClazz())).onTab();
        }
        catch (IllegalArgumentException e) {
            return List.of();
        }
    }

    @Generated
    public MainCommand(ConfigManager configManager, Injector injector) {
        this.configManager = configManager;
        this.injector = injector;
    }
}
