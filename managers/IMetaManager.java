package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.managers.IManager;
import dev.enco.greatcombat.api.models.IWrappedItem;
import dev.enco.greatcombat.api.models.MetaChecker;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IMetaManager
extends IManager {
    public boolean isSimilar(@NotNull IWrappedItem var1, @NotNull IWrappedItem var2, @NotNull MetaChecker[] var3);

    public void registerChecker(@NotNull String var1, @NotNull MetaChecker var2);

    @Nullable
    public MetaChecker getByID(@NotNull String var1);
}
