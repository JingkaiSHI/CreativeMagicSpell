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
    private static final ResourceLocation MANA_BAR_TEXTURE = new ResourceLocation(CreativeMagicMod.MOD_ID, "textures/gui/mana_bar.png");
    private static final int MANA_BAR_WIDTH = 160;
    private static final int MANA_BAR_HEIGHT = 10;
    private static final int MANA_BAR_X = 10; // Adjust X position
    private static final int MANA_BAR_Y = 10; // Adjust Y position

    private static final int PHASE1_U = 3;
    private static final int PHASE1_V = 3;

    private static final int PHASE1_X = 3;
    private static final int PHASE1_Y = 16;

    private static final int PHASE2_U = 3;
    private static final int PHASE2_V = 32;

    private static final int PHASE2_X = 3;
    private static final int PHASE2_Y = 48;

    private static final int PHASE3_U = 3;
    private static final int PHASE3_V = 64;

    private static final int PHASE3_X = 3;
    private static final int PHASE3_Y = 80;



    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player != null) {
            player.getCapability(PlayerManaProvider.PLAYER_MANA).ifPresent(mana -> {
                int currentMana = mana.getMana();
                int maxMana = mana.getMaxMana();
                int phase = mana.getPhase();

                int u, v, x, y;
                // set current player's mana to max to visualize effect (shouldn't be any as haven't done artwork to visualize the change)

                switch(phase){
                    case 1:
                        u = PHASE2_U;
                        v = PHASE2_V;
                        x = PHASE2_X;
                        y = PHASE2_Y;
                        break;
                    case 2:
                        u = PHASE3_U;
                        v = PHASE3_V;
                        x = PHASE3_X;
                        y = PHASE3_Y;
                        break;
                    default:
                        u = PHASE1_U;
                        v = PHASE1_V;
                        x = PHASE1_X;
                        y = PHASE1_Y;
                        break;
                }
                guiGraphics.blit(MANA_BAR_TEXTURE, MANA_BAR_X, MANA_BAR_Y, x, y, MANA_BAR_WIDTH, MANA_BAR_HEIGHT);
                int currentManaWidth = (int) (MANA_BAR_WIDTH * (currentMana/(float) maxMana));
                guiGraphics.blit(MANA_BAR_TEXTURE, MANA_BAR_X, MANA_BAR_Y, u, v, currentManaWidth, MANA_BAR_HEIGHT);
            });
        }
    }
}
