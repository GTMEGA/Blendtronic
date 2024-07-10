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

package mega.blendtronic.mixin.mixins.common.fastcraft;

import lombok.val;
import net.minecraft.crash.CrashReport;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CrashReport.class)
public abstract class CrashReportMixin {
    @Redirect(method = "<init>",
              at = @At(value = "INVOKE",
                       target = "Lfastcraft/H;ar(Lnet/minecraft/crash/CrashReport;Ljava/lang/String;Ljava/lang/Throwable;)V",
                       remap = false),
              require = 0,
              expect = 0)
    @Dynamic
    private void noHook(CrashReport report, String str, Throwable t) {
        try {
            val H = Class.forName("fastcraft.H");
            val m = H.getDeclaredMethod("ar", CrashReport.class, String.class, Throwable.class);
            m.setAccessible(true);
            m.invoke(null, report, str, t);
        } catch (Throwable tt) {
            tt.printStackTrace();
        }
    }
}
