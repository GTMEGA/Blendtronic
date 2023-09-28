package mega.blendtronic.mixin.mixins.common.buildcraft;

import buildcraft.api.power.IRedstoneEngineReceiver;
import buildcraft.core.lib.block.TileBuildCraft;
import buildcraft.core.lib.inventory.InvUtils;
import buildcraft.core.lib.inventory.filters.StackFilter;
import buildcraft.factory.TileHopper;
import lombok.val;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.common.util.ForgeDirection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileHopper.class)
public abstract class TileHopperMixin extends TileBuildCraft implements IInventory, IRedstoneEngineReceiver {
    @Inject(method = "updateEntity",
            at = @At(value = "INVOKE",
                     target = "Lbuildcraft/core/lib/block/TileBuildCraft;updateEntity()V",
                     shift = At.Shift.AFTER),
            require = 1)
    private void pullFromTop(CallbackInfo ci) {
        if (worldObj.isRemote)
            return;
        if (worldObj.getTotalWorldTime() % 2L != 0L)
            return;

        val inputTileEntity = getTile(ForgeDirection.UP);
        if (!(inputTileEntity instanceof IInventory))
            return;

        val inputInventory = (IInventory) inputTileEntity;
        InvUtils.moveOneItem(inputInventory, ForgeDirection.DOWN, this, ForgeDirection.UP, StackFilter.ALL);
    }
}
