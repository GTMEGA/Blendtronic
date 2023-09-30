package mega.blendtronic.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import lombok.NoArgsConstructor;
import mega.blendtronic.config.ModConfig;
import mega.blendtronic.modules.optifine.ShaderMappingFix;

@NoArgsConstructor
public final class ClientProxy extends CommonProxy {
    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        if (ModConfig.optiFineShaderMappingFix) {
            ShaderMappingFix.init();
        }
    }
}
