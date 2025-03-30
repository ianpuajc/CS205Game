package io.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.List;

public class InstructionRegisterUI extends Table {
    private InstructionRegister instructionRegister;
    private Inventory inventory;
    private Texture greenTexture, blueTexture, redTexture;

    public InstructionRegisterUI(InstructionRegister instructionRegister,
                                 Inventory inventory,
                                 Texture greenTexture,
                                 Texture blueTexture,
                                 Texture redTexture) {
        this.instructionRegister = instructionRegister;
        this.inventory = inventory;

        this.greenTexture = greenTexture;
        this.blueTexture = blueTexture;
        this.redTexture = redTexture;

        align(Align.topLeft);
        pad(20);
        setFillParent(true);
    }

    public void update() {
        clear(); // Clear the table before redrawing everything

        List<Process> processes = instructionRegister.getAllProcesses();

        for (Process process : processes) {
            Image processIcon = new Image(getTextureForColor(process.getColor()));

            // Darken icon based on how many steps are completed
            float progress = (float) process.getStepsCompletedCount() / process.getTotalStepsCount();
            float brightness = 1.0f - (0.5f * progress); // full dark at 100%
            processIcon.setColor(brightness, brightness, brightness, 1);

            // Add a click listener to pick up the process
            processIcon.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    int freeSlot = getFirstEmptySlot();
                    if (freeSlot != -1) {
                        instructionRegister.removeProcess(process);

                        Texture icon = getTextureForColor(process.getColor());
                        ProcessItem item = new ProcessItem(icon, process.getId(), process);
                        inventory.setItem(freeSlot, item);
                    } else {
                        System.out.println("Inventory full!"); // Optional: show popup or sound
                    }
                }
            });

            // Add the icon to the UI
            add(processIcon).size(128).pad(8);
        }
    }

    private Texture getTextureForColor(Process.ProcessColor color) {
        switch (color) {
            case GREEN: return greenTexture;
            case BLUE:  return blueTexture;
            case RED:   return redTexture;
            default:    return greenTexture;
        }
    }

    private int getFirstEmptySlot() {
        for (int i = 0; i < 12; i++) {
            if (inventory.getItem(i) == null) return i;
        }
        return -1;
    }
}
