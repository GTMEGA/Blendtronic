package mega.blendtronic.config;

import com.falsepattern.lib.config.Config;
import com.falsepattern.lib.config.ConfigurationManager;
import mega.blendtronic.Tags;

@Config(modid = Tags.MODID, category = "mods")
public final class ModConfig {
    @Config.Comment("[CLIENT]{OptiFine} Fix modded PBRs and connected textures when switching worlds")
    @Config.DefaultBoolean(true)
    public static boolean optiFineShaderMappingFix;

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

    static {
        ConfigurationManager.selfInit();
    }
}
