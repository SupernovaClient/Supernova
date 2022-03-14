package me.royalty.supernova.value.impl;

import me.royalty.supernova.value.Value;

import java.awt.*;

public class ColourValue extends Value<Color> {
    public ColourValue(String settingName, Color defaultValue) {
        super(settingName, defaultValue);
    }
    public ColourValue(String settingName, int defaultValue) {
        super(settingName, new Color(defaultValue));
    }
    public int getInt() {
        return getCurrentValue().getRGB();
    }
    public void setInt(int colourInt) {
        setCurrentValue(new Color(colourInt));
    }
}
