package com.outlook.shi_jing_kai.CreativeMagicMod.screen;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.checkerframework.checker.units.qual.C;

public class SpellCreationCanvas extends Screen {
    private final int canvasSize = 16;
    private int[][] canvasState = new int[canvasSize][canvasSize];
    private CanvasRenderer canvasRenderer;
    private SpellPredictor spellPredictor;
    protected SpellCreationCanvas() {
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


    private void saveSpell(){

    }

    private void discardSpell(){

    }

    private void resetCanvas(){

    }
}
