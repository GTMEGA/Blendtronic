package com.github.gtmega.blendtronic;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class Config {

    private static class Defaults {
        public static final boolean ENTITY_LIVING_DROP_LOOT_ON_DESPAWN_MIXIN = true;
        public static final boolean GUI_GAME_OVER_INIT_GUI_MIXIN = true;
        public static final boolean TESSELATOR_CARDINAL_BUFF_INDEX_MIXIN = true;
        public static final boolean WORLD_GET_BLOCK_LIGHT_VALUE_NPE_MIXIN = true;
        public static final boolean WORLD_UNSAFE_GET_BLOCK_MIXIN = true;
    }

    private static class Categories {
        public static final String GENERAL = "general";
        public static final String MINECRAFT = "minecraft";
    }

    public static boolean entityLivingDropLootOnDespawnMixin = Defaults.ENTITY_LIVING_DROP_LOOT_ON_DESPAWN_MIXIN;
    public static boolean guiGameOverInitGuiMixin = Defaults.GUI_GAME_OVER_INIT_GUI_MIXIN;
    public static boolean tesselatorCardinalBuffIndexMixin = Defaults.TESSELATOR_CARDINAL_BUFF_INDEX_MIXIN;
    public static boolean worldGetBlockLightValueNpeMixin = Defaults.WORLD_GET_BLOCK_LIGHT_VALUE_NPE_MIXIN;
    public static boolean worldUnsafeGetBlockMixin = Defaults.WORLD_UNSAFE_GET_BLOCK_MIXIN;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);
        configuration.load();

        Property entityLivingDropLootOnDespawnMixinProperty = configuration.get(Categories.MINECRAFT, "entityLivingDropLootOnDespawnMixin",
                Defaults.ENTITY_LIVING_DROP_LOOT_ON_DESPAWN_MIXIN, "[BOTH] Allows mobs that picked up player loot to despawn and drop that loot.");
        entityLivingDropLootOnDespawnMixin = entityLivingDropLootOnDespawnMixinProperty.getBoolean();

        Property guiGameOverInitGuiMixinProperty = configuration.get(Categories.MINECRAFT, "guiGameOverInitGuiMixin",
                Defaults.GUI_GAME_OVER_INIT_GUI_MIXIN, "[CLIENT] Resets GameOver ui after switching to fullscreen to fix buttons disabling.");
        guiGameOverInitGuiMixin = guiGameOverInitGuiMixinProperty.getBoolean();

        Property tesselatorCardinalBuffIndexMixinProperty = configuration.get(Categories.MINECRAFT, "tesselatorCardinalBuffIndexMixin",
                Defaults.TESSELATOR_CARDINAL_BUFF_INDEX_MIXIN, "[CLIENT] Fixes crash when tesselator buffer index becomes negative or zero.");
        tesselatorCardinalBuffIndexMixin = tesselatorCardinalBuffIndexMixinProperty.getBoolean();

        Property worldGetBlockLightValueNpeMixinProperty = configuration.get(Categories.MINECRAFT, "worldGetBlockLightValueNpeMixin",
                Defaults.WORLD_GET_BLOCK_LIGHT_VALUE_NPE_MIXIN, "[BOTH] Fixes NullPointerExceptions caused by getBlockLightValue returning NULL.");
        worldGetBlockLightValueNpeMixin = worldGetBlockLightValueNpeMixinProperty.getBoolean();

        Property worldUnsafeGetBlockMixinProperty = configuration.get(Categories.MINECRAFT, "worldUnsafeGetBlockMixin",
                Defaults.WORLD_UNSAFE_GET_BLOCK_MIXIN, "[BOTH] Fixes Mojang's null-unsafe World.class getBlock.");
        worldUnsafeGetBlockMixin = worldUnsafeGetBlockMixinProperty.getBoolean();

        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
