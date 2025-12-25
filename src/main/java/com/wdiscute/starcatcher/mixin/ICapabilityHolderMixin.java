package com.wdiscute.starcatcher.mixin;

import com.wdiscute.starcatcher.io.CapabilityHolderExtensions;
import com.wdiscute.starcatcher.io.ModDataAttachments;
import com.wdiscute.starcatcher.io.attachments.DataAttachmentType;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ICapabilityProvider.class, remap = false)
public interface ICapabilityHolderMixin extends CapabilityHolderExtensions {

    @Override
    default  <T> void setData(DataAttachmentType<T> attachmentType, T data) {
        ModDataAttachments.set(((ICapabilityProvider) (Object) this), attachmentType, data);
    }

    @Override
    default  <T> T getData(DataAttachmentType<T> attachmentType) {
        return ModDataAttachments.get(((ICapabilityProvider) (Object) this), attachmentType);
    }

    @Override
    default  <T> void removeData(DataAttachmentType<T> attachmentType) {
        ModDataAttachments.remove(((ICapabilityProvider) (Object) this), attachmentType);
    }
}
