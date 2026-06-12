package dev.enco.greatcombat.core.restrictions.meta;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.IMetaManager;
import dev.enco.greatcombat.api.models.IWrappedItem;
import dev.enco.greatcombat.api.models.MetaChecker;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.restrictions.CheckerHandle;
import dev.enco.greatcombat.core.restrictions.DefaultCheckers;
import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

@Singleton
public class MetaManager
implements IMetaManager {
    private final Map<String, CheckerHandle> registry = new HashMap<String, CheckerHandle>();

    @Inject
    public MetaManager(ConfigManager configManager) {
        this.registerDefaults(configManager);
    }

    private void registerDefaults(ConfigManager configManager) {
        for (DefaultCheckers meta : DefaultCheckers.values()) {
            this.registerChecker(meta.name(), meta);
        }
        for (String name : configManager.getCheckers()) {
            this.registerChecker(name, null);
        }
    }

    @Override
    public void registerChecker(@NotNull String name, @NotNull MetaChecker checker) {
        this.getByID(name).bind(checker);
    }

    @Override
    public CheckerHandle getByID(@NotNull String name) {
        return this.registry.computeIfAbsent(name.toUpperCase(), CheckerHandle::new);
    }

    @Override
    public boolean isSimilar(@NotNull IWrappedItem f, @NotNull IWrappedItem s, MetaChecker[] checkedMetas) {
        for (MetaChecker checker : checkedMetas) {
            if (checker.requiresMeta()) {
                if (f.hasMeta() != s.hasMeta()) {
                    return false;
                }
                if (!f.hasMeta()) continue;
            }
            if (checker.matches(f, s)) continue;
            return false;
        }
        return true;
    }
}
