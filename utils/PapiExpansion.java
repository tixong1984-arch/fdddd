package dev.enco.greatcombat.core.utils;

import com.google.inject.Inject;
import dev.enco.greatcombat.api.managers.ICombatManager;
import dev.enco.greatcombat.api.models.IUser;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Expansion;
import dev.enco.greatcombat.core.utils.Time;
import java.util.Arrays;
import java.util.UUID;
import lombok.Generated;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PapiExpansion
extends PlaceholderExpansion {
    private final ICombatManager combatManager;
    private final ConfigManager configManager;

    @NotNull
    public String getIdentifier() {
        return "greatcombat";
    }

    @NotNull
    public String getAuthor() {
        return "Encourager";
    }

    @NotNull
    public String getVersion() {
        return "1.9";
    }

    public boolean persist() {
        return true;
    }

    public String onPlaceholderRequest(Player player, @NotNull String params) {
        Expansion expansion = this.configManager.getExpansion();
        String[] args = params.split("_");
        IUser user = this.combatManager.getUser(player.getUniqueId());
        UUID uuid = player.getUniqueId();
        try {
            if (args[0].equals("player")) {
                String p = Bukkit.getPlayer((String)args[1]);
                uuid = p.getUniqueId();
                user = this.combatManager.getUser(p.getUniqueId());
                args = Arrays.copyOfRange(args, 2, args.length);
            }
            switch (args[0]) {
                case "time": {
                    int sec = (int)(user.getRemaining() / 1000L);
                    if (args.length == 1) {
                        return String.valueOf(sec);
                    }
                    if (args[1].equals("formatted")) {
                        return Time.format(sec);
                    }
                }
                case "in": {
                    boolean bool = this.combatManager.isInCombat(uuid);
                    if (args.length == 1) {
                        return String.valueOf(bool);
                    }
                    if (args[1].equals("formatted")) {
                        return bool ? expansion.boolTrue() : expansion.boolFalse();
                    }
                }
                case "opponents": {
                    if (args.length == 1) {
                        return user.getOpponentsFormatted(expansion.delimiter());
                    }
                    if (!args[1].equals("contains")) break;
                    IUser opponent = this.combatManager.getUser(Bukkit.getPlayer((String)args[2]).getUniqueId());
                    boolean bool = user.containsOpponent(opponent);
                    if (args.length == 3) {
                        return String.valueOf(bool);
                    }
                    if (!args[3].equals("formatted")) break;
                    return bool ? expansion.boolTrue() : expansion.boolFalse();
                }
            }
            return expansion.error();
        }
        catch (Exception e) {
            return expansion.error();
        }
    }

    @Inject
    @Generated
    public PapiExpansion(ICombatManager combatManager, ConfigManager configManager) {
        this.combatManager = combatManager;
        this.configManager = configManager;
    }
}
