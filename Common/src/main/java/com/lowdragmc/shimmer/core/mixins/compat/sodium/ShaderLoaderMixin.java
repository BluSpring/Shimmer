package com.lowdragmc.shimmer.core.mixins.compat.sodium;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.lowdragmc.shimmer.client.light.LightManager;
import com.lowdragmc.shimmer.client.postprocessing.PostProcessing;
import net.caffeinemc.mods.sodium.client.gl.shader.ShaderLoader;
import net.caffeinemc.mods.sodium.client.gl.shader.ShaderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.resources.ResourceLocation;

/**
 * @author KilaBash
 * @date 2022/05/28
 * @implNote ShaderLoaderMixin
 */
@Mixin(ShaderLoader.class)
public abstract class ShaderLoaderMixin {

    @SuppressWarnings("mapping")
    @ModifyExpressionValue(method = "loadShader",
        at = @At(value = "INVOKE",target = "Lnet/caffeinemc/mods/sodium/client/gl/shader/ShaderParser;parseShader(Ljava/lang/String;Lnet/caffeinemc/mods/sodium/client/gl/shader/ShaderConstants;)Ljava/lang/String;"))
    private static String transformShader(String shader, ShaderType type, ResourceLocation name){
        if (name.getPath().contains("block_layer_opaque")){
            if (type == ShaderType.FRAGMENT) {
                shader = PostProcessing.SodiumBloomMRTFSHInjection(shader);
            }
            if (type == ShaderType.VERTEX) {
                shader = LightManager.SodiumVVSHInjection(shader);
            }
        }
        return shader;
    }
}
