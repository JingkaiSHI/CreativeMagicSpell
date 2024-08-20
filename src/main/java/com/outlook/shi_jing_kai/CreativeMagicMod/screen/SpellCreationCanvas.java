package com.outlook.shi_jing_kai.CreativeMagicMod.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.checkerframework.checker.units.qual.C;

public class SpellCreationCanvas extends Screen {
    private final int canvasSize = 16;
    private int[][] canvasState = new int[canvasSize][canvasSize];
    private CanvasRenderer canvasRenderer;
    private SpellPredictor spellPredictor;
    public SpellCreationCanvas() {
        super(Component.literal("Spell Creation Canvas"));
        // initialize other components
    }

    @Override
    protected void init(){
        this.addRenderableWidget(Button.builder(Component.literal("Save Spell"), button -> saveSpell())
                .pos(this.width/2 - 100, this.height/2 - 40)
                .size(200, 20)
                .build());
        this.addRenderableWidget(Button.builder(Component.literal("Discard Spell"), button -> discardSpell())
                .pos(this.width/2 - 100, this.height/2 - 70)
                .size(200, 20)
                .build());
        this.addRenderableWidget(Button.builder(Component.literal("Reset Canvas"), button -> resetCanvas())
                .pos(this.width/2 - 100, this.height/2 - 100)
                .size(200, 20)
                .build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        // Render a half-transparent black background
        renderTransparentBackground(guiGraphics);

        // Render other elements like buttons
        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    private void renderTransparentBackground(GuiGraphics guiGraphics) {
        // Use fill to draw a half-transparent black rectangle
        guiGraphics.fill(0, 0, this.width, this.height, 0x80000000); // 50% transparent black (0x80 is the alpha channel)
    }


    private void saveSpell(){

    }

    private void discardSpell(){

    }

    private void resetCanvas(){

    }
}
