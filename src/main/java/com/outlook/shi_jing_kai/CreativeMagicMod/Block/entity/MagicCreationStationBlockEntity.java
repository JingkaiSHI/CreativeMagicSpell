package com.outlook.shi_jing_kai.CreativeMagicMod.Block.entity;

import com.outlook.shi_jing_kai.CreativeMagicMod.Block.ModBlockEntities;
import com.outlook.shi_jing_kai.CreativeMagicMod.screen.SpellCreationMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MagicCreationStationBlockEntity extends BlockEntity implements MenuProvider {

    protected final ContainerData data;
    public MagicCreationStationBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.MAGIC_CREATION_STATION_BLOCK_ENTITY.get(), blockPos, blockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Magic Creation Altar");
    }

    // Create the Menu the station is going to offer when opening the creation screen
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new SpellCreationMenu(containerId, playerInventory, this, this.data);
    }
}
