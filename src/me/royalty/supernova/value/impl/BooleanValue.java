package me.royalty.supernova.value.impl;

import me.royalty.supernova.value.Value;

public class BooleanValue extends Value<Boolean> {
    public BooleanValue(String settingName, boolean enabled) {
        super(settingName, enabled);
    }
    public void toggleValue() {
        super.setCurrentValue(!super.getCurrentValue());
    }
}
