/*
 * Blendtronic
 *
 * Copyright (C) 2021-2025 SirFell, the MEGA team
 * All Rights Reserved
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, only version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package mega.blendtronic.modules.FastLeafDecay;

import mega.blendtronic.config.MinecraftConfig;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.util.Random;

public class LeafDecayHelper {
    private static final Random rng = new Random();
    public static void handleLeafDecay(World world, int x, int y, int z, Block block) {
        if (MinecraftConfig.FAST_LEAF_DECAY) {
            world.scheduleBlockUpdate(x, y, z, block, MinecraftConfig.DECAY_SPEED + (MinecraftConfig.DECAY_FUZZ > 0 ? rng.nextInt(MinecraftConfig.DECAY_FUZZ) : 0));
        }
    }
}
