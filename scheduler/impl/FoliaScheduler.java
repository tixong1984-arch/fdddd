package dev.enco.greatcombat.core.scheduler.impl;

import dev.enco.greatcombat.api.models.IScheduler;
import dev.enco.greatcombat.api.models.WrappedTask;
import dev.enco.greatcombat.core.scheduler.tasks.FoliaTask;
import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import java.util.concurrent.TimeUnit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class FoliaScheduler
implements IScheduler {
    private final JavaPlugin plugin;
    private final GlobalRegionScheduler globalScheduler;
    private final AsyncScheduler asyncScheduler;

    public FoliaScheduler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.globalScheduler = plugin.getServer().getGlobalRegionScheduler();
        this.asyncScheduler = plugin.getServer().getAsyncScheduler();
    }

    @Override
    public void run(@NotNull Runnable task) {
        this.globalScheduler.run((Plugin)this.plugin, t -> task.run());
    }

    @Override
    public void runAsync(@NotNull Runnable task) {
        this.asyncScheduler.runNow((Plugin)this.plugin, t -> task.run());
    }

    @Override
    public void runLater(@NotNull Runnable task, long delay) {
        this.globalScheduler.runDelayed((Plugin)this.plugin, t -> task.run(), delay);
    }

    @Override
    public void runLaterAsync(@NotNull Runnable task, long delay) {
        this.asyncScheduler.runDelayed((Plugin)this.plugin, t -> task.run(), delay * 50L, TimeUnit.MILLISECONDS);
    }

    @Override
    @NotNull
    public WrappedTask<?> runRepeating(@NotNull Runnable task, long delay, long period) {
        return new FoliaTask(this.globalScheduler.runAtFixedRate((Plugin)this.plugin, t -> task.run(), delay, period));
    }

    @Override
    @NotNull
    public WrappedTask<?> runRepeatingAsync(@NotNull Runnable task, long delay, long period) {
        return new FoliaTask(this.asyncScheduler.runAtFixedRate((Plugin)this.plugin, t -> task.run(), delay * 50L, period * 50L, TimeUnit.MILLISECONDS));
    }
}
