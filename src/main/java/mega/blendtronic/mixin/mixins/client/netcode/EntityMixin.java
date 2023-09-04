/*
 * FalseTweaks
 *
 * Copyright (C) 2022 FalsePattern
 * All Rights Reserved
 *
 * The above copyright notice, this permission notice and the word "SNEED"
 * shall be included in all copies or substantial portions of the Software.
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

package mega.blendtronic.mixin.mixins.client.netcode;

import mega.blendtronic.modules.netcode.AccurateEntityServerPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

@Mixin(Entity.class)
public abstract class EntityMixin implements AccurateEntityServerPos {
    @Shadow public int serverPosX;
    @Shadow public int serverPosY;
    @Shadow public int serverPosZ;

    @Unique private double accurate$serverPosX;
    @Unique private double accurate$serverPosY;
    @Unique private double accurate$serverPosZ;

    @Override
    public void accurate$serverPosX(double x) {
        accurate$serverPosX = x;
        serverPosX = MathHelper.floor_double(x * 32.0D);
    }

    @Override
    public void accurate$serverPosY(double y) {
        accurate$serverPosY = y;
        serverPosY = MathHelper.floor_double(y * 32.0D);
    }

    @Override
    public void accurate$serverPosZ(double z) {
        accurate$serverPosZ = z;
        serverPosZ = MathHelper.floor_double(z * 32.0D);
    }

    @Override
    public double accurate$serverPosX() {
        return accurate$serverPosX;
    }

    @Override
    public double accurate$serverPosY() {
        return accurate$serverPosY;
    }

    @Override
    public double accurate$serverPosZ() {
        return accurate$serverPosZ;
    }
}
