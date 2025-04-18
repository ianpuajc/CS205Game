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



    public InputHandler(GameLevel gameLevel, Stage stage, Inventory inventory) {
        this.gameLevel = gameLevel;
        this.player = gameLevel.getPlayer();
        this.inventory = inventory; // use the one passed in
        this.touchpad = TouchpadController.createTouchpad();
        stage.addActor(touchpad);
        setupInventory(stage);
        setUpRightInteractButton(stage);
    }

    public Texture getTextureForProcess(Process process) {

        Texture singleUndone =  new Texture("Overclocked Assets/Data Packets/Single_Undone.PNG");
        Texture singleDone = new Texture("Overclocked Assets/Data Packets/Single_Done.PNG");
        Texture doubleUndone = new Texture("Overclocked Assets/Data Packets/Double_Undone.PNG");
        Texture doubleHalfA = new Texture("Overclocked Assets/Data Packets/Double_HalfA.PNG");
        Texture doubleHalfB = new Texture("Overclocked Assets/Data Packets/Double_HalfB.PNG");
        Texture doubleDone = new Texture("Overclocked Assets/Data Packets/Double_Done.PNG");


        int done = process.getStepsCompletedCount();
        int total = process.getTotalStepsCount();

        if (total == 1) {
            return done == 1 ? singleDone : singleUndone;
        } else if (total == 2) {
            if (done == 0) return doubleUndone;
            if (done == 1) return doubleHalfA; // or doubleHalfB
            return doubleDone;
        }

        return singleUndone; // fallback
    }

    private void setUpRightInteractButton(Stage stage){
        Texture buttonTexture = new Texture("button.png");
        Drawable upDrawable = new TextureRegionDrawable(new TextureRegion(buttonTexture));

        ImageButton.ImageButtonStyle buttonStyle = new ImageButton.ImageButtonStyle();

        buttonStyle.up = upDrawable;

        ImageButton imageButton = new ImageButton(buttonStyle);

        InstructionRegister instructionRegister = gameLevel.getInstructionRegister();

        stage.addActor(imageButton);

        imageButton.setSize(200, 200);

        imageButton.setPosition(Gdx.graphics.getWidth() - 300, 100);

        imageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.log("ImageButton", "Button clicked!");

                if (Gdx.input.isPeripheralAvailable(Input.Peripheral.Vibrator)) {
                    Gdx.input.vibrate(50);
                }

                // ✅ First: Try submitting to OutputRegister
                for (Obstacle obstacle : gameLevel.getObstacles()) {
                    if (obstacle instanceof OutputRegister) {
                        OutputRegister outputRegister = (OutputRegister) obstacle;

                        float playerX = player.getBounds().x;
                        float playerY = player.getBounds().y;
                        float registerX = outputRegister.getBounds().x;
                        float registerY = outputRegister.getBounds().y;

                        float playerCenterX = playerX + player.getBounds().width / 2f;
                        float playerCenterY = playerY + player.getBounds().height / 2f;
                        float registerCenterX = registerX + outputRegister.getBounds().width / 2f;
                        float registerCenterY = registerY + outputRegister.getBounds().height / 2f;

                        float dx = playerCenterX - registerCenterX;
                        float dy = playerCenterY - registerCenterY;
                        float distance = (float) Math.sqrt(dx * dx + dy * dy);

                        if (distance < 150f) {
                            int slot = inventory.getSelectedHotbarIndex();
                            Item item = inventory.getItem(slot);

                            if (item instanceof ProcessItem) {
                                boolean accepted = outputRegister.submitProcess((ProcessItem) item);
                                if (accepted) {
                                    inventory.setItem(slot, null);
                                    Gdx.app.log("OutputRegister", "✅ Item submitted and accepted.");
                                    return; // Exit after successful submission
                                } else {
                                    Gdx.app.log("OutputRegister", "❌ Process rejected.");
                                    return;
                                }
                            } else {
                                Gdx.app.log("OutputRegister", "⚠️ No valid item in selected slot.");
                            }
                        }
                    }

                    if (obstacle instanceof GPU) {
                        GPU gpu = (GPU) obstacle;

                        float playerX = player.getBounds().x;
                        float playerY = player.getBounds().y;

                        if (gpu.isInRange(playerX, playerY)) {
                            boolean interacted = gpu.interact(inventory);
                            if (interacted) {
                                Gdx.app.log("GPU", "✅ Interaction successful.");
                            }
                        }
                    } else if (obstacle instanceof HardDrive) {
                        HardDrive hardDrive = (HardDrive) obstacle;

                        float playerX = player.getBounds().x;
                        float playerY = player.getBounds().y;

                        // Check if player is in range of the HardDrive
                        if (hardDrive.isInRange(playerX, playerY)) {
                            // Try to interact with the HardDrive
                            boolean interacted = hardDrive.interact(inventory);

                            // If interaction is successful, log the result
                            if (interacted) {
                                Gdx.app.log("HardDrive", "✅ Interaction successful.");
                            }
                        }
                    }
                }

                // ✅ If submission failed or not near output, try retrieving process
                InstructionRegister instructionRegister = gameLevel.getInstructionRegister();

                if (!instructionRegister.isEmpty()) {
                    int slot = inventory.getSelectedHotbarIndex();
                    if (inventory.getItem(slot) == null) {
                        Process process = instructionRegister.remove();
                        if (process != null) {
                            Item item = new ProcessItem(process, getTextureForProcess(process));
                            inventory.setItem(slot, item);
                            Gdx.app.log("Inventory", "📥 Retrieved ProcessItem into slot " + slot);
                        }
                    } else {
                        Gdx.app.log("Inventory", "Selected slot not empty: " + slot);
                    }
                } else {
                    Gdx.app.log("InstructionRegister", "📭 No process to retrieve.");
                }
            }

        });

    }

    private void setupInventory(Stage stage){

//        inventory = new Inventory(12);

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
