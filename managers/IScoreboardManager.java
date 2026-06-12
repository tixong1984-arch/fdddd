package dev.enco.greatcombat.api.managers;

import dev.enco.greatcombat.api.managers.IManager;
import dev.enco.greatcombat.api.models.IUser;
import dev.enco.greatcombat.api.models.ScoreboardProvider;
import org.jetbrains.annotations.NotNull;

public interface IScoreboardManager
extends IManager {
    public void setProvider(@NotNull ScoreboardProvider var1);

    public void setScoreboard(@NotNull IUser var1, @NotNull String var2);

    public void resetScoreboard(@NotNull IUser var1);
}
