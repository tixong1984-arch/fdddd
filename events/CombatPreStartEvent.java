package dev.enco.greatcombat.api.events;

import lombok.Generated;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CombatPreStartEvent
extends Event
implements Cancellable {
    @NotNull
    private final Player damager;
    @NotNull
    private final Player target;
    @NotNull
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Generated
    public CombatPreStartEvent(@NotNull Player damager, @NotNull Player target) {
        if (damager == null) {
            throw new NullPointerException("damager is marked non-null but is null");
        }
        if (target == null) {
            throw new NullPointerException("target is marked non-null but is null");
        }
        this.damager = damager;
        this.target = target;
    }

    @NotNull
    @Generated
    public Player getDamager() {
        return this.damager;
    }

    @NotNull
    @Generated
    public Player getTarget() {
        return this.target;
    }
}
