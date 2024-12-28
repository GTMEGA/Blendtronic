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

@Config(modid = Tags.MOD_ID, category = "minecraft")
public final class MinecraftConfig {
    @Config.Comment("[BOTH] Allows mobs that picked up player loot to despawn and drop that loot.")
    @Config.DefaultBoolean(true)
    public static boolean entityLivingDropLootOnDespawnMixin;

    @Config.Comment("[BOTH] Fixes NullPointerExceptions caused by getBlockLightValue returning NULL.")
    @Config.DefaultBoolean(true)
    public static boolean worldGetBlockLightValueNpeMixin;

    @Config.Comment("[BOTH] Fixes Mojang's null-unsafe World.class getBlock.")
    @Config.DefaultBoolean(true)
    public static boolean worldUnsafeGetBlockMixin;

    @Config.Comment("[BOTH] Reduces lag spike when toRemoveTileEntities is large\n" +
                    "Note: Automatically disabled if FastCraft is present, it has its own patch for this.")
    @Config.DefaultBoolean(true)
    public static boolean worldUpdateEntitiesRemoveAllMixin;

    @Config.Comment("[SERVER] Redirects boat collision damage to the player instead of popping the boat.\n" +
                    "LORE: Added this cause I wanted drunk driving in Minecraft.")
    @Config.DefaultBoolean(true)
    public static boolean boatDamageRedirect;

    @Config.Comment("[CLIENT] Resets GameOver ui after switching to fullscreen to fix buttons disabling.")
    @Config.DefaultBoolean(true)
    public static boolean guiGameOverInitGuiMixin;

    @Config.Comment("[CLIENT] Fixes an occasional game crash caused by OpenAL linking errors")
    @Config.DefaultBoolean(true)
    public static boolean openALNativeCrashFix;

    @Config.Comment("[CLIENT] Fixes an occasional game crash caused by the sound pool becoming inconsistent")
    @Config.DefaultBoolean(true)
    public static boolean soundManagerCrashFix;

    @Config.Comment("[BOTH] Makes entity netcode more precise, fixing some rubber-banding and bouncing items.\n" +
                    "NOTE: This needs to be enabled both serverside and clientside, otherwise it leads to a crash when trying to join multiplayer!")
    @Config.DefaultBoolean(true)
    public static boolean entityNetcodeImprovements;

    @Config.Comment("[BOTH] Implements the ExtendedTileEntity interface on top of TileEntity.\n" +
                    "This does nothing on it's own, but other fixes may depend on it.")
    @Config.DefaultBoolean(true)
    public static boolean extendedTileEntity;

    @Config.Comment("[BOTH] Adds a Tile Entity tank to the Minecraft cauldron, this tile does not tick but extends it's functionality.\n" +
                    "It implements the Forge IFluidHandler interface, which allows mods to interact with it like a regular tank.\n" +
                    "The internal tank stores 750mb of water, a bucket can still refill it as usual.\n" +
                    "However, draining is disabled. This is to prevent the cauldron to be used as a cheap water buffer.\n" +
                    "Block metadata will be equivalent to (storedFluidAmount / 250) rounded to an int. \n" +
                    "So: [150mb -> meta 0], [300mb -> meta 1] and [750mb -> meta 3] etc. \n" +
                    "Thus vanilla behaviour and mod interactions are practically the same, a full bucket will still fill it to the top and bottles work the same etc.\n" +
                    "And so does washing GregTech dust, as all these interactions will do is read & update the meta value.\n" +
                    "REQUIREMENTS: extendedTileEntity=true\n" +
                    "LORE: Added this when I was still a slow-ass pre steam, but couldn't be fucked to fill a cauldron by hand.")
    @Config.DefaultBoolean(true)
    public static boolean cauldronTank;

    @Config.Comment("[SERVER] Replaces the world generator integer cache with a more thread safe and optimized variant.")
    @Config.DefaultBoolean(true)
    public static boolean newIntCache;

    @Config.Comment("[CLIENT] Allows switching tools when breaking blocks")
    @Config.DefaultBoolean(true)
    public static boolean allowToolSwitchOnBlockBreak;

    @Config.Comment("[SERVER] Whether or not enable faster leaf decay. Decay speed is controlled by Decay Speed and Fuzz")
    @Config.DefaultBoolean(true)
    public static boolean FAST_LEAF_DECAY;

    @Config.Comment("[SERVER] The amount of ticks every leave needs to decay (Lower is faster)")
    @Config.DefaultInt(21)
    @Config.NoSync
    public static int DECAY_SPEED;

    @Config.Comment({"[SERVER] A random number from 0 to (this config value) will be added to the decay speed for every leaf block.",
                     "Setting this to 0 will decay leaves rather linear while higher numbers will let the whole thing look more natural"})
    @Config.DefaultInt(15)
    @Config.NoSync
    public static int DECAY_FUZZ;

    static {
        ConfigurationManager.selfInit();
    }
}
