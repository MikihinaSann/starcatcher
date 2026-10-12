package net.neoforged.neoforge.client.model;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Fabric port shim of NeoForge's BakedModelWrapper: delegates all BakedModel
 * calls to the wrapped original.
 */
public class BakedModelWrapper<T extends BakedModel> implements BakedModel
{
    protected final T originalModel;

    public BakedModelWrapper(T originalModel)
    {
        this.originalModel = originalModel;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource rand)
    {
        return originalModel.getQuads(state, direction, rand);
    }

    @Override
    public boolean useAmbientOcclusion()
    {
        return originalModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d()
    {
        return originalModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight()
    {
        return originalModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer()
    {
        return originalModel.isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon()
    {
        return originalModel.getParticleIcon();
    }

    public TextureAtlasSprite getParticleIcon(ModelData data)
    {
        return getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms()
    {
        return originalModel.getTransforms();
    }

    @Override
    public ItemOverrides getOverrides()
    {
        return originalModel.getOverrides();
    }
}
