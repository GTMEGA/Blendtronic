package mega.blendtronic.mixin.mixins.common.misc;

import baubles.common.lib.PlayerHandler;
import ic2.core.block.wiring.*;
import lombok.val;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {TileEntityChargepadBatBox.class,
                TileEntityChargepadCESU.class,
                TileEntityChargepadMFE.class,
                TileEntityChargepadMFSU.class},
       remap = false)
public abstract class IC2ChargepadBaubleSupportMixin extends TileEntityChargepadBlock {
    public IC2ChargepadBaubleSupportMixin(int tier, int output, int maxStorage) {
        super(tier, output, maxStorage);
    }

    @Inject(method = "getItems",
            at = @At("RETURN"),
            require = 4)
    private void injectBaublesCharge(EntityPlayer player, CallbackInfo ci) {
        val baubles = PlayerHandler.getPlayerBaubles(player);
        for (val bauble : baubles.stackList) {
            if (bauble != null) {
                chargeitems(bauble, getOutput());
                baubles.markDirty();
            }
        }
    }
}
