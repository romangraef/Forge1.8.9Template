package com.example

import net.minecraft.init.Blocks
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent

@Mod(modid = "examplemod", version = "1.0.0")
class ExampleMod {
    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        println("Dirt: ${Blocks.dirt.unlocalizedName}")
    }
}