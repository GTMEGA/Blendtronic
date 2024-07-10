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

package mega.blendtronic.modules.optifine;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import mega.blendtronic.Share;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShaderMappingFix {
    private static final ShaderMappingFix INSTANCE = new ShaderMappingFix();

    private static Field field$Shaders_shaderPack;

    private static Method method$BlockAliases_update;

    public static void init() {
        if (!FMLClientHandler.instance().hasOptifine())
            return;

        //Preliminary check passed, populate reflection containers
        try {
            val class$Shaders = Class.forName("shadersmod.client.Shaders");
            val class$IShaderPack = Class.forName("shadersmod.client.IShaderPack");
            val class$BlockAliases = Class.forName("shadersmod.client.BlockAliases");

            field$Shaders_shaderPack = class$Shaders.getDeclaredField("shaderPack");
            field$Shaders_shaderPack.setAccessible(true);

            method$BlockAliases_update = class$BlockAliases.getDeclaredMethod("update", class$IShaderPack);
            method$BlockAliases_update.setAccessible(true);
        } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException e) {
            Share.LOG.error("Could not detect shadersmod, even though OptiFine is installed.\n" +
                            "Are you running an ancient OptiFine?", e);
            return;
        }

        FMLCommonHandler.instance().bus().register(INSTANCE);
    }

    @SubscribeEvent
    public void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent e) {
        try {
            method$BlockAliases_update.invoke(null, field$Shaders_shaderPack.get(null));
        } catch (IllegalAccessException | InvocationTargetException ex) {
            Share.LOG.error("Reloading shader aliases failed!", ex);
        }
    }
}
