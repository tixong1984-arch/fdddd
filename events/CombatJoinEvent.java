package dev.enco.greatcombat.api.events;

import dev.enco.greatcombat.api.events.CombatDamageEvent;
import dev.enco.greatcombat.api.models.IUser;
import lombok.Generated;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CombatJoinEvent
extends CombatDamageEvent {
    private static final HandlerList handlers = new HandlerList();
    private final boolean damagerJoiner;

    public CombatJoinEvent(@NotNull IUser damager, @NotNull IUser target, boolean damagerJoiner) {
        super(damager, target);
        this.damagerJoiner = damagerJoiner;
    }

    public CombatJoinEvent(@NotNull IUser damager, @NotNull IUser target, boolean damagerJoiner, @Nullable EntityDamageEvent.DamageCause cause) {
        super(damager, target, cause);
        this.damagerJoiner = damagerJoiner;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @Generated
    public boolean isDamagerJoiner() {
        return this.damagerJoiner;
    }
}
