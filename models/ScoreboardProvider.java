package dev.enco.greatcombat.api.models;

import dev.enco.greatcombat.api.models.IUser;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public interface ScoreboardProvider {
    public void setScoreboard(@NotNull IUser var1, @NotNull String var2, @NotNull List<String> var3);

    public void resetScoreboard(@NotNull IUser var1);
}
