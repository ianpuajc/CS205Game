package io.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.Collections;
import java.util.List;

public class InstructionRegisterUI extends Table {
    private InstructionRegister instructionRegister;
    private Inventory inventory;
    private Texture singleUndone, singleDone;
    private Texture doubleUndone, doubleHalfA, doubleHalfB, doubleDone;

    public InstructionRegisterUI(InstructionRegister instructionRegister,
                                 Inventory inventory,
                                 Texture singleUndone,
                                 Texture singleDone,
                                 Texture doubleUndone,
                                 Texture doubleHalfA,
                                 Texture doubleHalfB,
                                 Texture doubleDone) {
        this.instructionRegister = instructionRegister;
        this.inventory = inventory;

        this.singleUndone = singleUndone;
        this.singleDone = singleDone;
        this.doubleUndone = doubleUndone;
        this.doubleHalfA = doubleHalfA;
        this.doubleHalfB = doubleHalfB;
        this.doubleDone = doubleDone;

        align(Align.topLeft);
        pad(100);
        setFillParent(true);
        setTouchable(Touchable.enabled);
    }

    public void update() {
        clear(); // Clear the table before redrawing everything

        List<Process> processes = instructionRegister.getAllProcesses();
        Collections.reverse(processes); // make oldest at bottom

        for (final Process process : processes) {
            Image processIcon = new Image(getTextureForProcess(process));
            processIcon.setTouchable(Touchable.enabled);

            // Debug log to ensure click is working
            processIcon.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("Clicked process: " + process.getId());

                    int freeSlot = getFirstEmptySlot();
                    if (freeSlot != -1) {
                        instructionRegister.removeProcess(process);
                        Texture icon = getTextureForProcess(process);
                        inventory.setItem(freeSlot, new ProcessItem(icon, process.getId(), process));
                    } else {
                        System.out.println("Inventory full!");
                    }
                }
            });

            row(); // vertical stacking
            add(processIcon).size(128).pad(4).left();
        }
    }

    public Texture getTextureForProcess(Process process) {
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

    private int getFirstEmptySlot() {
        for (int i = 0; i < 12; i++) {
            if (inventory.getItem(i) == null) return i;
        }
        return -1;
    }
}
