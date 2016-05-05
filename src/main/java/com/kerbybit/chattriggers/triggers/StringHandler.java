package com.kerbybit.chattriggers.triggers;

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
			System.out.println(TMP_e);
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
	
	private static String doStringFunctions(String sn, String func, String args) {
		int stringnum;
		int tmpstringnum = -1;
		
		args = stringFunctions(args);
		while (args.contains("{array[") && args.contains("]}")) {
			args = ArrayHandler.arrayFunctions(args);
			args = stringFunctions(args);
		}
		while (args.contains("{string[") && args.contains("]}")) {
			args = stringFunctions(args);
		}
		
		sn = stringFunctions(sn);
		
		stringnum = getStringNum(sn);
		
		if (stringnum==-1) {
			tmpstringnum = getTempStringNum(sn);
			if (tmpstringnum == -1) {
				ArrayList<String> temp = new ArrayList<String>();
                temp.add(sn); temp.add("");
                global.TMP_string.add(temp);
                tmpstringnum = global.TMP_string.size()-1;
			}
		} 

		if (func.equalsIgnoreCase("SET")) {
			if (!args.equals("~")) {
				if (stringnum!=-1) {
					global.USR_string.get(stringnum).set(1, args.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
					global.backupUSR_strings.get(stringnum).set(1, args.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
				} else {
					global.TMP_string.get(tmpstringnum).set(1, args.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
					global.backupTMP_strings.get(tmpstringnum).set(1, args.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
				}
			} else {
				if (stringnum!=-1) {
					global.backupUSR_strings.get(stringnum).set(1, global.USR_string.get(stringnum).get(1));
				} else {
					global.backupTMP_strings.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1));
				}
			}

			return "{string["+sn+"]}";
		} else if (func.equalsIgnoreCase("SAVE")) {
            if (!args.equals("~")) {
                if (stringnum!=-1) {
                    global.USR_string.get(stringnum).set(1, args.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
                    global.backupUSR_strings.get(stringnum).set(1, args.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
                } else {
                    global.TMP_string.get(tmpstringnum).set(1, args.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
                    global.backupTMP_strings.get(tmpstringnum).set(1, args.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
                }
            } else {
                if (stringnum!=-1) {
                    global.backupUSR_strings.get(stringnum).set(1, global.USR_string.get(stringnum).get(1));
                } else {
                    global.backupTMP_strings.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1));
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
				} catch (NumberFormatException e) {return args+" is not a number!{string["+sn+"]}";}
			} catch (NumberFormatException e) {return sn+" is not a number!{string["+sn+"]}";}
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
					return "{string["+sn+"]}";
				} catch (NumberFormatException e) {return args+" is not a number!{string["+sn+"]}";}
			} catch (NumberFormatException e) {return sn+" is not a number!{string["+sn+"]}";}
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
					return "{string["+sn+"]}";
				} catch (NumberFormatException e) {return args+" is not a number!{string["+sn+"]}";}
			} catch (NumberFormatException e) {return sn+" is not a number!{string["+sn+"]}";}
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
					return "{string["+sn+"]}";
				} catch (NumberFormatException e) {return args+" is not a number!{string["+sn+"]}";}
			} catch (NumberFormatException e) {return sn+" is not a number!{string["+sn+"]}";}
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
					return "{string["+sn+"]}";
				} catch (NumberFormatException e) {return args+" is not a number!{string["+sn+"]}";}
			} catch (NumberFormatException e) {return sn+" is not a number!{string["+sn+"]}";}
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
					return "{string["+sn+"]}";
				} catch (NumberFormatException e) {return args+" is not a number!{string["+sn+"]}";}
			} catch (NumberFormatException e) {return sn+" is not a number!{string["+sn+"]}";}
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
					return "{string["+sn+"]}";
				} catch (NumberFormatException e) {return args+" is not a number!{string["+sn+"]}";}
			} catch (NumberFormatException e) {return sn+" is not a number!{string["+sn+"]}";}
		} else if (func.equalsIgnoreCase("ABSOLUTE") || func.equalsIgnoreCase("ABS")) {
			try {
                Double argnmbr = Double.parseDouble(global.USR_string.get(stringnum).get(1).replace(",",""));
                String rtnmbr = Math.abs(argnmbr) + "";
                if (rtnmbr.endsWith(".0")) {rtnmbr = rtnmbr.substring(0,rtnmbr.length()-2);}
				if (stringnum!=-1) {global.USR_string.get(stringnum).set(1,rtnmbr);}
				else {global.TMP_string.get(tmpstringnum).set(1,rtnmbr);}
                return "{string["+sn+"]}";
			} catch (NumberFormatException e) {return sn+" is not a number!{string["+sn+"]}";}
		} else if (func.equalsIgnoreCase("REPLACE")) {
			args = args.replace("','", "stringCommaReplacementF6cyUQp9stringCommaReplacement");
			if (args.contains(",")) {
				String replaced = args.substring(0, args.indexOf(","));
				String replacer = args.substring(args.indexOf(",")+1);

				if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1).replace("','", "stringCommaReplacementF6cyUQp9stringCommaReplacement").replace(replaced, replacer));}
				else {global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1).replace("','", "stringCommaReplacementF6cyUQp9stringCommaReplacement").replace(replaced, replacer));}
				return "{string["+sn+"]}";
			} else {
				if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1).replace(",", "stringCommaReplacementF6cyUQp9stringCommaReplacement").replace(args, ""));}
				else {global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1).replace(",", "stringCommaReplacementF6cyUQp9stringCommaReplacement").replace(args, ""));}
				return "{string["+sn+"]}";
			}
		} else if (func.equalsIgnoreCase("SUBSTRING")) {
            args = args.replace("','", "stringCommaReplacementF6cyUQp9stringCommaReplacement");
            String[] subargs = args.split(",");
            if (subargs.length == 2) {
                if (stringnum!=-1) {
                    String temp = global.USR_string.get(stringnum).get(1).replace(",", "stringCommaReplacementF6cyUQp9stringCommaReplacement");
                    System.out.println(subargs[0]);
                    System.out.println(temp);
                    if (temp.contains(subargs[0])) {
                        System.out.println("first");
                        temp = temp.substring(temp.indexOf(subargs[0]) + subargs[0].length());
                        if (temp.contains(subargs[1])) {
                            System.out.println("second");
                            temp = temp.substring(0, temp.indexOf(subargs[1]));
                            global.USR_string.get(stringnum).set(1, temp.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
                        } else {global.USR_string.get(stringnum).set(1, "false");}
                    } else {global.USR_string.get(stringnum).set(1, "false");}
                } else {
                    String temp = global.TMP_string.get(tmpstringnum).get(1).replace(",", "stringCommaReplacementF6cyUQp9stringCommaReplacement");
                    if (temp.contains(subargs[0])) {
                        temp = temp.substring(temp.indexOf(subargs[0]) + subargs[0].length());
                        if (temp.contains(subargs[1])) {
                            temp = temp.substring(0, temp.indexOf(subargs[1]));
                            global.TMP_string.get(tmpstringnum).set(1, temp.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
                        } else {global.TMP_string.get(tmpstringnum).set(1, "false");}
                    } else {global.TMP_string.get(tmpstringnum).set(1, "false");}
                }
                return "{string["+sn+"]}";
            } else {return "Missing args in .SUBSTRING! expected 2, got " + subargs.length + " {string["+sn+"]}";}
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
				} catch (NumberFormatException e) {return args+" is not a number!{string["+sn+"]}";}
			} catch (NumberFormatException e) {return sn+" is not a number!{string["+sn+"]}";}
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
				} catch (NumberFormatException e) {return args+" is not a number!{string["+sn+"]}";}
			} catch (NumberFormatException e) {return sn+" is not a number!{string["+sn+"]}";}
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
				} catch (NumberFormatException e) {return args+" is not a number!{string["+sn+"]}";}
			} catch (NumberFormatException e) {return sn+" is not a number!{string["+sn+"]}";}
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
				} catch (NumberFormatException e) {return args+" is not a number!{string["+sn+"]}";}
			} catch (NumberFormatException e) {return sn+" is not a number!{string["+sn+"]}";}
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
				global.TMP_string.get(tmpstringnum).set(1, global.USR_string.get(tmpstringnum).get(1).replace("\\", "\\\\"));
			}
			return "{string["+sn+"]}";
		}
			
		else {return func + " is not a function!{string["+sn+"]}";}
	}
	
	static String stringFunctions(String TMP_e) {
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
				sn = stringFunctions(sn);
				
				TMP_e = efirst + sn + esecond;
				sn = sn.replace("stringOpenStringF6cyUQp9stringOpenString", "{string[");
				while (args.contains("(")) {
					args = TMP_e.substring(TMP_e.indexOf(args), TMP_e.indexOf(")", TMP_e.indexOf(")")+1));
					
					String first;
					String second;
					String argsbefore;
					
					first = args.substring(0,args.indexOf("("));
					second = args.substring(args.indexOf("(")+1);
					argsbefore = args;
					args = first + "stringOpenBracketF6cyUQp9stringOpenBracket" + second;
					TMP_e = TMP_e.replace(argsbefore, args);
					first = args.substring(0,args.indexOf(")"));
					second = args.substring(args.indexOf(")")+1);
					argsbefore = args;
					args = first + "stringCloseBracketF6cyUQp9stringCloseBracket" + second;
					TMP_e = TMP_e.replace(argsbefore, args);
				}
				args = args.replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(").replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")");
				TMP_e = TMP_e.replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(").replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")");
				String fullreplace = "{string["+sn+"]}."+func+"("+args+")";
				String firstpart = TMP_e.substring(0, TMP_e.indexOf(fullreplace));
				String secondpart = TMP_e.substring(TMP_e.indexOf(fullreplace)+fullreplace.length());
				
				TMP_e = firstpart + StringHandler.doStringFunctions(sn,func,args) + secondpart;
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
				sn = stringFunctions(sn);
				
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
