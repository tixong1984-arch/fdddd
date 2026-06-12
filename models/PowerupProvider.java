package dev.enco.greatcombat.api.models;

import dev.enco.greatcombat.api.models.Powerup;
import org.jetbrains.annotations.NotNull;

public interface PowerupProvider {
    public void setup();

    @NotNull
    public Powerup flyPowerup();

    @NotNull
    public Powerup godPowerup();

    @NotNull
    public Powerup vanishPowerup();

    @NotNull
    public Powerup gamemodePowerup();

    @NotNull
    public Powerup walkspeedPowerup();
}
