package com.example;

import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = "examplemod", version = "1.0.0")
public class ExampleMod {
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("Dirt: " + Blocks.DIRT.getTranslationKey());
    }
}
