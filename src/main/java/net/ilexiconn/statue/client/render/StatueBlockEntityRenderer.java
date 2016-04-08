package net.ilexiconn.statue.client.render;

import net.ilexiconn.llibrary.client.model.VoxelModel;
import net.ilexiconn.statue.server.block.entity.StatueBlockEntity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import org.lwjgl.opengl.GL11;

public class StatueBlockEntityRenderer extends TileEntitySpecialRenderer<StatueBlockEntity> {
    private ModelBase voxelModel = new VoxelModel();

    @Override
    public void renderTileEntityAt(StatueBlockEntity entity, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.translate(0.5F, -1.735F, 0.5F);
        GL11.glDepthMask(false);
        GlStateManager.disableLighting();
        GlStateManager.translate(0.0F, -1.37F, 0.0F);
        this.renderVoxel(2.75F, 0.23F);
        GL11.glDepthMask(true);
        GlStateManager.enableLighting();
        GlStateManager.translate(0.0F, 0.128F, 0.0F);
        this.renderVoxel(2.65F, 1.0F);
        GlStateManager.popMatrix();
    }

    private void renderVoxel(float scale, float color) {
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.color(color, color, color, 1.0F);
        GlStateManager.scale(scale, scale, scale);
        this.voxelModel.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}
