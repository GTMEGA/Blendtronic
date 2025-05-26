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

package mega.blendtronic.mixin.mixins.common.intcache;

import mega.blendtronic.modules.intcache.NewIntCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.world.gen.layer.IntCache;

@Mixin(IntCache.class)
public abstract class IntCacheMixin {
    /**
     * @author ah-OOG-ah
     * @reason The old methods are non-threadsafe and barely safe at all - they recycle all allocated instances instead
     *         of explicitly releasing them.
     */
    @Overwrite
    public static synchronized int[] getIntCache(int size) {
        return NewIntCache.getCache(size);
    }
}
