package dev.enco.greatcombat.core.commands.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.core.commands.Subcommand;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.utils.ItemUtils;
import java.util.List;
import lombok.Generated;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
public class CopySubcommand
implements Subcommand {
    private final ConfigManager configManager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player pl = (Player)sender;
            ItemStack item = pl.getInventory().getItemInMainHand();
            Locale locale = this.configManager.getLocale();
            if (item == null) {
                pl.sendMessage(locale.emptyItem());
                return true;
            }
            Component component = Component.text((String)locale.click2Copy()).clickEvent(ClickEvent.copyToClipboard((String)ItemUtils.encode(item)));
            pl.sendMessage(component);
        }
        return true;
    }

    @Override
    @Nullable
    public List<String> onTab() {
        return List.of();
    }

    @Inject
    @Generated
    public CopySubcommand(ConfigManager configManager) {
        this.configManager = configManager;
    }
}
