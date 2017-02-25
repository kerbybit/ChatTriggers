package com.kerbybit.chattriggers.commands;

import java.util.ArrayList;
import java.util.Arrays;

import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.references.BugTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandTR extends CommandBase {

	public String getCommandName() {return "tr";}
    public String getName() {return getCommandName();}

	public String getCommandUsage(ICommandSender sender) {return "/trigger run [trigger name]";}
    public String getUsage(ICommandSender sender) {return getCommandUsage(sender);}

	public int getRequiredPermissionLevel() {return 0;}

	public void processCommand(ICommandSender sender, String[] args) {
        try {
            if (global.canUse) {
                if (global.settings.get(7).equalsIgnoreCase("true")) {
                    ArrayList<String> temporary = new ArrayList<String>();
                    temporary.add("run");
                    temporary.addAll(Arrays.asList(args));
                    CommandTrigger.doCommand(temporary.toArray(new String[temporary.size()]), false);
                } else {
                    String send = "";
                    for (String arg : args) {send += arg + " ";}
                    Minecraft.getMinecraft().player.sendChatMessage("/tr " + send.trim());
                }
            }
        } catch (Exception e) {
            BugTracker.show(e, "command");
        }
	}

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        processCommand(sender, args);
    }
}
