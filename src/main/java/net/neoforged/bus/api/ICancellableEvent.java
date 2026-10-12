package net.neoforged.bus.api;

public interface ICancellableEvent
{
    boolean isCanceled();

    void setCanceled(boolean canceled);

    default void setCanceled()
    {
        setCanceled(true);
    }
}
