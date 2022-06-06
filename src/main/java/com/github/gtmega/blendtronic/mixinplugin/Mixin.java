package com.github.gtmega.blendtronic.mixinplugin;

import cpw.mods.fml.relauncher.FMLLaunchHandler;

import java.util.Arrays;
import java.util.List;

import static com.github.gtmega.blendtronic.mixinplugin.TargetedMod.*;

/**
 *  IMPORTANT: Do not make any references to any mod from this file. This file is loaded quite early on and if
 *  you refer to other mods you load them as well. The consequence is: You can't inject any previously loaded classes!
 *  Exception: Tags.java, as long as it is used for Strings only!
 */
public enum Mixin {

    // Minecraft
    ENTITY_LIVING_DROP_LOOT_ON_DESPAWN_MIXIN("minecraft.EntityPickupDropMixin", Side.BOTH, VANILLA),
    GUI_GAME_OVER_INIT_GUI_MIXIN("minecraft.GameOverFullscreenFix", Side.CLIENT, VANILLA),
    TESSELATOR_CARDINAL_BUFF_INDEX_MIXIN("minecraft.TesselatorCardinalBuffIndexMixin", Side.CLIENT, VANILLA),
    WORLD_GET_BLOCK_LIGHT_VALUE_NPE_MIXIN("minecraft.NullSafeGetBlockLightMixin", Side.BOTH, VANILLA),
    WORLD_UNSAFE_GET_BLOCK_MIXIN("minecraft.WorldUnsafeGetBlockMixin", Side.BOTH, VANILLA),
    WORLD_UPDATE_ENTITIES_REMOVE_ALL_MIXIN("minecraft.WorldUpdateEntitiesRemoveAllMixin",Side.BOTH,VANILLA);

    public final String mixinClass;
    public final List<TargetedMod> targetedMods;
    private final Side side;

    Mixin(String mixinClass, Side side, TargetedMod... targetedMods) {
        this.mixinClass = mixinClass;
        this.targetedMods = Arrays.asList(targetedMods);
        this.side = side;
    }

    public boolean shouldLoad(List<TargetedMod> loadedMods) {
        return (side == Side.BOTH
                || side == Side.SERVER && FMLLaunchHandler.side().isServer()
                || side == Side.CLIENT && FMLLaunchHandler.side().isClient())
                && loadedMods.containsAll(targetedMods);
    }
}

enum Side {
    BOTH,
    CLIENT,
    SERVER;
}
