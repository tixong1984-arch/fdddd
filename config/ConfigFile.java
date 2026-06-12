package dev.enco.greatcombat.core.config;

import dev.enco.greatcombat.core.GreatCombat;
import dev.enco.greatcombat.core.utils.logger.Logger;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Generated;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigFile {
    private final JavaPlugin plugin = JavaPlugin.getPlugin(GreatCombat.class);
    private FileConfiguration fileConfiguration;
    private File file;
    private final String name;
    private final File folder;
    private final String filePath;
    private static final Map<String, Integer> versions = Map.of("config", 6, "logger", 2, "messages", 2, "scoreboard", 2);
    private static final Set<String> IGNORED = Set.of("preventable-items", "items-cooldowns");

    public ConfigFile(String name, File folder, String lang, boolean replace) {
        this.folder = folder;
        this.name = name;
        this.filePath = "resources-" + lang + "/" + name + ".yml";
        this.file = new File(folder, name + ".yml");
        if (replace || !this.file.exists()) {
            try (InputStream is = this.plugin.getResource(this.filePath);){
                Files.copy(is, this.file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            catch (Exception e) {
                Logger.error("Unable to create config file " + name + ".yml: " + e.getMessage());
            }
        }
        this.fileConfiguration = YamlConfiguration.loadConfiguration((File)this.file);
        this.update();
    }

    public void reload() {
        if (this.fileConfiguration == null) {
            this.file = new File(this.folder, this.name + ".yml");
        }
        this.fileConfiguration = YamlConfiguration.loadConfiguration((File)this.file);
        InputStreamReader inputStreamReader = new InputStreamReader(this.plugin.getResource(this.filePath), StandardCharsets.UTF_8);
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration((Reader)inputStreamReader);
        this.fileConfiguration.setDefaults((Configuration)configuration);
    }

    public void update() {
        Integer ver = this.fileConfiguration.getInt("config-version");
        int latest = versions.get(this.name);
        if (ver == null || ver < latest) {
            List list;
            InputStream def = this.plugin.getResource(this.filePath);
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration((Reader)new InputStreamReader(def, StandardCharsets.UTF_8));
            for (String key : defConfig.getKeys(true)) {
                if (IGNORED.contains(key.split("\\.")[0]) || this.fileConfiguration.contains(key)) continue;
                Object val = defConfig.get(key);
                this.fileConfiguration.set(key, val);
            }
            if (this.name.equals("config") && (ver == null || ver < 4) && this.fileConfiguration.isList("commands.commands") && (list = this.fileConfiguration.getList("commands.commands")) != null && !list.isEmpty()) {
                ArrayList newList = new ArrayList();
                for (Object obj : list) {
                    if (!(obj instanceof String)) continue;
                    String cmd = (String)obj;
                    HashMap map = new HashMap();
                    map.put(cmd, new ArrayList());
                    newList.add(map);
                }
                this.fileConfiguration.set("commands.commands", newList);
            }
            if (this.name.equals("scoreboard") && ver == 1) {
                this.fileConfiguration.set("opponent", (Object)this.fileConfiguration.getString("opponent", "&f{player} &c{health}\u2764 &9{ping}\u21c4").replace("{player}", "{0}").replace("{health}", "{1}").replace("{ping}", "{2}"));
            }
            this.fileConfiguration.set("config-version", (Object)latest);
            this.fileConfiguration.save(this.file);
            this.reload();
            def.close();
        }
    }

    public FileConfiguration get() {
        return this.fileConfiguration;
    }

    @Generated
    public JavaPlugin getPlugin() {
        return this.plugin;
    }

    @Generated
    public FileConfiguration getFileConfiguration() {
        return this.fileConfiguration;
    }

    @Generated
    public File getFile() {
        return this.file;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public File getFolder() {
        return this.folder;
    }

    @Generated
    public String getFilePath() {
        return this.filePath;
    }
}
