package mega.blendtronic.proxy;


import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import mega.blendtronic.Tags;
import mega.blendtronic.modules.cauldrontank.TileCauldron;

import static mega.blendtronic.config.MinecraftConfig.cauldronTank;
import static mega.blendtronic.config.MinecraftConfig.extendedTileEntity;

public abstract class CommonProxy {
    public void construct(FMLConstructionEvent e) {}

    public void preInit(FMLPreInitializationEvent e) {
        if (cauldronTank && extendedTileEntity)
            GameRegistry.registerTileEntity(TileCauldron.class, Tags.MODID + "_cauldron");
    }

    public void init(FMLInitializationEvent e) {}

    public void postInit(FMLPostInitializationEvent e) {}
}

