package com.github.supernova.value.impl;

import com.github.supernova.util.render.ColourUtil;
import com.github.supernova.util.render.RenderUtil;
import com.github.supernova.value.Value;

import java.awt.*;

public class ColourValue extends Value<Color> {

	ColourModes colourMode = ColourModes.DEFAULT;

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

	public Color getCurrentValue(int offset, int speed) {
		if (colourMode == ColourModes.ASTOLFO) {
			return ColourUtil.astolfoColour(offset*8, speed);
		}
		Color baseColour = super.getCurrentValue();
		if (colourMode == ColourModes.DEFAULT) {
			return baseColour;
		}
		if (colourMode == ColourModes.WAVY) {
			return ColourUtil.interpolateColorsDynamic(10, offset * 10, currentValue, currentValue.darker().darker());
		}
		return new Color(0xFFAA60BB);
	}

	public Color getCurrentValue(int offset) {
		return getCurrentValue(offset, 10000);
	}

	@Override
	public Color getCurrentValue() {
		return getCurrentValue(0, 10000);
	}

	public void setMode(ColourModes mode) {
		colourMode = mode;
	}

	public void switchMode() {
		if (colourMode == ColourModes.DEFAULT) {
			colourMode = ColourModes.ASTOLFO;
		} else if (colourMode == ColourModes.ASTOLFO) {
			colourMode = ColourModes.WAVY;
		} else if (colourMode == ColourModes.WAVY) {
			colourMode = ColourModes.DEFAULT;
		}
	}

	public enum ColourModes {
		DEFAULT,
		ASTOLFO,
		WAVY;
	}
}
