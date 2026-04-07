package com.lowdragmc.shimmer.core.mixins;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.lowdragmc.shimmer.client.light.ColorPointLight;
import com.lowdragmc.shimmer.client.light.LightManager;
import com.lowdragmc.shimmer.client.postprocessing.PostProcessing;
import com.lowdragmc.shimmer.core.IRenderChunk;
import com.mojang.blaze3d.vertex.VertexSorting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

/**
 * @author KilaBash
 * @date 2022/05/02
 * @implNote RebuildTaskMixin, used to compile and save light info to the chunk.
 */
@Mixin(SectionCompiler.class)
public abstract class SectionCompilerMixin {
    @WrapOperation(method = "compile",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/chunk/RenderChunkRegion;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;",
                    ordinal = 0))
    private BlockState injectCompile(RenderChunkRegion instance, BlockPos pPos, Operation<BlockState> original, @Share("lights") LocalRef<ImmutableList.Builder<ColorPointLight>> lightsRef) {
        BlockState blockstate = original.call(instance, pPos);
        FluidState fluidstate = blockstate.getFluidState();
        if (LightManager.INSTANCE.isBlockHasLight(blockstate.getBlock(), fluidstate)) {
            ColorPointLight light = LightManager.INSTANCE.getBlockStateLight(instance, pPos, blockstate, fluidstate);
            if (light != null) {
                lightsRef.get().add(light);
            }
        }

        PostProcessing.setupBloom(blockstate, fluidstate);
        return blockstate;
    }

    @Inject(method = "compile", at = @At(value = "HEAD"))
    private void injectCompilePre(SectionPos sectionPos, RenderChunkRegion region, VertexSorting vertexSorting, SectionBufferBuilderPack sectionBufferBuilderPack, CallbackInfoReturnable<SectionCompiler.Results> cir, @Share("lights") LocalRef<ImmutableList.Builder<ColorPointLight>> lightsRef) {
        lightsRef.set(ImmutableList.builder());
    }

    @Inject(method = "compile", at = @At(value = "RETURN"))
    private void injectCompilePost(SectionPos sectionPos, RenderChunkRegion region, VertexSorting vertexSorting, SectionBufferBuilderPack sectionBufferBuilderPack, CallbackInfoReturnable<SectionCompiler.Results> cir, @Share("lights") LocalRef<ImmutableList.Builder<ColorPointLight>> lightsRef) {
        if (region.getChunk(sectionPos.x(), sectionPos.z()) instanceof IRenderChunk renderChunk) {
            renderChunk.setShimmerLights(lightsRef.get().build());
        }
        PostProcessing.cleanBloom();
    }
}
