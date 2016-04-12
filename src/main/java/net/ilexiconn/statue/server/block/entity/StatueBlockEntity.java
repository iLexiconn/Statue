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

public class StatueBlockEntity extends BlockEntity {
    @NBTProperty
    public TabulaModelContainer currentModel;

    public BufferedImage texture;

    @SideOnly(Side.CLIENT)
    private TabulaModel renderModel;

    @SideOnly(Side.CLIENT)
    private DynamicTexture dynamicTexture;

    @SideOnly(Side.CLIENT)
    private ResourceLocation resourceLocation;

    @Override
    public void onLoad() {
        this.currentModel = Statue.CONTAINER_LIST.get(0);
        this.setTexture(Statue.TEXTURE_LIST.get(this.currentModel), true);
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
        if (this.worldObj.isRemote && this.currentModel != null) {
            this.renderModel = new TabulaModel(this.currentModel);
        }
    }

    private void updateTexture() {
        if (this.dynamicTexture != null) {
            Minecraft.getMinecraft().getTextureManager().deleteTexture(resourceLocation);
        }
        if (this.texture != null) {
            this.dynamicTexture = new DynamicTexture(this.texture);
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
        this.texture = texture;
        if (worldObj.isRemote) {
            if (sync) {
                Statue.NETWORK_WRAPPER.sendToServer(new TextureUpdateMessage(this));
            }
            this.updateTexture();
        } else {
            if (sync) {
                Statue.NETWORK_WRAPPER.sendToAll(new TextureUpdateMessage(this));
            }
        }
    }

    @Override
    public Packet<?> getDescriptionPacket() {
        Statue.NETWORK_WRAPPER.sendToAll(new TextureUpdateMessage(this));
        return super.getDescriptionPacket();
    }
}
