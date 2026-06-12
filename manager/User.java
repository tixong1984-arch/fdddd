package dev.enco.greatcombat.core.manager;

import dev.enco.greatcombat.api.events.CombatTickEvent;
import dev.enco.greatcombat.api.managers.ICombatManager;
import dev.enco.greatcombat.api.managers.IScoreboardManager;
import dev.enco.greatcombat.api.models.IScheduler;
import dev.enco.greatcombat.api.models.IUser;
import dev.enco.greatcombat.api.models.WrappedTask;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Bossbar;
import dev.enco.greatcombat.core.config.settings.Settings;
import dev.enco.greatcombat.core.utils.Time;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.UUID;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

public class User
implements IUser {
    private final Player player;
    private ObjectSet<IUser> opponents = new ObjectOpenHashSet<IUser>();
    private long startPvpTime;
    private final UUID playerUUID;
    private BossBar bossBar;
    private final ICombatManager combatManager;
    private WrappedTask<?> runnable;
    private static final PluginManager pm = Bukkit.getPluginManager();
    private final IScheduler scheduler;
    private final ConfigManager configManager;
    private final IScoreboardManager scoreboardManager;

    @Override
    @NotNull
    public String getOpponentsFormatted(@NotNull String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (IUser u : this.opponents) {
            if (!sb.isEmpty()) {
                sb.append(delimiter);
            }
            sb.append(u.asPlayer().getName());
        }
        return sb.toString();
    }

    @Override
    public Player asPlayer() {
        return this.player;
    }

    @Override
    public void removeFromOpponentsMaps() {
        for (IUser user : this.opponents) {
            user.removeOpponent(this);
        }
    }

    @Override
    public void startTimer() {
        this.createBossbar();
        Settings settings = this.configManager.getSettings();
        this.runnable = this.scheduler.runRepeating(() -> pm.callEvent((Event)new CombatTickEvent(this)), 20L, settings.tickInterval());
    }

    @Override
    public void refresh(long start) {
        this.startPvpTime = start;
        if (this.runnable != null) {
            this.runnable.cancel();
        }
        this.deleteBossbar();
        this.startTimer();
    }

    @Override
    public void deleteBossbar() {
        if (this.bossBar != null) {
            this.bossBar.removePlayer(this.asPlayer());
            this.bossBar = null;
        }
    }

    @Override
    public void addOpponent(@NotNull IUser opponent) {
        this.opponents.add(opponent);
    }

    @Override
    public void removeOpponent(@NotNull IUser opponent) {
        this.opponents.remove(opponent);
    }

    @Override
    public void createBossbar() {
        Bossbar barSettings = this.configManager.getBossbar();
        Settings settings = this.configManager.getSettings();
        String time = Time.format(settings.combatTime());
        this.scoreboardManager.setScoreboard(this, time);
        if (this.bossBar == null && barSettings.enable()) {
            this.bossBar = Bukkit.createBossBar((String)barSettings.title().replace("{time}", time), (BarColor)barSettings.color(), (BarStyle)barSettings.style(), (BarFlag[])new BarFlag[0]);
            this.bossBar.setProgress(1.0);
            this.bossBar.addPlayer(this.asPlayer());
            this.bossBar.setVisible(true);
        }
    }

    @Override
    public boolean containsOpponent(@NotNull IUser user) {
        return this.opponents.contains(user);
    }

    @Override
    public long getRemaining() {
        long leftTime = System.currentTimeMillis() - this.startPvpTime;
        Settings settings = this.configManager.getSettings();
        long totalTime = (long)settings.combatTime() * 1000L;
        return totalTime - leftTime;
    }

    @Override
    public void updateBoardAndBar(long remainingTime) {
        String time = Time.format((int)remainingTime / 1000);
        this.scoreboardManager.setScoreboard(this, time);
        if (this.bossBar != null) {
            Bossbar barSettings = this.configManager.getBossbar();
            Settings settings = this.configManager.getSettings();
            this.bossBar.setTitle(barSettings.title().replace("{time}", time));
            if (barSettings.progress()) {
                double progress = (double)remainingTime / (double)(settings.combatTime() * 1000);
                this.bossBar.setProgress(progress);
            }
        }
    }

    public boolean equals(Object other) {
        if (!(other instanceof IUser)) {
            return false;
        }
        IUser user = (IUser)other;
        return user.getPlayerUUID().equals(this.getPlayerUUID());
    }

    public int hashCode() {
        return this.playerUUID.hashCode();
    }

    @Generated
    public User(Player player, UUID playerUUID, ICombatManager combatManager, IScheduler scheduler, ConfigManager configManager, IScoreboardManager scoreboardManager) {
        this.player = player;
        this.playerUUID = playerUUID;
        this.combatManager = combatManager;
        this.scheduler = scheduler;
        this.configManager = configManager;
        this.scoreboardManager = scoreboardManager;
    }

    @Generated
    public Player getPlayer() {
        return this.player;
    }

    @Generated
    public ObjectSet<IUser> getOpponents() {
        return this.opponents;
    }

    @Generated
    public long getStartPvpTime() {
        return this.startPvpTime;
    }

    @Override
    @Generated
    public UUID getPlayerUUID() {
        return this.playerUUID;
    }

    @Generated
    public BossBar getBossBar() {
        return this.bossBar;
    }

    @Generated
    public ICombatManager getCombatManager() {
        return this.combatManager;
    }

    @Override
    @Generated
    public WrappedTask<?> getRunnable() {
        return this.runnable;
    }

    @Generated
    public IScheduler getScheduler() {
        return this.scheduler;
    }

    @Generated
    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    @Generated
    public IScoreboardManager getScoreboardManager() {
        return this.scoreboardManager;
    }

    @Generated
    public void setOpponents(ObjectSet<IUser> opponents) {
        this.opponents = opponents;
    }

    @Override
    @Generated
    public void setStartPvpTime(long startPvpTime) {
        this.startPvpTime = startPvpTime;
    }

    @Generated
    public void setBossBar(BossBar bossBar) {
        this.bossBar = bossBar;
    }

    @Generated
    public void setRunnable(WrappedTask<?> runnable) {
        this.runnable = runnable;
    }

    @Generated
    public String toString() {
        return "User(player=" + String.valueOf(this.getPlayer()) + ", opponents=" + String.valueOf(this.getOpponents()) + ", startPvpTime=" + this.getStartPvpTime() + ", playerUUID=" + String.valueOf(this.getPlayerUUID()) + ", bossBar=" + String.valueOf(this.getBossBar()) + ", combatManager=" + String.valueOf(this.getCombatManager()) + ", runnable=" + String.valueOf(this.getRunnable()) + ", scheduler=" + String.valueOf(this.getScheduler()) + ", configManager=" + String.valueOf(this.getConfigManager()) + ", scoreboardManager=" + String.valueOf(this.getScoreboardManager()) + ")";
    }
}
