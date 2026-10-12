package net.neoforged.bus.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fabric port shim: kept as a no-op marker so ported code compiles.
 * Handler methods are wired to Fabric callbacks explicitly instead.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SubscribeEvent
{
}
