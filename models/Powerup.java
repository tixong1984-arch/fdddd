package dev.enco.greatcombat.api.models;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Powerup {
    public boolean hasPowerup(@NotNull Player var1);

    public void disablePowerup(@NotNull Player var1);
}
