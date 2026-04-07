package com.lowdragmc.shimmer.fabric.core.mixins.flywheel;

import com.lowdragmc.shimmer.client.postprocessing.PostProcessing;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;

@Mixin(value = LevelRenderer.class , priority = 2000)
public class LevelRenderMixin {
    @Inject(method = "renderSectionLayer", expect = 0,
            at = @At(value = "INVOKE",target = "Lnet/minecraft/client/renderer/ShaderInstance;clear()V",
            shift = At.Shift.BEFORE,by = 1), require = 0)
    private void postRenderChunkLayerFlywheel(RenderType pRenderType, double x, double y, double z, Matrix4f frustrumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
        if (PostProcessing.CHUNK_TYPES.contains(pRenderType)) {
            GL30.glDrawBuffers(GL30.GL_COLOR_ATTACHMENT0);
        }
    }
}
