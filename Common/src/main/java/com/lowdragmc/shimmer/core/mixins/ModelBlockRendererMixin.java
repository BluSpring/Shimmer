package com.lowdragmc.shimmer.core.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lowdragmc.shimmer.client.postprocessing.PostProcessing;
import com.lowdragmc.shimmer.core.IBakedQuad;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;

/**
 * @author KilaBash
 * @date 2022/05/31
 * @implNote ModelBlockRendererMixin, reglowstone.pngcode uv2 for bloom info
 */
@Mixin(ModelBlockRenderer.class)
public abstract class ModelBlockRendererMixin {
    @WrapOperation(method = "putQuadData", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;putBulkData(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/renderer/block/model/BakedQuad;[FFFFF[IIZ)V"))
    private void injectPutQuadData(VertexConsumer instance, PoseStack.Pose pose, BakedQuad pQuad, float[] brightness, float red, float green, float blue, float alpha, int[] lightmaps, int packedOverlay, boolean readAlpha, Operation<Void> original) {
        if (((IBakedQuad)pQuad).isBloom() || PostProcessing.isBlockBloom()) {
            for (int i = 0; i < lightmaps.length; i++) {
                lightmaps[i] |= 0x1000100; // 0xf000f0 -> 0x1f001f0
            }
        }
        original.call(instance, pose, pQuad, brightness, red, green, blue, alpha, lightmaps, packedOverlay, readAlpha);
    }
}
