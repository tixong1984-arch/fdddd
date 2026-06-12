package dev.enco.greatcombat.core.restrictions;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dev.enco.greatcombat.api.managers.ICombatManager;
import dev.enco.greatcombat.api.managers.ICooldownManager;
import dev.enco.greatcombat.api.managers.IInteractionManager;
import dev.enco.greatcombat.api.managers.IPreventionManager;
import dev.enco.greatcombat.api.models.ICooldownItem;
import dev.enco.greatcombat.api.models.IPreventableItem;
import dev.enco.greatcombat.api.models.InteractionHandler;
import dev.enco.greatcombat.core.config.ConfigManager;
import dev.enco.greatcombat.core.restrictions.WrappedItem;
import dev.enco.greatcombat.core.utils.Time;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

@Singleton
public class InteractionManager
implements IInteractionManager {
    private final JavaPlugin plugin;
    private final ICombatManager combatManager;
    private final ICooldownManager cooldownManager;
    private final IPreventionManager preventionManager;
    private final ConfigManager configManager;
    private final Reference2ObjectMap<Class<? extends Event>, List<InteractionHandler<?>>> handlerMap = new Reference2ObjectOpenHashMap();
    private final Object2ObjectMap<String, InteractionHandler<?>> nameMap = new Object2ObjectOpenHashMap();
    private final ReferenceSet<Class<? extends Event>> registeredListeners = new ReferenceOpenHashSet<Class<? extends Event>>();

    @Override
    public <T extends Event> void registerMapping(@NotNull Class<T> eventClass, @NotNull InteractionHandler<T> handler) {
        ArrayList<InteractionHandler<T>> handlers = (ArrayList<InteractionHandler<T>>)this.handlerMap.get(eventClass);
        if (handlers == null) {
            handlers = new ArrayList<InteractionHandler<T>>();
            this.handlerMap.put(eventClass, handlers);
        }
        handlers.add(handler);
        this.nameMap.put(handler.name(), handler);
        if (!this.registeredListeners.contains(eventClass)) {
            this.registerDynamic(eventClass);
            this.registeredListeners.add(eventClass);
        }
    }

    @Override
    public <T extends Event> InteractionHandler<T> newHandler(final @NotNull String name, final @NotNull Predicate<T> predicate, final @NotNull Function<T, Player> playerExt, final @NotNull Function<T, ItemStack> itemExt) {
        return new InteractionHandler<T>(){

            @Override
            @NotNull
            public String name() {
                return name;
            }

            @Override
            @NotNull
            public Predicate<T> predicate() {
                return predicate;
            }

            @Override
            @NotNull
            public Function<T, Player> playerExtractor() {
                return playerExt;
            }

            @Override
            @NotNull
            public Function<T, ItemStack> itemExtractor() {
                return itemExt;
            }
        };
    }

    private <T extends Event> void registerDynamic(Class<T> eventClass) {
        Bukkit.getPluginManager().registerEvent(eventClass, new Listener(){}, EventPriority.HIGHEST, (listener, event) -> {
            if (eventClass == event.getClass()) {
                this.processEvent(event);
            }
        }, (Plugin)this.plugin);
    }

    private <T extends Event> void processEvent(T event) {
        InteractionHandler<T> h = this.getFirstPredicatedHandler(event);
        if (h == null) {
            return;
        }
        Player player = h.playerExtractor().apply(event);
        if (!this.combatManager.isInCombat(player.getUniqueId())) {
            return;
        }
        ItemStack itemStack = h.itemExtractor().apply(event);
        if (itemStack == null) {
            return;
        }
        boolean cancelled = this.runCoreChecks(player, itemStack, h);
        if (cancelled && event instanceof Cancellable) {
            Cancellable c = (Cancellable)event;
            c.setCancelled(true);
        }
    }

    private boolean runCoreChecks(Player player, ItemStack is, InteractionHandler<?> handler) {
        IPreventableItem preventionItem;
        WrappedItem wrapped = WrappedItem.wrap(is);
        ICooldownItem cooldownItem = this.cooldownManager.getCooldownItem(wrapped);
        if (cooldownItem != null && cooldownItem.handlers().contains(handler.name())) {
            if (player.hasPermission("greatcombat.cooldowns.bypass")) {
                return false;
            }
            if (this.cooldownManager.hasCooldown(player.getUniqueId(), cooldownItem)) {
                int time = this.cooldownManager.getCooldownTime(player.getUniqueId(), cooldownItem);
                this.configManager.getMessages().onItemCooldown().execute(player, Time.format(time), cooldownItem.translation());
                return true;
            }
            this.cooldownManager.putCooldown(player.getUniqueId(), player, cooldownItem);
        }
        if ((preventionItem = this.preventionManager.getPreventableItem(wrapped)) != null && preventionItem.handlers().contains(handler.name())) {
            if (player.hasPermission("greatcombat.prevention.bypass")) {
                return false;
            }
            this.configManager.getMessages().onInteract().execute(player, preventionItem.translation());
            return true;
        }
        return false;
    }

    @Override
    @NotNull
    public <T extends Event> Stream<InteractionHandler<T>> getHandlers(@NotNull T event) {
        return this.cast((List)this.handlerMap.get(event.getClass()));
    }

    @Override
    public <T extends Event> InteractionHandler<T> getFirstPredicatedHandler(@NotNull T event) {
        return this.getHandlers(event).filter(h -> h.predicate().test(event)).findFirst().orElse(null);
    }

    @Override
    public <T extends Event> void handle(@NotNull T event, @NotNull Consumer<T> consumer) {
        consumer.accept(event);
    }

    private <T extends Event> Stream<InteractionHandler<T>> cast(List<InteractionHandler<?>> uncasted) {
        return uncasted.stream().map(handler -> handler);
    }

    @Override
    public void registerDefaults() {
        this.registerMapping(PlayerItemConsumeEvent.class, this.newHandler("CONSUME", e -> true, PlayerEvent::getPlayer, PlayerItemConsumeEvent::getItem));
        this.registerMapping(PlayerInteractEvent.class, this.newHandler("RIGHT_CLICK_AIR", e -> e.getAction() == Action.RIGHT_CLICK_AIR, PlayerEvent::getPlayer, PlayerInteractEvent::getItem));
        this.registerMapping(PlayerInteractEvent.class, this.newHandler("RIGHT_CLICK_BLOCK", e -> e.getAction() == Action.RIGHT_CLICK_BLOCK, PlayerEvent::getPlayer, PlayerInteractEvent::getItem));
        this.registerMapping(PlayerInteractEvent.class, this.newHandler("LEFT_CLICK_AIR", e -> e.getAction() == Action.LEFT_CLICK_AIR, PlayerEvent::getPlayer, PlayerInteractEvent::getItem));
        this.registerMapping(PlayerInteractEvent.class, this.newHandler("LEFT_CLICK_BLOCK", e -> e.getAction() == Action.LEFT_CLICK_BLOCK, PlayerEvent::getPlayer, PlayerInteractEvent::getItem));
        this.registerMapping(BlockBreakEvent.class, this.newHandler("BLOCK_BREAK", e -> true, BlockBreakEvent::getPlayer, e -> e.getPlayer().getInventory().getItemInMainHand()));
        this.registerMapping(EntityResurrectEvent.class, this.newHandler("RESURRECT_MAINHAND", e -> e.getEntity() instanceof Player, e -> (Player)e.getEntity(), e -> ((Player)e.getEntity()).getInventory().getItemInMainHand()));
        this.registerMapping(EntityResurrectEvent.class, this.newHandler("RESURRECT_OFFHAND", e -> e.getEntity() instanceof Player, e -> (Player)e.getEntity(), e -> ((Player)e.getEntity()).getInventory().getItemInOffHand()));
        this.registerMapping(EntityShootBowEvent.class, this.newHandler("BOW_SHOOT", e -> e.getEntity() instanceof Player, e -> (Player)e.getEntity(), EntityShootBowEvent::getBow));
        this.registerMapping(ProjectileLaunchEvent.class, this.newHandler("PROJECTILE_LAUNCH", e -> e.getEntity().getShooter() instanceof Player, e -> (Player)e.getEntity().getShooter(), e -> ((Player)e.getEntity().getShooter()).getInventory().getItemInMainHand()));
        this.registerMapping(EntityDamageByEntityEvent.class, this.newHandler("PLAYER_HIT_ENTITY", e -> e.getDamager() instanceof Player, e -> (Player)e.getDamager(), e -> ((Player)e.getDamager()).getInventory().getItemInMainHand()));
        this.registerMapping(EntityDamageByEntityEvent.class, this.newHandler("PLAYER_HIT_PLAYER", e -> e.getDamager() instanceof Player && e.getEntity() instanceof Player, e -> (Player)e.getDamager(), e -> ((Player)e.getDamager()).getInventory().getItemInMainHand()));
    }

    @Inject
    @Generated
    public InteractionManager(JavaPlugin plugin, ICombatManager combatManager, ICooldownManager cooldownManager, IPreventionManager preventionManager, ConfigManager configManager) {
        this.plugin = plugin;
        this.combatManager = combatManager;
        this.cooldownManager = cooldownManager;
        this.preventionManager = preventionManager;
        this.configManager = configManager;
    }
}
