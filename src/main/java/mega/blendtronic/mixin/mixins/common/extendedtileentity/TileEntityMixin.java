package mega.blendtronic.mixin.mixins.common.extendedtileentity;

import mega.blendtronic.api.ExtendedTileEntity;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TileEntity.class)
public abstract class TileEntityMixin implements ExtendedTileEntity {
    @Override
    public void blockMetaChanged() {}
}
