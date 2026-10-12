package com.wdiscute.starcatcher.neoforged.fml.common;

import com.wdiscute.starcatcher.neoforged.api.distmarker.Dist;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fabric port shim: marker annotation. Handlers are wired manually.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EventBusSubscriber
{
    String modid() default "";

    Dist[] value() default {};

    Bus bus() default Bus.FORGE;

    enum Bus
    {
        FORGE,
        MOD
    }
}
