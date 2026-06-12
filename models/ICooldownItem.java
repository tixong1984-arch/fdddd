package dev.enco.greatcombat.api.models;

import dev.enco.greatcombat.api.models.IWrappedItem;
import dev.enco.greatcombat.api.models.MetaChecker;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public interface ICooldownItem {
    @NotNull
    public IWrappedItem wrappedItem();

    @NotNull
    public String translation();

    @NotNull
    public MetaChecker[] checkedMetas();

    @NotNull
    public Set<String> handlers();

    public int time();

    public boolean setMaterialCooldown();

    @NotNull
    public Set<String> linkedItems();
}
