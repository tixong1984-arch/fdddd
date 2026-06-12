package dev.enco.greatcombat.api.models;

import dev.enco.greatcombat.api.models.IWrappedItem;
import org.jetbrains.annotations.NotNull;

public interface MetaChecker {
    public boolean matches(@NotNull IWrappedItem var1, @NotNull IWrappedItem var2);

    public boolean requiresMeta();
}
