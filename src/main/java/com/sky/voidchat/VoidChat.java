package com.sky.voidchat;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = VoidChat.MODID, version = VoidChat.VERSION)
public class VoidChat
{
    public static final String MODID = "VoidChat";
    public static final String VERSION = "1.2.2B";
   
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	MinecraftForge.EVENT_BUS.register(this);
    }
}
