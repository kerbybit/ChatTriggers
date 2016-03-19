package com.kerbybit.chattriggers;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandT extends CommandBase{

	public String getCommandName() {return "t";}

	public String getCommandUsage(ICommandSender sender) {return "/trigger [create/add/list] <...>";}

	public int getRequiredPermissionLevel() {return 0;}
	
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		CommandTrigger.doCommand(args, false);
	}

	public String getName() {
		return "t";
	}

	public void execute(ICommandSender sender, String[] args) throws CommandException {
		CommandTrigger.doCommand(args, false);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		CommandTrigger.doCommand(args, false);
	}
}