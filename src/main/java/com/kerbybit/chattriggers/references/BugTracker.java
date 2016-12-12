package com.kerbybit.chattriggers.references;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.global;
import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class BugTracker {
    public static void send() {
        if (global.bugReport.size() > 0) {
            Thread threadSubmitBugReport = new Thread(new Runnable() {
                public void run() {
                    try {
                        ChatHandler.warn("&7Sending bug report...");
                        String bug = "";
                        for (String b : global.bugReport) {
                            bug += b + "\n";
                        }
                        URL url = new URL("http://ct.kerbybit.com/bugreport/");
                        Map<String,Object> params = new LinkedHashMap<String,Object>();
                        params.put("name", Minecraft.getMinecraft().thePlayer.getDisplayNameString());
                        params.put("uuid", Minecraft.getMinecraft().thePlayer.getUniqueID());
                        params.put("bug", bug);

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
                        ChatHandler.warn(global.settings.get(0) + "Bug report submitted successfully!");
                        global.bugReport.clear();
                    } catch (Exception e) {
                        ChatHandler.warn("&4An error occured while submitting a bug report!");
                        ChatHandler.warn("&4Is ct.kerbybit.com down?");
                    }

                }
            });
            threadSubmitBugReport.start();
        }
    }

    public static void show(Exception e) {
        show(e, "none");
    }

    public static void show(Exception e, String type) {
        for (StackTraceElement stack : e.getStackTrace()) {
            global.bugReport.add(stack.toString());
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
