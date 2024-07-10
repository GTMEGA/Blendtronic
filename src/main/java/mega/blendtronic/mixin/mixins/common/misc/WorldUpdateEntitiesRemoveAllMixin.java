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

package mega.blendtronic.mixin.mixins.common.misc;

import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Collection;
import java.util.HashSet;

@Mixin(World.class)
public abstract class WorldUpdateEntitiesRemoveAllMixin {
    /**
     * @author Botn365
     * <p>
     * Reduces lag spike when toRemoveTileEntities is large
     */
    @ModifyArg(method = "updateEntities",at = @At(value = "INVOKE",target = "Ljava/util/List;removeAll(Ljava/util/Collection;)Z",ordinal = 1),index = 0,require = 1)
    private Collection<?> fix(Collection<?> collection) {
        return new HashSet<>(collection);
    }
}
