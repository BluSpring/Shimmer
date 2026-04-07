package com.lowdragmc.shimmer.core.mixins;

import java.util.List;

import com.mojang.blaze3d.vertex.VertexFormatElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VertexFormatElement.class)
public interface VertexFormatElementAccessor {
    @Accessor("ELEMENTS")
    static List<VertexFormatElement> getElements() {
        throw new UnsupportedOperationException();
    }
}
