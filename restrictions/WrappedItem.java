package dev.enco.greatcombat.core.restrictions;

import dev.enco.greatcombat.api.models.IWrappedItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public record WrappedItem(ItemStack itemStack, ItemMeta itemMeta, boolean hasMeta) implements IWrappedItem
{
    public static WrappedItem wrap(ItemStack stack) {
        return stack.hasItemMeta() ? WrappedItem.withMeta(stack) : WrappedItem.noMeta(stack);
    }

    public static WrappedItem withMeta(ItemStack itemStack) {
        ItemMeta meta;
        return new WrappedItem(itemStack, meta, (meta = itemStack.getItemMeta()) != null);
    }

    public static WrappedItem noMeta(ItemStack itemStack) {
        return new WrappedItem(itemStack, null, false);
    }
}
