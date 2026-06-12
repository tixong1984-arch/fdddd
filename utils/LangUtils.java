package dev.enco.greatcombat.core.utils;

import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.utils.logger.Logger;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.boomearo.langhelper.LangHelper;
import ru.boomearo.langhelper.versions.LangType;

public final class LangUtils {
    private static boolean USE_LANG_HELPER;
    private static Object manager;
    private static Object type;
    private static Method getMethod;

    public static void setup(ConfigManager configManager) {
        Locale locale = configManager.getLocale();
        FileConfiguration config = configManager.getMainConfig();
        USE_LANG_HELPER = config.getBoolean("use-lang-helper");
        if (!USE_LANG_HELPER) {
            return;
        }
        try {
            String lang = config.getString("helper-lang");
            manager = LangHelper.getInstance().getTranslateManager();
            type = LangType.valueOf((String)config.getString("helper-lang"));
            getMethod = manager.getClass().getMethod("getItemName", ItemStack.class, LangType.class);
            Logger.info(MessageFormat.format(locale.langSuccess(), lang));
        }
        catch (Exception e) {
            Logger.warn(locale.langError() + String.valueOf(e));
            USE_LANG_HELPER = false;
        }
    }

    public static String getTranslation(String translation, ItemStack item) {
        if (!USE_LANG_HELPER) {
            return translation;
        }
        if (translation != null && !translation.isEmpty()) {
            return translation;
        }
        try {
            return (String)getMethod.invoke(manager, item, type);
        }
        catch (Exception ignored) {
            return translation;
        }
    }

    public static void shutdown(boolean bool) {
        if (USE_LANG_HELPER && bool) {
            Bukkit.getPluginManager().disablePlugin((Plugin)LangHelper.getInstance());
        }
    }

    @Generated
    private LangUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
