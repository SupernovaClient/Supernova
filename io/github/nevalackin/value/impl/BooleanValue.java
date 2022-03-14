package io.github.nevalackin.value.impl;

import io.github.nevalackin.value.Value;

public class BooleanValue extends Value<Boolean> {
    public BooleanValue(String settingName, boolean enabled) {
        super(settingName, enabled);
    }
    public void toggleValue() {
        super.setCurrentValue(!super.getCurrentValue());
    }
}
