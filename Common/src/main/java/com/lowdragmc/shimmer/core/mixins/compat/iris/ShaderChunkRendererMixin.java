package com.lowdragmc.shimmer.core.mixins.compat.iris;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.lowdragmc.shimmer.comp.iris.GBufferMainRenderTarget;
import net.caffeinemc.mods.sodium.client.render.chunk.ShaderChunkRenderer;
import net.irisshaders.iris.gl.framebuffer.GlFramebuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ShaderChunkRenderer.class, remap = false, priority = 1050)
public class ShaderChunkRendererMixin {

    @TargetHandler(mixin = "net.irisshaders.iris.compat.sodium.mixin.MixinShaderChunkRenderer", name = "redirectIrisProgram", prefix = "redirect")
    @ModifyReceiver(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/irisshaders/iris/gl/framebuffer/GlFramebuffer;bind()V"))
    @SuppressWarnings("unused")
    private GlFramebuffer recordBindedGbufferFrameBuffer(GlFramebuffer origin) {
        GBufferMainRenderTarget.lastGBufferUsedFrameBuffer = origin;
        return origin;
    }

}
