package com.lowdragmc.shimmer.core.mixins.compat.sodium;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder;
import net.caffeinemc.mods.sodium.client.render.chunk.vertex.format.impl.CompactChunkVertex;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = CompactChunkVertex.class, remap = false)
public abstract class CompactChunkVertexMixin {
    @ModifyArg(method = "lambda$getEncoder$0", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/vertex/format/impl/CompactChunkVertex;packLightAndData(III)I"), index = 1)
    private static int injectMaterialForBloom(int origin, @Local(name = "vertex") ChunkVertexEncoder.Vertex vertex) {
        if ((vertex.light & 0x100) != 0) {
            origin |= (0x01 << 4);
        }
        return origin;
    }

    @WrapOperation(method = "lambda$getEncoder$0", at = @At(value = "INVOKE", target = "Lnet/caffeinemc/mods/sodium/client/render/chunk/vertex/format/impl/CompactChunkVertex;encodeLight(I)I"))
    private static int injectLightForBloom(int light, Operation<Integer> original) {
        if ((light & 0x100) != 0) {
            return 15 | 15 << 4;
        }
        return original.call(light);
    }
}
