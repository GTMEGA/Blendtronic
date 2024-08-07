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

package mega.blendtronic.mixin.mixins.common.netcode.packets;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import mega.blendtronic.modules.netcode.AccuratePositionContainer;
import mega.blendtronic.modules.netcode.AccurateRotationContainer;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Accessors(fluent = true, chain = false)
@Mixin(S18PacketEntityTeleport.class)
public abstract class S18PacketEntityTeleportMixin implements AccuratePositionContainer, AccurateRotationContainer {
    @Getter
    @Setter
    @Unique
    private double accurate$posX;

    @Getter
    @Setter
    @Unique
    private double accurate$posY;

    @Getter
    @Setter
    @Unique
    private double accurate$posZ;

    @Getter
    @Setter
    @Unique
    private float accurate$rotationPitch;

    @Getter
    @Setter
    @Unique
    private float accurate$rotationYaw;

    @Inject(method = "<init>(Lnet/minecraft/entity/Entity;)V",
            at = @At("RETURN"),
            require = 1)
    private void onConstruct(Entity entity, CallbackInfo ci) {
        accurate$pos(entity.posX, entity.posY, entity.posZ);
        accurate$rotation(entity.rotationYaw, entity.rotationPitch);
    }

    @Inject(method = "readPacketData",
            at = @At("RETURN"),
            require = 1)
    private void onReadPacketData(PacketBuffer buf, CallbackInfo ci) {
        accuratePosition$readPacketData(buf);
        accurateRotation$readPacketData(buf);
    }

    @Inject(method = "writePacketData",
            at = @At("RETURN"),
            require = 1)
    private void onWritePacketData(PacketBuffer buf, CallbackInfo ci) {
        accuratePosition$writePacketData(buf);
        accurateRotation$writePacketData(buf);
    }
}
