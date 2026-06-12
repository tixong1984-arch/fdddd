package dev.enco.greatcombat.core.commands;

import java.util.List;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Subcommand {
    public boolean onCommand(@NotNull CommandSender var1, @NotNull String[] var2);

    @Nullable
    public List<String> onTab();
}
