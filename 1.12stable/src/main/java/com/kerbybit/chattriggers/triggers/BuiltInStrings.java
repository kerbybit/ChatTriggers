package com.kerbybit.chattriggers.triggers;

import com.google.common.collect.Ordering;
import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.commands.CommandReference;
import com.kerbybit.chattriggers.globalvars.Settings;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.gui.IconHandler;
import com.kerbybit.chattriggers.objects.ListHandler;
import com.kerbybit.chattriggers.objects.JsonHandler;
import com.kerbybit.chattriggers.references.Reference;
import com.kerbybit.chattriggers.util.RomanNumber;
import com.kerbybit.chattriggers.util.ScoreboardReader;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Math.floor;
import static java.lang.StrictMath.round;

public class BuiltInStrings {
    private static Minecraft mc = Minecraft.getMinecraft();
    private static final Ordering<NetworkPlayerInfo> tab = Ordering.from(new CommandReference.PlayerComparator());
    
    public static String builtInStrings(String TMP_e, ClientChatReceivedEvent chatEvent, Boolean isAsync) {
        while (TMP_e.contains("{imported(") && TMP_e.contains(")}")) {
            String temporary;
            String imp = TMP_e.substring(TMP_e.indexOf("{imported(")+10, TMP_e.indexOf(")}", TMP_e.indexOf("{imported(")));
            Boolean isImported = false;

            File dir = new File("./mods/ChatTriggers/Imports/");
            if (!dir.exists()) {
                if (!dir.mkdir()) {
                    ChatHandler.warn(ChatHandler.color("red", "Unable to create file!"));}
            }
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        if (file.getName().equals(imp+".txt")) {
                            isImported = true;
                        }
                    }
                }
            }

            if (isImported) {
                temporary = "true";
            } else {
                temporary = "false";
            }

            global.TMP_string.put("DefaultString->IMPORTED"+imp+"-"+(global.TMP_string.size()+1), temporary);
            global.backupTMP_strings.put("DefaultString->IMPORTED"+imp+"-"+global.TMP_string.size(), temporary);

            TMP_e = TMP_e.replace("{imported("+imp+")}", "{string[DefaultString->IMPORTED"+imp+"-"+global.TMP_string.size()+"]}");
        }
        while (TMP_e.contains("{random(") && TMP_e.contains(")}")) {
            String temporary;
            String temp_lowhigh = TMP_e.substring(TMP_e.indexOf("{random(")+8, TMP_e.indexOf(")}", TMP_e.indexOf("{random(")));
            String lowhigh = StringFunctions.nestedArgs(temp_lowhigh, chatEvent, isAsync);

            try {
                int low = 0;
                int high;
                if (lowhigh.contains(",")) {
                    String[] tmp_lowhigh = lowhigh.split(",");
                    String strlow = tmp_lowhigh[0].trim();
                    String strhigh = tmp_lowhigh[1].trim();
                    low = Integer.parseInt(strlow);
                    high = Integer.parseInt(strhigh);
                } else {
                    high = Integer.parseInt(lowhigh);
                }

                temporary = EventsHandler.randInt(low,high) + "";
            } catch (NumberFormatException e) {
                temporary = "Not a number!";
            }

            String stringName = "DefaultString->RANDOM"+lowhigh+"-"+(global.TMP_string.size()+1);
            global.TMP_string.put(stringName, temporary);
            global.backupTMP_strings.put(stringName, temporary);

            TMP_e = TMP_e.replace("{random("+temp_lowhigh+")}", "{string["+stringName+"]}");
        }
        while (TMP_e.contains("{msg(") && TMP_e.contains(")}")) {
            String strnum = TMP_e.substring(TMP_e.indexOf("{msg(")+5, TMP_e.indexOf(")}", TMP_e.indexOf("{msg(")));
            String temporary;
            String get_number = StringFunctions.nestedArgs(strnum, chatEvent, isAsync);

            try {
                int num = Integer.parseInt(get_number);
                if (num>=0) {
                    if (num<global.chatHistory.size()) {temporary = ChatHandler.removeFormatting(global.chatHistory.get(global.chatHistory.size()-(num+1)));}
                    else {temporary = "Number must be less than the chat history size! ("+global.chatHistory.size()+")";}
                } else {temporary = "Number must be greater than or equal to 0!";}
            } catch (NumberFormatException e) {temporary = "Not a number!";}

            String stringName = "DefaultString->MSGHISTORY"+get_number+"-"+(global.TMP_string.size()+1);
            global.TMP_string.put(stringName, temporary);
            global.backupTMP_strings.put(stringName, temporary);

            TMP_e = TMP_e.replace("{msg("+strnum+")}", "{string["+stringName+"]}");
        }
        if (chatEvent!=null) {
            if (TMP_e.contains("{msg}.meta()")) {
                global.TMP_string.put("DefaultString->MSGMETA-"+(global.TMP_string.size()+1), ChatHandler.removeFormatting(chatEvent.getMessage().getStyle().toString()));
                global.backupTMP_strings.put("DefaultString->MSGMETA-"+global.TMP_string.size(), ChatHandler.removeFormatting(chatEvent.getMessage().getStyle().toString()));
                TMP_e = TMP_e.replace("{msg}.meta()", "{string[DefaultString->MSGMETA-"+global.TMP_string.size()+"]}");
            }
            if (TMP_e.contains("{msg}")) {
                TMP_e = createDefaultString("msg", ChatHandler.removeFormatting(chatEvent.getMessage().getFormattedText()), TMP_e, isAsync);
            }
        }
        if (TMP_e.contains("{me}")) {
            TMP_e = createDefaultString("me", mc.player.getDisplayNameString(), TMP_e, isAsync);
        }
        if (TMP_e.contains("{server}")) {
            String current_server;
            if (mc.isSingleplayer()) current_server = "SinglePlayer";
            else current_server = mc.getCurrentServerData().serverName;

            TMP_e = createDefaultString("server", current_server, TMP_e, isAsync);
        }
        if (TMP_e.contains("{serverMOTD}") || TMP_e.contains("{servermotd}")) {
            String returnString;
            if (mc.isSingleplayer()) returnString = "Single Player world";
            else returnString = mc.getCurrentServerData().serverMOTD;

            TMP_e = createDefaultString("serverMOTD", "servermotd", returnString, TMP_e, isAsync);
        }
        if (TMP_e.contains("{serverIP}") || TMP_e.contains("{serverip}")) {
            String returnString;
            if (mc.isSingleplayer()) returnString = "localhost";
            else returnString = mc.getCurrentServerData().serverIP;

            TMP_e = createDefaultString("serverIP", "serverip", returnString, TMP_e, isAsync);
        }
        if (TMP_e.contains("{ping}")) {
            String returnString;
            if (mc.isSingleplayer()) {returnString = "5";}
            else {
                returnString = CommandReference.getPing();
            }

            TMP_e = createDefaultString("ping", returnString, TMP_e, isAsync);
        }
        if (TMP_e.contains("{yaw}")) {
            TMP_e = createDefaultString("yaw", String.valueOf(MathHelper.wrapDegrees(mc.player.rotationYaw)), TMP_e, isAsync);
        }
        if (TMP_e.contains("{pitch}")) {
            TMP_e = createDefaultString("pitch", String.valueOf(MathHelper.wrapDegrees(mc.player.rotationPitch)), TMP_e, isAsync);
        }
        if (TMP_e.contains("{serverversion}") || TMP_e.contains("{serverVersion}")) {
            String returnString;
            if (mc.isSingleplayer()) {returnString = "1.8";}
            else {returnString = mc.getCurrentServerData().gameVersion;}

            TMP_e = createDefaultString("serverversion", "serverVersion", returnString, TMP_e, isAsync);
        }
        if (TMP_e.contains("{isFullscreen}") || TMP_e.contains("{isfullscreen}")) {
            TMP_e = createDefaultString("isfullscreen", "isFullscreen", mc.isFullScreen() + "",TMP_e, isAsync);
        }
        if (TMP_e.contains("{windowheight}") || TMP_e.contains("{windowHeight}")) {
            TMP_e = createDefaultString("windowheight", "windowHeight", mc.displayHeight + "",TMP_e, isAsync);
        }
        if (TMP_e.contains("{windowwidth}") || TMP_e.contains("{windowWidth}")) {
            TMP_e = createDefaultString("windowwidth", "windowWidth", mc.displayWidth + "",TMP_e, isAsync);
        }
        if (TMP_e.contains("{playerlist}") || TMP_e.contains("{playerList}")) {
            StringBuilder returnString = new StringBuilder("[");
            for (EntityPlayer player : mc.world.playerEntities) {
                String playerName = player.getName();
                if (!playerName.equals("")) {
                    returnString.append(player.getName()).append(",");
                }
            }
            if (returnString.toString().equals("[")) {
                TMP_e = createDefaultString("playerlist", "playerList", "[]", TMP_e, isAsync);
            } else {
                String list_name;
                if (isAsync)
                    list_name = "AsyncDefaultList->PLAYERLIST-"+(ListHandler.getListsSize()+1);
                else
                    list_name = "DefaultList->PLAYERLIST-"+(ListHandler.getListsSize()+1);

                ListHandler.getList(list_name, returnString.substring(0, returnString.length()-1)+"]");
                TMP_e = TMP_e.replace("{playerlist}", "{list[" + list_name + "]}")
                    .replace("{playerList}", "{list[" + list_name + "]}");
            }
        }
        if (TMP_e.contains("{tabList}") || TMP_e.contains("{tablist}")) {
            StringBuilder returnString = new StringBuilder("[");
            mc.ingameGUI.getTabList().resetFooterHeader();
            NetHandlerPlayClient nethandlerplayclient = mc.player.connection;
            List<NetworkPlayerInfo> list = tab.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
            for (NetworkPlayerInfo player : list) {
                returnString.append(player.getGameProfile().getName()).append(",");
            }

            if (returnString.toString().equals("[")) {
                TMP_e = createDefaultString("tabList", "tablist", "[]", TMP_e, isAsync);
            } else {
                String list_name;
                if (isAsync)
                    list_name = "AsyncDefaultList->TABLIST-"+(ListHandler.getListsSize()+1);
                else
                    list_name = "DefaultList->TABLIST-"+(ListHandler.getListsSize()+1);

                ListHandler.getList(list_name, returnString.substring(0, returnString.length()-1) + "]");
                TMP_e = TMP_e.replace("{tabList}", "{list[" + list_name + "]}")
                        .replace("{tablist}", "{list[" + list_name + "]}");
            }
        }
        if (TMP_e.contains("{scoreboardlines}") || TMP_e.contains("{scoreboardLines}")) {
            StringBuilder returnString = new StringBuilder("[");
            ScoreboardReader.resetCache();

            ArrayList<String> scoreboardNames = ScoreboardReader.getScoreboardNames();
            Collections.reverse(scoreboardNames);

            for (String scoreboardLine : scoreboardNames) {
                returnString.append(scoreboardLine.replace(",","")).append(",");
            }
            if (returnString.toString().equals("[")) {
                TMP_e = createDefaultString("scoreboardlines", "scoreboardLines", "[]", TMP_e, isAsync);
            } else {
                String list_name;
                if (isAsync)
                    list_name = "AsyncDefaultList->SCOREBOARDLINES-"+(ListHandler.getListsSize()+1);
                else
                    list_name = "DefaultList->SCOREBOARDLINES-"+(ListHandler.getListsSize()+1);

                ListHandler.getList(list_name, returnString.substring(0, returnString.length()-1)+"]");
                TMP_e = TMP_e.replace("{scoreboardlines}", "{list[" + list_name + "]}")
                    .replace("{scoreboardLines}", "{list[" + list_name + "]}");
            }
        }
        if (TMP_e.contains("{debug}")) {
            TMP_e = createDefaultString("debug", global.debug + "", TMP_e, isAsync);
        }
        if (TMP_e.contains("{titletext}") || TMP_e.contains("{titleText}")) {
			String titleText;

			titleText = ReflectionHelper.getPrivateValue(
					GuiIngame.class, FMLClientHandler.instance().getClient().ingameGUI, "field_175201_x");

            if (titleText == null) {
                titleText = "null";
            }

            TMP_e = createDefaultString("titletext", "titleText", titleText, TMP_e, isAsync);
        }
        if (TMP_e.contains("{subtitletext}") || TMP_e.contains("{subtitleText}")) {
			String subtitleText;

			subtitleText = ReflectionHelper.getPrivateValue(
					GuiIngame.class, FMLClientHandler.instance().getClient().ingameGUI, "field_175200_y");

            if (subtitleText == null) {
                subtitleText = "null";
            }

            TMP_e = createDefaultString("subtitletext", "subtitleText", subtitleText, TMP_e, isAsync);
        }
        if (TMP_e.contains("{actionbartext}") || TMP_e.contains("{actionbarText}")) {
            String recordPlaying = ReflectionHelper.getPrivateValue(
                    GuiIngame.class, FMLClientHandler.instance().getClient().ingameGUI, "field_73838_g");

            if (recordPlaying == null) {
                recordPlaying = "null";
            }

            TMP_e = createDefaultString("actionbartext", "actionbarText", recordPlaying, TMP_e, isAsync);
        }
        if (TMP_e.contains("{bossbartext}") || TMP_e.contains("{bossbarText}")) {
            GuiBossOverlay bossOverlay = Minecraft.getMinecraft().ingameGUI.getBossOverlay();
            Map<UUID, BossInfo> mapBossInfos = ReflectionHelper.getPrivateValue(GuiBossOverlay.class, bossOverlay, "field_184060_g");

			if (TMP_e.contains("{bossbartext}.hide()") || TMP_e.contains("{bossbarText}.hide()")) {
				GuiIngameForge.renderBossHealth = false;
			}else if (TMP_e.contains("{bossbartext}.show()") || TMP_e.contains("{bossbarText}.show()")) {
				GuiIngameForge.renderBossHealth = true;
			} else {
                StringBuilder returnString = new StringBuilder("[");

                for (BossInfo bossInfo : mapBossInfos.values()) {
                    returnString.append(bossInfo.getName().getFormattedText()).append(TextFormatting.RESET).append(",");
                }

                if (returnString.toString().equals("[")) {
                    TMP_e = createDefaultString("bossbartext", "[]", TMP_e, isAsync);
                } else {
                    String list_name;
                    if (isAsync)
                        list_name = "AsyncDefaultList->BOSSBARTEXT-" + (ListHandler.getListsSize() + 1);
                    else
                        list_name = "DefaultList->BOSSBARTEXT-" + (ListHandler.getListsSize() + 1);
                    ListHandler.getList(list_name, returnString.substring(0, returnString.length() - 1) + "]");
                    TMP_e = TMP_e.replace("{bossbartext}", "{list[" + list_name + "]}");
                }
            }
        }
        if (TMP_e.contains("{setcol}")) {
            TMP_e = createDefaultString("setcol", Settings.col[0], TMP_e, isAsync);
        }

        if (TMP_e.contains("{br}")) {
            StringBuilder dashes = new StringBuilder();
            float chatWidth = mc.gameSettings.chatWidth;
            float chatScale = mc.gameSettings.chatScale;
            int numdash = (int) floor(((((280*(chatWidth))+40)/320) * (1/chatScale))*53);
            for (int j=0; j<numdash; j++) {dashes.append("-");}

            TMP_e = createDefaultString("br", dashes.toString(), TMP_e, isAsync);
        }

        if (TMP_e.contains("{br(") && TMP_e.contains(")}")) {
            String fillerChar = TMP_e.substring(TMP_e.indexOf("{br(") + 4, TMP_e.indexOf(")}", TMP_e.indexOf("{br(")));

            StringBuilder fillerString = new StringBuilder();
            int chatWidth = (int) ((mc.gameSettings.chatWidth * 360) - 40);
            int strWidth = mc.fontRenderer.getStringWidth(fillerChar);
            int charsToFill = (int) Math.floor(chatWidth / strWidth);

            for (int i = 0; i < charsToFill; i++) {
                fillerString.append(fillerChar);
            }

            String toReturn = mc.fontRenderer.trimStringToWidth(fillerString.toString(), chatWidth);

            TMP_e = TMP_e.replace("{br(" + fillerChar + ")}", toReturn);
        }
        if (TMP_e.contains("{chatwidth}") || TMP_e.contains("{chatWidth}")) {
            TMP_e = createDefaultString("chatwidth", "chatWidth", ""+(int)((280*(mc.gameSettings.chatWidth))+40), TMP_e, isAsync);
        }
        if (TMP_e.contains("{scoreboardtitle}") || TMP_e.contains("{scoreboardTitle}")) {
            ScoreboardReader.resetCache();
            TMP_e = createDefaultString("scoreboardtitle", "scoreboardTitle", ChatHandler.removeFormatting(ScoreboardReader.getScoreboardTitle()), TMP_e, isAsync);
        }
        if (TMP_e.contains("{hp}") || TMP_e.contains("{HP}")) {
            TMP_e = createDefaultString("hp", "HP", global.playerHealth + "", TMP_e, isAsync);
        }
        if (TMP_e.contains("{sneak}") || TMP_e.contains("{sneaking}")) {
            TMP_e = createDefaultString("sneak", "sneaking", mc.player.isSneaking()+"", TMP_e, isAsync);
        }
        if (TMP_e.contains("{sprint}") || TMP_e.contains("{sprinting}")) {
            TMP_e = createDefaultString("sprint", "sprinting", mc.player.isSprinting()+"", TMP_e, isAsync);
        }
        if (TMP_e.contains("{inchat}") || TMP_e.contains("{inChat}")) {
            TMP_e = createDefaultString("inchat", "inChat",mc.ingameGUI.getChatGUI().getChatOpen() + "", TMP_e, isAsync);
        }
        if (TMP_e.contains("{intab}") || TMP_e.contains("{inTab}")) {
            TMP_e = createDefaultString("intab", "inTab",  mc.gameSettings.keyBindPlayerList.isKeyDown() + "", TMP_e, isAsync);
        }
        if (TMP_e.contains("{inAltScreen}")) {
            TMP_e = createDefaultString("inAltScreen", global.displayMenu + "", TMP_e, isAsync);
        }
        if (TMP_e.contains("{coordX}") || TMP_e.contains("{x}")) {
            TMP_e = createDefaultString("coordX", "x", Math.round(mc.player.posX)+"", TMP_e, isAsync);
        }
        if (TMP_e.contains("{coordY}") || TMP_e.contains("{y}")) {
            TMP_e = createDefaultString("coordY", "y", Math.round(mc.player.posY)+"", TMP_e, isAsync);
        }
        if (TMP_e.contains("{coordZ}") || TMP_e.contains("{z}")) {
            TMP_e = createDefaultString("coordZ", "z", Math.round(mc.player.posZ)+"", TMP_e, isAsync);
        }
        if (TMP_e.contains("{exactX}")) {
            TMP_e = createDefaultString("exactX", mc.player.posX+"", TMP_e, isAsync);
        }
        if (TMP_e.contains("{exactY}")) {
            TMP_e = createDefaultString("exactY", mc.player.posY+"", TMP_e, isAsync);
        }
        if (TMP_e.contains("{exactZ}")) {
            TMP_e = createDefaultString("exactZ", mc.player.posZ+"", TMP_e, isAsync);
        }
		if (TMP_e.contains("{motionX}")) {
			TMP_e = createDefaultString("motionX", mc.player.motionX + "", TMP_e, isAsync);
		}
		if (TMP_e.contains("{motionY}")) {
			TMP_e = createDefaultString("motionY", mc.player.motionY + "", TMP_e, isAsync);
		}
		if (TMP_e.contains("{motionZ}")) {
			TMP_e = createDefaultString("motionZ", mc.player.motionZ + "", TMP_e, isAsync);
		}
        if (TMP_e.contains("{fps}") || TMP_e.contains("{FPS}")) {
            TMP_e = createDefaultString("fps", "FPS", Minecraft.getDebugFPS()+"", TMP_e, isAsync);
        }
        if (TMP_e.contains("{lightLevel}")) {
        	TMP_e = createDefaultString("lightLevel", mc.world.getLight(mc.player.getPosition()) + "", TMP_e, isAsync);
		}
        if (TMP_e.contains("{fpscol}") || TMP_e.contains("{fpsCol}")) {
            String col;
            if (Minecraft.getDebugFPS() >= global.fpshigh) {
                col = global.fpshighcol;
            } else if (Minecraft.getDebugFPS() >= global.fpslow) {
                col = global.fpsmedcol;
            } else {
                col = global.fpslowcol;
            }

            TMP_e = createDefaultString("fpscol", "fpsCol", col, TMP_e, isAsync);
        }
        if (TMP_e.contains("{facing}")) {
        	float yaw = MathHelper.wrapDegrees(mc.player.rotationYaw);

        	String direction = "";

        	if(yaw < 22.5 && yaw > -22.5) {
        		direction = "south";
			} else if (yaw < 67.5 && yaw > 22.5) {
        		direction = "south west";
			} else if (yaw < 112.5 && yaw > 67.5) {
        		direction = "west";
			} else if (yaw < 157.5 && yaw > 112.5) {
        		direction = "north west";
			} else if (yaw < -157.5 || yaw > 157.5) {
        		direction = "north";
			} else if (yaw > -157.5 && yaw < -112.5) {
				direction = "north east";
			} else if (yaw > -112.5 && yaw < -67.5) {
        		direction = "east";
			} else if (yaw > -67.5 && yaw < -22.5) {
        		direction = "south east";
			}

            TMP_e = createDefaultString("facing", direction, TMP_e, isAsync);
        }
        if (TMP_e.contains("{facingMin}")) {
            TMP_e = createDefaultString("facingMin", mc.player.getHorizontalFacing().toString(), TMP_e, isAsync);
        }
        if (TMP_e.contains("{time}")) {
            DateFormat dateFormat = new SimpleDateFormat(Settings.timeFormat);
            TMP_e = createDefaultString("time", dateFormat.format(new Date()), TMP_e, isAsync);
        }
        if (TMP_e.contains("{date}")) {
            DateFormat dateFormat = new SimpleDateFormat(Settings.dateFormat);
            TMP_e = createDefaultString("date", dateFormat.format(new Date()), TMP_e, isAsync);
        }
        if (TMP_e.contains("{unixtime}") || TMP_e.contains("{unixTime}")) {
            TMP_e = createDefaultString("unixtime", "unixTime", new Date().getTime()+"", TMP_e, isAsync);
        }
        if (TMP_e.contains("{day}")) {
            DateFormat dateFormat = new SimpleDateFormat("EEEE");
            TMP_e = createDefaultString("day", dateFormat.format(new Date()), TMP_e, isAsync);
        }
        if (TMP_e.contains("{month}")) {
            DateFormat dateFormat = new SimpleDateFormat("MMMM");
            TMP_e = createDefaultString("month", dateFormat.format(new Date()), TMP_e, isAsync);
        }
        if (TMP_e.contains("{year}")) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy");
            TMP_e = createDefaultString("year", dateFormat.format(new Date()), TMP_e, isAsync);
        }
        if (TMP_e.contains("{hour}")) {
            DateFormat dateFormat = new SimpleDateFormat("H");
            TMP_e = createDefaultString("hour", dateFormat.format(new Date()), TMP_e, isAsync);
        }
        if (TMP_e.contains("{hour12}")) {
            DateFormat dateFormat = new SimpleDateFormat("h");
            TMP_e = createDefaultString("hour12", dateFormat.format(new Date()), TMP_e, isAsync);
        }
        if (TMP_e.contains("{minute}")) {
            DateFormat dateFormat = new SimpleDateFormat("m");
            TMP_e = createDefaultString("minute", dateFormat.format(new Date()), TMP_e, isAsync);
        }
        if (TMP_e.contains("{second}")) {
            DateFormat dateFormat = new SimpleDateFormat("s");
            TMP_e = createDefaultString("second", dateFormat.format(new Date()), TMP_e, isAsync);
        }
        if (TMP_e.contains("{am/pm}")) {
            DateFormat dateFormat = new SimpleDateFormat("a");
            TMP_e = createDefaultString("am/pm", dateFormat.format(new Date()), TMP_e, isAsync);
        }
        if (TMP_e.contains("{timezone}")) {
            DateFormat dateFormat = new SimpleDateFormat("zzz");
            TMP_e = createDefaultString("timezone", dateFormat.format(new Date()), TMP_e, isAsync);
        }
        if (TMP_e.contains("{potionEffects}")) {
            Collection<PotionEffect> potionEffects = Minecraft.getMinecraft().player.getActivePotionEffects();
            StringBuilder potionList = new StringBuilder("{");
            for (PotionEffect potionEffect : potionEffects) {
                if (!potionEffect.getIsAmbient()) {
                    String potionName = IconHandler.simplifyPotionName(potionEffect.getEffectName().replace("effect.",""));
                    String potionAmp = RomanNumber.toRoman(potionEffect.getAmplifier()+1);
                    int potionDurationInt = potionEffect.getDuration();
                    String potionColor = IconHandler.getPotionColors(potionName);
                    String potionDuration;

                    if (potionEffect.getIsPotionDurationMax()) {
                        potionDuration = "**:**";
                    } else {
                        int minutes = potionDurationInt / 1200;
                        potionDurationInt = potionDurationInt - (minutes * 1200);
                        int seconds = potionDurationInt / 20;
                        if (seconds < 10) {
                            potionDuration = minutes + ":0" + seconds + "";
                        } else {
                            potionDuration = minutes + ":" + seconds + "";
                        }
                    }

                    potionList.append("\"").append(JsonHandler.getForJson(potionName)).append("\":{")
                            .append("\"amplitude\":\"").append(potionAmp)
                            .append("\",\"duration\":\"").append(potionDuration)
                            .append("\",\"color\":\"").append(potionColor).append("\"},");
                }
            }
            if (potionList.toString().equals("{")) {
                potionList.append("}");
            } else {
                potionList = new StringBuilder(potionList.substring(0, potionList.length()-1) + "}");
            }

            JsonHandler.getJson("DefaultJson->POTIONEFFECTS-"+(JsonHandler.getJsonsSize()+1), potionList.toString());

            TMP_e = TMP_e.replace("{potionEffects}", "{json[DefaultJson->POTIONEFFECTS-"+ JsonHandler.getJsonsSize()+"]}");
        }
        if (TMP_e.contains("{armor}")) {
            List<ItemStack> armor_set = Minecraft.getMinecraft().player.inventory.armorInventory;
            StringBuilder armorList = new StringBuilder("{");
            for (int i=armor_set.size()-1; i>=0; i--) {
                ItemStack armor = armor_set.get(i);
                if (armor != null) {
                    float armorMaxDamage = armor.getMaxDamage();
                    float armorDamage = armorMaxDamage - armor.getItemDamage();
                    float armorPercent = (armorDamage / armorMaxDamage) * 100;
                    String armorData = armor.getMetadata()+"";
                    NBTTagCompound armorNBT = armor.getTagCompound();
                    if (armorNBT!=null) {
                        if (armorNBT.hasKey("display")) {
                            String armorNBTbase = armorNBT.getTag("display").toString();
                            String armorColor = null;
                            if (armorNBTbase.startsWith("{color:")) {
                                armorColor = armorNBTbase.substring(7, armorNBTbase.indexOf("}"));
                                if (armorColor.contains(",")) {
                                    armorColor = armorColor.substring(0, armorColor.indexOf(","));
                                }
                            }

                            if (armorData.equals("0") && armorColor!=null) {
                                armorData = "#"+armorColor;
                            }
                        }
                    }
                    armorList.append("\"").append(armor.getItem().getRegistryName().toString().replace("minecraft:","")).append("\":{")
                            .append("\"displayName\":\"").append(JsonHandler.getForJson(armor.getDisplayName()))
                            .append("\",\"maxDurability\":").append((int)floor(armorMaxDamage))
                            .append(",\"durability\":").append((int)floor(armorDamage))
                            .append(",\"durabilityPercent\":").append((int)floor(armorPercent))
                            .append(",\"data\":\"").append(armorData).append("\"},");
                }
            }
            if (armorList.toString().equals("{")) {
                armorList.append("}");
            } else {
                armorList = new StringBuilder(armorList.substring(0, armorList.length()-1) + "}");
            }

            JsonHandler.getJson("DefaultJson->ARMOR-"+(JsonHandler.getJsonsSize()+1), armorList.toString());

            TMP_e = TMP_e.replace("{armor}", "{json[DefaultJson->ARMOR-"+ JsonHandler.getJsonsSize()+"]}");
        }
        if (TMP_e.contains("{heldItem}")) {
            ItemStack item = mc.player.getHeldItem(EnumHand.MAIN_HAND);

            JsonHandler.getJson("DefaultJson->HELDITEM-"+(JsonHandler.getJsonsSize()+1), getItemJson(item));

            TMP_e = TMP_e.replace("{heldItem}", "{json[DefaultJson->HELDITEM-"+ JsonHandler.getJsonsSize()+"]}");
        }
        if (TMP_e.contains("{heldItemOffhand}")) {
            ItemStack item = Minecraft.getMinecraft().player.getHeldItem(EnumHand.OFF_HAND);

            JsonHandler.getJson("DefaultJson->HELDITEMOFFHAND-"+(JsonHandler.getJsonsSize()+1), getItemJson(item));

            TMP_e = TMP_e.replace("{heldItem}", "{json[DefaultJson->HELDITEM-"+ JsonHandler.getJsonsSize()+"]}");
        }

		if (TMP_e.contains("{hotbar(") && TMP_e.contains(")}")) {
        	int beginIndex = TMP_e.indexOf("{hotbar(") + 8;
        	int endIndex = TMP_e.indexOf(")}", beginIndex);
        	String slotString = TMP_e.substring(beginIndex, endIndex);
        	int slot = Integer.parseInt(StringFunctions.nestedArgs(slotString, chatEvent, isAsync));

        	if (slot <= 8) {
				ItemStack itemStack = mc.player.inventory.getStackInSlot(slot);

				JsonHandler.getJson("DefaultJson->HOTBAR-" + (JsonHandler.getJsonsSize() + 1), getItemJson(itemStack));
				TMP_e = TMP_e.replace("{hotbar(" + slotString + ")}",
						"{json[DefaultJson->HOTBAR-" + JsonHandler.getJsonsSize() + "]}");
			}
		}

		if (TMP_e.contains("{lookingAtMin}")) {
			RayTraceResult mop = mc.objectMouseOver;
			String jsonString;

			try {
				if (mop.typeOfHit == RayTraceResult.Type.ENTITY) {
					Entity entity = mop.entityHit;
					String isHuman = entity instanceof EntityPlayer ? "true" : "false";

					jsonString = "{\"type\":\"entity\",";
					jsonString += "\"entity\":{";
					jsonString += "\"name\":\"" + entity.getName() + "\",";
					jsonString += "\"isHuman\":\"" + isHuman + "\",";
					jsonString += "\"displayName\":\"" + entity.getCustomNameTag() + TextFormatting.RESET + "\"";

					if (entity instanceof EntityLivingBase) {

						jsonString += ",\"teamName\":\"";

						if (((EntityLivingBase) entity).getTeam() == null) {
							jsonString += "null\"";
						} else {
							jsonString += ((EntityLivingBase) entity).getTeam().getName() + "\"";
						}
					}

					jsonString += "}}";
				} else if (mop.typeOfHit == RayTraceResult.Type.MISS) {
					jsonString = "{\"type\":\"null\"}";
				} else {
					IBlockState blockState = mc.world.getBlockState(mop.getBlockPos());
					Block block = blockState.getBlock();


					if (block == null) {
						jsonString = "{\"type\":\"null\"}";
					} else {
                        BlockPos pos = mop.getBlockPos();
                        String blockOnFace = "";

                        switch(mop.sideHit.toString()) {
                            case("up"):
                                blockOnFace = mc.world.getBlockState(pos.up()).getBlock().getUnlocalizedName();
                                break;
                            case("down"):
                                blockOnFace = mc.world.getBlockState(pos.down()).getBlock().getUnlocalizedName();
                                break;
                            case("north"):
                                blockOnFace = mc.world.getBlockState(pos.north()).getBlock().getUnlocalizedName();
                                break;
                            case("south"):
                                blockOnFace = mc.world.getBlockState(pos.south()).getBlock().getUnlocalizedName();
                                break;
                            case("east"):
                                blockOnFace = mc.world.getBlockState(pos.east()).getBlock().getUnlocalizedName();
                                break;
                            case("west"):
                                blockOnFace = mc.world.getBlockState(pos.west()).getBlock().getUnlocalizedName();
                                break;
                            default:
                                break;
                        }

                        if (blockOnFace.equals("tile.water") || blockOnFace.equals("tile.lava")) {
                            jsonString = "{\"type\":\"" + blockOnFace.replace("tile.", "") + "\"}";
                        } else {
                            int itemData = block.getMetaFromState(blockState);
                            String registryName = block.getRegistryName().toString().replace("minecraft:", "");
                            if (registryName.startsWith("double_") && (registryName.endsWith("_slab") || registryName.endsWith("_slab2"))) {
                                registryName = registryName.substring(7);
                            }
                            if (registryName.startsWith("lit_") && !registryName.endsWith("pumpkin")) {
                                registryName = registryName.substring(4);
                            }
                            if (registryName.equals("anvil")) {
                                if (itemData > 5) itemData = 2;
                                else if (itemData < 4) itemData = 0;
                                else itemData = 1;
                            } else if (registryName.endsWith("_slab")
                                    || registryName.endsWith("_slab2")
                                    || registryName.equals("sapling")
                                    || registryName.equals("leaves")) {
                                if (itemData > 7) {
                                    itemData -= 8;
                                }
                            } else if (registryName.equals("log")) {
                                itemData %= 4;
                            } else if (registryName.equals("ender_chest")
                                    || registryName.equals("chest")
                                    || registryName.equals("trapped_chest")
                                    || registryName.equals("vine")
                                    || registryName.equals("tripwire_hook")
                                    || registryName.equals("dropper")
                                    || registryName.equals("dispenser")
                                    || registryName.equals("bed")
                                    || registryName.equals("ladder")
                                    || registryName.equals("end_portal_frame")
                                    || registryName.equals("daylight_detector")
                                    || registryName.equals("hay_block")
                                    || registryName.equals("brewing_stand")
                                    || registryName.equals("furnace")
                                    || registryName.equals("lever")
                                    || registryName.equals("pumpkin")
                                    || registryName.equals("lit_pumpkin")
                                    || registryName.equals("hopper")
                                    || registryName.endsWith("_button")
                                    || registryName.endsWith("_pressure_plate")
                                    || registryName.endsWith("_door")
                                    || registryName.endsWith("_fence_gate")
                                    || registryName.endsWith("_stairs")
                                    || registryName.endsWith("torch")
                                    || registryName.endsWith("piston")
                                    || registryName.endsWith("trapdoor")
                                    || registryName.endsWith("rail")) {
                                itemData = 0;
                            } else if (registryName.endsWith("_repeater")) {
                                registryName = "repeater";
                                itemData = 0;
                            } else if (registryName.endsWith("_comparator")) {
                                registryName = "comparator";
                                itemData = 0;
                            } else if (registryName.equals("redstone_wire")) {
                                registryName = "redstone";
                                itemData = 0;
                            } else if (registryName.equals("piston_head")) {
                                registryName = "piston";
                                itemData = 0;
                            } else if (registryName.equals("tripwire")) {
                                registryName = "string";
                                itemData = 0;
                            } else if (registryName.equals("standing_banner")) {
                                registryName = "banner";
                                itemData = 15;
                            } else if (registryName.equals("double_plant")) {
                                registryName = "tallgrass";
                                itemData = 1;
                            } else if (registryName.equals("quartz_block")) {
                                if (itemData > 2) {
                                    itemData = 2;
                                }
                            }

                            jsonString = "{\"type\":\"block\",";
                            jsonString += "\"block\":{";
                            jsonString += "\"xPos\":" + pos.getX() + ",";
                            jsonString += "\"yPos\":" + pos.getY() + ",";
                            jsonString += "\"zPos\":" + pos.getZ() + ",";
                            jsonString += "\"data\":" + itemData + ",";
                            jsonString += "\"metadata\":" + block.getMetaFromState(blockState) + ",";
                            jsonString += "\"name\":\"" + block.getLocalizedName() + "\",";
                            jsonString += "\"unlocalizedName\":\"" + block.getUnlocalizedName().replace("tile.", "") + "\",";
                            jsonString += "\"registryName\":\"" + registryName + "\",";
                            jsonString += "\"id\":" + Block.getIdFromBlock(block) + "";
                            jsonString += "}}";
                        }
					}
				}
			} catch (Exception e) {
				jsonString = "{}";
			}

			JsonHandler.getJson("DefaultJson->LOOKINGATMIN-" + (JsonHandler.getJsonsSize() + 1), jsonString);
			TMP_e = TMP_e.replace("{lookingAtMin}", "{json[DefaultJson->LOOKINGATMIN-" + JsonHandler.getJsonsSize() + "]}");
		}

		if (TMP_e.contains("{lookingAt}")) {
			RayTraceResult mop = mc.objectMouseOver;
			String jsonString;

			try {
				if (mop.typeOfHit == RayTraceResult.Type.ENTITY) {
					Entity entity = mop.entityHit;
					NBTTagCompound tags = new NBTTagCompound();
					entity.writeToNBT(tags);
					String isHuman = entity instanceof EntityPlayer ? "true" : "false";

					jsonString = "{\"type\":\"entity\",";
					jsonString += "\"entity\":{";
					jsonString += "\"name\":\"" + entity.getName() + "\",";
					jsonString += "\"isHuman\":\"" + isHuman + "\",";
					jsonString += "\"displayName\":\"" + entity.getCustomNameTag() + TextFormatting.RESET + "\",";
					jsonString += "\"motionX\":" + entity.motionX + ",";
					jsonString += "\"motionY\":" + entity.motionX + ",";
					jsonString += "\"motionZ\":" + entity.motionX + "";

					if (entity instanceof EntityLivingBase) {

						jsonString += ",\"teamName\":\"";

						if (((EntityLivingBase) entity).getTeam() == null) {
							jsonString += "null\"";
						} else {
							jsonString += ((EntityLivingBase) entity).getTeam().getName() + "\"";
						}
					}

					jsonString += ",\"metadata\":{";

					StringBuilder jsonStringBuilder = new StringBuilder(jsonString);
					for (String key : tags.getKeySet()) {
						if (!tags.getTag(key).toString().startsWith("[") && !tags.getTag(key).toString().startsWith("{")) {

							if (key.equalsIgnoreCase("healf") || key.equalsIgnoreCase("health")
									|| key.equalsIgnoreCase("absorptionamount")) {
								continue;
							}

							jsonStringBuilder.append("\"").append(key).append("\":\"").append(tags.getTag(key).toString().replace("\"", "")).append("\",");
						}
					}
					jsonString = jsonStringBuilder.toString();

					if (!jsonString.endsWith("{")) {
						jsonString = jsonString.substring(0, jsonString.length() - 1);
					}

					jsonString += "}";

                    jsonString += "}}";
				} else if (mop.typeOfHit == RayTraceResult.Type.MISS) {
					jsonString = "{\"type\":\"null\"}";
				} else {
					IBlockState blockState = mc.world.getBlockState(mop.getBlockPos());
					Block block = blockState.getBlock();

					if (block == null) {
						jsonString = "{\"type\":\"null\"}";
					} else {
                        BlockPos pos = mop.getBlockPos();
                        String blockOnFace = "";

                        switch (mop.sideHit.toString()) {
                            case ("up"):
                                blockOnFace = mc.world.getBlockState(pos.up()).getBlock().getUnlocalizedName();
                                break;
                            case ("down"):
                                blockOnFace = mc.world.getBlockState(pos.down()).getBlock().getUnlocalizedName();
                                break;
                            case ("north"):
                                blockOnFace = mc.world.getBlockState(pos.north()).getBlock().getUnlocalizedName();
                                break;
                            case ("south"):
                                blockOnFace = mc.world.getBlockState(pos.south()).getBlock().getUnlocalizedName();
                                break;
                            case ("east"):
                                blockOnFace = mc.world.getBlockState(pos.east()).getBlock().getUnlocalizedName();
                                break;
                            case ("west"):
                                blockOnFace = mc.world.getBlockState(pos.west()).getBlock().getUnlocalizedName();
                                break;
                            default:
                                break;
                        }

                        if (blockOnFace.equals("tile.water") || blockOnFace.equals("tile.lava")) {
                            jsonString = "{\"type\":\"" + blockOnFace.replace("tile.", "") + "\"}";
                        } else {
                            int itemData = block.getMetaFromState(blockState);
                            String registryName = block.getRegistryName().toString().replace("minecraft:", "");
                            if (registryName.startsWith("double_") && (registryName.endsWith("_slab") || registryName.endsWith("_slab2"))) {
                                registryName = registryName.substring(7);
                            }
                            if (registryName.startsWith("lit_") && !registryName.endsWith("pumpkin")) {
                                registryName = registryName.substring(4);
                            }
                            if (registryName.equals("anvil")) {
                                if (itemData > 5) itemData = 2;
                                else if (itemData < 4) itemData = 0;
                                else itemData = 1;
                            } else if (registryName.endsWith("_slab")
                                    || registryName.endsWith("_slab2")
                                    || registryName.equals("sapling")
                                    || registryName.equals("leaves")) {
                                if (itemData > 7) {
                                    itemData -= 8;
                                }
                            } else if (registryName.equals("log")) {
                                itemData %= 4;
                            } else if (registryName.equals("ender_chest")
                                    || registryName.equals("chest")
                                    || registryName.equals("trapped_chest")
                                    || registryName.equals("vine")
                                    || registryName.equals("tripwire_hook")
                                    || registryName.equals("dropper")
                                    || registryName.equals("dispenser")
                                    || registryName.equals("bed")
                                    || registryName.equals("ladder")
                                    || registryName.equals("end_portal_frame")
                                    || registryName.equals("daylight_detector")
                                    || registryName.equals("hay_block")
                                    || registryName.equals("brewing_stand")
                                    || registryName.equals("furnace")
                                    || registryName.equals("lever")
                                    || registryName.equals("pumpkin")
                                    || registryName.equals("lit_pumpkin")
                                    || registryName.equals("hopper")
                                    || registryName.endsWith("_button")
                                    || registryName.endsWith("_pressure_plate")
                                    || registryName.endsWith("_door")
                                    || registryName.endsWith("_fence_gate")
                                    || registryName.endsWith("_stairs")
                                    || registryName.endsWith("torch")
                                    || registryName.endsWith("piston")
                                    || registryName.endsWith("trapdoor")
                                    || registryName.endsWith("rail")) {
                                itemData = 0;
                            } else if (registryName.endsWith("_repeater")) {
                                registryName = "repeater";
                                itemData = 0;
                            } else if (registryName.endsWith("_comparator")) {
                                registryName = "comparator";
                                itemData = 0;
                            } else if (registryName.equals("redstone_wire")) {
                                registryName = "redstone";
                                itemData = 0;
                            } else if (registryName.equals("piston_head")) {
                                registryName = "piston";
                                itemData = 0;
                            } else if (registryName.equals("tripwire")) {
                                registryName = "string";
                                itemData = 0;
                            } else if (registryName.equals("standing_banner")) {
                                registryName = "banner";
                                itemData = 15;
                            } else if (registryName.equals("double_plant")) {
                                registryName = "tallgrass";
                                itemData = 1;
                            } else if (registryName.equals("quartz_block")) {
                                if (itemData > 2) {
                                    itemData = 2;
                                }
                            }

                            jsonString = "{\"type\":\"block\",";
                            jsonString += "\"block\":{";
                            jsonString += "\"xPos\":" + pos.getX() + ",";
                            jsonString += "\"yPos\":" + pos.getY() + ",";
                            jsonString += "\"zPos\":" + pos.getZ() + ",";
                            jsonString += "\"data\":" + itemData + ",";
                            jsonString += "\"metadata\":" + block.getMetaFromState(blockState) + ",";
                            jsonString += "\"name\":\"" + block.getLocalizedName() + "\",";
                            jsonString += "\"unlocalizedName\":\"" + block.getUnlocalizedName().replace("tile.", "") + "\",";
                            jsonString += "\"registryName\":\"" + registryName + "\",";
                            jsonString += "\"id\":" + Block.getIdFromBlock(block) + ",";
                            jsonString += "\"lightLevel\":" + mc.world.getLight(mop.getBlockPos()) + ",";
                            jsonString += "\"isOnFire\":" + block.isFireSource(mc.world, mop.getBlockPos(), EnumFacing.UP);
                            jsonString += "}}";
                        }
                    }
				}
			} catch (Exception e) {
				jsonString = "{}";
			}

			JsonHandler.getJson("DefaultJson->LOOKINGAT-" + (JsonHandler.getJsonsSize() + 1), jsonString);
			TMP_e = TMP_e.replace("{lookingAt}", "{json[DefaultJson->LOOKINGAT-" + JsonHandler.getJsonsSize() + "]}");
		}

        if (TMP_e.contains("{arrows}")) {
            List<ItemStack> inventory = mc.player.inventory.mainInventory;
            int arrows = 0;

            for (ItemStack item : inventory) {
                if (item != null) {
                    if (item.getDisplayName().equals("Arrow")) {
                        arrows += item.getCount();
                    }
                }
            }

            TMP_e = createDefaultString("arrows", arrows+"", TMP_e, isAsync);
        }

        if (TMP_e.contains("{xpLevel}")) {
        	TMP_e = createDefaultString("xpLevel",
					mc.player.experienceLevel + "", TMP_e, isAsync);
		}

		if (TMP_e.contains("{xpProgress}")) {
        	EntityPlayerSP p = mc.player;
        	TMP_e = createDefaultString("xpProgress", p.experience + "", TMP_e, isAsync);
		}

		if (TMP_e.contains("{hunger}")) {
        	TMP_e = createDefaultString("hunger",
					mc.player.getFoodStats().getFoodLevel() + "", TMP_e, isAsync);
		}

		if (TMP_e.contains("{saturation}")) {
        	TMP_e = createDefaultString("saturation",
					mc.player.getFoodStats().getSaturationLevel() + "", TMP_e, isAsync);
		}

		if (TMP_e.contains("{renderDistance}")) {
        	TMP_e = createDefaultString("renderDistance",
					mc.gameSettings.renderDistanceChunks + "", TMP_e, isAsync);
		}

		if (TMP_e.contains("{fov}")) {
        	TMP_e = createDefaultString("fov",
					Math.floor(mc.gameSettings.fovSetting) + "", TMP_e, isAsync);
		}

		if (TMP_e.contains("{uuid}")) {
        	TMP_e = createDefaultString("uuid", mc.getSession().getPlayerID(), TMP_e, isAsync);
		}

		if (TMP_e.contains("{armorPoints}")) {
        	TMP_e = createDefaultString("armorPoints",
					mc.player.getTotalArmorValue() + "", TMP_e, isAsync);
		}

        if (TMP_e.contains("{cps}")) {
            String returnString;

            returnString = global.clicks.size() + "";

            TMP_e = createDefaultString("cps", returnString.replace(".0",""), TMP_e, isAsync);
        }
        if (TMP_e.contains("{rcps}")) {
            String returnString;

            returnString = global.rclicks.size() + "";

            TMP_e = createDefaultString("rcps", returnString.replace(".0", ""), TMP_e, isAsync);
        }
        if (TMP_e.contains("{cpsAve}")) {
            String clicksAve;

            if (global.clicks_ave.size() > 0) {
                Double clicks = 0.0;
                for (Double click : global.clicks_ave) {
                    clicks += click;
                }
                clicksAve = round(clicks/global.clicks_ave.size()) + "";
            } else {
                clicksAve = "0";
            }

            TMP_e = createDefaultString("cpsAve", clicksAve.replace(".0",""), TMP_e, isAsync);
        }
        if (TMP_e.contains("{rcpsAve}")) {
            String clicksAve;

            if (global.rclicks_ave.size() > 0) {
                Double clicks = 0.0;
                for (Double click : global.rclicks_ave) {
                    clicks += click;
                }
                clicksAve = round(clicks/global.rclicks_ave.size()) + "";
            } else {
                clicksAve = "0";
            }

            TMP_e = createDefaultString("rcpsAve", clicksAve.replace(".0",""), TMP_e, isAsync);
        }
        if (TMP_e.contains("{cpsMax}")) {
            TMP_e = createDefaultString("cpsMax", global.clicks_max.toString().replace(".0",""), TMP_e, isAsync);
        }
        if (TMP_e.contains("{rcpsMax}")) {
            TMP_e = createDefaultString("rcpsMax", global.rclicks_max.toString().replace(".0",""), TMP_e, isAsync);
        }
        if (TMP_e.contains("{CTVersion}")) {
            TMP_e = createDefaultString("CTVersion", Reference.VERSION, TMP_e, isAsync);
        }

        if (TMP_e.contains("{MCVersion}")) {
        	TMP_e = createDefaultString("MCVersion", mc.getVersion(), TMP_e, isAsync);
		}

		if (TMP_e.contains("{biome}")) {
        	Chunk chunk = mc.world.getChunkFromBlockCoords(mc.player.getPosition());
        	Biome biome = chunk.getBiome(mc.player.getPosition(),
					mc.world.getBiomeProvider());

        	TMP_e = createDefaultString("biome", biome.getBiomeName(), TMP_e, isAsync);
		}

		if (TMP_e.contains("{worldTime}")) {
        	TMP_e = createDefaultString("worldTime", mc.world.getWorldTime() + "", TMP_e, isAsync);
		}

		if (TMP_e.contains("{chunkX}")) {
			TMP_e = createDefaultString("chunkX", mc.player.chunkCoordX + "", TMP_e, isAsync);
		}

		if (TMP_e.contains("{chunkY}")) {
			TMP_e = createDefaultString("chunkY", mc.player.chunkCoordY + "", TMP_e, isAsync);
		}

		if (TMP_e.contains("{chunkZ}")) {
			TMP_e = createDefaultString("chunkZ", mc.player.chunkCoordZ + "", TMP_e, isAsync);
		}

        if (TMP_e.contains("{black}")) {
            TMP_e = createDefaultString("black", "&0", TMP_e, isAsync);
        }
        if (TMP_e.contains("{darkBlue}")) {
            TMP_e = createDefaultString("darkBlue", "&1", TMP_e, isAsync);
        }
        if (TMP_e.contains("{darkGreen}")) {
            TMP_e = createDefaultString("darkGreen", "&2", TMP_e, isAsync);
        }
        if (TMP_e.contains("{darkAqua}")) {
            TMP_e = createDefaultString("darkAqua", "&3", TMP_e, isAsync);
        }
        if (TMP_e.contains("{darkRed}")) {
            TMP_e = createDefaultString("darkRed", "&4", TMP_e, isAsync);
        }
        if (TMP_e.contains("{darkPurple}")) {
            TMP_e = createDefaultString("darkPurple", "&5", TMP_e, isAsync);
        }
        if (TMP_e.contains("{gold}")) {
            TMP_e = createDefaultString("gold", "&6", TMP_e, isAsync);
        }
        if (TMP_e.contains("{gray}")) {
            TMP_e = createDefaultString("gray", "&7", TMP_e, isAsync);
        }
        if (TMP_e.contains("{darkGray}")) {
            TMP_e = createDefaultString("darkGray", "&8", TMP_e, isAsync);
        }
        if (TMP_e.contains("{blue}")) {
            TMP_e = createDefaultString("blue", "&9", TMP_e, isAsync);
        }
        if (TMP_e.contains("{green}")) {
            TMP_e = createDefaultString("green", "&a", TMP_e, isAsync);
        }
        if (TMP_e.contains("{aqua}")) {
            TMP_e = createDefaultString("aqua", "&b", TMP_e, isAsync);
        }
        if (TMP_e.contains("{red}")) {
            TMP_e = createDefaultString("red", "&c", TMP_e, isAsync);
        }
        if (TMP_e.contains("{lightPurple}")) {
            TMP_e = createDefaultString("lightPurple", "&d", TMP_e, isAsync);
        }
        if (TMP_e.contains("{yellow}")) {
            TMP_e = createDefaultString("yellow", "&e", TMP_e, isAsync);
        }
        if (TMP_e.contains("{white}")) {
            TMP_e = createDefaultString("white", "&f", TMP_e, isAsync);
        }
        if (TMP_e.contains("{obfuscated}")) {
            TMP_e = createDefaultString("obfuscated", "&k", TMP_e, isAsync);
        }
        if (TMP_e.contains("{bold}")) {
            TMP_e = createDefaultString("bold", "&l", TMP_e, isAsync);
        }
        if (TMP_e.contains("{strikethrough}")) {
            TMP_e = createDefaultString("strikethrough", "&m", TMP_e, isAsync);
        }
        if (TMP_e.contains("{underline}")) {
            TMP_e = createDefaultString("underline", "&n", TMP_e, isAsync);
        }
        if (TMP_e.contains("{italic}")) {
            TMP_e = createDefaultString("italic", "&o", TMP_e, isAsync);
        }
        if (TMP_e.contains("{reset}")) {
            TMP_e = createDefaultString("reset", "&r", TMP_e, isAsync);
        }
        if (TMP_e.contains("{randcol}")) {
            String col = "&f";
            int num = EventsHandler.randInt(0,15);
            switch(num) {
                case (0):
                case (1):
                case (2):
                case (3):
                case (4):
                case (5):
                case (6):
                case (7):
                case (8):
                case (9):
                    col = "&"+num;
                    break;
                case (10):
                    col = "&a";
                    break;
                case (11):
                    col = "&b";
                    break;
                case (12):
                    col = "&c";
                    break;
                case (13):
                    col = "&d";
                    break;
                case (14):
                    col = "&e";
                    break;
                default:
                    break;
            }
            TMP_e = createDefaultString("randcol", col, TMP_e, isAsync);
        }


        return TMP_e;
    }

	private static String getItemJson(ItemStack item) {
    	String held;

		try {
			float itemMaxDamage = item.getMaxDamage();
			float itemDamage = itemMaxDamage - item.getItemDamage();
			float itemPercent = (itemDamage / itemMaxDamage) * 100;
			String itemData = item.getMetadata() + "";
			NBTTagCompound armorNBT = item.getTagCompound();
			if (armorNBT != null) {
				if (armorNBT.hasKey("display")) {
					String armorNBTbase = armorNBT.getTag("display").toString();
					String armorColor = null;
					if (armorNBTbase.startsWith("{color:")) {
						armorColor = armorNBTbase.substring(7, armorNBTbase.indexOf("}"));
						if (armorColor.contains(",")) {
							armorColor = armorColor.substring(0, armorColor.indexOf(","));
						}
					}

					if (itemData.equals("0") && armorColor != null) {
						itemData = "#" + armorColor;
					}
				}
			}
			held = "{\"registryName\":\"" + item.getItem().getRegistryName().toString().replace("minecraft:", "") + "\","
					+ "\"" + "item"
					+ "\":{\"displayName\":\"" + JsonHandler.getForJson(item.getDisplayName())
					+ "\",\"maxDurability\":" + (int) floor(itemMaxDamage)
					+ ",\"durability\":" + (int) floor(itemDamage)
					+ ",\"durabilityPercent\":" + (int) floor(itemPercent)
					+ ",\"itemCount\":" + item.getCount()
					+ ",\"id\":" + Item.getIdFromItem(item.getItem())
					+ ",\"data\":" + itemData + "}}";
		} catch (Exception e) {
			held = "{}";
		}

		return held;
	}

    private static String createDefaultString(String string_name, String string_value, String TMP_e, Boolean isAsync) {
        if (isAsync) {
            global.Async_string.put("AsyncDefaultString->" + string_name.toUpperCase() + "-" + (global.Async_string.size() + 1), string_value);
            global.backupAsync_string.put("AsyncDefaultString->" + string_name.toUpperCase() + "-" + global.Async_string.size(), string_value);
            return TMP_e.replace("{" + string_name + "}", "{string[AsyncDefaultString->" + string_name.toUpperCase() + "-" + global.Async_string.size() + "]}");
        } else {
            global.TMP_string.put("DefaultString->" + string_name.toUpperCase() + "-" + (global.TMP_string.size() + 1), string_value);
            global.backupTMP_strings.put("DefaultString->" + string_name.toUpperCase() + "-" + global.TMP_string.size(), string_value);
            return TMP_e.replace("{" + string_name + "}", "{string[DefaultString->" + string_name.toUpperCase() + "-" + global.TMP_string.size() + "]}");
        }
    }

    private static String createDefaultString(String string_name1, String string_name2, String string_value, String TMP_e, Boolean isAsync) {
        if (isAsync) {
            global.Async_string.put("AsyncDefaultString->" + string_name1.toUpperCase() + "-" + (global.Async_string.size() + 1), string_value);
            global.backupAsync_string.put("AsyncDefaultString->" + string_name1.toUpperCase() + "-" + global.Async_string.size(), string_value);
            return TMP_e.replace("{" + string_name1 + "}", "{string[AsyncDefaultString->" + string_name1.toUpperCase() + "-" + global.Async_string.size() + "]}")
                    .replace("{" + string_name2 + "}", "{string[AsyncDefaultString->" + string_name1.toUpperCase() + "-" + global.Async_string.size() + "]}");
        } else {
            global.TMP_string.put("DefaultString->" + string_name1.toUpperCase() + "-" + (global.TMP_string.size() + 1), string_value);
            global.backupTMP_strings.put("DefaultString->" + string_name1.toUpperCase() + "-" + global.TMP_string.size(), string_value);
            return TMP_e.replace("{" + string_name1 + "}", "{string[DefaultString->" + string_name1.toUpperCase() + "-" + global.TMP_string.size() + "]}")
                    .replace("{" + string_name2 + "}", "{string[DefaultString->" + string_name1.toUpperCase() + "-" + global.TMP_string.size() + "]}");
        }
    }
}
