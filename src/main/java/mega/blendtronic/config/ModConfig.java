package mega.blendtronic.config;

import com.falsepattern.lib.config.Config;
import com.falsepattern.lib.config.ConfigurationManager;
import mega.blendtronic.Tags;

@Config(modid = Tags.MODID,
        category = "mods")
public class ModConfig {

    @Config.Comment("[CLIENT]{OptiFine} Fix modded PBRs and connected textures when switching worlds")
    @Config.DefaultBoolean(true)
    public static boolean optiFineShaderMappingFix;

    static {
        ConfigurationManager.selfInit();
    }
}
