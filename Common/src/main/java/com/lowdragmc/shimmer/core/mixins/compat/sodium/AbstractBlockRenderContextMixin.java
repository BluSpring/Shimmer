package com.lowdragmc.shimmer.core.mixins.compat.sodium;

import com.lowdragmc.shimmer.client.postprocessing.PostProcessing;
import com.lowdragmc.shimmer.core.IBakedQuad;

import net.caffeinemc.mods.sodium.client.model.light.LightMode;
import net.caffeinemc.mods.sodium.client.model.light.data.QuadLightData;
import net.caffeinemc.mods.sodium.client.render.frapi.mesh.MutableQuadViewImpl;
import net.caffeinemc.mods.sodium.client.render.frapi.render.AbstractBlockRenderContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.fabric.api.renderer.v1.material.ShadeMode;

/**
 * @author KilaBash
 * @date 2022/05/31
 * @implNote ModelBlockRendererMixin, reglowstone.pngcode uv2 for bloom info
 */
@Mixin(value = AbstractBlockRenderContext.class)
public abstract class AbstractBlockRenderContextMixin {

    @Shadow
    @Final
    protected QuadLightData quadLightData;

    @Inject(method = "shadeQuad", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/model/light/LightPipeline;calculate(Lnet/caffeinemc/mods/sodium/client/model/quad/ModelQuadView;Lnet/minecraft/core/BlockPos;Lnet/caffeinemc/mods/sodium/client/model/light/data/QuadLightData;Lnet/minecraft/core/Direction;Lnet/minecraft/core/Direction;ZZ)V", shift = At.Shift.AFTER))
    private void reCalculateBloomLight(MutableQuadViewImpl quad, LightMode lightMode, boolean emissive, ShadeMode shadeMode, CallbackInfo ci) {
        if ((quad instanceof IBakedQuad bloomQuad && bloomQuad.isBloom()) || PostProcessing.isBlockBloom()){
            int[] lm = this.quadLightData.lm;
            for (int index = 0; index < lm.length; index++) {
                lm[index] = lm[index] | 0x10000100;
            }
        }
    }
}
