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

import baubles.common.lib.PlayerHandler;
import ic2.core.block.wiring.*;
import lombok.val;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {TileEntityChargepadBatBox.class,
                TileEntityChargepadCESU.class,
                TileEntityChargepadMFE.class,
                TileEntityChargepadMFSU.class},
       remap = false)
public abstract class IC2ChargepadBaubleSupportMixin extends TileEntityChargepadBlock {
    public IC2ChargepadBaubleSupportMixin(int tier, int output, int maxStorage) {
        super(tier, output, maxStorage);
    }

    @Inject(method = "getItems",
            at = @At("RETURN"),
            require = 1)
    private void injectBaublesCharge(EntityPlayer player, CallbackInfo ci) {
        val baubles = PlayerHandler.getPlayerBaubles(player);
        for (val bauble : baubles.stackList) {
            if (bauble != null) {
                chargeitems(bauble, getOutput());
                baubles.markDirty();
            }
        }
    }
}
