package mega.blendtronic.mixin.mixins.client.misc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.audio.SoundPoolEntry;

@Mixin(SoundManager.class)
public class SoundManagerCrashFix {
    @Redirect(method = "getNormalizedPitch",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/client/audio/SoundPoolEntry;getPitch()D"),
              require = 1)
    private double nullSafeGetPitch(SoundPoolEntry instance) {
        return instance != null ? instance.getPitch() : 0;
    }

    @Redirect(method = "getNormalizedVolume",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/client/audio/SoundPoolEntry;getVolume()D"),
              require = 1)
    private double nullSafeGetVolume(SoundPoolEntry instance) {
        return instance != null ? instance.getVolume() : 0;
    }
}
