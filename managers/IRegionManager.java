package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.managers.IManager;
import dev.enco.greatcombat.api.models.IRegionListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IRegionManager
extends IManager {
    public void setListener(@NotNull IRegionListener var1);

    @Nullable
    public IRegionListener getListener();
}
