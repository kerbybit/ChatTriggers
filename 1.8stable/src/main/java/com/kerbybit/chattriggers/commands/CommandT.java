package com.kerbybit.chattriggers.commands;

import com.kerbybit.chattriggers.globalvars.global;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class CommandT extends CommandBase {

	public String getCommandName() {return "t";}

	public String getCommandUsage(ICommandSender sender) {return "/trigger [create/add/list] <...>";}

	public int getRequiredPermissionLevel() {return 0;}
	
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		if (global.settings.get(6).equalsIgnoreCase("true")) {
			CommandTrigger.doCommand(args, false);
		} else {
			String send = "";
			for (String arg : args) {send += arg + " ";}
			Minecraft.getMinecraft().thePlayer.sendChatMessage("/t " + send.trim());
		}

	}



    public String getName() {
        return "t";
    }

    public void execute(ICommandSender sender, String[] args) throws CommandException {
        if (global.settings.get(6).equalsIgnoreCase("true")) {
            CommandTrigger.doCommand(args, false);
        } else {
            String send = "";
            for (String arg : args) {send += arg + " ";}
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/t " + send.trim());
        }
    }
}