package dev.enco.greatcombat.core.scoreboard.impl;

import com.xism4.sternalboard.SternalBoardHandler;
import dev.enco.greatcombat.api.models.IUser;
import dev.enco.greatcombat.api.models.ScoreboardProvider;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class SternalBoardProvider
implements ScoreboardProvider {
    private final Reference2ObjectMap<UUID, SternalBoardHandler> handlers = new Reference2ObjectOpenHashMap<UUID, SternalBoardHandler>();

    @Override
    public void setScoreboard(@NotNull IUser user, @NotNull String title, @NotNull List<String> lines) {
        UUID uuid = user.getPlayerUUID();
        SternalBoardHandler handler = (SternalBoardHandler)this.handlers.get(uuid);
        if (handler == null) {
            handler = new SternalBoardHandler(user.asPlayer());
            this.handlers.put(uuid, handler);
        }
        handler.updateTitle(title);
        handler.updateLines(lines);
    }

    @Override
    public void resetScoreboard(@NotNull IUser user) {
        UUID uuid = user.getPlayerUUID();
        SternalBoardHandler handler = (SternalBoardHandler)this.handlers.get(uuid);
        if (handler != null) {
            handler.delete();
            this.handlers.remove(uuid);
        }
    }
}
