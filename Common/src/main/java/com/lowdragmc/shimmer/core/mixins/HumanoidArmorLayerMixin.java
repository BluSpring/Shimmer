package com.lowdragmc.shimmer.core.mixins;

import com.lowdragmc.shimmer.client.ResourceUtils;
import com.lowdragmc.shimmer.client.ShimmerRenderTypes;
import com.lowdragmc.shimmer.client.postprocessing.PostProcessing;
import com.lowdragmc.shimmer.client.shader.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

/**
 * @author KilaBash
 * @date 2022/05/02
 * @implNote HumanoidArmorLayerMixin, used to inject emissive + bloom armor via custom resource pack.
 */
@Mixin(HumanoidArmorLayer.class)//FIXME
public abstract class HumanoidArmorLayerMixin<T extends LivingEntity, M extends HumanoidModel<T>, A extends HumanoidModel<T>> {

    @Inject(method = "renderModel", at = @At(value = "RETURN"))
    private void injectRenderModel(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, A model, int dyeColor, ResourceLocation armorResource, CallbackInfo ci) {
        ResourceLocation bloomResource = ResourceLocation.fromNamespaceAndPath(armorResource.getNamespace(), armorResource.getPath().replace(".png", "_bloom.png"));
        if (ResourceUtils.isResourceExist(bloomResource)) {
            PoseStack finalStack = RenderUtils.copyPoseStack(poseStack);
            PostProcessing.BLOOM_UNITY.postEntity(sourceConsumer -> model.renderToBuffer(finalStack, sourceConsumer.getBuffer(ShimmerRenderTypes.emissiveArmor(bloomResource)), 0xF000F0, OverlayTexture.NO_OVERLAY, dyeColor));
        }
    }
}
