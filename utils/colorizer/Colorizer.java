package dev.enco.greatcombat.core.utils.colorizer;

import dev.enco.greatcombat.core.utils.colorizer.IColorizer;
import dev.enco.greatcombat.core.utils.colorizer.impl.LegacyIColorizer;
import dev.enco.greatcombat.core.utils.colorizer.impl.MinimessageIColorizer;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

public final class Colorizer {
    private static IColorizer colorizer;

    public static void setColorizer(String type) {
        switch (type) {
            case "MINIMESSAGE": {
                colorizer = new MinimessageIColorizer();
                break;
            }
            default: {
                colorizer = new LegacyIColorizer();
            }
        }
    }

    public static String colorize(String message) {
        if (message == null || message.isEmpty()) {
            return message;
        }
        return colorizer.colorize(message);
    }

    public static List<String> colorizeAll(List<String> list) {
        ArrayList<String> colored = new ArrayList<String>();
        for (String str : list) {
            colored.add(Colorizer.colorize(str));
        }
        return colored;
    }

    @Generated
    private Colorizer() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
