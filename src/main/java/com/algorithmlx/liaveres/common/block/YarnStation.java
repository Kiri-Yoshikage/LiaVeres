package com.algorithmlx.liaveres.common.block;

import com.algorithmlx.liaveres.common.block.entity.YarnStationBlockEntity;
import com.algorithmlx.liaveres.common.setup.Registration;
import liquid.objects.block.entity.TickingBlockEntity;
import liquid.tool.VoxelShapeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("all")
public class YarnStation extends BaseEntityBlock {
    public static final DirectionProperty FACING_DIRECTION = HorizontalDirectionalBlock.FACING;

    public YarnStation() {
        super(BlockBehaviour.Properties.of(Material.WOOD)
                .strength(5f, 5f)
                .noOcclusion()
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING_DIRECTION, Direction.NORTH));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof YarnStationBlockEntity entity) {
                NetworkHooks.openGui((ServerPlayer) pPlayer, entity, pPos);
            } else {
                throw new IllegalStateException("Menu provider for " + YarnStation.class.getName() + " is missing!");
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new YarnStationBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING_DIRECTION, pContext.getHorizontalDirection().getClockWise());
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(FACING_DIRECTION, direction.rotate(state.getValue(FACING_DIRECTION)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING_DIRECTION);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof YarnStationBlockEntity e)
                e.drops();
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return this.createTickerHelper(pBlockEntityType, Registration.YARN_STATION_BLOCK_ENTITY.get(),
                YarnStationBlockEntity::tick0);
    }
}
