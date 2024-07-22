package com.outlook.shi_jing_kai.CreativeMagicMod.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import com.outlook.shi_jing_kai.CreativeMagicMod.CreativeMagicMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.gui.GuiGraphics;

public class CanvasRenderer {
    private static final ResourceLocation CANVAS_TEXTURE = new ResourceLocation(CreativeMagicMod.MOD_ID, "textures/gui/canvas.png");

    public static void renderCanvas(GuiGraphics guiGraphics, int x, int y, int[][] canvasState){
        Minecraft.getInstance().getTextureManager().bindForSetup(CANVAS_TEXTURE);
        TextureAtlasSprite sprite = getTextureAtlasSprite(CANVAS_TEXTURE);
        for(int i = 0; i < canvasState.length; i++){
            for(int j = 0; j < canvasState[i].length; j++){
                if(canvasState[i][j] == 1){
                    guiGraphics.blit(x + i * 16, y + j * 16, 0, 16, 16, sprite);
                }
            }
        }
    }

    private static TextureAtlasSprite getTextureAtlasSprite(ResourceLocation resourceLocation){
        TextureAtlas atlas = (TextureAtlas) Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS);
        return atlas.getSprite(resourceLocation);
    }
}
