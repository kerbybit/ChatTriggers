package com.kerbybit.chattriggers.commands;

import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.references.BugTracker;
import com.kerbybit.chattriggers.triggers.EventsHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandT extends CommandBase{

	public String getCommandName() {return "t";}

	public String getCommandUsage(ICommandSender sender) {return "/trigger [create/add/list] <...>";}

	public int getRequiredPermissionLevel() {return 0;}
	
	public void processCommand(ICommandSender sender, String[] args) {
        try {
            if (global.canUse) {
                if (global.settings.get(6).equalsIgnoreCase("true")) {
                    CommandTrigger.doCommand(args, false);
                } else {
                    String send = "";
                    for (String arg : args) {send += arg + " ";}
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/t " + send.trim());
                }
            } else {
                if (EventsHandler.randInt(0,5) == 0) {
                    BugTracker.show(null, "blacklisted");
                } else {
                    if (global.settings.get(6).equalsIgnoreCase("true")) {
                        CommandTrigger.doCommand(args, false);
                    } else {
                        String send = "";
                        for (String arg : args) {send += arg + " ";}
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("/t " + send.trim());
                    }
                }
            }
        } catch (Exception e) {
            BugTracker.show(e, "command");
        }
	}
}