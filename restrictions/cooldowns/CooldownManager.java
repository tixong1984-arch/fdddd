package dev.enco.greatcombat.core.restrictions.cooldowns;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.Scheduler;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.ICooldownManager;
import dev.enco.greatcombat.api.managers.IMetaManager;
import dev.enco.greatcombat.api.managers.ITaskManager;
import dev.enco.greatcombat.api.models.ICooldownItem;
import dev.enco.greatcombat.api.models.IWrappedItem;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.restrictions.CheckerHandle;
import dev.enco.greatcombat.core.restrictions.WrappedItem;
import dev.enco.greatcombat.core.restrictions.cooldowns.CooldownItem;
import dev.enco.greatcombat.core.utils.ItemUtils;
import dev.enco.greatcombat.core.utils.LangUtils;
import dev.enco.greatcombat.core.utils.colorizer.Colorizer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Singleton
public class CooldownManager
implements ICooldownManager {
    private ImmutableMap<ICooldownItem, Cache<UUID, Long>> itemsCooldowns;
    private final Map<String, ICooldownItem> idsRefs = new HashMap<String, ICooldownItem>();
    private final IMetaManager metaManager;
    private final ITaskManager taskManager;

    @Inject
    public CooldownManager(IMetaManager metaManager, ITaskManager taskManager, ConfigManager configManager) {
        this.metaManager = metaManager;
        this.taskManager = taskManager;
        this.setupCooldownItems(configManager);
    }

    @Override
    public ICooldownItem getCooldownItem(@NotNull ItemStack i) {
        WrappedItem wrapped = WrappedItem.wrap(i);
        return this.getCooldownItem(wrapped);
    }

    @Override
    public ICooldownItem getCooldownItem(@NotNull IWrappedItem wrapped) {
        for (ICooldownItem item : this.itemsCooldowns.keySet()) {
            if (!this.metaManager.isSimilar(item.wrappedItem(), wrapped, item.checkedMetas())) continue;
            return item;
        }
        return null;
    }

    public void setupCooldownItems(ConfigManager configManager) {
        FileConfiguration config = configManager.getMainConfig();
        Locale locale = configManager.getLocale();
        ConfigurationSection section = config.getConfigurationSection("items-cooldowns");
        HashMap<CooldownItem, Cache> temp = new HashMap<CooldownItem, Cache>();
        for (String key : section.getKeys(false)) {
            ConfigurationSection itemSection = section.getConfigurationSection(key);
            HashSet<String> handlers = new HashSet<String>(itemSection.getStringList("handlers"));
            List metaKeys = itemSection.getStringList("checked-meta");
            int size = metaKeys.size();
            CheckerHandle[] handles = new CheckerHandle[size];
            for (int i = 0; i < size; ++i) {
                handles[i] = (CheckerHandle)this.metaManager.getByID((String)metaKeys.get(i));
            }
            int time = itemSection.getInt("time");
            ItemStack itemStack = ItemUtils.decode(itemSection.getString("base64"));
            String translation = Colorizer.colorize(LangUtils.getTranslation(itemSection.getString("translation"), itemStack));
            CooldownItem item = new CooldownItem(WrappedItem.wrap(itemStack), translation, handles, handlers, time, itemSection.getBoolean("set-material-cooldown"), new HashSet<String>(itemSection.getStringList("linked-items")));
            this.idsRefs.put(key, item);
            temp.put(item, Caffeine.newBuilder().expireAfterWrite((long)time, TimeUnit.SECONDS).scheduler(Scheduler.systemScheduler()).removalListener((k, v, cause) -> {
                if (cause == RemovalCause.EXPIRED) {
                    Player player = Bukkit.getPlayer((UUID)((UUID)k));
                    if (player == null || !player.isOnline()) {
                        return;
                    }
                    configManager.getMessages().onCooldownExpired().execute(player, translation);
                }
            }).build());
        }
        this.itemsCooldowns = ImmutableMap.copyOf(temp);
    }

    @Override
    public boolean hasCooldown(@NotNull UUID playerUUID, @NotNull ICooldownItem item) {
        return this.itemsCooldowns.get(item).getIfPresent((Object)playerUUID) != null;
    }

    @Override
    public int getCooldownTime(@NotNull UUID playerUUID, @NotNull ICooldownItem item) {
        long startTime = (Long)this.itemsCooldowns.get(item).getIfPresent((Object)playerUUID);
        long leftTime = System.currentTimeMillis() - startTime;
        long remainingTime = (long)item.time() * 1000L - leftTime;
        return (int)(remainingTime / 1000L);
    }

    @Override
    public void putCooldown(@NotNull UUID playerUUID, @NotNull Player player, @NotNull ICooldownItem item) {
        this.setCooldown(playerUUID, player, item);
        for (String id : item.linkedItems()) {
            this.setCooldown(playerUUID, player, this.idsRefs.get(id));
        }
    }

    private void setCooldown(UUID playerUUID, Player player, ICooldownItem item) {
        this.itemsCooldowns.get(item).put((Object)playerUUID, (Object)System.currentTimeMillis());
        if (item.setMaterialCooldown()) {
            this.taskManager.getGlobalScheduler().runLater(() -> player.setCooldown(item.wrappedItem().itemStack().getType(), item.time() * 20), 1L);
        }
    }

    @Override
    public void clearPlayerCooldowns(@NotNull Player player) {
        for (Map.Entry entry : this.itemsCooldowns.entrySet()) {
            player.setCooldown(((ICooldownItem)entry.getKey()).wrappedItem().itemStack().getType(), 0);
            ((Cache)entry.getValue()).invalidate((Object)player.getUniqueId());
        }
    }
}
