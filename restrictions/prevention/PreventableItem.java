package dev.enco.greatcombat.core.restrictions.prevention;

import dev.enco.greatcombat.api.models.IPreventableItem;
import dev.enco.greatcombat.api.models.PreventionType;
import dev.enco.greatcombat.core.restrictions.CheckerHandle;
import dev.enco.greatcombat.core.restrictions.WrappedItem;
import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import java.util.EnumSet;
import java.util.Set;

public final class PreventableItem
extends Record
implements IPreventableItem {
    private final WrappedItem wrappedItem;
    private final String translation;
    private final EnumSet<PreventionType> types;
    private final Set<String> handlers;
    private final CheckerHandle[] checkedMetas;
    private final boolean setMaterialCooldown;

    public PreventableItem(WrappedItem wrappedItem, String translation, EnumSet<PreventionType> types, Set<String> handlers, CheckerHandle[] checkedMetas, boolean setMaterialCooldown) {
        this.wrappedItem = wrappedItem;
        this.translation = translation;
        this.types = types;
        this.handlers = handlers;
        this.checkedMetas = checkedMetas;
        this.setMaterialCooldown = setMaterialCooldown;
    }

    @Override
    public final String toString() {
        return ObjectMethods.bootstrap("toString", new MethodHandle[]{PreventableItem.class, "wrappedItem;translation;types;handlers;checkedMetas;setMaterialCooldown", "wrappedItem", "translation", "types", "handlers", "checkedMetas", "setMaterialCooldown"}, this);
    }

    @Override
    public final int hashCode() {
        return (int)ObjectMethods.bootstrap("hashCode", new MethodHandle[]{PreventableItem.class, "wrappedItem;translation;types;handlers;checkedMetas;setMaterialCooldown", "wrappedItem", "translation", "types", "handlers", "checkedMetas", "setMaterialCooldown"}, this);
    }

    @Override
    public final boolean equals(Object o) {
        return (boolean)ObjectMethods.bootstrap("equals", new MethodHandle[]{PreventableItem.class, "wrappedItem;translation;types;handlers;checkedMetas;setMaterialCooldown", "wrappedItem", "translation", "types", "handlers", "checkedMetas", "setMaterialCooldown"}, this, o);
    }

    @Override
    public WrappedItem wrappedItem() {
        return this.wrappedItem;
    }

    @Override
    public String translation() {
        return this.translation;
    }

    @Override
    public EnumSet<PreventionType> types() {
        return this.types;
    }

    @Override
    public Set<String> handlers() {
        return this.handlers;
    }

    public CheckerHandle[] checkedMetas() {
        return this.checkedMetas;
    }

    @Override
    public boolean setMaterialCooldown() {
        return this.setMaterialCooldown;
    }
}
