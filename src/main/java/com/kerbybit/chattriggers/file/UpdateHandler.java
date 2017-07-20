package com.kerbybit.chattriggers.file;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.commands.CommandReference;
import com.kerbybit.chattriggers.commands.CommandTrigger;
import com.kerbybit.chattriggers.globalvars.Settings;
import com.kerbybit.chattriggers.globalvars.global;

import net.minecraft.client.Minecraft;

public class UpdateHandler {
	static void getCanUse(String url1, String url2, String url3) {
		global.canUseURL1 = url1;
		global.canUseURL2 = url2;
        global.hasWatermarkURL = url3;
		Thread threadCanUse1 = new Thread(() -> {
            try {
                URL web = new URL(global.canUseURL1);
                InputStream fis = web.openStream();
                List<String> lines = new ArrayList<>();
                String line;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
                while ((line = bufferedReader.readLine()) != null) {
                    lines.add(line);
                }
                bufferedReader.close();

                for (String getline : lines) {
                    String getthis = "\"uuid\":\"";
                    if (getline.contains(getthis)) {
                        String getuuid = getline.substring(getline.indexOf(getthis) + getthis.length(), getline.indexOf("\"", getline.indexOf(getthis) + getthis.length()));
                        try {
                            if (getuuid.equals(Minecraft.getMinecraft().thePlayer.getUniqueID().toString().replace("-", "")))
                                global.canUse = false;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }



                web = new URL(global.canUseURL2);
                fis = web.openStream();
                lines = new ArrayList<>();
                bufferedReader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
                while ((line = bufferedReader.readLine()) != null) {
                    lines.add(line);
                }
                bufferedReader.close();

                for (String getline : lines) {
                    String getthis = "\"ChatTriggers\":\"";
                    if (getline.contains(getthis)) {
                        String getenabled = getline.substring(getline.indexOf(getthis) + getthis.length(), getline.indexOf("\"", getline.indexOf(getthis) + getthis.length()));
                        try {
                            if (getenabled.equals("false")) {
                                global.canUse = false;
                                ChatHandler.warn(ChatHandler.color("red", "ChatTriggers is currently disabled!"));
                                ChatHandler.warn(ChatHandler.color("red", "  Why am I seeing this? clickable(&7[click],open_url,http://www.kerbybit.com/enabledmods/info/,&7Open &7kerbybit.com/EnabledMods/info/)"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                web = new URL(global.hasWatermarkURL+"?send="+Minecraft.getMinecraft().thePlayer.getUniqueID());
                fis = web.openStream();
                lines = new ArrayList<>();
                bufferedReader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
                while ((line = bufferedReader.readLine()) != null) {
                    lines.add(line);
                }
                bufferedReader.close();

                String myuuid = Minecraft.getMinecraft().thePlayer.getUniqueID().toString().replace("-","");

                for (String getline : lines) {
                    if (myuuid.equals(getline.trim())) {
                        global.hasWatermark = false;
                    }
                }

                System.out.println("Done getting watermark  " + global.illegalChat);
                if (global.hasWatermark && global.illegalChat)
                    CommandReference.showIllegalChatWarning(global.illegalChatEvent);

            } catch (MalformedURLException e) {
                ChatHandler.warn(ChatHandler.color("red", "Can't grab update! Update services must be down"));
                e.printStackTrace();
            } catch (IOException e) {
                ChatHandler.warn(ChatHandler.color("red", "Can't grab update! Report this to kerbybit ASAP"));
                e.printStackTrace();
            }
		});
		threadCanUse1.start();
	}

	private static String get;
	public static void loadVersion(String url) {
	    get = url;
		Thread threadLoadVersion = new Thread(() -> {
            try {
                URL web = new URL(get);
                String myVersion = null;

                InputStream fis = web.openStream();
                List<String> lines = new ArrayList<>();
                String line;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
                while ((line = bufferedReader.readLine()) != null) {
                    lines.add(line);
                }
                bufferedReader.close();

                File folder = new File("./mods/");
                try {
                    for (File file : folder.listFiles()) {
                        if (!file.isDirectory()) { // get file
                            if (file.getName().startsWith("ChatTriggers_")
                                    && file.getName().endsWith(".jar")) { // check name
                                myVersion = file.getName().substring(13); // get version number in name
                                myVersion = myVersion.substring(myVersion.indexOf("_") + 1);
                                break;
                            }
                        } else {
                            String mcVersion = Minecraft.getMinecraft().getVersion(); // get mc version
                            if (mcVersion.contains("-")) // fix mc version
                                mcVersion = mcVersion.substring(0, mcVersion.indexOf("-"));
                            if (file.getName().equals(mcVersion)) { // check if mc version matches folder
                                for (File subfile : file.listFiles()) {
                                    if (subfile.getName().startsWith("ChatTriggers_")
                                            && subfile.getName().endsWith(".jar")) { // check name
                                        myVersion = subfile.getName().substring(13); // get version number in name
                                        myVersion = myVersion.substring(myVersion.indexOf("_") + 1);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    if (!myVersion.equals(lines.get(0) + ".jar")) { // your version doesnt match version found online
                        ChatHandler.warnBreak(0);
                        if (Settings.isBeta) {
                            ChatHandler.warn(ChatHandler.color("red", "You are running on an outdated version of ChatTriggers!"));
                            ChatHandler.warn("clickable(&cct.kerbybit.com/download/#beta,open_url,http://ct.kerbybit.com/download/#beta,Open download page)");
                            ChatHandler.warn(ChatHandler.color("red", "Current beta version: " + lines.get(0)));
                        } else {
                            ChatHandler.warn(ChatHandler.color("red", "You are running on an outdated version of ChatTriggers!"));
                            ChatHandler.warn("clickable(&cct.kerbybit.com/download,open_url,http://ct.kerbybit.com/download,Open download page)");
                            ChatHandler.warn(ChatHandler.color("red", "Current stable version: " + lines.get(0)));
                        }
                        ChatHandler.warnBreak(1);
                    }

                    Settings.version = myVersion.replace(".jar", "");
                } catch (Exception e) {
                    ChatHandler.warn("red", "Unable to finish checking for an update for ChatTriggers. Did you change the name of the jar in your mods folder?");
                }
            } catch (IOException e) {
                ChatHandler.warn(ChatHandler.color("red", "Can't grab update! is ct.kerbybit.com down?"));
                e.printStackTrace();
            }

            StringBuilder updatedImports = new StringBuilder("import ");
            for (Map.Entry<String, String> importMap : global.imported.entrySet()) {
                String importName = importMap.getKey();
                String importVersion = importMap.getValue();
                try {
                    URL web = new URL("http://ct.kerbybit.com/exports/meta/"+importName.replace(".txt",".json"));
                    InputStream fis = web.openStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
                    String line;
                    String check = "\"packVersion\":\"";



                    while ((line = bufferedReader.readLine()) != null) {
                        if (line.contains(check)) {
                            String currentVersion = line.substring(line.indexOf(check)+check.length(), line.length()-2);
                            if (!currentVersion.equals(importVersion)) {
                                updatedImports.append(importName.replace(".txt","")).append(" ");
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Did not find import " + importName.replace(".txt","") + " online. skipping update.");
                }
            }
            if (!updatedImports.toString().equals("import ")) {
                ChatHandler.warn(ChatHandler.color(Settings.col[0], "Found updates for the following imports:"));
                ChatHandler.warn("  " + updatedImports.toString().trim().replace("import ", "").replace(" ", ", "));
                CommandTrigger.doCommand(updatedImports.toString().trim().split(" "), false);
            }
		});
		threadLoadVersion.start();
	}
}
