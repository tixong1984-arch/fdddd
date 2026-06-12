package dev.enco.greatcombat.core.scoreboard.impl;

import dev.enco.greatcombat.api.models.IUser;
import dev.enco.greatcombat.api.models.ScoreboardProvider;
import dev.enco.greatcombat.core.scoreboard.fastboard.FastBoard;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class FastBoardProvider
implements ScoreboardProvider {
    private final Reference2ObjectMap<UUID, FastBoard> boards = new Reference2ObjectOpenHashMap<UUID, FastBoard>();

    @Override
    public void setScoreboard(@NotNull IUser user, @NotNull String title, @NotNull List<String> lines) {
        UUID uuid = user.getPlayerUUID();
        FastBoard board = (FastBoard)this.boards.get(uuid);
        if (board == null) {
            board = new FastBoard(user.asPlayer());
            this.boards.put(uuid, board);
        }
        board.updateTitle(title);
        board.updateLines(lines);
    }

    @Override
    public void resetScoreboard(@NotNull IUser user) {
        UUID uuid = user.getPlayerUUID();
        FastBoard board = (FastBoard)this.boards.get(uuid);
        if (board != null) {
            board.delete();
            this.boards.remove(uuid);
        }
    }
}
