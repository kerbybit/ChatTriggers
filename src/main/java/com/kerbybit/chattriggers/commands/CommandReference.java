package com.kerbybit.chattriggers.commands;

public class CommandReference {
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
