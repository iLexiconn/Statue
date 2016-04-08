package net.ilexiconn.statue;

import net.ilexiconn.llibrary.server.network.NetworkWrapper;
import net.ilexiconn.statue.server.ServerProxy;
import net.ilexiconn.statue.server.block.StatueBlock;
import net.ilexiconn.statue.server.block.entity.StatueBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static net.minecraftforge.fml.common.Mod.EventHandler;
import static net.minecraftforge.fml.common.Mod.Instance;

@Mod(modid = "statue", name = "Statue", version = Statue.VERSION, dependencies = "required-after:llibrary@[" + Statue.LLIBRARY_VERSION + ",)")
public class Statue {
    public static final String VERSION = "1.0.0";
    public static final String LLIBRARY_VERSION = "1.2.0";

    @Instance
    public static Statue INSTANCE;
    @SidedProxy(serverSide = "net.ilexiconn.statue.server.ServerProxy", clientSide = "net.ilexiconn.statue.client.ClientProxy")
    public static ServerProxy PROXY;
    @NetworkWrapper({})
    public static SimpleNetworkWrapper NETWORK_WRAPPER;

    public static final Block STATUE = new StatueBlock();
    public static final Item STATUE_ITEM = new ItemBlock(Statue.STATUE).setRegistryName(Statue.STATUE.getRegistryName());

    @EventHandler
    public void onInit(FMLInitializationEvent event) {
        GameRegistry.register(Statue.STATUE);
        GameRegistry.register(Statue.STATUE_ITEM);
        GameRegistry.registerTileEntity(StatueBlockEntity.class, "statue_entity");
        Statue.PROXY.onInit();
    }

    @EventHandler
    public void onPostInit(FMLPostInitializationEvent event) {
        Statue.PROXY.onPostInit();
    }
}
