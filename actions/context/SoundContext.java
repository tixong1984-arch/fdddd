package dev.enco.greatcombat.core.actions.context;

import dev.enco.greatcombat.core.actions.context.Context;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.utils.logger.Logger;
import java.text.MessageFormat;
import org.bukkit.Sound;

public record SoundContext(Sound sound, float volume, float pitch) implements Context
{
    public static SoundContext validate(String s, Locale locale) {
        String[] args = s.split(";");
        Sound sound = null;
        try {
            if (args.length < 1) {
                Logger.warn(locale.nullSound());
                return null;
            }
            sound = Sound.valueOf((String)args[0].toUpperCase());
        }
        catch (IllegalArgumentException e) {
            Logger.warn(MessageFormat.format(locale.soundDoesNotExist(), args[0]));
        }
        try {
            float volume = args.length > 1 ? Float.parseFloat(args[1]) : 1.0f;
            float pitch = args.length > 2 ? Float.parseFloat(args[2]) : 1.0f;
            return new SoundContext(sound, volume, pitch);
        }
        catch (NumberFormatException e) {
            Logger.warn(locale.volumeAndPitchError());
            return null;
        }
    }
}
