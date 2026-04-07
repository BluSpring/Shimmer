package com.lowdragmc.shimmer.neoforge;

import com.lowdragmc.shimmer.ShimmerConstants;
import com.lowdragmc.shimmer.neoforge.client.ClientProxy;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;

@Mod(ShimmerConstants.MOD_ID)
public class ShimmerMod {

    public ShimmerMod(ModContainer container, IEventBus bus) {
        ForgeShimmerConfig.registerConfig(container);
//        context.registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        if (FMLLoader.getDist() == Dist.CLIENT) {
            new ClientProxy(bus);
        } else {
            new CommonProxy(bus);
        }
    }

    public static boolean isRubidiumLoaded() {
        return ModList.get().isLoaded("rubidium");
    }
}
