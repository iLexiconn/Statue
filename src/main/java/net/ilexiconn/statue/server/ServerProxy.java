package net.ilexiconn.statue.server;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.ilexiconn.statue.Statue;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ServerProxy {
    public void onInit() {
        MinecraftForge.EVENT_BUS.register(ServerEventHandler.INSTANCE);
        NetworkRegistry.INSTANCE.registerGuiHandler(Statue.INSTANCE, new GUIHandler());
    }

    public void onPostInit() {
        if (Statue.TABULA_DIR.exists()) {
            this.readDirectory(Statue.TABULA_DIR);
        }
        if (!Statue.STATUE_DIR.exists()) {
            Statue.STATUE_DIR.mkdirs();
        }
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
                        InputStream inputStream = new FileInputStream(f);
                        TabulaModelContainer container = TabulaModelHandler.INSTANCE.loadTabulaModel(this.getModelJsonStream(f.getAbsolutePath(), inputStream));
                        InputStream textureStream = this.getTextureStream(inputStream);
                        Statue.CONTAINER_LIST.add(container);
                        if (textureStream != null) {
                            Statue.TEXTURE_LIST.put(container, ImageIO.read(inputStream));
                        }
                    } catch (Exception e) {
                        System.err.println("Failed to load model " + f.getAbsolutePath());
                    }
                }
            }
            ProgressManager.pop(progressBar);
        }
    }

    private InputStream getModelJsonStream(String name, InputStream file) throws IOException {
        ZipInputStream zip = new ZipInputStream(file);
        ZipEntry entry;
        while ((entry = zip.getNextEntry()) != null) {
            if (entry.getName().equals("model.json")) {
                return zip;
            }
        }
        throw new RuntimeException("No model.json present in " + name);
    }

    private InputStream getTextureStream(InputStream file) throws IOException {
        ZipInputStream zip = new ZipInputStream(file);
        ZipEntry entry;
        while ((entry = zip.getNextEntry()) != null) {
            if (entry.getName().equals("texture.png")) {
                return zip;
            }
        }
        return null;
    }
}
