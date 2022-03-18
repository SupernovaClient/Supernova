package com.github.supernova.value.impl;

import com.github.supernova.value.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MultiEnumValue<T extends Enum<T>> extends Value<ArrayList<T>> {

    private final ArrayList<T> enumValues;

    public MultiEnumValue(String name, T[] values, T... enabledValues) {
        super(name, Arrays.stream(enabledValues).collect(Collectors.toCollection(ArrayList::new)));
        this.enumValues = Arrays.stream(values).collect(Collectors.toCollection(ArrayList::new));
    }


    public ArrayList<T> getAllValues() {
        return enumValues;
    }

    public ArrayList<T> getEnabledValues() {
        return currentValue;
    }

    public boolean isEnabled(T value) {
        return currentValue.contains(value);
    }

    public void toggleValue(T value) {
        if (enumValues.contains(value)) {
            if (!currentValue.remove(value)) {
                currentValue.add(value);
            }
        }
    }
}