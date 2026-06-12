package dev.enco.greatcombat.core.scheduler.tasks;

import dev.enco.greatcombat.api.models.WrappedTask;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;

public class FoliaTask
implements WrappedTask<ScheduledTask> {
    private final ScheduledTask task;

    @Override
    public void cancel() {
        this.task.cancel();
    }

    @Override
    @NotNull
    public ScheduledTask getRunnable() {
        return this.task;
    }

    @Generated
    public FoliaTask(ScheduledTask task) {
        this.task = task;
    }
}
