package com.kerbybit.chattriggers.triggers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.file.FileHandler;
import com.kerbybit.chattriggers.globalvars.global;

import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Score;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class StringHandler {
	public static String builtInStrings(String TMP_e, ClientChatReceivedEvent chatEvent) {
		while (TMP_e.contains("{msg[") && TMP_e.contains("]}")) {
			String strnum = TMP_e.substring(TMP_e.indexOf("{msg[")+5, TMP_e.indexOf("]}", TMP_e.indexOf("{msg[")));
			List<String> temporary = new ArrayList<String>();
			temporary.add("DefaultString->MSGHISTORY"+strnum+"-"+(global.TMP_string.size()+1));
			try {
				int num = Integer.parseInt(strnum);
				if (num>=0) {
					if (num<global.chatHistory.size()) {temporary.add(global.chatHistory.get(global.chatHistory.size()-(num+1)));} 
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
				temporary.add(chatEvent.message.getFormattedText());
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
			String current_server = "";
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
			String returnString = "";
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
			String returnString = "";
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
			String returnString = "";
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
			String returnString = "";
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
			for (int j=0; j<Math.floor((((280*(Minecraft.getMinecraft().gameSettings.chatWidth))+40)/320)*52); j++) {dashes += "-";}
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
		if (TMP_e.contains("{hp}")) {
			List<String> temporary = new ArrayList<String>();
			temporary.add("DefaultString->HP-"+(global.TMP_string.size()+1));
			temporary.add(Minecraft.getMinecraft().thePlayer.getHealth() + "");
			global.TMP_string.add(temporary);
			global.backupTMP_strings.add(temporary);
			TMP_e = TMP_e.replace("{hp}", "{string[DefaultString->HP-"+global.TMP_string.size()+"]}");
		}
		return TMP_e;
	}
	
	public static void resetBackupStrings() {
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
	}
	
	public static int getStringNum(String sn) {
		for (int i=0; i<global.USR_string.size(); i++) {
			if (global.USR_string.get(i).get(0).equals(sn)) {
				return i;
			}
		}
		return -1;
	}
	
	public static int getTempStringNum(String sn) {
		for (int i=0; i<global.TMP_string.size(); i++) {
			if (global.TMP_string.get(i).get(0).equals(sn)) {
				return i;
			}
		}
		return -1;
	}
	
	public static String doStringFunctions(String sn, String func, String args) {
		String returnstring = "something went wrong!{string["+sn+"]}";
		int stringnum = -1;
		int tmpstringnum = -1;
		
		args = ArrayHandler.arrayFunctions(args);
		args = stringFunctions(args);
		sn = stringFunctions(sn);
		
		stringnum = getStringNum(sn);
		
		if (stringnum==-1) {
			tmpstringnum = getTempStringNum(sn);
			if (tmpstringnum == -1) {returnstring = "That is not a string!";}
		} 

		if (stringnum!=-1 || tmpstringnum!=-1){
			if (func.equalsIgnoreCase("SET")) {
				if (stringnum!=-1) {
					global.USR_string.get(stringnum).set(1, args);
					global.backupUSR_strings.get(stringnum).set(1, args);
				} else if (tmpstringnum!=-1) {
					global.TMP_string.get(tmpstringnum).set(1, args);
					global.backupTMP_strings.get(tmpstringnum).set(1, args);
				}
				return args;
			} else if (func.equalsIgnoreCase("SAVE")) {
				if (stringnum!=-1) {
					global.USR_string.get(stringnum).set(1, args);
					global.backupUSR_strings.get(stringnum).set(1, args);
				} else if (tmpstringnum!=-1) {
					global.TMP_string.get(tmpstringnum).set(1, args);
					global.backupTMP_strings.get(tmpstringnum).set(1, args);
				}
				try {FileHandler.saveAll();} catch (IOException e) {ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));}
				return args;
			} else if (func.equalsIgnoreCase("ADD") || func.equalsIgnoreCase("PLUS")) {
				try {
					int strnmbr = -99999;
					if (stringnum!=-1) {strnmbr = Integer.parseInt(global.USR_string.get(stringnum).get(1));} 
					else if (tmpstringnum!=-1) {strnmbr = Integer.parseInt(global.TMP_string.get(tmpstringnum).get(1));}
					try {
						int argnmbr = Integer.parseInt(args);
						if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, strnmbr + argnmbr + "");} 
						else {global.TMP_string.get(tmpstringnum).set(1, strnmbr + argnmbr + "");}
						return "{string["+sn+"]}";
					} catch (NumberFormatException e) {return args+" is not a number!{string["+sn+"]}";}
				} catch (NumberFormatException e) {return sn+" is not a number!{string["+sn+"]}";}
			} else if (func.equalsIgnoreCase("SUBTRACT") || func.equalsIgnoreCase("MINUS")) {
				try {
					int strnmbr = -99999;
					if (stringnum!=-1) {strnmbr = Integer.parseInt(global.USR_string.get(stringnum).get(1));} 
					else if (tmpstringnum!=-1) {strnmbr = Integer.parseInt(global.TMP_string.get(tmpstringnum).get(1));}
					try {
						int argnmbr = Integer.parseInt(args);
						if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, strnmbr - argnmbr + "");} 
						else {global.TMP_string.get(tmpstringnum).set(1, strnmbr - argnmbr + "");}
						return "{string["+sn+"]}";
					} catch (NumberFormatException e) {return args+" is not a number!{string["+sn+"]}";}
				} catch (NumberFormatException e) {return sn+" is not a number!{string["+sn+"]}";}
			} else if (func.equalsIgnoreCase("MULTIPLY") || func.equalsIgnoreCase("MULT") || func.equalsIgnoreCase("TIMES")) {
				try {
					int strnmbr = -99999;
					if (stringnum!=-1) {strnmbr = Integer.parseInt(global.USR_string.get(stringnum).get(1));} 
					else if (tmpstringnum!=-1) {strnmbr = Integer.parseInt(global.TMP_string.get(tmpstringnum).get(1));}
					try {
						int argnmbr = Integer.parseInt(args);
						if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, strnmbr * argnmbr + "");} 
						else {global.TMP_string.get(tmpstringnum).set(1, strnmbr * argnmbr + "");}
						return "{string["+sn+"]}";
					} catch (NumberFormatException e) {return args+" is not a number!{string["+sn+"]}";}
				} catch (NumberFormatException e) {return sn+" is not a number!{string["+sn+"]}";}
			} else if (func.equalsIgnoreCase("DIVIDE") || func.equalsIgnoreCase("DIV")) {
				try {
					int strnmbr = -99999;
					if (stringnum!=-1) {strnmbr = Integer.parseInt(global.USR_string.get(stringnum).get(1));} 
					else if (tmpstringnum!=-1) {strnmbr = Integer.parseInt(global.TMP_string.get(tmpstringnum).get(1));}
					try {
						int argnmbr = Integer.parseInt(args);
						Float returnNum = ((float) strnmbr)/((float) argnmbr);
						if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, returnNum+"");} 
						else {global.TMP_string.get(tmpstringnum).set(1, returnNum+"");}
						return "{string["+sn+"]}";
					} catch (NumberFormatException e) {return args+" is not a number!{string["+sn+"]}";}
				} catch (NumberFormatException e) {return sn+" is not a number!{string["+sn+"]}";}
			} else if (func.equalsIgnoreCase("POW") || func.equalsIgnoreCase("POWER")) {
				try {
					int strnmbr = -99999;
					if (stringnum!=-1) {strnmbr = Integer.parseInt(global.USR_string.get(stringnum).get(1));} 
					else if (tmpstringnum!=-1) {strnmbr = Integer.parseInt(global.TMP_string.get(tmpstringnum).get(1));}
					try {
						int argnmbr = Integer.parseInt(args);
						if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, Math.pow(strnmbr,argnmbr) + "");} 
						else {global.TMP_string.get(tmpstringnum).set(1, Math.pow(strnmbr,argnmbr) + "");}
						return "{string["+sn+"]}";
					} catch (NumberFormatException e) {return args+" is not a number!{string["+sn+"]}";}
				} catch (NumberFormatException e) {return sn+" is not a number!{string["+sn+"]}";}
			} else if (func.equalsIgnoreCase("REPLACE")) {
				if (args.contains(",")) {
					String replaced = args.substring(0, args.indexOf(","));
					String replacer = args.substring(args.indexOf(",")+1);
					if (replaced!=null) {
						if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1).replace(replaced, replacer));} 
						else if (tmpstringnum!=-1) {global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1).replace(replaced, replacer));}
						return "{string["+sn+"]}";
					} else {return "Improper format! use replace(toreplace,replacement){string["+sn+"]}";}
				} else {
					if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1).replace(args, ""));} 
					else {global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1).replace(args, ""));}
					return "{string["+sn+"]}";
				}
			} else if (func.equalsIgnoreCase("PREFIX")) {
				if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, args + global.USR_string.get(stringnum).get(1));} 
				else if (tmpstringnum!=-1) {global.TMP_string.get(tmpstringnum).set(1, args + global.TMP_string.get(tmpstringnum).get(1));}
				return "{string["+sn+"]}";
			} else if (func.equalsIgnoreCase("SUFFIX")) {
				if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, args + global.USR_string.get(stringnum).get(1));} 
				else if (tmpstringnum!=-1) {global.TMP_string.get(tmpstringnum).set(1, args + global.TMP_string.get(tmpstringnum).get(1));}
				return "{string["+sn+"]}";
			} else if (func.equalsIgnoreCase("TOUPPER") || func.equalsIgnoreCase("TOUPPERCASE")) {
				if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1).toUpperCase());} 
				else if (tmpstringnum!=-1) {global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1).toUpperCase());}
				return "{string["+sn+"]}";
			} else if (func.equalsIgnoreCase("TOLOWER") || func.equalsIgnoreCase("TOLOWERCASE")) {
				if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1).toLowerCase());} 
				else if (tmpstringnum!=-1) {global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1).toLowerCase());}
				return "{string["+sn+"]}";
			} else if (func.equalsIgnoreCase("REMOVEFORMATTING") || func.equalsIgnoreCase("REMFORMATTING") || func.equalsIgnoreCase("REMOVEFORM") || func.equalsIgnoreCase("REMFORM")) {
				if (stringnum!=-1) {
					global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1)
							.replace("&0","").replace("&1","").replace("&2","").replace("&3","").replace("&4","").replace("&5","").replace("&6","").replace("&7","").replace("&8","").replace("&9","").replace("&a","").replace("&b","").replace("&c","").replace("&d","").replace("&e","").replace("&f","")
							.replace("&k", "").replace("&l", "").replace("&m", "").replace("&n", "").replace("&o", "").replace("&r", ""));
				} else if (tmpstringnum!=-1) {
					global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1)
							.replace("&0","").replace("&1","").replace("&2","").replace("&3","").replace("&4","").replace("&5","").replace("&6","").replace("&7","").replace("&8","").replace("&9","").replace("&a","").replace("&b","").replace("&c","").replace("&d","").replace("&e","").replace("&f","")
							.replace("&k", "").replace("&l", "").replace("&m", "").replace("&n", "").replace("&o", "").replace("&r", ""));
				}
				return "{string["+sn+"]}";
			} else if (func.equalsIgnoreCase("IMPORTJSONFILE")){
				String sj = "Improper format! use importJsonFile(file,node)";
				if (args.contains(",")) {
					sj = FileHandler.importJsonFile("string", args.substring(0, args.indexOf(",")), sn + "=>" + args.substring(args.indexOf(",")+1));
					if (stringnum!=-1) {
						global.USR_string.get(stringnum).set(1, sj);
						global.backupUSR_strings.get(stringnum).set(1, sj);
					} else if (tmpstringnum!=-1) {
						global.TMP_string.get(tmpstringnum).set(1, sj);
						global.backupTMP_strings.get(tmpstringnum).set(1, sj);
					}
				} else {
					if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, sj);} 
					else if (tmpstringnum!=-1) {global.TMP_string.get(tmpstringnum).set(1, sj);}
				}
				return "{string["+sn+"]}";
			} else if (func.equalsIgnoreCase("IMPORTJSONURL")) {
				String sj = "Improper format! use importJsonFile(file,node)";
				if (args.contains(",")) {
					sj = FileHandler.importJsonURL("string", args.substring(0, args.indexOf(",")), sn + "=>" + args.substring(args.indexOf(",")+1));
					if (stringnum!=-1) {
						global.USR_string.get(stringnum).set(1, sj);
						global.backupUSR_strings.get(stringnum).set(1, sj);
					} else if (tmpstringnum!=-1) {
						global.TMP_string.get(tmpstringnum).set(1, sj);
						global.backupTMP_strings.get(tmpstringnum).set(1, sj);
					}
				} else {
					if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, sj);} 
					else if (tmpstringnum!=-1) {global.TMP_string.get(tmpstringnum).set(1, sj);}
				}
				return "{string["+sn+"]}";
			} else if (func.equalsIgnoreCase("EQUALS") || (func.equalsIgnoreCase("="))) {
				if (stringnum!=-1) {
					if (global.USR_string.get(stringnum).get(1).equals(args)) {
						global.USR_string.get(stringnum).set(1, "true");
					} else {global.USR_string.get(stringnum).set(1, "false");}
				} else if (tmpstringnum!=-1) {
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
				} else if (tmpstringnum!=-1) {
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
				} else if (tmpstringnum!=-1) {
					if (global.TMP_string.get(tmpstringnum).get(1).startsWith(args)) {
						global.TMP_string.get(tmpstringnum).set(1, "true");
					} else {global.TMP_string.get(tmpstringnum).set(1, "false");}
				}
				return "{string["+sn+"]}";
			} else if (func.equalsIgnoreCase("CONTAINS")) {
				if (stringnum!=-1) {
					if (global.USR_string.get(stringnum).get(1).contains(args)) {
						global.USR_string.get(stringnum).set(1, "true");
					} else {global.USR_string.get(stringnum).set(1, "false");}
				} else if (tmpstringnum!=-1) {
					if (global.TMP_string.get(tmpstringnum).get(1).contains(args)) {
						global.TMP_string.get(tmpstringnum).set(1, "true");
					} else {global.TMP_string.get(tmpstringnum).set(1, "false");}
				}
				return "{string["+sn+"]}";
			} else if (func.equalsIgnoreCase("ENDSWITH")) {
				if (stringnum!=-1) {
					if (global.USR_string.get(stringnum).get(1).endsWith(args)) {
						global.USR_string.get(stringnum).set(1, "true");
					} else {global.USR_string.get(stringnum).set(1, "false");}
				} else if (tmpstringnum!=-1) {
					if (global.TMP_string.get(tmpstringnum).get(1).endsWith(args)) {
						global.TMP_string.get(tmpstringnum).set(1, "true");
					} else {global.TMP_string.get(tmpstringnum).set(1, "false");}
				}
				return "{string["+sn+"]}";
			} else if (func.equalsIgnoreCase("GREATERTHAN") || func.equalsIgnoreCase("GT") || func.equalsIgnoreCase(">")) {
				try {
					int strnmbr = -99999;
					if (stringnum!=-1) {strnmbr = Integer.parseInt(global.USR_string.get(stringnum).get(1));} 
					else {strnmbr = Integer.parseInt(global.TMP_string.get(tmpstringnum).get(1));}
					try {
						int argnmbr = Integer.parseInt(args);
						if (stringnum!=-1) {
							if (strnmbr>argnmbr) {global.USR_string.get(stringnum).set(1, "true");} 
							else {global.USR_string.get(stringnum).set(1, "false");}
						} else if (tmpstringnum!=-1) {
							if (strnmbr>argnmbr) {global.TMP_string.get(tmpstringnum).set(1, "true");} 
							else {global.TMP_string.get(tmpstringnum).set(1, "false");}
						}
						return "{string["+sn+"]}";
					} catch (NumberFormatException e) {return args+" is not a number!{string["+sn+"]}";}
				} catch (NumberFormatException e) {return sn+" is not a number!{string["+sn+"]}";}
			} else if (func.equalsIgnoreCase("LESSTHAN") || func.equalsIgnoreCase("LT") || func.equalsIgnoreCase("<")) {
				try {
					int strnmbr = -99999;
					if (stringnum!=-1) {strnmbr = Integer.parseInt(global.USR_string.get(stringnum).get(1));} 
					else {strnmbr = Integer.parseInt(global.TMP_string.get(tmpstringnum).get(1));}
					try {
						int argnmbr = Integer.parseInt(args);
						if (stringnum!=-1) {
							if (strnmbr<argnmbr) {global.USR_string.get(stringnum).set(1, "true");} 
							else {global.USR_string.get(stringnum).set(1, "false");}
						} else if (tmpstringnum!=-1) {
							if (strnmbr<argnmbr) {global.TMP_string.get(tmpstringnum).set(1, "true");} 
							else {global.TMP_string.get(tmpstringnum).set(1, "false");}
						}
						return "{string["+sn+"]}";
					} catch (NumberFormatException e) {return args+" is not a number!{string["+sn+"]}";}
				} catch (NumberFormatException e) {return sn+" is not a number!{string["+sn+"]}";}
			} else if (func.equalsIgnoreCase("GREATERTHANOREQUALTO") || func.equalsIgnoreCase("GTE") || func.equalsIgnoreCase(">=")) {
				try {
					int strnmbr = -99999;
					if (stringnum!=-1) {strnmbr = Integer.parseInt(global.USR_string.get(stringnum).get(1));} 
					else {strnmbr = Integer.parseInt(global.TMP_string.get(tmpstringnum).get(1));}
					try {
						int argnmbr = Integer.parseInt(args);
						if (stringnum!=-1) {
							if (strnmbr>=argnmbr) {global.USR_string.get(stringnum).set(1, "true");} 
							else {global.USR_string.get(stringnum).set(1, "false");}
						} else if (tmpstringnum!=-1) {
							if (strnmbr>=argnmbr) {global.TMP_string.get(tmpstringnum).set(1, "true");} 
							else {global.TMP_string.get(tmpstringnum).set(1, "false");}
						}
						return "{string["+sn+"]}";
					} catch (NumberFormatException e) {return args+" is not a number!{string["+sn+"]}";}
				} catch (NumberFormatException e) {return sn+" is not a number!{string["+sn+"]}";}
			} else if (func.equalsIgnoreCase("LESSTHANOREQUALTO") || func.equalsIgnoreCase("LTE") || func.equalsIgnoreCase("<=")) {
				try {
					int strnmbr = -99999;
					if (stringnum!=-1) {strnmbr = Integer.parseInt(global.USR_string.get(stringnum).get(1));} 
					else {strnmbr = Integer.parseInt(global.TMP_string.get(tmpstringnum).get(1));}
					try {
						int argnmbr = Integer.parseInt(args);
						if (stringnum!=-1) {
							if (strnmbr<=argnmbr) {global.USR_string.get(stringnum).set(1, "true");} 
							else {global.USR_string.get(stringnum).set(1, "false");}
						} else if (tmpstringnum!=-1) {
							if (strnmbr<=argnmbr) {global.TMP_string.get(tmpstringnum).set(1, "true");} 
							else {global.TMP_string.get(tmpstringnum).set(1, "false");}
						}
						return "{string["+sn+"]}";
					} catch (NumberFormatException e) {return args+" is not a number!{string["+sn+"]}";}
				} catch (NumberFormatException e) {return sn+" is not a number!{string["+sn+"]}";}
			}
			
			else {return func + " is not a function!{string["+sn+"]}";}
		}
		return returnstring;
	}
	
	public static String stringFunctions(String TMP_e) {
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
					String ereplacement = "stringOpenStringF6cyUQp9stringOpenString";
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
					
					String first = "";
					String second = "";
					String argsbefore = "";
					
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
				args = args.replace("stringOpenBracketF6cyUQp9stringOpenBracket", "("); args = args.replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")");
				TMP_e = TMP_e.replace("stringOpenBracketF6cyUQp9stringOpenBracket", "("); TMP_e = TMP_e.replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")");
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
				if (returnString == "Not a string!") {
					for (int i=0; i<global.TMP_string.size(); i++) {
						if (global.TMP_string.get(i).get(0).equals(sn)) {returnString = global.TMP_string.get(i).get(1);}
					}
				}
				
				String fullreplace = "{string["+sn+"]}";
				String firstpart = TMP_e.substring(0, TMP_e.indexOf(fullreplace));
				String secondpart = TMP_e.substring(TMP_e.indexOf(fullreplace)+fullreplace.length());
				
				TMP_e = firstpart + returnString.replace(",", "stringCommaReplacementF6cyUQp9stringCommaReplacement") + secondpart;
				
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
	
	public static String setStrings(String msg, String trig) {
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
							for (String value : stringCheckSplit) {
								stringListSplit.add(value);
							}
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
									int check_stringLength = 0;
									if (checkCharacter==true) {
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
										} catch (NumberFormatException e1) {} catch (ArrayIndexOutOfBoundsException e1) {}
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
									int check_stringLength = 0;
									if (checkCharacter==true) {
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
										} catch (NumberFormatException e1) {} catch (ArrayIndexOutOfBoundsException e1) {}
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
							int checkafter = -1;
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
										int check_stringLength = 0;
										if (checkCharacter==true) {
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
											} catch (NumberFormatException e1) {} catch (ArrayIndexOutOfBoundsException e1) {}
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
