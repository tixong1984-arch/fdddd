package dev.enco.greatcombat.core.scheduler.impl;

import dev.enco.greatcombat.api.models.IScheduler;
import dev.enco.greatcombat.api.models.WrappedTask;
import dev.enco.greatcombat.core.scheduler.tasks.FoliaTask;
import lombok.Generated;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class EntityScheduler
implements IScheduler {
    private final JavaPlugin plugin;
    private final Player player;

    @Override
    public void run(@NotNull Runnable task) {
        this.player.getScheduler().run((Plugin)this.plugin, t -> task.run(), null);
    }

    @Override
    public void runAsync(@NotNull Runnable task) {
        this.run(task);
    }

    @Override
    public void runLater(@NotNull Runnable task, long delay) {
        this.player.getScheduler().runDelayed((Plugin)this.plugin, t -> task.run(), null, delay);
    }

    @Override
    public void runLaterAsync(@NotNull Runnable task, long delay) {
        this.runLater(task, delay);
    }

    @Override
    @NotNull
    public WrappedTask<?> runRepeating(@NotNull Runnable task, long delay, long period) {
        return new FoliaTask(this.player.getScheduler().runAtFixedRate((Plugin)this.plugin, t -> task.run(), null, delay, period));
    }

    @Override
    @NotNull
    public WrappedTask<?> runRepeatingAsync(@NotNull Runnable task, long delay, long period) {
        return this.runRepeating(task, delay, period);
    }

    @Generated
    public EntityScheduler(JavaPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }
}
