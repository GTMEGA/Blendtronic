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

package mega.blendtronic.mixin.mixins.common.misc;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(World.class)
public abstract class NullSafeGetBlockLightMixin {

    /**
     * @author FalsePattern
     * <p>
     * Makes Block light values null safe.
     */
    @Redirect(method = "getBlockLightValue_do",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/chunk/Chunk;getBlockLightValue(IIII)I",
                    ordinal = 0),
            require = 1)
    public int nullSafeGetBlockLight(Chunk chunk, int x, int y, int z, int l) {
        return chunk == null ? 0 : chunk.getBlockLightValue(x, y, z, l);
    }

}
