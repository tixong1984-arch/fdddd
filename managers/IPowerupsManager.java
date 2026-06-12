package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.managers.IManager;
import dev.enco.greatcombat.api.models.PowerupProvider;
import dev.enco.greatcombat.api.models.PowerupType;
import java.util.EnumSet;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface IPowerupsManager
extends IManager {
    public void setPowerupProvider(@NotNull PowerupProvider var1);

    public boolean hasPowerups(@NotNull Player var1, @NotNull EnumSet<PowerupType> var2);

    public void disablePowerups(@NotNull Player var1, @NotNull EnumSet<PowerupType> var2);
}
