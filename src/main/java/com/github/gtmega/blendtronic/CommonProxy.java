package com.github.gtmega.blendtronic;

import cpw.mods.fml.common.event.*;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) 	{
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());
    }
}
