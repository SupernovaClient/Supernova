package com.github.supernova.modules;

public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    RENDER("Render"),
    MISC("Misc"),
    MACRO("Macro");

    public final String name;
    Category(String name) {
        this.name = name;
    }
}
