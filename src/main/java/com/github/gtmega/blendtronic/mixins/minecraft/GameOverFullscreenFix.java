package com.github.gtmega.blendtronic.mixins.minecraft;

import net.minecraft.client.gui.GuiGameOver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiGameOver.class)
public class GameOverFullscreenFix {

    @Shadow private int field_146347_a;

    /**
     * @author FalsePattern
     * <p>
     * Prevents the player from being stuck at the game over screen
     */
    @SuppressWarnings("rawtypes")
    @Redirect(method = "initGui",
            at = @At(value = "INVOKE",
                    target = "Ljava/util/List;clear()V",
                    ordinal = 0),
            require = 1)
    private void resetGui(List instance) {
        field_146347_a = Math.min(field_146347_a, 19);
        instance.clear();
    }

}
