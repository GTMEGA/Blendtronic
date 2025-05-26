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

package mega.blendtronic.mixin.mixins.common.fancyfurnace;

import lombok.val;
import mega.blendtronic.modules.fancyfurnace.FancyFurnaceUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.world.World;

import static mega.blendtronic.modules.fancyfurnace.FancyFurnaceUtil.META_FULL_BIT;
import static mega.blendtronic.modules.fancyfurnace.FancyFurnaceUtil.META_ROT_BITS;

@Mixin(TileEntityFurnace.class)
public abstract class TileEntityFurnaceMixin extends TileEntity implements ISidedInventory {
    @Shadow
    private ItemStack[] furnaceItemStacks;

    @Inject(method = "updateEntity",
            at = @At("HEAD"),
            require = 1)
    private void updateVisualsOnSlotChange(CallbackInfo ci) {
        if (!worldObj.isRemote) {
            val meta = worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
            boolean hasItemVisual = (meta & META_FULL_BIT) != 0;
            boolean hasItem = furnaceItemStacks[2] != null;
            if (hasItem != hasItemVisual) {
                worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, (meta & META_ROT_BITS) | (hasItem ? META_FULL_BIT : 0), 2);
            }
        }
    }

    @Inject(method = "updateEntity",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/block/BlockFurnace;updateFurnaceBlockState(ZLnet/minecraft/world/World;III)V"),
            require = 1)
    private void updateBlockStateFancy(CallbackInfo ci) {
        FancyFurnaceUtil.updateFurnaceBlockState((TileEntityFurnace) (Object) this, worldObj, xCoord, yCoord, zCoord);
    }

    @Redirect(method = "updateEntity",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/block/BlockFurnace;updateFurnaceBlockState(ZLnet/minecraft/world/World;III)V"),
              require = 1)
    private void updateBlockStateSuppressOriginal(boolean p_149931_0_, World p_149931_1_, int p_149931_2_, int p_149931_3_, int p_149931_4_) {

    }
}
