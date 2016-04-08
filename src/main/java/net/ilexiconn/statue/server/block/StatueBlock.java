package net.ilexiconn.statue.server.block;

import net.ilexiconn.statue.server.block.entity.StatueBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StatueBlock extends BlockContainer {
    public StatueBlock() {
        super(Material.cloth);
        this.setRegistryName("statue");
        this.setUnlocalizedName("statue");
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockUnbreakable();
    }

    @Override //Collision bounding box. Mappings messed up.
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World world, BlockPos pos) {
        return Block.NULL_AABB;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new StatueBlockEntity();
    }
}
