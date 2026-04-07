package com.lowdragmc.shimmer.core.mixins.compat.sodium;

import com.lowdragmc.shimmer.core.IBakedQuad;
import com.lowdragmc.shimmer.core.IQuadViewImpl;
import net.caffeinemc.mods.sodium.client.render.frapi.mesh.QuadViewImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(QuadViewImpl.class)
public abstract class QuadViewImplMixin implements IQuadViewImpl, IBakedQuad {

    private boolean isBloom;

    @Inject(method = "lightmap", at = @At(value = "HEAD"), cancellable = true, remap = false)
    public void injectBaked(int vertexIndex, CallbackInfoReturnable<Integer> cir) {
        if (isBloom) {
            cir.setReturnValue(0x1000100);
        }
    }

    public boolean isBloom() {
        return isBloom;
    }

    public void setBloom(boolean isBloom) {
        this.isBloom = isBloom;
    }
}

