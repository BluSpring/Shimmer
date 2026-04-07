package com.lowdragmc.shimmer.core.mixins.compat.iris;

import net.irisshaders.iris.gl.buffer.BuiltShaderStorageInfo;
import net.irisshaders.iris.gl.buffer.ShaderStorageBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = ShaderStorageBuffer.class, remap = false)
public interface ShaderStorageBufferAccessor {
    @Accessor
    BuiltShaderStorageInfo getInfo();

    @Invoker
    void callDestroy();
}
