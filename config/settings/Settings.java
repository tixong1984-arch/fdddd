package dev.enco.greatcombat.core.config.settings;

import com.google.common.collect.ImmutableSet;
import java.util.EnumSet;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerTeleportEvent;

public record Settings(int combatTime, EnumSet<PlayerTeleportEvent.TeleportCause> allowedTpCause, ImmutableSet<String> ignoredWorlds, boolean killOnLeave, boolean killOnKick, ImmutableSet<String> kickMessages, long tickInterval, long minTime, ImmutableSet<EntityType> ignoredProjectile) {
}
