package mega.blendtronic.mixin.mixins.common.cauldrontank;


import mega.blendtronic.modules.cauldrontank.TileCauldron;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockCauldron.class)
public abstract class BlockCauldronMixin extends Block implements ITileEntityProvider {
    protected BlockCauldronMixin(Material material) {
        super(material);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int blockMeta) {
        return new TileCauldron(blockMeta);
    }
}
