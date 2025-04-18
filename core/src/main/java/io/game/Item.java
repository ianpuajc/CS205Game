package io.game;

import com.badlogic.gdx.graphics.Texture;

public class Item {
    private Texture texture;
    private String name;

    public Item(Texture texture, String name) {
        this.texture = texture;
        this.name = name;
    }

    public Item(Texture texture){
        this.texture = texture;
    }

    public Texture getTexture() {
        return texture;
    }

    public String getName() {
        return name;
    }
}
