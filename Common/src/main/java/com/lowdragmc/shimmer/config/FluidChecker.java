package com.lowdragmc.shimmer.config;

import java.util.Objects;

import com.lowdragmc.shimmer.ShimmerConstants;
import it.unimi.dsi.fastutil.Pair;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

interface FluidChecker extends Check {

	String getFluidName();

	default Pair<ResourceLocation, Fluid> fluid() {
		var fluidName = getFluidName();
		Objects.requireNonNull(fluidName);
		var fluidLocation = ResourceLocation.tryParse(fluidName);
		if (fluidLocation == null) {
			ShimmerConstants.LOGGER.error("invalid fluid name " + fluidName + " form" + getConfigSource());
			return null;
		}
		if (!BuiltInRegistries.FLUID.containsKey(fluidLocation)) {
			ShimmerConstants.LOGGER.error("can't find fluid " + fluidLocation + " from" + getConfigSource());
			return Pair.of(fluidLocation, null);
		}
		Fluid fluid = BuiltInRegistries.FLUID.get(fluidLocation);
		return Pair.of(fluidLocation, fluid);
	}
}