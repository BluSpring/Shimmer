package com.lowdragmc.shimmer.core.mixins.compat.iris;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.lowdragmc.shimmer.comp.iris.IrisFrameBufferWrapper;
import net.irisshaders.iris.targets.RenderTargets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = RenderTargets.class, remap = false)
public class RenderTargetsMixin {
    @ModifyReturnValue(method = "resizeIfNeeded", at = @At("RETURN"))
    @SuppressWarnings("unused")
    private boolean injectResize(boolean sizeChanged) {
        if (sizeChanged) {
            IrisFrameBufferWrapper.clear();
        }
        return sizeChanged;
    }
}
