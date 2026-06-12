package dev.enco.greatcombat.core.utils;

import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Base64;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.UUID;
import lombok.Generated;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class ItemUtils {
    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();
    private static final Reference2ObjectMap<UUID, EnumMap<Material, ItemStack[]>> stored = new Reference2ObjectOpenHashMap<UUID, EnumMap<Material, ItemStack[]>>();

    public static String encode(ItemStack item) {
        return encoder.encodeToString(item.serializeAsBytes());
    }

    public static ItemStack decode(String s) {
        return ItemStack.deserializeBytes((byte[])decoder.decode(s));
    }

    public static ItemStack[] findAndRemove(Inventory inventory, Material material) {
        ArrayList<ItemStack> result = new ArrayList<ItemStack>();
        int size = inventory.getSize();
        for (int i = 0; i < size; ++i) {
            ItemStack stack = inventory.getItem(i);
            if (stack == null || stack.getType() != material) continue;
            result.add(stack.clone());
            inventory.setItem(i, null);
        }
        return result.toArray(new ItemStack[0]);
    }

    public static void giveOrDrop(Player player, ItemStack[] items) {
        HashMap over = player.getInventory().addItem(items);
        if (!over.isEmpty()) {
            World world = player.getWorld();
            Location location = player.getLocation();
            for (ItemStack stack : over.values()) {
                world.dropItemNaturally(location, stack);
            }
        }
    }

    public static void removeItems(Player player, Material material) {
        UUID uuid = player.getUniqueId();
        EnumMap<Material, ItemStack[]> matMap = stored.getOrDefault(uuid, new EnumMap(Material.class));
        matMap.put(material, ItemUtils.findAndRemove((Inventory)player.getInventory(), material));
        stored.put(uuid, matMap);
    }

    public static void backItems(Player player) {
        UUID uuid = player.getUniqueId();
        EnumMap matMap = (EnumMap)stored.get(uuid);
        if (matMap == null) {
            return;
        }
        for (ItemStack[] i : matMap.values()) {
            ItemUtils.giveOrDrop(player, i);
        }
        stored.remove(uuid);
    }

    @Generated
    private ItemUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
