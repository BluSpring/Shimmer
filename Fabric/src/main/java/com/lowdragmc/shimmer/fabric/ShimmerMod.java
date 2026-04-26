package com.lowdragmc.shimmer.fabric;

import com.lowdragmc.shimmer.ShimmerComponents;

import net.fabricmc.api.ModInitializer;

/**
 * @author HypherionSA
 * @date 2022/06/09
 */
@SuppressWarnings("unused")
public class ShimmerMod implements ModInitializer {

    @Override
    public void onInitialize() {
        ShimmerComponents.init();
    }
}
