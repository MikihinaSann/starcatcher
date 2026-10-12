package com.wdiscute.starcatcher.neoforged.neoforge.event.entity.player;

import net.minecraft.world.entity.player.Player;
import com.wdiscute.starcatcher.neoforged.bus.api.Event;

public abstract class PlayerEvent extends Event
{
    private final Player player;

    public PlayerEvent(Player player)
    {
        this.player = player;
    }

    public Player getEntity()
    {
        return player;
    }

    public static class PlayerLoggedInEvent extends PlayerEvent
    {
        public PlayerLoggedInEvent(Player player)
        {
            super(player);
        }
    }

    public static class PlayerLoggedOutEvent extends PlayerEvent
    {
        public PlayerLoggedOutEvent(Player player)
        {
            super(player);
        }
    }

    public static class PlayerRespawnEvent extends PlayerEvent
    {
        private final boolean endConquered;

        public PlayerRespawnEvent(Player player, boolean endConquered)
        {
            super(player);
            this.endConquered = endConquered;
        }

        public boolean isEndConquered()
        {
            return endConquered;
        }
    }

    public static class Clone extends PlayerEvent
    {
        private final Player original;
        private final boolean wasDeath;

        public Clone(Player player, Player original, boolean wasDeath)
        {
            super(player);
            this.original = original;
            this.wasDeath = wasDeath;
        }

        public Player getOriginal()
        {
            return original;
        }

        public boolean isWasDeath()
        {
            return wasDeath;
        }
    }

    public static class PlayerChangedDimensionEvent extends PlayerEvent
    {
        private final net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> from;
        private final net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> to;

        public PlayerChangedDimensionEvent(Player player,
                                           net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> from,
                                           net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> to)
        {
            super(player);
            this.from = from;
            this.to = to;
        }

        public net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> getFrom()
        {
            return from;
        }

        public net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> getTo()
        {
            return to;
        }
    }
}
