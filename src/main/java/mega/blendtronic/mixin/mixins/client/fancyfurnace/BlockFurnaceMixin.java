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

package mega.blendtronic.mixin.mixins.client.fancyfurnace;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import static mega.blendtronic.modules.fancyfurnace.FancyFurnaceUtil.META_FULL_BIT;
import static mega.blendtronic.modules.fancyfurnace.FancyFurnaceUtil.META_ROT_BITS;

@Mixin(BlockFurnace.class)
public abstract class BlockFurnaceMixin extends BlockContainer {

    @Shadow
    @Final
    private boolean field_149932_b;
    @Shadow
    private IIcon field_149935_N;
    @Shadow
    private IIcon field_149936_O;
    @Unique
    private IIcon blend$fullIcon;

    protected BlockFurnaceMixin(Material material) {
        super(material);
    }

    /**
     * Gets the block's texture. Args: side, meta
     *
     * @author FalsePattern
     * @reason Need to hijack the icon logic
     */
    @Overwrite
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        if (side == 1) {
            return this.field_149935_N;
        }
        if (side == 0) {
            return this.field_149935_N;
        }
        if (side != (meta & META_ROT_BITS)) {
            return this.blockIcon;
        }
        if ((meta & META_FULL_BIT) != 0) {
            return this.blend$fullIcon;
        }
        return this.field_149936_O;
    }

    @Inject(method = "registerBlockIcons",
            at = @At("RETURN"),
            require = 1)
    @SideOnly(Side.CLIENT)
    private void registerIcons(IIconRegister reg, CallbackInfo ci) {
        blend$fullIcon = reg.registerIcon(field_149932_b ? "furnace_front_on_full" : "furnace_front_off_full");
    }

    @Redirect(method = "randomDisplayTick",
              at = @At(value = "INVOKE",
                       target = "Lnet/minecraft/world/World;getBlockMetadata(III)I"),
              require = 1)
    private int getMetaWithoutFull(World world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z) & META_ROT_BITS;
    }
}
