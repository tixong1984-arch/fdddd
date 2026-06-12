package dev.enco.greatcombat.core.scheduler;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.ITaskManager;
import dev.enco.greatcombat.api.models.IScheduler;
import dev.enco.greatcombat.core.scheduler.impl.BukkitScheduler;
import dev.enco.greatcombat.core.scheduler.impl.EntityScheduler;
import dev.enco.greatcombat.core.scheduler.impl.FoliaScheduler;
import lombok.Generated;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@Singleton
public class TaskManager
implements ITaskManager {
    private boolean IS_FOLIA;
    private final IScheduler globalScheduler;
    private final JavaPlugin plugin;

    @Inject
    public TaskManager(JavaPlugin plugin) {
        IScheduler scheduler;
        this.plugin = plugin;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            scheduler = new FoliaScheduler(plugin);
            this.IS_FOLIA = true;
        }
        catch (ClassNotFoundException e) {
            scheduler = new BukkitScheduler(plugin);
            this.IS_FOLIA = false;
        }
        this.globalScheduler = scheduler;
    }

    @Override
    public boolean isFolia() {
        return this.IS_FOLIA;
    }

    @Override
    @NotNull
    public IScheduler getEntityScheduler(@NotNull Player player) {
        if (this.IS_FOLIA) {
            return new EntityScheduler(this.plugin, player);
        }
        return this.globalScheduler;
    }

    @Override
    @Generated
    public IScheduler getGlobalScheduler() {
        return this.globalScheduler;
    }
}
