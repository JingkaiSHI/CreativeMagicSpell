package com.outlook.shi_jing_kai.CreativeMagicMod.Block.custom;

import com.outlook.shi_jing_kai.CreativeMagicMod.Block.ModBlockEntities;
import com.outlook.shi_jing_kai.CreativeMagicMod.Block.entity.MagicCreationStationBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class MagicCreationStationBlock extends BaseEntityBlock {

    // New property 'LEVEL' will determine whether the station to be opened with specific elements available for strokes
    // LEVEL = 0: default state, access to all normal elements: wind, water, thunder, plant, ice, fire, earth
    // LEVEL = 1: advanced state, access to all elements in LEVEL = 0 and 2 more elements: light, dark
    // LEVEL = 2: supreme state, access to all elements in LEVEL = 1 and 2 more elements: time, space
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 2);
    public MagicCreationStationBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.AMETHYST_BLOCK));
        this.registerDefaultState(this.getStateDefinition().any().setValue(LEVEL, 0));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new MagicCreationStationBlockEntity(ModBlockEntities.MAGIC_CREATION_STATION_BLOCK_ENTITY.get(), blockPos, blockState);
    }


    // Open a new canvas (can leave that as a stub for now)
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit){
        if(!level.isClientSide){
            level.setBlock(pos, state.cycle(LEVEL), 3);
            // Open the GUI for spell creation
            NetworkHooks.openScreen((ServerPlayer) player, (MenuProvider) level.getBlockEntity(pos), pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
        return Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D); // create 2-block tall
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    public RenderShape getRenderShape(BlockState state){
        return RenderShape.MODEL;
    }


    // properly handle the destruction of a block entity on block destruction
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving){
        if(!state.is(newState.getBlock())){
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof MagicCreationStationBlockEntity){
                level.removeBlockEntity(pos);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
}
