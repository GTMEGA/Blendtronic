package mega.blendtronic.mixin.mixins.common;

import lombok.val;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.crash.CrashReport;

@Mixin(CrashReport.class)
public abstract class FCCrashReportMixin {
    @Redirect(method = "<init>",
              at = @At(value = "INVOKE",
                       target = "Lfastcraft/H;ar(Lnet/minecraft/crash/CrashReport;Ljava/lang/String;Ljava/lang/Throwable;)V",
                       remap = false),
              require = 0,
              expect = 0)
    @Dynamic
    private void noHook(CrashReport report, String str, Throwable t) {
        try {
            val H = Class.forName("fastcraft.H");
            val m = H.getDeclaredMethod("ar", CrashReport.class, String.class, Throwable.class);
            m.setAccessible(true);
            m.invoke(null, report, str, t);
        } catch (Throwable tt) {
            tt.printStackTrace();
        }
    }
}
