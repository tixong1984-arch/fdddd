package dev.enco.greatcombat.core.scheduler.tasks;

import dev.enco.greatcombat.api.models.WrappedTask;
import lombok.Generated;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class BukkitTask
implements WrappedTask<BukkitRunnable> {
    private final BukkitRunnable runnable;

    @Override
    public void cancel() {
        this.runnable.cancel();
    }

    @Override
    @NotNull
    public BukkitRunnable getRunnable() {
        return this.runnable;
    }

    @Generated
    public BukkitTask(BukkitRunnable runnable) {
        this.runnable = runnable;
    }
}
