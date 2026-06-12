package dev.enco.greatcombat.core.actions;

import dev.enco.greatcombat.core.actions.context.Context;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Action<C extends Context> {
    public void execute(@NotNull Player var1, @Nullable C var2, String ... var3);
}
