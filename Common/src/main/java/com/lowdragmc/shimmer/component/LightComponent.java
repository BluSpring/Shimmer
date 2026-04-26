package com.lowdragmc.shimmer.component;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.FastColor;

public record LightComponent(int color, float radius) {
    public static final Codec<LightComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.withAlternative(
            Codec.INT,
            Codec.withAlternative(
                Codec.INT.listOf(3, 4)
                    .xmap(e -> e.size() == 3 ? FastColor.ARGB32.color(255, e.get(0), e.get(1), e.get(2)) : FastColor.ARGB32.color(e.get(0), e.get(1), e.get(2), e.get(3)),
                        color -> List.of(FastColor.ARGB32.alpha(color), FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color))
                    ),
                Codec.FLOAT.listOf(3, 4)
                    .xmap(e -> e.size() == 3 ? FastColor.ARGB32.colorFromFloat(1f, e.get(0), e.get(1), e.get(2)) : FastColor.ARGB32.colorFromFloat(e.get(0), e.get(1), e.get(2), e.get(3)),
                        color -> List.of(FastColor.ARGB32.alpha(color) / 255f, FastColor.ARGB32.red(color) / 255f, FastColor.ARGB32.green(color) / 255f, FastColor.ARGB32.blue(color) / 255f)
                    )
            )
        )
            .fieldOf("color")
            .forGetter(LightComponent::color),
        Codec.FLOAT.fieldOf("radius")
            .forGetter(LightComponent::radius)
    ).apply(instance, LightComponent::new));

    public static final StreamCodec<ByteBuf, LightComponent> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, LightComponent::color,
        ByteBufCodecs.FLOAT, LightComponent::radius,
        LightComponent::new
    );
}
