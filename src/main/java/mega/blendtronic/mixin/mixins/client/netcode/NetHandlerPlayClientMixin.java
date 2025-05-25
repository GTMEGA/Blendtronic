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

package mega.blendtronic.mixin.mixins.client.netcode;

import com.llamalad7.mixinextras.sugar.Local;
import mega.blendtronic.modules.netcode.AccurateEntityServerPos;
import mega.blendtronic.modules.netcode.AccurateMotionContainer;
import mega.blendtronic.modules.netcode.AccuratePositionContainer;
import mega.blendtronic.modules.netcode.AccurateRotationContainer;
import mega.blendtronic.modules.netcode.AccurateRotationYawHeadContainer;
import com.mojang.authlib.GameProfile;
import lombok.val;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S0EPacketSpawnObject;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S11PacketSpawnExperienceOrb;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;

import java.util.List;

@Mixin(NetHandlerPlayClient.class)
public abstract class NetHandlerPlayClientMixin {
    @Shadow private WorldClient clientWorldController;

    @Inject(method = "handleSpawnPlayer",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/client/multiplayer/WorldClient;addEntityToWorld(ILnet/minecraft/entity/Entity;)V"),
            require = 1)
    private void onHandleSpawnPlayer(S0CPacketSpawnPlayer packet, CallbackInfo ci, @Local EntityOtherPlayerMP entityotherplayermp) {
        val packetPos = (AccuratePositionContainer)packet;
        val packetRot = (AccurateRotationContainer)packet;

        val x = packetPos.accurate$posX();
        val y = packetPos.accurate$posY();
        val z = packetPos.accurate$posZ();
        val yaw = packetRot.accurate$rotationYaw();
        val pitch = packetRot.accurate$rotationPitch();

        val entityPos = (AccurateEntityServerPos)entityotherplayermp;
        entityPos.accurate$serverPosX(x);
        entityPos.accurate$serverPosY(y);
        entityPos.accurate$serverPosZ(z);

        entityotherplayermp.setPositionAndRotation(x, y, z, yaw, pitch);
    }

    @Inject(method = "handleSpawnObject",
            at = @At("RETURN"),
            require = 1)
    private void onHandleSpawnObject(S0EPacketSpawnObject packet, CallbackInfo ci, @Local Object object) {
        if (object == null) {
            return;
        }
        val entity = (Entity) object;

        val packetPos = (AccuratePositionContainer) packet;
        val packetRot = (AccurateRotationContainer) packet;
        val packetMotion = (AccurateMotionContainer) packet;

        double x = packetPos.accurate$posX();
        double y = packetPos.accurate$posY();
        double z = packetPos.accurate$posZ();
        float yaw = packetRot.accurate$rotationYaw();
        float pitch = packetRot.accurate$rotationPitch();
        double motionX = packetMotion.accurate$motionX();
        double motionY = packetMotion.accurate$motionY();
        double motionZ = packetMotion.accurate$motionZ();

        ((AccurateEntityServerPos)entity).accurate$serverPos(x, y, z);

        entity.rotationPitch = pitch;
        entity.rotationYaw = yaw;
        if (packet.func_149009_m() > 0) {
            entity.setVelocity(motionX, motionY, motionZ);
        }
    }

    @Inject(method = "handleSpawnMob",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/client/multiplayer/WorldClient;addEntityToWorld(ILnet/minecraft/entity/Entity;)V"),
            require = 1)
    private void onHandleSpawnMob(S0FPacketSpawnMob packet, CallbackInfo ci, @Local EntityLivingBase entitylivingbase) {
        val packetPos = (AccuratePositionContainer)packet;
        val packetRot = (AccurateRotationContainer)packet;
        val packetMotion = (AccurateMotionContainer)packet;

        val x = packetPos.accurate$posX();
        val y = packetPos.accurate$posY();
        val z = packetPos.accurate$posZ();
        val yaw = packetRot.accurate$rotationYaw();
        val pitch = packetRot.accurate$rotationPitch();
        val motionX = packetMotion.accurate$motionX();
        val motionY = packetMotion.accurate$motionY();
        val motionZ = packetMotion.accurate$motionZ();

        ((AccurateEntityServerPos)entitylivingbase).accurate$serverPos(x, y, z);

        entitylivingbase.setPositionAndRotation(x, y, z, yaw, pitch);

        entitylivingbase.setVelocity(motionX, motionY, motionZ);
    }

    @Inject(method = "handleSpawnExperienceOrb",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/client/multiplayer/WorldClient;addEntityToWorld(ILnet/minecraft/entity/Entity;)V"),
            require = 1)
    private void onHandleSpawnExperienceOrb(S11PacketSpawnExperienceOrb packet, CallbackInfo ci, @Local EntityXPOrb entityxporb) {
        val packetPos = (AccuratePositionContainer)packet;

        val x = packetPos.accurate$posX();
        val y = packetPos.accurate$posY();
        val z = packetPos.accurate$posZ();

        ((AccurateEntityServerPos)entityxporb).accurate$serverPos(x, y, z);
    }

    @Inject(method = "handleSpawnGlobalEntity",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/client/multiplayer/WorldClient;addWeatherEffect(Lnet/minecraft/entity/Entity;)Z"),
            require = 1)
    private void onHandleSpawnGlobalEntity(S2CPacketSpawnGlobalEntity packet, CallbackInfo ci, @Local EntityLightningBolt entitylightningbolt) {
        if (entitylightningbolt == null) {
            return;
        }

        val packetPos = (AccuratePositionContainer)packet;

        val x = packetPos.accurate$posX();
        val y = packetPos.accurate$posY();
        val z = packetPos.accurate$posZ();

        ((AccurateEntityServerPos)entitylightningbolt).accurate$serverPos(x, y, z);
    }

    @Inject(method = "handleEntityVelocity",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/entity/Entity;setVelocity(DDD)V"),
            require = 1)
    private void onHandleEntityVelocity(S12PacketEntityVelocity packet, CallbackInfo ci, @Local Entity entity) {
        if (entity == null) {
            return;
        }

        val packetMotion = (AccurateMotionContainer)packet;

        val motionX = packetMotion.accurate$motionX();
        val motionY = packetMotion.accurate$motionY();
        val motionZ = packetMotion.accurate$motionZ();

        entity.setVelocity(motionX, motionY, motionZ);
    }

    @Redirect(method = "handleEntityVelocity",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/entity/Entity;setVelocity(DDD)V"))
    private void killHandleEntityVelocitySet(Entity instance, double x, double y, double z) {

    }

    /**
     * @author FalsePattern
     * @reason Accurate entity movement
     */
    @Overwrite
    public void handleEntityMovement(S14PacketEntity packet) {
        Entity entity = packet.func_149065_a(this.clientWorldController);

        if (entity != null) {
            val packetPos = (AccuratePositionContainer)packet;
            val packetRot = (AccurateRotationContainer)packet;

            val serverPosContainer = (AccurateEntityServerPos)entity;

            val dX = packetPos.accurate$posX();
            val dY = packetPos.accurate$posY();
            val dZ = packetPos.accurate$posZ();

            val yaw = packet.func_149060_h() ? packetRot.accurate$rotationYaw() : entity.rotationYaw;
            val pitch = packet.func_149060_h() ? packetRot.accurate$rotationPitch() : entity.rotationPitch;

            val sX = serverPosContainer.accurate$serverPosX() + dX;
            val sY = serverPosContainer.accurate$serverPosY() + dY;
            val sZ = serverPosContainer.accurate$serverPosZ() + dZ;

            serverPosContainer.accurate$serverPos(sX, sY, sZ);

            entity.setPositionAndRotation2(sX, sY, sZ, yaw, pitch, 3);
        }
    }


    /**
     * Updates an entity's position and rotation as specified by the packet
     * @author FalsePattern
     * @reason Accurate entity movement
     */
    @Overwrite
    public void handleEntityTeleport(S18PacketEntityTeleport p_147275_1_) {
        Entity entity = this.clientWorldController.getEntityByID(p_147275_1_.func_149451_c());

        if (entity != null) {
            val packetPos = (AccuratePositionContainer)p_147275_1_;
            val packetRot = (AccurateRotationContainer)p_147275_1_;

            val serverPosContainer = (AccurateEntityServerPos)entity;

            val x = packetPos.accurate$posX();
            val y = packetPos.accurate$posY();
            val z = packetPos.accurate$posZ();

            val yaw = packetRot.accurate$rotationYaw();
            val pitch = packetRot.accurate$rotationPitch();

            serverPosContainer.accurate$serverPos(x, y, z);
            entity.setPositionAndRotation2(x, y, z, yaw, pitch, 3);
        }
    }

    /**
     * Updates the direction in which the specified entity is looking, normally this head rotation is independent of the
     * rotation of the entity itself
     * @author FalsePattern
     * @reason Accurate entity movement
     */
    @Overwrite
    public void handleEntityHeadLook(S19PacketEntityHeadLook packet)
    {
        Entity entity = packet.func_149381_a(this.clientWorldController);

        if (entity != null) {
            entity.setRotationYawHead(((AccurateRotationYawHeadContainer)packet).accurate$rotationYawHead());
        }
    }
}
