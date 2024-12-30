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

package mega.blendtronic.mixin.plugin;

import com.falsepattern.lib.mixin.IMixin;
import com.falsepattern.lib.mixin.ITargetedMod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Predicate;

import static com.falsepattern.lib.mixin.IMixin.PredicateHelpers.*;
import static com.falsepattern.lib.mixin.IMixin.Side.CLIENT;
import static com.falsepattern.lib.mixin.IMixin.Side.COMMON;
import static mega.blendtronic.config.MinecraftConfig.*;
import static mega.blendtronic.config.ModConfig.buildCraftChuteImprovements;
import static mega.blendtronic.config.ModConfig.extraUtilitiesBlockBreakingRegistryFix;
import static mega.blendtronic.config.ModConfig.optiFineSunAngleFix;
import static mega.blendtronic.mixin.plugin.TargetedMod.*;

@RequiredArgsConstructor
public enum Mixin implements IMixin {
    // region Misc
    common_misc_EntityPickupDropMixin(COMMON, condition(() -> entityLivingDropLootOnDespawnMixin), "misc.EntityPickupDropMixin"),
    common_misc_NullSafeGetBlockLightMixin(COMMON, condition(() -> worldGetBlockLightValueNpeMixin), "misc.NullSafeGetBlockLightMixin"),
    common_misc_WorldUnsafeGetBlockMixin(COMMON, condition(() -> worldUnsafeGetBlockMixin), "misc.WorldUnsafeGetBlockMixin"),
    common_misc_WorldUpdateEntitiesRemoveAllMixin(COMMON, avoid(FASTCRAFT).and(condition(() -> worldUpdateEntitiesRemoveAllMixin)), "misc.WorldUpdateEntitiesRemoveAllMixin"),
    common_misc_BoatDamageRedirectMixin(COMMON, condition(() -> boatDamageRedirect), "misc.BoatDamageRedirectMixin"),
    common_misc_DietHopper_BlockHopperMixin(COMMON, condition(() -> dietHoppers), "misc.DietHopper_BlockHopperMixin"),

    client_misc_GameOverFullscreenFix(CLIENT, condition(() -> guiGameOverInitGuiMixin), "misc.GameOverFullscreenFix"),
    client_misc_OpenALNativeCrashFix(CLIENT, condition(() -> openALNativeCrashFix), "misc.OpenALNativeCrashFix"),
    client_misc_SoundManagerCrashFix(CLIENT, condition(() -> soundManagerCrashFix), "misc.SoundManagerCrashFix"),
    client_misc_AllowToolSwitchMiningMixin(CLIENT, condition(() -> allowToolSwitchOnBlockBreak), "misc.AllowToolSwitchMiningMixin"),
    // endregion

    // region Netcode
    common_netcode_S0CPacketSpawnPlayerMixin(COMMON, condition(() -> entityNetcodeImprovements), "netcode.packets.S0CPacketSpawnPlayerMixin"),
    common_netcode_S0EPacketSpawnObjectMixin(COMMON, condition(() -> entityNetcodeImprovements), "netcode.packets.S0EPacketSpawnObjectMixin"),
    common_netcode_S0FPacketSpawnMobMixin(COMMON, condition(() -> entityNetcodeImprovements), "netcode.packets.S0FPacketSpawnMobMixin"),
    common_netcode_S2CPacketSpawnGlobalEntityMixin(COMMON, condition(() -> entityNetcodeImprovements), "netcode.packets.S2CPacketSpawnGlobalEntityMixin"),
    common_netcode_S11PacketSpawnExperienceOrbMixin(COMMON, condition(() -> entityNetcodeImprovements), "netcode.packets.S11PacketSpawnExperienceOrbMixin"),
    common_netcode_S12PacketEntityVelocityMixin(COMMON, condition(() -> entityNetcodeImprovements), "netcode.packets.S12PacketEntityVelocityMixin"),
    common_netcode_S14PacketEntityMixin(COMMON, condition(() -> entityNetcodeImprovements), "netcode.packets.S14PacketEntityMixin"),
    common_netcode_S15PacketEntityRelMoveMixin(COMMON, condition(() -> entityNetcodeImprovements), "netcode.packets.S15PacketEntityRelMoveMixin"),
    common_netcode_S16PacketEntityLookMixin(COMMON, condition(() -> entityNetcodeImprovements), "netcode.packets.S16PacketEntityLookMixin"),
    common_netcode_S17PacketEntityLookMoveMixin(COMMON, condition(() -> entityNetcodeImprovements), "netcode.packets.S17PacketEntityLookMoveMixin"),
    common_netcode_S18PacketEntityTeleportMixin(COMMON, condition(() -> entityNetcodeImprovements), "netcode.packets.S18PacketEntityTeleportMixin"),
    common_netcode_S19PacketEntityHeadLookMixin(COMMON, condition(() -> entityNetcodeImprovements), "netcode.packets.S19PacketEntityHeadLookMixin"),
    common_netcode_EntityTrackerEntityMixin(COMMON, condition(() -> entityNetcodeImprovements), "netcode.EntityTrackerEntityMixin"),

    common_netcode_EntityMixin(CLIENT, condition(() -> entityNetcodeImprovements), "netcode.EntityMixin"),
    common_netcode_NetHandlerPlayClientMixin(CLIENT, condition(() -> entityNetcodeImprovements), "netcode.NetHandlerPlayClientMixin"),
    // endregion

    // region Extended Tile Entity
    common_extendedtileentity_TileEntityMixin(COMMON, condition(() -> extendedTileEntity), "extendedtileentity.TileEntityMixin"),
    common_extendedtileentity_ChunkMixin(COMMON, condition(() -> extendedTileEntity), "extendedtileentity.ChunkMixin"),

    client_extendedtileentity_ChunkMixin(CLIENT, condition(() -> extendedTileEntity), "extendedtileentity.ChunkMixin"),
    // endregion

    // region Cauldron Tank
    common_cauldrontank_BlockCauldronMixin(COMMON, condition(() -> cauldronTank && extendedTileEntity), "cauldrontank.BlockCauldronMixin"),

    client_cauldrontank_RenderBlocksMixin(CLIENT, condition(() -> cauldronTank && extendedTileEntity), "cauldrontank.RenderBlocksMixin"),
    // endregion

    // region FastCraft
    common_fastcraft_FCCrashReportCategoryMixin(COMMON, require(FASTCRAFT), "fastcraft.CrashReportCategoryMixin"),
    common_fastcraft_FCCrashReportMixin(COMMON, require(FASTCRAFT), "fastcraft.CrashReportCategory"),
    // endregion

    // region Extra Utilities
    common_extrautils_BlockBreakingRegistryMixin(COMMON, require(EXTRA_UTILITIES).and(condition(() -> extraUtilitiesBlockBreakingRegistryFix)), "extrautils.BlockBreakingRegistryMixin"),
    // endregion

    // region BuildCraft
    common_buildcraft_TileHopperMixin(COMMON, require(BUILDCRAFT).and(condition(() -> buildCraftChuteImprovements)), "buildcraft.TileHopperMixin"),

    common_thaumcraft_TileCruicble(COMMON,require(THAUMCRAFT),"thaumcraft.TileCrucibleMixin"),
    // endregion

    // region BiblioCraft
    common_bibliocraft_FileUtilMixin(COMMON, require(BIBLIOCRAFT), "bibliocraft.FileUtilMixin"),

    common_bibliocraft_ServerPacketHandlerMixin(COMMON, require(BIBLIOCRAFT), "bibliocraft.ServerPacketHandlerMixin"),
    // endregion

    // region IntCache
    common_intcache_IntCacheMixin(COMMON, condition(() -> newIntCache), "intcache.IntCacheMixin"),
    common_intcache_WorldChunkManagerMixin(COMMON, condition(() -> newIntCache), "intcache.WorldChunkManagerMixin"),
    // endregion

    // region Shaders
    client_shaders_OptiFineSunAngle_ShadersRenderMixin(CLIENT, REQUIRE_OPTIFINE_WITH_SHADERS.and(condition(() -> optiFineSunAngleFix)), "shaders.OptiFineSunAngle_ShadersRenderMixin"),
    // endregion

    // region Fancy Furnace
    client_fancyfurnace_BlockFurnaceMixin(CLIENT, always(), "fancyfurnace.BlockFurnaceMixin"),
    common_fancyfurnace_TileEntityFurnace(COMMON, always(), "fancyfurnace.TileEntityFurnaceMixin"),
    // endregion

    // region Fast Leaf Decay
    common_FastLeafDecay_BlockLeavesBaseMixin(COMMON, always(), "FastLeafDecay.BlockLeavesBaseMixin"),
    // endregion
    ;

    @Getter
    private final Side side;
    @Getter
    private final Predicate<List<ITargetedMod>> filter;
    @Getter
    private final String mixin;
}
