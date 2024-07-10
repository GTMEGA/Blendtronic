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
