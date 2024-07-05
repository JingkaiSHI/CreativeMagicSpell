package com.outlook.shi_jing_kai.CreativeMagicMod.event;

import com.outlook.shi_jing_kai.CreativeMagicMod.Client.PlayerDataStorage;
import com.outlook.shi_jing_kai.CreativeMagicMod.Client.hud.ManaHudElement;
import com.outlook.shi_jing_kai.CreativeMagicMod.CreativeMagicMod;
import com.outlook.shi_jing_kai.CreativeMagicMod.Mana.PlayerManaProvider;
import com.outlook.shi_jing_kai.CreativeMagicMod.networking.ModMessages;
import com.outlook.shi_jing_kai.CreativeMagicMod.networking.packet.SyncManaS2CPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CreativeMagicMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {

    private static final ManaHudElement manaHudElement = new ManaHudElement();

    @SubscribeEvent
    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation(CreativeMagicMod.MOD_ID, "playermana"), new PlayerManaProvider());
            System.out.println("PlayerManaProvider attached to player");
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event){
        syncManaDataWithClient(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            System.out.println("Player death detected, syncing mana capability");

            event.getOriginal().getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(oldMana -> {
                System.out.println("Original player Capability found, current mana: " + oldMana.getMana());
            });
            event.getOriginal().getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(oldMana -> {
                System.out.println("attempting to sync previous player's mana data..." + oldMana);
                event.getEntity().getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(newMana -> {
                    newMana.copyFrom(oldMana);
                    System.out.println("Player mana copied from old instance");
                });
            });
            syncManaDataWithClient(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event){
        event.getEntity().getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
            CompoundTag tag = new CompoundTag();
            System.out.println("saving player's mana upon entering portal...");
            mana.saveNBTData(tag);
            System.out.println("Saved!");
            mana.loadNBTData(tag);
            System.out.println("Successfully Loaded data after getting in portal!");
            System.out.println(mana.getMana());
            System.out.println(mana.getMaxMana());
            System.out.println(mana.getPhase());
        });
        syncManaDataWithClient(event.getEntity());
    }


    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event){
        event.getEntity().getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
            System.out.println("Attempting to load existing player's mana...");
            CompoundTag tag = PlayerDataStorage.loadPlayerData(event.getEntity().getUUID());
            if(tag != null){
                System.out.println("Previously Saved data detected, loading player's mana data...");
                mana.loadNBTData(tag);
                System.out.println(mana.getMana());
                System.out.println(mana.getMaxMana());
                System.out.println(mana.getPhase());
                System.out.println("Data Loaded!");
            }else{
                System.out.println("No Player Mana data saved!");
            }
        });
        syncManaDataWithClient(event.getEntity());
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event){
        event.getEntity().getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana ->{
            System.out.println("Saving Player's mana on logging out...");
            CompoundTag tag = new CompoundTag();
            mana.saveNBTData(tag);
            System.out.println(mana.getMana());
            System.out.println(mana.getMaxMana());
            System.out.println(mana.getPhase());
            PlayerDataStorage.savePlayerData(event.getEntity().getUUID(), tag);
            System.out.println("Saving Complete");
        });
    }

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay().id().equals(VanillaGuiOverlay.EXPERIENCE_BAR.id())) {
            manaHudElement.render(null, event.getGuiGraphics(), event.getPartialTick(), event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight());
        }
    }


    private static void syncManaDataWithClient(Player player){
        player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
            CompoundTag tag = new CompoundTag();
            mana.saveNBTData(tag);
            System.out.println("syncing mana data with client: " + tag);
            ModMessages.sendToClient(new SyncManaS2CPacket(tag), (ServerPlayer) player);
        });
    }
}
