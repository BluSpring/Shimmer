package com.lowdragmc.shimmer.core.mixins.compat.iris;

import java.io.IOException;
import java.util.Objects;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.lowdragmc.shimmer.Utils;
import com.lowdragmc.shimmer.comp.iris.IrisFrameBufferWrapper;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.irisshaders.iris.gl.framebuffer.GlFramebuffer;
import net.irisshaders.iris.pipeline.programs.ExtendedShader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;

@Mixin(value = ExtendedShader.class, remap = false)
public class ExtendedShaderMixin extends ShaderInstance {
    public ExtendedShaderMixin(ResourceProvider resourceProvider, String name, VertexFormat vertexFormat) throws IOException {
        super(resourceProvider, name, vertexFormat);
    }

    @SuppressWarnings("unused")
    @ModifyReceiver(method = "bindFramebuffer", at = @At(value = "INVOKE", target = "Lnet/irisshaders/iris/gl/framebuffer/GlFramebuffer;bind()V"))
    private GlFramebuffer injectFrameBufferToUseWrapper(GlFramebuffer origin) {
        if (Utils.postTextureId > 0 && Objects.equals(this.getName(), "particles")) {
            return IrisFrameBufferWrapper.from(origin, Utils.postTextureId);
        }
        return origin;
    }
}
