package com.wdiscute.starcatcher.io;

import com.wdiscute.starcatcher.io.attachments.DataAttachmentType;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public interface CapabilityHolderExtensions {
    default <T> void setData(DataAttachmentType<T> attachmentType, T data){}

    default <T> T getData(DataAttachmentType<T> attachmentType){
        return null;
    }

    default <T> void removeData(DataAttachmentType<T> attachmentType){}
}
