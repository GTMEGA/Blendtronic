package mega.blendtronic.mixin.mixins.common.misc;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import lombok.val;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ConcurrentModificationException;
import java.util.List;

@Mixin(World.class)
public abstract class NullSafeGetTE_WorldMixin {
    @Shadow
    private List<TileEntity> addedTileEntityList;

    @Redirect(method = "getTileEntity",
              at = @At(value = "INVOKE",
                       target = "Ljava/util/List;get(I)Ljava/lang/Object;"),
              require = 2)
    private Object checkForNullTE(List<TileEntity> instance,
                                  int i,
                                  @Local(argsOnly = true, ordinal = 0) int x,
                                  @Local(argsOnly = true, ordinal = 1) int y,
                                  @Local(argsOnly = true, ordinal = 2) int z,
                                  @Share("hadNullTE") LocalBooleanRef hadNullTE) {
        val te = instance.get(i);
        if (te == null) {
            mega.blendtronic.Share.LOG.warn("Crash prevented, null TileEntity at index: [{}] with pos: X:[{}] Y:[{}] Z:[{}]", i, x, y, z);
            mega.blendtronic.Share.LOG.trace("Trace: ", new Throwable());
            hadNullTE.set(true);
        }
        return te;
    }

    @Redirect(method = "getTileEntity",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/tileentity/TileEntity;isInvalid()Z"),
              require = 2)
    private boolean safeTEInvalidCheck(TileEntity instance) {
        return instance == null || instance.isInvalid();
    }

    @Inject(method = "getTileEntity",
            at = @At(value = "RETURN"),
            require = 2)
    private void cleanTEListFromNulls(CallbackInfoReturnable<TileEntity> cir,
                                      @Share("hadNullTE") LocalBooleanRef hadNullTE) {
        if (hadNullTE.get()) {
            var removed = 0;
            for (int retry = 0; retry < 3; retry++) {
                try {
                    val it = this.addedTileEntityList.iterator();
                    var i = 0;
                    while (it.hasNext()) {
                        if (it.next() == null) {
                            it.remove();
                            removed++;
                            mega.blendtronic.Share.LOG.trace("Removed null TE at index: [{}]", i);
                        }
                        i++;
                    }
                    break;
                } catch (ConcurrentModificationException cme) {
                    mega.blendtronic.Share.LOG.warn("CME while cleaning TE list", cme);
                }
            }

            mega.blendtronic.Share.LOG.warn("Removed :[{}] null TEs from World", removed);
        }
    }
}
