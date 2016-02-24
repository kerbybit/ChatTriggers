package com.kerbybit.chattriggers;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class CommandT extends CommandBase{

	public String getCommandName() {return "t";}

	public String getCommandUsage(ICommandSender sender) {return "/trigger [create/add/list] <...>";}

	public int getRequiredPermissionLevel() {return 0;}
	
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		CommandTrigger.doCommand(args, false);
	}

	@Override
	public String getName() {
		return "t";
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		CommandTrigger.doCommand(args, false);
	}
}