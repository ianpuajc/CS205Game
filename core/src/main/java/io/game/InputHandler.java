package io.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Input;  // Add this at the top of your file

public class InputHandler {
    private Player player;
    private Touchpad touchpad;
    private GameLevel gameLevel;
    private Inventory inventory;
    private HotbarUI hotbarUI;
    private InventoryUI inventoryUI;
    private DragAndDrop dragAndDrop;



    public InputHandler(GameLevel gameLevel, Stage stage) {
        this.gameLevel = gameLevel;
        this.player = gameLevel.getPlayer();
        this.touchpad = TouchpadController.createTouchpad();
        stage.addActor(touchpad);
        setupInventory(stage);
        setUpRightInteractButton(stage);

    }

    private void setUpRightInteractButton(Stage stage){
        Texture buttonTexture = new Texture("button.png");
        Drawable upDrawable = new TextureRegionDrawable(new TextureRegion(buttonTexture));

        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();

        buttonStyle.up = upDrawable;

        ImageButton imageButton = new ImageButton(buttonStyle);



        stage.addActor(imageButton);

        imageButton.setSize(200, 200);

        imageButton.setPosition(Gdx.graphics.getWidth() - 300, 100);

        imageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ImageButton", "Button clicked!");

                // Short vibration feedback
                if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Vibrator)) {
                    Gdx.input.vibrate(50); // Vibrates for 50 milliseconds
                }
            }
        });


    }

    private void setupInventory(Stage stage){

        inventory = new Inventory(12);

        Texture slotTexture = new Texture("lineLight06.png");
        Texture inventoryIcon = new Texture("inventory_icon.png");


        // Initialize HotbarUI
        hotbarUI = new HotbarUI(inventory, slotTexture, inventoryIcon);
        stage.addActor(hotbarUI);
        hotbarUI.setPosition((Gdx.graphics.getWidth() - hotbarUI.getWidth()) / 2, 96); // Bottom of the screen

        // Initialize InventoryUI (hidden by default)
        inventoryUI = new InventoryUI(inventory, slotTexture);
        inventoryUI.setVisible(false);
        stage.addActor(inventoryUI);
        inventoryUI.setPosition(
            (Gdx.graphics.getWidth() - inventoryUI.getWidth()) / 2,
            (Gdx.graphics.getHeight() - inventoryUI.getHeight()) / 2
        ); // Center when visible

        hotbarUI.getInventoryButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                inventoryUI.setVisible(!inventoryUI.isVisible());
            }
        });

        dragAndDrop = new DragAndDrop();
        for (Slot slot : hotbarUI.getSlots()) {
            addDragAndDrop(dragAndDrop, slot);
        }
        for (Slot slot : inventoryUI.getSlots()) {
            addDragAndDrop(dragAndDrop, slot);
        }
    }

    public void update(float deltaTime) {
        float knobPercentX = touchpad.getKnobPercentX();
        float knobPercentY = touchpad.getKnobPercentY();

        // Calculate movement based on touchpad input
        float moveX = knobPercentX * player.getSpeed() * deltaTime;
        float moveY = knobPercentY * player.getSpeed() * deltaTime;

        // Update player's position and animation state
        if (moveX != 0 || moveY != 0) {
            Vector2 direction = new Vector2(knobPercentX, knobPercentY).nor();
            player.move(moveX, moveY, gameLevel.getObstacles());
            player.updateAnimationState(direction);
        } else {
            player.setIdleAnimation();
        }

        updateUI();
        hotbarUI.update();
        inventoryUI.update();
    }


    private void addDragAndDrop(DragAndDrop dragAndDrop, Slot slot) {
        dragAndDrop.addSource(new DragAndDrop.Source(slot) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                Item item = inventory.getItem(slot.getIndex());
                if (item == null) return null;
                DragAndDrop.Payload payload = new DragAndDrop.Payload();
                payload.setObject(item);
                payload.setDragActor(new Image(item.getTexture()));
                return payload;
            }
        });

        dragAndDrop.addTarget(new DragAndDrop.Target(slot) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                return true;
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                Slot sourceSlot = (Slot) source.getActor();
                Slot targetSlot = (Slot) getActor();
                int sourceIndex = sourceSlot.getIndex();
                int targetIndex = targetSlot.getIndex();
                Item temp = inventory.getItem(sourceIndex);
                inventory.setItem(sourceIndex, inventory.getItem(targetIndex));
                inventory.setItem(targetIndex, temp);
                updateUI();
            }
        });
    }

    private void updateUI() {
        for (Slot slot : hotbarUI.getSlots()) {
            Item item = inventory.getItem(slot.getIndex());
            slot.setItemTexture(item != null ? item.getTexture() : null);
        }
        inventoryUI.update();
    }
}
