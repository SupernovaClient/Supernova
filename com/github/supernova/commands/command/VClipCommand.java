package com.github.supernova.commands.command;

import com.github.supernova.Supernova;
import com.github.supernova.commands.Command;
import com.github.supernova.commands.CommandAnnotation;
import net.minecraft.client.Minecraft;

@CommandAnnotation(name = "VClip", description = "Teleports Downwards", usages = {"vclip"})
public class VClipCommand extends Command {
    @Override
    public void execute(String... args) {
        if(args.length != 1) {
            Supernova.INSTANCE.chat("§cInvalid Usage.");
            return;
        }
        try {
            int amount = Integer.parseInt(args[0]);
            mc.thePlayer.setPositionAndUpdate(mc.thePlayer.posX, mc.thePlayer.posY+amount, mc.thePlayer.posZ);
        } catch (NumberFormatException e) {
            Supernova.INSTANCE.chat("§cInvalid Usage.");
        }
    }
}
