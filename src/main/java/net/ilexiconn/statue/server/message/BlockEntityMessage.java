package net.ilexiconn.statue.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.server.network.AbstractMessage;
import net.ilexiconn.statue.server.block.entity.BlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEntityMessage extends AbstractMessage<BlockEntityMessage> {
    private BlockPos pos;
    private NBTTagCompound compound;

    public BlockEntityMessage() {

    }

    public BlockEntityMessage(BlockEntity entity) {
        this.pos = entity.getPos();
        this.compound = new NBTTagCompound();
        entity.saveTrackingSensitiveData(this.compound);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onClientReceived(Minecraft client, BlockEntityMessage message, EntityPlayer player, MessageContext messageContext) {
        BlockEntity blockEntity = (BlockEntity) player.worldObj.getTileEntity(message.pos);
        blockEntity.loadTrackingSensitiveData(message.compound);
        System.out.println("updated");
    }

    @Override
    public void onServerReceived(MinecraftServer server, BlockEntityMessage message, EntityPlayer player, MessageContext messageContext) {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.compound = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
        ByteBufUtils.writeTag(buf, this.compound);
    }
}
