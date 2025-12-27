package com.wdiscute.starcatcher.tournament;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.io.ExtraComposites;
import com.wdiscute.starcatcher.io.SingleStackContainer;
import com.wdiscute.starcatcher.io.StreamCodec;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.UUIDUtil;
import net.minecraft.util.StringRepresentable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Tournament
{
    public UUID tournamentUUID;
    public String name;
    public Status status;
    public UUID owner;
    public Map<UUID, TournamentPlayerScore> playerScores;
    public TournamentSettings settings;
    public List<SingleStackContainer> lootPool;
    public long lastsUntilEpoch;

    public static final Tournament DEFAULT = new Tournament(
            UUID.randomUUID(),
            "missingno",
            Status.SETUP,
            UUID.randomUUID(),
            new HashMap<>(),
            TournamentSettings.DEFAULT,
            List.of(),
            0);

    public static final Codec<Tournament> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    UUIDUtil.CODEC.fieldOf("tournament_uuid").forGetter(Tournament::getTournamentUUID),
                    Codec.STRING.optionalFieldOf("name", "Unnamed Tournament").forGetter(Tournament::getName),
                    Status.CODEC.fieldOf("status").forGetter(Tournament::getStatus),
                    UUIDUtil.CODEC.fieldOf("owner").forGetter(Tournament::getOwner),
                    Codec.unboundedMap(UUIDUtil.CODEC, TournamentPlayerScore.CODEC).fieldOf("player_scores").forGetter(Tournament::getPlayerScores),
                    TournamentSettings.CODEC.fieldOf("settings").forGetter(Tournament::getSettings),
                    SingleStackContainer.LIST_CODEC.optionalFieldOf("loot_pool", SingleStackContainer.EMPTY_LIST).forGetter(Tournament::getLootPool),
                    Codec.LONG.fieldOf("lastsUntil").forGetter(Tournament::getLastsUntilEpoch)
            ).apply(instance, Tournament::new)
    );

    public static final StreamCodec<Tournament> STREAM_CODEC = ExtraComposites.composite(
            StreamCodec.UUID, Tournament::getTournamentUUID,
            StreamCodec.STRING, Tournament::getName,
            Status.STREAM_CODEC, Tournament::getStatus,
            StreamCodec.UUID, Tournament::getOwner,
            StreamCodec.map(Object2ObjectOpenHashMap::new, StreamCodec.UUID, TournamentPlayerScore.STREAM_CODEC), Tournament::getPlayerScores,
            TournamentSettings.STREAM_CODEC, Tournament::getSettings,
            SingleStackContainer.STREAM_CODEC_LIST, Tournament::getLootPool,
            StreamCodec.LONG, Tournament::getLastsUntilEpoch,
            Tournament::new
    );

    public Tournament(UUID tournamentUUID,
                      String name,
                      Status status,
                      UUID owner,
                      Map<UUID, TournamentPlayerScore> playerScore,
                      TournamentSettings settings,
                      List<SingleStackContainer> pool,
                      long lastsUntil
    )
    {
        this.tournamentUUID = tournamentUUID;
        this.name = name;
        this.status = status;
        this.owner = owner;
        this.playerScores = playerScore;
        this.settings = settings;
        this.lootPool = pool;
        this.lastsUntilEpoch = lastsUntil;
    }

    public UUID getTournamentUUID()
    {
        return tournamentUUID;
    }

    public String getName()
    {
        return name;
    }

    public List<SingleStackContainer> getLootPool()
    {
        return lootPool;
    }

    public Map<UUID, TournamentPlayerScore> getPlayerScores()
    {
        return playerScores;
    }

    public long getLastsUntilEpoch()
    {
        return lastsUntilEpoch;
    }

    public Status getStatus()
    {
        return status;
    }

    public TournamentSettings getSettings()
    {
        return settings;
    }

    public UUID getOwner()
    {
        return owner;
    }

    public enum Status implements StringRepresentable
    {
        SETUP("gui.starcatcher.tournament.status.setup"),
        ACTIVE("gui.starcatcher.tournament.status.active"),
        CANCELLED("gui.starcatcher.tournament.status.cancelled"),
        FINISHED("gui.starcatcher.tournament.status.finished");

        Status(String name)
        {
            this.key = name;
        }

        public String toString()
        {
            return this.key;
        }

        public static final Codec<Status> CODEC = StringRepresentable.fromEnum(Status::values);
        public static final Codec<List<Status>> LIST_CODEC = Status.CODEC.listOf();
        public static final StreamCodec<Status> STREAM_CODEC = StreamCodec.enumCodec(Status.class);
        public static final StreamCodec<List<Status>> LIST_STREAM_CODEC = STREAM_CODEC.list();
        private final String key;

        @Override
        public String getSerializedName()
        {
            return this.key;
        }
    }

}