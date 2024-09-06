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

package mega.blendtronic.modules.fancyfurnace;

import net.minecraft.block.BlockFurnace;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;

public class FancyFurnaceUtil {
    public static final int META_FULL_BIT = 0b1000;
    public static final int META_ROT_BITS = 0b0111;

    public static void updateFurnaceBlockState(TileEntityFurnace tile, World world, int x, int y, int z) {
        var meta = world.getBlockMetadata(x, y, z) & META_ROT_BITS;
        BlockFurnace.field_149934_M = true;

        if (tile.furnaceBurnTime > 0) {
            world.setBlock(x, y, z, Blocks.lit_furnace);
        } else {
            world.setBlock(x, y, z, Blocks.furnace);
        }

        BlockFurnace.field_149934_M = false;
        if (tile.getStackInSlot(2) != null) {
            meta |= META_FULL_BIT;
        }
        world.setBlockMetadataWithNotify(x, y, z, meta, 2);

        tile.validate();
        world.setTileEntity(x, y, z, tile);
    }
}
