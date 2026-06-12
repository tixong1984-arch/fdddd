package dev.enco.greatcombat.api.models;

import dev.enco.greatcombat.api.models.WrappedTask;
import org.jetbrains.annotations.NotNull;

public interface IScheduler {
    public void run(@NotNull Runnable var1);

    public void runAsync(@NotNull Runnable var1);

    public void runLater(@NotNull Runnable var1, long var2);

    public void runLaterAsync(@NotNull Runnable var1, long var2);

    @NotNull
    public WrappedTask<?> runRepeating(@NotNull Runnable var1, long var2, long var4);

    @NotNull
    public WrappedTask<?> runRepeatingAsync(@NotNull Runnable var1, long var2, long var4);
}
