package net.ilexiconn.statue.server;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.ilexiconn.statue.Statue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.io.File;
import java.io.IOException;

public class ServerProxy {
    public void onInit() {
        MinecraftForge.EVENT_BUS.register(ServerEventHandler.INSTANCE);
        NetworkRegistry.INSTANCE.registerGuiHandler(Statue.INSTANCE, new GUIHandler());
    }

    public void onPostInit() {
        if (!Statue.TABULA_DIR.exists()) {
            Statue.TABULA_DIR.mkdirs();
        }
        if (!Statue.STATUE_DIR.exists()) {
            Statue.STATUE_DIR.mkdirs();
        }

        this.readDirectory(Statue.TABULA_DIR);
        this.readDirectory(Statue.STATUE_DIR);
    }

    private void readDirectory(File file) {
        if (file == null || !file.isDirectory()) {
            return;
        }

        File[] files = file.listFiles();
        if (files != null && files.length != 0) {
            ProgressManager.ProgressBar progressBar = ProgressManager.push("Loading models", files.length);
            for (File f : files) {
                progressBar.step(f.getName());
                if (f.getName().endsWith(".tbl")) {
                    try {
                        TabulaModelContainer container = TabulaModelHandler.INSTANCE.loadTabulaModel(f.getAbsolutePath());
                        Statue.CONTAINER_LIST.add(container);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            ProgressManager.pop(progressBar);
        }
    }
}
