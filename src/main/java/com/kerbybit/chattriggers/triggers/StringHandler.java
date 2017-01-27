package com.kerbybit.chattriggers.triggers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.commands.CommandReference;
import com.kerbybit.chattriggers.file.FileHandler;
import com.kerbybit.chattriggers.file.JsonHandler;
import com.kerbybit.chattriggers.globalvars.global;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class StringHandler {
	static String builtInStrings(String TMP_e, ClientChatReceivedEvent chatEvent) {
        while (TMP_e.contains("{imported(") && TMP_e.contains(")}")) {
            List<String> temporary = new ArrayList<String>();
            String imp = TMP_e.substring(TMP_e.indexOf("{imported(")+10, TMP_e.indexOf(")}", TMP_e.indexOf("{imported(")));
            temporary.add("DefaultString->IMPORTED"+imp+"-"+(global.TMP_string.size()+1));
            Boolean isImported = false;

            File dir = new File("./mods/ChatTriggers/Imports/");
            if (!dir.exists()) {
                if (!dir.mkdir()) {ChatHandler.warn(ChatHandler.color("red", "Unable to create file!"));}
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
		if (TMP_e.contains("{ping}")) {///TODO
			String returnString;
			if (Minecraft.getMinecraft().isSingleplayer()) {returnString = "5";}
			else {returnString = Minecraft.getMinecraft().getCurrentServerData().pingToServer+"";}
			
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
			int numdash = (int) Math.floor(((((280*(chatWidth))+40)/320) * (1/chatScale))*53);
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
			if (Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(0) != null) {
				boardTitle = Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(0).getDisplayName();
	        } else if (Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1) != null) {
	        	boardTitle = Minecraft.getMinecraft().theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName();
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
					.replace("{y}", "{string[DefaultString->COORDX-"+global.TMP_string.size()+"]}");
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
					.replace("{y}", "{string[DefaultString->COORDZ-"+global.TMP_string.size()+"]}");
		}
		return TMP_e;
	}
	
	static void resetBackupStrings() {
		try {
			global.backupUSR_strings.clear();
			for (int j=0; j<global.USR_string.size(); j++) {
				String first = global.USR_string.get(j).get(0);
				String second = global.USR_string.get(j).get(1);
				List<String> temporary = new ArrayList<String>();
				temporary.add(first);
				temporary.add(second);
				global.backupUSR_strings.add(temporary);
			}
			
			global.backupTMP_strings.clear();
			for (int j=0; j<global.TMP_string.size(); j++) {
				String first = global.TMP_string.get(j).get(0);
				String second = global.TMP_string.get(j).get(1);
				List<String> temporary = new ArrayList<String>();
				temporary.add(first);
				temporary.add(second);
				global.backupTMP_strings.add(temporary);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ChatHandler.warn(ChatHandler.color("red", "Something went really really wrong! Check logs for details."));
			CommandReference.resetAll();
		}
		
	}
	
	public static int getStringNum(String sn) {
		for (int i=0; i<global.USR_string.size(); i++) {
			if (global.USR_string.get(i).get(0).equals(sn)) {
				return i;
			}
		}
		return -1;
	}
	
	private static int getTempStringNum(String sn) {
		for (int i=0; i<global.TMP_string.size(); i++) {
			if (global.TMP_string.get(i).get(0).equals(sn)) {
				return i;
			}
		}
		return -1;
	}
	
	private static String doStringFunctions(String sn, String func, String args, ClientChatReceivedEvent chatEvent) {
		int stringnum;
		int tmpstringnum = -1;
		
		args = stringFunctions(args, chatEvent);
		while (args.contains("{array[") && args.contains("]}")) {
			args = ArrayHandler.arrayFunctions(args, chatEvent);
			args = stringFunctions(args, chatEvent);
		}
		while (args.contains("{string[") && args.contains("]}")) {
			args = stringFunctions(args, chatEvent);
		}
		
		sn = stringFunctions(sn, chatEvent);
		
		stringnum = getStringNum(sn);
		
		if (stringnum==-1) {
			tmpstringnum = getTempStringNum(sn);
			if (tmpstringnum == -1) {
				ArrayList<String> temp = new ArrayList<String>();
                temp.add(sn); temp.add("");
                global.TMP_string.add(temp);
                global.backupTMP_strings.add(temp);
                tmpstringnum = global.TMP_string.size()-1;
			}
		} 

		if (func.equalsIgnoreCase("SET")) {
			if (!args.equals("~")) {
				if (stringnum!=-1) {
					global.USR_string.get(stringnum).set(1, args
							.replace("stringOpenBracketF6cyUQp9stringOpenBracket","(")
							.replace("stringCloseBracketF6cyUQp9stringCloseBracket",")")
							.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
					global.backupUSR_strings.get(stringnum).set(1, args
							.replace("stringOpenBracketF6cyUQp9stringOpenBracket","(")
							.replace("stringCloseBracketF6cyUQp9stringCloseBracket",")")
							.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
				} else {
					global.TMP_string.get(tmpstringnum).set(1, args
							.replace("stringOpenBracketF6cyUQp9stringOpenBracket","(")
							.replace("stringCloseBracketF6cyUQp9stringCloseBracket",")")
							.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
					global.backupTMP_strings.get(tmpstringnum).set(1, args
							.replace("stringOpenBracketF6cyUQp9stringOpenBracket","(")
							.replace("stringCloseBracketF6cyUQp9stringCloseBracket",")")
							.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
				}
			} else {
				if (stringnum!=-1) {
					global.backupUSR_strings.get(stringnum).set(1, global.USR_string.get(stringnum).get(1)
							.replace("stringOpenBracketF6cyUQp9stringOpenBracket","(")
							.replace("stringCloseBracketF6cyUQp9stringCloseBracket",")")
							.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
				} else {
					global.backupTMP_strings.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1)
							.replace("stringOpenBracketF6cyUQp9stringOpenBracket","(")
							.replace("stringCloseBracketF6cyUQp9stringCloseBracket",")")
							.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
				}
			}
			return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("SAVE")) {
			if (!args.equals("~")) {
				if (stringnum!=-1) {
					global.USR_string.get(stringnum).set(1, args
							.replace("stringOpenBracketF6cyUQp9stringOpenBracket","(")
							.replace("stringCloseBracketF6cyUQp9stringCloseBracket",")")
							.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
					global.backupUSR_strings.get(stringnum).set(1, args
							.replace("stringOpenBracketF6cyUQp9stringOpenBracket","(")
							.replace("stringCloseBracketF6cyUQp9stringCloseBracket",")")
							.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
				} else {
					global.TMP_string.get(tmpstringnum).set(1, args
							.replace("stringOpenBracketF6cyUQp9stringOpenBracket","(")
							.replace("stringCloseBracketF6cyUQp9stringCloseBracket",")")
							.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
					global.backupTMP_strings.get(tmpstringnum).set(1, args
							.replace("stringOpenBracketF6cyUQp9stringOpenBracket","(")
							.replace("stringCloseBracketF6cyUQp9stringCloseBracket",")")
							.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
				}
			} else {
				if (stringnum!=-1) {
					global.backupUSR_strings.get(stringnum).set(1, global.USR_string.get(stringnum).get(1)
							.replace("stringOpenBracketF6cyUQp9stringOpenBracket","(")
							.replace("stringCloseBracketF6cyUQp9stringCloseBracket",")")
							.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
				} else {
					global.backupTMP_strings.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1)
							.replace("stringOpenBracketF6cyUQp9stringOpenBracket","(")
							.replace("stringCloseBracketF6cyUQp9stringCloseBracket",")")
							.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
				}
			}
			try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
            return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("ADD") || func.equalsIgnoreCase("PLUS") || func.equalsIgnoreCase("+")) {
			try {
				Double strnmbr;
				if (stringnum!=-1) {strnmbr = Double.parseDouble(global.USR_string.get(stringnum).get(1).replace(",",""));}
				else {strnmbr = Double.parseDouble(global.TMP_string.get(tmpstringnum).get(1).replace(",",""));}
				try {
					Double argnmbr = Double.parseDouble(args.replace(",",""));
					String rtnmbr = (strnmbr + argnmbr) + "";
                    if (rtnmbr.endsWith(".0")) {rtnmbr = rtnmbr.substring(0,rtnmbr.length()-2);}
					if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, rtnmbr);}
					else {global.TMP_string.get(tmpstringnum).set(1, rtnmbr);}
					return "{string["+sn+"]}";
				} catch (NumberFormatException e) {
					if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", args+" is not a number!"));}
				}
			} catch (NumberFormatException e) {
				if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", sn+" is not a number!"));}
			}
            return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("SUBTRACT") || func.equalsIgnoreCase("MINUS") || func.equalsIgnoreCase("-")) {
			try {
				Double strnmbr;
				if (stringnum!=-1) {strnmbr = Double.parseDouble(global.USR_string.get(stringnum).get(1).replace(",",""));}
				else {strnmbr = Double.parseDouble(global.TMP_string.get(tmpstringnum).get(1).replace(",",""));}
				try {
					Double argnmbr = Double.parseDouble(args.replace(",",""));
                    String rtnmbr = (strnmbr - argnmbr) + "";
                    if (rtnmbr.endsWith(".0")) {rtnmbr = rtnmbr.substring(0,rtnmbr.length()-2);}
					if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, rtnmbr);}
					else {global.TMP_string.get(tmpstringnum).set(1, rtnmbr);}
				} catch (NumberFormatException e) {
					if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", args+" is not a number!"));}
				}
			} catch (NumberFormatException e) {
				if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", sn+" is not a number!"));}
			}
            return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("MULTIPLY") || func.equalsIgnoreCase("MULT") || func.equalsIgnoreCase("TIMES") || func.equalsIgnoreCase("*")) {
			try {
				Double strnmbr;
				if (stringnum!=-1) {strnmbr = Double.parseDouble(global.USR_string.get(stringnum).get(1).replace(",",""));}
				else {strnmbr = Double.parseDouble(global.TMP_string.get(tmpstringnum).get(1).replace(",",""));}
				try {
					Double argnmbr = Double.parseDouble(args.replace(",",""));
                    String rtnmbr = (strnmbr * argnmbr) + "";
                    if (rtnmbr.endsWith(".0")) {rtnmbr = rtnmbr.substring(0,rtnmbr.length()-2);}
					if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, rtnmbr);}
					else {global.TMP_string.get(tmpstringnum).set(1, rtnmbr);}
				} catch (NumberFormatException e) {
					if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", args+" is not a number!"));}
				}
			} catch (NumberFormatException e) {
				if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", sn+" is not a number!"));}
			}
            return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("DIVIDE") || func.equalsIgnoreCase("DIV") || func.equalsIgnoreCase("/")) {
			try {
				Double strnmbr;
				if (stringnum!=-1) {strnmbr = Double.parseDouble(global.USR_string.get(stringnum).get(1).replace(",",""));}
				else {strnmbr = Double.parseDouble(global.TMP_string.get(tmpstringnum).get(1).replace(",",""));}
				try {
					Double argnmbr = Double.parseDouble(args.replace(",",""));
                    String rtnmbr = (strnmbr / argnmbr) + "";
                    if (rtnmbr.endsWith(".0")) {rtnmbr = rtnmbr.substring(0,rtnmbr.length()-2);}
					if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, rtnmbr);}
					else {global.TMP_string.get(tmpstringnum).set(1, rtnmbr);}
				} catch (NumberFormatException e) {
					if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", args+" is not a number!"));}
				}
			} catch (NumberFormatException e) {
				if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", sn+" is not a number!"));}
			}
            return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("DIVIDEGETPERCENTAGE") || func.equalsIgnoreCase("DIVPERCENT") || func.equalsIgnoreCase("/%")) {
			try {
				Double strnmbr;
				if (stringnum!=-1) {strnmbr = Double.parseDouble(global.USR_string.get(stringnum).get(1).replace(",",""));}
				else {strnmbr = Double.parseDouble(global.TMP_string.get(tmpstringnum).get(1).replace(",",""));}
				try {
					Double argnmbr = Double.parseDouble(args.replace(",",""));
                    String rtnmbr = ((strnmbr / argnmbr) * 100) + "";
                    if (rtnmbr.endsWith(".0")) {rtnmbr = rtnmbr.substring(0,rtnmbr.length()-2);}
					if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, rtnmbr);}
					else {global.TMP_string.get(tmpstringnum).set(1, rtnmbr);}
				} catch (NumberFormatException e) {
					if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", args+" is not a number!"));}
				}
			} catch (NumberFormatException e) {
				if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", sn+" is not a number!"));}
			}
            return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("POW") || func.equalsIgnoreCase("POWER") || func.equalsIgnoreCase("^")) {
			try {
				Double strnmbr;
				if (stringnum!=-1) {strnmbr = Double.parseDouble(global.USR_string.get(stringnum).get(1).replace(",",""));}
				else {strnmbr = Double.parseDouble(global.TMP_string.get(tmpstringnum).get(1).replace(",",""));}
				try {
					Double argnmbr = Double.parseDouble(args.replace(",",""));
                    String rtnmbr = Math.pow(strnmbr, argnmbr) + "";
                    if (rtnmbr.endsWith(".0")) {rtnmbr = rtnmbr.substring(0,rtnmbr.length()-2);}
					if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, rtnmbr);}
					else {global.TMP_string.get(tmpstringnum).set(1, rtnmbr);}
				} catch (NumberFormatException e) {
					if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", args+" is not a number!"));}
				}
			} catch (NumberFormatException e) {
				if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", sn+" is not a number!"));}
			}
            return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("MODULUS") || func.equalsIgnoreCase("MOD") || func.equalsIgnoreCase("%")) {
			try {
				Double strnmbr;
				if (stringnum!=-1) {strnmbr = Double.parseDouble(global.USR_string.get(stringnum).get(1).replace(",",""));}
				else {strnmbr = Double.parseDouble(global.TMP_string.get(tmpstringnum).get(1).replace(",",""));}
				try {
					Double argnmbr = Double.parseDouble(args.replace(",",""));
                    String rtnmbr = (strnmbr % argnmbr) + "";
                    if (rtnmbr.endsWith(".0")) {rtnmbr = rtnmbr.substring(0,rtnmbr.length()-2);}
					if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, rtnmbr);}
					else {global.TMP_string.get(tmpstringnum).set(1, rtnmbr);}
				} catch (NumberFormatException e) {
					if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", args+" is not a number!"));}
				}
			} catch (NumberFormatException e) {
				if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", sn+" is not a number!"));}
			}
            return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("ABSOLUTE") || func.equalsIgnoreCase("ABS")) {
			try {
                Double argnmbr;
                if (stringnum!=-1) {argnmbr = Double.parseDouble(global.USR_string.get(stringnum).get(1).replace(",",""));}
                else {argnmbr = Double.parseDouble(global.TMP_string.get(tmpstringnum).get(1).replace(",",""));}
                String rtnmbr = Math.abs(argnmbr) + "";
                if (rtnmbr.endsWith(".0")) {rtnmbr = rtnmbr.substring(0,rtnmbr.length()-2);}
				if (stringnum!=-1) {global.USR_string.get(stringnum).set(1,rtnmbr);}
				else {global.TMP_string.get(tmpstringnum).set(1,rtnmbr);}
			} catch (NumberFormatException e) {
				if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", sn+" is not a number!"));}
			}
            return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("ROUND")) {
            try {
                Float strnmbr;
                if (stringnum!=-1) {strnmbr = Float.parseFloat(global.USR_string.get(stringnum).get(1).replace(",",""));}
                else {strnmbr = Float.parseFloat(global.TMP_string.get(tmpstringnum).get(1).replace(",",""));}
                try {
                    int argnmbr = Integer.parseInt(args.replace(",",""));
                    Double rtnmbr = Math.round(strnmbr * Math.pow(10,argnmbr)) / Math.pow(10,argnmbr);
                    String rtstrng = rtnmbr + "";
                    if (stringnum!=-1) {global.USR_string.get(stringnum).set(1,rtstrng);}
                    else {global.TMP_string.get(tmpstringnum).set(1,rtstrng);}
                } catch (NumberFormatException e) {
                    if (global.debug) {
                        ChatHandler.warn(ChatHandler.color("gray", args + " is not a number!"));
                    }
                }
            } catch (NumberFormatException e) {
                if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", sn+" is not a number!"));}
            }
            return "{string["+sn+"]}";
        } else if (func.equalsIgnoreCase("REPLACE")) {
			args = args
					.replace("'('", "stringOpenBracketF6cyUQp9stringOpenBracket")
					.replace("')'", "stringCloseBracketF6cyUQp9stringCloseBracket")
                    .replace("','", "stringCommaReplacementF6cyUQp9stringCommaReplacement");
			if (args.contains(",")) {
				String replaced = args.substring(0, args.indexOf(","));
				String replacer = args.substring(args.indexOf(",")+1);

				if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1)
						.replace("'('", "stringOpenBracketF6cyUQp9stringOpenBracket")
						.replace("')'", "stringCloseBracketF6cyUQp9stringCloseBracket")
                        .replace("','", "stringCommaReplacementF6cyUQp9stringCommaReplacement")
                        .replace(replaced, replacer));}
				else {global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1)
						.replace("'('", "stringOpenBracketF6cyUQp9stringOpenBracket")
						.replace("')'", "stringCloseBracketF6cyUQp9stringCloseBracket")
                        .replace("','", "stringCommaReplacementF6cyUQp9stringCommaReplacement")
                        .replace(replaced, replacer));}
				return "{string["+sn+"]}";
			} else {
				if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1)
						.replace("(","stringOpenBracketF6cyUQp9stringOpenBracket")
						.replace(")","stringCloseBracketF6cyUQp9stringCloseBracket")
                        .replace(",", "stringCommaReplacementF6cyUQp9stringCommaReplacement")
                        .replace(args, ""));}
				else {global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1)
						.replace("(","stringOpenBracketF6cyUQp9stringOpenBracket")
						.replace(")","stringCloseBracketF6cyUQp9stringCloseBracket")
                        .replace(",", "stringCommaReplacementF6cyUQp9stringCommaReplacement")
                        .replace(args, ""));}
				return "{string["+sn+"]}";
			}
		} else if (func.equalsIgnoreCase("SUBSTRING")) {
            args = args
					.replace("'('", "stringOpenBracketF6cyUQp9stringOpenBracket")
					.replace("')'", "stringCloseBracketF6cyUQp9stringCloseBracket")
					.replace("','", "stringCommaReplacementF6cyUQp9stringCommaReplacement");
            String[] subargs = args.split(",");
            if (subargs.length == 2) {
                try {
                    if (stringnum!=-1) {
                        String temp = global.USR_string.get(stringnum).get(1);
                        int num1 = Integer.parseInt(subargs[0]);
                        subargs[1] = subargs[1].replace("<end>",temp.length()+"").replace("<e>",temp.length()+"");
                        int num2 = Integer.parseInt(subargs[1]);
                        if (num1 >= 0 && num1 < num2) {
                            temp = temp.substring(num1, num2);
                            global.USR_string.get(stringnum).set(1, temp);
                        }
                    } else {
                        String temp = global.TMP_string.get(stringnum).get(1);
                        int num1 = Integer.parseInt(subargs[0]);
                        subargs[1] = subargs[1].replace("<end>",temp.length()+"").replace("<e>",temp.length()+"");
                        int num2 = Integer.parseInt(subargs[1]);
                        if (num1 >= 0 && num1 < num2) {
                            temp = temp.substring(num1, num2);
                            global.TMP_string.get(stringnum).set(1, temp);
                        }
                    }
                } catch (NumberFormatException e) {
                    if (stringnum!=-1) {
                        String temp = global.USR_string.get(stringnum).get(1)
                                .replace("(","stringOpenBracketF6cyUQp9stringOpenBracket")
                                .replace(")","stringCloseBracketF6cyUQp9stringCloseBracket")
                                .replace(",", "stringCommaReplacementF6cyUQp9stringCommaReplacement");
                        if (temp.contains(subargs[0])) {
                            temp = temp.substring(temp.indexOf(subargs[0]) + subargs[0].length());
                            if (temp.contains(subargs[1])) {
                                temp = temp.substring(0, temp.indexOf(subargs[1]));
                                global.USR_string.get(stringnum).set(1, temp
                                        .replace("stringOpenBracketF6cyUQp9stringOpenBracket","(")
                                        .replace("stringCloseBracketF6cyUQp9stringCloseBracket",")")
                                        .replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
                            } else {global.USR_string.get(stringnum).set(1, "false");}
                            if (global.debug) {ChatHandler.warn(ChatHandler.color("gray","Did not find " + subargs[1] + " in string"));}
                        } else {
                            global.USR_string.get(stringnum).set(1, "false");
                            if (global.debug) {ChatHandler.warn(ChatHandler.color("gray","Did not find " + subargs[0] + " in string"));}
                        }
                    } else {
                        String temp = global.TMP_string.get(tmpstringnum).get(1)
                                .replace("(","stringOpenBracketF6cyUQp9stringOpenBracket")
                                .replace(")","stringCloseBracketF6cyUQp9stringCloseBracket")
                                .replace(",", "stringCommaReplacementF6cyUQp9stringCommaReplacement");
                        if (temp.contains(subargs[0])) {
                            temp = temp.substring(temp.indexOf(subargs[0]) + subargs[0].length());
                            if (temp.contains(subargs[1])) {
                                temp = temp.substring(0, temp.indexOf(subargs[1]));
                                global.TMP_string.get(tmpstringnum).set(1, temp
                                        .replace("stringOpenBracketF6cyUQp9stringOpenBracket","(")
                                        .replace("stringCloseBracketF6cyUQp9stringCloseBracket",")")
                                        .replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
                            } else {global.TMP_string.get(tmpstringnum).set(1, "false");
                                if (global.debug) {ChatHandler.warn(ChatHandler.color("gray","Did not find " + subargs[1] + " in string"));}}
                        } else {global.TMP_string.get(tmpstringnum).set(1, "false");
                            if (global.debug) {ChatHandler.warn(ChatHandler.color("gray","Did not find " + subargs[1] + " in string"));}}
                    }
                }
                return "{string["+sn+"]}";
            } else {
				if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", "Missing args in .SUBSTRING! expected 2, got " + subargs.length));}
				return "{string["+sn+"]}";
			}
        } else if (func.equalsIgnoreCase("TRIM")){
            if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1).trim());}
            else {global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1).trim());}
            return "{string["+sn+"]}";
        } else if (func.equalsIgnoreCase("PREFIX")) {
			if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, args + global.USR_string.get(stringnum).get(1));}
			else {global.TMP_string.get(tmpstringnum).set(1, args + global.TMP_string.get(tmpstringnum).get(1));}
			return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("SUFFIX")) {
			if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1) + args);}
			else {global.TMP_string.get(tmpstringnum).set(1, args + global.TMP_string.get(tmpstringnum).get(1));}
			return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("TOUPPER") || func.equalsIgnoreCase("TOUPPERCASE")) {
			if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1).toUpperCase());}
			else {global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1).toUpperCase());}
			return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("TOLOWER") || func.equalsIgnoreCase("TOLOWERCASE")) {
			if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1).toLowerCase());}
			else {global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1).toLowerCase());}
			return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("REMOVEFORMATTING") || func.equalsIgnoreCase("REMFORMATTING") || func.equalsIgnoreCase("REMOVEFORM") || func.equalsIgnoreCase("REMFORM")) {
			if (stringnum!=-1) {
				global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1)
						.replace("&0","").replace("&1","").replace("&2","").replace("&3","").replace("&4","").replace("&5","").replace("&6","").replace("&7","").replace("&8","").replace("&9","").replace("&a","").replace("&b","").replace("&c","").replace("&d","").replace("&e","").replace("&f","")
						.replace("&k", "").replace("&l", "").replace("&m", "").replace("&n", "").replace("&o", "").replace("&r", ""));
			} else {
				global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1)
						.replace("&0","").replace("&1","").replace("&2","").replace("&3","").replace("&4","").replace("&5","").replace("&6","").replace("&7","").replace("&8","").replace("&9","").replace("&a","").replace("&b","").replace("&c","").replace("&d","").replace("&e","").replace("&f","")
						.replace("&k", "").replace("&l", "").replace("&m", "").replace("&n", "").replace("&o", "").replace("&r", ""));
			}
			return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("IMPORTJSONFILE")){
			String sj = "Improper format! use importJsonFile(file,node)";
			if (args.contains(",")) {
				sj = JsonHandler.importJsonFile("string", args.substring(0, args.indexOf(",")), sn + "=>" + args.substring(args.indexOf(",")+1));
				if (stringnum!=-1) {
					global.USR_string.get(stringnum).set(1, sj);
					global.backupUSR_strings.get(stringnum).set(1, sj);
				} else {
					global.TMP_string.get(tmpstringnum).set(1, sj);
					global.backupTMP_strings.get(tmpstringnum).set(1, sj);
				}
			} else {
				if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, sj);}
				else {global.TMP_string.get(tmpstringnum).set(1, sj);}
			}
			return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("IMPORTJSONURL")) {
			String sj = "Improper format! use importJsonFile(file,node)";
			if (args.contains(",")) {
				sj = JsonHandler.importJsonURL("string", args.substring(0, args.indexOf(",")), sn + "=>" + args.substring(args.indexOf(",")+1));
				if (stringnum!=-1) {
					global.USR_string.get(stringnum).set(1, sj);
					global.backupUSR_strings.get(stringnum).set(1, sj);
				} else {
					global.TMP_string.get(tmpstringnum).set(1, sj);
					global.backupTMP_strings.get(tmpstringnum).set(1, sj);
				}
			} else {
				if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, sj);}
				else {global.TMP_string.get(tmpstringnum).set(1, sj);}
			}
			return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("EQUALS") || (func.equalsIgnoreCase("="))) {
			if (stringnum!=-1) {
				if (global.USR_string.get(stringnum).get(1).equals(args)) {
					global.USR_string.get(stringnum).set(1, "true");
				} else {global.USR_string.get(stringnum).set(1, "false");}
			} else {
				if (global.TMP_string.get(tmpstringnum).get(1).equals(args)) {
					global.TMP_string.get(tmpstringnum).set(1, "true");
				} else {global.TMP_string.get(tmpstringnum).set(1, "false");}
			}
			return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("EQUALSIGNORECASE")) {
			if (stringnum!=-1) {
				if (global.USR_string.get(stringnum).get(1).equalsIgnoreCase(args)) {
					global.USR_string.get(stringnum).set(1, "true");
				} else {global.USR_string.get(stringnum).set(1, "false");}
			} else {
				if (global.TMP_string.get(tmpstringnum).get(1).equalsIgnoreCase(args)) {
					global.TMP_string.get(tmpstringnum).set(1, "true");
				} else {global.TMP_string.get(tmpstringnum).set(1, "false");}
			}
			return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("STARTSWITH")) {
			if (stringnum!=-1) {
				if (global.USR_string.get(stringnum).get(1).startsWith(args)) {
					global.USR_string.get(stringnum).set(1, "true");
				} else {global.USR_string.get(stringnum).set(1, "false");}
			} else {
				if (global.TMP_string.get(tmpstringnum).get(1).startsWith(args)) {
					global.TMP_string.get(tmpstringnum).set(1, "true");
				} else {global.TMP_string.get(tmpstringnum).set(1, "false");}
			}
			return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("STARTSWITHIGNORECASE")) {
			if (stringnum!=-1) {
				if (global.USR_string.get(stringnum).get(1).toUpperCase().startsWith(args.toUpperCase())) {
					global.USR_string.get(stringnum).set(1, "true");
				} else {global.USR_string.get(stringnum).set(1, "false");}
			} else {
				if (global.TMP_string.get(tmpstringnum).get(1).toUpperCase().startsWith(args.toUpperCase())) {
					global.TMP_string.get(tmpstringnum).set(1, "true");
				} else {global.TMP_string.get(tmpstringnum).set(1, "false");}
			}
			return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("CONTAINS")) {
			if (stringnum!=-1) {
				if (global.USR_string.get(stringnum).get(1).contains(args)) {
					global.USR_string.get(stringnum).set(1, "true");
				} else {global.USR_string.get(stringnum).set(1, "false");}
			} else {
				if (global.TMP_string.get(tmpstringnum).get(1).contains(args)) {
					global.TMP_string.get(tmpstringnum).set(1, "true");
				} else {global.TMP_string.get(tmpstringnum).set(1, "false");}
			}
			return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("CONTAINSIGNORECASE")) {
			if (stringnum!=-1) {
				if (global.USR_string.get(stringnum).get(1).toUpperCase().contains(args.toUpperCase())) {
					global.USR_string.get(stringnum).set(1, "true");
				} else {global.USR_string.get(stringnum).set(1, "false");}
			} else {
				if (global.TMP_string.get(tmpstringnum).get(1).toUpperCase().contains(args.toUpperCase())) {
					global.TMP_string.get(tmpstringnum).set(1, "true");
				} else {global.TMP_string.get(tmpstringnum).set(1, "false");}
			}
			return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("ENDSWITH")) {
			if (stringnum!=-1) {
				if (global.USR_string.get(stringnum).get(1).endsWith(args)) {
					global.USR_string.get(stringnum).set(1, "true");
				} else {global.USR_string.get(stringnum).set(1, "false");}
			} else {
				if (global.TMP_string.get(tmpstringnum).get(1).endsWith(args)) {
					global.TMP_string.get(tmpstringnum).set(1, "true");
				} else {global.TMP_string.get(tmpstringnum).set(1, "false");}
			}
			return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("ENDSWITHIGNORECASE")) {
			if (stringnum!=-1) {
				if (global.USR_string.get(stringnum).get(1).toUpperCase().endsWith(args.toUpperCase())) {
					global.USR_string.get(stringnum).set(1, "true");
				} else {global.USR_string.get(stringnum).set(1, "false");}
			} else {
				if (global.TMP_string.get(tmpstringnum).get(1).toUpperCase().endsWith(args.toUpperCase())) {
					global.TMP_string.get(tmpstringnum).set(1, "true");
				} else {global.TMP_string.get(tmpstringnum).set(1, "false");}
			}
			return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("GREATERTHAN") || func.equalsIgnoreCase("GT") || func.equalsIgnoreCase(">")) {
			try {
				int strnmbr;
				if (stringnum!=-1) {strnmbr = Integer.parseInt(global.USR_string.get(stringnum).get(1));}
				else {strnmbr = Integer.parseInt(global.TMP_string.get(tmpstringnum).get(1));}
				try {
					int argnmbr = Integer.parseInt(args);
					if (stringnum!=-1) {
						if (strnmbr>argnmbr) {global.USR_string.get(stringnum).set(1, "true");}
						else {global.USR_string.get(stringnum).set(1, "false");}
					} else {
						if (strnmbr>argnmbr) {global.TMP_string.get(tmpstringnum).set(1, "true");}
						else {global.TMP_string.get(tmpstringnum).set(1, "false");}
					}
					return "{string["+sn+"]}";
				} catch (NumberFormatException e) {
					if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", args+" is not a number!"));}
					return "{string["+sn+"]}";
				}
			} catch (NumberFormatException e) {
				if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", sn+" is not a number!"));}
				return "{string["+sn+"]}";
			}
		} else if (func.equalsIgnoreCase("LESSTHAN") || func.equalsIgnoreCase("LT") || func.equalsIgnoreCase("<")) {
			try {
				int strnmbr;
				if (stringnum!=-1) {strnmbr = Integer.parseInt(global.USR_string.get(stringnum).get(1));}
				else {strnmbr = Integer.parseInt(global.TMP_string.get(tmpstringnum).get(1));}
				try {
					int argnmbr = Integer.parseInt(args);
					if (stringnum!=-1) {
						if (strnmbr<argnmbr) {global.USR_string.get(stringnum).set(1, "true");}
						else {global.USR_string.get(stringnum).set(1, "false");}
					} else {
						if (strnmbr<argnmbr) {global.TMP_string.get(tmpstringnum).set(1, "true");}
						else {global.TMP_string.get(tmpstringnum).set(1, "false");}
					}
					return "{string["+sn+"]}";
				} catch (NumberFormatException e) {
					if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", args+" is not a number!"));}
					return "{string["+sn+"]}";
				}
			} catch (NumberFormatException e) {
				if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", sn+" is not a number!"));}
				return "{string["+sn+"]}";
			}
		} else if (func.equalsIgnoreCase("GREATERTHANOREQUALTO") || func.equalsIgnoreCase("GTE") || func.equalsIgnoreCase(">=")) {
			try {
				int strnmbr;
				if (stringnum!=-1) {strnmbr = Integer.parseInt(global.USR_string.get(stringnum).get(1));}
				else {strnmbr = Integer.parseInt(global.TMP_string.get(tmpstringnum).get(1));}
				try {
					int argnmbr = Integer.parseInt(args);
					if (stringnum!=-1) {
						if (strnmbr>=argnmbr) {global.USR_string.get(stringnum).set(1, "true");}
						else {global.USR_string.get(stringnum).set(1, "false");}
					} else {
						if (strnmbr>=argnmbr) {global.TMP_string.get(tmpstringnum).set(1, "true");}
						else {global.TMP_string.get(tmpstringnum).set(1, "false");}
					}
					return "{string["+sn+"]}";
				} catch (NumberFormatException e) {
					if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", args+" is not a number!"));}
					return "{string["+sn+"]}";
				}
			} catch (NumberFormatException e) {
				if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", sn+" is not a number!"));}
				return "{string["+sn+"]}";
			}
		} else if (func.equalsIgnoreCase("LESSTHANOREQUALTO") || func.equalsIgnoreCase("LTE") || func.equalsIgnoreCase("<=")) {
			try {
				int strnmbr;
				if (stringnum!=-1) {strnmbr = Integer.parseInt(global.USR_string.get(stringnum).get(1));}
				else {strnmbr = Integer.parseInt(global.TMP_string.get(tmpstringnum).get(1));}
				try {
					int argnmbr = Integer.parseInt(args);
					if (stringnum!=-1) {
						if (strnmbr<=argnmbr) {global.USR_string.get(stringnum).set(1, "true");}
						else {global.USR_string.get(stringnum).set(1, "false");}
					} else {
						if (strnmbr<=argnmbr) {global.TMP_string.get(tmpstringnum).set(1, "true");}
						else {global.TMP_string.get(tmpstringnum).set(1, "false");}
					}
					return "{string["+sn+"]}";
				} catch (NumberFormatException e) {
					if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", args+" is not a number!"));}
					return "{string["+sn+"]}";
				}
			} catch (NumberFormatException e) {
				if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", sn+" is not a number!"));}
				return "{string["+sn+"]}";
			}
		} else if (func.equalsIgnoreCase("CAPITALIZEFIRSTWORD") || func.equalsIgnoreCase("CAPFIRT")) {
			if (stringnum!=-1) {
				global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1).substring(0, 1).toUpperCase() + global.USR_string.get(stringnum).get(1).substring(1));
			} else {
				global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1).substring(0, 1).toUpperCase() + global.TMP_string.get(tmpstringnum).get(1).substring(1));
			}
			return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("CAPITALIZEALLWORDS") || func.equalsIgnoreCase("CAPALL")) {
			if (stringnum!=-1) {
				global.USR_string.get(stringnum).set(1, WordUtils.capitalize(global.USR_string.get(stringnum).get(1)));
			} else {
				global.TMP_string.get(tmpstringnum).set(1, WordUtils.capitalize(global.TMP_string.get(tmpstringnum).get(1)));
			}
			return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("IGNOREESCAPE")) {
			if (stringnum!=-1) {
				global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1).replace("\\", "\\\\"));
			} else {
				global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1).replace("\\", "\\\\"));
			}
			return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("LENGTH")) {
            if (stringnum!=-1) {
                global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1).length()+"");
            } else {
                global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1).length()+"");
            }
            return "{string["+sn+"]}";
        } else if (func.equalsIgnoreCase("SIZE")) {
            if (stringnum!=-1) {
                global.USR_string.get(stringnum).set(1, Minecraft.getMinecraft().fontRendererObj.getStringWidth(global.USR_string.get(stringnum).get(1))+"");
            } else {
                global.TMP_string.get(tmpstringnum).set(1, Minecraft.getMinecraft().fontRendererObj.getStringWidth(global.TMP_string.get(tmpstringnum).get(1))+"");
            }
            return "{string["+sn+"]}";
        } else if (func.equalsIgnoreCase("FIXLINKS")) {
            String tmp_string;
            if (stringnum!=-1) {
                tmp_string = global.USR_string.get(stringnum).get(1);
            } else {
                tmp_string = global.TMP_string.get(tmpstringnum).get(1);
            }

            String[] split_tmp_string = tmp_string.split(" ");
            for (String value : split_tmp_string) {
                String newvalue = ChatHandler.deleteFormatting(value);
                if (value.contains(".")) {
                    if (!(newvalue.toUpperCase().startsWith("HTTP://") || newvalue.toUpperCase().startsWith("HTTPS://"))) {
                        newvalue = "http://"+value;
                    }
                    tmp_string = tmp_string.replace(value, "{link[" + value + "],[" + newvalue + "]}");
                }
            }

            if (stringnum!=-1) {
                global.USR_string.get(stringnum).set(1,tmp_string);
            } else {
                global.TMP_string.get(tmpstringnum).set(1,tmp_string);
            }
            return "{string["+sn+"]}";
        } else {
            for (List<String> function : global.function) {
                if (function.size() > 2) {
                    String func_define = function.get(1);
                    if (func_define.contains(".") && func_define.contains("(") && func_define.contains(")")) {
                        String func_name = func_define.substring(func_define.indexOf(".")+1, func_define.indexOf("(", func_define.indexOf(".")));
                        if (func_name.equals(func)) {
                            String func_to = func_define.substring(0, func_define.indexOf("."));
                            String func_arg = func_define.substring(func_define.indexOf("(")+1, func_define.indexOf(")", func_define.indexOf(")")));
                            if (func_arg.contains(",")) {
                                String[] func_args = func_arg.split(",");
                                String[] func_args_to = args.split(",");
                                if (func_args.length == func_args_to.length) {
                                    List<String> TMP_events = new ArrayList<String>();
                                    for (int j = 2; j < function.size(); j++) {
                                        TMP_events.add(function.get(j));
                                    }
                                    if (stringnum != -1) {
                                        String ret = EventsHandler.doEvents(TMP_events, chatEvent, global.append(func_args, func_to), global.append(func_args_to, global.USR_string.get(stringnum).get(1)));
                                        global.USR_string.get(stringnum).set(1, ret);
                                    } else {
                                        String ret = EventsHandler.doEvents(TMP_events, chatEvent, global.append(func_args, func_to), global.append(func_args_to, global.TMP_string.get(tmpstringnum).get(1)));
                                        global.TMP_string.get(tmpstringnum).set(1, ret);
                                    }
                                    return "{string[" + sn + "]}";
                                }
                            } else {
                                List<String> TMP_events = new ArrayList<String>();
                                for (int j = 2; j < function.size(); j++) {
                                    TMP_events.add(function.get(j));
                                }
                                if (stringnum != -1) {
                                    String ret = EventsHandler.doEvents(TMP_events, chatEvent, new String[] {func_to}, new String[] {global.USR_string.get(stringnum).get(1)});
                                    global.USR_string.get(stringnum).set(1, ret);
                                } else {
                                    String ret = EventsHandler.doEvents(TMP_events, chatEvent, new String[] {func_to}, new String[] {global.TMP_string.get(tmpstringnum).get(1)});
                                    global.TMP_string.get(tmpstringnum).set(1, ret);
                                }
                                return "{string[" + sn + "]}";
                            }
                        }
                    }
                }
            }
            if (global.debug) {
                ChatHandler.warn(ChatHandler.color("gray", func + " is not a function!"));
            }
            return "{string[" + sn + "]}";
		}
	}
	
	static String stringFunctions(String TMP_e, ClientChatReceivedEvent chatEvent) {
		TMP_e = TMP_e.replace("'('", "stringOpenBracketReplacementF6cyUQp9stringOpenBracketReplacement")
				.replace("')'", "stringCloseBracketReplacementF6cyUQp9stringCloseBracketReplacement");
		while (TMP_e.contains("{string[") && TMP_e.contains("]}")) {
			String testfor = TMP_e.substring(TMP_e.indexOf("]}", TMP_e.indexOf("{string["))+2);
			if (testfor.contains("]}.") && !(testfor.contains("{string[") || testfor.contains("{array["))) {
				if (testfor.indexOf("]}.") < testfor.indexOf("(")) {testfor = "."+testfor.substring(testfor.indexOf("]}.")+3);}
			}
			if (testfor.startsWith(".") && testfor.contains("(") && testfor.contains(")")) {
				if (testfor.substring(testfor.indexOf("."), testfor.indexOf("(")).contains(" ")) {testfor = testfor.substring(1);}
			}
			if (testfor.startsWith(".") && testfor.contains("(") && testfor.contains(")")) {
				String sn = TMP_e.substring(TMP_e.indexOf("{string[")+8, TMP_e.indexOf("]}.", TMP_e.indexOf("{string[")));
				String func = TMP_e.substring(TMP_e.indexOf("]}.", TMP_e.indexOf("{string["))+3, TMP_e.indexOf("(", TMP_e.indexOf("]}.", TMP_e.indexOf("{string["))));
				String args = TMP_e.substring(TMP_e.indexOf("(", TMP_e.indexOf("]}.", TMP_e.indexOf("{string[")))+1, TMP_e.indexOf(")",  TMP_e.indexOf("(", TMP_e.indexOf("]}.", TMP_e.indexOf("{string[")))));
				while (sn.contains("{string[")) {
					sn = TMP_e.substring(TMP_e.indexOf("{string[")+8, TMP_e.indexOf("]}", TMP_e.indexOf(sn)+sn.length()));
					String first = sn.substring(0, sn.indexOf("{string["));
					String replacement = "stringOpenStringF6cyUQp9stringOpenString";
					String second = sn.substring(sn.indexOf("{string[")+8);
					String efirst = TMP_e.substring(0, TMP_e.indexOf("{string[", TMP_e.indexOf("{string[")+8));
					String esecond = TMP_e.substring(TMP_e.indexOf("{string[", TMP_e.indexOf("{string[")+8)+8);
					TMP_e = efirst + replacement + esecond;
					sn = first + replacement + second;
				}
				TMP_e = TMP_e.replace("stringOpenStringF6cyUQp9stringOpenString", "{string[");
				sn = sn.replace("stringOpenStringF6cyUQp9stringOpenString", "{string[");
				
				String efirst = TMP_e.substring(0, TMP_e.indexOf(sn));
				String esecond = TMP_e.substring(TMP_e.indexOf(sn)+sn.length());
				sn = stringFunctions(sn, chatEvent);
				
				TMP_e = efirst + sn + esecond;
				sn = sn.replace("stringOpenStringF6cyUQp9stringOpenString", "{string[");
				while (args.contains("(")) {
					try {
                        args = TMP_e.substring(TMP_e.indexOf(args), TMP_e.indexOf(")", TMP_e.indexOf(")") + 1));

                        String first;
                        String second;
                        String argsbefore;

                        first = args.substring(0, args.indexOf("("));
                        second = args.substring(args.indexOf("(") + 1);
                        argsbefore = args;
                        args = first + "stringOpenBracketF6cyUQp9stringOpenBracket" + second;
                        TMP_e = TMP_e.replace(argsbefore, args);
                        first = args.substring(0, args.indexOf(")"));
                        second = args.substring(args.indexOf(")") + 1);
                        argsbefore = args;
                        args = first + "stringCloseBracketF6cyUQp9stringCloseBracket" + second;
                        TMP_e = TMP_e.replace(argsbefore, args);
                    } catch (Exception e) {
                        e.printStackTrace();
                        ChatHandler.warn(ChatHandler.color("red","ERR: Missing closing bracket!"));
                        return "Something broke!!";
                    }
				}
				args = args.replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(").replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")");
				TMP_e = TMP_e.replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(").replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")");
				String fullreplace = "{string["+sn+"]}."+func+"("+args+")";
				String firstpart = TMP_e.substring(0, TMP_e.indexOf(fullreplace));
				String secondpart = TMP_e.substring(TMP_e.indexOf(fullreplace)+fullreplace.length());
				
				TMP_e = firstpart + StringHandler.doStringFunctions(sn,func,args, chatEvent) + secondpart;
			} else {
				String sn = TMP_e.substring(TMP_e.indexOf("{string[")+8, TMP_e.indexOf("]}", TMP_e.indexOf("{string[")));
				while (sn.contains("{string[")) {
					sn = TMP_e.substring(TMP_e.indexOf("{string[")+8, TMP_e.indexOf("]}", TMP_e.indexOf(sn)+sn.length()+2));
					
					String first = sn.substring(0, sn.indexOf("{string["));
					String replacement = "stringOpenStringF6cyUQp9stringOpenString";
					String second = sn.substring(sn.indexOf("{string[")+8);
					String efirst = TMP_e.substring(0, TMP_e.indexOf("{string[", TMP_e.indexOf("{string[")+8));
					String ereplacement = "stringOpenStringF6cyUQp9stringOpenString";
					String esecond = TMP_e.substring(TMP_e.indexOf("{string[", TMP_e.indexOf("{string[")+8)+8);
					
					TMP_e = efirst + ereplacement + esecond;
					sn = first + replacement + second;
				}
				TMP_e = TMP_e.replace("stringOpenStringF6cyUQp9stringOpenString", "{string[");
				sn = sn.replace("stringOpenStringF6cyUQp9stringOpenString", "{string[");
				
				String efirst = TMP_e.substring(0, TMP_e.indexOf(sn));
				String esecond = TMP_e.substring(TMP_e.indexOf(sn)+sn.length());
				sn = stringFunctions(sn, chatEvent);
				
				TMP_e = efirst + sn + esecond;
				
				String returnString = "Not a string!";
				
				for (int i=0; i<global.USR_string.size(); i++) {
					if (global.USR_string.get(i).get(0).equals(sn)) {returnString = global.USR_string.get(i).get(1);}
				}
				if (returnString.equals("Not a string!")) {
					for (int i=0; i<global.TMP_string.size(); i++) {
						if (global.TMP_string.get(i).get(0).equals(sn)) {returnString = global.TMP_string.get(i).get(1);}
					}
				}
				
				String fullreplace = "{string["+sn+"]}";
				String firstpart = TMP_e.substring(0, TMP_e.indexOf(fullreplace));
				String secondpart = TMP_e.substring(TMP_e.indexOf(fullreplace)+fullreplace.length());
				
				TMP_e = firstpart + 
						returnString.replace(",", "stringCommaReplacementF6cyUQp9stringCommaReplacement")
						.replace("(", "stringOpenBracketF6cyUQp9stringOpenBracket")
						.replace(")", "stringCloseBracketF6cyUQp9stringCloseBracket")
						+ secondpart;
				
				global.USR_string.clear();
				for (int i=0; i<global.backupUSR_strings.size(); i++) {
					String first = global.backupUSR_strings.get(i).get(0);
					String second = global.backupUSR_strings.get(i).get(1);
					List<String> temporary = new ArrayList<String>();
					temporary.add(first);
					temporary.add(second);
					global.USR_string.add(temporary);
				}
				global.TMP_string.clear();
				for (int i=0; i<global.backupTMP_strings.size(); i++) {
					String first = global.backupTMP_strings.get(i).get(0);
					String second = global.backupTMP_strings.get(i).get(1);
					List<String> temporary = new ArrayList<String>();
					temporary.add(first);
					temporary.add(second);
					global.TMP_string.add(temporary);
				}
			}
		}
		return TMP_e;
	}
	
	static String setStrings(String msg, String trig) {
		//some magic shit is going on here
		//trust me, this used to be a LOT worse and only sort of kinda worked
		//it works pretty well now
		if (trig.contains("{string[") && trig.contains("]") && trig.contains("}")) {
			trig = trig.replace("}", "{");
			String[] split_trig = trig.split("\\{");
			for (int i=0; i<split_trig.length; i++) {
				if (split_trig[i].startsWith("string[") && split_trig[i].contains("]")) {
					String stringName = split_trig[i].substring(split_trig[i].indexOf("string[")+7, split_trig[i].indexOf("]"));
					int stringLength = 0;
					List<String> stringListSplit = new ArrayList<String>();
					Boolean checkCharacter = false;
					if (!split_trig[i].endsWith("]")) {
						try {
							stringLength = Integer.parseInt(split_trig[i].substring(split_trig[i].indexOf("]") + 1, split_trig[i].length()));
						} catch (NumberFormatException e) {
							String[] stringCheckSplit = split_trig[i].substring(split_trig[i].indexOf("]")+1, split_trig[i].length()).split(",") ;
                            stringListSplit.addAll(Arrays.asList(stringCheckSplit));

							if (stringListSplit.get(0).startsWith("c:")) {
								checkCharacter=true;
								stringListSplit.set(0,stringListSplit.get(0).replace("c:", ""));
							}
							stringLength = -1;
						}
					}
					if (i==0) {
						if (msg.contains(split_trig[i+1])) {
							int stringnum = -1;
							int tmpstringnum = -1;
							
							for (int j=0; j<global.USR_string.size(); j++) {
								if (global.USR_string.get(j).get(0).equals(stringName)) {
									stringnum = j;
								}
							}
							if (stringnum==-1) {
								List<String> temporary = new ArrayList<String>();
								String tmpnum = global.TMP_string.size()+"";
								temporary.add("TEMP-USER-STRING"+tmpnum+"->"+stringName);
								temporary.add("");
								global.TMP_string.add(temporary);
								tmpstringnum = global.TMP_string.size()-1;
								global.temporary_replace.add("{string["+stringName+"]");
								global.temporary_replacement.add("{string[TEMP-USER-STRING"+tmpnum+"->"+stringName+"]"); 
								global.temporary_replace.add("{string<"+stringName+">");
								global.temporary_replacement.add("{string[TEMP-USER-STRING"+tmpnum+"->"+stringName+"]"); 
							}
							
							
							String set_string = msg.substring(0, msg.indexOf(split_trig[i+1]));
							if (stringLength > 0) {
								int check_stringLength = set_string.length() - set_string.replace(" ", "").length() + 1;
								if (check_stringLength == stringLength) {
									if (stringnum!=-1) {
										global.USR_string.get(stringnum).set(1, set_string);
										split_trig[i] = global.USR_string.get(stringnum).get(1);
									} else {
										global.TMP_string.get(tmpstringnum).set(1, set_string);
										split_trig[i] = global.TMP_string.get(tmpstringnum).get(1);
									}
								}
							} else if (stringLength < 0) {
								for (String value : stringListSplit) {
									int check_stringLength;
									if (checkCharacter) {check_stringLength = set_string.length();}
                                    else {check_stringLength = set_string.length() - set_string.replace(" ", "").length() + 1;}

									try {
										if (check_stringLength == Integer.parseInt(value)) {
											if (stringnum!=-1) {
												global.USR_string.get(stringnum).set(1, set_string);
												split_trig[i] = global.USR_string.get(stringnum).get(1);
											} else {
												global.TMP_string.get(tmpstringnum).set(1, set_string);
												split_trig[i] = global.TMP_string.get(tmpstringnum).get(1);
											}
										}
									} catch (NumberFormatException e) {
										String[] fromtoSplit = value.split("-");
										try {
											int fromSplit = Integer.parseInt(fromtoSplit[0]);
											int toSplit = Integer.parseInt(fromtoSplit[fromtoSplit.length-1]);
											if (fromSplit <= check_stringLength && toSplit >= check_stringLength) {
												if (stringnum!=-1) {
													global.USR_string.get(stringnum).set(1, set_string);
													split_trig[i] = global.USR_string.get(stringnum).get(1);
												} else {
													global.TMP_string.get(tmpstringnum).set(1, set_string);
													split_trig[i] = global.TMP_string.get(tmpstringnum).get(1);
												}
											}
										} catch (NumberFormatException e1) {/*do nothing*/} catch (ArrayIndexOutOfBoundsException e1) {/*do nothing*/}
									}
								}
							} else {
								if (stringnum!=-1) {
									global.USR_string.get(stringnum).set(1, set_string);
									split_trig[i] = global.USR_string.get(stringnum).get(1);
								} else {
									global.TMP_string.get(tmpstringnum).set(1, set_string);
									split_trig[i] = global.TMP_string.get(tmpstringnum).get(1);
								}
							}
						}
					} else if (i==split_trig.length-1) {
						if (msg.contains(split_trig[i-1])) {
							int stringnum = -1;
							int tmpstringnum = -1;
							
							for (int j=0; j<global.USR_string.size(); j++) {
								if (global.USR_string.get(j).get(0).equals(stringName)) {
									stringnum = j;
								}
							}
							if (stringnum==-1) {
								List<String> temporary = new ArrayList<String>();
								String tmpnum = global.TMP_string.size()+"";
								temporary.add("TEMP-USER-STRING"+tmpnum+"->"+stringName);
								temporary.add("");
								global.TMP_string.add(temporary);
								tmpstringnum = global.TMP_string.size()-1;
								global.temporary_replace.add("{string["+stringName+"]");
								global.temporary_replacement.add("{string[TEMP-USER-STRING"+tmpnum+"->"+stringName+"]");
								global.temporary_replace.add("{string<"+stringName+">");
								global.temporary_replacement.add("{string[TEMP-USER-STRING"+tmpnum+"->"+stringName+"]");
							}
							
							String set_string = msg.substring(msg.indexOf(split_trig[i-1])+split_trig[i-1].length(), msg.length());
							if (stringLength > 0) {
								int check_stringLength = set_string.length() - set_string.replace(" ", "").length() + 1;
								if (check_stringLength == stringLength) {
									if (stringnum!=-1) {
										global.USR_string.get(stringnum).set(1, set_string);
										split_trig[i] = global.USR_string.get(stringnum).get(1);
									} else {
										global.TMP_string.get(tmpstringnum).set(1, set_string);
										split_trig[i] = global.TMP_string.get(tmpstringnum).get(1);
									}
								}
							} else if (stringLength < 0) {
								for (String value : stringListSplit) {
									int check_stringLength;
									if (checkCharacter) {
										check_stringLength = set_string.length();
									} else {
										check_stringLength = set_string.length() - set_string.replace(" ", "").length() + 1;
									}
									try {
										if (check_stringLength == Integer.parseInt(value)) {
											if (stringnum!=-1) {
												global.USR_string.get(stringnum).set(1, set_string);
												split_trig[i] = global.USR_string.get(stringnum).get(1);
											} else {
												global.TMP_string.get(tmpstringnum).set(1, set_string);
												split_trig[i] = global.TMP_string.get(tmpstringnum).get(1);
											}
										}
									} catch (NumberFormatException e) {
										String[] fromtoSplit = value.split("-");
										try {
											int fromSplit = Integer.parseInt(fromtoSplit[0]);
											int toSplit = Integer.parseInt(fromtoSplit[fromtoSplit.length-1]);
											if (fromSplit <= check_stringLength && toSplit >= check_stringLength) {
												if (stringnum!=-1) {
													global.USR_string.get(stringnum).set(1, set_string);
													split_trig[i] = global.USR_string.get(stringnum).get(1);
												} else {
													global.TMP_string.get(tmpstringnum).set(1, set_string);
													split_trig[i] = global.TMP_string.get(tmpstringnum).get(1);
												}
											}
										} catch (NumberFormatException e1) {/*do nothing*/} catch (ArrayIndexOutOfBoundsException e1) {/*do nothing*/}
									}
								}
							} else {
								if (stringnum!=-1) {
									global.USR_string.get(stringnum).set(1, set_string);
									split_trig[i] = global.USR_string.get(stringnum).get(1);
								} else {
									global.TMP_string.get(tmpstringnum).set(1, set_string);
									split_trig[i] = global.TMP_string.get(tmpstringnum).get(1);
								}
							}
						}
					} else {
						if (msg.contains(split_trig[i-1]) && msg.contains(split_trig[i+1])) {
							int stringnum = -1;
							int tmpstringnum = -1;
							
							for (int j=0; j<global.USR_string.size(); j++) {
								if (global.USR_string.get(j).get(0).equals(stringName)) {
									stringnum = j;
								}
							}
							if (stringnum==-1) {
								List<String> temporary = new ArrayList<String>();
								String tmpnum = global.TMP_string.size()+"";
								temporary.add("TEMP-USER-STRING"+tmpnum+"->"+stringName);
								temporary.add("");
								global.TMP_string.add(temporary);
								tmpstringnum = global.TMP_string.size()-1;
								global.temporary_replace.add("{string["+stringName+"]");
								global.temporary_replacement.add("{string[TEMP-USER-STRING"+tmpnum+"->"+stringName+"]");
								global.temporary_replace.add("{string<"+stringName+">");
								global.temporary_replacement.add("{string[TEMP-USER-STRING"+tmpnum+"->"+stringName+"]");
							}
							
							int checkbefore = msg.indexOf(split_trig[i-1])+split_trig[i-1].length();
							int checkafter;
							try {checkafter = msg.indexOf(split_trig[i+1], checkbefore);}
							catch (StringIndexOutOfBoundsException e) {checkafter=-1;}
							if (checkbefore < checkafter) {
								String set_string = msg.substring(checkbefore, checkafter);
								msg = msg.substring(checkbefore, msg.length());
								if (stringLength > 0) {
									int check_stringLength = set_string.length() - set_string.replace(" ", "").length() + 1;
									if (check_stringLength == stringLength) {
										if (stringnum!=-1) {
											global.USR_string.get(stringnum).set(1, set_string);
											split_trig[i] = global.USR_string.get(stringnum).get(1);
										} else {
											global.TMP_string.get(tmpstringnum).set(1, set_string);
											split_trig[i] = global.TMP_string.get(tmpstringnum).get(1);
										}
									}
								} else if (stringLength < 0) {
									for (String value : stringListSplit) {
										int check_stringLength;
										if (checkCharacter) {
											check_stringLength = set_string.length();
										} else {
											check_stringLength = set_string.length() - set_string.replace(" ", "").length() + 1;
										}
										try {
											if (check_stringLength == Integer.parseInt(value)) {
												if (stringnum!=-1) {
													global.USR_string.get(stringnum).set(1, set_string);
													split_trig[i] = global.USR_string.get(stringnum).get(1);
												} else {
													global.TMP_string.get(tmpstringnum).set(1, set_string);
													split_trig[i] = global.TMP_string.get(tmpstringnum).get(1);
												}
											}
										} catch (NumberFormatException e) {
											String[] fromtoSplit = value.split("-");
											try {
												int fromSplit = Integer.parseInt(fromtoSplit[0]);
												int toSplit = Integer.parseInt(fromtoSplit[fromtoSplit.length-1]);
												if (fromSplit <= check_stringLength && toSplit >= check_stringLength) {
													if (stringnum!=-1) {
														global.USR_string.get(stringnum).set(1, set_string);
														split_trig[i] = global.USR_string.get(stringnum).get(1);
													} else {
														global.TMP_string.get(tmpstringnum).set(1, set_string);
														split_trig[i] = global.TMP_string.get(tmpstringnum).get(1);
													}
												}
											} catch (NumberFormatException e1) {/*do nothing*/} catch (ArrayIndexOutOfBoundsException e1) {/*do nothing*/}
										}
									}
								} else {
									if (stringnum!=-1) {
										global.USR_string.get(stringnum).set(1, set_string);
										split_trig[i] = global.USR_string.get(stringnum).get(1);
									} else {
										global.TMP_string.get(tmpstringnum).set(1, set_string);
										split_trig[i] = global.TMP_string.get(tmpstringnum).get(1);
									}
								}
							}
						}
					}
				}
				trig = "";
				for (String value : split_trig) {trig += value;}
			}
		}
		return trig;
	}
}
