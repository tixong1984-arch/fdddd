package dev.enco.greatcombat.core.regions;

import dev.enco.greatcombat.api.models.IRegionListener;
import dev.enco.greatcombat.core.regions.impl.LandsListener;
import dev.enco.greatcombat.core.regions.impl.TownyListener;
import dev.enco.greatcombat.core.regions.impl.WorldGuardListener;
import lombok.Generated;

public enum RegionProvider {
    LANDS(LandsListener.class),
    TOWNY(TownyListener.class),
    WORLDGUARD(WorldGuardListener.class);

    private final Class<? extends IRegionListener> clazz;

    private RegionProvider(Class<? extends IRegionListener> clazz) {
        this.clazz = clazz;
    }

    @Generated
    public Class<? extends IRegionListener> getClazz() {
        return this.clazz;
    }
}
