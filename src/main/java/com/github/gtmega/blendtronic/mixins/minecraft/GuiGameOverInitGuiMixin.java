package com.github.gtmega.blendtronic.mixins.minecraft;

import net.minecraft.client.gui.GuiGameOver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGameOver.class)
public class GuiGameOverInitGuiMixin {

    // Number of ticks screen was open
    @Shadow
    private int field_146347_a;

    /**
     * @author ElNounch
     * <p>
     * Resets GameOver ui after switching to fullscreen to fix buttons disabling.
     */
    @Inject(method = "initGui", at = @At("HEAD"))
    public void resetInitGui(CallbackInfo ci) {
        if (field_146347_a > 19) {
            // Make sure buttons will be re-enabled next tick
            field_146347_a = 19;
        }
    }
}
