package me.zircta.modtemplate;

import net.weavemc.loader.api.ModInitializer;

public class Main implements ModInitializer {
    @Override
    public void preInit() {
        System.out.println("preinit");
    }
}