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

package mega.blendtronic.mixin.mixins.client.shaders;

import lombok.val;
import mega.blendtronic.modules.shaders.stubpackage.shadersmod.client.Shaders;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shadersmod.client.ShadersRender;

import net.minecraft.client.renderer.EntityRenderer;

@Mixin(value = ShadersRender.class,
       remap = false)
public abstract class OptiFineSunAngle_ShadersRenderMixin {
    @Inject(method = "renderShadowMap",
            at = @At("HEAD"))
    private static void updateSunAngleNoShadows(EntityRenderer entityRenderer, int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
        if (Shaders.usedShadowDepthBuffers == 0) {
            val celestialAngle = Shaders.mc.theWorld.getCelestialAngle(partialTicks);
            Shaders.celestialAngle = celestialAngle;
            Shaders.sunAngle = celestialAngle < 0.75F ? celestialAngle + 0.25F : celestialAngle - 0.75F;
        }
    }
}
