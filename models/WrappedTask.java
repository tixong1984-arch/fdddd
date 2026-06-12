package dev.enco.greatcombat.api.models;

import org.jetbrains.annotations.NotNull;

public interface WrappedTask<T> {
    public void cancel();

    @NotNull
    public T getRunnable();
}
