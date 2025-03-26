package io.game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Slot extends Stack {
    private int index;
    private Image background;
    private Image itemImage;
    private boolean isSelected;

    public Slot(int index, Texture slotTexture) {
        this.index = index;
        background = new Image(slotTexture);
        add(background);
    }

    public void setItemTexture(Texture texture) {
        if (itemImage != null) itemImage.remove();
        if (texture != null) {
            itemImage = new Image(texture);
            add(itemImage);
        }
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
        // Add highlight effect, e.g., change border or tint (implement as needed)
        background.setColor(isSelected ? Color.YELLOW : Color.WHITE);
    }

    public int getIndex() {
        return index;
    }
}
