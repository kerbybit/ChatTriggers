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
	public static void getCanUse(String url) {
		global.canUseURL = url;
		Thread threadCanUse = new Thread(new Runnable() {
			public void run() {
				try {
					URL web = new URL(global.canUseURL);
					InputStream fis = web.openStream();
		 			List<String> lines = new ArrayList<String>();
		 			String line = null;
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
			 					}
		 					} catch (Exception e) {
		 						e.printStackTrace();
		 						System.out.println("Could not get list! trying again");
		 						getCanUse(global.canUseURL);
		 					}
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
		threadCanUse.start();
	}
	
	public static void loadVersion(String url) {
		global.versionURL = url;
		Thread threadLoadVersion = new Thread(new Runnable() {
		     public void run() {
		    	 try {
		 			URL web = new URL(global.versionURL);
		 			InputStream fis = web.openStream();
		 			List<String> lines = new ArrayList<String>();
		 			String line = null;
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
