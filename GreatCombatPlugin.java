package dev.enco.greatcombat.api;

import dev.enco.greatcombat.api.managers.IManager;
import org.jetbrains.annotations.NotNull;

public interface GreatCombatPlugin {
    @NotNull
    public <T extends IManager> T getManager(@NotNull Class<T> var1);
}
