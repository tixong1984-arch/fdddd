package dev.enco.greatcombat.core.restrictions.cooldowns;

import dev.enco.greatcombat.api.models.ICooldownItem;
import dev.enco.greatcombat.core.restrictions.CheckerHandle;
import dev.enco.greatcombat.core.restrictions.WrappedItem;
import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import java.util.Set;

public final class CooldownItem
extends Record
implements ICooldownItem {
    private final WrappedItem wrappedItem;
    private final String translation;
    private final CheckerHandle[] checkedMetas;
    private final Set<String> handlers;
    private final int time;
    private final boolean setMaterialCooldown;
    private final Set<String> linkedItems;

    public CooldownItem(WrappedItem wrappedItem, String translation, CheckerHandle[] checkedMetas, Set<String> handlers, int time, boolean setMaterialCooldown, Set<String> linkedItems) {
        this.wrappedItem = wrappedItem;
        this.translation = translation;
        this.checkedMetas = checkedMetas;
        this.handlers = handlers;
        this.time = time;
        this.setMaterialCooldown = setMaterialCooldown;
        this.linkedItems = linkedItems;
    }

    @Override
    public final String toString() {
        return ObjectMethods.bootstrap("toString", new MethodHandle[]{CooldownItem.class, "wrappedItem;translation;checkedMetas;handlers;time;setMaterialCooldown;linkedItems", "wrappedItem", "translation", "checkedMetas", "handlers", "time", "setMaterialCooldown", "linkedItems"}, this);
    }

    @Override
    public final int hashCode() {
        return (int)ObjectMethods.bootstrap("hashCode", new MethodHandle[]{CooldownItem.class, "wrappedItem;translation;checkedMetas;handlers;time;setMaterialCooldown;linkedItems", "wrappedItem", "translation", "checkedMetas", "handlers", "time", "setMaterialCooldown", "linkedItems"}, this);
    }

    @Override
    public final boolean equals(Object o) {
        return (boolean)ObjectMethods.bootstrap("equals", new MethodHandle[]{CooldownItem.class, "wrappedItem;translation;checkedMetas;handlers;time;setMaterialCooldown;linkedItems", "wrappedItem", "translation", "checkedMetas", "handlers", "time", "setMaterialCooldown", "linkedItems"}, this, o);
    }

    @Override
    public WrappedItem wrappedItem() {
        return this.wrappedItem;
    }

    @Override
    public String translation() {
        return this.translation;
    }

    public CheckerHandle[] checkedMetas() {
        return this.checkedMetas;
    }

    @Override
    public Set<String> handlers() {
        return this.handlers;
    }

    @Override
    public int time() {
        return this.time;
    }

    @Override
    public boolean setMaterialCooldown() {
        return this.setMaterialCooldown;
    }

    @Override
    public Set<String> linkedItems() {
        return this.linkedItems;
    }
}
