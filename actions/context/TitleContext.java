package dev.enco.greatcombat.core.actions.context;

import dev.enco.greatcombat.core.actions.context.Context;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.utils.colorizer.Colorizer;

public record TitleContext(String title, String subtitle, int fadeIn, int stayIn, int fadeOut) implements Context
{
    public static TitleContext validate(String s, Locale locale) {
        String[] args = (s = Colorizer.colorize(s)).split(";");
        String title = args.length > 0 ? args[0] : "";
        String subTitle = args.length > 1 ? args[1] : "";
        int fadeIn = args.length > 2 ? Integer.valueOf(args[2]) : 10;
        int stayIn = args.length > 3 ? Integer.valueOf(args[3]) : 70;
        int fadeOut = args.length > 4 ? Integer.valueOf(args[4]) : 20;
        return new TitleContext(title, subTitle, fadeIn, stayIn, fadeOut);
    }
}
