package io.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class HotbarUI extends Table {
    private Inventory inventory;
//    private Image[] itemImages;
//    private Image selectionImage;
    private ImageButton inventoryButton;
    private Slot[] slots = new Slot[4];

    public HotbarUI(Inventory inventory, Texture slotTexture, Texture inventoryButtonTexture) {
        this.inventory = inventory;
        for (int i = 0; i < 4; i++) {
            Slot slot = new Slot(i, slotTexture);
            slots[i] = slot;
            add(slot).size(192, 192).pad(5);
            slot.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Slot tappedSlot = (Slot) event.getListenerActor();
                    inventory.setSelectedHotbarIndex(tappedSlot.getIndex());
                }
            });
        }

        inventoryButton = new ImageButton(new TextureRegionDrawable(inventoryButtonTexture));
        add(inventoryButton).size(192, 192).pad(5);

        // Initialize the selection image with the selection texture
//        selectionImage = new Image(selectionTexture);
//        addActor(selectionImage); // Add it to the table as an actor
        setSelectedSlot(inventory.getSelectedHotbarIndex());     // Set the initial position
    }

    public void update() {
        for (int i = 0; i < 4; i++) {
            Item item = inventory.getItem(i);
            slots[i].setItemTexture(item != null ? item.getTexture() : null);
        }
        setSelectedSlot(inventory.getSelectedHotbarIndex()); // Update the selection highlight position
    }

    public ImageButton getInventoryButton() {
        return inventoryButton;
    }

    public Slot[] getSlots() {
        return slots;
    }

    public void setSelectedSlot(int slotIndex) {
        for (Slot slot : slots) {
            slot.setSelected(slot.getIndex() == slotIndex);
        }
    }
}
