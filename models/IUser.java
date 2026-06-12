package dev.enco.greatcombat.api.models;

import dev.enco.greatcombat.api.models.WrappedTask;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IUser {
    @Nullable
    public Player asPlayer();

    @NotNull
    public UUID getPlayerUUID();

    public void removeFromOpponentsMaps();

    public void startTimer();

    public void refresh(long var1);

    public void deleteBossbar();

    public void addOpponent(@NotNull IUser var1);

    public void removeOpponent(@NotNull IUser var1);

    public void createBossbar();

    public boolean containsOpponent(@NotNull IUser var1);

    public long getRemaining();

    public void updateBoardAndBar(long var1);

    @NotNull
    public Set<IUser> getOpponents();

    @Nullable
    public WrappedTask<?> getRunnable();

    public void setStartPvpTime(long var1);

    @NotNull
    public String getOpponentsFormatted(@NotNull String var1);
}
