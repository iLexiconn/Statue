package net.ilexiconn.statue.client;

import net.ilexiconn.statue.Statue;
import net.ilexiconn.statue.client.render.StatueBlockEntityRenderer;
import net.ilexiconn.statue.server.ServerProxy;
import net.ilexiconn.statue.server.block.entity.StatueBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    public static final Minecraft MINECRAFT = Minecraft.getMinecraft();

    @Override
    public void onInit() {
        ClientRegistry.bindTileEntitySpecialRenderer(StatueBlockEntity.class, new StatueBlockEntityRenderer());
    }

    @Override
    public void onPostInit() {
        ClientProxy.MINECRAFT.getRenderItem().getItemModelMesher().register(Statue.STATUE_ITEM, 0, new ModelResourceLocation("statue:statue", "inventory"));
    }
}
