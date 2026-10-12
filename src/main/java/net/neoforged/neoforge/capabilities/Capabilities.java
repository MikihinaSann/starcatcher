package net.neoforged.neoforge.capabilities;

/**
 * Fabric port shim of NeoForge's Capabilities. Only the item-handler surface
 * used by this codebase is provided; behaviour is implemented by
 * {@link ItemHandlerStorage} + {@link RegisterCapabilitiesEvent} backed by
 * Fabric's ItemStorage API.
 */
public class Capabilities
{
    public static class ItemHandler
    {
        public static final BlockCapabilityMark BLOCK = new BlockCapabilityMark();
        public static final EntityCapabilityMark ENTITY = new EntityCapabilityMark();
        public static final ItemCapabilityMark ITEM = new ItemCapabilityMark();

        public static class BlockCapabilityMark
        {
        }

        public static class EntityCapabilityMark
        {
        }

        public static class ItemCapabilityMark
        {
        }
    }
}
