package com.lowdragmc.shimmer;

import com.lowdragmc.shimmer.component.LightComponent;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class ShimmerComponents {
    public static final DataComponentType<LightComponent> LIGHT = register("light", DataComponentType.<LightComponent>builder()
        .persistent(LightComponent.CODEC)
        .networkSynchronized(LightComponent.STREAM_CODEC)
        .build()
    );

    public static void init() {}

    private static <T> DataComponentType<T> register(String path, DataComponentType<T> type) {
        return Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, ResourceLocation.fromNamespaceAndPath(ShimmerConstants.MOD_ID, path), type);
    }
}
