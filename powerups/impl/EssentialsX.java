package dev.enco.greatcombat.core.powerups.impl;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.google.inject.Inject;
import dev.enco.greatcombat.api.models.Powerup;
import dev.enco.greatcombat.api.models.PowerupProvider;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.powerups.impl.DefaultPowerups;
import dev.enco.greatcombat.core.utils.logger.Logger;
import java.text.MessageFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EssentialsX
implements PowerupProvider {
    private final ConfigManager configManager;
    private Essentials essentials;
    private Powerup flyPowerup;
    private Powerup godPowerup;
    private Powerup vanishPowerup;
    private Powerup gamemodePowerup;
    private Powerup walkspeedPowerup;

    @Inject
    public EssentialsX(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public void setup() {
        Locale locale = this.configManager.getLocale();
        long start = System.currentTimeMillis();
        Logger.info(MessageFormat.format(locale.serverManagerLoading(), "EssentialsX"));
        try {
            this.essentials = (Essentials)Bukkit.getServer().getPluginManager().getPlugin("Essentials");
            this.setupFlyPowerup();
            this.setupGodPowerup();
            this.setupGamemodePowerup();
            this.setupVanishPowerup();
            this.setupWalkspeedPowerup();
            Logger.info(MessageFormat.format(locale.serverManagerLoaded(), "EssentialsX") + (System.currentTimeMillis() - start) + " ms.");
        }
        catch (Exception e) {
            Logger.error(locale.serverManagerError() + " EssentialsX " + String.valueOf(e));
        }
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
                User user = EssentialsX.this.essentials.getUser(player);
                return user.isGodModeEnabled();
            }

            @Override
            public void disablePowerup(@NotNull Player player) {
                User user = EssentialsX.this.essentials.getUser(player);
                user.setGodModeEnabled(false);
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
                User user = EssentialsX.this.essentials.getUser(player);
                return user.isVanished();
            }

            @Override
            public void disablePowerup(@NotNull Player player) {
                User user = EssentialsX.this.essentials.getUser(player);
                user.setVanished(false);
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
