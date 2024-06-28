package com.outlook.shi_jing_kai.CreativeMagicMod.Client.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.outlook.shi_jing_kai.CreativeMagicMod.CreativeMagicMod;
import com.outlook.shi_jing_kai.CreativeMagicMod.Mana.PlayerManaProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

@OnlyIn(Dist.CLIENT)
public class ManaHudElement implements IGuiOverlay {

    // TODO: Remember to replace this current texture by the new drawn mana_bar.png
    private static final ResourceLocation MANA_BAR_TEXTURE = new ResourceLocation(CreativeMagicMod.MOD_ID, "textures/item/mana_crystal.png");
    private static final int MANA_BAR_WIDTH = 180;
    private static final int MANA_BAR_HEIGHT = 5;
    private static final int MANA_BAR_X = 10; // Adjust X position
    private static final int MANA_BAR_Y = 10; // Adjust Y position

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player != null) {
            player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                int currentMana = mana.getMana();
                int maxMana = mana.getMaxMana();
                // set current player's mana to max to visualize effect (shouldn't be any as haven't done artwork to visualize the change)

                RenderSystem.setShaderTexture(0, MANA_BAR_TEXTURE);
                int manaBarWidth = (int) ((float) currentMana / maxMana * MANA_BAR_WIDTH);
                guiGraphics.blit(MANA_BAR_TEXTURE, MANA_BAR_X, MANA_BAR_Y, 0, 0, MANA_BAR_WIDTH, MANA_BAR_HEIGHT); // Draw background
                guiGraphics.blit(MANA_BAR_TEXTURE, MANA_BAR_X, MANA_BAR_Y, 0, MANA_BAR_HEIGHT, manaBarWidth, MANA_BAR_HEIGHT); // Draw foreground
            });
        }
    }
}
