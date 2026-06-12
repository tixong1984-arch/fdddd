package dev.enco.greatcombat.core.utils;

import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.utils.logger.Logger;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Consumer;
import lombok.Generated;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class UpdateUtils {
    public static void check(Locale locale, Consumer<String> consumer) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://raw.githubusercontent.com/Enc0urager/GreatCombat/master/VERSION").openStream()));){
            consumer.accept(reader.readLine().trim());
        }
        catch (IOException e) {
            Logger.warn(locale.errorUpdates() + String.valueOf(e));
        }
    }

    public static void update(JavaPlugin plugin, String ver, CommandSender sender) {
        String currentJarName = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getName();
        String downloadUrl = "https://github.com/Enc0urager/GreatCombat/releases/download/" + ver + "/GreatCombat-" + ver + ".jar";
        File updateFolder = new File("plugins");
        File targetFile = new File(updateFolder, currentJarName);
        UpdateUtils.downloadFile(downloadUrl, targetFile, sender);
    }

    private static void downloadFile(String fileURL, File targetFile, CommandSender sender) {
        URL url = new URL(fileURL);
        URLConnection connection = url.openConnection();
        int fileSize = connection.getContentLength();
        try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
             FileOutputStream out = new FileOutputStream(targetFile);){
            int bytesRead;
            byte[] data = new byte[1024];
            int totalBytesRead = 0;
            int lastPercentage = 0;
            while ((bytesRead = in.read(data, 0, 1024)) != -1) {
                out.write(data, 0, bytesRead);
                int progressPercentage = (int)((double)(totalBytesRead += bytesRead) / (double)fileSize * 100.0);
                if (progressPercentage < lastPercentage + 10) continue;
                lastPercentage = progressPercentage;
                int downloadedKB = totalBytesRead / 1024;
                int fullSizeKB = fileSize / 1024;
                sender.sendMessage(downloadedKB + "/" + fullSizeKB + "KB (" + progressPercentage + "%)");
            }
        }
    }

    @Generated
    private UpdateUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
