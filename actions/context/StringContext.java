package dev.enco.greatcombat.core.actions.context;

import dev.enco.greatcombat.core.actions.context.Context;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.utils.colorizer.Colorizer;

public record StringContext(String string) implements Context
{
    public static StringContext validate(String args, Locale locale) {
        return new StringContext(Colorizer.colorize(args));
    }
}
