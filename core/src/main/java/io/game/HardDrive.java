package io.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.Timer;

public class HardDrive extends Obstacle {

    private boolean isActive = false;
    private boolean workComplete = false;

    private float animationDuration = 1.5f;
    private float stateTime = 0f;

    private Texture idleTexture;
    private Texture doneTexture;
    private Animation<TextureRegion> workingAnimation;

    private ProcessItem storedProcessItem;
    private TextureRegion currentFrame;

    public HardDrive(float x, float y, float width, float height) {
        super(x, y, width, height);
        loadTextures();
    }

    private void loadTextures() {
        idleTexture = new Texture("Overclocked Assets/Workstations/Harddisk_Idle.PNG");
        doneTexture = new Texture("Overclocked Assets/Workstations/Harddisk_Done.PNG");

        TextureRegion[] frames = new TextureRegion[15];
        for (int i = 1; i < 10; i++) {
            Texture frameTexture = new Texture("Overclocked Assets/Workstations/Harddisk_Loading/Harddisk_Loading_000" + i + ".png");
            frames[i-1] = new TextureRegion(frameTexture);
        }
        for (int i = 10; i <= 15; i++) {
            Texture frameTexture = new Texture("Overclocked Assets/Workstations/Harddisk_Loading/Harddisk_Loading_00" + i + ".png");
            frames[i-1] = new TextureRegion(frameTexture);
        }

        workingAnimation = new Animation<>(animationDuration / 30f, frames);
        currentFrame = new TextureRegion(idleTexture);
    }

    public void update(float delta) {
        if (isActive && !workComplete) {
            stateTime += delta;
            currentFrame = workingAnimation.getKeyFrame(stateTime, false);

            if (workingAnimation.isAnimationFinished(stateTime)) {
                isActive = false;
                workComplete = true;
                currentFrame = new TextureRegion(doneTexture);
            }
        } else if (!isActive && !workComplete) {
            currentFrame = new TextureRegion(idleTexture);
        }
    }
    public boolean isInRange(float playerX, float playerY) {
        float centerX = getBounds().x + getBounds().width / 2f;
        float centerY = getBounds().y + getBounds().height / 2f;

        float dx = playerX - centerX;
        float dy = playerY - centerY;

        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < 200f;
    }

    public boolean interact(Inventory inventory) {
        int slot = inventory.getSelectedHotbarIndex();
        Item item = inventory.getItem(slot);

        if (!isActive && !workComplete && item instanceof ProcessItem && storedProcessItem == null) {
            ProcessItem pi = (ProcessItem) item;
            if (pi.getProcess().getColor() != Process.ProcessColor.RED) {
                return false; // only accepts RED (case) processes
            }

            if (pi.getProcess().isStepDone(Process.StepType.IO)){
                return false;
            }

            storedProcessItem = pi;
            inventory.setItem(slot, null);
            isActive = true;
            stateTime = 0f;
            return true;

        } else if (storedProcessItem != null && workComplete && item == null) {
            Process process = storedProcessItem.getProcess();

            if (process.isStepDone(Process.StepType.CPU)){
                Texture newTexture = new Texture("Overclocked Assets/Data Packets/Double_Done.PNG");
                storedProcessItem = new ProcessItem(newTexture, "Processed", process);

            }else{
                Texture newTexture = new Texture("Overclocked Assets/Data Packets/Double_HalfB.PNG");
                storedProcessItem = new ProcessItem(newTexture, "Processed", process);
            }
            process.completeStep(Process.StepType.IO);

            // Replace with "Double_Done" texture only


            inventory.setItem(slot, storedProcessItem);

            storedProcessItem = null;
            workComplete = false;
            return true;
        }

        return false;
    }

    public Texture getTexture() {
        return currentFrame.getTexture();
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public void dispose() {
        idleTexture.dispose();
        doneTexture.dispose();
    }
}
