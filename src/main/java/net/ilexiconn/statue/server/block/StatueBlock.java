package net.ilexiconn.statue.server.block;

import net.ilexiconn.statue.Statue;
import net.ilexiconn.statue.server.block.entity.StatueBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
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

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        player.openGui(Statue.INSTANCE, 0, world, 0, 0, 0);
        return true;
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
