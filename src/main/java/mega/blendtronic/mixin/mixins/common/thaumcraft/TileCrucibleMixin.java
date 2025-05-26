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

package mega.blendtronic.mixin.mixins.common.thaumcraft;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.common.tiles.TileCrucible;

@Mixin(value = TileCrucible.class)
public class TileCrucibleMixin extends TileThaumcraft {

    /**
     * @author bot
     * @reason method call causes a crash and should not be run at all
     */
    @Overwrite(remap = false)
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (resource != null && resource.getFluidID() != FluidRegistry.WATER.getID()) {
            return 0;
        } else {
            if (doFill) {
                this.worldObj.markBlockForUpdate(this.xCoord,this.yCoord,this.zCoord);
            }
            return this.tank.fill(resource, doFill);
        }
    }

    @Shadow(remap = false)
    public FluidTank tank;
}
