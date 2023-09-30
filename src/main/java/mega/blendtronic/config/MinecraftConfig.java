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

    @Config.Comment("[BOTH] Makes entity netcode more precise, fixing some rubber-banding and bouncing items.\n" +
                    "NOTE: This needs to be enabled both serverside and clientside, otherwise it leads to a crash when trying to join multiplayer!")
    @Config.DefaultBoolean(true)
    public static boolean entityNetcodeImprovements;

    @Config.Comment("[SERVER] Stops ExtraUtilities from checking the render type of blocks to determine if they are a fence, by checking render type.\n" +
                    "This *usually* has no side effects as the code is often present both server and client side, but mods which will run both\n" +
                    "should *NEVER* touch render code in any way shape or form.\n" +
                    "LORE: Was crashing with RPLE of all things, how did we even get this far without it being noticed???")
    @Config.DefaultBoolean(true)
    public static boolean extraUtilitiesBlockBreakingRegistryFix;

    @Config.Comment("[SERVER] Improves the BuildCraft Chute (Original Hopper) to pull items from the top at the same speed it can push items from the bottom.\n" +
                    "LORE: Added this as in MEGA they are expensive to craft and should be at least worth the cost.")
    @Config.DefaultBoolean(true)
    public static boolean buildCraftChuteImprovements;

    static {
        ConfigurationManager.selfInit();
    }
}
