package com.github.supernova.commands.command;

import com.github.supernova.Supernova;
import com.github.supernova.commands.Command;
import com.github.supernova.commands.CommandAnnotation;
import com.github.supernova.modules.Module;
import com.github.supernova.modules.ModuleManager;
import org.lwjgl.input.Keyboard;

@CommandAnnotation(name = "Bind", description = "Bind Modules", usages = {"bind", "b"})
public class BindCommand extends Command {
    @Override
    public void execute(String... args) {
        if(args.length != 2) {
            Supernova.INSTANCE.chat("§cInvalid Usage.");
            return;
        }
        Module module = ModuleManager.INSTANCE.get(args[0]);
        if(module == null) {
            Supernova.INSTANCE.chat("§cInvalid Module.");
            return;
        }
        int keyCode = Keyboard.getKeyIndex(args[1].toUpperCase());
        Supernova.INSTANCE.chat("§dSupernova §f| §7"+module.getModuleName()+" Has been bound to "+Keyboard.getKeyName(keyCode));
        module.setKeyCode(keyCode);
    }
}
