package dev.enco.greatcombat.api.models;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IWrappedItem {
    @NotNull
    public ItemStack itemStack();

    @Nullable
    public ItemMeta itemMeta();

    public boolean hasMeta();
}
