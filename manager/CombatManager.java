package dev.enco.greatcombat.core.manager;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.events.CombatContinueEvent;
import dev.enco.greatcombat.api.events.CombatDamageEvent;
import dev.enco.greatcombat.api.events.CombatEndEvent;
import dev.enco.greatcombat.api.events.CombatJoinEvent;
import dev.enco.greatcombat.api.events.CombatMergeEvent;
import dev.enco.greatcombat.api.events.CombatPreStartEvent;
import dev.enco.greatcombat.api.events.CombatStartEvent;
import dev.enco.greatcombat.api.managers.ICombatManager;
import dev.enco.greatcombat.api.managers.IScoreboardManager;
import dev.enco.greatcombat.api.managers.ITaskManager;
import dev.enco.greatcombat.api.models.IUser;
import dev.enco.greatcombat.api.models.WrappedTask;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.manager.User;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import java.util.UUID;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;

@Singleton
public class CombatManager
implements ICombatManager {
    private final Reference2ObjectMap<UUID, IUser> playersInCombat = new Reference2ObjectOpenHashMap<UUID, IUser>();
    private final PluginManager pm = Bukkit.getPluginManager();
    private final IScoreboardManager scoreboardManager;
    private final ITaskManager taskManager;
    private final ConfigManager configManager;

    @Override
    public void stop() {
        for (IUser user : this.playersInCombat.values()) {
            user.deleteBossbar();
            this.scoreboardManager.resetScoreboard(user);
            WrappedTask<?> runnable = user.getRunnable();
            if (runnable == null) continue;
            runnable.cancel();
        }
        this.playersInCombat.clear();
    }

    @Override
    public boolean isInCombat(@NotNull UUID uuid) {
        return this.playersInCombat.containsKey(uuid);
    }

    @Override
    public void removeFromCombatMap(@NotNull IUser user) {
        this.playersInCombat.remove(user.getPlayerUUID());
    }

    @Override
    public IUser getUser(@NotNull UUID uuid) {
        return (IUser)this.playersInCombat.get(uuid);
    }

    @Override
    public void startCombat(@NotNull Player damager, @NotNull Player target) {
        boolean alreadyOpponents;
        boolean isTargetInCombat;
        if (damager == target) {
            return;
        }
        UUID damagerUUID = damager.getUniqueId();
        UUID targetUUID = target.getUniqueId();
        IUser damagerUser = (IUser)this.playersInCombat.get(damagerUUID);
        IUser targetUser = (IUser)this.playersInCombat.get(targetUUID);
        boolean isDamagerInCombat = damagerUser != null;
        boolean bl = isTargetInCombat = targetUser != null;
        if (!isDamagerInCombat) {
            damagerUser = this.getOrCreateUser(damagerUUID);
        }
        if (!isTargetInCombat) {
            targetUser = this.getOrCreateUser(targetUUID);
        }
        if (!(alreadyOpponents = damagerUser.containsOpponent(targetUser))) {
            CombatPreStartEvent preStartEvent = new CombatPreStartEvent(damager, target);
            this.pm.callEvent((Event)preStartEvent);
            if (preStartEvent.isCancelled()) {
                if (!isDamagerInCombat) {
                    this.removeFromCombatMap(damagerUser);
                }
                if (!isTargetInCombat) {
                    this.removeFromCombatMap(targetUser);
                }
                return;
            }
        }
        CombatDamageEvent event = alreadyOpponents ? new CombatContinueEvent(damagerUser, targetUser) : (!isDamagerInCombat && !isTargetInCombat ? new CombatStartEvent(damagerUser, targetUser) : (isDamagerInCombat && isTargetInCombat ? new CombatMergeEvent(damagerUser, targetUser) : new CombatJoinEvent(damagerUser, targetUser, isTargetInCombat)));
        this.pm.callEvent((Event)event);
    }

    @Override
    public void startSingle(@NotNull Player player) {
        UUID playerUUID = player.getUniqueId();
        IUser user = this.getOrCreateUser(playerUUID);
        user.refresh(System.currentTimeMillis());
    }

    @Override
    @NotNull
    public IUser getOrCreateUser(@NotNull UUID uuid) {
        IUser user = this.getUser(uuid);
        if (user != null) {
            return user;
        }
        Player player = Bukkit.getPlayer((UUID)uuid);
        User u = new User(player, uuid, this, this.taskManager.getEntityScheduler(player), this.configManager, this.scoreboardManager);
        this.playersInCombat.put(uuid, u);
        return u;
    }

    @Override
    public void stopCombat(@NotNull IUser user) {
        this.pm.callEvent((Event)new CombatEndEvent(user));
    }

    @Inject
    @Generated
    public CombatManager(IScoreboardManager scoreboardManager, ITaskManager taskManager, ConfigManager configManager) {
        this.scoreboardManager = scoreboardManager;
        this.taskManager = taskManager;
        this.configManager = configManager;
    }
}
