package com.github.supernova.commands;

import com.github.supernova.modules.ModuleAnnotation;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Command {

    public Minecraft mc = Minecraft.getMinecraft();

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public ArrayList<String> getUsages() {
        return usages;
    }
    public boolean isUsage(String usage) {
        return usages.contains(usage);
    }
    private String name;
    private String description;
    private ArrayList<String> usages;

    public Command() {
        if (!this.getClass().isAnnotationPresent(CommandAnnotation.class)) return;
        CommandAnnotation annotation = this.getClass().getAnnotation(CommandAnnotation.class);
        this.name = annotation.name();
        this.description = annotation.description();
        this.usages = new ArrayList<>(Arrays.asList(annotation.usages()));
    }

    public abstract void execute(String... args);
}
