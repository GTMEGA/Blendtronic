package com.github.gtmega.blendtronic;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class Config {
    
    private static class Defaults {
        public static final boolean entityLivingDropLootOnDespawnMixin = true;
        public static final boolean guiGameOverInitGuiMixin = true;
        public static final boolean tesselatorCardinalBuffIndexMixin = true;
        public static final boolean worldGetBlockLightValueNpeMixin = true;
        public static final boolean worldUnsafeGetBlockMixin = true;
    }

    private static class Categories {
        public static final String general = "general";
        public static final String minecraft = "minecraft";
    }

    public static boolean entityLivingDropLootOnDespawnMixin = Defaults.entityLivingDropLootOnDespawnMixin;
    public static boolean guiGameOverInitGuiMixin = Defaults.guiGameOverInitGuiMixin;
    public static boolean tesselatorCardinalBuffIndexMixin = Defaults.tesselatorCardinalBuffIndexMixin;
    public static boolean worldGetBlockLightValueNpeMixin = Defaults.worldGetBlockLightValueNpeMixin;
    public static boolean worldUnsafeGetBlockMixin = Defaults.worldUnsafeGetBlockMixin;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);
        configuration.load();

        Property entityLivingDropLootOnDespawnMixinProperty = configuration.get(Categories.minecraft, "entityLivingDropLootOnDespawnMixin",
                Defaults.entityLivingDropLootOnDespawnMixin, "[BOTH] Allows mobs that picked up player loot to despawn and drop that loot.");
        entityLivingDropLootOnDespawnMixin = entityLivingDropLootOnDespawnMixinProperty.getBoolean();

        Property guiGameOverInitGuiMixinProperty = configuration.get(Categories.minecraft, "guiGameOverInitGuiMixin",
                Defaults.guiGameOverInitGuiMixin, "[CLIENT] Resets GameOver ui after switching to fullscreen to fix buttons disabling.");
        guiGameOverInitGuiMixin = guiGameOverInitGuiMixinProperty.getBoolean();

        Property tesselatorCardinalBuffIndexMixinProperty = configuration.get(Categories.minecraft, "tesselatorCardinalBuffIndexMixin",
                Defaults.tesselatorCardinalBuffIndexMixin, "[CLIENT] Fixes crash when tesselator buffer index becomes negative or zero.");
        tesselatorCardinalBuffIndexMixin = tesselatorCardinalBuffIndexMixinProperty.getBoolean();

        Property worldGetBlockLightValueNpeMixinProperty = configuration.get(Categories.minecraft, "worldGetBlockLightValueNpeMixin",
                Defaults.worldGetBlockLightValueNpeMixin, "[BOTH] Fixes NullPointerExceptions caused by getBlockLightValue returning NULL.");
        worldGetBlockLightValueNpeMixin = worldGetBlockLightValueNpeMixinProperty.getBoolean();

        Property worldUnsafeGetBlockMixinProperty = configuration.get(Categories.minecraft, "worldUnsafeGetBlockMixin",
                Defaults.worldUnsafeGetBlockMixin, "[BOTH] Fixes Mojang's null-unsafe World.class getBlock.");
        worldUnsafeGetBlockMixin = worldUnsafeGetBlockMixinProperty.getBoolean();

        if(configuration.hasChanged()) {
            configuration.save();
        }
    }
}