package dev.enco.greatcombat.core.powerups.impl;

import com.Zrips.CMI.Containers.CMIUser;
import com.Zrips.CMI.PlayerManager;
import dev.enco.greatcombat.api.models.Powerup;
import dev.enco.greatcombat.api.models.PowerupProvider;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.utils.logger.Logger;
import java.text.MessageFormat;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CMI
implements PowerupProvider {
    private PlayerManager playerManager;
    private com.Zrips.CMI.CMI cmi;
    private final ConfigManager configManager;
    private Powerup flyPowerup;
    private Powerup godPowerup;
    private Powerup vanishPowerup;
    private Powerup gamemodePowerup;
    private Powerup walkspeedPowerup;

    public CMI(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public void setup() {
        Locale locale = this.configManager.getLocale();
        Logger.info(MessageFormat.format(locale.serverManagerLoading(), "CMI"));
        long start = System.currentTimeMillis();
        try {
            this.cmi = com.Zrips.CMI.CMI.getInstance();
            this.playerManager = this.cmi.getPlayerManager();
            this.setupFlyPowerup();
            this.setupGodPowerup();
            this.setupGamemodePowerup();
            this.setupVanishPowerup();
            this.setupWalkspeedPowerup();
            Logger.info(MessageFormat.format(locale.serverManagerLoaded(), "CMI") + (System.currentTimeMillis() - start) + " ms.");
        }
        catch (Exception e) {
            Logger.error(locale.serverManagerError() + " CMI " + String.valueOf(e));
        }
    }

    private void setupFlyPowerup() {
        this.flyPowerup = new Powerup(){

            @Override
            public boolean hasPowerup(@NotNull Player player) {
                CMIUser user = CMI.this.playerManager.getUser(player);
                return user.isFlying();
            }

            @Override
            public void disablePowerup(@NotNull Player player) {
                CMIUser user = CMI.this.playerManager.getUser(player);
                user.setFlying(false);
            }
        };
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
                CMIUser user = CMI.this.playerManager.getUser(player);
                return user.isGod();
            }

            @Override
            public void disablePowerup(@NotNull Player player) {
                CMIUser user = CMI.this.playerManager.getUser(player);
                CMI.this.cmi.getNMS().changeGodMode(player, false);
                user.setTgod(0L);
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
                CMIUser user = CMI.this.playerManager.getUser(player);
                return user.isVanished();
            }

            @Override
            public void disablePowerup(@NotNull Player player) {
                CMIUser user = CMI.this.playerManager.getUser(player);
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
        this.gamemodePowerup = new Powerup(){

            @Override
            public boolean hasPowerup(@NotNull Player player) {
                return !player.getGameMode().equals((Object)GameMode.SURVIVAL);
            }

            @Override
            public void disablePowerup(@NotNull Player player) {
                CMIUser user = CMI.this.playerManager.getUser(player);
                user.setGameMode(GameMode.SURVIVAL);
            }
        };
    }

    @Override
    @NotNull
    public Powerup gamemodePowerup() {
        return this.gamemodePowerup;
    }

    private void setupWalkspeedPowerup() {
        this.walkspeedPowerup = new Powerup(){

            @Override
            public boolean hasPowerup(@NotNull Player player) {
                return player.getWalkSpeed() != 0.2f;
            }

            @Override
            public void disablePowerup(@NotNull Player player) {
                player.setWalkSpeed(0.2f);
            }
        };
    }

    @Override
    @NotNull
    public Powerup walkspeedPowerup() {
        return this.walkspeedPowerup;
    }
}
