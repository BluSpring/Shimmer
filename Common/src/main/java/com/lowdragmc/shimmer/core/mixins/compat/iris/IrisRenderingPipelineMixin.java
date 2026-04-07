package com.lowdragmc.shimmer.core.mixins.compat.iris;

import com.lowdragmc.shimmer.comp.iris.IrisFrameBufferWrapper;
import net.irisshaders.iris.pipeline.IrisRenderingPipeline;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = IrisRenderingPipeline.class, remap = false)
public class IrisRenderingPipelineMixin {
    @Inject(method = "destroy",at = @At("RETURN"))
    private void injectDestroy(CallbackInfo ci){
        IrisFrameBufferWrapper.clear();
    }
}
