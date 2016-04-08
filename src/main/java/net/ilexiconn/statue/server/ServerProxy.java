package net.ilexiconn.statue.server;

import net.minecraftforge.common.MinecraftForge;

public class ServerProxy {
    public void onInit() {
        MinecraftForge.EVENT_BUS.register(ServerEventHandler.INSTANCE);
    }

    public void onPostInit() {

    }
}
