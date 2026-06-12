package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.managers.IManager;
import dev.enco.greatcombat.api.models.IPreventableItem;
import dev.enco.greatcombat.api.models.IWrappedItem;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IPreventionManager
extends IManager {
    @Nullable
    public IPreventableItem getPreventableItem(@NotNull ItemStack var1);

    @Nullable
    public IPreventableItem getPreventableItem(@NotNull IWrappedItem var1);
}
