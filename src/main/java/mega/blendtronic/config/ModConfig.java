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

package mega.blendtronic.config;

import com.falsepattern.lib.config.Config;
import com.falsepattern.lib.config.ConfigurationManager;
import mega.blendtronic.Tags;

@Config(modid = Tags.MOD_ID, category = "mods")
public final class ModConfig {
    @Config.Comment("[CLIENT]{OptiFine} Fix modded PBRs and connected textures when switching worlds")
    @Config.DefaultBoolean(true)
    public static boolean optiFineShaderMappingFix;

    @Config.Comment("[CLIENT]{OptiFine} Fix sun angle not updating without shadow pass")
    @Config.DefaultBoolean(true)
    public static boolean optiFineSunAngleFix;

    @Config.Comment("[SERVER]{ExtraUtilities} Removes checking the render type of blocks to determine if they are a fence, by checking render type.\n" +
                    "This may make certain modded fences which do not extend the base Minecraft fence to not function as perimeter blocks for the Ender Quarry.\n" +
                    "This *usually* has no side effects as the code is often present both server and client side, but mods which will run both\n" +
                    "should *NEVER* touch render code in any way shape or form.\n" +
                    "LORE: Was crashing with RPLE of all things, how did we even get this far without it being noticed???")
    @Config.DefaultBoolean(true)
    public static boolean extraUtilitiesBlockBreakingRegistryFix;

    @Config.Comment("[SERVER]{BuildCraft} Improves the Chute (Original Hopper) to pull items from the top at the same speed it can push items from the bottom.\n" +
                    "LORE: Added this as in MEGA they are expensive to craft and should be at least worth the cost.")
    @Config.DefaultBoolean(true)
    public static boolean buildCraftChuteImprovements;

    @Config.Comment("[BOTH] Makes IC2 Chargepads aware of bauble slots")
    @Config.DefaultBoolean(true)
    @Config.NoSync
    public static boolean ic2ChargepadBaubleSupport;

    static {
        ConfigurationManager.selfInit();
    }
}
