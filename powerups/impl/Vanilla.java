package dev.enco.greatcombat.core.powerups.impl;

import com.google.inject.Inject;
import dev.enco.greatcombat.api.models.Powerup;
import dev.enco.greatcombat.api.models.PowerupProvider;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.powerups.impl.DefaultPowerups;
import dev.enco.greatcombat.core.utils.logger.Logger;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Vanilla
implements PowerupProvider {
    private final ConfigManager configManager;
    private Powerup flyPowerup;
    private Powerup godPowerup;
    private Powerup vanishPowerup;
    private Powerup gamemodePowerup;
    private Powerup walkspeedPowerup;

    @Inject
    public Vanilla(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public void setup() {
        Logger.info(this.configManager.getLocale().serverManagerLoading().replace("{0}", "Vanilla"));
        this.setupFlyPowerup();
        this.setupGodPowerup();
        this.setupGamemodePowerup();
        this.setupVanishPowerup();
        this.setupWalkspeedPowerup();
    }

    private void setupFlyPowerup() {
        this.flyPowerup = DefaultPowerups.FLY_POWERUP;
    }

    @Override
    @NotNull
    public Powerup flyPowerup() {
        return this.flyPowerup;
    }

    private void setupGodPowerup() {
        this.godPowerup = new Powerup(){

            @Override
            public boolean hasPowerup(@NotNull Player player) {
                return player.isInvulnerable();
            }

            @Override
            public void disablePowerup(@NotNull Player player) {
                player.setInvulnerable(false);
            }
        };
    }

    @Override
    @NotNull
    public Powerup godPowerup() {
        return this.godPowerup;
    }

    private void setupVanishPowerup() {
        this.vanishPowerup = new Powerup(){

            @Override
            public boolean hasPowerup(@NotNull Player player) {
                return player.isInvisible();
            }

            @Override
            public void disablePowerup(@NotNull Player player) {
                player.setInvisible(false);
            }
        };
    }

    @Override
    @NotNull
    public Powerup vanishPowerup() {
        return this.vanishPowerup;
    }

    private void setupGamemodePowerup() {
        this.gamemodePowerup = DefaultPowerups.GM_POWERUP;
    }

    @Override
    @NotNull
    public Powerup gamemodePowerup() {
        return this.gamemodePowerup;
    }

    private void setupWalkspeedPowerup() {
        this.walkspeedPowerup = DefaultPowerups.WALKSPEED_POWERUP;
    }

    @Override
    @NotNull
    public Powerup walkspeedPowerup() {
        return this.walkspeedPowerup;
    }
}
