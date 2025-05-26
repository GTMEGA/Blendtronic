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

package mega.blendtronic.mixin.mixins.common.netcode;

import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.inventory.Slot;
import net.minecraft.network.NetHandlerPlayServer;

@Mixin(NetHandlerPlayServer.class)
public class NetHandlerPlayServerMixin {

    @Inject(method = "processPlayerBlockPlacement",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/inventory/Container;detectAndSendChanges()V"),
            require = 1,
            cancellable = true)
    private void onProcessPlayerBlockPlacement(CallbackInfo ci, @Local(ordinal = 0) Slot slot) {
        if (slot == null)
            ci.cancel();
    }
}
