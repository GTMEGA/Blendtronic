package mega.blendtronic.mixin.mixins.common.extendedtileentity;

import lombok.val;
import mega.blendtronic.api.ExtendedTileEntity;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Chunk.class)
public abstract class ChunkMixin {
    @Inject(method = "func_150807_a",
            at = @At(value = "FIELD",
                     target = "Lnet/minecraft/tileentity/TileEntity;blockMetadata:I",
                     opcode = Opcodes.PUTFIELD,
                     shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD,
            require = 1)
    private void blockMetaChangedOnSetBlock(int chunkPosX,
                                            int posY,
                                            int chunkPosZ,
                                            Block block,
                                            int blockMeta,
                                            CallbackInfoReturnable<Boolean> cir,
                                            int subChunkIndex,
                                            int heightMapValue,
                                            Block oldBlock,
                                            int oldBlockMeta,
                                            ExtendedBlockStorage subChunk,
                                            boolean createdNewSubChunk,
                                            int posX,
                                            int posZ,
                                            int lightOpacity,
                                            TileEntity tileEntity) {
        if (blockMeta == oldBlockMeta || !(tileEntity instanceof ExtendedTileEntity))
            return;
        val extendedTileEntity = (ExtendedTileEntity) tileEntity;
        extendedTileEntity.blockMetaChanged();
    }

    @Inject(method = "setBlockMetadata",
            at = @At(value = "FIELD",
                     target = "Lnet/minecraft/tileentity/TileEntity;blockMetadata:I",
                     opcode = Opcodes.PUTFIELD,
                     shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD,
            require = 1)
    private void blockMetaChangedOnSetBlockMeta(int chunkPosX,
                                                int posY,
                                                int chunkPosZ,
                                                int blockMeta,
                                                CallbackInfoReturnable<Boolean> cir,
                                                ExtendedBlockStorage subChunk,
                                                int oldBlockMeta,
                                                TileEntity tileEntity) {
        if (blockMeta == oldBlockMeta || !(tileEntity instanceof ExtendedTileEntity))
            return;
        val extendedTileEntity = (ExtendedTileEntity) tileEntity;
        extendedTileEntity.blockMetaChanged();
    }
}
