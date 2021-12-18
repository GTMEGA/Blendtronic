package com.github.gtmega.blendtronic.mixins.minecraft;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.shader.TesselatorVertexState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Tessellator.class)
public class TesselatorCardinalBuffIndexMixin {
    @Shadow
    private int rawBufferIndex;

    /**
     * @author SirFell
     * <p>
     * Fixes <a href="https://github.com/MinecraftForge/MinecraftForge/issues/981">MinecraftForge#981</a> . Crash on <a href="https://github.com/MinecraftForge/MinecraftForge/issues/981#issuecomment-57375939">bad moder rendering"(©LexManos)</a> of transparent/translucent blocks when they draw nothing.
     */
    @Inject(method = "getVertexState", at = @At("HEAD"), cancellable = true)
    public void getVertexStateNatural0Safe(float x, float y, float z, CallbackInfoReturnable<TesselatorVertexState> cir){
        if(this.rawBufferIndex <= 0) cir.setReturnValue(null); cir.cancel();
    }
}
