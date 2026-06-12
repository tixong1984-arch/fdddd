package dev.enco.greatcombat.core;

import com.google.inject.AbstractModule;
import dev.enco.greatcombat.api.GreatCombatPlugin;
import dev.enco.greatcombat.api.managers.ICombatManager;
import dev.enco.greatcombat.api.managers.ICooldownManager;
import dev.enco.greatcombat.api.managers.IInteractionManager;
import dev.enco.greatcombat.api.managers.IMetaManager;
import dev.enco.greatcombat.api.managers.IPowerupsManager;
import dev.enco.greatcombat.api.managers.IPreventionManager;
import dev.enco.greatcombat.api.managers.IRegionManager;
import dev.enco.greatcombat.api.managers.IScoreboardManager;
import dev.enco.greatcombat.api.managers.ITaskManager;
import dev.enco.greatcombat.core.GreatCombat;
import dev.enco.greatcombat.core.manager.CombatManager;
import dev.enco.greatcombat.core.powerups.PowerupsManager;
import dev.enco.greatcombat.core.regions.RegionManager;
import dev.enco.greatcombat.core.restrictions.InteractionManager;
import dev.enco.greatcombat.core.restrictions.cooldowns.CooldownManager;
import dev.enco.greatcombat.core.restrictions.meta.MetaManager;
import dev.enco.greatcombat.core.restrictions.prevention.PreventionManager;
import dev.enco.greatcombat.core.scheduler.TaskManager;
import dev.enco.greatcombat.core.scoreboard.ScoreboardManager;
import org.bukkit.plugin.java.JavaPlugin;

public class GreatCombatModule
extends AbstractModule {
    private final JavaPlugin plugin;

    public GreatCombatModule(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    protected void configure() {
        this.bind(JavaPlugin.class).toInstance((Object)this.plugin);
        this.bind(ITaskManager.class).to(TaskManager.class);
        this.bind(ICooldownManager.class).to(CooldownManager.class);
        this.bind(IScoreboardManager.class).to(ScoreboardManager.class);
        this.bind(IPreventionManager.class).to(PreventionManager.class);
        this.bind(IMetaManager.class).to(MetaManager.class);
        this.bind(IPowerupsManager.class).to(PowerupsManager.class);
        this.bind(ICombatManager.class).to(CombatManager.class);
        this.bind(IRegionManager.class).to(RegionManager.class);
        this.bind(GreatCombatPlugin.class).to(GreatCombat.class);
        this.bind(IInteractionManager.class).to(InteractionManager.class);
    }
}
