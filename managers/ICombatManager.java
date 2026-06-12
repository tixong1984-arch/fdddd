package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.managers.IManager;
import dev.enco.greatcombat.api.models.IUser;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ICombatManager
extends IManager {
    public void stop();

    public boolean isInCombat(@NotNull UUID var1);

    public void removeFromCombatMap(@NotNull IUser var1);

    @Nullable
    public IUser getUser(@NotNull UUID var1);

    public void startCombat(@NotNull Player var1, @NotNull Player var2);

    public void startSingle(@NotNull Player var1);

    @NotNull
    public IUser getOrCreateUser(@NotNull UUID var1);

    public void stopCombat(@NotNull IUser var1);
}
