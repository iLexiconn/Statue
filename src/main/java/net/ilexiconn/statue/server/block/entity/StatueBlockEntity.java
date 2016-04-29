package net.ilexiconn.statue.server.block.entity;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.ilexiconn.llibrary.server.nbt.NBTHandler;
import net.ilexiconn.llibrary.server.nbt.NBTProperty;
import net.ilexiconn.statue.Statue;
import net.ilexiconn.statue.server.message.TextureUpdateMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class StatueBlockEntity extends BlockEntity {
    @NBTProperty
    public TabulaModelContainer currentModel;

    public BufferedImage loadingTexture;
    public boolean finishedLoading = true;

    @SideOnly(Side.CLIENT)
    private TabulaModel renderModel;

    @SideOnly(Side.CLIENT)
    private DynamicTexture dynamicTexture;

    @SideOnly(Side.CLIENT)
    private ResourceLocation resourceLocation;

    @Override
    public void onLoad() {
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTHandler.INSTANCE.saveNBTData(this, compound);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTHandler.INSTANCE.loadNBTData(this, compound);
        this.updateRenderModel();
    }

    private void updateRenderModel() {
        if (this.worldObj != null && this.worldObj.isRemote && this.currentModel != null) {
            this.renderModel = new TabulaModel(this.currentModel);
        }
    }

    private void updateTexture() {
        if (this.dynamicTexture != null) {
            Minecraft.getMinecraft().getTextureManager().deleteTexture(resourceLocation);
        }
        if (this.loadingTexture != null && this.finishedLoading) {
            this.dynamicTexture = new DynamicTexture(this.loadingTexture);
            this.loadingTexture = null;
            this.resourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("statue_" + pos, dynamicTexture);
        }
    }

    @SideOnly(Side.CLIENT)
    public TabulaModel getRenderModel() {
        if (this.renderModel == null && this.currentModel != null) {
            this.updateRenderModel();
        }
        return this.renderModel;
    }

    @SideOnly(Side.CLIENT)
    public ResourceLocation getTexture() {
        return this.resourceLocation;
    }

    public void setTexture(BufferedImage texture, boolean sync) {
        this.loadingTexture = texture;
        this.finishedLoading = true;
        if (worldObj.isRemote) {
            if (sync) {
                new Thread(() -> {
                    for (TextureUpdateMessage message : StatueBlockEntity.this.getTextureUpdateMessages()) {
                        Statue.NETWORK_WRAPPER.sendToServer(message);
                    }
                }).start();
            }
            this.updateTexture();
        } else {
            if (sync) {
                new Thread(() -> {
                    for (TextureUpdateMessage message : StatueBlockEntity.this.getTextureUpdateMessages()) {
                        Statue.NETWORK_WRAPPER.sendToAll(message);
                    }
                }).start();
            }
        }
    }

    @Override
    public Packet<?> getDescriptionPacket() {
        for (TextureUpdateMessage message : this.getTextureUpdateMessages()) {
            Statue.NETWORK_WRAPPER.sendToAll(message);
        }
        return super.getDescriptionPacket();
    }

    private List<TextureUpdateMessage> getTextureUpdateMessages() {
        List<TextureUpdateMessage> messages = new ArrayList<TextureUpdateMessage>();
        if (this.loadingTexture == null) {
            messages.add(new TextureUpdateMessage(this, 0, 0));
        } else {
            for (int x = 0; x < Math.ceil(loadingTexture.getWidth() / 256.0); x++) {
                for (int y = 0; y < Math.ceil(loadingTexture.getHeight() / 256.0); y++) {
                    messages.add(new TextureUpdateMessage(this, x, y));
                }
            }
        }
        return messages;
    }
}
