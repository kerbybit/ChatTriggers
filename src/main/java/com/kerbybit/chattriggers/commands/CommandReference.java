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
        global.notifySize = 0;
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

        List<String> check = new ArrayList<>(getTriggerTypes());

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
        List<String> r = new ArrayList<>();

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
            r.add("onSoundPlay");
            r.add("");
            r.add("function");
            r.add("onUnknownError");

        return r;
    }
	
	public static void clearTriggerList() {
		global.chatTrigger.clear();
        global.onChatTrigger.clear();
        global.actionTrigger.clear();
		global.tickTrigger.clear();
		global.tickTriggerTime.clear();
		global.onWorldLoadTrigger.clear();
		global.onWorldFirstLoadTrigger.clear();
		global.onServerChangeTrigger.clear();
		global.onNewDayTrigger.clear();
		global.onRightClickPlayerTrigger.clear();
		global.onSoundPlayTrigger.clear();
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
		} else if (tmp_list.get(0).equalsIgnoreCase("ONSOUNDPLAY")) {
            global.onSoundPlayTrigger.add(tmp_list);
        } else if (tmp_list.get(0).equalsIgnoreCase("FUNCTION")) {
            global.function.add(tmp_list);
        } else if (tmp_list.get(0).equalsIgnoreCase("ONUNKNOWNERROR")) {
            global.onUnknownError.add(tmp_list);
        }
	}
	
	static Boolean isEventType(String TMP_etype) {
        List<String> check = new ArrayList<>(getAllEventTypes());
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
        List<String> r = new ArrayList<>();

            r.add("chat");
            r.add("actionbar");
            r.add("cancel");
            r.add("killfeed");
            r.add("notify");
            r.add("sound");
            r.add("title");
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
            r.add("break");
            r.add("return");
            r.add("");
            r.add("debug");
            r.add("log");
            r.add("simulate");
            r.add("enableimport");
            r.add("disableimport");

        return r;
    }

    public static List<String> getAllEventTypes() {
        List<String> r = new ArrayList<>(getEventTypes());

        r.add("say");
        r.add("command");

        return r;
    }

    static List<String> getStrings() {
        List<String> r = new ArrayList<>();

            r.add("{msg} {msg}.meta() {msg($number)}");
            r.add("{imported($import)}");
            r.add("{br}");
            r.add("{setcol}");
            r.add("{debug}");
            r.add("{chatWidth}");
            r.add("");
            r.add("{me} {uuid}");
            r.add("{hp} {hunger} {saturation}");
            r.add("{xpLevel} {xpProgress}");
            r.add("{inChat} {inTab} {inAltScreen}");
            r.add("{renderDistance} {fov}");
            r.add("{sneak} {sprint}");
            r.add("{x} {y} {z}");
            r.add("{exacX} {exacY} {exacZ}");
            r.add("{chunkX} {chunkY} {chunkZ}");
            r.add("{motionX} {motionY} motionZ}");
            r.add("{facing} {facingMin}");
            r.add("{biome} {worldTime}");
            r.add("{fps} {fpscol}");
            r.add("{lightLevel} {potionEffects}");
            r.add("{lookingAt} {lookingAtMin}");
            r.add("{armor} {armorPoints}");
            r.add("{heldItem} {arrows} {hotbar($number)}");
            r.add("{pitch} {yaw}");
            r.add("{cps} {cpsAve} {cpsMax}");
            r.add("{rcps} {rcpsAve} {rcpsMax}");
            r.add("");
            r.add("{server} {serverIP} {serverMOTD} {serverVersion}");
            r.add("{ping} {playerList}");
            r.add("{scoreboardTitle} {scoreboardLines}");
            r.add("");
            r.add("{actionbarText} {bossbarText}");
            r.add("{titleText} {subtitleText}");
            r.add("");
            r.add("{random($low,$high)} {random($high)}");
            r.add("{time} {date} {unixTime}");
            r.add("{year} {month} {day}");
            r.add("{hour} {hour12} {minute} {second}");
            r.add("{am/pm} {timezone}");
            r.add("{CTVersion} {MCVersion}");
            r.add("");
            r.add("{black} {darkBlue} {darkGreen} {darkAqua} {darkRed} {darkPurple} {gold} {gray} {darkGray} {blue} {green} {aqua} {red} {lightPurple} {yellow} {white}");
            r.add("{obfuscated} {bold} {strikethrough} {underline} {italic} {reset}");
            r.add("{randcol}");

        return r;
    }

    static List<String> getStringFunctions() {
        List<String> r = new ArrayList<>();

            r.add(".set($value) .set(~)");
            r.add(".save($value) .save(~)");
            r.add("");
            r.add(".add($number) .plus($number) .+($number)");
            r.add(".subtract($number) .minus($number) .-($number)");
            r.add(".multiply($number) .times($number) .*($number)");
            r.add(".divide($number) ./($number)");
            r.add(".divideGetPercentage($number) .divPercent($number) ./%($number)");
            r.add(".power($number) .pow($number) .^($number)");
            r.add(".modulus($number) .mod($number) .%($number)");
            r.add(".absolute() .abs()");
            r.add(".round() .round($number)");
            r.add(".floor() .ceil()");
            r.add(".sin() .cos() .tan()");
            r.add(".asin() .acos() .atan() .atan2($number)");
            r.add(".sqrt()");
            r.add("");
            r.add(".greaterThan($number) .gt($number) .>($number)");
            r.add(".greaterThanOrEqualTo($number) .gte($number) .>=($number)");
            r.add(".lessThan($number) .lt($number) .<($number)");
            r.add(".lessThanOrEqualTo($number) .lte($number) .<=($number)");
            r.add("");
            r.add(".equals($value) .=($value) .==($value) .equalsIgnoreCase($value)");
            r.add(".startsWith($value) .startsWithIgnoreCase($value)");
            r.add(".contains($value) .containsIgnoreCase($value)");
            r.add(".endsWith($value) .endsWithIgnoreCase($value)");
            r.add(".hasRegex($regex) .matchesRegex($regex)");
            r.add("");
            r.add(".replace($value1,$value2) .replace($value)");
            r.add(".replaceIgnoreCase($value1,$value2)");
            r.add(".substring($v1,$v2) .substring($n1,$n2)");
            r.add(".trim()");
            r.add(".prefix($value) .suffix($value)");
            r.add(".toUpper() .toUpperCase()");
            r.add(".toLower() .toLowerCase()");
            r.add(".removeFormatting() .remForm()");
            r.add(".ignoreFormatting() .ignoreForm()");
            r.add(".capitalizeFirstWord() .capFirst()");
            r.add(".capitalizeAllWords() .capAll()");
            r.add(".ignoreEscape()");
            r.add(".fixLinks()");
            r.add("");
            r.add(".length()");
            r.add(".size()");
            r.add(".split($value)");

        return r;
    }

    static List<String> getArrayFunctions() {
        List<String> r = new ArrayList<>();

            r.add(".clear()");
            r.add(".add($v)");
            r.add(".remove($v) .remove($n)");
            r.add(".get($v) .get($n)");
            r.add(".size()");
            r.add("");
            r.add(".setSplit($v,$split)");
            r.add(".importJsonFile($file,$node)");
            r.add(".importJsonURL($URL,$node)");
            r.add(".exportJson($file,$node)");

        return r;
    }

    static List<String> getListFunctions() {
        List<String> r = new ArrayList<>();

            r.add(".load($v) .load($file) .load($URL)");
            r.add(".export($file)");
            r.add(".size()");
            r.add(".add($v)");
            r.add(".get($v) .get($n)");
            r.add(".has($v)");
            r.add(".getRandom()");
            r.add(".remove($n) .remove($v)");
            r.add(".sort()");
            r.add(".reverse()");
            r.add(".clear()");

        return r;
    }

    static List<String> getJsonFunctions() {
        List<String> r = new ArrayList<>();

            r.add(".clear()");
            r.add(".load($v) .load($file) .load($URL)");
            r.add(".get($v)");
            r.add(".getKeys($v)");
            r.add(".getValues($v)");

        return r;
    }

    static List<String> getDisplayFunctions() {
        List<String> r = new ArrayList<>();

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
            if (global.rclicks.size() > 0) {
                for (int i=0; i<global.rclicks.size(); i++) {
                    global.rclicks.set(i, global.rclicks.get(i)-1);
                    if (global.rclicks.get(i) == 0) {
                        global.rclicks.remove(i);
                    }
                }
            }

            global.clicks_ave.add((double) global.clicks.size());
            global.rclicks_ave.add((double) global.rclicks.size());

            if (global.clicks_ave.size() > 100) {
                global.clicks_ave.remove(0);
            }
            if (global.rclicks_ave.size() > 100) {
                global.rclicks_ave.remove(0);
            }
            if (global.clicks_ave.size() > 0) {
                if (global.clicks_ave.get(global.clicks_ave.size() - 1) == 0) {
                    global.clicks_ave.clear();
                    global.clicks_max = 0;
                }
            }
            if (global.rclicks_ave.size() > 0) {
                if (global.rclicks_ave.get(global.rclicks_ave.size() -1 ) == 0) {
                    global.rclicks_ave.clear();
                    global.rclicks_max = 0;
                }
            }
        }
        if (global.clicks.size() > global.clicks_max) {
            global.clicks_max = global.clicks.size();
        }
        if (global.rclicks.size() > global.rclicks_max) {
            global.rclicks_max = global.rclicks.size();
        }
    }
}
