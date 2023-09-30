package mega.blendtronic.modules.cauldrontank;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.val;
import lombok.var;
import mega.blendtronic.api.ExtendedTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;

public final class TileCauldron extends TileEntity implements IFluidHandler, ExtendedTileEntity {
    private static final String FLUID_AMOUNT_NBT_TAG = "fluid_amount";

    private static final int CAPACITY_MB = 750;

    private static final float MIN_RENDER_FLUID_LEVEL = 0.255F;
    private static final float MAX_RENDER_FLUID_LEVEL = 0.9375F;
    private static final float RENDER_FLUID_LEVEL_SCALE = (MAX_RENDER_FLUID_LEVEL - MIN_RENDER_FLUID_LEVEL) / CAPACITY_MB;

    private final FluidTank tank = new FluidTank(CAPACITY_MB);
    private int fluidAmount = 0;

    /**
     * Required for init
     */
    @SuppressWarnings("unused")
    public TileCauldron() {
    }

    public TileCauldron(int blockMeta) {
        setFluidAmount(fluidAmountFromBlockMeta(blockMeta));
    }

    // region Accessors
    public void setFluidAmount(int fluidAmount) {
        var fluidStack = tank.getFluid();
        if (isFluidStackValid(fluidStack)) {
            fluidStack.amount = clampedFluidAmount(fluidAmount);
        } else {
            fluidStack = new FluidStack(FluidRegistry.WATER, clampedFluidAmount(fluidAmount));
            tank.setFluid(fluidStack);
        }
        updateFluidAmount();
    }

    public int getFluidAmount() {
        return tank.getFluidAmount();
    }

    public boolean isEmpty() {
        return tank.getFluidAmount() <= 0;
    }
    // endregion

    //region Fluid Handler
    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (isDirectionValid(from) && isFluidStackValid(resource)) {
            val filledAmount = tank.fill(resource, doFill);
            if (doFill)
                updateFluidAmount();
            return filledAmount;
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (isDirectionValid(from) && isFluidStackValid(resource)) {
            val drainedFluidStack = tank.drain(resource.amount, doDrain);
            if (doDrain)
                updateFluidAmount();
            return drainedFluidStack;
        }
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if (isDirectionValid(from)) {
            val drainedFluidStack = tank.drain(maxDrain, doDrain);
            if (doDrain)
                updateFluidAmount();
            return drainedFluidStack;
        }
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return isDirectionValid(from) && isFluidValid(fluid);
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return isDirectionValid(from) && isFluidValid(fluid);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[]{tank.getInfo()};
    }
    // endregion

    // region NBT & Packets
    @Override
    public void readFromNBT(NBTTagCompound input) {
        super.readFromNBT(input);
        readTankFromNBT(input);
    }

    @Override
    public void writeToNBT(NBTTagCompound output) {
        super.writeToNBT(output);
        writeTankToNBT(output);
    }

    @Override
    public Packet getDescriptionPacket() {
        if (worldObj.isRemote)
            return null;
        val output = new NBTTagCompound();
        writeTankToNBT(output);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 4, output);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager networkManager, S35PacketUpdateTileEntity packet) {
        if (!worldObj.isRemote)
            return;
        val input = packet.func_148857_g();
        readTankFromNBT(input);
    }

    private void readTankFromNBT(NBTTagCompound input) {
        setFluidAmount(input.getInteger(FLUID_AMOUNT_NBT_TAG));
    }

    private void writeTankToNBT(NBTTagCompound output) {
        output.setInteger(FLUID_AMOUNT_NBT_TAG, getFluidAmount());
    }
    // endregion

    // region Updating
    @Override
    public void blockMetaChanged() {
        val oldBlockMeta = blockMetaFromFluidAmount(fluidAmount);
        if (blockMetadata == oldBlockMeta)
            return;

        val oldFluidAmountFromBlockMeta = fluidAmountFromBlockMeta(oldBlockMeta);
        val newFluidAmountFromBlockMeta = fluidAmountFromBlockMeta(blockMetadata);

        val fluidOffset = fluidAmount - oldFluidAmountFromBlockMeta;
        setFluidAmount(newFluidAmountFromBlockMeta + fluidOffset);
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    private void updateFluidAmount() {
        val tankFluidAmount = tank.getFluidAmount();
        if (tankFluidAmount == fluidAmount)
            return;
        fluidAmount = tankFluidAmount;

        if (worldObj == null)
            return;

        val newBlockMeta = blockMetaFromFluidAmount(fluidAmount);
        if (newBlockMeta == blockMetadata) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        } else {
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, newBlockMeta, 3);
        }

        if (!worldObj.isRemote)
            markDirty();
    }
    // endregion

    // region Render
    @SideOnly(Side.CLIENT)
    public float getWaterHeightForRender() {
        return MIN_RENDER_FLUID_LEVEL + clampedFluidAmount(fluidAmount) * RENDER_FLUID_LEVEL_SCALE;
    }
    // endregion

    // region Helpers
    private static boolean isFluidStackValid(FluidStack fluidStack) {
        if (fluidStack == null)
            return false;
        return isFluidValid(fluidStack.getFluid());
    }

    private static boolean isFluidValid(Fluid fluid) {
        return fluid == FluidRegistry.WATER;
    }

    private static boolean isDirectionValid(ForgeDirection direction) {
        return direction != ForgeDirection.UP;
    }

    private static int clampedFluidAmount(int fluidAmount) {
        return MathHelper.clamp_int(fluidAmount, 0, CAPACITY_MB);
    }

    private static int blockMetaFromFluidAmount(int fluidAmount) {
        if (fluidAmount < 250)
            return 0;
        if (fluidAmount < 500)
            return 1;
        if (fluidAmount < 750)
            return 2;
        return 3;
    }

    private static int fluidAmountFromBlockMeta(int blockMeta) {
        switch (blockMeta) {
            default:
            case 0:
                return 0;
            case 1:
                return 250;
            case 2:
                return 500;
            case 3:
                return 750;
        }
    }
    // endregion
}
