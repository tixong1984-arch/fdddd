package dev.enco.greatcombat.api.models;

import dev.enco.greatcombat.api.models.Powerup;
import dev.enco.greatcombat.api.models.PowerupProvider;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum PowerupType {
    FLY,
    GOD,
    VANISH,
    GAMEMODE,
    WALKSPEED;

    @Nullable
    private Powerup powerup;

    public void initialize(@NotNull PowerupProvider serverManager) {
        switch (this.ordinal()) {
            case 0: {
                this.powerup = serverManager.flyPowerup();
                break;
            }
            case 1: {
                this.powerup = serverManager.godPowerup();
                break;
            }
            case 2: {
                this.powerup = serverManager.vanishPowerup();
                break;
            }
            case 3: {
                this.powerup = serverManager.gamemodePowerup();
                break;
            }
            case 4: {
                this.powerup = serverManager.walkspeedPowerup();
            }
        }
    }

    @Nullable
    @Generated
    public Powerup getPowerup() {
        return this.powerup;
    }
}
