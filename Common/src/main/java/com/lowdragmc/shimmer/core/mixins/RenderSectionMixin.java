package com.lowdragmc.shimmer.core.mixins;

import java.util.Collections;
import java.util.List;

import com.lowdragmc.shimmer.client.light.ColorPointLight;
import com.lowdragmc.shimmer.core.IRenderChunk;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;

/**
 * @author KilaBash
 * @date 2022/05/02
 * @implNote RenderChunkMixin, used to compile and save light info to the chunk.
 */
@Mixin(SectionRenderDispatcher.RenderSection.class)
public abstract class RenderSectionMixin implements IRenderChunk {
    List<ColorPointLight> shimmerLights = Collections.emptyList();

    @Override
    public List<ColorPointLight> getShimmerLights() {
        return shimmerLights;
    }

    @Override
    public void setShimmerLights(List<ColorPointLight> lights) {
        shimmerLights = lights;
    }
}
