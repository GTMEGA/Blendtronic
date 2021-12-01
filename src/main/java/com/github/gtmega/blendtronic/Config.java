package com.github.gtmega.blendtronic;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public class Config {
    
    private static class Defaults {

    }

    private static class Categories {
        public static final String general = "general";
    }


    public static void syncronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);
        configuration.load();

        if(configuration.hasChanged()) {
            configuration.save();
        }
    }
}