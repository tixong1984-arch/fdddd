package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.managers.IManager;
import dev.enco.greatcombat.api.models.InteractionHandler;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IInteractionManager
extends IManager {
    public <T extends Event> void registerMapping(@NotNull Class<T> var1, @NotNull InteractionHandler<T> var2);

    public <T extends Event> InteractionHandler<T> newHandler(@NotNull String var1, @NotNull Predicate<T> var2, @NotNull @NotNull Function<T, @NotNull Player> var3, @NotNull Function<T, @Nullable ItemStack> var4);

    @NotNull
    public <T extends Event> Stream<InteractionHandler<T>> getHandlers(@NotNull T var1);

    @Nullable
    public <T extends Event> InteractionHandler<T> getFirstPredicatedHandler(@NotNull T var1);

    public <T extends Event> void handle(@NotNull T var1, @NotNull Consumer<T> var2);

    public void registerDefaults();
}
