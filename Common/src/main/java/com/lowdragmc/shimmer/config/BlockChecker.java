package com.lowdragmc.shimmer.config;

import java.util.Objects;

import com.lowdragmc.shimmer.ShimmerConstants;
import it.unimi.dsi.fastutil.Pair;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

interface BlockChecker extends Check {
	String getBlockName();

	default Pair<ResourceLocation, Block> block() {
		var blockName = getBlockName();
		Objects.requireNonNull(blockName);
		var blockLocation = ResourceLocation.tryParse(blockName);
		if (blockLocation == null) {
			ShimmerConstants.LOGGER.error("invalid block name " + blockName + " form" + getConfigSource());
			return null;
		}
		if (!BuiltInRegistries.BLOCK.containsKey(blockLocation)) {
			ShimmerConstants.LOGGER.error("can't find block " + blockLocation + " from" + getConfigSource());
			return Pair.of(blockLocation, null);
		}
		return Pair.of(blockLocation, BuiltInRegistries.BLOCK.get(blockLocation));
	}
}