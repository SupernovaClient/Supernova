package com.github.supernova.commands.command;

import com.github.supernova.Supernova;
import com.github.supernova.commands.Command;
import com.github.supernova.commands.CommandAnnotation;
import com.github.supernova.modules.ModuleManager;
import com.github.supernova.modules.render.HUD;

import java.util.Arrays;

@CommandAnnotation(name = "Watermark", description = "Changes Watermark", usages = {"watermark"})
public class WatermarkCommand extends Command {

    @Override
    public void execute(String... args) {
        HUD hud = (HUD) ModuleManager.INSTANCE.get(HUD.class);
        String newWatermark = String.join(" ",args);
        newWatermark = newWatermark.equals("") ? "Supernova" : newWatermark;
        hud.setWatermark(newWatermark);
        Supernova.INSTANCE.chat("§dSupernova §f| §aSet client name to §7"+newWatermark);
    }
}
