package dev.enco.greatcombat.api.models;

import dev.enco.greatcombat.api.models.IWrappedItem;
import dev.enco.greatcombat.api.models.MetaChecker;
import dev.enco.greatcombat.api.models.PreventionType;
import java.util.EnumSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public interface IPreventableItem {
    @NotNull
    public IWrappedItem wrappedItem();

    @NotNull
    public String translation();

    @NotNull
    public EnumSet<PreventionType> types();

    @NotNull
    public Set<String> handlers();

    @NotNull
    public MetaChecker[] checkedMetas();

    public boolean setMaterialCooldown();
}
