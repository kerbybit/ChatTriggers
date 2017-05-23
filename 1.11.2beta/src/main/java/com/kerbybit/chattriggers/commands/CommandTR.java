package com.kerbybit.chattriggers.commands;

import java.util.ArrayList;
import java.util.Arrays;

import com.kerbybit.chattriggers.globalvars.Settings;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.references.BugTracker;
import com.kerbybit.chattriggers.triggers.EventsHandler;
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

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {processCommand(sender,args);}
	public void processCommand(ICommandSender sender, String[] args) {
        try {
            if (global.canUse) {
                if (Settings.commandTR) {
                    ArrayList<String> temporary = new ArrayList<String>();
                    temporary.add("run");
                    temporary.addAll(Arrays.asList(args));
                    CommandTrigger.doCommand(temporary.toArray(new String[temporary.size()]), false);
                } else {
                    StringBuilder send = new StringBuilder();
                    for (String arg : args) {send.append(arg).append(" ");}
                    Minecraft.getMinecraft().player.sendChatMessage("/tr " + send.toString().trim());
                }
            } else {
                if (EventsHandler.randInt(0,5) == 0) {
                    BugTracker.show(null, "blacklisted");
                } else {
                    if (Settings.commandTR) {
                        ArrayList<String> temporary = new ArrayList<String>();
                        temporary.add("run");
                        temporary.addAll(Arrays.asList(args));
                        CommandTrigger.doCommand(temporary.toArray(new String[temporary.size()]), false);
                    } else {
                        StringBuilder send = new StringBuilder();
                        for (String arg : args) {send.append(arg).append(" ");}
                        Minecraft.getMinecraft().player.sendChatMessage("/tr " + send.toString().trim());
                    }
                }
            }
        } catch (Exception e) {
            BugTracker.show(e, "command");
        }
	}
}
