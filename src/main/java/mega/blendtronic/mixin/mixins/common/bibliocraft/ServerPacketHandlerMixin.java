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

package mega.blendtronic.mixin.mixins.common.bibliocraft;

import io.netty.buffer.ByteBuf;
import jds.bibliocraft.Config;
import jds.bibliocraft.items.ItemAtlas;
import jds.bibliocraft.network.ServerPacketHandler;
import jds.bibliocraft.tileentities.TileEntityFancySign;
import jds.bibliocraft.tileentities.TileEntityMapFrame;
import lombok.val;
import mega.blendtronic.Share;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEmptyMap;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.FMLNetworkEvent;

@Mixin(ServerPacketHandler.class)
public abstract class ServerPacketHandlerMixin {

    // Some methods don't pass in all the critical packet information like the player
    @Unique
    private static FMLNetworkEvent.ServerCustomPacketEvent bt$currentEvent;

    @Inject(method = "onServerPacket",
            at = @At("HEAD"),
            remap = false)
    private void storeEventInfo(FMLNetworkEvent.ServerCustomPacketEvent event, CallbackInfo c) {
        bt$currentEvent = event;
    }

    @Inject(method = "onServerPacket",
            at = @At("RETURN"),
            remap = false)
    private void freeEventInfo(FMLNetworkEvent.ServerCustomPacketEvent event, CallbackInfo c) {
        bt$currentEvent = null;
    }

    @Inject(method = "transferWaypointsToAtlas",
            at = @At("HEAD"),
            remap = false,
            cancellable = true)
    private void fixTransferWaypointsToAtlas(TileEntityMapFrame frameTile, ItemStack atlasStack,
                                             EntityPlayerMP player, CallbackInfo c) {
        if (!(atlasStack.getItem() instanceof ItemAtlas)) {
            this.bt$kickAndWarn(player, c, "BiblioAtlasGive");
        }
    }

    @Inject(method = "handleAtlasSwapUpdate",
            at = @At("HEAD"),
            remap = false,
            cancellable = true)
    private void fixHandleAtlasSwapUpdate(ByteBuf packet, EntityPlayerMP player, CallbackInfo c) {
        packet.markReaderIndex();
        final ItemStack insecureStack = ByteBufUtils.readItemStack(packet);
        if (insecureStack == null) return;
        packet.resetReaderIndex();

        NBTTagCompound tags = insecureStack.getTagCompound();
        if (tags == null) return;

        NBTTagList inventoryTagList = tags.getTagList("Inventory", 10);
        if (inventoryTagList == null) return;
        if (!player.worldObj.isRemote) {

            for (int i = 0; i < inventoryTagList.tagCount(); i++) {
                Item itemChecks = ItemStack.loadItemStackFromNBT(inventoryTagList.getCompoundTagAt(i)).getItem();

                if (!(itemChecks instanceof ItemMap || itemChecks instanceof ItemEmptyMap)) {
                    bt$kickAndWarn(player, c, "BiblioFrameGive");
                }
            }
        }
    }

    @Inject(method = "handleBookEdit",
            at = @At("HEAD"),
            remap = false,
            cancellable = true)
    private void fixHandleBookEdit(ByteBuf packet, EntityPlayerMP player, CallbackInfo c) {
        packet.markReaderIndex();
        ItemStack insecureStack = ByteBufUtils.readItemStack(packet);
        packet.resetReaderIndex();
        String namePrior = insecureStack.getDisplayName();
        insecureStack.func_135074_t();
        String nameAfter = insecureStack.getDisplayName();
        if (!namePrior.equals(nameAfter) && !Config.testBookValidity(insecureStack)) {
            this.bt$kickAndWarn(player, c, "BiblioTableGive");
        }
    }

    @Redirect(method = "handleFancySignUpdate",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/world/World;getTileEntity(III)Lnet/minecraft/tileentity/TileEntity;"),
              require = 1)
    private TileEntity handleFancySignUpdate(World world, int x, int y, int z) {
        val tile = world.getTileEntity(x, y, z);
        if (!(tile instanceof TileEntityFancySign)) {
            return null;
        }
        EntityPlayerMP player = ((NetHandlerPlayServer) bt$currentEvent.handler).playerEntity;
        PlayerInteractEvent pie = new PlayerInteractEvent(
                player,
                PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK,
                tile.xCoord,
                tile.yCoord,
                tile.zCoord,
                0,
                tile.getWorldObj());
        if (MinecraftForge.EVENT_BUS.post(pie)) {
            // Event cancelled by a world protection plugin or otherwise
            // Don't kick as there are legitimate ways this could happen (open a gui, then someone claims the chunk,
            // then exit gui)
            Share.LOG.warn("{} tried to edit a sign without permission", player.getGameProfile());
            return null;
        }
        return tile;
    }

    @Unique
    private void bt$kickAndWarn(EntityPlayerMP player, CallbackInfo c, String exploitName) {
        player.playerNetServerHandler.kickPlayerFromServer(player.getDisplayName() + " tried to cheat with \"" + exploitName + "\"-Exploit!");
        Share.LOG.error("{} tried to cheat with \"{}\"-Exploit!", player.getGameProfile(), exploitName);
        c.cancel();
    }
}
