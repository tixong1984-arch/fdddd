package dev.enco.greatcombat.core.regions.impl;

import com.google.inject.Inject;
import com.palmergames.bukkit.towny.event.damage.TownyPlayerDamagePlayerEvent;
import dev.enco.greatcombat.api.managers.ICombatManager;
import dev.enco.greatcombat.api.models.IRegionListener;
import dev.enco.greatcombat.core.config.ConfigManager;
import lombok.Generated;
import org.bukkit.event.EventHandler;

public class TownyListener
implements IRegionListener {
    private final ICombatManager combatManager;
    private final ConfigManager configManager;

    @EventHandler
    public void listen(TownyPlayerDamagePlayerEvent event) {
        if (!this.configManager.getRegionWorlds().contains(event.getAttackingPlayer().getWorld().getName())) {
            return;
        }
        if (event.isCancelled()) {
            boolean damagerInCombat = this.combatManager.isInCombat(event.getAttackingPlayer().getUniqueId());
            boolean targetInCombat = this.combatManager.isInCombat(event.getVictimPlayer().getUniqueId());
            if (damagerInCombat && targetInCombat) {
                event.setCancelled(false);
            }
        }
    }

    @Inject
    @Generated
    public TownyListener(ICombatManager combatManager, ConfigManager configManager) {
        this.combatManager = combatManager;
        this.configManager = configManager;
    }
}
