package com.lowdragmc.shimmer.core.mixins;

import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.lowdragmc.shimmer.client.rendertarget.ProxyTarget;
import com.lowdragmc.shimmer.client.rendertarget.ScaleTextureTarget;
import com.lowdragmc.shimmer.client.rendertarget.SelectRenderTarget;
import com.lowdragmc.shimmer.platform.Services;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.shaders.Uniform;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.util.GsonHelper;

/**
 * @author KilaBash
 * @date 2022/05/03
 * @implNote PostChainMixin, add more features for vanilla PostChain stuff
 */
@Mixin(PostChain.class)
public abstract class PostChainMixin {

    @Final @Shadow private RenderTarget screenTarget;

    @Shadow @Final private Map<String, RenderTarget> customRenderTargets;
    @Shadow private int screenWidth;
    @Shadow private int screenHeight;
    @Shadow @Final private List<RenderTarget> fullSizedTargets;

    @Shadow public abstract void addTempTarget(String pName, int pWidth, int pHeight);

    @Unique
    public void shimmer$addTempTarget(String pName, float sw, float sh) {
        RenderTarget rendertarget = new ScaleTextureTarget(sw, sh, screenWidth, screenHeight, true, Minecraft.ON_OSX);
        rendertarget.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        if (Services.PLATFORM.isStencilEnabled(screenTarget)) { Services.PLATFORM.enableStencil(rendertarget); }
        this.customRenderTargets.put(pName, rendertarget);
        this.fullSizedTargets.add(rendertarget);
    }

    /**
     * @author KilaBash
     */
    @Inject(method = "parseTargetNode",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/GsonHelper;getAsInt(Lcom/google/gson/JsonObject;Ljava/lang/String;I)I",
                    ordinal = 0), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void injectParseTargetNode(JsonElement pJson, CallbackInfo ci, JsonObject jsonobject, String s) {
        if (jsonobject.has("scaleSize")) {
            JsonObject scaleSize = GsonHelper.getAsJsonObject(jsonobject, "scaleSize");
            float width = GsonHelper.getAsFloat(scaleSize, "width", 1);
            float height = GsonHelper.getAsFloat(scaleSize, "height", 1);
            shimmer$addTempTarget(s, width, height);
            if (GsonHelper.getAsBoolean(jsonobject, "bilinear", false)) {
                customRenderTargets.get(s).setFilterMode(GL11.GL_LINEAR);
            }
            ci.cancel();
        }
    }

    @Inject(method = "parseTargetNode",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/PostChain;addTempTarget(Ljava/lang/String;II)V",
                    ordinal = 1,
                    shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void injectParseTargetNodePost(JsonElement pJson, CallbackInfo ci, JsonObject jsonobject, String s) {
        if (GsonHelper.getAsBoolean(jsonobject, "bilinear", false)) {
            customRenderTargets.get(s).setFilterMode(GL11.GL_LINEAR);
        }
    }

    @Inject(method = "getRenderTarget", at = @At(value = "HEAD"), cancellable = true)
    private void injectGetRenderTarget(String pTarget, CallbackInfoReturnable<RenderTarget> cir) {
        if (pTarget != null) {
            if (pTarget.equals("shimmer:input")) {
                cir.setReturnValue(customRenderTargets.computeIfAbsent(pTarget, k -> new ProxyTarget(screenTarget)));
            } else if (pTarget.equals("shimmer:output")) {
                if (!customRenderTargets.containsKey(pTarget)) {
                    addTempTarget(pTarget, screenWidth, screenHeight);
                }
            } else if (pTarget.equals("shimmer:composite_source")) {
                cir.setReturnValue(new SelectRenderTarget());
            }
        }
    }

    @WrapOperation(method = "parseUniformNode", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/shaders/Uniform;set(F)V"))
    private void injectParseUniformNode(Uniform instance, float pX, Operation<Void> original) {
        if (instance.getType() < 4) {
            instance.set((int)pX);
        } else {
            original.call(instance, pX);
        }
    }

    @WrapOperation(method = "parseUniformNode", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/shaders/Uniform;set(FF)V"))
    private void injectParseUniformNode2(Uniform instance, float pX, float pY, Operation<Void> original) {
        if (instance.getType() < 4) {
            instance.set((int)pX, (int)pY);
        } else {
            original.call(instance, pX, pY);
        }
    }

    @WrapOperation(method = "parseUniformNode", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/shaders/Uniform;set(FFF)V"))
    private void injectParseUniformNode3(Uniform instance, float pX, float pY, float pZ, Operation<Void> original) {
        if (instance.getType() < 4) {
            instance.set((int)pX, (int)pY, (int)pZ);
        } else {
            original.call(instance, pX, pY, pZ);
        }
    }

    @WrapOperation(method = "parseUniformNode", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/shaders/Uniform;set(FFFF)V"))
    private void injectParseUniformNode4(Uniform instance, float pX, float pY, float pZ, float pW, Operation<Void> original) {
        if (instance.getType() < 4) {
            instance.set((int)pX, (int)pY, (int)pZ, (int)pW);
        } else {
            original.call(instance, pX, pY, pZ, pW);
        }
    }
}
