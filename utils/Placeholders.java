package dev.enco.greatcombat.core.utils;

import dev.enco.greatcombat.core.utils.colorizer.Colorizer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import lombok.Generated;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

public final class Placeholders {
    private static final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    private static final DecimalFormat df = new DecimalFormat("#.#", symbols);
    private static boolean usingPapi;

    public static String replaceInBoard(Player player, String s) {
        return Placeholders.replaceInMessage(player, s, player.getName(), df.format(player.getHealth()), player.getPing());
    }

    public static String replaceInMessage(Player player, String s, Object ... replacement) {
        return Placeholders.replace(player, Placeholders.parseLocals(s, replacement));
    }

    public static String replace(Player player, String s) {
        if (usingPapi && PlaceholderAPI.containsPlaceholders((String)s)) {
            return Colorizer.colorize(PlaceholderAPI.setPlaceholders((Player)player, (String)s));
        }
        return s;
    }

    public static String parseLocals(String text, Object ... replacement) {
        if (text == null || replacement == null || replacement.length == 0) {
            return text;
        }
        int length = text.length();
        int rLength = replacement.length;
        if (length < 3) {
            return text;
        }
        int argIndex = 0;
        StringBuilder result = new StringBuilder(length + 16);
        for (int i = 0; i < length; ++i) {
            char c = text.charAt(i);
            if (c == '{') {
                int start = i + 1;
                int end = Placeholders.findClosing(text, start);
                if (end == -1) {
                    result.append(c);
                    continue;
                }
                int index = Placeholders.resolveIndex(text, start, end, argIndex);
                if (index < rLength) {
                    result.append(replacement[index]);
                    if (start == end) {
                        ++argIndex;
                    }
                } else {
                    result.append(text, i, end + 1);
                }
                i = end;
                continue;
            }
            result.append(c);
        }
        return result.toString();
    }

    private static int findClosing(String text, int from) {
        for (int i = from; i < text.length(); ++i) {
            if (text.charAt(i) != '}') continue;
            return i;
        }
        return -1;
    }

    private static int resolveIndex(String text, int start, int end, int fallback) {
        if (start == end) {
            return fallback;
        }
        int value = 0;
        for (int i = start; i < end; ++i) {
            char c = text.charAt(i);
            if (c < '0' || c > '9') {
                return fallback;
            }
            value = value * 10 + (c - 48);
        }
        return value;
    }

    @Generated
    private Placeholders() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Generated
    public static void setUsingPapi(boolean usingPapi) {
        Placeholders.usingPapi = usingPapi;
    }
}
