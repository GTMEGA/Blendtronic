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

import net.minecraft.entity.EntityLiving;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityLiving.class)
public abstract class EntityPickupDropMixin {

    @Shadow protected abstract void dropEquipment(boolean p_82160_1_, int p_82160_2_);

    /**
     * @author FalsePattern
     * <p>
     * Let mobs holding loot despawn.
     */
    @Redirect(method = "onLivingUpdate",
            at = @At(value = "FIELD",
                    target = "Lnet/minecraft/entity/EntityLiving;persistenceRequired:Z",
                    opcode = Opcodes.PUTFIELD,
                    ordinal = 0),
            require = 1)
    private void bypassPersistence(EntityLiving instance, boolean value) {}

    /**
     * @author FalsePattern
     * <p>
     * Forces despawning entities to drop their equipment.
     */
    @Redirect(method = "despawnEntity",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/EntityLiving;setDead()V"),
            require = 1)
    private void dropEquipmentOnDespawn(EntityLiving instance) {
        ((EntityPickupDropMixin)(Object)instance).dropEquipment(false, 0);
        instance.setDead();
    }

}
