package com.kerbybit.chattriggers.commands;

import java.util.ArrayList;
import java.util.Arrays;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.global;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class CommandTR extends CommandBase {

	public String getCommandName() {return "tr";}

	public String getCommandUsage(ICommandSender sender) {return "/trigger run [trigger name]";}

	public int getRequiredPermissionLevel() {return 0;}

	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        try {
            if (global.settings.get(7).equalsIgnoreCase("true")) {
                ArrayList<String> temporary = new ArrayList<String>();
                temporary.add("run");
                temporary.addAll(Arrays.asList(args));
                CommandTrigger.doCommand(temporary.toArray(new String[temporary.size()]), false);
            } else {
                String send = "";
                for (String arg : args) {send += arg + " ";}
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/tr " + send.trim());
            }
        } catch (Exception e) {
            for (StackTraceElement stack : e.getStackTrace()) {
                if (stack.toString().toUpperCase().contains("CHATTRIGGERS")) {
                    global.bugReport.add(stack.toString());
                }
            }
            ChatHandler.warn(ChatHandler.color("red","An unknown error occurred while performing this command"));
            ChatHandler.warn("&4Click clickable(&c[HERE],run_command,/trigger submitbugreport,Send a bug report) &4to submit a bug report");
        }
	}
}
