package com.kerbybit.chattriggers.commands;

import com.kerbybit.chattriggers.globalvars.Settings;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.references.BugTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandT extends CommandBase{

    public String getName() {return getCommandName();}
    public String getUsage(ICommandSender sender) {return getCommandUsage(sender);}
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        processCommand(sender, args);
    }

	public String getCommandName() {return "t";}

	public String getCommandUsage(ICommandSender sender) {return "/trigger [create/add/list] <...>";}

	public int getRequiredPermissionLevel() {return 0;}

	public void processCommand(ICommandSender sender, String[] args) {
        try {
            if (global.canUse) {
                if (Settings.commandT) {
                    CommandTrigger.doCommand(args, false);
                } else {
                    StringBuilder send = new StringBuilder();
                    for (String arg : args) {send.append(arg).append(" ");}
                    Minecraft.getMinecraft().player.sendChatMessage("/t " + send.toString().trim());
                }
            }
        } catch (Exception e) {
            BugTracker.show(e, "command");
        }
	}
}