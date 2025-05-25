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

package mega.blendtronic.mixin.mixins.common.netcode;

import mega.blendtronic.modules.netcode.AccurateEntityTrackerEntity;
import mega.blendtronic.modules.netcode.AccuratePositionContainer;
import mega.blendtronic.modules.netcode.AccurateRotationContainer;
import mega.blendtronic.modules.netcode.AccurateRotationYawHeadContainer;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.val;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTrackerEntry;
import net.minecraft.entity.ai.attributes.ServersideAttributeMap;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S04PacketEntityEquipment;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0FPacketSpawnMob;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.server.S19PacketEntityHeadLook;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1CPacketEntityMetadata;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S20PacketEntityProperties;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;
import net.minecraft.world.storage.MapData;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Accessors(fluent = true, chain = false)
@Mixin(EntityTrackerEntry.class)
public abstract class EntityTrackerEntityMixin implements AccurateEntityTrackerEntity {

    @Shadow public boolean playerEntitiesUpdated;

    @Shadow private boolean isDataInitialized;

    @Shadow public Entity myEntity;

    @Shadow private double posX;

    @Shadow private double posY;

    @Shadow private double posZ;

    @Shadow public abstract void sendEventsToPlayers(List p_73125_1_);

    @Shadow private Entity field_85178_v;

    @Shadow public abstract void func_151259_a(Packet p_151259_1_);

    @Shadow public int ticks;

    @Shadow protected abstract void sendMetadataToAllAssociatedPlayers();

    @Shadow public int updateFrequency;
    @Shadow private int ticksSinceLastForcedTeleport;
    @Shadow public int lastScaledXPosition;
    @Shadow public int lastScaledYPosition;
    @Shadow public int lastScaledZPosition;
    @Shadow public int lastYaw;
    @Shadow public int lastPitch;
    @Shadow private boolean sendVelocityUpdates;
    @Shadow public double motionX;
    @Shadow public double motionY;
    @Shadow public double motionZ;
    @Shadow private boolean ridingEntity;
    @Shadow public int lastHeadMotion;

    @Shadow public abstract void func_151261_b(Packet p_151261_1_);

    @Shadow public int blocksDistanceThreshold;

    @Shadow protected abstract boolean isPlayerWatchingThisChunk(EntityPlayerMP p_73121_1_);

    @Shadow public Set<EntityPlayerMP> trackingPlayers;

    @Shadow protected abstract Packet func_151260_c();

    @Getter
    @Unique
    private double accurate$lastScaledXPosition;

    @Getter
    @Unique
    private double accurate$lastScaledYPosition;

    @Getter
    @Unique
    private double accurate$lastScaledZPosition;

    @Getter
    @Unique
    private float accurate$lastYaw;

    @Getter
    @Unique
    private float accurate$lastPitch;

    @Getter
    @Unique
    private float accurate$lastHeadMotion;

    @Inject(method = "<init>",
            at = @At("RETURN"),
            require = 1)
    private void onConstruct(Entity entity, int blocksDistanceThreshold, int updateFrequency, boolean sendVelocityUpdates, CallbackInfo ci) {
        accurate$lastScaledXPosition = entity.posX;
        accurate$lastScaledYPosition = entity.posY;
        accurate$lastScaledZPosition = entity.posZ;

        accurate$lastYaw = entity.rotationYaw;
        accurate$lastPitch = entity.rotationPitch;
        accurate$lastHeadMotion = entity.getRotationYawHead();
        if (updateFrequency == Integer.MAX_VALUE) {
            this.updateFrequency = 200;
        }
    }

    @Override
    public void accurate$lastScaledXPosition(double value) {
        accurate$lastScaledXPosition = value;
        lastScaledXPosition = this.myEntity.myEntitySize.multiplyBy32AndRound(value);
    }

    @Override
    public void accurate$lastScaledYPosition(double value) {
        accurate$lastScaledYPosition = value;
        lastScaledYPosition = MathHelper.floor_double(value * 32.0D);
    }

    @Override
    public void accurate$lastScaledZPosition(double value) {
        accurate$lastScaledZPosition = value;
        lastScaledZPosition = this.myEntity.myEntitySize.multiplyBy32AndRound(value);
    }

    @Override
    public void accurate$lastYaw(float value) {
        accurate$lastYaw = value;
        lastYaw = MathHelper.floor_float(value * 256.0F / 360.0F);
    }

    @Override
    public void accurate$lastPitch(float value) {
        accurate$lastPitch = value;
        lastPitch = MathHelper.floor_float(value * 256.0F / 360.0F);
    }

    @Override
    public void accurate$lastHeadMotion(float value) {
        accurate$lastHeadMotion = value;
        lastHeadMotion = MathHelper.floor_float(value * 256.0F / 360.0F);
    }

    /**
     * also sends velocity, rotation, and riding info.
     * @author FalsePattern
     * @reason Accurate position and rotation
     */
    @Overwrite
    public void sendLocationToAllClients(List p_73122_1_)
    {
        this.playerEntitiesUpdated = false;

        if (!this.isDataInitialized || this.myEntity.getDistanceSq(this.posX, this.posY, this.posZ) > 16.0D)
        {
            this.posX = this.myEntity.posX;
            this.posY = this.myEntity.posY;
            this.posZ = this.myEntity.posZ;
            this.isDataInitialized = true;
            this.playerEntitiesUpdated = true;
            this.sendEventsToPlayers(p_73122_1_);
        }

        if (this.field_85178_v != this.myEntity.ridingEntity || this.myEntity.ridingEntity != null && this.ticks % 60 == 0)
        {
            this.field_85178_v = this.myEntity.ridingEntity;
            this.func_151259_a(new S1BPacketEntityAttach(0, this.myEntity, this.myEntity.ridingEntity));
        }

        if (this.myEntity instanceof EntityItemFrame && this.ticks % 10 == 0) {
            EntityItemFrame entityitemframe = (EntityItemFrame)this.myEntity;
            ItemStack itemstack = entityitemframe.getDisplayedItem();

            if (itemstack != null && itemstack.getItem() instanceof ItemMap)
            {
                MapData mapdata = Items.filled_map.getMapData(itemstack, this.myEntity.worldObj);
                Iterator iterator = p_73122_1_.iterator();

                while (iterator.hasNext())
                {
                    EntityPlayer entityplayer = (EntityPlayer)iterator.next();
                    EntityPlayerMP entityplayermp = (EntityPlayerMP)entityplayer;
                    mapdata.updateVisiblePlayers(entityplayermp, itemstack);
                    Packet packet = Items.filled_map.func_150911_c(itemstack, this.myEntity.worldObj, entityplayermp);

                    if (packet != null)
                    {
                        entityplayermp.playerNetServerHandler.sendPacket(packet);
                    }
                }
            }

            this.sendMetadataToAllAssociatedPlayers();
        } else if (this.ticks % this.updateFrequency == 0 || this.myEntity.isAirBorne || this.myEntity.getDataWatcher().hasChanges()) {
            if (this.myEntity.ridingEntity == null) {
                int xPos = this.myEntity.myEntitySize.multiplyBy32AndRound(this.myEntity.posX);
                int yPos = MathHelper.floor_double(this.myEntity.posY * 32.0D);
                int zPos = this.myEntity.myEntitySize.multiplyBy32AndRound(this.myEntity.posZ);
                int rotYaw = MathHelper.floor_float(this.myEntity.rotationYaw * 256.0F / 360.0F);
                int rotPitch = MathHelper.floor_float(this.myEntity.rotationPitch * 256.0F / 360.0F);
                int xDelta = xPos - this.lastScaledXPosition;
                int yDelta = yPos - this.lastScaledYPosition;
                int zDelta = zPos - this.lastScaledZPosition;

                double acc$xPos = this.myEntity.posX;
                double acc$yPos = this.myEntity.posY;
                double acc$zPos = this.myEntity.posZ;
                float acc$rotYaw = this.myEntity.rotationYaw;
                float acc$rotPitch = this.myEntity.rotationPitch;

                Packet packet = null;
                boolean significantMotion = Math.abs(xDelta) >= 4 || Math.abs(yDelta) >= 4 || Math.abs(zDelta) >= 4 || this.ticks % 60 == 0;
                boolean significantRotation = Math.abs(rotYaw - this.lastYaw) >= 4 || Math.abs(rotPitch - this.lastPitch) >= 4;

                if (this.ticks > 0 || this.myEntity instanceof EntityArrow) {
                    if (xDelta >= -128 && xDelta < 128 && yDelta >= -128 && yDelta < 128 && zDelta >= -128 && zDelta < 128 && (this.ticks - this.ticksSinceLastForcedTeleport) <= 20 && !this.ridingEntity) {
                        if (significantMotion && significantRotation) {
                            packet = new S14PacketEntity.S17PacketEntityLookMove(this.myEntity.getEntityId(), (byte)xDelta, (byte)yDelta, (byte)zDelta, (byte)rotYaw, (byte)rotPitch);
                            ((AccuratePositionContainer)packet).accurate$pos(acc$xPos, acc$yPos, acc$zPos);
                            ((AccurateRotationContainer)packet).accurate$rotation(acc$rotYaw, acc$rotPitch);
                        } else if (significantMotion) {
                            packet = new S14PacketEntity.S15PacketEntityRelMove(this.myEntity.getEntityId(), (byte)xDelta, (byte)yDelta, (byte)zDelta);
                            ((AccuratePositionContainer)packet).accurate$pos(acc$xPos, acc$yPos, acc$zPos);
                        } else if (significantRotation) {
                            packet = new S14PacketEntity.S16PacketEntityLook(this.myEntity.getEntityId(), (byte)rotYaw, (byte)rotPitch);
                            ((AccurateRotationContainer)packet).accurate$rotation(acc$rotYaw, acc$rotPitch);
                        }
                    } else {
                        this.ticksSinceLastForcedTeleport = this.ticks;
                        packet = new S18PacketEntityTeleport(this.myEntity.getEntityId(), xPos, yPos, zPos, (byte)rotYaw, (byte)rotPitch);
                        ((AccuratePositionContainer)packet).accurate$pos(acc$xPos, acc$yPos, acc$zPos);
                        ((AccurateRotationContainer)packet).accurate$rotation(acc$rotYaw, acc$rotPitch);
                    }
                }

                if (this.sendVelocityUpdates) {
                    double dmX = this.myEntity.motionX - this.motionX;
                    double dmY = this.myEntity.motionY - this.motionY;
                    double dmZ = this.myEntity.motionZ - this.motionZ;
                    double dmMin = 0.02D;
                    double dmAbsSq = dmX * dmX + dmY * dmY + dmZ * dmZ;

                    if (dmAbsSq > dmMin * dmMin || dmAbsSq > 0.0D && this.myEntity.motionX == 0.0D && this.myEntity.motionY == 0.0D && this.myEntity.motionZ == 0.0D) {
                        this.motionX = this.myEntity.motionX;
                        this.motionY = this.myEntity.motionY;
                        this.motionZ = this.myEntity.motionZ;
                        this.func_151259_a(new S12PacketEntityVelocity(this.myEntity.getEntityId(), this.motionX, this.motionY, this.motionZ));
                    }
                }

                if (packet != null) {
                    this.func_151259_a(packet);
                }

                this.sendMetadataToAllAssociatedPlayers();

                if (significantMotion) {
                    accurate$lastScaledXPosition(acc$xPos);
                    accurate$lastScaledYPosition(acc$yPos);
                    accurate$lastScaledZPosition(acc$zPos);
                }

                if (significantRotation) {
                    accurate$lastYaw(acc$rotYaw);
                    accurate$lastPitch(acc$rotPitch);
                }

                this.ridingEntity = false;
            } else {
                int rotYaw = MathHelper.floor_float(this.myEntity.rotationYaw * 256.0F / 360.0F);
                int rotPitch = MathHelper.floor_float(this.myEntity.rotationPitch * 256.0F / 360.0F);

                float acc$rotYaw = this.myEntity.rotationYaw;
                float acc$rotPitch = this.myEntity.rotationPitch;

                boolean significantRotation = Math.abs(rotYaw - this.lastYaw) >= 4 || Math.abs(rotPitch - this.lastPitch) >= 4;

                if (significantRotation) {
                    val thePacket = new S14PacketEntity.S16PacketEntityLook(this.myEntity.getEntityId(), (byte)rotYaw, (byte)rotPitch);
                    ((AccurateRotationContainer)thePacket).accurate$rotation(acc$rotYaw, acc$rotPitch);
                    this.func_151259_a(thePacket);

                    accurate$lastYaw(acc$rotYaw);
                    accurate$lastPitch(acc$rotPitch);
                }

                accurate$lastScaledXPosition(this.myEntity.posX);
                accurate$lastScaledYPosition(this.myEntity.posY);
                accurate$lastScaledZPosition(this.myEntity.posZ);

                this.sendMetadataToAllAssociatedPlayers();
                this.ridingEntity = true;
            }

            int yawHead = MathHelper.floor_float(this.myEntity.getRotationYawHead() * 256.0F / 360.0F);
            float acc$yawHead = this.myEntity.getRotationYawHead();

            if (Math.abs(yawHead - this.lastHeadMotion) >= 4) {
                val thePacket = new S19PacketEntityHeadLook(this.myEntity, (byte)yawHead);
                ((AccurateRotationYawHeadContainer)thePacket).accurate$rotationYawHead(acc$yawHead);
                this.func_151259_a(thePacket);
                accurate$lastHeadMotion(acc$yawHead);
            }

            this.myEntity.isAirBorne = false;
        }

        ++this.ticks;

        if (this.myEntity.velocityChanged) {
            this.func_151261_b(new S12PacketEntityVelocity(this.myEntity));
            this.myEntity.velocityChanged = false;
        }
    }

    /**
     * if the player is more than the distance threshold (typically 64) then the player is removed instead
     * @author FalsePattern
     * @reason Accurate position
     */
    @Overwrite
    public void tryStartWachingThis(EntityPlayerMP player)
    {
        if (player != this.myEntity)
        {
            double xDistance = player.posX - accurate$lastScaledXPosition();
            double yDistance = player.posZ - accurate$lastScaledZPosition();

            if (xDistance >= (double)(-this.blocksDistanceThreshold) && xDistance <= (double)this.blocksDistanceThreshold && yDistance >= (double)(-this.blocksDistanceThreshold) && yDistance <= (double)this.blocksDistanceThreshold)
            {
                if (!this.trackingPlayers.contains(player) && (this.isPlayerWatchingThisChunk(player) || this.myEntity.forceSpawn))
                {
                    this.trackingPlayers.add(player);
                    Packet packet = this.func_151260_c();
                    player.playerNetServerHandler.sendPacket(packet);

                    if (!this.myEntity.getDataWatcher().getIsBlank())
                    {
                        player.playerNetServerHandler.sendPacket(new S1CPacketEntityMetadata(this.myEntity.getEntityId(), this.myEntity.getDataWatcher(), true));
                    }

                    if (this.myEntity instanceof EntityLivingBase)
                    {
                        ServersideAttributeMap
                                serversideattributemap = (ServersideAttributeMap)((EntityLivingBase)this.myEntity).getAttributeMap();
                        val collection = serversideattributemap.getWatchedAttributes();

                        if (!collection.isEmpty())
                        {
                            player.playerNetServerHandler.sendPacket(new S20PacketEntityProperties(this.myEntity.getEntityId(), collection));
                        }
                    }

                    this.motionX = this.myEntity.motionX;
                    this.motionY = this.myEntity.motionY;
                    this.motionZ = this.myEntity.motionZ;


                    int posX = MathHelper.floor_double(this.myEntity.posX * 32.0D);
                    int posY = MathHelper.floor_double(this.myEntity.posY * 32.0D);
                    int posZ = MathHelper.floor_double(this.myEntity.posZ * 32.0D);
                    if (posX != this.lastScaledXPosition || posY != this.lastScaledYPosition || posZ != this.lastScaledZPosition)
                    {
                        FMLNetworkHandler.makeEntitySpawnAdjustment(this.myEntity, player, this.lastScaledXPosition, this.lastScaledYPosition, this.lastScaledZPosition);
                    }

                    if (this.sendVelocityUpdates && !(packet instanceof S0FPacketSpawnMob))
                    {
                        player.playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(this.myEntity.getEntityId(), this.myEntity.motionX, this.myEntity.motionY, this.myEntity.motionZ));
                    }

                    if (this.myEntity.ridingEntity != null)
                    {
                        player.playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(0, this.myEntity, this.myEntity.ridingEntity));
                    }

                    if (this.myEntity instanceof EntityLiving && ((EntityLiving)this.myEntity).getLeashedToEntity() != null)
                    {
                        player.playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(1, this.myEntity, ((EntityLiving)this.myEntity).getLeashedToEntity()));
                    }

                    if (this.myEntity instanceof EntityLivingBase)
                    {
                        for (int i = 0; i < 5; ++i)
                        {
                            ItemStack itemstack = ((EntityLivingBase)this.myEntity).getEquipmentInSlot(i);

                            if (itemstack != null)
                            {
                                player.playerNetServerHandler.sendPacket(new S04PacketEntityEquipment(this.myEntity.getEntityId(), i, itemstack));
                            }
                        }
                    }

                    if (this.myEntity instanceof EntityPlayer)
                    {
                        EntityPlayer entityplayer = (EntityPlayer)this.myEntity;

                        if (entityplayer.isPlayerSleeping())
                        {
                            player.playerNetServerHandler.sendPacket(new S0APacketUseBed(entityplayer, MathHelper.floor_double(this.myEntity.posX), MathHelper.floor_double(this.myEntity.posY), MathHelper.floor_double(this.myEntity.posZ)));
                        }
                    }

                    if (this.myEntity instanceof EntityLivingBase)
                    {
                        EntityLivingBase entitylivingbase = (EntityLivingBase)this.myEntity;
                        val iterator = entitylivingbase.getActivePotionEffects().iterator();

                        while (iterator.hasNext())
                        {
                            PotionEffect potioneffect = (PotionEffect)iterator.next();
                            player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(this.myEntity.getEntityId(), potioneffect));
                        }
                    }
                    net.minecraftforge.event.ForgeEventFactory.onStartEntityTracking(myEntity, player);
                }
            }
            else if (this.trackingPlayers.contains(player))
            {
                this.trackingPlayers.remove(player);
                player.func_152339_d(this.myEntity);
                net.minecraftforge.event.ForgeEventFactory.onStopEntityTracking(myEntity, player);
            }
        }
    }
}
