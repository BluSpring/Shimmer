package com.lowdragmc.shimmer.core.mixins.compat.sodium;

import com.llamalad7.mixinextras.sugar.Local;
import com.lowdragmc.shimmer.client.postprocessing.PostProcessing;
import com.lowdragmc.shimmer.core.IBakedQuad;
import com.lowdragmc.shimmer.core.IQuadViewImpl;
import net.caffeinemc.mods.sodium.client.render.frapi.mesh.MutableQuadViewImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.block.model.BakedQuad;

@Mixin(MutableQuadViewImpl.class)
public abstract class MutableQuadViewImplMixin {

    @Inject(method = {
        "fromVanilla(Lnet/minecraft/client/renderer/block/model/BakedQuad;Lnet/fabricmc/fabric/api/renderer/v1/material/RenderMaterial;Lnet/minecraft/core/Direction;)Lnet/caffeinemc/mods/sodium/client/render/frapi/mesh/MutableQuadViewImpl;"
    },
        at = @At(value = "HEAD"))
    private void injectBaked(CallbackInfoReturnable<MutableQuadViewImpl> cir, @Local(argsOnly = true) BakedQuad quad) {
        ((IQuadViewImpl) this).setBloom(((IBakedQuad)quad).isBloom() || PostProcessing.isBlockBloom());
    }

}

