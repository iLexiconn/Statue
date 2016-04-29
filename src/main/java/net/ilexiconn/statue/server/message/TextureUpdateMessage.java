package net.ilexiconn.statue.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.ilexiconn.statue.server.block.entity.StatueBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.awt.image.BufferedImage;

public class TextureUpdateMessage extends AbstractMessage<TextureUpdateMessage> {
    private BlockPos pos;
    private int[][] texture;
    private int startX;
    private int startY;
    private int fullWidth;
    private int fullHeight;

    public TextureUpdateMessage() {
    }

    public TextureUpdateMessage(StatueBlockEntity statue, int sectionX, int sectionY) {
        this.pos = statue.getPos();
        this.startX = sectionX * 256;
        this.startY = sectionY * 256;
        BufferedImage texture = statue.loadingTexture;
        if (texture != null) {
            int width = Math.min(this.startX + 256, texture.getWidth());
            int height = Math.min(this.startY + 256, texture.getHeight());
            this.texture = new int[width - this.startX][height - this.startY];
            for (int x = this.startX; x < width; x++) {
                for (int y = this.startY; y < height; y++) {
                    this.texture[x - this.startX][y - this.startY] = texture.getRGB(x, y);
                }
            }
            this.fullWidth = texture.getWidth();
            this.fullHeight = texture.getHeight();
        }
    }

    @Override
    public void onClientReceived(Minecraft client, TextureUpdateMessage message, EntityPlayer player, MessageContext messageContext) {
        this.handle(message, player, false);
    }

    @Override
    public void onServerReceived(MinecraftServer server, TextureUpdateMessage message, EntityPlayer player, MessageContext messageContext) {
        this.handle(message, player, true);
    }

    private void handle(TextureUpdateMessage message, EntityPlayer player, boolean sync) {
        TileEntity tile = player.worldObj.getTileEntity(message.pos);
        if (tile instanceof StatueBlockEntity) {
            StatueBlockEntity statue = (StatueBlockEntity) tile;
            int[][] messageTexture = message.texture;
            if (messageTexture != null) {
                BufferedImage texture = statue.loadingTexture;
                if (texture == null) {
                    texture = new BufferedImage(message.fullWidth, message.fullHeight, BufferedImage.TYPE_INT_ARGB);
                }
                for (int x = 0; x < messageTexture.length; x++) {
                    for (int y = 0; y < messageTexture[0].length; y++) {
                        texture.setRGB(message.startX + x, message.startY + y, messageTexture[x][y]);
                    }
                }
                if (message.startX + messageTexture.length >= message.fullWidth && message.startY + messageTexture[0].length >= message.fullHeight) {
                    statue.setTexture(texture, sync);
                } else {
                    statue.finishedLoading = false;
                    statue.loadingTexture = texture;
                }
                messageTexture = null;
                message.texture = null;
            } else {
                statue.setTexture(null, sync);
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.startX = buf.readInt();
        this.startY = buf.readInt();
        this.fullWidth = buf.readInt();
        this.fullHeight = buf.readInt();
        if (buf.readBoolean()) {
            int width = buf.readInt();
            int height = buf.readInt();
            this.texture = new int[width][height];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    this.texture[x][y] = buf.readInt();
                }
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
        buf.writeInt(this.startX);
        buf.writeInt(this.startY);
        buf.writeInt(this.fullWidth);
        buf.writeInt(this.fullHeight);
        buf.writeBoolean(this.texture != null);
        if (this.texture != null) {
            int width = this.texture.length;
            int height = this.texture[0].length;
            buf.writeInt(width);
            buf.writeInt(height);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    buf.writeInt(this.texture[x][y]);
                }
            }
        }
    }
}
