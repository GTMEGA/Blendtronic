package mega.blendtronic.mixin.mixins.common.fastcraft;

import net.minecraft.crash.CrashReportCategory;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CrashReportCategory.class)
public abstract class CrashReportCategoryMixin {
    @Redirect(method = "getPrunedStackTrace",
              at = @At(value = "INVOKE",
                       target = "Lfastcraft/H;at(Ljava/lang/Thread;)[Ljava/lang/StackTraceElement;",
                       remap = false),
              require = 0,
              expect = 0)
    @Dynamic
    private StackTraceElement[] noFastCraft(Thread thread) {
        return thread.getStackTrace();
    }
}
