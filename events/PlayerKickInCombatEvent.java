package dev.enco.greatcombat.api.events;

import dev.enco.greatcombat.api.models.IUser;
import lombok.Generated;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerKickInCombatEvent
extends Event {
    @NotNull
    private static final HandlerList handlers = new HandlerList();
    @NotNull
    private final IUser user;
    @NotNull
    private final String reason;

    public PlayerKickInCombatEvent(@NotNull IUser user, @NotNull String reason) {
        this.user = user;
        this.reason = reason;
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

    @NotNull
    @Generated
    public String getReason() {
        return this.reason;
    }
}
