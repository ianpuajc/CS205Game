package io.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class InventoryUI extends Table {
    private Inventory inventory;
    private Slot[] slots = new Slot[8];

    public InventoryUI(Inventory inventory, Texture slotTexture) {
        this.inventory = inventory;
        for (int i = 0; i < 8; i++) {
            int slotIndex = i + 4; // Slots 4-11
            slots[i] = new Slot(slotIndex, slotTexture);
            add(slots[i]).size(192, 192).pad(5);
            if ((i + 1) % 4 == 0) row();
        }
    }

    public void update() {
        for (int i = 0; i < 8; i++) {
            Item item = inventory.getItem(i+4);
            slots[i].setItemTexture(item != null ? item.getTexture() : null);
        }
    }

    public Slot[] getSlots() {
        return slots;
    }
}
