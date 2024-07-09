package com.outlook.shi_jing_kai.CreativeMagicMod.screen;

import com.outlook.shi_jing_kai.CreativeMagicMod.Block.entity.MagicCreationStationBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class MagicCreationMenu extends AbstractContainerMenu {
    public final MagicCreationStationBlockEntity entity;
    private final Level level;
    private final ContainerData data;

    public MagicCreationMenu(int containerId, Inventory inventory, FriendlyByteBuf friendlyByteBuf) {
        this(containerId, inventory, inventory.player.level().getBlockEntity(friendlyByteBuf.readBlockPos()), new SimpleContainerData(2));
    }

    public MagicCreationMenu(int containerId, Inventory inventory, BlockEntity blockEntity, ContainerData data) {
        super(ModMenuTypes.MAGIC_CREATION_MENU.get(), containerId);
        checkContainerSize(inventory, 2);
        this.entity = (MagicCreationStationBlockEntity) blockEntity;
        this.level = inventory.player.level();
        this.data = data;

        // addPlayerInventory(inventory);
        // addPlayerHotbar(inventory);


    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return null;
    }

    @Override
    public boolean stillValid(Player p_38874_) {
        return false;
    }

    private void addPlayerInventory(Inventory inv){
        for(int i = 0; i < 3; ++i){
            for(int l = 0; l <9; ++l){
                this.addSlot(new Slot(inv, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotBar(Inventory inv){
        for(int i = 0; i < 9; ++i){
            this.addSlot(new Slot(inv, i, 8 + i * 18, 142));
        }
    }
}
