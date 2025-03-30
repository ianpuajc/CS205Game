package io.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Random;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Stage stage;
    private GameLevel gameLevel;
    private GameRenderer renderer;
    private InputHandler inputHandler;
    private Inventory inventory;
    private HotbarUI hotbarUI;
    private InventoryUI inventoryUI;
    private InstructionRegisterUI irUI;
    private DragAndDrop dragAndDrop;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f, 0);
        camera.update();
        inventory = new Inventory(12);

        stage = new Stage(new ScreenViewport());
        loadLevel(LevelLoader.getCurrentLevel());

        //Load textures for slots and selection
        Texture slotTexture = new Texture("slot.png");
        Texture inventoryIcon = new Texture("inventory_icon.png");

        //testing items
        Texture itemTexture1 = new Texture("item1.png");
        Texture itemTexture2 = new Texture("item2.png");

        Texture[] itemTextures = {itemTexture1, itemTexture2};

        // processes
        Texture greenTex = new Texture("process_green.png");
        Texture blueTex = new Texture("process_blue.png");
        Texture redTex = new Texture("process_red.png");

        // Populate with random items
        Random random = new Random();
        for (int i = 0; i < 12; i++) {
            if (random.nextBoolean()) { // 50% chance of an item
                int textureIndex = random.nextInt(itemTextures.length);
                inventory.setItem(i, new Item(itemTextures[textureIndex], ""));
            }
        }

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

        // Initialize IR
        irUI = new InstructionRegisterUI(
            gameLevel.getInstructionRegister(),
            inventory,
            greenTex, blueTex, redTex
        );
        stage.addActor(irUI);


        // Add listener to toggle inventory
        hotbarUI.getInventoryButton().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                inventoryUI.setVisible(!inventoryUI.isVisible());
            }
        });

        DragAndDrop dragAndDrop = new DragAndDrop();
        for (Slot slot : hotbarUI.getSlots()) {
            addDragAndDrop(dragAndDrop, slot);
        }
        for (Slot slot : inventoryUI.getSlots()) {
            addDragAndDrop(dragAndDrop, slot);
        }
    }

    private void updateUI() {
        for (Slot slot : hotbarUI.getSlots()) {
            Item item = inventory.getItem(slot.getIndex());
            slot.setItemTexture(item != null ? item.getTexture() : null);
        }
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

    private void loadLevel(int level) {
        if (gameLevel != null) gameLevel.dispose();
        gameLevel = LevelLoader.loadLevel(level);
        renderer = new GameRenderer(batch, camera, gameLevel, stage);
        inputHandler = new InputHandler(gameLevel, stage);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        hotbarUI.update();
        inventoryUI.update();
        irUI.update();

        float deltaTime = Gdx.graphics.getDeltaTime();
        gameLevel.update(deltaTime);
        inputHandler.update(deltaTime);
        renderer.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        gameLevel.dispose();
        stage.dispose();
    }
}
