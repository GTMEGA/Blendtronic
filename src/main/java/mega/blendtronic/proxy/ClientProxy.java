package mega.blendtronic.proxy;

import mega.blendtronic.config.ModConfig;
import mega.blendtronic.modules.optifine.ShaderMappingFix;

import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {
    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        if (ModConfig.optiFineShaderMappingFix) {
            ShaderMappingFix.init();
        }
    }
}
