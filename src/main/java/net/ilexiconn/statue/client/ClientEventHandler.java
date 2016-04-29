package net.ilexiconn.statue.client;

import net.ilexiconn.statue.server.block.entity.StatueBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public enum ClientEventHandler {
    INSTANCE;

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        TileEntity tile = event.getWorld().getTileEntity(event.getPos());
        if (tile instanceof StatueBlockEntity) {
            StatueBlockEntity statue = (StatueBlockEntity) tile;
            Minecraft.getMinecraft().getTextureManager().deleteTexture(statue.getTexture());
        }
    }
}
