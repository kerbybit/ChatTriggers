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
		global.killfeedDelay.clear();
		global.notify.clear();
		global.notifyAnimation.clear();
		try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
		ChatHandler.warn(ChatHandler.color("green", "Reset completed"));
	}

    public static void silentResetAll() {
        global.waitEvents.clear();
        global.asyncEvents.clear();
        global.backupTMP_strings.clear();
        global.backupUSR_strings.clear();
        global.killfeed.clear();
        global.killfeedDelay.clear();
        global.notify.clear();
        global.notifyAnimation.clear();
        try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
    }
	
	
	static boolean isTriggerType(String TMP_type) {

        List<String> check = new ArrayList<String>(getTriggerTypes());

        for (String value : check) {
            if (!value.equals("")) {
                if (TMP_type.equalsIgnoreCase(value)) {
                    return true;
                }
            }
        }

        return false;
	}

    static List<String> getTriggerTypes() {
        List<String> r = new ArrayList<String>();

            r.add("chat");
            r.add("onChat");
            r.add("other");
            r.add("");
            r.add("onWorldLoad");
            r.add("onWorldFirstLoad");
            r.add("onServerChange");
            r.add("onNewDay");
            r.add("");
            r.add("onClientTick");
            r.add("onRightClickPlayer");

        return r;
    }
	
	public static void clearTriggerList() {
		global.chatTrigger.clear();
        global.onChatTrigger.clear();
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
		} else if (tmp_list.get(0).equalsIgnoreCase("ONCHAT")) {
            global.onChatTrigger.add(tmp_list);
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
	
	static Boolean isEventType(String TMP_etype) {
        List<String> check = new ArrayList<String>(getEventTypes());
        for (String value : check) {
            if (!value.equals("")) {
                if (TMP_etype.equalsIgnoreCase(value)) {
                    return true;
                }
            }

        }
        return false;
	}

    static List<String> getEventTypes() {
        List<String> r = new ArrayList<String>();

            r.add("chat");
            r.add("cancel");
            r.add("killfeed");
            r.add("notify");
            r.add("sound");
            r.add("");
            r.add("trigger");
            r.add("copy");
            r.add("url");
            r.add("do");
            r.add("");
            r.add("if");
            r.add("else");
            r.add("elseif");
            r.add("for");
            r.add("wait");
            r.add("choose");
            r.add("async");
            r.add("end");
            r.add("");
            r.add("debug");
            r.add("log");
            r.add("simulate");

        return r;
    }

    static List<String> getStrings() {
        List<String> r = new ArrayList<String>();

            r.add("{msg} {msg[$n]}");
            r.add("{br}");
            r.add("{trigsize}");
            r.add("{notifysize}");
            r.add("{setcol}");
            r.add("{debug}");
            r.add("");
            r.add("{me}");
            r.add("{hp}");
            r.add("{sneak}");
            r.add("{x} {y} {z}");
            r.add("");
            r.add("{server}");
            r.add("{serverIP}");
            r.add("{serverMOTD}");
            r.add("{serverversion}");
            r.add("{ping}");
            r.add("{scoreboardtitle}");

        return r;
    }

    static List<String> getStringFunctions() {
        List<String> r = new ArrayList<String>();

            r.add(".set($v) .set(~)");
            r.add(".save($v) .save(~)");
            r.add("");
            r.add(".add($n) .plus($n) .+($n)");
            r.add(".subtract($n) .minus($n) .-($n)");
            r.add(".multiply($n) .mult($n) .*($n)");
            r.add(".divide($n) .div($n) ./($n)");
            r.add(".divideGetPercentage($n) .divPercent($n) ./%($n)");
            r.add(".power($n) .pow($n) .^($n)");
            r.add(".modulus($n) .mod($n) .%($n)");
            r.add(".absolute($n) .abs($n)");
            r.add("");
            r.add(".greaterThan($n) .>($n)");
            r.add(".greaterThanOrEqualTo($n) .>=($n)");
            r.add(".lessThan($n) .<($n)");
            r.add(".lessThanOrEqualTo($n) .<=($n)");
            r.add("");
            r.add(".equals($v) .=($v) .equalsIgnoreCase($v)");
            r.add(".startsWith($v) .startsWithIgnoreCase($v)");
            r.add(".contains($v) .containsIgnoreCase($v)");
            r.add("endsWith($v) .endsWithIgnoreCase($v)");
            r.add("");
            r.add(".replace($v1,$v2) .replace($v)");
            r.add(".substring($v1,$v2)");
            r.add(".trim()");
            r.add(".prefix($v) .suffix($v)");
            r.add(".toUpper($v) .toUpperCase($v)");
            r.add(".toLower($v) .toLowerCase($v)");
            r.add(".removeFormatting() .remForm()");
            r.add(".capitalizeFirstWord($v) .capFirst($v)");
            r.add(".capitalizeAllWords($v) .capAll($v)");
            r.add(".ignoreEscape()");
            r.add("");
            r.add(".importJsonFile($file,$node)");
            r.add(".importJsonURL($URL,$node)");
            r.add("");
            r.add(".length()");
            r.add(".size()");

        return r;
    }

    static List<String> getArrayFunctions() {
        List<String> r = new ArrayList<String>();

            r.add(".clear()");
            r.add(".add($v)");
            r.add(".remove($v) .remove($n)");
            r.add(".get($v) .get($n)");
            r.add(".size()");
            r.add("");
            r.add(".setSplit($v,$split)");
            r.add(".importJsonFile($file,$node)");
            r.add(".improtJsonURL($URL,$node)");
            r.add(".exportJson($file,$node)");

        return r;
    }
}
