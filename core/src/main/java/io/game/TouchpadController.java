package io.game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class TouchpadController {

    public static Touchpad createTouchpad() {
        final int touchPadYLoc = 50;
        final int touchPadXLoc = 50;
        final int touchPadWidth = 400;
        final int touchPadHeight = 400;

        Skin touchpadSkin = new Skin();
        touchpadSkin.add("touchBackground", new Texture("touchpad_background.png"));
        touchpadSkin.add("touchKnob", new Texture("touchpad_knob.png"));

        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        touchpadStyle.background = touchpadSkin.getDrawable("touchBackground");
        touchpadStyle.knob = touchpadSkin.getDrawable("touchKnob");

        Touchpad touchpad = new Touchpad(10, touchpadStyle);

        touchpad.setBounds(touchPadXLoc, touchPadYLoc, touchPadWidth, touchPadHeight);

        return touchpad;
    }
}
