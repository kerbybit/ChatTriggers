package com.kerbybit.chattriggers.references;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.commands.CommandTrigger;
import com.kerbybit.chattriggers.globalvars.Settings;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.triggers.EventsHandler;
import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BugTracker {
    public static void send() {
        if (global.bugReport.size() > 0) {
            Thread threadSubmitBugReport = new Thread(() -> {
                try {
                    ChatHandler.warn("&7Sending bug report...");
                    StringBuilder bug = new StringBuilder();
                    if (global.bugLastCommand.equals("")) {
                        bug.append(global.bugLastEvent).append("\n\n");
                    } else {
                        bug.append(global.bugLastCommand).append("\n\n");
                    }
                    for (String b : global.bugReport) {
                        bug.append(b).append("\n");
                    }
                    URL url = new URL("http://ct.kerbybit.com/bugreport/");
                    Map<String,Object> params = new LinkedHashMap<>();
                    params.put("name", Minecraft.getMinecraft().player.getDisplayNameString());
                    params.put("uuid", Minecraft.getMinecraft().player.getUniqueID());
                    params.put("bug", bug.toString());

                    StringBuilder postData = new StringBuilder();
                    for (Map.Entry<String,Object> param : params.entrySet()) {
                        if (postData.length() != 0) postData.append('&');
                        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                    }
                    byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postDataBytes);
                    Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    ChatHandler.warn(Settings.col[0] + "Bug report submitted successfully!");
                    global.bugReport.clear();
                } catch (Exception e) {
                    ChatHandler.warn("&4An error occured while submitting a bug report!");
                    ChatHandler.warn("&4Is ct.kerbybit.com down?");
                }
            });
            threadSubmitBugReport.start();
        }
    }

    public static void show(Exception e) {
        show(e, "none");
    }

    public static void show(Exception e, String type) {
        if (e != null) {
            e.printStackTrace();

            for (StackTraceElement stack : e.getStackTrace()) {
                global.bugReport.add(stack.toString());
            }
        }
        if (type.equals("command")) {
            global.bugLastCommand = global.lastCommand;
            global.bugLastEvent = "";
        } else {
            global.bugLastCommand = "";
            global.bugLastEvent = global.lastEvent;
        }
        ChatHandler.warn(ChatHandler.color("darkred",getError(type)));
        ChatHandler.warn("&4Click clickable(&c[HERE],run_command,/trigger submitbugreport,Send a bug report) &4to submit a bug report");

        for (int i=0; i<global.onUnknownError.size(); i++) {
            //add all events to temp list
            List<String> TMP_events = new ArrayList<>();
            for (int j=2; j<global.onUnknownError.get(i).size(); j++) {TMP_events.add(global.onUnknownError.get(i).get(j));}

            //do events
            EventsHandler.doEvents(TMP_events, null);
        }
    }

    private static String getError(String type) {
        switch (type.toLowerCase()) {
            case "command":
                return "An unknown error occurred while performing this command";
            case "chat":
                return "An unknown error has occured while executing \"&cchat&4\"";
            case "onrightclickplayer":
                return "An unknown error has occured while executing \"&conRightClickPlayer&4\"";
            case "onworldload":
                return "An unknown error has occured while executing \"&conWorldLoad&4\"";
            case "onclienttick":
                return "An unknown error has occured while executing \"&conClientTick&4\"";
            case "async":
                CommandTrigger.commandLoad();
                return "An unknown error has occured while executing \"&casync&4\"";
            case "onsoundplay":
                return "An unknown error has occured while executing \"&conSoundPlay&4\"";
            default:
                return "An unknown error has occurred";
        }
    }
}
