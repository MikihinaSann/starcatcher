package net.neoforged.neoforge.attachment.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.attachment.AttachmentHandler;
import net.neoforged.neoforge.attachment.AttachmentHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Mixin(Entity.class)
public class EntityAttachmentMixin implements AttachmentHolder
{
    @Unique
    private final Map<ResourceLocation, Object> starcatcher$attachments = new HashMap<>();

    @Override
    public Map<ResourceLocation, Object> starcatcher$attachments()
    {
        return starcatcher$attachments;
    }

    //fabric port: Entity#addAdditionalSaveData/readAdditionalSaveData are abstract;
    //hook the concrete saveWithoutId/load instead (NeoForge hooks the leaf methods)
    @Inject(method = "saveWithoutId", at = @At("RETURN"))
    private void starcatcher$writeAttachments(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir)
    {
        CompoundTag attachments = AttachmentHandler.writeAttachments(this, ((Entity) (Object) this).registryAccess());
        if (!attachments.isEmpty())
            tag.put(AttachmentHandler.nbtKey(), attachments);
    }

    @Inject(method = "load", at = @At("RETURN"))
    private void starcatcher$readAttachments(CompoundTag tag, CallbackInfo ci)
    {
        AttachmentHandler.readAttachments(this, tag, ((Entity) (Object) this).registryAccess());
    }
}
