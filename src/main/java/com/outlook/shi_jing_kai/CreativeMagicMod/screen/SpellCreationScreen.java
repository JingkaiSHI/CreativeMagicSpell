package com.outlook.shi_jing_kai.CreativeMagicMod.screen;

import com.outlook.shi_jing_kai.CreativeMagicMod.Block.entity.MagicCreationStationBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class SpellCreationScreen extends Screen {
    private final int canvasSize = 16;
    private int[][] canvasState = new int[canvasSize][canvasSize];
    private CanvasRenderer canvasRenderer;
    private SpellPredictor spellPredictor;

    private MagicCreationStationBlockEntity blockEntity;
    public SpellCreationScreen(MagicCreationStationBlockEntity blockEntity) {
        super(Component.literal("Spell Creation Canvas"));
        // initialize other components
        this.blockEntity = blockEntity;
    }

    @Override
    protected void init(){
        this.addRenderableWidget(Button.builder(Component.literal("Save Spell"), button -> saveSpell())
                .pos(this.width - 100, this.height/2 - 40)
                .size(80, 20)
                .build());
        this.addRenderableWidget(Button.builder(Component.literal("Discard Spell"), button -> discardSpell())
                .pos(this.width - 100, this.height/2 - 70)
                .size(80, 20)
                .build());
        this.addRenderableWidget(Button.builder(Component.literal("Reset Canvas"), button -> resetCanvas())
                .pos(this.width - 100, this.height/2 - 100)
                .size(80, 20)
                .build());
        this.addRenderableWidget(Button.builder(Component.literal("Confirm Stroke"), button -> confirmStroke())
                .pos(this.width - 100, this.height/2 - 10)
                .size(80, 20)
                .build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        // Render a half-transparent black background
        renderTransparentBackground(guiGraphics);

        // render the canvas
        // calculate the size of each cell in the grid
        int cellSize = 10;
        int canvasWidth = canvasSize * cellSize;
        int canvasHeight = canvasSize * cellSize;
        int startX = (this.width - (canvasSize * cellSize)) / 2;
        int startY = (this.height - (canvasSize * cellSize)) / 2;

        guiGraphics.fill(startX - 1, startY - 1, startX + canvasWidth + 1, startY + canvasHeight + 1, 0xFFFFFFFF); // White boundary
        guiGraphics.fill(startX, startY, startX + canvasWidth, startY + canvasHeight, 0xFFAAAAAA); // Light gray canvas background


        // render the grid
        for (int row = 0; row < canvasSize; row++){
            for (int col = 0; col < canvasSize; col++){
                int color = (canvasState[row][col] == 1) ? 0xFF0000FF : 0xFF888888;

                guiGraphics.fill(startX + col * cellSize, startY + row * cellSize,
                        startX + (col + 1) * cellSize, startY + (row + 1) * cellSize, color);
            }
        }

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
        for (int row = 0; row < canvasSize; row++) {
            for (int col = 0; col < canvasSize; col++) {
                canvasState[row][col] = 0; // Reset all cells to 0
            }
        }
    }


    private void confirmStroke(){

    }


    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        // Calculate the size and position of each cell
        int cellSize = 10;
        int startX = (this.width - (canvasSize * cellSize)) / 2;
        int startY = (this.height - (canvasSize * cellSize)) / 2;

        // Determine which cell was dragged over
        int col = (int) ((mouseX - startX) / cellSize);
        int row = (int) ((mouseY - startY) / cellSize);

        // Check if the drag is within the canvas bounds
        if (col >= 0 && col < canvasSize && row >= 0 && row < canvasSize) {
            // Clear the canvas and set the dragged cell
            canvasState[row][col] = 1;

            // Indicate that the screen needs to be re-rendered
            return true;
        }

        resetCanvasOnStroke(); // Resets the canvas to its initial state

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }


    private void resetCanvasOnStroke() {
        for (int row = 0; row < canvasSize; row++) {
            for (int col = 0; col < canvasSize; col++) {
                canvasState[row][col] = 0; // Reset all cells to 0
            }
        }
    }
}
