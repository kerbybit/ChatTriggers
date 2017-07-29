package com.kerbybit.chattriggers.commands;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.util.*;
import java.util.List;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.file.FileHandler;
import com.kerbybit.chattriggers.file.UpdateHandler;
import com.kerbybit.chattriggers.globalvars.Settings;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.objects.DisplayHandler;
import com.kerbybit.chattriggers.objects.JsonHandler;
import com.kerbybit.chattriggers.objects.ListHandler;
import com.kerbybit.chattriggers.overlay.KillfeedHandler;
import com.kerbybit.chattriggers.overlay.NotifyHandler;
import com.kerbybit.chattriggers.references.BugTracker;
import com.kerbybit.chattriggers.triggers.*;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import static com.kerbybit.chattriggers.triggers.TriggerHandler.onChat;

public class CommandTrigger extends CommandBase {

    public String getCommandName() {return "trigger";}

    public void processCommand(ICommandSender sender, String[] args) {
        if (global.canUse) {
            try {
                doCommand(args, false);
            } catch (Exception e) {
                StringBuilder command = new StringBuilder("/trigger");
                for (String arg : args) {
                    command.append(" ").append(arg);
                }
                BugTracker.show(e, "command", command.toString());
            }
        }
    }

    public int getRequiredPermissionLevel() {return 0;}

    public String getCommandUsage(ICommandSender sender) {return "/trigger [create/add/list] <...>";}

    private static void logCommand(String args[]) {
        StringBuilder temp_command = new StringBuilder("/trigger");
        for (String arg : args) {
            temp_command.append(" ").append(arg);
        }
        global.lastCommand = temp_command.toString();
    }

    public static void doCommand(String args[], Boolean silent) {
        logCommand(args);
        String command = "null";
        if (args.length != 0) {
            command = args[0].toUpperCase();
        }
        switch (command) {
            case("FAIL"):
                commandFail();
                break;
            case("FILES"):
            case("FILE"):
                commandFiles();
                break;
            case("SUBMITBUGREPORT"):
                commandSubmitBugReport();
                break;
            case("SHOWBUGREPORT"):
                commandShowBugReport();
                break;
            case("SIMULATE"):
            case("SIM"):
                commandSimulate(args);
                break;
            case("EXECUTE"):
            case("EXEC"):
                commandExecute(args);
                break;
            case("HELP"):
            case("?"):
                commandHelp(args);
                break;
            case("COPY"):
                commandCopy(args, silent);
                break;
            case("CLEARCHAT"):
                commandClearChat();
                break;
            case("IMPORTS"):
                commandImports();
                break;
            case("RESET"):
                CommandReference.resetAll();
                break;
            case("IMPORT"):
                commandImport(args);
                break;
            case("DISABLEIMPORT"):
                commandDisableImport(args, silent);
                break;
            case("ENABLEIMPORT"):
                commandEnableImport(args, silent);
                break;
            case("RUN"):
                commandRun(args);
                break;
            case("CREATE"):
                commandCreate(args, silent);
                break;
            case("DELETE"):
                commandDelete(args, silent);
                break;
            case("ADD"):
                commandAdd(args, silent);
                break;
            case("REMOVE"):
                commandRemove(args, silent);
                break;
            case("STRING"):
                commandString(args, silent);
                break;
            case("LIST"):
                commandList(args);
                break;
            case("SETTINGS"):
                commandSettings(args);
                break;
            case("SAVE"):
                commandSave();
                break;
            case("LOAD"):
                commandLoad();
                break;
            case("TESTIMPORT"):
                commandTestImport(args);
                break;
            case("RELOAD"):
                commandReload();
                break;
            case("TEST"):
                commandTest();
                break;
            case("INFO"):
                commandInfo();
                break;
            default:
                ChatHandler.warn("&c/trigger &c[clickable(&ccreate,suggest_command,/trigger create ,&7Suggest &7/trigger &7create)&c/clickable(&cadd,suggest_command,/trigger add ,&7Suggest &7/trigger &7add)&c/clickable(&clist,run_command,/trigger list,&7Run &7/trigger &7list)&c/clickable(&cstring,suggest_command,/trigger string ,&7Suggest &7/trigger &7string)&c] &c<...>");
                ChatHandler.warn("&c/trigger &c[clickable(&csave,run_command,/trigger save,&7Run &7/trigger &7save)&c/clickable(&cload,run_command,/trigger load,&7Run &7/trigger 77load)&c]");
                ChatHandler.warn("&c/trigger &c[clickable(&crun,suggest_command,/trigger run ,&7Suggest &7/trigger &7run)&c/clickable(&cimport,suggest_command,/trigger import ,&7Suggest &7/trigger &7import)&c/clickable(&cimports,run_command,/trigger imports,&7Run &7/trigger &7imports)&c] &c<...>");
                break;
        }
    }

    private static void commandInfo() {
        ChatHandler.warnBreak(0);

        ChatHandler.warn(" &cChat&9Triggers");
        ChatHandler.warn("   &fVersion: " + Settings.col[0] + Settings.version);
        ChatHandler.warn("   &fRunning Beta: " + Settings.col[0] + Settings.isBeta);
        ChatHandler.warn("");
        ChatHandler.warn("   &fStrings Loaded: " + Settings.col[0] + global.USR_string.size());
        int totalStrings = global.USR_string.size() + global.TMP_string.size() + global.Async_string.size();
        ChatHandler.warn("   &fStrings In Memory: " + Settings.col[0] + totalStrings);
        ChatHandler.warn("");
        int totalEvents = 0;
        for (List<String> trigger : global.trigger) {
            if (trigger.size() > 2) {
                for (int i = 2; i < global.trigger.size(); i++) {
                    totalEvents++;
                }
            }
        }
        ChatHandler.warn("   &fEvents Loaded: " + Settings.col[0] + totalEvents);
        ChatHandler.warn("   &fTriggers Loaded: " + Settings.col[0] + global.trigger.size());
        int totalOthers = global.trigger.size() - (global.chatTrigger.size() + global.actionTrigger.size()
                + global.tickTrigger.size() + global.onWorldLoadTrigger.size()
                + global.onWorldFirstLoadTrigger.size() + global.onServerChangeTrigger.size()
                + global.onNewDayTrigger.size() + global.onRightClickPlayerTrigger.size()
                + global.onSoundPlayTrigger.size() + global.onChatTrigger.size()
                + global.function.size() + global.onUnknownError.size());
        if (totalOthers > 0)
            ChatHandler.warn("     &fother: " + Settings.col[0] + totalOthers);
        if (global.chatTrigger.size() > 0)
            ChatHandler.warn("     &fchat: " + Settings.col[0] + global.chatTrigger.size());
        if (global.onChatTrigger.size() > 0)
            ChatHandler.warn("     &fonChat: " + Settings.col[0] + global.onChatTrigger.size());
        if (global.actionTrigger.size() > 0)
            ChatHandler.warn("     &factionBar: " + Settings.col[0] + global.actionTrigger.size());
        if (global.onSoundPlayTrigger.size() > 0)
            ChatHandler.warn("     &fonSoundPlay: " + Settings.col[0] + global.onSoundPlayTrigger.size());
        if (global.onRightClickPlayerTrigger.size() > 0)
            ChatHandler.warn("     &fonRightClickPlayer: " + Settings.col[0] + global.onRightClickPlayerTrigger.size());
        if (global.onWorldLoadTrigger.size() > 0)
            ChatHandler.warn("     &fonWorldLoad: " + Settings.col[0] + global.onWorldLoadTrigger.size());
        if (global.onWorldFirstLoadTrigger.size() > 0)
            ChatHandler.warn("     &fonWorldFirstLoad: " + Settings.col[0] + global.onWorldFirstLoadTrigger.size());
        if (global.onServerChangeTrigger.size() > 0)
            ChatHandler.warn("     &fonServerChange: " + Settings.col[0] + global.onServerChangeTrigger.size());
        if (global.onNewDayTrigger.size() > 0)
            ChatHandler.warn("     &fonNewDay: " + Settings.col[0] + global.onNewDayTrigger.size());
        if (global.tickTrigger.size() > 0)
            ChatHandler.warn("     &fonClientTick: " + Settings.col[0] + global.tickTrigger.size());
        if (global.onUnknownError.size() > 0)
            ChatHandler.warn("     &fonUnknownError: " + Settings.col[0] + global.onUnknownError.size());
        if (global.function.size() > 0)
            ChatHandler.warn("     &ffunction: " + Settings.col[0] + global.function.size());


        ChatHandler.warnBreak(1);
    }

    private static void commandFiles() {
        ChatHandler.warn(ChatHandler.color(Settings.col[0], "Opening ChatTriggers file location..."));
        try {
            Desktop.getDesktop().open(new File("./mods/ChatTriggers"));
        } catch (IOException e) {
            e.printStackTrace();
            ChatHandler.warn(ChatHandler.color("red", "Could not find ChatTriggers files!"));
        }
    }

    private static void commandSubmitBugReport() {
        BugTracker.send();
    }

    private static void commandShowBugReport() {
        BugTracker.showMore();
    }

    private static void commandSimulate(String args[]) {
        StringBuilder TMP_e = new StringBuilder();
        for (int i=1; i<args.length; i++) {
            TMP_e.append(args[i]).append(" ");
        }
        ClientChatReceivedEvent chatEvent = new ClientChatReceivedEvent((byte)0, IChatComponent.Serializer.jsonToComponent("{text:'"+TMP_e.toString().replace("'", "\\'").trim()+"'}"));
        onChat(TMP_e.toString().trim(), ChatHandler.deleteFormatting(TMP_e.toString().trim()), chatEvent);
        if (!chatEvent.isCanceled())
            ChatHandler.warn(TMP_e.toString().trim());
    }

    private static void commandExecute(String args[]) {
        StringBuilder TMP_e = new StringBuilder();
        for (int i=1; i<args.length; i++) {
            TMP_e.append(args[i]).append(" ");
        }
        String event = TMP_e.toString().trim();
        if (event.startsWith("{"))
            event = "do " + event;

        List<String> temp = new ArrayList<>();
        temp.add(event);
        EventsHandler.doEvents(temp, null);
    }

    private static void commandClearChat() {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().deleteChatLine(0);
    }

    private static void commandHelp(String args[]) {
        if (args.length==1) {
            ChatHandler.warnBreak(0);
            ChatHandler.warn(" &f>> clickable(" + Settings.col[0] + "Get started,run_command,/trigger help getstarted,&7Getting started guides and tutorials)");
            ChatHandler.warn("");
            ChatHandler.warn(" &f>> clickable(" + Settings.col[0] + "Available trigger types,run_command,/trigger help triggers,&7Trigger type list)");
            ChatHandler.warn(" &f>> clickable(" + Settings.col[0] + "Available event types,run_command,/trigger help events,&7Event type list)");
            ChatHandler.warn("");
            ChatHandler.warn(" &f>> clickable(" + Settings.col[0] + "Built in strings,run_command,/trigger help strings,&7Built in string list)");
            ChatHandler.warn(" &f>> clickable(" + Settings.col[0] + "String functions,run_command,/trigger help stringfunctions,&7String function list)");
            ChatHandler.warn(" &f>> clickable(" + Settings.col[0] + "Array functions,run_command,/trigger help arrayfunctions,&7Array function list)");
            ChatHandler.warn(" &f>> clickable(" + Settings.col[0] + "List functions,run_command,/trigger help listfunctions,&7List function list)");
            ChatHandler.warn(" &f>> clickable(" + Settings.col[0] + "Json functions,run_command,/trigger help jsonfunctions,&7Json function list)");
            ChatHandler.warn(" &f>> clickable(" + Settings.col[0] + "Display functions,run_command,/trigger help displayfunctions,&7Display function list)");
            ChatHandler.warnBreak(1);
        } else {
            if (args[1].equalsIgnoreCase("GETSTARTED")) {
                ChatHandler.warnBreak(0);
                ChatHandler.warn("&fJoin the Discord to get live help");
                ChatHandler.warn(" &f>> clickable("+Settings.col[0]+"Discord,open_url,http://ct.kerbybit.com/discord/,&7Join the Discord)");
                ChatHandler.warn("");
                ChatHandler.warn("&fCheck out the starting tutorial");
                ChatHandler.warn(" &f>> clickable("+Settings.col[0]+"Tutorials,open_url,http://ct.kerbybit.com/tutorials/,&7Go to the starting tutorial)");
                ChatHandler.warn("");
                ChatHandler.warn("&fRead the terms of service");
                ChatHandler.warn(" &f>> clickable("+Settings.col[0]+"Terms of Service,open_url,https://goo.gl/E8zt5t,&7Open the ToS)");
                ChatHandler.warn("");
                ChatHandler.warn("clickable(&f< Back,run_command,/trigger help,&7Go back to help page)");
                ChatHandler.warnBreak(1);
            } else if (args[1].equalsIgnoreCase("TRIGGERS")) {
                ChatHandler.warnBreak(0);
                ChatHandler.warn("&fTrigger types");
                for (String value : CommandReference.getTriggerTypes()) {
                    ChatHandler.warn(Settings.col[0], " " + value);
                }
                ChatHandler.warn("");
                ChatHandler.warn("clickable(&f< Back,run_command,/trigger help,&7Go back to help page)");
                ChatHandler.warnBreak(1);
            } else if (args[1].equalsIgnoreCase("EVENTS")) {
                ChatHandler.warnBreak(0);
                ChatHandler.warn("&fEvent types");
                for (String value : CommandReference.getEventTypes()) {
                    ChatHandler.warn(Settings.col[0], " " + value);
                }
                ChatHandler.warn("");
                ChatHandler.warn("clickable(&f< Back,run_command,/trigger help,&7Go back to help page)");
                ChatHandler.warnBreak(1);
            } else if (args[1].equalsIgnoreCase("STRINGS")) {
                ChatHandler.warnBreak(0);
                ChatHandler.warn("&fBuilt in strings");
                for (String value : CommandReference.getStrings()) {
                    ChatHandler.warn(Settings.col[0], " " + value);
                }
                ChatHandler.warn("");
                ChatHandler.warn("clickable(&f< Back,run_command,/trigger help,&7Go back to help page)");
                ChatHandler.warnBreak(1);
            } else if (args[1].equalsIgnoreCase("STRINGFUNCTIONS")) {
                ChatHandler.warnBreak(0);
                ChatHandler.warn("&fString functions");
                for (String value : CommandReference.getStringFunctions()) {
                    ChatHandler.warn(Settings.col[0], " " + value.replace("(", "(&7").replace(")", Settings.col[0]+")"));
                }
                ChatHandler.warn("");
                ChatHandler.warn("clickable(&f< Back,run_command,/trigger help,&7Go back to help page)");
                ChatHandler.warnBreak(1);
            } else if (args[1].equalsIgnoreCase("ARRAYFUNCTIONS")) {
                ChatHandler.warnBreak(0);
                ChatHandler.warn("&fArray functions (depreciated)");
                for (String value : CommandReference.getArrayFunctions()) {
                    ChatHandler.warn(Settings.col[0], " " + value.replace("(", "(&7").replace(")", Settings.col[0]+")"));
                }
                ChatHandler.warn("");
                ChatHandler.warn("clickable(&f< Back,run_command,/trigger help,&7Go back to help page)");
                ChatHandler.warnBreak(1);
            } else if (args[1].equalsIgnoreCase("LISTFUNCTIONS")) {
                ChatHandler.warnBreak(0);
                ChatHandler.warn("&fList functions");
                for (String value : CommandReference.getListFunctions()) {
                    ChatHandler.warn(Settings.col[0], " " + value.replace("(", "(&7").replace(")", Settings.col[0]+")"));
                }
                ChatHandler.warn("");
                ChatHandler.warn("clickable(&f< Back,run_command,/trigger help,&7Go back to help page)");
                ChatHandler.warnBreak(1);
            } else if (args[1].equalsIgnoreCase("JSONFUNCTIONS")) {
                ChatHandler.warnBreak(0);
                ChatHandler.warn("&fJson functions");
                for (String value : CommandReference.getJsonFunctions()) {
                    ChatHandler.warn(Settings.col[0], " " + value.replace("(", "(&7").replace(")", Settings.col[0]+")"));
                }
                ChatHandler.warn("");
                ChatHandler.warn("clickable(&f< Back,run_command,/trigger help,&7Go back to help page)");
                ChatHandler.warnBreak(1);
            } else if (args[1].equalsIgnoreCase("DISPLAYFUNCTIONS")) {
                ChatHandler.warnBreak(0);
                ChatHandler.warn("&fDisplay functions");
                for (String value : CommandReference.getDisplayFunctions()) {
                    ChatHandler.warn(Settings.col[0], " " + value.replace("(", "(&7").replace(")", Settings.col[0]+")"));
                }
                ChatHandler.warn("");
                ChatHandler.warn("clickable(&f< Back,run_command,/trigger help,&7Go back to help page)");
                ChatHandler.warnBreak(1);
            }
        }
    }

    private static void commandCopy(String args[], Boolean silent) {
        if (args.length != 1) {
            if (args.length == 3) {
                if (args[1].equals("CopyFromDebugChat")) {
                    try {
                        int num = Integer.parseInt(args[2]);
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(new StringSelection(global.copyText.get(num)), null);
                        doCommand(new String[] {"execute", "notify", ChatHandler.color("green", "Copied debug chat to clipboard")}, silent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ChatHandler.warn(ChatHandler.color("red", "Something went wrong when copying text!"));
                    }
                } else {
                    StringBuilder TMP_e = new StringBuilder();
                    for (int i=1; i<args.length; i++) {TMP_e.append(args[i]).append(" ");}
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(new StringSelection(TMP_e.toString().trim()), null);
                    doCommand(new String[] {"execute", "notify", ChatHandler.color("green", "Copied chat to clipboard")}, silent);
                }
            } else {
                StringBuilder TMP_e = new StringBuilder();
                for (int i=1; i<args.length; i++) {TMP_e.append(args[i]).append(" ");}
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(TMP_e.toString().trim()), null);
                doCommand(new String[] {"execute", "notify", ChatHandler.color("green", "Copied chat to clipboard")}, silent);
            }
        } else {
            ChatHandler.warn(ChatHandler.color("red", "/trigger copy <text>"));
        }
    }

    private static void commandTestImport(String args[]) {
        if (args.length < 2) {
            ChatHandler.warn(ChatHandler.color("red", "/trigger testImport [import name]"));
        } else {
            Boolean canTest = true;
            for (int i=1; i<args.length; i++) {
                String dir = "./mods/ChatTriggers/Imports/"+args[i]+".txt";
                File f = new File(dir);
                if (!f.exists() || !f.isFile()) {
                    canTest=false;
                    ChatHandler.warn(ChatHandler.color("red", args[i]+".txt is not an import! Could not start test mode"));
                }
            }

            if (canTest) {
                if (global.canSave) {
                    global.trigger.clear(); global.USR_string.clear(); global.TMP_string.clear();
                    CommandReference.clearTriggerList();
                    global.waitEvents.clear(); global.asyncMap.clear();
                    for (int i=1; i<args.length; i++) {
                        ChatHandler.warn(ChatHandler.color(Settings.col[0], "You are now in testing mode for import '"+args[i]+"'"));
                    }
                    ChatHandler.warn(ChatHandler.color(Settings.col[0], "To reload all of your triggers and strings, do </trigger load>"));
                    try {
                        for (int i=1; i<args.length; i++) {
                            String dir = "./mods/ChatTriggers/Imports/"+args[i]+".txt";
                            global.trigger.addAll(FileHandler.loadTriggers(dir, true, null));
                        }
                    } catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Unable to load import!")); e.printStackTrace();}
                    global.canSave = false;
                } else {
                    ChatHandler.warn(ChatHandler.color("red", "You must leave test mode before testing another trigger!"));
                    ChatHandler.warn(ChatHandler.color("red", "</trigger load> to leave testing mode"));
                }
            } else {
                ChatHandler.warn(ChatHandler.color("red", "Not an import!"));
            }
        }
    }

    private static void commandReload() {
        ChatHandler.warn(Settings.col[0]+"Reloading ChatTriggers");
        CommandReference.clearAll();
        global.tick = 0;
        FileHandler.firstFileLoad();
        global.worldLoaded = true;
        global.worldFirstLoad = true;
    }

    private static void commandTest() {
        ChatHandler.warn(ChatHandler.color("&7", "This command does nothing :D"));
    }

    private static void commandImport(String args[]) {
        if (args.length>=2) {
            for (int i=1; i<args.length; i++) {
                String value = args[i];
                Boolean has = false;
                for (String neededImport : global.neededImports) {
                    if (neededImport.equals(value)) {
                        has = true;
                        break;
                    }
                }
                if (!has) global.neededImports.add(value);
            }
        } else {ChatHandler.warn(ChatHandler.color("red", "/trigger import <import name>"));}
    }

    private static void commandImports() {
        ChatHandler.warnBreak(0);


        File dir = new File("./mods/ChatTriggers/Imports/");
        if (!dir.exists()) {
            if (!dir.mkdir()) {ChatHandler.warn(ChatHandler.color("red", "Unable to create file!"));}
        }
        File[] activeFiles = dir.listFiles();
        if (activeFiles != null) {
            ChatHandler.warn("Active Imports");
            for (File file : activeFiles) {
                if (file.isFile()) {
                    if (file.getName().endsWith(".txt")) {
                        ChatHandler.warn("clickable( &l\u02C5,run_command,/t disableimport -showlist " + file.getName().replace(".txt","") + ",&7Disable import " + file.getName().replace(".txt","") + ") " + ChatHandler.color(Settings.col[0], file.getName().replace(".txt","")));
                    }
                }
            }
        }

        dir = new File("./mods/ChatTriggers/Imports/DisabledImports/");
        if (!dir.exists()) {
            if (!dir.mkdir()) {ChatHandler.warn(ChatHandler.color("red", "Unable to create file!"));}
        }
        File[] disabledFiles = dir.listFiles();
        if (disabledFiles !=null) {
            ChatHandler.warn("Disabled Imports");
            for (File file : disabledFiles) {
                if (file.getName().endsWith(".txt")) {
                    ChatHandler.warn("clickable( &l\u02C4,run_command,/t enableimport -showlist " + file.getName().replace(".txt","") + ",&7Enable import " + file.getName().replace(".txt","") + ") " + ChatHandler.color(Settings.col[0], file.getName().replace(".txt","")));
                }
            }
        }

        ChatHandler.warnBreak(1);
    }

    private static void commandDisableImport(String args[], Boolean silent) {
        if (args.length>=2) {
            Boolean showlist = false;
            for (int i=1; i<args.length; i++) {
                try {
                    if (args[i].equalsIgnoreCase("-showlist")) {
                        showlist = true;
                    } else {
                        File dir = new File("./mods/ChatTriggers/Imports/DisabledImports/");
                        if (!dir.exists()) {if (!dir.mkdir()) {ChatHandler.warn(ChatHandler.color("red","Something went wrong while creating the file!"));}}
                        File file = new File("./mods/ChatTriggers/Imports/" + args[i]+".txt");
                        if (!file.exists()) {throw new IOException();}
                        if (!file.renameTo(new File("./mods/ChatTriggers/Imports/DisabledImports/" + args[i] + ".txt"))) {ChatHandler.warn(ChatHandler.color("red", "Something went wrong while moving the file!"));}
                        if (!silent) {ChatHandler.warn(ChatHandler.color(Settings.col[0], "Disabled " + args[i] + ".txt"));}
                        try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                        global.worldLoaded = true;
                    }
                } catch (IOException e) {
                    if (!silent) {ChatHandler.warn(ChatHandler.color("red", args[i] + " is not an active import!"));}
                }
            }
            if (showlist) {
                commandImports();
            }
        } else {ChatHandler.warn(ChatHandler.color("red", "/trigger disableImport <import name>"));}
    }

    private static void commandEnableImport(String args[], Boolean silent) {
        if (args.length>=2) {
            Boolean showlist = false;
            for (int i=1; i<args.length; i++) {
                try {
                    if (args[i].equalsIgnoreCase("-showlist")) {
                        showlist = true;
                    } else {
                        File dir = new File("./mods/ChatTriggers/Imports/DisabledImports/");
                        if (!dir.exists()) {if (!dir.mkdir()) {ChatHandler.warn(ChatHandler.color("red","Something went wrong while creating the file!"));}}
                        File file = new File("./mods/ChatTriggers/Imports/DisabledImports/" + args[i]+".txt");
                        if (!file.exists()) {throw new IOException();}
                        if (!file.renameTo(new File("./mods/ChatTriggers/Imports/" + args[i] + ".txt"))) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                        if (!silent) {ChatHandler.warn(ChatHandler.color(Settings.col[0], "Enabled " + args[i] + ".txt"));}
                        try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                        global.worldLoaded = true;
                    }

                } catch (IOException e) {
                    if (!silent) {ChatHandler.warn(ChatHandler.color("red", args[i] + " is not an inactive import!"));}
                }
            }
            if (showlist) {
                commandImports();
            }
        } else {ChatHandler.warn(ChatHandler.color("red", "/trigger disableImport <import name>"));}
    }

    static void commandRun(String args[]) {
        if (args.length < 2) {
            ChatHandler.warn(ChatHandler.color("red", "/tr <trigger>"));
            ArrayList<List<String>> listCommands = new ArrayList<>();
            ArrayList<List<String>> showCommands = new ArrayList<>();
            for (int i=0; i<global.trigger.size(); i++) {
                if (global.trigger.get(i).get(1).contains("<imported>") && global.trigger.get(i).get(1).contains("<list=")) {
                    if (global.trigger.get(i).get(0).trim().toUpperCase().startsWith("OTHER ")) {
                        String toShow = global.trigger.get(i).get(0).trim();
                        toShow = toShow.substring(toShow.toUpperCase().indexOf("OTHER ")+6);
                        String inList = global.trigger.get(i).get(1);
                        inList = inList.substring(inList.indexOf("<list=")+6, inList.indexOf(">", inList.indexOf("<list=")));
                        ArrayList<String> temporary = new ArrayList<>();
                        temporary.add(inList);
                        temporary.add(toShow);
                        listCommands.add(temporary);
                    }
                }
            }
            if (listCommands.size() != 0) {
                ChatHandler.warn(ChatHandler.color("white", "Available imported commands"));
                for (List<String> listCommand : listCommands) {
                    Boolean isNewList = false;
                    int isInList = -1;
                    if (showCommands.size()>0) {
                        for (int j=0; j<showCommands.size(); j++) {
                            if (showCommands.get(j).get(0).equals(listCommand.get(0))) {isInList=j;}
                        }
                        if (isInList == -1) {isNewList = true;}
                    } else {isNewList = true;}
                    if (isNewList) {
                        ArrayList<String> temporary = new ArrayList<>();
                        temporary.add(listCommand.get(0));
                        temporary.add(listCommand.get(1));
                        showCommands.add(temporary);
                    } else {showCommands.get(isInList).add(listCommand.get(1));}
                }
                for (List<String> showCommand : showCommands) {
                    ChatHandler.warn(ChatHandler.color("white", showCommand.get(0)));
                    for (int j = 1; j < showCommand.size(); j++) {
                        ChatHandler.warn(ChatHandler.color(Settings.col[0], "  /tr " + showCommand.get(j)));
                    }
                }
            }
        } else {
            StringBuilder TMP_e = new StringBuilder();
            for (int i=1; i<args.length; i++) {
                if (i==args.length-1) {TMP_e.append(args[i]);}
                else {TMP_e.append(args[i]).append(" ");}
            }
            try {
                int num = Integer.parseInt(args[1]);
                if (num >= 0 && num < global.trigger.size()) {
                    List<String> TMP_events = new ArrayList<>();
                    for (int i=2; i<global.trigger.get(num).size(); i++) {
                        TMP_events.add(global.trigger.get(num).get(i));
                    }
                    EventsHandler.doEvents(TMP_events, null);
                }
            } catch (NumberFormatException e1) {
                Boolean ranTrigger = false;
                for (int k=0; k<global.trigger.size(); k++) {

                    String TMP_trig = global.trigger.get(k).get(1);

                    boolean getCase = true;
                    if (TMP_trig.contains("<case=false>")) {
                        getCase = false;
                    }

                    TMP_trig = TagHandler.removeTags(TMP_trig);

                    if (getCase) {
                        if (TMP_trig.equals(TMP_e.toString())) {
                            List<String> TMP_events = new ArrayList<>();
                            for (int i=2; i<global.trigger.get(k).size(); i++) {
                                TMP_events.add(global.trigger.get(k).get(i));
                            }
                            EventsHandler.doEvents(TMP_events, null);
                        } else {
                            String TMP_trigtype = global.trigger.get(k).get(0);
                            if (TMP_trigtype.toUpperCase().startsWith("OTHER")) {
                                if (TMP_trig.contains("(") && TMP_trig.endsWith(")")) {
                                    String TMP_trigtest = TMP_trig.substring(0, TMP_trig.indexOf("("));
                                    if (TMP_e.toString().startsWith(TMP_trigtest) && TMP_e.toString().endsWith(")")) {
                                        String TMP_argsIn = TMP_e.substring(TMP_e.indexOf("(") + 1, TMP_e.length() - 1);
                                        String TMP_argsOut = TMP_trig.substring(TMP_trig.indexOf("(") + 1, TMP_trig.length() - 1);
                                        String[] argsIn = TMP_argsIn.split(",");
                                        String[] argsOut = TMP_argsOut.split(",");
                                        if (argsIn.length == argsOut.length) {
                                            List<String> TMP_events = new ArrayList<>();
                                            for (int j = 2; j < global.trigger.get(k).size(); j++) {
                                                TMP_events.add(global.trigger.get(k).get(j));
                                            }
                                            EventsHandler.doEvents(TMP_events, null, argsOut, argsIn);
                                            ranTrigger = true;
                                        }
                                    } else if (TMP_e.toString().startsWith(TMP_trigtest)) {
                                        String[] argsIn = TMP_e.toString().replace(TMP_trigtest,"").trim().split(" ");

                                        String TMP_argsOut = TMP_trig.substring(TMP_trig.indexOf("(") + 1, TMP_trig.length() - 1);
                                        String[] argsOut = TMP_argsOut.split(",");

                                        if (argsIn.length == argsOut.length) {
                                            List<String> TMP_events = new ArrayList<>();
                                            for (int j = 2; j < global.trigger.get(k).size(); j++) {
                                                TMP_events.add(global.trigger.get(k).get(j));
                                            }
                                            EventsHandler.doEvents(TMP_events, null, argsOut, argsIn);
                                            ranTrigger = true;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if (TMP_trig.equalsIgnoreCase(TMP_e.toString())) {
                            List<String> TMP_events = new ArrayList<>();
                            for (int i=2; i<global.trigger.get(k).size(); i++) {
                                TMP_events.add(global.trigger.get(k).get(i));
                            }
                            EventsHandler.doEvents(TMP_events, null);
                        } else {
                            String TMP_trigtype = global.trigger.get(k).get(0);
                            if (TMP_trigtype.toUpperCase().startsWith("OTHER")) {
                                if (TMP_trig.contains("(") && TMP_trig.endsWith(")")) {
                                    String TMP_trigtest = TMP_trig.substring(0, TMP_trig.indexOf("("));
                                    if (TMP_e.toString().toUpperCase().startsWith(TMP_trigtest.toUpperCase()) && TMP_e.toString().endsWith(")")) {
                                        String TMP_argsIn = TMP_e.substring(TMP_e.indexOf("(") + 1, TMP_e.length() - 1);
                                        String TMP_argsOut = TMP_trig.substring(TMP_trig.indexOf("(") + 1, TMP_trig.length() - 1);
                                        String[] argsIn = TMP_argsIn.split(",");
                                        String[] argsOut = TMP_argsOut.split(",");
                                        if (argsIn.length == argsOut.length) {
                                            List<String> TMP_events = new ArrayList<>();
                                            for (int j = 2; j < global.trigger.get(k).size(); j++) {
                                                TMP_events.add(global.trigger.get(k).get(j));
                                            }
                                            EventsHandler.doEvents(TMP_events, null, argsOut, argsIn);
                                            ranTrigger = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (!ranTrigger) {
                    for (int k=0; k<global.trigger.size(); k++) {
                        String TMP_trig = global.trigger.get(k).get(1);
                        String TMP_trigtype = global.trigger.get(k).get(0);

                        TMP_trig = TagHandler.removeTags(TMP_trig);

                        if (TMP_trigtype.toUpperCase().startsWith("OTHER")) {
                            if (TMP_trig.contains("(") && TMP_trig.endsWith(")")) {
                                String TMP_trigtest = TMP_trig.substring(0, TMP_trig.indexOf("("));
                                if (TMP_e.toString().startsWith(TMP_trigtest) && TMP_e.toString().endsWith(")")) {
                                    return;
                                } else if (TMP_e.toString().startsWith(TMP_trigtest)) {
                                    //String[] argsIn = TMP_e.toString().replace(TMP_trigtest,"").trim().split(" ");

                                    String TMP_argsOut = TMP_trig.substring(TMP_trig.indexOf("(") + 1, TMP_trig.length() - 1);
                                    String[] argsOut = TMP_argsOut.split(",");

                                    String[] argsIn = new String[argsOut.length];
                                    String[] get = TMP_e.toString().replace(TMP_trigtest,"").trim().split(" ");
                                    for (int i = 0; i<get.length;i++ ) {
                                        try {
                                            argsIn[i] = get[i];
                                        } catch (IndexOutOfBoundsException e) {
                                            argsIn[argsIn.length-1] = argsIn[argsIn.length-1] + " " + get[i];
                                        }
                                    }

                                    if (argsIn.length == argsOut.length) {
                                        List<String> TMP_events = new ArrayList<>();
                                        for (int j = 2; j < global.trigger.get(k).size(); j++) {
                                            TMP_events.add(global.trigger.get(k).get(j));
                                        }
                                        EventsHandler.doEvents(TMP_events, null, argsOut, argsIn);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void commandCreate(String args[], Boolean silent) {
        if (args.length < 3) {
            StringBuilder hover_value = new StringBuilder();
            for (String value : CommandReference.getTriggerTypes()) {
                hover_value.append(" ").append(value);
            }
            hover_value = new StringBuilder(hover_value.toString().trim().replace(" ", "\n&7"));
            ChatHandler.warn("&c/trigger &ccreate &c<hover(&ctype,&7"+hover_value.toString()+")&c> &c<trigger>");
        } else {
            String TMP_type = args[1];
            StringBuilder TMP_trig = new StringBuilder();
            for (int i=2; i<args.length; i++) {
                if (i==args.length-1) {TMP_trig.append(args[i]);}
                else {TMP_trig.append(args[i]).append(" ");}
            }
            List<String> TMP_l = new ArrayList<>();
            TMP_l.add(TMP_type);
            TMP_l.add(TMP_trig.toString());
            if (CommandReference.isTriggerType(TMP_type)) {
                global.trigger.add(TMP_l);
                try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                int TMP_num = -1;
                for (int i=0; i<global.trigger.size(); i++) {
                    if (global.trigger.get(i).equals(TMP_l)) {
                        TMP_num = i;
                    }
                }
                if (!silent) {
                    TMP_trig = new StringBuilder(TMP_trig.toString().replace("(","LeftParF6cyUQp9LeftPar").replace(")","RightParF6cyUQp9RightPar").replace("&","AmpF6cyUQp9Amp"));
                    if (TMP_num == -1) {
                        ChatHandler.warn("&7Created trigger "+Settings.col[0]+TMP_trig.toString().replace(" "," "+Settings.col[0])+" &7with trigger type "+Settings.col[0]+TMP_type);
                    } else {
                        ChatHandler.warn("&7Created trigger "+Settings.col[0]+TMP_trig.toString().replace(" "," "+Settings.col[0])+" &7with trigger type "+Settings.col[0]+TMP_type+" clickable(&7("+TMP_num+"),suggest_command,/trigger add "+TMP_num+" ,Add an event)");
                    }
                }

            } else {
                ChatHandler.warn(ChatHandler.color("red", "Not a valid trigger type"));
            }
        }
    }

    private static void commandDelete(String args[], Boolean silent) {
        if (args.length < 2) {
            ChatHandler.warn(ChatHandler.color("red", "/trigger delete <trigger number>"));
        } else {
            int num;
            try {num = Integer.parseInt(args[1]);}
            catch(NumberFormatException e) {num = -1;}
            if (num>-1 && num<global.trigger.size()) {
                String TMP_rem = global.trigger.get(num).get(1);
                if (!TMP_rem.contains("<imported>")) {
                    if (!silent) {
                        ChatHandler.warn(ChatHandler.color("gray", "Deleted trigger") + " " + ChatHandler.color(Settings.col[0], ChatHandler.ignoreFormatting(TMP_rem)) + " " + ChatHandler.color("gray", "and all of its events"));
                    }
                    global.trigger.remove(num);
                    try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                } else {
                    ChatHandler.warn(ChatHandler.color("red", "You cannot edit imported triggers!"));
                    ChatHandler.warn(ChatHandler.color("red", "You must edit them from the imported file!"));
                }
            } else {
                ChatHandler.warn(ChatHandler.color("red", "/trigger delete <trigger number>"));
            }
        }
    }

    private static void commandAdd(String args[], Boolean silent) {
        if (args.length < 3) {
            StringBuilder hover_value = new StringBuilder();
            for (String value : CommandReference.getEventTypes()) {
                hover_value.append(" ").append(value);
            }
            hover_value = new StringBuilder(hover_value.toString().trim().replace(" ", "\n&7"));
            ChatHandler.warn("&c/trigger &cadd &c<trigger number> &c<hover(&cevent,&7"+hover_value.toString()+")&c> &c<event &cargument(s)>");
        } else {
            int num;
            try {num = Integer.parseInt(args[1]);}
            catch(NumberFormatException e) {num = -1;}
            StringBuilder TMP_e = new StringBuilder();
            for (int i=2; i<args.length; i++) {
                if (i==args.length-1) {TMP_e.append(args[i]);}
                else {TMP_e.append(args[i]).append(" ");}
            }
            if (num>-1 && num<global.trigger.size()) {
                if (!global.trigger.get(num).get(1).contains("<imported>")) {
                    String TMP_etype = TMP_e.toString();
                    if (TMP_e.toString().contains(" ")) {TMP_etype = TMP_e.substring(0,TMP_e.indexOf(" "));}

                    if (CommandReference.isEventType(TMP_etype)) {
                        global.trigger.get(num).add(TMP_e.toString());
                        if (!silent) {
                            TMP_e = new StringBuilder(TMP_e.toString().replace("(", "LeftParF6cyUQp9LeftPar")
                                    .replace(")", "RightParF6cyUQp9RightPar")
                                    .replace(",", "CommaReplacementF6cyUQp9CommaReplacement"));
                            ChatHandler.warn(ChatHandler.color("gray", "Added event") + " " + ChatHandler.color(Settings.col[0], ChatHandler.ignoreFormatting(TMP_e.toString())) + " " + ChatHandler.color("gray", "to trigger") + " " + ChatHandler.color(Settings.col[0], ChatHandler.ignoreFormatting(global.trigger.get(num).get(1))));
                        }
                        try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                    } else {
                        ChatHandler.warn(ChatHandler.color("red", "Not a valid event type!"));
                    }
                } else {
                    ChatHandler.warn(ChatHandler.color("red", "You cannot edit imported triggers!"));
                    ChatHandler.warn(ChatHandler.color("red", "You must edit them from the imported file!"));
                }
            } else {
                ChatHandler.warn(ChatHandler.color("red", "/trigger add <trigger number> <event> <event argument(s)>"));
            }
        }
    }

    private static void commandRemove(String args[], Boolean silent) {
        if (args.length < 3) {
            ChatHandler.warn(ChatHandler.color("red", "/trigger remove [trigger number] [event number]"));
        } else {
            int num;
            try {num = Integer.parseInt(args[1]);}
            catch(NumberFormatException e) {num = -1;}
            if (num>-1 && num<global.trigger.size()) {
                if (!global.trigger.get(num).get(1).contains("<imported>")) {
                    int num2;
                    try {num2 = Integer.parseInt(args[2]);}
                    catch(NumberFormatException e) {num2 = 1;}
                    if (num2>1 && num2<global.trigger.get(num).size()) {
                        String TMP_rem = global.trigger.get(num).remove(num2);
                        if (!silent) {
                            ChatHandler.warnUnformatted(ChatHandler.color("gray", "Removed event") + " " + ChatHandler.color(Settings.col[0], TMP_rem) + " " + ChatHandler.color("gray", "from trigger") + " " + ChatHandler.color(Settings.col[0], global.trigger.get(num).get(1)));
                        }
                        try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                    } else {
                        ChatHandler.warn(ChatHandler.color("red", "/trigger remove [trigger number] [event number]"));
                    }
                } else {
                    ChatHandler.warnUnformatted(ChatHandler.color("red", "You cannot edit imported triggers!"));
                    ChatHandler.warnUnformatted(ChatHandler.color("red", "You must edit them from the imported file!"));
                }
            } else {
                ChatHandler.warn(ChatHandler.color("red", "/trigger remove [trigger number] [event number]"));
            }
        }
    }

    private static void commandString(String args[], Boolean silent) {
        if (args.length < 2) {
            ChatHandler.warn(ChatHandler.color("red", "/trigger string [create/set/list] <...>"));
            ChatHandler.warn(ChatHandler.color("red", "/trigger string <string name>"));
        } else {
            if (args[1].equalsIgnoreCase("CREATE")) {
                if (args.length < 3) {
                    ChatHandler.warn(ChatHandler.color("red", "/trigger string create <string name>"));
                } else {
                    if (global.canSave) {
                        String stringName = args[2];

                        StringBuilder stringValueBuilder = new StringBuilder();
                        if (args.length > 3) {
                            for (int i=3; i<args.length; i++) {
                                stringValueBuilder.append(args[i]).append(" ");
                            }
                        }
                        String stringValue = stringValueBuilder.toString().trim();

                        if (global.USR_string.containsKey(stringName)) {
                            stringName = stringName.replace("(","LeftParF6cyUQp9LeftPar").replace(")","RightParF6cyUQp9RightPar").replace("&","AmpF6cyUQp9Amp").replace("\\", "BackslashF6cyUQp9Backslash");
                            stringValue = stringValue.replace("(","LeftParF6cyUQp9LeftPar").replace(")","RightParF6cyUQp9RightPar").replace("&","AmpF6cyUQp9Amp").replace("\\", "BackslashF6cyUQp9Backslash");
                            ChatHandler.warn(ChatHandler.color("red", "hover(" + Settings.col[0] + stringName + ",&7Value: &f" + stringValue.replace(" ", " &f") + ") already exists!"));
                        } else {
                            global.USR_string.put(stringName, stringValue);
                            if (!silent) {
                                stringName = stringName.replace("(","LeftParF6cyUQp9LeftPar").replace(")","RightParF6cyUQp9RightPar").replace("&","AmpF6cyUQp9Amp").replace("\\", "BackslashF6cyUQp9Backslash");
                                if (stringValue.equals("")) {
                                    ChatHandler.warn("&7Created string clickable("+Settings.col[0]+stringName+",suggest_command,/trigger string set "+stringName+" ,Set "+stringName+")");
                                } else {
                                    ChatHandler.warn("&7Created string clickable("+Settings.col[0]+stringName+",suggest_command,/trigger string set "+stringName+" ,Set "+stringName+") &7with value " + Settings.col[0] + stringValue);
                                }
                            }
                            try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                        }
                    } else {
                        ChatHandler.warn(ChatHandler.color("red", "cannot create string while in test mode"));
                        ChatHandler.warn(ChatHandler.color("red", "</trigger load> to leave testing mode"));
                    }
                }
            } else if (args[1].equalsIgnoreCase("DELETE")) {
                if (args.length < 3) {
                    ChatHandler.warn(ChatHandler.color("red", "/trigger string delete <string name>"));
                } else {
                    if (global.canSave) {
                        if (global.USR_string.containsKey(args[2])) {
                            String TMP_rem = global.USR_string.remove(args[2]);
                            if (!silent) {
                                TMP_rem = TMP_rem.replace("(","LeftParF6cyUQp9LeftPar").replace(")","RightParF6cyUQp9RightPar").replace("&","AmpF6cyUQp9Amp");
                                ChatHandler.warnUnformatted(ChatHandler.color("gray", "Deleted string") + " " + ChatHandler.color(Settings.col[0], TMP_rem));
                            }
                            try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                        }
                    } else {
                        ChatHandler.warn(ChatHandler.color("red", "cannot delete string while in test mode"));
                        ChatHandler.warn(ChatHandler.color("red", "</trigger load> to leave testing mode"));
                    }
                }
            } else if (args[1].equalsIgnoreCase("SET")) {
                if (args.length < 4) {
                    ChatHandler.warn(ChatHandler.color("red", "/trigger string set <string name> <string>"));
                } else {
                    StringBuilder TMP_s = new StringBuilder();
                    for (int i=3; i<args.length; i++) {
                        if (i==args.length-1) {TMP_s.append(args[i]);}
                        else {TMP_s.append(args[i]).append(" ");}
                    }
                    if (global.USR_string.containsKey(args[2])) {
                        if (!silent) {ChatHandler.warnUnformatted(ChatHandler.color("gray", "Set value") + " " + ChatHandler.color(Settings.col[0], TMP_s.toString()) + " " + ChatHandler.color("gray", "in string") + " " + ChatHandler.color(Settings.col[0], args[2]));}
                        if (TMP_s.toString().equals("{null}")) {TMP_s = new StringBuilder();}
                        global.USR_string.put(args[2], TMP_s.toString());
                        try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                    } else {ChatHandler.warn(ChatHandler.color("red", "/trigger string set [string number] [string]"));}
                }
            } else if (args[1].equalsIgnoreCase("LIST")) {
                if (args.length==2) {
                    ChatHandler.warnBreak(0);
                    if (global.USR_string.size() == 0) {
                        ChatHandler.warnUnformatted(ChatHandler.color("red", "No strings created"));
                        ChatHandler.warnUnformatted(ChatHandler.color("red", "Do </trigger string> to get started"));
                    } else {
                        for (Map.Entry<String, String> entry : global.USR_string.entrySet()) {
                            String TMP_sn = entry.getKey();
                            TMP_sn = TMP_sn.replace("(","LeftParF6cyUQp9LeftPar").replace(")","RightParF6cyUQp9RightPar").replace("&","AmpF6cyUQp9Amp");
                            ChatHandler.warn("clickable(&7>,suggest_command,/trigger string set "+TMP_sn+" ,Set string) "+Settings.col[0]+TMP_sn+" clickable(&c-,suggest_command,/trigger string delete "+TMP_sn+" [enter to confirm],Delete string)");
                            ChatHandler.warnUnformatted(ChatHandler.color("gray", "  " + entry.getValue()));
                        }
                    }
                    ChatHandler.warnBreak(1);
                }
            } else {
                if (args.length==2) {
                    if (global.USR_string.containsKey(args[1])) {
                        ChatHandler.warnUnformatted(ChatHandler.color("gray","value:") + " " + ChatHandler.color(Settings.col[0], global.USR_string.get(args[1])));
                    } else {
                        ChatHandler.warn(ChatHandler.color("red", "Not a string!"));
                    }
                } else {
                    ChatHandler.warn(ChatHandler.color("red", "/trigger string [create/set/list] <...>"));
                    ChatHandler.warn(ChatHandler.color("red", "/trigger string <string name>"));
                }
            }
        }
    }

    private static void commandList(String args[]) {
        if (args.length==1) {
            ChatHandler.warnBreak(0);
            if (global.trigger.size() == 0) {
                ChatHandler.warn(ChatHandler.color("red", "No triggers created"));
                ChatHandler.warn(ChatHandler.color("red", "Do </trigger> to get started"));
            } else {
                List<String> TMP_lists = new ArrayList<>();
                for (int i=0; i<global.trigger.size(); i++) {
                    String TMP_type = global.trigger.get(i).get(0);
                    String TMP_trig = global.trigger.get(i).get(1);
                    String TMP_list = "";
                    String TMP_w    = "";
                    String TMP_server = "";
                    String TMP_case = "";
                    Boolean TMP_imported = false;
                    Boolean TMP_formatted = false;

                    if (TMP_trig.contains("<s>")) {TMP_w = "start"; TMP_trig = TMP_trig.replace("<s>", "");}
                    if (TMP_trig.contains("<c>")) {TMP_w = "contain"; TMP_trig = TMP_trig.replace("<c>", "");}
                    if (TMP_trig.contains("<e>")) {TMP_w = "end"; TMP_trig = TMP_trig.replace("<e>", "");}
                    if (TMP_trig.contains("<start>")) {TMP_w = "start"; TMP_trig = TMP_trig.replace("<start>", "");}
                    if (TMP_trig.contains("<contain>")) {TMP_w = "contain"; TMP_trig = TMP_trig.replace("<contain>", "");}
                    if (TMP_trig.contains("<end>")) {TMP_w = "end"; TMP_trig = TMP_trig.replace("<end>", "");}

                    if (TMP_trig.contains("<list=") && TMP_trig.contains(">")) {TMP_list = TMP_trig.substring(TMP_trig.indexOf("<list=")+6, TMP_trig.indexOf(">", TMP_trig.indexOf("<list="))); TMP_trig = TMP_trig.replace("<list="+TMP_list+">","");}
                    TMP_lists.add(TMP_list);
                    if (TMP_trig.contains("<server=") && TMP_trig.contains(">")) {TMP_server = TMP_trig.substring(TMP_trig.indexOf("<server=")+8, TMP_trig.indexOf(">", TMP_trig.indexOf("<server="))); TMP_trig = TMP_trig.replace("<server="+TMP_server+">","");}
                    if (TMP_trig.contains("<case=") && TMP_trig.contains(">")) {TMP_case = TMP_trig.substring(TMP_trig.indexOf("<case=")+6, TMP_trig.indexOf(">", TMP_trig.indexOf("<case="))); TMP_trig = TMP_trig.replace("<case="+TMP_case+">","");}
                    if (!TMP_case.equals("false")) {TMP_case = "";}
                    if (TMP_trig.contains("<imported>")) {TMP_imported = true; TMP_trig = TMP_trig.replace("<imported>", "");}
                    if (TMP_trig.contains("<formatted>")) {TMP_formatted = true; TMP_trig = TMP_trig.replace("<formatted>", "");}

                    String TMP_tags = "";
                    if (TMP_imported) {TMP_tags+="Imported";}
                    if (!TMP_w.equals("")) {if (!TMP_tags.equals("")) {TMP_tags += "\n";} TMP_tags += "Modifier: " + TMP_w;}
                    if (!TMP_list.equals("")) {if (!TMP_tags.equals("")) {TMP_tags += "\n";} TMP_tags += "List: " + TMP_list;}
                    if (!TMP_server.equals("")) {if (!TMP_tags.equals("")) {TMP_tags += "\n";} TMP_tags += "Server: " + TMP_server;}
                    if (!TMP_case.equals("")) {if (!TMP_tags.equals("")) {TMP_tags += "\n";} TMP_tags += "Case Insensitive";}
                    if (TMP_formatted) {if (!TMP_tags.equals("")) {TMP_tags += "\n";} TMP_tags += "Formatted";}

                    if (TMP_list.equals("")) {
                        TMP_type = TMP_type.replace("(","LeftParF6cyUQp9LeftPar").replace(")","RightParF6cyUQp9RightPar").replace("&","AmpF6cyUQp9Amp");
                        TMP_trig = TMP_trig.replace("(","LeftParF6cyUQp9LeftPar").replace(")","RightParF6cyUQp9RightPar").replace("&","AmpF6cyUQp9Amp");
                        String pre_out;
                        if (TMP_imported) {pre_out = "hover(&7>,You cannot edit\nimported triggers)";}
                        else {pre_out = "clickable(&7>,suggest_command,/trigger add "+i+" ,Add an event)";}
                        String post_out = "";
                        if (!TMP_tags.equals("")) {post_out = "hover(&8tags ,"+TMP_tags+")";}
                        if (!TMP_imported) {post_out += "clickable(&c-,suggest_command,/trigger delete "+i+" [enter to confirm],Remove trigger)";}
                        ChatHandler.warn(pre_out+" &8"+TMP_type+" "+Settings.col[0]+TMP_trig.replace(" "," "+Settings.col[0])+" "+post_out);

                        int tabbed_logic = 0;

                        for (int j=2; j<global.trigger.get(i).size(); j++) {

                            StringBuilder TMP_extraspaces = new StringBuilder("  ");

                            String TMP_e = global.trigger.get(i).get(j);
                            String TMP_c;
                            if (!TMP_e.contains(" ")) {TMP_c = TMP_e; TMP_e = "";}
                            else {TMP_c = TMP_e.substring(0, TMP_e.indexOf(" ")); TMP_e = TMP_e.substring(TMP_e.indexOf(" ") + 1, TMP_e.length());}

                            if (TMP_c.equalsIgnoreCase("END")
                                    || TMP_c.toUpperCase().startsWith("ELSE")) {
                                tabbed_logic--;
                            }

                            for (int k=0; k<tabbed_logic; k++) {TMP_extraspaces.append("  ");}
                            TMP_c = TMP_c.replace("(","LeftParF6cyUQp9LeftPar").replace(")","RightParF6cyUQp9RightPar").replace("&","AmpF6cyUQp9Amp");
                            TMP_e = TMP_e.replace("(","LeftParF6cyUQp9LeftPar").replace(")","RightParF6cyUQp9RightPar").replace("&","AmpF6cyUQp9Amp");
                            String pre_out_event;
                            if (TMP_imported) {pre_out_event = " ";}
                            else {pre_out_event = "clickable(&c-,suggest_command,/trigger remove "+i+" "+j+" [enter to confirm],Remove event)";}
                            ChatHandler.warn(TMP_extraspaces+pre_out_event+" &8"+TMP_c+" &7"+TMP_e.replace(" "," &7").replace("\\","BackslashF6cyUQp9Backslash"));


                            if (TMP_c.toUpperCase().startsWith("IF")
                                    || TMP_c.toUpperCase().startsWith("FOR")
                                    || TMP_c.toUpperCase().startsWith("CHOOSE")
                                    || TMP_c.toUpperCase().startsWith("WAIT")
                                    || TMP_c.toUpperCase().startsWith("ELSE")
                                    || TMP_c.toUpperCase().startsWith("ASYNC")) {
                                tabbed_logic++;
                            }
                        }
                    }
                }
                Set<String> uniqueTMP_lists = new HashSet<>(TMP_lists);
                for (String value : uniqueTMP_lists) {
                    if (!value.equals("")) {
                        ChatHandler.warn("clickable(&7List>,run_command,/trigger list "+value+") "+Settings.col[0]+value);
                    }
                }
            }
            ChatHandler.warnBreak(1);
        } else {
            StringBuilder TMP_check = new StringBuilder();
            for (int i=1; i<args.length; i++) {
                if (i==args.length-1) {TMP_check.append(args[i]);}
                else {TMP_check.append(args[i]).append(" ");}
            }

            ChatHandler.warnBreak(0);
            if (global.trigger.size() == 0) {
                ChatHandler.warn(ChatHandler.color("red", "No triggers created"));
                ChatHandler.warn(ChatHandler.color("red", "Do </trigger> to get started"));
            } else {
                int TMP_test = 0;
                for (int i=0; i<global.trigger.size(); i++) {
                    String TMP_type = global.trigger.get(i).get(0);
                    String TMP_trig = global.trigger.get(i).get(1);
                    String TMP_list = "";
                    String TMP_w    = "";
                    String TMP_server = "";
                    String TMP_case = "";
                    Boolean TMP_imported = false;
                    Boolean TMP_formatted = false;

                    if (TMP_trig.contains("<s>")) {TMP_w = "start"; TMP_trig = TMP_trig.replace("<s>", "");}
                    if (TMP_trig.contains("<c>")) {TMP_w = "contain"; TMP_trig = TMP_trig.replace("<c>", "");}
                    if (TMP_trig.contains("<e>")) {TMP_w = "end"; TMP_trig = TMP_trig.replace("<e>", "");}
                    if (TMP_trig.contains("<start>")) {TMP_w = "start"; TMP_trig = TMP_trig.replace("<start>", "");}
                    if (TMP_trig.contains("<contain>")) {TMP_w = "contain"; TMP_trig = TMP_trig.replace("<contain>", "");}
                    if (TMP_trig.contains("<end>")) {TMP_w = "end"; TMP_trig = TMP_trig.replace("<end>", "");}

                    if (TMP_trig.contains("<list=") && TMP_trig.contains(">")) {TMP_list = TMP_trig.substring(TMP_trig.indexOf("<list=")+6, TMP_trig.indexOf(">", TMP_trig.indexOf("<list="))); TMP_trig = TMP_trig.replace("<list="+TMP_list+">","");}
                    if (TMP_trig.contains("<server=") && TMP_trig.contains(">")) {TMP_server = TMP_trig.substring(TMP_trig.indexOf("<server=")+8, TMP_trig.indexOf(">", TMP_trig.indexOf("<server="))); TMP_trig = TMP_trig.replace("<server="+TMP_server+">","");}
                    if (TMP_trig.contains("<case=") && TMP_trig.contains(">")) {TMP_case = TMP_trig.substring(TMP_trig.indexOf("<case=")+6, TMP_trig.indexOf(">", TMP_trig.indexOf("<case="))); TMP_trig = TMP_trig.replace("<case="+TMP_case+">","");}
                    if (!TMP_case.equals("false")) {TMP_case = "";}
                    if (TMP_trig.contains("<imported>")) {TMP_imported = true; TMP_trig = TMP_trig.replace("<imported>", "");}
                    if (TMP_trig.contains("<formatted>")) {TMP_formatted = true; TMP_trig = TMP_trig.replace("<formatted>", "");}

                    String TMP_tags = "";
                    if (TMP_imported) {TMP_tags+="Imported";}
                    if (!TMP_w.equals("")) {if (!TMP_tags.equals("")) {TMP_tags += "\n";} TMP_tags += "Modifier: " + TMP_w;}
                    if (!TMP_list.equals("")) {if (!TMP_tags.equals("")) {TMP_tags += "\n";} TMP_tags += "List: " + TMP_list;}
                    if (!TMP_server.equals("")) {if (!TMP_tags.equals("")) {TMP_tags += "\n";} TMP_tags += "Server: " + TMP_server;}
                    if (!TMP_case.equals("")) {if (!TMP_tags.equals("")) {TMP_tags += "\n";} TMP_tags += "Case Insensitive";}
                    if (TMP_formatted) {if (!TMP_tags.equals("")) {TMP_tags += "\n";} TMP_tags += "Formatted";}


                    if (TMP_check.toString().equals(TMP_list)) {
                        TMP_test++;
                        TMP_type = TMP_type.replace("(","LeftParF6cyUQp9LeftPar").replace(")","RightParF6cyUQp9RightPar").replace("&","AmpF6cyUQp9Amp");
                        TMP_trig = TMP_trig.replace("(","LeftParF6cyUQp9LeftPar").replace(")","RightParF6cyUQp9RightPar").replace("&","AmpF6cyUQp9Amp");
                        String pre_out;
                        if (TMP_imported) {pre_out = "hover(&7>,You cannot edit\nimported triggers)";}
                        else {pre_out = "clickable(&7>,suggest_command,/trigger add "+i+" ,Add an event)";}
                        String post_out = "";
                        if (!TMP_tags.equals("")) {post_out = "hover(&8tags ,"+TMP_tags+")";}
                        if (!TMP_imported) {post_out += "clickable(&c-,suggest_command,/trigger delete "+i+" [enter to confirm],Remove trigger)";}
                        ChatHandler.warn(pre_out+" &8"+TMP_type+" "+Settings.col[0]+TMP_trig.replace(" "," "+Settings.col[0])+" "+post_out);

                        int tabbed_logic=0;

                        for (int j=2; j<global.trigger.get(i).size(); j++) {

                            StringBuilder TMP_extraspaces = new StringBuilder("  ");

                            String TMP_e = global.trigger.get(i).get(j);
                            String TMP_c;
                            if (!TMP_e.contains(" ")) {TMP_c = TMP_e; TMP_e = "";}
                            else {TMP_c = TMP_e.substring(0, TMP_e.indexOf(" ")); TMP_e = TMP_e.substring(TMP_e.indexOf(" ") + 1, TMP_e.length());}

                            if (TMP_c.equalsIgnoreCase("END")
                                    || TMP_c.toUpperCase().startsWith("ELSE")) {
                                tabbed_logic--;
                            }
                            for (int k=0; k<tabbed_logic; k++) {TMP_extraspaces.append("  ");}
                            TMP_c = TMP_c.replace("(","LeftParF6cyUQp9LeftPar").replace(")","RightParF6cyUQp9RightPar").replace("&","AmpF6cyUQp9Amp");
                            TMP_e = TMP_e.replace("(","LeftParF6cyUQp9LeftPar").replace(")","RightParF6cyUQp9RightPar").replace("&","AmpF6cyUQp9Amp");
                            String pre_out_event;
                            if (TMP_imported) {pre_out_event = " ";}
                            else {pre_out_event = "clickable(&c-,suggest_command,/trigger remove "+i+" "+j+" [enter to confirm],Remove event)";}
                            ChatHandler.warn(TMP_extraspaces+pre_out_event+" &8"+TMP_c+" &7"+TMP_e.replace(" "," &7").replace("\\","BackslashF6cyUQp9Backslash"));

                            if (TMP_c.toUpperCase().startsWith("IF")
                                    || TMP_c.toUpperCase().startsWith("FOR")
                                    || TMP_c.toUpperCase().startsWith("CHOOSE")
                                    || TMP_c.toUpperCase().startsWith("WAIT")
                                    || TMP_c.toUpperCase().startsWith("ELSE")
                                    || TMP_c.toUpperCase().startsWith("ASYNC")) {
                                tabbed_logic++;
                            }

                        }
                    }
                }
                if (TMP_test==0) {
                    ChatHandler.warn(ChatHandler.color("red", "There is no list with name " + TMP_check));
                    ChatHandler.warn(ChatHandler.color("red", "Create list " + TMP_check + " by using <list=" + TMP_check + "> when you create a trigger"));
                }
            }
            ChatHandler.warnBreak(1);
        }
    }

    private static void commandSettings(String args[]) {
        if (args.length < 2) {
            ChatHandler.warn("&c/trigger &csettings &c[clickable(&cdebug,suggest_command,/trigger settings debug ,&7Suggest &7/trigger &7settings &7debug)&c"
                    +"/clickable(&ctest,suggest_command,/trigger settings test ,&7Suggest &7/trigger &7settings &7test)&c"
                    +"/clickable(&ccolor,suggeest_command,/trigger settings color ,&7Suggest &7/trigger &7settings &7color)&c"
                    +"/clickable(&ckillfeed,suggest_command,/trigger settings killfeed ,&7Suggest &7/trigger &7settings &7killfeed)&c"
                    +"/clickable(&cnotify,suggest_command,/trigger settings notify ,&7Suggest &7/trigger &7settings &7notify)&c"
                    +"] &c<...>");
            ChatHandler.warn("&c/trigger settings &c[clickable(&cbeta,run_command,/trigger settings beta,&7Run &7/trigger &7settings &7beta)&c"
                    +"/clickable(&cfile,suggest_command,/trigger settings file ,&7Suggest &7/trigger &7settings &7file)&c"
                    +"/clickable(&cbackup,run_command,/trigger settings backup,&7Run &7/trigger &7settings &7backup)&c"
                    +"/clickable(&cfps,suggest_command,/trigger settings fps ,&7Run &7/trigger &7settings &7fps)&c"
                    +"] &c<...>");
        } else {
            if (args[1].equalsIgnoreCase("DEBUG")) {
                if (args.length == 2) {
                    if (!global.debug) {ChatHandler.warn(ChatHandler.color("gray", "Toggled debug mode") + " " + ChatHandler.color(Settings.col[0], "on")); global.debug=true;}
                    else {ChatHandler.warn(ChatHandler.color("gray", "Toggled debug mode") + " " + ChatHandler.color(Settings.col[0], "off")); global.debug=false;}
                } else {
                    if (args[2].equalsIgnoreCase("CHAT")) {
                        if (!global.debugChat) {ChatHandler.warn(ChatHandler.color("gray", "Toggled debug chat mode") + " " + ChatHandler.color(Settings.col[0], "on")); global.debugChat=true;}
                        else {ChatHandler.warn(ChatHandler.color("gray", "Toggled debug chat mode") + " " + ChatHandler.color(Settings.col[0], "off")); global.debugChat=false;}
                    }
                }

            } else if (args[1].equalsIgnoreCase("COLOR") || args[1].equalsIgnoreCase("COLOUR")) {
                if (args.length < 3) {
                    ChatHandler.warn(ChatHandler.color("red", "/trigger settings color [color]"));
                } else {
                    args[2] = args[2].replace("_", "");
                    args[2] = args[2].replace("&", "");
                    if (args.length==4) {args[2] = args[2]+args[3];}
                    if      (args[2].equalsIgnoreCase("0") || args[2].equalsIgnoreCase("BLACK"))       {Settings.col[0] = "&0"; Settings.col[1] = "black";          ChatHandler.warn(ChatHandler.color(Settings.col[0], "Changed color"));}
                    else if (args[2].equalsIgnoreCase("1") || args[2].equalsIgnoreCase("DARKBLUE"))    {Settings.col[0] = "&1"; Settings.col[1] = "dark_blue";      ChatHandler.warn(ChatHandler.color(Settings.col[0], "Changed color"));}
                    else if (args[2].equalsIgnoreCase("2") || args[2].equalsIgnoreCase("DARKGREEN"))   {Settings.col[0] = "&2"; Settings.col[1] = "dark_green";     ChatHandler.warn(ChatHandler.color(Settings.col[0], "Changed color"));}
                    else if (args[2].equalsIgnoreCase("3") || args[2].equalsIgnoreCase("DARKAQUA"))    {Settings.col[0] = "&3"; Settings.col[1] = "dark_aqua";      ChatHandler.warn(ChatHandler.color(Settings.col[0], "Changed color"));}
                    else if (args[2].equalsIgnoreCase("4") || args[2].equalsIgnoreCase("DARKRED"))     {Settings.col[0] = "&4"; Settings.col[1] = "dark_red";       ChatHandler.warn(ChatHandler.color(Settings.col[0], "Changed color"));}
                    else if (args[2].equalsIgnoreCase("5") || args[2].equalsIgnoreCase("DARKPURPLE"))  {Settings.col[0] = "&5"; Settings.col[1] = "dark_purple";    ChatHandler.warn(ChatHandler.color(Settings.col[0], "Changed color"));}
                    else if (args[2].equalsIgnoreCase("6") || args[2].equalsIgnoreCase("GOLD"))        {Settings.col[0] = "&6"; Settings.col[1] = "gold";           ChatHandler.warn(ChatHandler.color(Settings.col[0], "Changed color"));}
                    else if (args[2].equalsIgnoreCase("7") || args[2].equalsIgnoreCase("GRAY"))        {Settings.col[0] = "&7"; Settings.col[1] = "gray";           ChatHandler.warn(ChatHandler.color(Settings.col[0], "Changed color"));}
                    else if (args[2].equalsIgnoreCase("8") || args[2].equalsIgnoreCase("DARKGRAY"))    {Settings.col[0] = "&8"; Settings.col[1] = "dark_gray";      ChatHandler.warn(ChatHandler.color(Settings.col[0], "Changed color"));}
                    else if (args[2].equalsIgnoreCase("9") || args[2].equalsIgnoreCase("BLUE"))        {Settings.col[0] = "&9"; Settings.col[1] = "blue";           ChatHandler.warn(ChatHandler.color(Settings.col[0], "Changed color"));}
                    else if (args[2].equalsIgnoreCase("a") || args[2].equalsIgnoreCase("GREEN"))       {Settings.col[0] = "&a"; Settings.col[1] = "green";          ChatHandler.warn(ChatHandler.color(Settings.col[0], "Changed color"));}
                    else if (args[2].equalsIgnoreCase("b") || args[2].equalsIgnoreCase("AQUA"))        {Settings.col[0] = "&b"; Settings.col[1] = "aqua";           ChatHandler.warn(ChatHandler.color(Settings.col[0], "Changed color"));}
                    else if (args[2].equalsIgnoreCase("c") || args[2].equalsIgnoreCase("RED"))         {Settings.col[0] = "&c"; Settings.col[1] = "red";            ChatHandler.warn(ChatHandler.color(Settings.col[0], "Changed color"));}
                    else if (args[2].equalsIgnoreCase("d") || args[2].equalsIgnoreCase("LIGHTPURPLE")) {Settings.col[0] = "&d"; Settings.col[1] = "light_purple";   ChatHandler.warn(ChatHandler.color(Settings.col[0], "Changed color"));}
                    else if (args[2].equalsIgnoreCase("e") || args[2].equalsIgnoreCase("YELLOW"))      {Settings.col[0] = "&e"; Settings.col[1] = "yellow";         ChatHandler.warn(ChatHandler.color(Settings.col[0], "Changed color"));}
                    else if (args[2].equalsIgnoreCase("f") || args[2].equalsIgnoreCase("WHITE"))       {Settings.col[0] = "&f"; Settings.col[1] = "white";          ChatHandler.warn(ChatHandler.color(Settings.col[0], "Changed color"));}
                    else {ChatHandler.warn(ChatHandler.color("red", "Not a valid color"));}
                    try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                }
            } else if (args[1].equalsIgnoreCase("KILLFEED")) {
                if (args.length>2) {
                    if (args[2].equalsIgnoreCase("POS") || args[2].equalsIgnoreCase("POSITION")) {
                        if (args.length>3) {
                            ScaledResolution var5 = new ScaledResolution(Minecraft.getMinecraft());
                            float width = var5.getScaledWidth();
                            float height = var5.getScaledHeight();
                            if (args[3].equalsIgnoreCase("TOP-LEFT") || args[3].equalsIgnoreCase("TL")) {
                                Settings.killfeedPosition[0] = 5.0/width;
                                Settings.killfeedPosition[1] = 5.0/height;
                                ChatHandler.warn(ChatHandler.color("gray", "Changed killfeed position to") + " " + ChatHandler.color(Settings.col[0], "top-left"));
                                try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                            } else if (args[3].equalsIgnoreCase("TOP-RIGHT") || args[3].equalsIgnoreCase("TR")) {
                                Settings.killfeedPosition[0] = (width - 5.0)/width;
                                Settings.killfeedPosition[1]= 5.0/height;
                                ChatHandler.warn(ChatHandler.color("gray", "Changed killfeed position to ") + " " + ChatHandler.color(Settings.col[0], "top-right"));
                                try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                            } else {
                                global.showDisplayGui = true;
                            }
                        } else {
                            global.showDisplayGui = true;
                        }
                    } else if (args[2].equalsIgnoreCase("FADE")) {
                        if (Settings.killfeedFade) {
                            Settings.killfeedFade = false;
                            CommandReference.silentResetAll();
                            ChatHandler.warn(ChatHandler.color("gray", "Toggled killfeed fade") + " " + ChatHandler.color("red", "off"));
                            try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                        } else {
                            Settings.killfeedFade = true;
                            CommandReference.silentResetAll();
                            ChatHandler.warn(ChatHandler.color("gray", "Toggled killfeed fade") + " " + ChatHandler.color("green", "on"));
                            try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                        }
                    } else if (args[2].equalsIgnoreCase("SHOWINNOTIFICATION") || args[2].equalsIgnoreCase("SHOWINNOTIFICATIONS") || args[2].equalsIgnoreCase("SHOWINNOTIFY")) {
                        if (Settings.killfeedInNotify) {
                            Settings.killfeedInNotify = false;
                            CommandReference.silentResetAll();
                            ChatHandler.warn(ChatHandler.color("gray", "Toggled showing killfeed as notifications") + " " + ChatHandler.color("red", "off"));
                            try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                        } else {
                            Settings.killfeedInNotify = true;
                            CommandReference.silentResetAll();
                            ChatHandler.warn(ChatHandler.color("gray", "Toggled showing killfeed as notifications") + " " + ChatHandler.color("green", "on"));
                            try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                        }
                    } else if (args[2].equalsIgnoreCase("BACKGROUND")) {
                        if (Settings.killfeedBackground) {
                            Settings.killfeedBackground = false;
                            CommandReference.silentResetAll();
                            ChatHandler.warn(ChatHandler.color("gray", "Toggled killfeed background") + " " + ChatHandler.color("red", "off"));
                            try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                        } else {
                            Settings.killfeedBackground = true;
                            CommandReference.silentResetAll();
                            ChatHandler.warn(ChatHandler.color("gray", "Toggled killfeed background") + " " + ChatHandler.color("green", "on"));
                            try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                        }
                    } else if (args[2].equalsIgnoreCase("PAUSE")) {
                        if (args.length == 4) {
                            try {
                                Settings.killfeedPause = Integer.parseInt(args[3].trim());
                                ChatHandler.warn("gray", "Changed default killfeed pause to " + Settings.col[0] + Settings.notifyPause);
                                try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                            } catch (NumberFormatException exception) {
                                ChatHandler.warn("red", "/trigger settings killfeed pause <number>");
                            }
                        } else {
                            ChatHandler.warn("red", "/trigger settings killfeed pause <number>");
                        }
                    } else {
                        ChatHandler.warn("&c/trigger &csettings &ckillfeed &c[clickable(&cposition,suggest_command,/trigger settings killfeed position ,&7Suggest &7/trigger &7settings &7killfeed &7position)&c/clickable(&cshowInNotify,run_command,/trigger settings killfeed showInNotify,&7Run &7/trigger &7settings &7killfeed &7showInNotify)&c] &c<...>");
                        ChatHandler.warn("&c/trigger &csettings &ckillfeed &c[clickable(&cfade,run_command,/trigger settings killfeed fade,&7Run &7/trigger &7settings &7killfeed &7fade)&c/clickable(&cbackground,run_command,/trigger settings killfeed background,&7Run &7/trigger &7setting &7killfeed &7background)&c/clickable(&cpause,suggest_command,/trigger settings pause ,&7Suggest &7/trigger &7settings &7killfeed &7pause)&c] &c<...>");
                    }
                } else {
                    ChatHandler.warn("&c/trigger &csettings &ckillfeed &c[clickable(&cposition,suggest_command,/trigger settings killfeed position ,&7Suggest &7/trigger &7settings &7killfeed &7position)&c/clickable(&cshowInNotify,run_command,/trigger settings killfeed showInNotify,&7Run &7/trigger &7settings &7killfeed &7showInNotify)&c] &c<...>");
                    ChatHandler.warn("&c/trigger &csettings &ckillfeed &c[clickable(&cfade,run_command,/trigger settings killfeed fade,&7Run &7/trigger &7settings &7killfeed &7fade)&c/clickable(&cbackground,run_command,/trigger settings killfeed background,&7Run &7/trigger &7setting &7killfeed &7background)&c/clickable(&cpause,suggest_command,/trigger settings pause ,&7Suggest &7/trigger &7settings &7killfeed &7pause)&c] &c<...>");
                }
            } else if (args[1].equalsIgnoreCase("NOTIFY")) {
                if (args.length>2) {
                    if (args[2].equalsIgnoreCase("SPEED")) {
                        if (args.length>3) {
                            try {
                                int get = Integer.parseInt(args[3]);
                                if (get<=1) {
                                    throw new NumberFormatException();
                                } else {
                                    Settings.notifySpeed = get;
                                    ChatHandler.warn(ChatHandler.color("gray", "Notification speed set to") + " " + ChatHandler.color(Settings.col[0], get+""));
                                    try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                                }
                            } catch(NumberFormatException e) {
                                e.printStackTrace();
                                ChatHandler.warn(ChatHandler.color("red", "/trigger settings notify [speed] <integer (greater is slower)>"));
                                ChatHandler.warn(ChatHandler.color("red", "<integer> MUST be a number greater than 1!"));
                            }
                        } else {
                            ChatHandler.warn(ChatHandler.color("red", "/trigger settings notify [speed] <integer (greater is slower)>"));
                        }
                    } else if (args[2].equalsIgnoreCase("BACKGROUND")) {
                        if (Settings.notifyBackground) {
                            Settings.notifyBackground = false;
                            CommandReference.silentResetAll();
                            ChatHandler.warn(ChatHandler.color("gray", "Toggled notify background") + " " + ChatHandler.color("red", "off"));
                            try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                        } else {
                            Settings.notifyBackground = true;
                            CommandReference.silentResetAll();
                            ChatHandler.warn(ChatHandler.color("gray", "Toggled notify background") + " " + ChatHandler.color("green", "on"));
                            try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                        }
                    } else if (args[2].equalsIgnoreCase("PAUSE")) {
                        if (args.length == 4) {
                            try {
                                Settings.notifyPause = Integer.parseInt(args[3].trim());
                                ChatHandler.warn("gray", "Changed default notify pause to " + Settings.col[0] + Settings.notifyPause);
                                try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                            } catch (NumberFormatException exception) {
                                ChatHandler.warn("red", "/trigger settings notify pause <number>");
                            }
                        } else {
                            ChatHandler.warn("red", "/trigger settings notify pause <number>");
                        }
                    } else {
                        ChatHandler.warn("&c/trigger &csettings &cnotify &c[clickable(&cspeed,suggest_command,/trigger settings notify speed ,&7Suggest &7/trigger &7settings &7notify &7speed)&c/clickable(&cbackground,run_command,/trigger settings notify background,&7Run &7/trigger &7settings &7notify &7background)&c/clickable(&cpause,suggest_command,/trigger settings notify pause ,&7Suggest &7/trigger &7settings &7notify &7pause)&c] &c<...>");
                    }
                } else {
                    ChatHandler.warn("&c/trigger &csettings &cnotify &c[clickable(&cspeed,suggest_command,/trigger settings notify speed ,&7Suggest &7/trigger &7settings &7notify &7speed)&c/clickable(&cbackground,run_command,/trigger settings notify background,&7Run &7/trigger &7settings &7notify &7background)&c/clickable(&cpause,suggest_command,/trigger settings notify pause ,&7Suggest &7/trigger &7settings &7notify &7pause)&c] &c<...>");
                }
            } else if (args[1].equalsIgnoreCase("BETA")) {
                if (args.length>2) {
                    if (args[2].equalsIgnoreCase("TOGGLE")) {
                        if (Settings.isBeta) {
                            Settings.isBeta = false;
                            ChatHandler.warn(ChatHandler.color("red", "You have turned beta notifications off!"));
                            ChatHandler.warn(ChatHandler.color("red", "For more info, do </trigger settings beta>"));
                            UpdateHandler.loadVersion("http://ct.kerbybit.com/download/version.txt");
                            try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                        } else {
                            Settings.isBeta = true;
                            ChatHandler.warn(ChatHandler.color("red", "You have turned nightly notifications")+" "+ChatHandler.color("green","on!"));
                            ChatHandler.warn(ChatHandler.color("red", "For more info, do </trigger settings beta>"));
                            UpdateHandler.loadVersion("http://ct.kerbybit.com/download/betaversion.txt");
                            try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                        }
                    } else {
                        ChatHandler.warn(ChatHandler.color("red", "/trigger settings beta [toggle]"));
                    }
                } else {
                    ChatHandler.warnBreak(0);
                    if (Settings.isBeta) {
                        ChatHandler.warn(ChatHandler.color("red", "You currently have the beta version")+" "+ChatHandler.color("green","enabled!"));
                        ChatHandler.warn(ChatHandler.color("red", "You will recieve notifications on nightly builds"));
                        ChatHandler.warn("&cTo &cchange &cthis, &cdo &c<clickable(&c/trigger settings beta toggle,run_command,/trigger settings beta toggle,&7Run &7trigger &7settings &7beta &7toggle)&c>");
                        ChatHandler.warn(ChatHandler.color("red", ""));
                        ChatHandler.warn(ChatHandler.color("red", "The beta versions may have unforseen bugs"));
                        ChatHandler.warn(ChatHandler.color("red", "but gets updated regularly with new features"));
                    } else {
                        ChatHandler.warn(ChatHandler.color("red", "You currently have the beta version disabled!"));
                        ChatHandler.warn(ChatHandler.color("red", "Although this doesnt prevent you from downloading"));
                        ChatHandler.warn(ChatHandler.color("red", "and using the beta version,"));
                        ChatHandler.warn(ChatHandler.color("red", "You will NOT get notified of nightly builds"));
                        ChatHandler.warn("&cTo &copt &cinto &cthe &cbeta, &cdo &c<clickable(&c/trigger settings beta toggle,run_command,/trigger settings beta toggle,&7Run &7trigger &7settings &7beta &7toggle)&c>");
                        ChatHandler.warn(ChatHandler.color("red", ""));
                        ChatHandler.warn(ChatHandler.color("red", "The beta versions may have unforseen bugs"));
                        ChatHandler.warn(ChatHandler.color("red", "but gets updated regularly with new features"));
                    }
                    ChatHandler.warnBreak(1);
                }
            } else if (args[1].equalsIgnoreCase("TEST")){
                if (args.length<3) {
                    ChatHandler.warn("&c/trigger &csettings &ctest &c[clickable(&conWorldLoad,run_command,/trigger settings test onWorldLoad,&7Run &7/trigger &7settings &7test &7onWorldLoad)&c/clickable(&conWorldFirstLoad,run_command,/trigger settings test onWorldFirstLoad,&7Run &7/trigger &7settings &7test &7onWorldFirstLoad)&c]");
                    ChatHandler.warn("&c/trigger &csettings &ctest &c[clickable(&conServerChange,run_command,/trigger settings test onServerChange,&7Run &7/trigger &7settings &7test &7onServerChange)&c/clickable(&conNewDay,run_command,/trigger settings test onNewDay,&7Run &7/trigger &7settings &7test &7onNewDay)&c]");
                } else {
                    if (args[2].equalsIgnoreCase("ONWORLDLOAD")) {
                        global.worldLoaded = true;
                    } else if (args[2].equalsIgnoreCase("ONWORLDFIRSTLOAD")) {
                        global.worldLoaded = true;
                        global.worldFirstLoad = true;
                    } else if (args[2].equalsIgnoreCase("ONSERVERCHANGE")) {
                        global.worldLoaded = true;
                        global.connectedToServer = "";
                    } else if (args[2].equalsIgnoreCase("ONNEWDAY")) {
                        global.currentDate = "";
                    } else {
                        ChatHandler.warn("&c/trigger &csettings &ctest &c[clickable(&conWorldLoad,run_command,/trigger settings test onWorldLoad,&7Run &7/trigger &7settings &7test &7onWorldLoad)&c/clickable(&conWorldFirstLoad,run_command,/trigger settings test onWorldFirstLoad,&7Run &7/trigger &7settings &7test &7onWorldFirstLoad)&c]");
                        ChatHandler.warn("&c/trigger &csettings &ctest &c[clickable(&conServerChange,run_command,/trigger settings test onServerChange,&7Run &7/trigger &7settings &7test &7onServerChange)&c/clickable(&conNewDay,run_command,/trigger settings test onNewDay,&7Run &7/trigger &7settings &7test &7onNewDay)&c]");
                    }
                }
            } else if (args[1].equalsIgnoreCase("DUMP")) {
                if (args.length == 2) {
                    for (String fmsg : global.chatHistory) {
                        String tmp_out = ChatHandler.removeFormatting(fmsg);
                        global.copyText.add(tmp_out.replace("\n", "\\n"));
                        tmp_out = tmp_out.replace("'", "\\'");
                        List<String> TMP_eventout = new ArrayList<>();
                        TMP_eventout.add("text:'" + tmp_out + "',clickEvent:{action:'run_command',value:'/trigger copy CopyFromDebugChat " + (global.copyText.size()-1) + "'},hoverEvent:{action:'show_text',value:'Click to copy\n" + tmp_out + "'}");
                        ChatHandler.sendJson(TMP_eventout);
                    }
                } else {
                    try {
                        int get = Integer.parseInt(args[2]);
                        if (get > global.chatHistory.size()) {
                            ChatHandler.warn("&c/trigger settings dump [number]");
                            ChatHandler.warn("&c[number] must not be bigger than "+global.chatHistory.size()+"!");
                        } else {
                            for (int i=global.chatHistory.size() - get; i < global.chatHistory.size(); i++) {
                                String tmp_out = ChatHandler.removeFormatting(global.chatHistory.get(i));
                                global.copyText.add(tmp_out.replace("\n", "\\n"));
                                tmp_out = tmp_out.replace("'", "\\'");
                                List<String> TMP_eventout = new ArrayList<>();
                                TMP_eventout.add("text:'" + tmp_out + "',clickEvent:{action:'run_command',value:'/trigger copy CopyFromDebugChat " + (global.copyText.size() - 1) + "'},hoverEvent:{action:'show_text',value:'Click to copy\n" + tmp_out + "'}");
                                ChatHandler.sendJson(TMP_eventout);
                            }
                        }
                    } catch (Exception e) {
                        if (args[2].equalsIgnoreCase("killfeed")) {
                            ChatHandler.warnBreak(0);
                            if (args.length == 4) {
                                if (args[3].equalsIgnoreCase("formatted")) {
                                    KillfeedHandler.showKillfeedHistory(true);
                                } else KillfeedHandler.showKillfeedHistory();
                            } else KillfeedHandler.showKillfeedHistory();
                            ChatHandler.warnBreak(1);
                        } else if (args[2].equalsIgnoreCase("notify")) {
                            ChatHandler.warnBreak(0);
                            if (args.length == 4) {
                                if (args[3].equalsIgnoreCase("formatted")) {
                                    NotifyHandler.showNotifyHistory(true);
                                } else NotifyHandler.showNotifyHistory();
                            } else NotifyHandler.showNotifyHistory();
                            ChatHandler.warnBreak(1);
                        } else if (args[2].equalsIgnoreCase("ASYNC")) {
                            ChatHandler.warnBreak(0);
                            if (global.Async_string.size() > 0) {
                                Map<String, String> temp = new HashMap<>(global.Async_string);
                                for (Map.Entry<String, String> entry : temp.entrySet()) {
                                    ChatHandler.warn(entry.getKey() + " - " + entry.getValue());
                                }
                            } else ChatHandler.warn("red", "There are currently no async strings.");
                            ChatHandler.warnBreak(1);
                        } else if (args[2].equalsIgnoreCase("TEMP")) {
                            ChatHandler.warnBreak(0);
                            if (global.TMP_string.size() > 0) {
                                Map<String, String> temp = new HashMap<>(global.TMP_string);
                                for (Map.Entry<String, String> string : temp.entrySet()) {
                                    ChatHandler.warn(string.getKey() + " - " + string.getValue());
                                }
                            } else ChatHandler.warn("red", "There are currently no temp strings.");
                            ChatHandler.warnBreak(1);
                        } else if (args[2].equalsIgnoreCase("STRINGS")) {
                            ChatHandler.warnBreak(0);
                            if (global.USR_string.size() > 0) {
                                Map<String, String> temp = new HashMap<>(global.USR_string);
                                for (Map.Entry<String, String> string : temp.entrySet()) {
                                    ChatHandler.warn(string.getKey() + " - " + string.getValue());
                                }
                            } else ChatHandler.warn("red", "There are currently no user strings.");
                            ChatHandler.warnBreak(1);
                        } else if (args[2].equalsIgnoreCase("MARKEDSTRINGS")) {
                            ChatHandler.warnBreak(0);
                            if (global.USR_string_mark.size() > 0) {
                                Map<String, String> temp = new HashMap<>(global.USR_string_mark);
                                for (Map.Entry<String, String> string : temp.entrySet()) {
                                    ChatHandler.warn(string.getKey() + " - " + string.getValue());
                                }
                            } else ChatHandler.warn("red", "There are currently no marked user strings.");
                            ChatHandler.warnBreak(1);
                        } else if (args[2].equalsIgnoreCase("MARKEDDELSTRINGS")) {
                            ChatHandler.warnBreak(0);
                            if (global.USR_string_markdel.size() > 0) {
                                Map<String, String> temp = new HashMap<>(global.USR_string_markdel);
                                for (Map.Entry<String, String> string : temp.entrySet()) {
                                    ChatHandler.warn(string.getKey() + " - " + string.getValue());
                                }
                            } else ChatHandler.warn("red", "There are currently no marked user strings ready for deletion.");
                            ChatHandler.warnBreak(1);
                        } else if (args[2].equalsIgnoreCase("ACTIONBAR")) {
                            List<String> temp = new ArrayList<>(global.actionHistory);
                            for (String action : temp) {
                                String tmp_out = ChatHandler.removeFormatting(action);
                                global.copyText.add(tmp_out.replace("\n", "\\n"));
                                tmp_out = tmp_out.replace("'", "\\'");
                                List<String> TMP_eventout = new ArrayList<>();
                                TMP_eventout.add("text:'" + tmp_out + "',clickEvent:{action:'run_command',value:'/trigger copy CopyFromDebugChat " + (global.copyText.size() - 1) + "'},hoverEvent:{action:'show_text',value:'Click to copy\n" + tmp_out + "'}");
                                ChatHandler.sendJson(TMP_eventout);
                            }
                        } else if (args[2].equalsIgnoreCase("LISTS")) {
                            ChatHandler.warnBreak(0);
                            ListHandler.dumpLists();
                            ChatHandler.warnBreak(1);
                        } else if (args[2].equalsIgnoreCase("JSONS")) {
                            ChatHandler.warnBreak(0);
                            JsonHandler.dumpJsons();
                            ChatHandler.warnBreak(1);
                        } else if (args[2].equalsIgnoreCase("DISPLAYS")) {
                            ChatHandler.warnBreak(0);
                            DisplayHandler.dumpDisplays();
                            ChatHandler.warnBreak(1);
                        } else {
                            ChatHandler.warn(ChatHandler.color("red", "/trigger settings dump [number]"));
                            ChatHandler.warn(ChatHandler.color("red", args[2] + " is not a number!"));
                        }
                    }
                }
            } else if (args[1].equalsIgnoreCase("FPS")) {
                if (args.length == 2) {
                    ChatHandler.warn(ChatHandler.color("red", "/trigger settings fps [low/medium/high] <...>"));
                } else {
                    if (args[2].equalsIgnoreCase("LOW") || args[2].equalsIgnoreCase("L")) {
                        if (args.length == 3) {
                            ChatHandler.warn(ChatHandler.color("red","/trigger settings fps low [color] [number]"));
                        } else {
                            if (args.length == 4) {
                                global.fpslowcol = args[3];
                                ChatHandler.warn("gray", "Changed fps low to " + global.fpslowcol + global.fpslow);
                                try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                            } else {
                                try {
                                    global.fpslowcol = args[3];
                                    global.fpslow = Integer.parseInt(args[4]);
                                    ChatHandler.warn("gray", "Changed fps low to " + global.fpslowcol + global.fpslow);
                                    try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                                } catch (NumberFormatException e) {
                                    ChatHandler.warn(ChatHandler.color("red", "/trigger settings fps low [color] [number]"));
                                    ChatHandler.warn(ChatHandler.color("red", args[4] + " is not a number!"));
                                }
                            }
                        }
                    } else if (args[2].equalsIgnoreCase("MED") || args[2].equalsIgnoreCase("MEDIUM") || args[2].equalsIgnoreCase("M")) {
                        if (args.length == 3) {
                            ChatHandler.warn(ChatHandler.color("red","/trigger settings fps medium [color]"));
                        } else {
                            global.fpsmedcol = args[3];
                            ChatHandler.warn("gray", "Changed fps medium to " + global.fpsmedcol + global.fpslow + "-" + global.fpshigh);
                            try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                        }
                    } else if (args[2].equalsIgnoreCase("HIGH") || args[2].equalsIgnoreCase("H")) {
                        if (args.length == 3) {
                            ChatHandler.warn(ChatHandler.color("red","/trigger settings fps high [color] [number]"));
                        } else {
                            if (args.length == 4) {
                                global.fpshighcol = args[3];
                                ChatHandler.warn("gray", "Changed fps high to " + global.fpshighcol + global.fpshigh);
                                try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                            } else {
                                try {
                                    global.fpshighcol = args[3];
                                    global.fpshigh = Integer.parseInt(args[4]);
                                    ChatHandler.warn("gray", "Changed fps high to " + global.fpshighcol + global.fpshigh);
                                    try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                                } catch (NumberFormatException e) {
                                    ChatHandler.warn(ChatHandler.color("red", "/trigger settings fps high [color] [number]"));
                                    ChatHandler.warn(ChatHandler.color("red", args[4] + " is not a number!"));
                                }
                            }
                        }
                    } else {
                        ChatHandler.warn(ChatHandler.color("red", "/trigger settings fps [low/medium/high] <...>"));
                    }
                }
            } else if (args[1].equalsIgnoreCase("file")) {
                if (args.length == 2) {
                    ChatHandler.warn("&c/trigger &csettings &cfile &c[clickable(&cformatting,run_command,/trigger settings file formatting,&7Run &7/trigger &7settings &7file &7formatting)&c] &c<...>");
                } else {
                    if (args.length == 3) {
                        if (args[2].equalsIgnoreCase("FORMATTING")) {
                            if (Settings.oldFormatting) {
                                Settings.oldFormatting = false;
                                try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                                ChatHandler.warn("gray", "Toggled old file formatting &cOFF");
                            } else {
                                Settings.oldFormatting = true;
                                try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                                ChatHandler.warn("gray", "Toggled old file formatting &aON");
                            }
                        } else {
                            ChatHandler.warn("&c/trigger &csettings &cfile &c[clickable(&cformatting,run_command,/trigger settings file formatting,&7Run &7/trigger &7settings &7file &7formatting)&c] &c<...>");
                        }
                    } else {
                        if (args.length == 4) {
                            if (args[3].equalsIgnoreCase("new")) {
                                Settings.oldFormatting = false;
                                try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                                ChatHandler.warn("gray", "Toggled old file formatting &cOFF");
                            } else if (args[3].equalsIgnoreCase("old")) {
                                Settings.oldFormatting = true;
                                try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                                ChatHandler.warn("gray", "Toggled old file formatting &aON");
                            } else {
                                ChatHandler.warn("red", "/trigger settings file formatting [new/old]");
                            }
                        } else {
                            ChatHandler.warn("red", "/trigger settings file formatting [new/old]");
                        }
                    }
                }
            } else if (args[1].equalsIgnoreCase("backup")) {
                if (Settings.backupFiles) {
                    Settings.backupFiles = false;
                    try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                    ChatHandler.warn("gray", "Toggled file backup &cOFF");
                } else {
                    Settings.backupFiles = true;
                    try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
                    ChatHandler.warn("gray", "Toggled file backup &aON");
                }
            } else {
                ChatHandler.warn("&c/trigger &csettings &c[clickable(&cdebug,suggest_command,/trigger settings debug ,&7Suggest &7/trigger &7settings &7debug)&c"
                        +"/clickable(&ctest,suggest_command,/trigger settings test ,&7Suggest &7/trigger &7settings &7test)&c"
                        +"/clickable(&ccolor,suggeest_command,/trigger settings color ,&7Suggest &7/trigger &7settings &7color)&c"
                        +"/clickable(&ckillfeed,suggest_command,/trigger settings killfeed ,&7Suggest &7/trigger &7settings &7killfeed)&c"
                        +"/clickable(&cnotify,suggest_command,/trigger settings notify ,&7Suggest &7/trigger &7settings &7notify)&c"
                        +"] &c<...>");
                ChatHandler.warn("&c/trigger settings &c[clickable(&cbeta,run_command,/trigger settings beta,&7Run &7/trigger &7settings &7beta)&c"
                        +"/clickable(&cfile,suggest_command,/trigger settings file ,&7Run &7/trigger &7settings &7file)&c"
                        +"/clickable(&cbackup,run_command,/trigger settings backup,&7Run &7/trigger &7settings &7backup)&c"
                        +"/clickable(&cfps,suggest_command,/trigger settings fps ,&7Run &7/trigger &7settings &7fps)&c"
                        +"] &c<...>");
            }
        }
    }

    private static void commandSave() {
        CommandReference.silentResetAll();
        ChatHandler.warn(ChatHandler.color(Settings.col[0], "Organized and saved files"));
    }

    public static void commandLoad() {
        global.canSave = true;
        try {
            CommandReference.clearAll();
            global.trigger = FileHandler.loadTriggers("./mods/ChatTriggers/triggers.txt", false, null);
            global.USR_string = FileHandler.loadStrings();
            FileHandler.loadSettings();
            FileHandler.loadImports();
            CommandReference.silentResetAll();
            ChatHandler.warn(Settings.col[0] + "Files loaded");
            global.worldLoaded = true;
        } catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error loading triggers!"));}
    }

    private static void commandFail() {
        ChatHandler.warn(ChatHandler.color(Settings.col[0], "Simulating massive failure..."));
        CommandReference.resetAll();
    }
}
