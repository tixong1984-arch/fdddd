package dev.enco.greatcombat.core.utils.colorizer.impl;

import dev.enco.greatcombat.core.utils.colorizer.IColorizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LegacyIColorizer
implements IColorizer {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([a-fA-F\\d]{6})");
    private static final char COLOR_CHAR = '\u00a7';

    @Override
    public String colorize(String message) {
        Matcher matcher = HEX_PATTERN.matcher(message);
        StringBuilder builder = new StringBuilder(message.length() + 32);
        while (matcher.find()) {
            String group = matcher.group(1);
            matcher.appendReplacement(builder, "\u00a7x\u00a7" + group.charAt(0) + "\u00a7" + group.charAt(1) + "\u00a7" + group.charAt(2) + "\u00a7" + group.charAt(3) + "\u00a7" + group.charAt(4) + "\u00a7" + group.charAt(5));
        }
        message = matcher.appendTail(builder).toString();
        return this.translateAlternateColorCodes('&', message);
    }

    public String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        int length = b.length - 1;
        for (int i = 0; i < length; ++i) {
            if (b[i] != altColorChar || !LegacyIColorizer.isValidColorCharacter(b[i + 1])) continue;
            b[i++] = 167;
            int n = i;
            b[n] = (char)(b[n] | 0x20);
        }
        return new String(b);
    }

    private static boolean isValidColorCharacter(char c) {
        return c >= '0' && c <= '9' || c >= 'a' && c <= 'f' || c == 'r' || c >= 'k' && c <= 'o' || c == 'x' || c >= 'A' && c <= 'F' || c == 'R' || c >= 'K' && c <= 'O' || c == 'X';
    }
}
