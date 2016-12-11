package com.kerbybit.chattriggers.references;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.global;

public class BugTracker {
    public static void send(Exception e) {
        send(e, "none");
    }

    public static void send(Exception e, String type) {

        for (StackTraceElement stack : e.getStackTrace()) {
            //if (stack.toString().toUpperCase().contains("CHATTRIGGERS")) {
                global.bugReport.add(stack.toString());
            //}
        }
        ChatHandler.warn(ChatHandler.color("darkred",getError(type)));
        ChatHandler.warn("&4Click clickable(&c[HERE],run_command,/trigger submitbugreport,Send a bug report) &4to submit a bug report");
    }

    private static String getError(String type) {
        if (type.equalsIgnoreCase("command")) {
            return "An unknown error occurred while performing this command";
        } else if (type.equalsIgnoreCase("chat")) {
            return "An unknown error has occured while executing \"&cchat&4\"";
        } else if (type.equalsIgnoreCase("onRightClickPlayer")) {
            return "An unknown error has occured while executing \"&conRightClickPlayer&4\"";
        } else if (type.equalsIgnoreCase("onWorldLoad")) {
            return "An unknown error has occured while executing \"&conWorldLoad&4\"";
        } else if (type.equalsIgnoreCase("onClientTick")) {
            return "An unknown error has occured while executing \"&conClientTick&4\"";
        } else {
            return "An unknown error has occurred";
        }
    }
}
