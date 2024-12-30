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

package mega.blendtronic.mixin.mixins.client.misc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.audio.SoundPoolEntry;

@Mixin(SoundManager.class)
public class SoundManagerCrashFix {
    @Redirect(method = "getNormalizedPitch",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/client/audio/SoundPoolEntry;getPitch()D"),
              require = 1)
    private double nullSafeGetPitch(SoundPoolEntry instance) {
        return instance == null ? 0 : instance.getPitch();
    }

    @Redirect(method = "getNormalizedVolume",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/client/audio/SoundPoolEntry;getVolume()D"),
              require = 1)
    private double nullSafeGetVolume(SoundPoolEntry instance) {
        return instance == null ? 0 : instance.getVolume();
    }
}
