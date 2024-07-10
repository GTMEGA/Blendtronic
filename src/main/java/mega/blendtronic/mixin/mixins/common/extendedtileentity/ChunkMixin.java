/*
 * Blendtronic
 *
 * Copyright (C) 2021-2024 SirFell, the MEGA team
 * All Rights Reserved
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
