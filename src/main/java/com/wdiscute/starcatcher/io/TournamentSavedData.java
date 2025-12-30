package com.wdiscute.starcatcher.io;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wdiscute.starcatcher.Starcatcher;
import com.wdiscute.starcatcher.tournament.Tournament;
import com.wdiscute.starcatcher.tournament.TournamentHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.raid.Raids;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TournamentSavedData extends SavedData
{
    public static final Codec<List<Tournament>> CODEC = Tournament.CODEC.listOf();
    public static final String NAME = "tournaments";

    private List<Tournament> tournaments = new ArrayList<>();

    public TournamentSavedData(List<Tournament> tournaments)
    {
        this.tournaments = tournaments;
    }

    public TournamentSavedData() {}

    public static TournamentSavedData get(ServerLevel level)
    {
        return level.getDataStorage().computeIfAbsent(TournamentSavedData::load, TournamentSavedData::new, NAME);
    }

    public List<Tournament> getTournaments()
    {
        return tournaments;
    }

    public void setTournaments(List<Tournament> tournaments)
    {
        this.tournaments = tournaments;
        setDirty();
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag)
    {
        Tournament wd = TournamentHandler.getTournamentOrNew(UUID.randomUUID());

        wd.owner = UUID.randomUUID();

        CODEC.encodeStart(NbtOps.INSTANCE, tournaments)
                .resultOrPartial(Starcatcher.LOGGER::error)
                .ifPresent(tag -> compoundTag.put(NAME, tag));

        return compoundTag;
    }

    public static TournamentSavedData load(CompoundTag compoundTag)
    {
        Tag tag = compoundTag.get(NAME);

        List<Tournament> tournamentsNew = CODEC.decode(NbtOps.INSTANCE, tag)
                .resultOrPartial(Starcatcher.LOGGER::error)
                .map(Pair::getFirst)
                .orElseGet(ArrayList::new);

        return new TournamentSavedData(tournamentsNew);
    }
}
