package com.kerbybit.chattriggers.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.global;

import net.minecraft.client.Minecraft;

public class UpdateHandler {
	public static void getCanUse(String url1, String url2, String url3) {
		global.canUseURL1 = url1;
		global.canUseURL2 = url2;
        global.hasWatermarkURL = url3;
		Thread threadCanUse1 = new Thread(new Runnable() {
			public void run() {
				try {
					URL web = new URL(global.canUseURL1);
					InputStream fis = web.openStream();
		 			List<String> lines = new ArrayList<String>();
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
			 					if (getuuid.equals(Minecraft.getMinecraft().thePlayer.getUniqueID().toString().replace("-", ""))) {
			 						global.canUse = false;
			 						ChatHandler.warn(ChatHandler.color("red", "ChatTriggers is currently disabled!"));
			 						ChatHandler.warn(ChatHandler.color("red", "  Why am I seeing this? clickable(&7[click],open_url,http://www.kerbybit.com/enabledmods/info/,&7Open &7kerbybit.com/EnabledMods/info/)"));
			 					}
		 					} catch (Exception e) {
		 						e.printStackTrace();
		 						System.out.println("Update not loading. Is www.kerbybit.com down?");
		 					}
		 				}
		 			}
		 			
		 			
		 			
		 			web = new URL(global.canUseURL2);
					fis = web.openStream();
		 			lines = new ArrayList<String>();
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
		 						System.out.println("Could not get list! ChatTriggers may not work properly!");
		 					}
		 				}
		 			}

                    web = new URL(global.hasWatermarkURL);
                    fis = web.openStream();
                    lines = new ArrayList<String>();
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
		 			
				} catch (MalformedURLException e) {
		 			ChatHandler.warn(ChatHandler.color("red", "Can't grab update! Update services must be down"));
		 			e.printStackTrace();
		 		} catch (IOException e) {
		 			ChatHandler.warn(ChatHandler.color("red", "Can't grab update! Report this to kerbybit ASAP"));
		 			e.printStackTrace();
		 		}
			}
		});
		threadCanUse1.start();
	}
	
	public static void loadVersion(String url) {
		global.versionURL = url;
		Thread threadLoadVersion = new Thread(new Runnable() {
		     public void run() {
		    	 try {
		 			URL web = new URL(global.versionURL);
		 			InputStream fis = web.openStream();
		 			List<String> lines = new ArrayList<String>();
		 			String line;
		 			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
		 			while ((line = bufferedReader.readLine()) != null) {
		 				lines.add(line);
		 			}
		 			bufferedReader.close();
		 			
		 			if (!global.settings.get(2).equals("null")) {
		 				if (!lines.get(0).equals(global.settings.get(2))) {
		 					ChatHandler.warnBreak(0);
		 					if (global.settings.get(4).equals("false")) {
		 						ChatHandler.warn(ChatHandler.color("red", "You are running on an outdated version of ChatTriggers!"));
		 						List<String> TMP_out = new ArrayList<String>();
		 						TMP_out.add("text:'http://ChatTriggers.kerbybit.com/download',color:red,hoverEvent:{action:'show_text',value:'Click to download update'},clickEvent:{action:'open_url',value:'http://chattrigger.kerbybit.com/download'}");
		 						ChatHandler.sendJson(TMP_out);
		 						ChatHandler.warn(ChatHandler.color("red", "Current stable version: " + lines.get(0)));
		 					} else {
		 						ChatHandler.warn(ChatHandler.color("red", "You are running on an outdated version of ChatTriggers!"));
		 						List<String> TMP_out = new ArrayList<String>();
		 						TMP_out.add("text:'http://ChatTriggers.kerbybit.com/download',color:red,hoverEvent:{action:'show_text',value:'Click to download update'},clickEvent:{action:'open_url',value:'http://chattriggers.kerbybit.com/download'}");
		 						ChatHandler.sendJson(TMP_out);
		 						ChatHandler.warn(ChatHandler.color("red", "Current beta version: " + lines.get(0)));
		 					}
		 					ChatHandler.warn(ChatHandler.color("red", "Your version: " + global.settings.get(2)));
		 					ChatHandler.warn(ChatHandler.color("red", "You will only see this message once until the next update"));
		 					ChatHandler.warnBreak(1);
		 					global.settings.set(2,lines.get(0));
		 					FileHandler.saveAll();
		 				}
		 			} else {
		 				global.settings.set(2, lines.get(0));
		 				FileHandler.saveAll();
		 			}
		 		} catch (MalformedURLException e) {
		 			ChatHandler.warn(ChatHandler.color("red", "Can't grab update! Update services must be down"));
		 			e.printStackTrace();
		 		} catch (IOException e) {
		 			ChatHandler.warn(ChatHandler.color("red", "Can't grab update! Report this to kerbybit ASAP"));
		 			e.printStackTrace();
		 		}
		     }
		});
		threadLoadVersion.start();
	}
}
