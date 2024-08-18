package com.outlook.shi_jing_kai.CreativeMagicMod.Block;

import com.google.common.collect.ImmutableSet;
import com.outlook.shi_jing_kai.CreativeMagicMod.Block.entity.MagicCreationStationBlockEntity;
import com.outlook.shi_jing_kai.CreativeMagicMod.CreativeMagicMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CreativeMagicMod.MOD_ID);


    // Register the BlockEntityTypes
    public static final RegistryObject<BlockEntityType<MagicCreationStationBlockEntity>> MAGIC_CREATION_STATION_BLOCK_ENTITY =
             BLOCK_ENTITIES.register("magic_creation_station", () -> new BlockEntityType<>((BlockPos pos, BlockState state) -> new MagicCreationStationBlockEntity(null, pos, state), ImmutableSet.of(ModBlock.MAGIC_CREATION_STATION.get()), null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
