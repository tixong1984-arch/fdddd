package dev.enco.greatcombat.core.commands.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.ICombatManager;
import dev.enco.greatcombat.core.commands.Subcommand;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Locale;
import java.text.MessageFormat;
import java.util.List;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class CombatSubcommand
implements Subcommand {
    private final ConfigManager configManager;
    private final ICombatManager combatManager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        Locale locale = this.configManager.getLocale();
        if (args.length < 1) {
            sender.sendMessage(locale.notSpecifiedPlayer());
            return true;
        }
        Player player1 = Bukkit.getPlayer((String)args[0]);
        if (player1 == null) {
            sender.sendMessage(MessageFormat.format(locale.playerNotFound(), args[0]));
            return true;
        }
        if (args.length == 1) {
            this.combatManager.startSingle(player1);
            sender.sendMessage(locale.combatStarted());
            return true;
        }
        Player player2 = Bukkit.getPlayer((String)args[1]);
        if (player2 == null) {
            sender.sendMessage(MessageFormat.format(locale.playerNotFound(), args[1]));
            return true;
        }
        this.combatManager.startCombat(player1, player2);
        sender.sendMessage(locale.combatStarted());
        return true;
    }

    @Override
    @Nullable
    public List<String> onTab() {
        return null;
    }

    @Inject
    @Generated
    public CombatSubcommand(ConfigManager configManager, ICombatManager combatManager) {
        this.configManager = configManager;
        this.combatManager = combatManager;
    }
}
