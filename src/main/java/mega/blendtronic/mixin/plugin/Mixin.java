package mega.blendtronic.mixin.plugin;

import com.falsepattern.lib.mixin.IMixin;
import com.falsepattern.lib.mixin.ITargetedMod;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import mega.blendtronic.config.MinecraftConfig;

import java.util.List;
import java.util.function.Predicate;

import static com.falsepattern.lib.mixin.IMixin.PredicateHelpers.condition;

@RequiredArgsConstructor
public enum Mixin implements IMixin {
    // @formatter:off

    //region Common
    //region Misc
    EntityPickupDropMixin(Side.COMMON, condition(() -> MinecraftConfig.entityLivingDropLootOnDespawnMixin), "misc.EntityPickupDropMixin"),
    NullSafeGetBlockLightMixin(Side.COMMON, condition(() -> MinecraftConfig.worldGetBlockLightValueNpeMixin), "misc.NullSafeGetBlockLightMixin"),
    WorldUnsafeGetBlockMixin(Side.COMMON, condition(() -> MinecraftConfig.worldUnsafeGetBlockMixin), "misc.WorldUnsafeGetBlockMixin"),
    WorldUpdateEntitiesRemoveAllMixin(Side.COMMON, condition(() -> MinecraftConfig.worldUpdateEntitiesRemoveAllMixin), "misc.WorldUpdateEntitiesRemoveAllMixin"),
    //endregion Misc
    //endregion Common

    //region Client
    //region Misc
    GameOverFullscreenFix(Side.CLIENT, condition(() -> MinecraftConfig.guiGameOverInitGuiMixin), "misc.GameOverFullscreenFix"),
    //endregion Misc
    //endregion Client

    // @formatter:on
    ;

    @Getter
    private final Side side;
    @Getter
    private final Predicate<List<ITargetedMod>> filter;
    @Getter
    private final String mixin;
}
