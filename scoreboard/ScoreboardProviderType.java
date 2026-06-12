package dev.enco.greatcombat.core.scoreboard;

import dev.enco.greatcombat.api.models.ScoreboardProvider;
import dev.enco.greatcombat.core.scoreboard.impl.FastBoardProvider;
import dev.enco.greatcombat.core.scoreboard.impl.SternalBoardProvider;
import dev.enco.greatcombat.core.scoreboard.impl.TABProvider;
import lombok.Generated;

public enum ScoreboardProviderType {
    TAB(TABProvider.class),
    STERNAL_BOARD(SternalBoardProvider.class),
    FASTBOARD(FastBoardProvider.class);

    public final Class<? extends ScoreboardProvider> providingClass;

    public ScoreboardProvider getProvider() {
        return this.providingClass.newInstance();
    }

    @Generated
    private ScoreboardProviderType(Class<? extends ScoreboardProvider> providingClass) {
        this.providingClass = providingClass;
    }
}
