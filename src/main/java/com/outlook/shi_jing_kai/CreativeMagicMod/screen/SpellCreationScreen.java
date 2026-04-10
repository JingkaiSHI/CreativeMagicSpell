package com.outlook.shi_jing_kai.CreativeMagicMod.screen;


import com.outlook.shi_jing_kai.CreativeMagicMod.Block.entity.MagicCreationStationBlockEntity;
import com.outlook.shi_jing_kai.CreativeMagicMod.Block.entity.RuneClassifier;
import com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.SpellBookItem;
import com.outlook.shi_jing_kai.CreativeMagicMod.networking.ModMessages;
import com.outlook.shi_jing_kai.CreativeMagicMod.networking.packet.DiscardSpellBookC2SPacket;
import com.outlook.shi_jing_kai.CreativeMagicMod.networking.packet.SaveSpellBookC2SPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import com.outlook.shi_jing_kai.CreativeMagicMod.Item.custom.data.Effects;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class SpellCreationScreen extends Screen {
    public static Effects[] EFFECTS = new Effects[16];
    private static final int DRAFT_PANEL_WIDTH = 110;
    private static final int DRAFT_PANEL_X_OFFSET = 120;
    private static final int DRAFT_ENTRY_START_Y = 56;
    private static final int DRAFT_ENTRY_HEIGHT = 12;
    private static final int DRAFT_ENTRY_VISIBLE_COUNT = 8;
    private final int canvasSize = 16;
    private final int[][] canvasState = new int[canvasSize][canvasSize];
    private final List<Effects> createdEffects = new ArrayList<>();
    private int draggingEffectIndex = -1;
    private int dragTargetIndex = -1;

    static {
        EFFECTS[0] = Effects.ACCELERATE;
        EFFECTS[1] = Effects.CONFUSE;
        EFFECTS[2] = Effects.CONSTRAINT;
        EFFECTS[3] = Effects.EMIT;
        EFFECTS[4] = Effects.ENHANCE;
        EFFECTS[5] = Effects.ERASE;
        EFFECTS[6] = Effects.FORM_BOOTS;
        EFFECTS[7] = Effects.FORM_CHESTPLATE;
        EFFECTS[8] = Effects.FORM_HELMET;
        EFFECTS[9] = Effects.FORM_LEGGING;
        EFFECTS[10] = Effects.FORM_SHIELD;
        EFFECTS[11] = Effects.FORM_SWORD;
        EFFECTS[12] = Effects.REALITY_MARBLE;
        EFFECTS[13] = Effects.SLOW;
        EFFECTS[14] = Effects.STOP;
        EFFECTS[15] = Effects.WALL;
    }

    public SpellCreationScreen(MagicCreationStationBlockEntity blockEntity) {
        super(Component.literal("Spell Creation Canvas"));
    }

    @Override
    protected void init(){
        loadEffectsFromHeldSpellBook();
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
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
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

        renderSpellStatus(guiGraphics, startX, startY, canvasWidth, canvasHeight);

        // Render other elements like buttons
        super.render(guiGraphics, mouseX, mouseY, delta);
    }

    private void renderTransparentBackground(GuiGraphics guiGraphics) {
        // Use fill to draw a half-transparent black rectangle
        guiGraphics.fill(0, 0, this.width, this.height, 0x80000000); // 50% transparent black (0x80 is the alpha channel)
    }


    private void saveSpell(){
        Player player = Minecraft.getInstance().player;
        InteractionHand spellBookHand = getSpellBookHand(player);
        if (player == null || spellBookHand == null) {
            showClientMessage("Hold a spell book before saving.");
            return;
        }
        if (createdEffects.isEmpty()) {
            showClientMessage("Draw and confirm at least one rune first.");
            return;
        }

        ModMessages.sendToServer(new SaveSpellBookC2SPacket(spellBookHand, new ArrayList<>(createdEffects)), Minecraft.getInstance().player);
        showClientMessage("Sent spell book save request: " + SpellBookItem.describeEffects(createdEffects));
    }

    private void discardSpell(){
        Player player = Minecraft.getInstance().player;
        InteractionHand spellBookHand = getSpellBookHand(player);
        resetCanvas();
        createdEffects.clear();
        if (player != null && spellBookHand != null) {
            ModMessages.sendToServer(new DiscardSpellBookC2SPacket(spellBookHand), Minecraft.getInstance().player);
        }
        showClientMessage("Cleared the current spell draft.");
    }

    private void resetCanvas(){
        for (int row = 0; row < canvasSize; row++) {
            for (int col = 0; col < canvasSize; col++) {
                canvasState[row][col] = 0; // Reset all cells to 0
            }
        }
    }


    private void confirmStroke(){
        try {
            float[][][][] tensor = RuneClassifier.canvasToTensor(canvasState);
            int runeClass = RuneClassifier.classifyRune(tensor);

            if (runeClass >= 0) {
                // assign rune class number to the effect
                Effects effect = EFFECTS[runeClass];
                createdEffects.add(effect);
                showClientMessage("Added rune effect: " + effect.name());
                resetCanvas();
            } else {
                showClientMessage("Failed to classify rune.");
            }
        } catch (Exception e) {
            showClientMessage("Failed in saving spell: " + e.getMessage());
        }
    }


    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (draggingEffectIndex >= 0) {
            int hoveredIndex = getDraftEntryIndexAt(mouseX, mouseY);
            if (hoveredIndex >= 0 && hoveredIndex < createdEffects.size()) {
                dragTargetIndex = hoveredIndex;
            }
            return true;
        }

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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int removeIndex = getRemoveEntryIndexAt(mouseX, mouseY);
        if (removeIndex >= 0 && removeIndex < createdEffects.size()) {
            Effects removedEffect = createdEffects.remove(removeIndex);
            showClientMessage("Removed rune effect: " + removedEffect.name());
            return true;
        }

        int entryIndex = getDraftEntryIndexAt(mouseX, mouseY);
        if (entryIndex >= 0 && entryIndex < createdEffects.size()) {
            draggingEffectIndex = entryIndex;
            dragTargetIndex = entryIndex;
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (draggingEffectIndex >= 0) {
            if (dragTargetIndex >= 0 && dragTargetIndex < createdEffects.size() && dragTargetIndex != draggingEffectIndex) {
                Effects movedEffect = createdEffects.remove(draggingEffectIndex);
                createdEffects.add(dragTargetIndex, movedEffect);
                showClientMessage("Moved rune effect to slot " + (dragTargetIndex + 1) + ".");
            }
            draggingEffectIndex = -1;
            dragTargetIndex = -1;
            return true;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }


    private void resetCanvasOnStroke() {
        for (int row = 0; row < canvasSize; row++) {
            for (int col = 0; col < canvasSize; col++) {
                canvasState[row][col] = 0; // Reset all cells to 0
            }
        }
    }

    private void showClientMessage(String message) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            player.displayClientMessage(Component.literal(message), true);
        }
    }

    private void loadEffectsFromHeldSpellBook() {
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        InteractionHand spellBookHand = getSpellBookHand(player);
        if (spellBookHand == null) {
            return;
        }

        ItemStack spellBook = player.getItemInHand(spellBookHand);
        createdEffects.clear();
        createdEffects.addAll(SpellBookItem.getStoredEffects(spellBook));
    }

    private void renderSpellStatus(GuiGraphics guiGraphics, int startX, int startY, int canvasWidth, int canvasHeight) {
        Player player = Minecraft.getInstance().player;
        InteractionHand spellBookHand = getSpellBookHand(player);
        int panelX = startX - DRAFT_PANEL_X_OFFSET;
        int panelY = startY;
        int panelWidth = DRAFT_PANEL_WIDTH;
        int panelHeight = canvasHeight;

        guiGraphics.fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, 0xAA111111);

        guiGraphics.drawString(this.font, "Spell Draft", panelX + 8, panelY + 8, 0xFFFFFF, false);
        if (spellBookHand == null) {
            guiGraphics.drawString(this.font, "No spell book", panelX + 8, panelY + 24, 0xFF8080, false);
            guiGraphics.drawString(this.font, "in hand", panelX + 8, panelY + 36, 0xFF8080, false);
        } else {
            guiGraphics.drawString(this.font, "Editing: " + formatHand(spellBookHand), panelX + 8, panelY + 24, 0xA0E0FF, false);
        }

        if (createdEffects.isEmpty()) {
            guiGraphics.drawString(this.font, "No effects yet", panelX + 8, panelY + 56, 0xC0C0C0, false);
            guiGraphics.drawString(this.font, "Draw + confirm", panelX + 8, panelY + 68, 0xC0C0C0, false);
            return;
        }

        int textY = panelY + DRAFT_ENTRY_START_Y;
        for (int i = 0; i < createdEffects.size() && i < DRAFT_ENTRY_VISIBLE_COUNT; i++) {
            int entryTop = textY - 2;
            int entryBottom = entryTop + DRAFT_ENTRY_HEIGHT;
            int backgroundColor = 0x66333333;

            if (i == draggingEffectIndex) {
                backgroundColor = 0xAA4B3D00;
            } else if (i == dragTargetIndex && draggingEffectIndex >= 0) {
                backgroundColor = 0xAA1F5C3F;
            }

            guiGraphics.fill(panelX + 6, entryTop, panelX + panelWidth - 6, entryBottom, backgroundColor);
            guiGraphics.drawString(this.font, (i + 1) + ". " + shortenEffect(createdEffects.get(i)), panelX + 8, textY, 0xFFFFFF, false);
            guiGraphics.fill(panelX + panelWidth - 18, entryTop + 1, panelX + panelWidth - 8, entryBottom - 1, 0xAA7A2020);
            guiGraphics.drawString(this.font, "X", panelX + panelWidth - 15, textY, 0xFFFFFF, false);
            textY += DRAFT_ENTRY_HEIGHT;
        }

        if (createdEffects.size() > DRAFT_ENTRY_VISIBLE_COUNT) {
            guiGraphics.drawString(this.font, "+" + (createdEffects.size() - DRAFT_ENTRY_VISIBLE_COUNT) + " more", panelX + 8, textY, 0xC0C0C0, false);
        } else {
            guiGraphics.drawString(this.font, "Drag entries to reorder", panelX + 8, panelY + panelHeight - 22, 0xC0C0C0, false);
        }
    }

    private String formatHand(InteractionHand hand) {
        return hand == InteractionHand.MAIN_HAND ? "Main Hand" : "Off Hand";
    }

    private String shortenEffect(Effects effect) {
        return switch (effect) {
            case FORM_CHESTPLATE -> "FORM_CHEST";
            case REALITY_MARBLE -> "REALITY";
            default -> effect.name();
        };
    }

    private int getDraftEntryIndexAt(double mouseX, double mouseY) {
        int cellSize = 10;
        int canvasWidth = canvasSize * cellSize;
        int canvasHeight = canvasSize * cellSize;
        int startX = (this.width - canvasWidth) / 2;
        int startY = (this.height - canvasHeight) / 2;

        int panelX = startX - DRAFT_PANEL_X_OFFSET;
        int panelY = startY;
        int localX = (int) mouseX;
        int localY = (int) mouseY;

        if (localX < panelX + 6 || localX > panelX + DRAFT_PANEL_WIDTH - 20) {
            return -1;
        }

        int entriesTop = panelY + DRAFT_ENTRY_START_Y - 2;
        if (localY < entriesTop) {
            return -1;
        }

        int index = (localY - entriesTop) / DRAFT_ENTRY_HEIGHT;
        if (index < 0 || index >= Math.min(createdEffects.size(), DRAFT_ENTRY_VISIBLE_COUNT)) {
            return -1;
        }
        return index;
    }

    private int getRemoveEntryIndexAt(double mouseX, double mouseY) {
        int cellSize = 10;
        int canvasWidth = canvasSize * cellSize;
        int canvasHeight = canvasSize * cellSize;
        int startX = (this.width - canvasWidth) / 2;
        int startY = (this.height - canvasHeight) / 2;

        int panelX = startX - DRAFT_PANEL_X_OFFSET;
        int panelY = startY;
        int localX = (int) mouseX;
        int localY = (int) mouseY;

        if (localX < panelX + DRAFT_PANEL_WIDTH - 18 || localX > panelX + DRAFT_PANEL_WIDTH - 8) {
            return -1;
        }

        int entriesTop = panelY + DRAFT_ENTRY_START_Y - 2;
        if (localY < entriesTop) {
            return -1;
        }

        int index = (localY - entriesTop) / DRAFT_ENTRY_HEIGHT;
        if (index < 0 || index >= Math.min(createdEffects.size(), DRAFT_ENTRY_VISIBLE_COUNT)) {
            return -1;
        }
        return index;
    }

    private InteractionHand getSpellBookHand(Player player) {
        if (player == null) {
            return null;
        }
        return SpellBookItem.findSpellBookHand(player);
    }
}
