package dev.enco.greatcombat.core.utils;

import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.TimeFormats;
import lombok.Generated;

public final class Time {
    private static ConfigManager configManager;

    public static String format(int sec) {
        TimeFormats secondsForms = configManager.getSecondsFormats();
        if (sec <= 0) {
            return "1 " + secondsForms.form1();
        }
        int h = sec / 3600;
        int min = sec % 3600 / 60;
        int s = sec % 60;
        StringBuilder sb = new StringBuilder();
        TimeFormats minutesForms = configManager.getMinutesFormats();
        TimeFormats hoursForms = configManager.getHoursFormats();
        Time.appendTimeUnit(sb, h, hoursForms.form1(), hoursForms.form3(), hoursForms.form5());
        Time.appendTimeUnit(sb, min, minutesForms.form1(), minutesForms.form3(), minutesForms.form5());
        Time.appendTimeUnit(sb, s, secondsForms.form1(), secondsForms.form3(), secondsForms.form5());
        return sb.toString().trim();
    }

    private static void appendTimeUnit(StringBuilder sb, int value, String form1, String form3, String form5) {
        if (value > 0) {
            sb.append(String.format("%d %s ", value, Time.getCorrectForm(value, form1, form3, form5)));
        }
    }

    private static String getCorrectForm(int value, String form1, String form3, String form5) {
        int abs = Math.abs(value);
        int r10 = abs % 10;
        int r100 = abs % 100;
        if (r10 == 1 && r100 != 11) {
            return form1;
        }
        if (r10 >= 2 && r10 <= 4 && (r100 < 10 || r100 >= 20)) {
            return form3;
        }
        return form5;
    }

    @Generated
    private Time() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    @Generated
    public static void setConfigManager(ConfigManager configManager) {
        Time.configManager = configManager;
    }
}
