package com.kerbybit.chattriggers.commands;

import java.util.ArrayList;
import java.util.Arrays;

import com.kerbybit.chattriggers.globalvars.Settings;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.references.BugTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandTR extends CommandBase {

    public String getName() {return getCommandName();}
    public String getUsage(ICommandSender sender) {return getCommandUsage(sender);}
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        processCommand(sender, args);
    }

	public String getCommandName() {return "tr";}

	public String getCommandUsage(ICommandSender sender) {return "/trigger run [trigger name]";}

	public int getRequiredPermissionLevel() {return 0;}

	public void processCommand(ICommandSender sender, String[] args) {
        try {
            if (global.canUse) {
                if (Settings.commandTR) {
                    ArrayList<String> temporary = new ArrayList<>();
                    temporary.add("run");
                    temporary.addAll(Arrays.asList(args));
                    CommandTrigger.commandRun(temporary.toArray(new String[temporary.size()]));
                } else {
                    StringBuilder send = new StringBuilder();
                    for (String arg : args) {send.append(arg).append(" ");}
                    Minecraft.getMinecraft().player.sendChatMessage("/tr " + send.toString().trim());
                }
            }
        } catch (Exception e) {
            BugTracker.show(e, "command");
        }
	}
}
