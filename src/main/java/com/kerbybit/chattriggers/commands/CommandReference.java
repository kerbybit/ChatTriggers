package com.kerbybit.chattriggers.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.file.FileHandler;
import com.kerbybit.chattriggers.globalvars.global;

public class CommandReference {
	public static void resetAll() {
		ChatHandler.warn(ChatHandler.color("red", "Resetting everything in attempt to fix things"));
		global.waitEvents.clear();
		global.asyncEvents.clear();
		global.backupTMP_strings.clear();
		global.backupUSR_strings.clear();
		global.killfeed.clear();
		global.notify.clear();
		try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
		ChatHandler.warn(ChatHandler.color("green", "Reset completed"));
	}
	
	
	public static boolean isTriggerType(String TMP_type) {
		if (TMP_type.equalsIgnoreCase("CHAT") 
		|| TMP_type.equalsIgnoreCase("OTHER")
		|| TMP_type.equalsIgnoreCase("ONWORLDLOAD")
		|| TMP_type.equalsIgnoreCase("ONWORLDFIRSTLOAD")
		|| TMP_type.equalsIgnoreCase("ONSERVERCHANGE")
		|| TMP_type.equalsIgnoreCase("ONNEWDAY")
		|| TMP_type.equalsIgnoreCase("ONCLIENTTICK")
		|| TMP_type.equalsIgnoreCase("ONRIGHTCLICKPLAYER")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void clearTriggerList() {
		global.chatTrigger.clear();
		global.tickTrigger.clear();
		global.onWorldLoadTrigger.clear();
		global.onWorldFirstLoadTrigger.clear();
		global.onServerChangeTrigger.clear();
		global.onNewDayTrigger.clear();
		global.onRightClickPlayerTrigger.clear();
	}
	
	public static void addToTriggerList(List<String> tmp_list) {
		if (tmp_list.get(0).equalsIgnoreCase("CHAT")) {
			global.chatTrigger.add(tmp_list);
		} else if (tmp_list.get(0).toUpperCase().startsWith("ONCLIENTTICK")) {
			global.tickTrigger.add(tmp_list);
			if (tmp_list.get(0).equalsIgnoreCase("ONCLIENTTICK")) {
				global.tickTriggerTime.add(1);
			} else {
				try {
					int num = Integer.parseInt(tmp_list.get(0).substring(12).trim());
					global.tickTriggerTime.add(num);
				} catch (NumberFormatException e) {
					global.tickTriggerTime.add(1);
				}
			}
		} else if (tmp_list.get(0).equalsIgnoreCase("ONWORLDLOAD")) {
			global.onWorldLoadTrigger.add(tmp_list);
		} else if (tmp_list.get(0).equalsIgnoreCase("ONWORLDFIRSTLOAD")) {
			global.onWorldFirstLoadTrigger.add(tmp_list);
		} else if (tmp_list.get(0).equalsIgnoreCase("ONSERVERCHANGE")) {
			global.onServerChangeTrigger.add(tmp_list);
		} else if (tmp_list.get(0).equalsIgnoreCase("ONNEWDAY")) {
			global.onNewDayTrigger.add(tmp_list);
		} else if (tmp_list.get(0).equalsIgnoreCase("ONRIGHTCLICKPLAYER")) {
			global.onRightClickPlayerTrigger.add(tmp_list);
		}
	}
	
	public static Boolean isEventType(String TMP_etype) {
		if (TMP_etype.equalsIgnoreCase("CHAT")
		|| TMP_etype.equalsIgnoreCase("CANCEL") 
		|| TMP_etype.equalsIgnoreCase("CHOOSE") 
		|| TMP_etype.equalsIgnoreCase("KILLFEED")
		|| TMP_etype.equalsIgnoreCase("NOTIFY")
		|| TMP_etype.equalsIgnoreCase("TRIGGER") 
		|| TMP_etype.equalsIgnoreCase("SOUND")
		|| TMP_etype.equalsIgnoreCase("COPY")
		|| TMP_etype.equalsIgnoreCase("URL")
		|| TMP_etype.equalsIgnoreCase("DO")
		|| TMP_etype.equalsIgnoreCase("IF")
		|| TMP_etype.equalsIgnoreCase("ELSE")
		|| TMP_etype.equalsIgnoreCase("ELSEIF")
		|| TMP_etype.equalsIgnoreCase("FOR")
		|| TMP_etype.equalsIgnoreCase("WAIT")
		|| TMP_etype.equalsIgnoreCase("END")
		|| TMP_etype.equalsIgnoreCase("ASYNC")) {
			return true;
		} else {
			return false;
		}
	}
}
