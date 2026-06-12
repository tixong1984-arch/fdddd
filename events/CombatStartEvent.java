package dev.enco.greatcombat.api.events;

import dev.enco.greatcombat.api.events.CombatDamageEvent;
import dev.enco.greatcombat.api.models.IUser;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CombatStartEvent
extends CombatDamageEvent {
    @NotNull
    private static final HandlerList handlers = new HandlerList();

    public CombatStartEvent(@NotNull IUser damager, @NotNull IUser target) {
        super(damager, target);
    }

    public CombatStartEvent(@NotNull IUser damager, @NotNull IUser target, @Nullable EntityDamageEvent.DamageCause cause) {
        super(damager, target, cause);
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }
}
