package com.wdiscute.starcatcher.mixin;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import com.wdiscute.starcatcher.datagen.backport.ConditionalOps;
import com.wdiscute.starcatcher.datagen.backport.IDatagenConditionsExtension;
import com.wdiscute.starcatcher.datagen.backport.WithConditions;
import net.minecraft.core.Holder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.RegistriesDatapackGenerator;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.mojang.text2speech.Narrator.LOGGER;

@SuppressWarnings("deprecation")
@Mixin(RegistriesDatapackGenerator.class)
public class DatagenConditionsBackportMixin implements IDatagenConditionsExtension {
    @Unique
    public Map<ResourceKey<?>, List<ICondition>> starcatcherForge$conditions;
    @Unique
    private static Map<ResourceKey<?>, List<ICondition>> starcatcherForge$tempConditions;


    @Inject(method = "run", at = @At("HEAD"))
    private void injectConditionMap(CachedOutput output, CallbackInfoReturnable<CompletableFuture<?>> cir){
        starcatcherForge$conditions = getConditionsMap();
        starcatcherForge$tempConditions = starcatcherForge$conditions;
    }

    @Inject(method = "lambda$dumpRegistryCap$5", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/data/registries/RegistriesDatapackGenerator;dumpValue(Ljava/nio/file/Path;Lnet/minecraft/data/CachedOutput;Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Encoder;Ljava/lang/Object;)Ljava/util/concurrent/CompletableFuture;"), cancellable = true)
    private static <E> void wrapWithConditions(PackOutput.PathProvider packoutput$pathprovider, CachedOutput output, DynamicOps<JsonElement> ops, RegistryDataLoader.RegistryData<E> registryData, Holder.Reference<E> reference,
                                               CallbackInfoReturnable<CompletableFuture<?>> cir){
        if (!starcatcherForge$tempConditions.isEmpty()) {
            var conditionalCodec = ConditionalOps.createConditionalCodecWithConditions(registryData.elementCodec());
            var conditionalValue = Optional.of(new WithConditions<>(starcatcherForge$tempConditions.getOrDefault(reference.key(), java.util.List.of()), reference.value()));

           cir.setReturnValue(RegistriesDatapackGenerator.dumpValue(packoutput$pathprovider.json(reference.key().location()), output, ops, conditionalCodec,  conditionalValue));
        }
    }


}
