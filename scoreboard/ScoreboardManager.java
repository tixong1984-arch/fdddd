package dev.enco.greatcombat.core.scoreboard;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.IScoreboardManager;
import dev.enco.greatcombat.api.models.IUser;
import dev.enco.greatcombat.api.models.ScoreboardProvider;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.config.settings.Scoreboard;
import dev.enco.greatcombat.core.scoreboard.ScoreboardProviderType;
import dev.enco.greatcombat.core.utils.Placeholders;
import dev.enco.greatcombat.core.utils.logger.Logger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

@Singleton
public class ScoreboardManager
implements IScoreboardManager {
    private final ConfigManager configManager;
    private ScoreboardProvider provider;

    @Inject
    public ScoreboardManager(ConfigManager configManager) {
        this.configManager = configManager;
        this.setProvider(configManager.getMainConfig().getString("scoreboard-manager"));
    }

    public void setProvider(String s) {
        Locale locale = this.configManager.getLocale();
        try {
            ScoreboardProviderType type = ScoreboardProviderType.valueOf(s.toUpperCase());
            this.setProvider(type);
            Logger.info(locale.sbProvider().replace("{0}", s));
        }
        catch (IllegalArgumentException e) {
            Logger.warn(MessageFormat.format(locale.sbError(), s));
            this.setProvider(ScoreboardProviderType.FASTBOARD);
        }
    }

    @Override
    public void setProvider(@NotNull ScoreboardProvider scoreboardProvider) {
        this.provider = scoreboardProvider;
    }

    public void setProvider(ScoreboardProviderType type) {
        this.setProvider(type.getProvider());
    }

    @Override
    public void setScoreboard(@NotNull IUser user, @NotNull String time) {
        Scoreboard boardSettings = this.configManager.getScoreboard();
        if (boardSettings.enable()) {
            this.provider.setScoreboard(user, boardSettings.title(), this.getLines(user, time));
        }
    }

    @Override
    public void resetScoreboard(@NotNull IUser user) {
        Scoreboard boardSettings = this.configManager.getScoreboard();
        if (boardSettings.enable()) {
            this.provider.resetScoreboard(user);
        }
    }

    private List<String> getLines(IUser user, String time) {
        Scoreboard boardSettings = this.configManager.getScoreboard();
        ArrayList<String> replaced = new ArrayList<String>();
        for (String line : boardSettings.lines()) {
            if (line.contains("{opponents}")) {
                replaced.addAll(this.getOpponents(user));
                continue;
            }
            replaced.add(Placeholders.replace(user.asPlayer(), line.replace("{time}", time)));
        }
        return replaced;
    }

    private List<String> getOpponents(IUser user) {
        Scoreboard boardSettings = this.configManager.getScoreboard();
        ArrayList<String> opponentsList = new ArrayList<String>();
        Set<IUser> opponents = user.getOpponents();
        if (opponents.isEmpty()) {
            opponentsList.add(boardSettings.empty());
        }
        for (IUser opponent : opponents) {
            opponentsList.add(Placeholders.replaceInBoard(opponent.asPlayer(), boardSettings.opponent()));
        }
        return opponentsList;
    }
}
