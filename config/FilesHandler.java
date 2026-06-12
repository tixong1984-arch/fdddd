package dev.enco.greatcombat.core.config;

import dev.enco.greatcombat.core.config.ConfigFile;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;

public final class FilesHandler {
    private static final List<ConfigFile> configFiles = new ArrayList<ConfigFile>();

    public static void reloadAll() {
        for (ConfigFile file : configFiles) {
            file.reload();
        }
    }

    public static void addConfigFile2List(ConfigFile configFile) {
        configFiles.add(configFile);
    }

    public static ConfigFile getConfigFile(String name) {
        return configFiles.stream().filter(configFile -> configFile.getName().equalsIgnoreCase(name)).findFirst().orElseThrow(() -> new NullPointerException(name));
    }

    @Generated
    private FilesHandler() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
