package mega.blendtronic.config;

import com.falsepattern.lib.config.Config;
import com.falsepattern.lib.config.ConfigurationManager;
import mega.blendtronic.Tags;

@Config(modid = Tags.MODID,
        category = "minecraft")
public class MinecraftConfig {
    @Config.Comment("[BOTH] Allows mobs that picked up player loot to despawn and drop that loot.")
    @Config.DefaultBoolean(true)
    public static boolean entityLivingDropLootOnDespawnMixin;

    @Config.Comment("[CLIENT] Resets GameOver ui after switching to fullscreen to fix buttons disabling.")
    @Config.DefaultBoolean(true)
    public static boolean guiGameOverInitGuiMixin;

    @Config.Comment("[BOTH] Fixes NullPointerExceptions caused by getBlockLightValue returning NULL.")
    @Config.DefaultBoolean(true)
    public static boolean worldGetBlockLightValueNpeMixin;

    @Config.Comment("[BOTH] Fixes Mojang's null-unsafe World.class getBlock.")
    @Config.DefaultBoolean(true)
    public static boolean worldUnsafeGetBlockMixin;

    @Config.Comment("[BOTH] Reduces lag spike when toRemoveTileEntities is large")
    @Config.DefaultBoolean(true)
    public static boolean worldUpdateEntitiesRemoveAllMixin;

    @Config.Comment("[CLIENT] Fixes an occasional game crash caused by OpenAL linking errors")
    @Config.DefaultBoolean(true)
    public static boolean openALNativeCrashFix;

    @Config.Comment("[BOTH] Makes entity netcode more precise, fixing some rubber-banding and bouncing items.\n" +
                    "NOTE: This needs to be enabled both serverside and clientside, otherwise it leads to a crash when trying to join multiplayer!")
    @Config.DefaultBoolean(true)
    public static boolean entityNetcodeImprovements;

    static {
        ConfigurationManager.selfInit();
    }
}
