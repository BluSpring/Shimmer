package com.lowdragmc.shimmer.core.mixins.compat.sodium;

import java.util.Collections;
import java.util.List;

import com.lowdragmc.shimmer.client.light.ColorPointLight;
import com.lowdragmc.shimmer.core.IRenderChunk;
import net.caffeinemc.mods.sodium.client.render.chunk.RenderSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * @author KilaBash
 * @date 2022/05/28
 * @implNote TODO
 */
@Mixin(RenderSection.class)
public abstract class RenderSectionMixin implements IRenderChunk {
    @Unique
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
