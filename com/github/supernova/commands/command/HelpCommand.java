package com.github.supernova.commands.command;

import com.github.supernova.Supernova;
import com.github.supernova.commands.Command;
import com.github.supernova.commands.CommandAnnotation;
import com.github.supernova.commands.CommandManager;

@CommandAnnotation(name = "Help", description = "retard.", usages = {"h", "help"})
public class HelpCommand extends Command {

    @Override
    public void execute(String... args) {
        Supernova.INSTANCE.chat("Help");
        for(Command command : CommandManager.INSTANCE.getCommands()) {
            Supernova.INSTANCE.chat(command.getName()+" - "+command.getDescription()+" "+command.getUsages());
        }
    }
}
