package dev.enco.greatcombat.core.actions.context;

import dev.enco.greatcombat.core.actions.context.Context;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.utils.logger.Logger;
import java.text.MessageFormat;
import org.bukkit.Material;

public record MaterialContext(Material material) implements Context
{
    public static MaterialContext validate(String s, Locale locale) {
        String[] args = s.split(";");
        try {
            if (args.length >= 1) {
                return new MaterialContext(Material.valueOf((String)args[0].toUpperCase()));
            }
            Logger.warn(locale.nullMaterial());
        }
        catch (IllegalArgumentException e) {
            Logger.warn(MessageFormat.format(locale.materialError(), args[0]));
        }
        return null;
    }
}
