package dev.enco.greatcombat.api.events;

import dev.enco.greatcombat.api.models.IUser;
import lombok.Generated;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CombatEndEvent
extends Event {
    private static final HandlerList handlers = new HandlerList();
    @NotNull
    private final IUser user;

    public CombatEndEvent(@NotNull IUser user) {
        this.user = user;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @NotNull
    @Generated
    public IUser getUser() {
        return this.user;
    }
}
