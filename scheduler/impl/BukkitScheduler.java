package dev.enco.greatcombat.core.scheduler.impl;

import dev.enco.greatcombat.api.models.IScheduler;
import dev.enco.greatcombat.api.models.WrappedTask;
import dev.enco.greatcombat.core.scheduler.tasks.BukkitTask;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class BukkitScheduler
implements IScheduler {
    private final JavaPlugin plugin;

    @Override
    public void run(@NotNull Runnable task) {
        Bukkit.getScheduler().runTask((Plugin)this.plugin, task);
    }

    @Override
    public void runAsync(@NotNull Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)this.plugin, task);
    }

    @Override
    public void runLater(@NotNull Runnable task, long delay) {
        Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, task, delay);
    }

    @Override
    public void runLaterAsync(@NotNull Runnable task, long delay) {
        Bukkit.getScheduler().runTaskLaterAsynchronously((Plugin)this.plugin, task, delay);
    }

    @Override
    @NotNull
    public WrappedTask<?> runRepeating(final @NotNull Runnable task, long delay, long period) {
        BukkitRunnable runnable = new BukkitRunnable(){

            public void run() {
                task.run();
            }
        };
        runnable.runTaskTimer((Plugin)this.plugin, delay, period);
        return new BukkitTask(runnable);
    }

    @Override
    @NotNull
    public WrappedTask<?> runRepeatingAsync(final @NotNull Runnable task, long delay, long period) {
        BukkitRunnable runnable = new BukkitRunnable(){

            public void run() {
                task.run();
            }
        };
        runnable.runTaskTimerAsynchronously((Plugin)this.plugin, delay, period);
        return new BukkitTask(runnable);
    }

    @Generated
    public BukkitScheduler(JavaPlugin plugin) {
        this.plugin = plugin;
    }
}
