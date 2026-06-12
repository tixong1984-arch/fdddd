package dev.enco.greatcombat.api.events;

import dev.enco.greatcombat.api.models.IUser;
import lombok.Generated;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CombatDamageEvent
extends Event
implements Cancellable {
    @NotNull
    protected final IUser damager;
    @NotNull
    protected final IUser target;
    @Nullable
    protected EntityDamageEvent.DamageCause cause;
    protected boolean cancelled;

    public CombatDamageEvent(@NotNull IUser damager, @NotNull IUser target) {
        this.damager = damager;
        this.target = target;
    }

    public CombatDamageEvent(@NotNull IUser damager, @NotNull IUser target, @Nullable EntityDamageEvent.DamageCause cause) {
        this.damager = damager;
        this.target = target;
        this.cause = cause;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @NotNull
    @Generated
    public IUser getDamager() {
        return this.damager;
    }

    @NotNull
    @Generated
    public IUser getTarget() {
        return this.target;
    }

    @Nullable
    @Generated
    public EntityDamageEvent.DamageCause getCause() {
        return this.cause;
    }
}
