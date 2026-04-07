package com.lowdragmc.shimmer.core.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lowdragmc.shimmer.core.IMainTarget;
import com.mojang.blaze3d.pipeline.RenderTarget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author KilaBash
 * @date 2022/05/03
 * @implNote RenderTargetMixin, fix filterMode settings.
 */
@Mixin(RenderTarget.class)
public abstract class RenderTargetMixin {

    @WrapOperation(method = "createBuffers",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;setFilterMode(IZ)V"))
    private void injectCreateBuffers(RenderTarget instance, int filterMode, boolean force, Operation<Void> original) {
        if (!force)
            original.call(instance, instance.filterMode == 0 ? filterMode : instance.filterMode, force);
        else original.call(instance, filterMode, force);
    }

    @Inject(method = "destroyBuffers", at = @At("TAIL"))
    private void injectDestroyBuffers(CallbackInfo ci) {
        if (this instanceof IMainTarget mainTarget) {
            mainTarget.destroyBloomTextureBuffers();
        }
    }

    @Inject(method = "setFilterMode(IZ)V", at = @At("TAIL"))
    private void injectSetFilterMode(int $$0, boolean force, CallbackInfo ci) {
        if (this instanceof IMainTarget mainTarget) {
            mainTarget.setBloomFilterMode($$0);
        }
    }

    @Inject(method = "createBuffers", at = @At("HEAD"))
    private void injectCreateBuffersHead(int pWidth, int pHeight, boolean pClearError, CallbackInfo ci) {
        if (this instanceof IMainTarget mainTarget) {
            mainTarget.createBuffersHeads(pWidth, pHeight, pClearError);
        }
    }

    @Inject(method = "createBuffers", at = @At("TAIL"))
    private void injectCreateBuffersTail(int pWidth, int pHeight, boolean pClearError, CallbackInfo ci) {
        if (this instanceof IMainTarget mainTarget) {
            mainTarget.createBuffersTail(pWidth, pHeight, pClearError);
        }
    }
}
