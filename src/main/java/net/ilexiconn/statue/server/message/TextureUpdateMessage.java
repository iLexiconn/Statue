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
    private BufferedImage texture;

    public TextureUpdateMessage() {

    }

    public TextureUpdateMessage(StatueBlockEntity statue) {
        this.texture = statue.texture;
        this.pos = statue.getPos();
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
            statue.setTexture(message.texture, sync);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        if (buf.readBoolean()) {
            int width = buf.readInt();
            int height = buf.readInt();
            this.texture = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    this.texture.setRGB(x, y, buf.readInt());
                }
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(this.pos.toLong());
        buf.writeBoolean(this.texture != null);
        if (this.texture != null) {
            buf.writeInt(this.texture.getWidth());
            buf.writeInt(this.texture.getHeight());
            for (int x = 0; x < this.texture.getWidth(); x++) {
                for (int y = 0; y < this.texture.getHeight(); y++) {
                    buf.writeInt(this.texture.getRGB(x, y));
                }
            }
        }
    }
}
