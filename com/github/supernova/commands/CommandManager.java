package com.github.supernova.commands;

import com.github.supernova.Supernova;
import com.github.supernova.commands.command.BindCommand;
import com.github.supernova.commands.command.HelpCommand;
import com.github.supernova.commands.command.VClipCommand;
import com.github.supernova.commands.command.WatermarkCommand;
import com.github.supernova.modules.Module;
import com.github.supernova.modules.ModuleManager;
import com.github.supernova.modules.combat.BowAura;
import com.github.supernova.modules.macro.CropNuker;
import com.github.supernova.modules.render.ClickGUI;
import com.github.supernova.modules.render.DebugESP;
import com.github.supernova.modules.render.HUD;
import com.github.supernova.modules.render.World;

import java.util.ArrayList;

public class CommandManager {

    public static final CommandManager INSTANCE = new CommandManager();

    private final ArrayList<Command> commands = new ArrayList<>();
    private boolean initiated;

    public void init() {
        if (initiated) return;
        commands.add(new WatermarkCommand());
        commands.add(new HelpCommand());
        commands.add(new VClipCommand());
        commands.add(new BindCommand());
        initiated = true;
    }

    public boolean call(String name, String... args) {
        for(Command command : commands) {
            if(command.isUsage(name)) {
                command.execute(args);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }
}
