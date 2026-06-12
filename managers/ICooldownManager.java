package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.managers.IManager;
import dev.enco.greatcombat.api.models.ICooldownItem;
import dev.enco.greatcombat.api.models.IWrappedItem;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ICooldownManager
extends IManager {
    @Nullable
    public ICooldownItem getCooldownItem(@NotNull ItemStack var1);

    @Nullable
    public ICooldownItem getCooldownItem(@NotNull IWrappedItem var1);

    public boolean hasCooldown(@NotNull UUID var1, @NotNull ICooldownItem var2);

    public int getCooldownTime(@NotNull UUID var1, @NotNull ICooldownItem var2);

    public void putCooldown(@NotNull UUID var1, @NotNull Player var2, @NotNull ICooldownItem var3);

    public void clearPlayerCooldowns(@NotNull Player var1);
}
