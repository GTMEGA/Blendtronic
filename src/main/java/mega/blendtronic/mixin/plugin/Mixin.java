package mega.blendtronic.mixin.plugin;

import com.falsepattern.lib.mixin.IMixin;
import com.falsepattern.lib.mixin.ITargetedMod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Predicate;

import static com.falsepattern.lib.mixin.IMixin.PredicateHelpers.condition;
import static mega.blendtronic.config.MinecraftConfig.*;

@RequiredArgsConstructor
public enum Mixin implements IMixin {
    // @formatter:off

    //region Misc
    //region Common
    EntityPickupDropMixin                    (Side.COMMON, condition(() -> entityLivingDropLootOnDespawnMixin), "misc.EntityPickupDropMixin"),
    NullSafeGetBlockLightMixin               (Side.COMMON, condition(() -> worldGetBlockLightValueNpeMixin),    "misc.NullSafeGetBlockLightMixin"),
    WorldUnsafeGetBlockMixin                 (Side.COMMON, condition(() -> worldUnsafeGetBlockMixin),           "misc.WorldUnsafeGetBlockMixin"),
    WorldUpdateEntitiesRemoveAllMixin        (Side.COMMON, condition(() -> worldUpdateEntitiesRemoveAllMixin),  "misc.WorldUpdateEntitiesRemoveAllMixin"),
    //endregion Common
    //region Client
    GameOverFullscreenFix                    (Side.CLIENT, condition(() -> guiGameOverInitGuiMixin),            "misc.GameOverFullscreenFix"),
    //endregion Client
    //endregion Misc

    //region Netcode
    //region Common
    //region Packets
    Netcode_S0CPacketSpawnPlayerMixin        (Side.COMMON, condition(() -> entityNetcodeImprovements),          "netcode.packets.S0CPacketSpawnPlayerMixin"),
    Netcode_S0EPacketSpawnObjectMixin        (Side.COMMON, condition(() -> entityNetcodeImprovements),          "netcode.packets.S0EPacketSpawnObjectMixin"),
    Netcode_S0FPacketSpawnMobMixin           (Side.COMMON, condition(() -> entityNetcodeImprovements),          "netcode.packets.S0FPacketSpawnMobMixin"),
    Netcode_S2CPacketSpawnGlobalEntityMixin  (Side.COMMON, condition(() -> entityNetcodeImprovements),          "netcode.packets.S2CPacketSpawnGlobalEntityMixin"),
    Netcode_S11PacketSpawnExperienceOrbMixin (Side.COMMON, condition(() -> entityNetcodeImprovements),          "netcode.packets.S11PacketSpawnExperienceOrbMixin"),
    Netcode_S12PacketEntityVelocityMixin     (Side.COMMON, condition(() -> entityNetcodeImprovements),          "netcode.packets.S12PacketEntityVelocityMixin"),
    Netcode_S14PacketEntityMixin             (Side.COMMON, condition(() -> entityNetcodeImprovements),          "netcode.packets.S14PacketEntityMixin"),
    Netcode_S15PacketEntityRelMoveMixin      (Side.COMMON, condition(() -> entityNetcodeImprovements),          "netcode.packets.S15PacketEntityRelMoveMixin"),
    Netcode_S16PacketEntityLookMixin         (Side.COMMON, condition(() -> entityNetcodeImprovements),          "netcode.packets.S16PacketEntityLookMixin"),
    Netcode_S17PacketEntityLookMoveMixin     (Side.COMMON, condition(() -> entityNetcodeImprovements),          "netcode.packets.S17PacketEntityLookMoveMixin"),
    Netcode_S18PacketEntityTeleportMixin     (Side.COMMON, condition(() -> entityNetcodeImprovements),          "netcode.packets.S18PacketEntityTeleportMixin"),
    Netcode_S19PacketEntityHeadLookMixin     (Side.COMMON, condition(() -> entityNetcodeImprovements),          "netcode.packets.S19PacketEntityHeadLookMixin"),
    //endregion Packets
    Netcode_EntityTrackerEntityMixin         (Side.COMMON, condition(() -> entityNetcodeImprovements),          "netcode.EntityTrackerEntityMixin"),
    //endregion Common
    //region Client
    Netcode_EntityMixin                      (Side.CLIENT, condition(() -> entityNetcodeImprovements),          "netcode.EntityMixin"),
    Netcode_NetHandlerPlayClientMixin        (Side.CLIENT, condition(() -> entityNetcodeImprovements),          "netcode.NetHandlerPlayClientMixin"),
    //endregion Client
    //endregion Netcode

    // @formatter:on
    ;

    @Getter
    private final Side side;
    @Getter
    private final Predicate<List<ITargetedMod>> filter;
    @Getter
    private final String mixin;
}
