package com.kerbybit.chattriggers.triggers;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.commands.CommandReference;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.objects.NewJsonHandler;
import com.kerbybit.chattriggers.references.RomanNumber;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import static java.lang.Math.floor;

/**
 * Created in ${PACKAGE} by Axiom on 3/16/2017.
 */
public class BuiltInStrings {
    public static String builtInStrings(String TMP_e, ClientChatReceivedEvent chatEvent) {
        while (TMP_e.contains("{imported(") && TMP_e.contains(")}")) {
            List<String> temporary = new ArrayList<String>();
            String imp = TMP_e.substring(TMP_e.indexOf("{imported(")+10, TMP_e.indexOf(")}", TMP_e.indexOf("{imported(")));
            temporary.add("DefaultString->IMPORTED"+imp+"-"+(global.TMP_string.size()+1));
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
                temporary.add("true");
            } else {
                temporary.add("false");
            }

            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{imported("+imp+")}", "{string[DefaultString->IMPORTED"+imp+"-"+global.TMP_string.size()+"]}");
        }
        while (TMP_e.contains("{random(") && TMP_e.contains(")}")) {
            List<String> temporary = new ArrayList<String>();
            String lowhigh = TMP_e.substring(TMP_e.indexOf("{random(")+8, TMP_e.indexOf(")}", TMP_e.indexOf("{random(")));
            temporary.add("DefaultString->RANDOM"+lowhigh+"-"+(global.TMP_string.size()+1));
            try {
                int low = 0;
                int high;
                if (lowhigh.contains(",")) {
                    String[] tmp_lowhigh = lowhigh.split(",");
                    low = Integer.parseInt(tmp_lowhigh[0].trim());
                    high = Integer.parseInt(tmp_lowhigh[1].trim());
                } else {
                    high = Integer.parseInt(lowhigh);
                }

                temporary.add(EventsHandler.randInt(low,high) + "");
            } catch (NumberFormatException e) {
                temporary.add("Not a number!");
            }

            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{random("+lowhigh+")}", "{string[DefaultString->RANDOM"+lowhigh+"-"+global.TMP_string.size()+"]}");
        }
        while (TMP_e.contains("{msg[") && TMP_e.contains("]}")) {
            String strnum = TMP_e.substring(TMP_e.indexOf("{msg[")+5, TMP_e.indexOf("]}", TMP_e.indexOf("{msg[")));
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->MSGHISTORY"+strnum+"-"+(global.TMP_string.size()+1));
            try {
                int num = Integer.parseInt(strnum);
                if (num>=0) {
                    if (num<global.chatHistory.size()) {temporary.add(ChatHandler.removeFormatting(global.chatHistory.get(global.chatHistory.size()-(num+1))));}
                    else {temporary.add("Number must be less than the chat history size! ("+global.chatHistory.size()+")");}
                } else {temporary.add("Number must be greater than or equal to 0!");}
            } catch (NumberFormatException e) {temporary.add("Not a number!");}
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{msg["+strnum+"]}", "{string[DefaultString->MSGHISTORY"+strnum+"-"+global.TMP_string.size()+"]}");
        }
        if (chatEvent!=null) {
            if (TMP_e.contains("{msg}.meta().clickEvent().action()")) {
                String tmp = chatEvent.message.getChatStyle().toString();
                if (tmp.contains("clickEvent=ClickEvent{") && tmp.contains("}")) {
                    tmp = tmp.substring(tmp.indexOf("clickEvent=ClickEvent{")+22, tmp.indexOf("}", tmp.indexOf("clickEvent=ClickEvent{")));
                    if (tmp.contains("action=")) {
                        tmp = tmp.substring(tmp.indexOf("action=")+7, tmp.indexOf(", value", tmp.indexOf("action=")+7));
                    } else {
                        tmp = "null";
                    }
                } else {
                    tmp = "null";
                }
                List<String> temporary = new ArrayList<String>();
                temporary.add("DefaultString->MSGMETACLICKACTION-"+(global.TMP_string.size()+1));
                temporary.add(tmp);
                global.TMP_string.add(temporary);
                global.backupTMP_strings.add(temporary);
                TMP_e = TMP_e.replace("{msg}.meta().clickEvent().action()", "{string[DefaultString->MSGMETACLICKACTION-"+global.TMP_string.size()+"]}");
            }
            if (TMP_e.contains("{msg}.meta().clickEvent().value()")) {
                String tmp = chatEvent.message.getChatStyle().toString();
                if (tmp.contains("clickEvent=ClickEvent{") && tmp.contains("}")) {
                    tmp = tmp.substring(tmp.indexOf("clickEvent=ClickEvent{")+22, tmp.indexOf("}", tmp.indexOf("clickEvent=ClickEvent{")));
                    if (tmp.contains("value='")) {
                        tmp = tmp.substring(tmp.indexOf("value='")+7, tmp.indexOf("'", tmp.indexOf("value='")+7));
                    } else {
                        tmp = "null";
                    }
                } else {
                    tmp = "null";
                }
                List<String> temporary = new ArrayList<String>();
                temporary.add("DefaultString->MSGMETACLICKVALUE-"+(global.TMP_string.size()+1));
                temporary.add(tmp);
                global.TMP_string.add(temporary);
                global.backupTMP_strings.add(temporary);
                TMP_e = TMP_e.replace("{msg}.meta().clickEvent().value()", "{string[DefaultString->MSGMETACLICKVALUE-"+global.TMP_string.size()+"]}");
            }
            if (TMP_e.contains("{msg}.meta().hoverEvent().action()")) {
                String tmp = chatEvent.message.getChatStyle().toString();
                if (tmp.contains("hoverEvent=HoverEvent{") && tmp.contains("}")) {
                    tmp = tmp.substring(tmp.indexOf("hoverEvent=HoverEvent{")+22, tmp.indexOf("}", tmp.indexOf("hoverEvent=HoverEvent{")));
                    if (tmp.contains("action=")) {
                        tmp = tmp.substring(tmp.indexOf("action=")+7, tmp.indexOf(", value", tmp.indexOf("action=")+7));
                    } else {
                        tmp = "null";
                    }
                } else {
                    tmp = "null";
                }
                List<String> temporary = new ArrayList<String>();
                temporary.add("DefaultString->MSGMETAHOVERACTION-"+(global.TMP_string.size()+1));
                temporary.add(tmp);
                global.TMP_string.add(temporary);
                global.backupTMP_strings.add(temporary);
                TMP_e = TMP_e.replace("{msg}.meta().hoverEvent().action()", "{string[DefaultString->MSGMETAHOVERACTION-"+global.TMP_string.size()+"]}");
            }
            if (TMP_e.contains("{msg}.meta().hoverEvent().value()")) {
                String tmp = chatEvent.message.getChatStyle().toString();
                if (tmp.contains("hoverEvent=HoverEvent{") && tmp.contains("}")) {
                    tmp = tmp.substring(tmp.indexOf("hoverEvent=HoverEvent{")+22, tmp.indexOf("}", tmp.indexOf("hoverEvent=HoverEvent{")));
                    if (tmp.contains("action=")) {
                        tmp = tmp.substring(tmp.indexOf("value='TextComponent{text='")+27, tmp.indexOf("',", tmp.indexOf("value='TextComponent{text='")+27));
                    } else {
                        tmp = "null";
                    }
                } else {
                    tmp = "null";
                }
                List<String> temporary = new ArrayList<String>();
                temporary.add("DefaultString->MSGMETAHOVERVALUE-"+(global.TMP_string.size()+1));
                temporary.add(tmp);
                global.TMP_string.add(temporary);
                global.backupTMP_strings.add(temporary);
                TMP_e = TMP_e.replace("{msg}.meta().hoverEvent().value()", "{string[DefaultString->MSGMETAHOVERVALUE-"+global.TMP_string.size()+"]}");
            }
            if (TMP_e.contains("{msg}.meta().clickEvent()")) {
                String tmp = chatEvent.message.getChatStyle().toString();
                if (tmp.contains("clickEvent=") && tmp.contains(", hoverEvent")) {
                    tmp = tmp.substring(tmp.indexOf("clickEvent=")+11, tmp.indexOf(", hoverEvent=", tmp.indexOf("clickEvent=")));
                } else {
                    tmp = "An unknown error has occured";
                }
                List<String> temporary = new ArrayList<String>();
                temporary.add("DefaultString->MSGMETACLICK-"+(global.TMP_string.size()+1));
                temporary.add(tmp);
                global.TMP_string.add(temporary);
                global.backupTMP_strings.add(temporary);
                TMP_e = TMP_e.replace("{msg}.meta().clickEvent()", "{string[DefaultString->MSGMETACLICK-"+global.TMP_string.size()+"]}");
            }
            if (TMP_e.contains("{msg}.meta().hoverEvent()")) {
                String tmp = chatEvent.message.getChatStyle().toString();
                if (tmp.contains("hoverEvent=") && tmp.contains(", insertion=")) {
                    tmp = tmp.substring(tmp.indexOf("hoverEvent=")+11, tmp.indexOf(", insertion=", tmp.indexOf("hoverEvent=")));
                } else {
                    tmp = "An unknown error has occured";
                }
                List<String> temporary = new ArrayList<String>();
                temporary.add("DefaultString->MSGMETAHOVER-"+(global.TMP_string.size()+1));
                temporary.add(tmp);
                global.TMP_string.add(temporary);
                global.backupTMP_strings.add(temporary);
                TMP_e = TMP_e.replace("{msg}.meta().hoverEvent()", "{string[DefaultString->MSGMETAHOVER-"+global.TMP_string.size()+"]}");
            }
            if (TMP_e.contains("{msg}.meta()")) {
                List<String> temporary = new ArrayList<String>();
                temporary.add("DefaultString->MSGMETA-"+(global.TMP_string.size()+1));
                temporary.add(ChatHandler.removeFormatting(chatEvent.message.getChatStyle().toString()));
                global.TMP_string.add(temporary);
                global.backupTMP_strings.add(temporary);
                TMP_e = TMP_e.replace("{msg}.meta()", "{string[DefaultString->MSGMETA-"+global.TMP_string.size()+"]}");
            }
            if (TMP_e.contains("{msg}")) {
                List<String> temporary = new ArrayList<String>();
                temporary.add("DefaultString->MSG-"+(global.TMP_string.size()+1));
                temporary.add(ChatHandler.removeFormatting(chatEvent.message.getFormattedText()));
                global.TMP_string.add(temporary);
                global.backupTMP_strings.add(temporary);
                TMP_e = TMP_e.replace("{msg}", "{string[DefaultString->MSG-"+global.TMP_string.size()+"]}");
            }
        }
        if (TMP_e.contains("{trigsize}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->TRIGSIZE-"+(global.TMP_string.size()+1));
            temporary.add(global.trigger.size()+"");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{trigsize}", "{string[DefaultString->TRIGSIZE-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{notifysize}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->NOTIFYSIZE-"+(global.TMP_string.size()+1));
            temporary.add(global.notifySize+"");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{notifysize}", "{string[DefaultString->NOTIFYSIZE-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{me}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->ME-"+(global.TMP_string.size()+1));
            temporary.add(Minecraft.getMinecraft().thePlayer.getDisplayNameString());
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{me}", "{string[DefaultString->ME-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{server}")) {
            String current_server;
            if (Minecraft.getMinecraft().isSingleplayer()) {current_server = "SinglePlayer";}
            else {current_server = Minecraft.getMinecraft().getCurrentServerData().serverName;}

            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->SERVER-"+(global.TMP_string.size()+1));
            temporary.add(current_server);
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{server}", "{string[DefaultString->SERVER-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{serverMOTD}")) {
            String returnString;
            if (Minecraft.getMinecraft().isSingleplayer()) {returnString = "Single Player world";}
            else {returnString = Minecraft.getMinecraft().getCurrentServerData().serverMOTD;}

            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->SERVERMOTD-"+(global.TMP_string.size()+1));
            temporary.add(returnString);
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{serverMOTD}", "{string[DefaultString->SERVERMOTD-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{serverIP}")) {
            String returnString;
            if (Minecraft.getMinecraft().isSingleplayer()) {returnString = "localhost";}
            else {returnString = Minecraft.getMinecraft().getCurrentServerData().serverIP;}

            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->SERVERIP-"+(global.TMP_string.size()+1));
            temporary.add(returnString);
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{serverIP}", "{string[DefaultString->SERVERIP-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{ping}")) {
            String returnString;
            if (Minecraft.getMinecraft().isSingleplayer()) {returnString = "5";}
            else {
                returnString = CommandReference.getPing();
            }

            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->SERVERPING-"+(global.TMP_string.size()+1));
            temporary.add(returnString);
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{ping}", "{string[DefaultString->SERVERPING-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{serverversion}")) {
            String returnString;
            if (Minecraft.getMinecraft().isSingleplayer()) {returnString = "1.8";}
            else {returnString = Minecraft.getMinecraft().getCurrentServerData().gameVersion;}

            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->SERVERVERSION-"+(global.TMP_string.size()+1));
            temporary.add(returnString);
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{serverversion}", "{string[DefaultString->SERVERVERSION-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{debug}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->DEBUG-"+(global.TMP_string.size()+1));
            temporary.add(global.debug+"");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{debug}", "{string[DefaultString->DEBUG-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{setcol}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->SETCOL-"+(global.TMP_string.size()+1));
            temporary.add(global.settings.get(0));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{setcol}", "{string[DefaultString->SETCOL-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{br}")) {
            String dashes = "";
            float chatWidth = Minecraft.getMinecraft().gameSettings.chatWidth;
            float chatScale = Minecraft.getMinecraft().gameSettings.chatScale;
            int numdash = (int) floor(((((280*(chatWidth))+40)/320) * (1/chatScale))*53);
            for (int j=0; j<numdash; j++) {dashes += "-";}
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->BR-"+(global.TMP_string.size()+1));
            temporary.add(dashes);
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{br}", "{string[DefaultString->BR-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{chatwidth}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->CHATWIDTH-"+(global.TMP_string.size()+1));
            temporary.add(""+(int)((280*(Minecraft.getMinecraft().gameSettings.chatWidth))+40));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{chatwidth}", "{string[DefaultString->CHATWIDTH-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{scoreboardtitle}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->SCOREBOARDTITLE-"+(global.TMP_string.size()+1));
            String boardTitle = "null";
            try {
                if (Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(0) != null) {
                    boardTitle = Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(0).getDisplayName();
                } else if (Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1) != null) {
                    boardTitle = Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName();
                }
            } catch (Exception e) {
                //Do nothing//
                //catch for ReplayMod//
            }
            temporary.add(ChatHandler.removeFormatting(boardTitle));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{scoreboardtitle}", "{string[DefaultString->SCOREBOARDTITLE-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{hp}") || TMP_e.contains("{HP}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->HP-"+(global.TMP_string.size()+1));
            temporary.add(global.playerHealth + "");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{hp}", "{string[DefaultString->HP-"+global.TMP_string.size()+"]}")
                    .replace("{HP}", "{string[DefaultString->HP-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{sneak}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->SNEAK-"+(global.TMP_string.size()+1));
            temporary.add(Minecraft.getMinecraft().thePlayer.isSneaking()+"");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{sneak}", "{string[DefaultString->SNEAK-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{coordx}") || TMP_e.contains("{x}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->COORDX-"+(global.TMP_string.size()+1));
            temporary.add(Math.round(Minecraft.getMinecraft().thePlayer.posX)+"");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{coordx}", "{string[DefaultString->COORDX-"+global.TMP_string.size()+"]}")
                    .replace("{x}", "{string[DefaultString->COORDX-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{coordy}") || TMP_e.contains("{y}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->COORDY-"+(global.TMP_string.size()+1));
            temporary.add(Math.round(Minecraft.getMinecraft().thePlayer.posY)+"");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{coordy}", "{string[DefaultString->COORDY-"+global.TMP_string.size()+"]}")
                    .replace("{y}", "{string[DefaultString->COORDY-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{coordz}") || TMP_e.contains("{z}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->COORDZ-"+(global.TMP_string.size()+1));
            temporary.add(Math.round(Minecraft.getMinecraft().thePlayer.posZ)+"");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{coordz}", "{string[DefaultString->COORDZ-"+global.TMP_string.size()+"]}")
                    .replace("{z}", "{string[DefaultString->COORDZ-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{fps}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->FPS-"+(global.TMP_string.size()+1));
            temporary.add(Math.round(global.fps)+"");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{fps}", "{string[DefaultString->FPS-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{fpscol}")) {
            String col;
            if (global.fps >= global.fpshigh) {
                col = global.fpshighcol;
            } else if (global.fps >= global.fpslow) {
                col = global.fpsmedcol;
            } else {
                col = global.fpslowcol;
            }

            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->FPSCOL-"+(global.TMP_string.size()+1));
            temporary.add(col);
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{fpscol}", "{string[DefaultString->FPSCOL-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{facing}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->FACING-"+(global.TMP_string.size()+1));
            temporary.add(Minecraft.getMinecraft().thePlayer.getHorizontalFacing().toString());
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{facing}", "{string[DefaultString->FACING-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{time}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->TIME-"+(global.TMP_string.size()+1));
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            String minute_string;
            if (minute < 10) {
                minute_string = "0" + minute;
            } else {
                minute_string = "" + minute;
            }
            if (hour > 12) {
                minute_string = minute_string + "pm";
                hour = hour - 12;
            } else {
                minute_string = minute_string + "am";
            }
            temporary.add(hour + ":" + minute_string);
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{time}", "{string[DefaultString->TIME-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{potionEffects}")) {
            Collection<PotionEffect> potionEffects = Minecraft.getMinecraft().thePlayer.getActivePotionEffects();
            String potionList = "{";
            for (PotionEffect potionEffect : potionEffects) {
                if (!potionEffect.getIsAmbient()) {
                    String potionName = CommandReference.simplifyPotionName(Potion.potionTypes[potionEffect.getPotionID()].getName().replace("potion.",""));
                    String potionAmp = RomanNumber.toRoman(potionEffect.getAmplifier()+1);
                    String potionDuration = Potion.getDurationString(potionEffect);
                    String potionColor = CommandReference.getPotionColors(potionName);

                    potionList += "\"" + potionName + "\":{";
                    potionList += "\"amplitude\":\"" + potionAmp + "\",\"duration\":\"" + potionDuration + "\",\"color\":\"" + potionColor + "\"},";
                }
            }
            if (potionList.equals("{")) {
                potionList += "}";
            } else {
                potionList = potionList.substring(0, potionList.length()-1) + "}";
            }

            NewJsonHandler.getJson("DefaultJson->POTIONEFFECTS-"+(global.jsons.size()+1), potionList);

            TMP_e = TMP_e.replace("{potionEffects}", "{json[DefaultJson->POTIONEFFECTS-"+global.jsons.size()+"]}");
        }
        if (TMP_e.contains("{armor}")) {
            ItemStack[] armor_set = Minecraft.getMinecraft().thePlayer.inventory.armorInventory;
            String armorList = "{";
            for (int i=armor_set.length-1; i>=0; i--) {
                ItemStack armor = armor_set[i];
                if (armor != null) {
                    float armorMaxDamage = armor.getMaxDamage();
                    float armorDamage = armorMaxDamage - armor.getItemDamage();
                    float armorPercent = (armorDamage / armorMaxDamage) * 100;
                    armorList += "\"" + armor.getItem().getRegistryName().replace("minecraft:","") + "\":{";
                    armorList += "\"displayName\":\"" + armor.getDisplayName() + "\",\"maxDurability\":" + (int)floor(armorMaxDamage) + ",\"durability\":" + (int)floor(armorDamage) + ",\"durabilityPercent\":" + (int)floor(armorPercent) + "},";
                }
            }
            if (armorList.equals("{")) {
                armorList += "}";
            } else {
                armorList = armorList.substring(0, armorList.length()-1) + "}";
            }

            NewJsonHandler.getJson("DefaultJson->ARMOR-"+(global.jsons.size()+1), armorList);

            TMP_e = TMP_e.replace("{armor}", "{json[DefaultJson->ARMOR-"+global.jsons.size()+"]}");
        }
        if (TMP_e.contains("{cps}")) {
            String returnString;

            if (global.clicks_ave.size() > 0) {
                returnString = floor(global.clicks_ave.get(global.clicks_ave.size()-1)) + "";
            } else {
                returnString = "0";
            }

            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->CPS-"+(global.TMP_string.size()+1));
            temporary.add(returnString.replace(".0",""));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{cps}", "{string[DefaultString->CPS-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{cpsAve}")) {
            String returnString;

            if (global.clicks_ave.size() > 0) {
                Double clicks = 0.0;
                for (Double click : global.clicks_ave) {
                    clicks += click;
                }
                returnString = clicks/global.clicks_ave.size() + "";
            } else {
                returnString = "0";
            }

            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->CPSAVE-"+(global.TMP_string.size()+1));
            temporary.add(returnString.replace(".0",""));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{cpsAve}", "{string[DefaultString->CPSAVE-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{cpsMax}")) {
            String returnString = global.clicks_max + "";

            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->CPSMAX-"+(global.TMP_string.size()+1));
            temporary.add(returnString.replace(".0",""));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{cpsMax}", "{string[DefaultString->CPSMAX-"+global.TMP_string.size()+"]}");
        }

        if (TMP_e.contains("{black}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->BLACK-"+(global.TMP_string.size()+1));
            temporary.add("&0");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{black}", "{string[DefaultString->BLACK-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{darkBlue}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->DARKBLUE-"+(global.TMP_string.size()+1));
            temporary.add("&1");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{darkBlue}", "{string[DefaultString->DARKBLUE-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{darkGreen}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->DARKGREEN-"+(global.TMP_string.size()+1));
            temporary.add("&2");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{darkGreen}", "{string[DefaultString->DARKGREEN-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{darkAqua}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->DARKAQUA-"+(global.TMP_string.size()+1));
            temporary.add("&3");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{darkAqua}", "{string[DefaultString->DARKAQUA-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{darkRed}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->DARKRED-"+(global.TMP_string.size()+1));
            temporary.add("&4");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{darkRed}", "{string[DefaultString->DARKRED-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{darkPurple}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->DARKPURPLE-"+(global.TMP_string.size()+1));
            temporary.add("&5");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{darkPurple}", "{string[DefaultString->DARKPURPLE-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{gold}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->GOLD-"+(global.TMP_string.size()+1));
            temporary.add("&6");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{gold}", "{string[DefaultString->GOLD-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{gray}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->GRAY-"+(global.TMP_string.size()+1));
            temporary.add("&7");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{gray}", "{string[DefaultString->GRAY-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{darkGray}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->DARKGRAY-"+(global.TMP_string.size()+1));
            temporary.add("&8");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{darkGray}", "{string[DefaultString->DARKGRAY-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{blue}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->BLUE-"+(global.TMP_string.size()+1));
            temporary.add("&9");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{blue}", "{string[DefaultString->BLUE-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{green}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->GREEN-"+(global.TMP_string.size()+1));
            temporary.add("&a");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{green}", "{string[DefaultString->GREEN-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{aqua}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->AQUA-"+(global.TMP_string.size()+1));
            temporary.add("&b");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{aqua}", "{string[DefaultString->AQUA-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{red}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->RED-"+(global.TMP_string.size()+1));
            temporary.add("&c");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{red}", "{string[DefaultString->RED-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{lightPurple}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->LIGHTPURPLE-"+(global.TMP_string.size()+1));
            temporary.add("&d");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{lightPurple}", "{string[DefaultString->LIGHTPURPLE-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{yellow}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->YELLOW-"+(global.TMP_string.size()+1));
            temporary.add("&e");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{yellow}", "{string[DefaultString->YELLOW-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{white}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->WHITE-"+(global.TMP_string.size()+1));
            temporary.add("&f");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{white}", "{string[DefaultString->WHITE-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{obfuscated}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->OBFUSCATED-"+(global.TMP_string.size()+1));
            temporary.add("&k");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{obfuscated}", "{string[DefaultString->OBFUSCATED-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{bold}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->BOLD-"+(global.TMP_string.size()+1));
            temporary.add("&l");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{bold}", "{string[DefaultString->BOLD-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{strikethrough}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->STRIKETHROUGH-"+(global.TMP_string.size()+1));
            temporary.add("&m");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{strikethrough}", "{string[DefaultString->STRIKETHROUGH-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{underline}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->UNDERLINE-"+(global.TMP_string.size()+1));
            temporary.add("&n");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{underline}", "{string[DefaultString->UNDERLINE-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{italic}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->ITALIC-"+(global.TMP_string.size()+1));
            temporary.add("&o");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{italic}", "{string[DefaultString->ITALIC-"+global.TMP_string.size()+"]}");
        }
        if (TMP_e.contains("{reset}")) {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DefaultString->RESET-"+(global.TMP_string.size()+1));
            temporary.add("&r");
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{reset}", "{string[DefaultString->RESET-"+global.TMP_string.size()+"]}");
        }

        return TMP_e;
    }
}
