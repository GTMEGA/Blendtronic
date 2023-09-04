package mega.blendtronic.modules.optifine;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import mega.blendtronic.Share;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ShaderMappingFix {

    private static Field field$Shaders_shaderPack;

    private static Method method$BlockAliases_update;

    public static void init() {
        if (!FMLClientHandler.instance().hasOptifine())
            return;

        //Preliminary check passed, populate reflection containers
        try {
            val class$Shaders = Class.forName("shadersmod.client.Shaders");
            val class$IShaderPack = Class.forName("shadersmod.client.IShaderPack");
            val class$BlockAliases = Class.forName("shadersmod.client.BlockAliases");

            field$Shaders_shaderPack = class$Shaders.getDeclaredField("shaderPack");
            field$Shaders_shaderPack.setAccessible(true);

            method$BlockAliases_update = class$BlockAliases.getDeclaredMethod("update", class$IShaderPack);
            method$BlockAliases_update.setAccessible(true);
        } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException e) {
            Share.LOG.error("Could not detect shadersmod, even though OptiFine is installed.\n" +
                            "Are you running an ancient OptiFine?", e);
            return;
        }

        //Reflection containers populated, register event handler
        FMLCommonHandler.instance().bus().register(TheEventHandler.INSTANCE);
    }

    //This is a separate class to avoid unnecessary event handler population when OF is not present
    private static class TheEventHandler {
        private static final TheEventHandler INSTANCE = new TheEventHandler();
        @SubscribeEvent
        public void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent e) {
            try {
                method$BlockAliases_update.invoke(null, field$Shaders_shaderPack.get(null));
            } catch (IllegalAccessException | InvocationTargetException ex) {
                Share.LOG.error("Reloading shader aliases failed!", ex);
            }
        }
    }
}
