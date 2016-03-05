package com.kerbybit.chattriggers.commands;

import java.util.ArrayList;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class CommandTR extends CommandBase {

	public String getCommandName() {return "tr";}

	public String getCommandUsage(ICommandSender sender) {return "/trigger run [trigger name]";}

	public int getRequiredPermissionLevel() {return 0;}

	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		ArrayList<String> temporary = new ArrayList<String>();
		temporary.add("run");
		for (String value : args) {temporary.add(value);}
		CommandTrigger.commandRun(temporary.toArray(new String[temporary.size()]),false);
	}

}
