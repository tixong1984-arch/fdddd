package dev.enco.greatcombat.core.regions;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.IRegionManager;
import dev.enco.greatcombat.api.models.IRegionListener;
import lombok.Generated;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@Singleton
public class RegionManager
implements IRegionManager {
    private final JavaPlugin plugin;
    private IRegionListener listener;

    @Override
    public void setListener(@NotNull IRegionListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("IRegionListener cannot be null");
        }
        if (this.listener != null) {
            this.listener.unregisterListener();
        }
        this.listener = listener;
        listener.registerListener(this.plugin);
    }

    @Override
    public IRegionListener getListener() {
        return this.listener;
    }

    @Inject
    @Generated
    public RegionManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }
}
