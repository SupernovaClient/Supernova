package io.github.nevalackin.modules;

public enum Category {
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    RENDER("Render"),
    MISC("Misc");

    final String name;
    Category(String name) {
        this.name = name;
    }
}
