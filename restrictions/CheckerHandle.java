package dev.enco.greatcombat.core.restrictions;

import dev.enco.greatcombat.api.models.IWrappedItem;
import dev.enco.greatcombat.api.models.MetaChecker;
import dev.enco.greatcombat.core.utils.logger.Logger;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;

public class CheckerHandle
implements MetaChecker {
    private final String name;
    private MetaChecker delegate;

    public void bind(MetaChecker checker) {
        this.delegate = checker;
    }

    @Override
    public boolean matches(@NotNull IWrappedItem first, @NotNull IWrappedItem second) {
        if (this.delegate == null) {
            Logger.warn("Delegating MetaChecker for " + this.name + " is not initialized yet! Make sure, that you initialized it using IMetaManager#registerChecker");
            return false;
        }
        return this.delegate.matches(first, second);
    }

    @Override
    public boolean requiresMeta() {
        return this.delegate.requiresMeta();
    }

    @Generated
    public CheckerHandle(String name) {
        this.name = name;
    }
}
