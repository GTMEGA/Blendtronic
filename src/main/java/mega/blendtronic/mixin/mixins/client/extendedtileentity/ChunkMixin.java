package mega.blendtronic.mixin.mixins.client.extendedtileentity;

import lombok.val;
import mega.blendtronic.api.ExtendedTileEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static mega.blendtronic.mixin.plugin.MixinPlugin.POST_CHUNK_API_MIXIN_PRIORITY;

@Mixin(value = Chunk.class, priority = POST_CHUNK_API_MIXIN_PRIORITY)
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
