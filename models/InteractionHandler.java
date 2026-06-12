package dev.enco.greatcombat.api.models;

import java.util.function.Function;
import java.util.function.Predicate;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface InteractionHandler<T extends Event> {
    @NotNull
    public String name();

    @NotNull
    public Predicate<T> predicate();

    @NotNull
    public @NotNull Function<T, @NotNull Player> playerExtractor();

    public @NotNull Function<T, @Nullable ItemStack> itemExtractor();
}
