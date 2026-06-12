package dev.enco.greatcombat.core.powerups;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.IPowerupsManager;
import dev.enco.greatcombat.api.models.PowerupProvider;
import dev.enco.greatcombat.api.models.PowerupType;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.powerups.impl.CMI;
import dev.enco.greatcombat.core.powerups.impl.EssentialsX;
import dev.enco.greatcombat.core.powerups.impl.Vanilla;
import java.util.EnumSet;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PowerupsManager
implements IPowerupsManager {
    private final ConfigManager configManager;
    private PowerupProvider provider;

    @Inject
    public PowerupsManager(ConfigManager configManager) {
        this.configManager = configManager;
        this.setServerManager(configManager.getServerManager());
    }

    @Override
    public void setPowerupProvider(@NotNull PowerupProvider provider) {
        this.provider = provider;
        provider.setup();
        for (PowerupType type : PowerupType.values()) {
            type.initialize(provider);
        }
    }

    public void setServerManager(String manager) {
        PluginManager pm = Bukkit.getPluginManager();
        this.setPowerupProvider(switch (manager) {
            case "CMI" -> new CMI(this.configManager);
            case "Essentials" -> new EssentialsX(this.configManager);
            default -> new Vanilla(this.configManager);
        });
    }

    @Override
    public boolean hasPowerups(@NotNull Player player, @NotNull EnumSet<PowerupType> checks) {
        if (player.hasPermission("greatcombat.powerups.bypass")) {
            return false;
        }
        for (PowerupType check : checks) {
            if (!check.getPowerup().hasPowerup(player)) continue;
            return true;
        }
        return false;
    }

    @Override
    public void disablePowerups(@NotNull Player player, @NotNull EnumSet<PowerupType> checks) {
        if (player.hasPermission("greatcombat.powerups.bypass")) {
            return;
        }
        for (PowerupType check : checks) {
            check.getPowerup().disablePowerup(player);
        }
    }

    @Generated
    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    @Generated
    public PowerupProvider getProvider() {
        return this.provider;
    }
}
