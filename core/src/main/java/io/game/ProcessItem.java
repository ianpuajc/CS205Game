package io.game;

import com.badlogic.gdx.graphics.Texture;

public class ProcessItem extends Item {
    private Process process;

    public ProcessItem(Texture texture, String name, Process process) {
        super(texture, name);
        this.process = process;
    }

    public Process getProcess() {
        return process;
    }
}
