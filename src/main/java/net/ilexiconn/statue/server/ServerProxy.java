package net.ilexiconn.statue.server;

import net.ilexiconn.statue.Statue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ServerProxy {
    public void onInit() {
        MinecraftForge.EVENT_BUS.register(ServerEventHandler.INSTANCE);
        NetworkRegistry.INSTANCE.registerGuiHandler(Statue.INSTANCE, new GUIHandler());
    }

    public void onPostInit() {

    }
}
