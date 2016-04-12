package net.ilexiconn.statue.client.render;

import net.ilexiconn.llibrary.client.model.VoxelModel;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.statue.server.block.entity.StatueBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class StatueBlockEntityRenderer extends TileEntitySpecialRenderer<StatueBlockEntity> {
    private ModelBase voxelModel = new VoxelModel();

    @Override
    public void renderTileEntityAt(StatueBlockEntity entity, double x, double y, double z, float partialTicks, int destroyStage) {
        TabulaModel model = entity.getRenderModel();
        ResourceLocation resourceLocation = entity.getTexture();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        if (model == null || resourceLocation == null) {
            GlStateManager.disableTexture2D();
            GlStateManager.translate(0.5F, -1.735F, 0.5F);
            GL11.glDepthMask(false);
            GlStateManager.disableLighting();
            GlStateManager.translate(0.0F, -1.37F, 0.0F);
            this.renderModel(this.voxelModel, 2.75F, 0.23F);
            GL11.glDepthMask(true);
            GlStateManager.enableLighting();
            GlStateManager.translate(0.0F, 0.128F, 0.0F);
            this.renderModel(this.voxelModel, 2.65F, 1.0F);
            GlStateManager.enableTexture2D();
        } else {
            Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
            GlStateManager.translate(0.5F, 1.5F, 0.5F);
            GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            this.renderModel(model, 1.0F, 1.0F);
        }
        GlStateManager.popMatrix();
    }

    private void renderModel(ModelBase model, float scale, float color) {
        GlStateManager.pushMatrix();
        GlStateManager.color(color, color, color, 1.0F);
        GlStateManager.scale(scale, scale, scale);
        model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();
    }
}
