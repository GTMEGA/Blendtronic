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

package mega.blendtronic.proxy;


import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import mega.blendtronic.Tags;
import mega.blendtronic.modules.cauldrontank.TileCauldron;

import static mega.blendtronic.config.MinecraftConfig.cauldronTank;
import static mega.blendtronic.config.MinecraftConfig.extendedTileEntity;

public abstract class CommonProxy {
    public void construct(FMLConstructionEvent e) {}

    public void preInit(FMLPreInitializationEvent e) {
        if (cauldronTank && extendedTileEntity)
            GameRegistry.registerTileEntity(TileCauldron.class, Tags.MOD_ID + "_cauldron");
    }

    public void init(FMLInitializationEvent e) {}

    public void postInit(FMLPostInitializationEvent e) {}
}

