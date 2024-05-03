package mega.blendtronic.mixin.mixins.common.thaumcraft;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.common.tiles.TileCrucible;

@Mixin(value = TileCrucible.class)
public class TileCrucibleMixin extends TileThaumcraft {

    /**
     * @author bot
     * @reason method call causes a crash and should not be run at all
     */
    @Overwrite(remap = false)
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource != null && resource.getFluidID() != FluidRegistry.WATER.getID()) {
            return 0;
        } else {
            if (doFill) {
                this.worldObj.markBlockForUpdate(this.xCoord,this.yCoord,this.zCoord);
            }
            return this.tank.fill(resource, doFill);
        }
    }

    @Shadow
    public FluidTank tank;
}
