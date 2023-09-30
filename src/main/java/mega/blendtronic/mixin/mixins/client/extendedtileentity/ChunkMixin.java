package mega.blendtronic.mixin.mixins.client.extendedtileentity;

import lombok.val;
import mega.blendtronic.api.ExtendedTileEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Chunk.class)
public abstract class ChunkMixin {
    @Redirect(method = "fillChunk",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/tileentity/TileEntity;getBlockType()Lnet/minecraft/block/Block;",
                       ordinal = 0),
              require = 1)
    private Block blockMetaChangedOnClientLoad(TileEntity tileEntity) {
        val block = tileEntity.getBlockType();
        if (tileEntity instanceof ExtendedTileEntity) {
            val extendedTileEntity = (ExtendedTileEntity) tileEntity;
            extendedTileEntity.blockMetaChanged();
        }
        return block;
    }
}
