package mega.blendtronic.mixin.mixins.common.netcode;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.inventory.Slot;
import net.minecraft.network.NetHandlerPlayServer;

@Mixin(NetHandlerPlayServer.class)
public class NetHandlerPlayServerMixin {

    @Inject(method = "processPlayerBlockPlacement",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/inventory/Container;detectAndSendChanges()V"),
            require = 1,
            cancellable = true)
    private void onProcessPlayerBlockPlacement(CallbackInfo ci, @Local(ordinal = 0) Slot slot) {
        if (slot == null)
            ci.cancel();
    }
}
