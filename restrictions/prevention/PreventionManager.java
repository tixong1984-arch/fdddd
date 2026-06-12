package dev.enco.greatcombat.core.restrictions.prevention;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.IMetaManager;
import dev.enco.greatcombat.api.managers.IPreventionManager;
import dev.enco.greatcombat.api.models.IPreventableItem;
import dev.enco.greatcombat.api.models.IWrappedItem;
import dev.enco.greatcombat.api.models.PreventionType;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.config.settings.Locale;
import dev.enco.greatcombat.core.restrictions.CheckerHandle;
import dev.enco.greatcombat.core.restrictions.WrappedItem;
import dev.enco.greatcombat.core.restrictions.prevention.PreventableItem;
import dev.enco.greatcombat.core.utils.EnumUtils;
import dev.enco.greatcombat.core.utils.ItemUtils;
import dev.enco.greatcombat.core.utils.LangUtils;
import dev.enco.greatcombat.core.utils.colorizer.Colorizer;
import dev.enco.greatcombat.core.utils.logger.Logger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Singleton
public class PreventionManager
implements IPreventionManager {
    private IPreventableItem[] preventableItems = new IPreventableItem[0];
    private final IMetaManager metaManager;

    @Inject
    public PreventionManager(IMetaManager metaManager, ConfigManager configManager) {
        this.metaManager = metaManager;
        this.load(configManager);
    }

    @Override
    public IPreventableItem getPreventableItem(@NotNull ItemStack itemStack) {
        WrappedItem wrapped = WrappedItem.wrap(itemStack);
        return this.getPreventableItem(wrapped);
    }

    @Override
    public IPreventableItem getPreventableItem(@NotNull IWrappedItem wrapped) {
        for (IPreventableItem item : this.preventableItems) {
            if (!this.metaManager.isSimilar(item.wrappedItem(), wrapped, item.checkedMetas())) continue;
            return item;
        }
        return null;
    }

    public void load(ConfigManager configManager) {
        ConfigurationSection section = configManager.getMainConfig().getConfigurationSection("preventable-items");
        Locale locale = configManager.getLocale();
        ArrayList<PreventableItem> itemsList = new ArrayList<PreventableItem>();
        for (String key : section.getKeys(false)) {
            ConfigurationSection itemSection = section.getConfigurationSection(key);
            HashSet<String> handlers = new HashSet<String>(itemSection.getStringList("handlers"));
            List metaKeys = itemSection.getStringList("checked-meta");
            int size = metaKeys.size();
            CheckerHandle[] handles = new CheckerHandle[size];
            for (int i = 0; i < size; ++i) {
                handles[i] = (CheckerHandle)this.metaManager.getByID((String)metaKeys.get(i));
            }
            EnumSet<PreventionType> types = EnumUtils.toEnumSet(itemSection.getStringList("types"), PreventionType.class, type -> Logger.warn(MessageFormat.format(locale.blockerDoesNotExist(), type)));
            ItemStack item = ItemUtils.decode(itemSection.getString("base64"));
            itemsList.add(new PreventableItem(WrappedItem.wrap(item), Colorizer.colorize(LangUtils.getTranslation(itemSection.getString("translation"), item)), types, handlers, handles, itemSection.getBoolean("set-material-cooldown")));
        }
        this.preventableItems = itemsList.toArray(new PreventableItem[0]);
    }
}
