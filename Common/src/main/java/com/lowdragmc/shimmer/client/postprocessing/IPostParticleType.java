package com.lowdragmc.shimmer.client.postprocessing;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureManager;

/**
 * @author KilaBash
 * @date 2022/05/09
 * @implNote TODO
 */
public interface IPostParticleType extends ParticleRenderType {

    ParticleRenderType getParent();
    PostProcessing getPost();

    @Override
    default @Nullable BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
        return getParent().begin(tesselator, textureManager);
    }
}
