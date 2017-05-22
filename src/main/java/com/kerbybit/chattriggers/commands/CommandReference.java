package com.kerbybit.chattriggers.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.file.FileHandler;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.objects.DisplayHandler;
import com.kerbybit.chattriggers.references.AsyncHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class CommandReference {
    static void clearAll() {
        global.waitEvents.clear();
        global.waitTime.clear();
        AsyncHandler.clearAsyncs();
        global.backupTMP_strings.clear();
        global.backupUSR_strings.clear();
        global.killfeed.clear();
        global.killfeedDelay.clear();
        global.notify.clear();
        global.notifyAnimation.clear();
        DisplayHandler.clearDisplays();
    }

	static void resetAll() {
		ChatHandler.warn(ChatHandler.color("red", "Resetting everything in attempt to fix things"));
		CommandTrigger.commandLoad();
		ChatHandler.warn(ChatHandler.color("green", "Reset completed"));
	}

    static void silentResetAll() {
        clearAll();
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
            r.add("");
            r.add("function");
            r.add("onUnknownError");

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
        global.function.clear();
        global.onUnknownError.clear();
	}
	
	public static void addToTriggerList(List<String> tmp_list) {
		if (tmp_list.get(0).equalsIgnoreCase("CHAT")) {
			global.chatTrigger.add(tmp_list);
		} else if (tmp_list.get(0).equalsIgnoreCase("ACTIONBAR")) {
		    global.actionTrigger.add(tmp_list);
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
		} else if (tmp_list.get(0).equalsIgnoreCase("FUNCTION")) {
            global.function.add(tmp_list);
        } else if (tmp_list.get(0).equalsIgnoreCase("ONUNKNOWNERROR")) {
            global.onUnknownError.add(tmp_list);
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
            r.add("actionbar");
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
            r.add("return");
            r.add("");
            r.add("debug");
            r.add("log");
            r.add("simulate");

        return r;
    }

    public static List<String> getAllEventTypes() {
        List<String> r = new ArrayList<String>();

        r.add("chat");
        r.add("cancel");
        r.add("killfeed");
        r.add("notify");
        r.add("sound");
        r.add("trigger");
        r.add("copy");
        r.add("url");
        r.add("do");
        r.add("if");
        r.add("else");
        r.add("elseif");
        r.add("for");
        r.add("wait");
        r.add("choose");
        r.add("async");
        r.add("end");
        r.add("return");
        r.add("debug");
        r.add("log");
        r.add("simulate");
        r.add("say");
        r.add("command");

        return r;
    }

    static List<String> getStrings() {
        List<String> r = new ArrayList<String>();

            r.add("{msg} {msg[$number]}");
            r.add("{br}");
            r.add("{trigsize}");
            r.add("{notifysize}");
            r.add("{setcol}");
            r.add("{debug}");
            r.add("{chatwidth}");
            r.add("");
            r.add("{me}");
            r.add("{hp}");
            r.add("{sneak}");
            r.add("{x} {y} {z}");
            r.add("{facing}");
            r.add("{fps} {fpscol}");
            r.add("{potionEffects}");
            r.add("{armor}");
            r.add("{heldItem}");
            r.add("{arrows}");
            r.add("{cps} {cpsAve} {cpsMax}");
            r.add("");
            r.add("{server}");
            r.add("{serverIP}");
            r.add("{serverMOTD}");
            r.add("{serverversion}");
            r.add("{ping}");
            r.add("{scoreboardtitle}");
            r.add("");
            r.add("{random($low,$high)} {random($high)}");
            r.add("{time} {date} {unixtime}");

        return r;
    }

    static List<String> getStringFunctions() {
        List<String> r = new ArrayList<String>();

            r.add(".set($value) .set(~)");
            r.add(".save($value) .save(~)");
            r.add("");
            r.add(".add($number) .plus($number) .+($number)");
            r.add(".subtract($number) .minus($number) .-($number)");
            r.add(".multiply($number) .mult($number) .*($number)");
            r.add(".divide($number) .div($number) ./($number)");
            r.add(".divideGetPercentage($number) .divPercent($number) ./%($number)");
            r.add(".power($number) .pow($number) .^($number)");
            r.add(".modulus($number) .mod($number) .%($number)");
            r.add(".absolute() .abs()");
            r.add("");
            r.add(".greaterThan($number) .>($number)");
            r.add(".greaterThanOrEqualTo($number) .>=($number)");
            r.add(".lessThan($number) .<($number)");
            r.add(".lessThanOrEqualTo($number) .<=($number)");
            r.add("");
            r.add(".equals($value) .=($value) .equalsIgnoreCase($value)");
            r.add(".startsWith($value) .startsWithIgnoreCase($value)");
            r.add(".contains($value) .containsIgnoreCase($value)");
            r.add(".endsWith($value) .endsWithIgnoreCase($value)");
            r.add("");
            r.add(".replace($value1,$value2) .replace($value)");
            r.add(".substring($v1,$v2)");
            r.add(".trim()");
            r.add(".prefix($value) .suffix($value)");
            r.add(".toUpper() .toUpperCase()");
            r.add(".toLower() .toLowerCase()");
            r.add(".removeFormatting() .remForm()");
            r.add(".capitalizeFirstWord() .capFirst()");
            r.add(".capitalizeAllWords() .capAll()");
            r.add(".ignoreEscape()");
            r.add("");
            r.add(".importJsonFile($file,$node)");
            r.add(".importJsonURL($URL,$node)");
            r.add("");
            r.add(".length()");
            r.add(".size()");
            r.add(".split($value)");

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

    static List<String> getListFunctions() {
        List<String> r = new ArrayList<String>();

            r.add(".load($v) .load($file) .load($URL)");
            r.add(".export($file)");
            r.add(".size()");
            r.add(".add($v)");
            r.add(".get($v) .get($n)");
            r.add(".has($v)");
            r.add(".getRandom()");
            r.add(".remove($n) .remove($v)");
            r.add(".clear()");

        return r;
    }

    static List<String> getJsonFunctions() {
        List<String> r = new ArrayList<String>();

            r.add(".clear()");
            r.add(".load($v) .load($file) .load($URL)");
            r.add(".get($v)");
            r.add(".getKeys($v)");
            r.add(".getValues($v)");

        return r;
    }

    static List<String> getDisplayFunctions() {
        List<String> r = new ArrayList<String>();

            r.add(".add($v)");
            r.add(".clear()");
            r.add(".getX() .getY()");
            r.add(".setX($v) .setY($v)");
            r.add(".settings($v)");
            r.add(".update()");

        return r;
    }

    public static String getPing() {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player == null) {
            return "" + Minecraft.getMinecraft().getCurrentServerData().pingToServer;
        }
        if(Minecraft.getMinecraft().getNetHandler().getPlayerInfo(UUID.fromString(Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString())) != null) {
            return "" + Minecraft.getMinecraft().getNetHandler().getPlayerInfo(UUID.fromString(Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString())).getResponseTime();
        }
        return "" + Minecraft.getMinecraft().getCurrentServerData().pingToServer;
    }

    private static Long sysTime = Minecraft.getSystemTime();
    private static Long secSysTime = Minecraft.getSystemTime();
    //run on render
    public static void clickCalc() {

        while (Minecraft.getSystemTime() > secSysTime + 50L) {
            secSysTime += 50L;

            if (global.clicks.size() > 0) {
                for (int i=0; i<global.clicks.size(); i++) {
                    global.clicks.set(i, global.clicks.get(i)-1);
                    if (global.clicks.get(i) == 0) {
                        global.clicks.remove(i);
                    }
                }
            }

            global.clicks_ave.add((double) global.clicks.size());
            global.rclicks_ave.add(global.rclicks);
            if (global.clicks.size() > global.clicks_max) {
                global.clicks_max = global.clicks.size();
            }
            if (global.clicks_ave.size() > 100) {
                global.clicks_ave.remove(0);
            }
            if (global.clicks_ave.size() > 0) {
                if (global.clicks_ave.get(global.clicks_ave.size() - 1) == 0) {
                    global.clicks_ave.clear();
                    global.clicks_max = 0;
                }
            }
        }

        while (Minecraft.getSystemTime() > sysTime + 1000L) {
            sysTime += 1000L;

            global.rclicks = 0.0;

            if (global.rclicks_ave.size() > 10) {
                global.rclicks_ave.remove(0);
            }
            if (global.rclicks > global.rclicks_max) {
                global.rclicks_max = global.rclicks;
            }
        }
    }
}
