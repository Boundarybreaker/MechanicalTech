package io.engi.mechanicaltech.block;

import io.engi.mechanicaltech.entity.WindsailBlockEntity;
import io.engi.mechanicaltech.util.Utilities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;

import javax.annotation.Nullable;

public class WindsailBlock extends AbstractTurbineAttachmentBlock implements BlockEntityProvider {
	public static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 12, 16, 16, 16);

	private final int multiplier;

	public WindsailBlock(AbstractBlock.Settings settings, int multiplier) {
		super(settings);
		this.multiplier = multiplier;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction facing = computeFacing(ctx.getWorld(), ctx.getBlockPos());
		if (facing == null) {
			return getDefaultState();
		}
		return getDefaultState().with(FACING, computeFacing(ctx.getWorld(), ctx.getBlockPos()));
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos, Direction facing) {
		int xRange = facing.getAxis() == Direction.Axis.X ? 0 : 1;
		int zRange = facing.getAxis() == Direction.Axis.Z ? 0 : 1;
		for (BlockPos checkPos : BlockPos.iterateOutwards(pos, xRange, 2, zRange)) {
			if (world.getBlockState(checkPos).getBlock() != Blocks.AIR) {
				return false;
			}
		}
		return true;
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView view) {
		return new WindsailBlockEntity(multiplier);
	}

	@Override
	public VoxelShape getOutlineShape(
		BlockState state, BlockView world, BlockPos pos, ShapeContext context
	) {
		return Utilities.rotateShape(Direction.NORTH, state.get(FACING), SHAPE);
	}
}
