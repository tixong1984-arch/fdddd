package dev.enco.greatcombat.api;

import dev.enco.greatcombat.api.GreatCombatPlugin;
import org.jetbrains.annotations.NotNull;

public final class GreatCombatProvider {
    private static GreatCombatPlugin plugin = null;

    private GreatCombatProvider() {
        throw new UnsupportedOperationException("GreatCombatProvider cannot be initialized!");
    }

    public static boolean isLoaded() {
        return plugin != null;
    }

    @NotNull
    public static GreatCombatPlugin getPlugin() {
        if (!GreatCombatProvider.isLoaded()) {
            throw new IllegalStateException("GreatCombat isn't loaded yet!");
        }
        return plugin;
    }

    public static void setPlugin(GreatCombatPlugin pl) {
        if (GreatCombatProvider.isLoaded()) {
            throw new IllegalStateException("GreatCombat is already loaded!");
        }
        plugin = pl;
    }
}
