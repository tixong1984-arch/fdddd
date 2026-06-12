package dev.enco.greatcombat.core.actions.impl;

import dev.enco.greatcombat.core.actions.Action;
import dev.enco.greatcombat.core.actions.context.StringContext;
import dev.enco.greatcombat.core.utils.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConsoleAction
implements Action<StringContext> {
    @Override
    public void execute(@NotNull Player player, StringContext context, String ... replacement) {
        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)Placeholders.replaceInMessage(player, context.string(), replacement));
    }
}
