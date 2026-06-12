package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.managers.IManager;
import dev.enco.greatcombat.api.models.IScheduler;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface ITaskManager
extends IManager {
    public boolean isFolia();

    @NotNull
    public IScheduler getGlobalScheduler();

    @NotNull
    public IScheduler getEntityScheduler(@NotNull Player var1);
}
