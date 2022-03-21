package com.github.supernova.modules;

public enum Category {
	COMBAT("Combat"),
	MOVEMENT("Movement"),
	PLAYER("Player"),
	RENDER("Render"),
	OTHER("Other"),
	MACRO("Macro");

	final String name;

	Category(String name) {
		this.name = name;
	}
}
