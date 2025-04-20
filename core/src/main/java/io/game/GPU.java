package io.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class GPU extends Obstacle {

    private Animation<TextureRegion> activationAnimation;
    private TextureRegion[] frames;

    private Texture idleTexture;
    private Texture doneTexture;

    private TextureRegion idleRegion;
    private TextureRegion doneRegion;

    private float stateTime;
    private boolean isWorking;
    private boolean workComplete;

    private ProcessItem storedProcessItem;
    private float activationRadius = 200f;
    private TextureRegion currentFrame;

    public GPU(float x, float y, float width, float height) {
        super(x, y, width, height);
        this.isWorking = false;
        this.workComplete = false;

        idleTexture = new Texture("Overclocked Assets/Workstations/GPU_Idle.PNG");
        doneTexture = new Texture("Overclocked Assets/Workstations/GPU_Done.PNG");

        idleRegion = new TextureRegion(idleTexture);
        doneRegion = new TextureRegion(doneTexture);

        loadAnimation();
        currentFrame = new TextureRegion(idleTexture);
    }

    private void loadAnimation() {
        frames = new TextureRegion[30];
        int frameIndex = 0;

        // Frames 0001 to 0009
        for (int i = 1; i < 10; i++) {
            Texture frameTexture = new Texture("Overclocked Assets/Workstations/GPU_Loading/GPU_Loading_000" + i + ".png");
            frames[frameIndex++] = new TextureRegion(frameTexture);
        }

        // Frames 0010 to 0030
        for (int i = 10; i <= 30; i++) {
            Texture frameTexture = new Texture("Overclocked Assets/Workstations/GPU_Loading/GPU_Loading_00" + i + ".png");
            frames[frameIndex++] = new TextureRegion(frameTexture);
        }

        activationAnimation = new Animation<>(0.05f, frames);
        activationAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    public void update(float delta) {
        if (isWorking && !workComplete) {
            stateTime += delta;
            currentFrame = activationAnimation.getKeyFrame(stateTime, false);

            if (activationAnimation.isAnimationFinished(stateTime)) {
                isWorking = false;
                workComplete = true;
                currentFrame = doneRegion;
            }
        } else if (!isWorking && !workComplete) {
            currentFrame = idleRegion;
        }
    }

    public boolean isInRange(float playerX, float playerY) {
        float dx = (playerX + getBounds().width / 2f) - (getBounds().x + getBounds().width / 2f);
        float dy = (playerY + getBounds().height / 2f) - (getBounds().y + getBounds().height / 2f);
        return Math.sqrt(dx * dx + dy * dy) < activationRadius;
    }

    public boolean interact(Inventory inventory) {
        int slot = inventory.getSelectedHotbarIndex();
        Item item = inventory.getItem(slot);

        // Start working if idle and valid process item inserted
        if (storedProcessItem == null && !isWorking && !workComplete) {
            if (item instanceof ProcessItem && !((ProcessItem) item).getProcess().isStepDone(Process.StepType.CPU)) {
                storedProcessItem = (ProcessItem) item;
                inventory.setItem(slot, null);
                startWorking();
                return true;
            }
        }

        // If work is done, give back the processed item
        else if (storedProcessItem != null && workComplete && item == null) {
            Process process = storedProcessItem.getProcess();

            // Complete the CPU step for the process
            process.completeStep(Process.StepType.CPU);

            // Update the texture of the ProcessItem (for example, if process is completed)
            // You can check for other steps or conditions, but assuming it's done after CPU step

            Texture newTexture = getTextureForProcess(process); // Custom method to get the updated texture
            storedProcessItem.setTexture(newTexture); // Update the texture of the ProcessItem

            // Place the ProcessItem back in the inventory
            inventory.setItem(slot, storedProcessItem);

            // Reset the GPU state
            storedProcessItem = null;
            workComplete = false;

            return true;
        }

        return false;
    }

    private Texture getTextureForProcess(Process process) {
        String basePath = "Overclocked Assets/Data Packets/";

        switch (process.getColor()) {
            case SINGLE:
                return new Texture(basePath + "Single_Done.PNG");


            case DOUBLE:
                if (process.isStepDone(Process.StepType.IO)) {
                    return new Texture(basePath + "Double_Done.PNG");
                } else {
                    return new Texture(basePath + "Double_HalfA.PNG");
                }

            default:
                // Fallback texture
                return new Texture(basePath + "Single_Done.png");
        }
    }

    private void startWorking() {
        stateTime = 0;
        isWorking = true;
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public boolean isBusy() {
        return isWorking || (storedProcessItem != null && !workComplete);
    }
}
