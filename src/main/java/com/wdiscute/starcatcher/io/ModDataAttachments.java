package com.wdiscute.starcatcher.io;

public class ModDataAttachments
{
    /*private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(
            NeoForgeRegistries.ATTACHMENT_TYPES, Starcatcher.MOD_ID);


    public static final Supplier<AttachmentType<FishingBobAttachment>> FISHING_BOB = ATTACHMENT_TYPES.register(
            "fishing_bob", () -> AttachmentType.builder(() -> new FishingBobAttachment(""))
                    .sync(FishingBobAttachment.STREAM_CODEC)
                    .build()
    );


    public static final Supplier<AttachmentType<FishingGuideAttachment>> FISHING_GUIDE = ATTACHMENT_TYPES.register(
            "fishing_guide", () -> AttachmentType.builder(FishingGuideAttachment::createDefault)
                    .serialize(FishingGuideAttachment.CODEC)
                    .sync(FishingGuideAttachment.STREAM_CODEC)
                    .copyOnDeath()
                    .build()
    );


    @Deprecated // use FISHING_GUIDE attachment!!!
    public static final Supplier<AttachmentType<Boolean>> RECEIVED_GUIDE = ATTACHMENT_TYPES.register(
            "received_guide", () -> AttachmentType.builder(() -> false)
                    .serialize(Codec.BOOL)
                    .sync(ByteBufCodecs.BOOL)
                    .build()
    );

    @Deprecated // use FISHING_GUIDE attachment!!!
    public static final Supplier<AttachmentType<List<LegacyFishCaughtCounter>>> FISHES_CAUGHT = ATTACHMENT_TYPES.register(
            "fishes_caught", () ->
                    AttachmentType.builder(() -> List.<LegacyFishCaughtCounter>of())
                            .serialize(LegacyFishCaughtCounter.LIST_CODEC)
                            .sync(LegacyFishCaughtCounter.LIST_STREAM_CODEC)
                            .copyOnDeath()
                            .build()
    );

    @Deprecated  // use FISHING_GUIDE attachment!!!
    public static final Supplier<AttachmentType<List<ResourceLocation>>> TROPHIES_CAUGHT = ATTACHMENT_TYPES.register(
            "trophies_caught", () ->
                    AttachmentType.builder(() -> List.<ResourceLocation>of())
                            .serialize(ResourceLocation.CODEC.listOf())
                            .sync(ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()))
                            .copyOnDeath()
                            .build()
    );

    @Deprecated // use FISHING_GUIDE attachment!!!
    public static final Supplier<AttachmentType<List<ResourceLocation>>> FISHES_NOTIFICATION = ATTACHMENT_TYPES.register(
            "fishes_notification", () ->
                    AttachmentType.builder(() -> List.<ResourceLocation>of())
                            .serialize(ResourceLocation.CODEC.listOf())
                            .sync(ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()))
                            .copyOnDeath()
                            .build()
    );


    public static final Supplier<AttachmentType<SingleStackContainer>> BOBBER_SKIN = ATTACHMENT_TYPES.register(
            "bobber_skin", () ->
                    AttachmentType.builder(() -> SingleStackContainer.EMPTY)
                            .serialize(SingleStackContainer.CODEC)
                            .sync(SingleStackContainer.STREAM_CODEC)
                            .build()
    );


    // sets the value to default
    public static <T> T remove(Entity holder, Supplier<AttachmentType<T>> attachmentType)
    {
        return holder.removeData(attachmentType);
    }

    // sets the value to default
    public static <T> T remove(Entity holder, AttachmentType<T> attachmentType)
    {
        return holder.removeData(attachmentType);
    }

    public static <T> T set(Entity holder, Supplier<AttachmentType<T>> attachmentType, T data)
    {
        return holder.setData(attachmentType, data);
    }

    public static <T> T set(Entity holder, AttachmentType<T> attachmentType, T data)
    {
        return holder.setData(attachmentType, data);
    }

    public static <T> T get(Entity holder, Supplier<AttachmentType<T>> attachmentType)
    {
        return holder.getData(attachmentType);
    }

    public static <T> T get(Entity holder, AttachmentType<T> attachmentType)
    {
        return holder.getData(attachmentType);
    }

    public static void register(IEventBus eventBus)
    {
        ATTACHMENT_TYPES.register(eventBus);
    }*/

}
