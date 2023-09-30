package mega.blendtronic.mixin.mixins.client.cauldrontank;

import lombok.val;
import mega.blendtronic.modules.cauldrontank.TileCauldron;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderBlocks.class)
public abstract class RenderBlocksMixin {
    @Shadow
    public abstract void renderFaceYPos(Block block, double posX, double posY, double posZ, IIcon icon);

    @Redirect(method = "renderBlockCauldron",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/world/IBlockAccess;getBlockMetadata(III)I"),
              require = 1)
    private int replaceWaterLevel(IBlockAccess world, int posX, int posY, int posZ) {
        checkTile:
        {
            val tileEntity = world.getTileEntity(posX, posY, posZ);
            if (!(tileEntity instanceof TileCauldron))
                break checkTile;
            val cauldronTile = (TileCauldron) tileEntity;
            if (cauldronTile.isEmpty())
                break checkTile;
            val waterHeight = cauldronTile.getWaterHeightForRender();
            val icon = BlockLiquid.getLiquidIcon("water_still");
            renderFaceYPos(Blocks.cauldron, posX, (float) posY - 1.0F + waterHeight, posZ, icon);
        }
        return 0;
    }
}
