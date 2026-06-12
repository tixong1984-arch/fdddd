package dev.enco.greatcombat.core.utils;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Consumer;
import lombok.Generated;

public final class EnumUtils {
    public static <T extends Enum<T>> EnumSet<T> toEnumSet(List<String> values, Class<T> enumClass, Consumer<String> onError) {
        EnumSet<T> set = EnumSet.noneOf(enumClass);
        for (String value : values) {
            try {
                set.add(Enum.valueOf(enumClass, value));
            }
            catch (IllegalArgumentException e) {
                onError.accept(value);
            }
        }
        return set;
    }

    @Generated
    private EnumUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
