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

package mega.blendtronic.asm;

import com.falsepattern.lib.turboasm.MergeableTurboTransformer;
import lombok.SneakyThrows;
import lombok.val;
import mega.blendtronic.Tags;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.classloading.FluidIdTransformer;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.List;
import java.util.Map;

@IFMLLoadingPlugin.TransformerExclusions(Tags.ROOT_PKG + ".asm")
public class CoreLoadingPlugin implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{Tags.ROOT_PKG + ".asm.BlendtronicTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @SneakyThrows
    @Override
    public void injectData(Map<String, Object> data) {

        val f = LaunchClassLoader.class.getDeclaredField("transformers");
        f.setAccessible(true);

        @SuppressWarnings("unchecked")
        val transformers = (List<IClassTransformer>) f.get(Launch.classLoader);
        for (int i = 0; i < transformers.size(); i++) {
            val a = transformers.get(i);
            if (a instanceof FluidIdTransformer) {
                transformers.remove(i);
                break;
            }
        }
        MergeableTurboTransformer.mergeAllTurboTransformers();
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
