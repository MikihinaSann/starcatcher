package net.neoforged.neoforge.registries;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.attachment.AttachmentType;

/**
 * Fabric port shim of NeoForgeRegistries. Attachment types live in a small
 * custom registry under the neoforge namespace to keep ids identical.
 */
public class NeoForgeRegistries
{
    public static final ResourceKey<Registry<AttachmentType<?>>> ATTACHMENT_TYPES_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("neoforge", "attachment_types"));

    public static final Registry<AttachmentType<?>> ATTACHMENT_TYPES =
            FabricRegistryBuilder.<AttachmentType<?>>createSimple(ATTACHMENT_TYPES_KEY).buildAndRegister();

    public static final ResourceKey<Registry<com.mojang.serialization.MapCodec<? extends net.neoforged.neoforge.common.loot.IGlobalLootModifier>>> GLOBAL_LOOT_MODIFIER_SERIALIZERS_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("neoforge", "global_loot_modifier_serializers"));

    public static final Registry<com.mojang.serialization.MapCodec<? extends net.neoforged.neoforge.common.loot.IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_SERIALIZERS =
            FabricRegistryBuilder.<com.mojang.serialization.MapCodec<? extends net.neoforged.neoforge.common.loot.IGlobalLootModifier>>createSimple(GLOBAL_LOOT_MODIFIER_SERIALIZERS_KEY).buildAndRegister();

    public static class Keys
    {
        public static final ResourceKey<Registry<AttachmentType<?>>> ATTACHMENT_TYPES = NeoForgeRegistries.ATTACHMENT_TYPES_KEY;
        public static final ResourceKey<Registry<com.mojang.serialization.MapCodec<? extends net.neoforged.neoforge.common.loot.IGlobalLootModifier>>> GLOBAL_LOOT_MODIFIER_SERIALIZERS = NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS_KEY;
    }
}
