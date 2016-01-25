package com.kerbybit.chattriggers;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class events {
	
	static List<List<String>> backupUSR_strings = new ArrayList<List<String>>();
	static List<List<String>> backupTMP_strings = new ArrayList<List<String>>();
	
	public static void doEvents(List<String> tmp_tmp_event, ClientChatReceivedEvent chatEvent) {
		List<String> tmp_event = new ArrayList<String>(tmp_tmp_event);
		String[] snull = null;
		doEvents(tmp_event, chatEvent, snull, snull);
	}
	
	public static String arrayFunctions(String TMP_e) {
		while (TMP_e.contains("{array[") && TMP_e.contains("]}.add(") && TMP_e.contains(")")) {
			String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.add(", TMP_e.indexOf("{array[")));
			String checkTo = TMP_e.substring(TMP_e.indexOf("]}.add(")+7, TMP_e.indexOf(")", TMP_e.indexOf("]}.add(")));
			Boolean isArray = false;
			
			for (int j=0; j<global.USR_array.size(); j++) {
				if (global.USR_array.get(j).get(0).equals(checkFrom)) {
					global.USR_array.get(j).add(checkTo);
					isArray = true;
				}
			}
			
			if (isArray == false) {
				List<String> prearray = new ArrayList<String>();
				prearray.add(checkFrom);
				prearray.add(checkTo);
				global.USR_array.add(prearray);
			}
			
			List<String> temporary = new ArrayList<String>();
			temporary.add("ArrayToString->"+checkFrom+"ADD"+checkTo+"-"+(global.TMP_string.size()+1));
			temporary.add(checkTo);
			global.TMP_string.add(temporary);
			backupTMP_strings.add(temporary);
			TMP_e = TMP_e.replace("{array[" + checkFrom + "]}.add(" + checkTo + ")", "{string[ArrayToString->"+checkFrom+"ADD"+checkTo+"-"+global.TMP_string.size()+"]}");
		}
		
		while (TMP_e.contains("{array[") && TMP_e.contains("]}.clear()")) {
			String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.clear()", TMP_e.indexOf("{array[")));
			String returnString = checkFrom + " is not an array!";
			
			for (int j=0; j<global.USR_array.size(); j++) {
				if (global.USR_array.get(j).get(0).equals(checkFrom)) {
					global.USR_array.remove(j);
					returnString = checkFrom + " cleared.";
				}
			}
			
			List<String> temporary = new ArrayList<String>();
			temporary.add("ArrayToString->"+checkFrom+"CLEAR"+"-"+(global.USR_string.size()+1));
			temporary.add(returnString);
			global.TMP_string.add(temporary);
			backupTMP_strings.add(temporary);
			TMP_e = TMP_e.replace("{array[" + checkFrom + "]}.clear()", "{string[ArrayToString->"+checkFrom+"CLEAR"+"-"+global.TMP_string.size()+"]}");
		}
		
		while (TMP_e.contains("{array[") && TMP_e.contains("]}.has(") && TMP_e.contains(")")) {
			String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.has(", TMP_e.indexOf("{array[")));
			String checkTo = TMP_e.substring(TMP_e.indexOf("]}.has(")+7, TMP_e.indexOf(")", TMP_e.indexOf("]}.has(")));
			String checkThis = "false";
			
			for (int j=0; j<global.USR_array.size(); j++) {
				if (global.USR_array.get(j).get(0).equals(checkFrom)) {
					for (int k=1; k<global.USR_array.get(j).size(); k++) {
						if (global.USR_array.get(j).get(k).equals(checkTo)) {
							checkThis = "true";
						}
					}
				}
			}
			
			List<String> temporary = new ArrayList<String>();
			temporary.add("ArrayToString->"+checkFrom+"HAS"+checkTo+"-"+(global.TMP_string.size()+1));
			temporary.add(checkThis);
			global.TMP_string.add(temporary);
			backupTMP_strings.add(temporary);
			TMP_e = TMP_e.replace("{array["+checkFrom+"]}.has("+checkTo+")", "{string[ArrayToString->"+checkFrom+"HAS"+checkTo+"-"+global.TMP_string.size()+"]}");
		}
		
		while (TMP_e.contains("{array[") && TMP_e.contains("]}.remove(") && TMP_e.contains(")")) {
			String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.remove(", TMP_e.indexOf("{array[")));
			String checkTo = TMP_e.substring(TMP_e.indexOf("]}.remove(")+10, TMP_e.indexOf(")", TMP_e.indexOf("]}.remove(")));
			String removed = "";
			int toRemove = -1;
			int toRemoveArray = -1;
			String returnString = checkFrom + " is not an array!";
			
			try {
				toRemove = Integer.parseInt(checkTo);
				if (toRemove > 0) {
					for (int j=0; j<global.USR_array.size(); j++) {
						if (global.USR_array.get(j).get(0).equals(checkFrom)) {
							if (toRemove < global.USR_array.get(j).size()) {
								removed = global.USR_array.get(j).remove(toRemove);
								returnString = removed;
								if (global.USR_array.get(j).size()==1) {
									toRemoveArray = j;
								}
							} else {
								returnString = "Value over bounds! (index "+toRemove+" - expecting "+global.USR_array.size()+")";
							}
						}
					}
				} else {
					returnString = "{array}.remove($value) - Value under bounds! (index "+toRemove+" - expecting 1)";
				}
			} catch (NumberFormatException e) {
				returnString = "Value must be an integer!";
			}
			
			if (toRemoveArray != -1) {
				global.USR_array.remove(toRemoveArray);
			}
			
			List<String> temporary = new ArrayList<String>();
			temporary.add("ArrayToString->"+checkFrom+"REMOVE"+checkTo+"-"+(global.TMP_string.size()+1));
			temporary.add(returnString);
			global.TMP_string.add(temporary);
			backupTMP_strings.add(temporary);
			TMP_e = TMP_e.replace("{array[" + checkFrom + "]}.remove(" + checkTo + ")", "{string[ArrayToString->"+checkFrom+"REMOVE"+checkTo+"-"+global.TMP_string.size()+"]}");
		}
		
		while (TMP_e.contains("{array[") && TMP_e.contains("]}.size()")) {
			String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.size()", TMP_e.indexOf("{array[")));
			int arraysize = 0;
			
			for (int j=0; j<global.USR_array.size(); j++) {
				if (global.USR_array.get(j).get(0).equals(checkFrom)) {
					arraysize = global.USR_array.get(j).size()-1;
				}
			}
			
			List<String> temporary = new ArrayList<String>();
			temporary.add("ArrayToString->"+checkFrom+"SIZE"+"-"+(global.TMP_string.size()+1));
			temporary.add(arraysize+"");
			global.TMP_string.add(temporary);
			backupTMP_strings.add(temporary);
			TMP_e = TMP_e.replace("{array[" + checkFrom + "]}.size()", "{string[ArrayToString->"+checkFrom+"SIZE"+"-"+global.TMP_string.size()+"]}");
		}
		
		while (TMP_e.contains("{array[") && TMP_e.contains("]}.importJsonFile(") && TMP_e.contains(",") && TMP_e.contains(")")) {
			String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.importJsonFile(", TMP_e.indexOf("{array[")));
			String checkFile = TMP_e.substring(TMP_e.indexOf("]}.importJsonFile(")+18, TMP_e.indexOf(",", TMP_e.indexOf("{array["+checkFrom+"]}.importJsonFile(")));
			String checkTo = TMP_e.substring(TMP_e.indexOf(",", TMP_e.indexOf("{array["+checkFrom+"]}.importJsonFile("))+1, TMP_e.indexOf(")", TMP_e.indexOf("{array["+checkFrom+"]}.importJsonFile("+checkFile+",")));
			
			String checkJson = file.importJsonFile("array",checkFile, checkFrom+"=>"+checkTo);
			
			List<String> temporary = new ArrayList<String>();
			temporary.add("ArrayToString->"+checkFrom+"IMPORTJSONFILE"+checkTo+"FROM"+checkFile+"-"+(global.TMP_string.size()+1));
			temporary.add(checkJson);
			global.TMP_string.add(temporary);
			backupTMP_strings.add(temporary);
			TMP_e = TMP_e.replace("{array["+checkFrom+"]}.importJsonFile("+checkFile+","+checkTo+")", "{string[ArrayToString->"+checkFrom+"IMPORTJSONFILE"+checkTo+"FROM"+checkFile+"-"+global.TMP_string.size()+"]}");
		}
		
		while (TMP_e.contains("{array[") && TMP_e.contains("]}.importJsonURL(") && TMP_e.contains(",") && TMP_e.contains(")")) {
			String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.importJsonURL(", TMP_e.indexOf("{array[")));
			String checkFile = TMP_e.substring(TMP_e.indexOf("]}.importJsonURL(")+17, TMP_e.indexOf(",", TMP_e.indexOf("{array["+checkFrom+"]}.importJsonURL(")));
			String checkTo = TMP_e.substring(TMP_e.indexOf(",", TMP_e.indexOf("{array["+checkFrom+"]}.importJsonURL("))+1, TMP_e.indexOf(")", TMP_e.indexOf("{array["+checkFrom+"]}.importJsonURL("+checkFile+",")));
			
			String checkJson = file.importJsonURL("array",checkFile, checkFrom + "=>" + checkTo);
			
			List<String> temporary = new ArrayList<String>();
			temporary.add("ArrayToString->"+checkFrom+"IMPORTJSONURL"+checkTo+"FROM"+checkFile+"-"+(global.TMP_string.size()+1));
			temporary.add(checkJson);
			global.TMP_string.add(temporary);
			backupTMP_strings.add(temporary);
			TMP_e = TMP_e.replace("{array["+checkFrom+"]}.importJsonURL("+checkFile+","+checkTo+")", "{string[ArrayToString->"+checkFrom+"IMPORTJSONURL"+checkTo+"FROM"+checkFile+"-"+global.TMP_string.size()+"]}");
		}
		
		return TMP_e;
	}
	
	public static String doStringFunctions(String sn, String func, String args) {
		String returnstring = "something went wrong!{string["+sn+"]}";
		int stringnum = -1;
		int tmpstringnum = -1;
		
		while (args.contains("{string[") && args.contains("]}")) {
			String testfor = args.substring(args.indexOf("]}", args.indexOf("{string["))+2);
			if (testfor.contains("]}.") && !testfor.contains("{string[")) {
				if (testfor.indexOf("]}.") < testfor.indexOf("(")) {
					testfor = "."+testfor.substring(testfor.indexOf("]}.")+3);
				}
			}
			if (testfor.startsWith(".") && testfor.contains("(") && testfor.contains(")")) {
				if (testfor.substring(testfor.indexOf("."), testfor.indexOf("(")).contains(" ")) {
					testfor = testfor.substring(1);
				}
			}
			if (testfor.startsWith(".") && testfor.contains("(") && testfor.contains(")")) {
				String snalt = args.substring(args.indexOf("{string[")+8, args.indexOf("]}.", args.indexOf("{string[")));
				String funcalt = args.substring(args.indexOf("]}.", args.indexOf("{string["))+3, args.indexOf("(", args.indexOf("]}.", args.indexOf("{string["))));
				String argsalt = args.substring(args.indexOf("(", args.indexOf("]}.", args.indexOf("{string[")))+1, args.indexOf(")",  args.indexOf("(", args.indexOf("]}.", args.indexOf("{string[")))));
				while (snalt.contains("{string[")) {
					snalt = sn.substring(sn.indexOf("{string[")+8, sn.indexOf("]}", sn.indexOf(snalt)+snalt.length()));
					
					String first = snalt.substring(0, snalt.indexOf("{string["));
					String replacement = "stringOpenStringF6cyUQp9stringOpenString";
					String second = snalt.substring(snalt.indexOf("{string[")+8);
					
					String efirst = sn.substring(0, sn.indexOf("{string[", sn.indexOf("{string[")+8));
					String ereplacement = "stringOpenStringF6cyUQp9stringOpenString";
					String esecond = sn.substring(sn.indexOf("{string[", sn.indexOf("{string[")+8)+8);
					
					sn = efirst + replacement + esecond;
					snalt = first + replacement + second;
				}
				sn = sn.replace("stringOpenStringF6cyUQp9stringOpenString", "{string[");
				snalt = snalt.replace("stringOpenStringF6cyUQp9stringOpenString", "{string[");
				
				String efirst = sn.substring(0, sn.indexOf(snalt));
				String esecond = sn.substring(sn.indexOf(snalt)+snalt.length());
				snalt = stringFunctions(snalt);
				while (argsalt.contains("(")) {
					argsalt = args.substring(args.indexOf(argsalt), args.indexOf(")", args.indexOf(")")+1));
					
					String first = "";
					String second = "";
					String argsbefore = "";
					
					first = argsalt.substring(0,argsalt.indexOf("("));
					second = argsalt.substring(argsalt.indexOf("(")+1);
					argsbefore = argsalt;
					argsalt = first + "stringOpenBracketF6cyUQp9stringOpenBracket" + second;
					args = args.replace(argsbefore, argsalt);
					
					first = argsalt.substring(0,argsalt.indexOf(")"));
					second = argsalt.substring(argsalt.indexOf(")")+1);
					argsbefore = argsalt;
					args = first + "stringCloseBracketF6cyUQp9stringCloseBracket" + second;
					args = args.replace(argsbefore, argsalt);
				}
				argsalt = argsalt.replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(");
				argsalt = argsalt.replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")");
				args = args.replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(");
				args = args.replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")");
				
				String fullreplace = "{string["+snalt+"]}."+funcalt+"("+argsalt+")";
				String firstpart = args.substring(0, args.indexOf(fullreplace));
				String secondpart = args.substring(args.indexOf(fullreplace)+fullreplace.length());
				
				args = firstpart + doStringFunctions(snalt,funcalt,argsalt) + secondpart;
			} else {
				String snalt = args.substring(args.indexOf("{string[")+8, args.indexOf("]}", args.indexOf("{string[")));
				String returnString = "Not a string!";
				while (snalt.contains("{string[")) {
					snalt = sn.substring(sn.indexOf("{string[")+8, sn.indexOf("]}", sn.indexOf(snalt)+snalt.length()+2));
					
					String first = snalt.substring(0, snalt.indexOf("{string["));
					String replacement = "stringOpenStringF6cyUQp9stringOpenString";
					String second = snalt.substring(snalt.indexOf("{string[")+8);
					
					String efirst = sn.substring(0, sn.indexOf("{string[", sn.indexOf("{string[")+8));
					String ereplacement = "stringOpenStringF6cyUQp9stringOpenString";
					String esecond = sn.substring(sn.indexOf("{string[", sn.indexOf("{string[")+8)+8);
					
					sn = efirst + replacement + esecond;
					snalt = first + replacement + second;
				}
				sn = sn.replace("stringOpenStringF6cyUQp9stringOpenString", "{string[");
				snalt = snalt.replace("stringOpenStringF6cyUQp9stringOpenString", "{string[");
				
				if (sn.contains(snalt)) {
					String efirst = sn.substring(0, sn.indexOf(snalt));
					String esecond = sn.substring(sn.indexOf(snalt)+snalt.length());
					snalt = stringFunctions(snalt);
				}
				
				for (int i=0; i<global.USR_string.size(); i++) {
					if (global.USR_string.get(i).get(0).equals(snalt)) {
						returnString = global.USR_string.get(i).get(1);
					}
				}
				if (returnString == "Not a string!") {
					for (int i=0; i<global.TMP_string.size(); i++) {
						if (global.TMP_string.get(i).get(0).equals(snalt)) {
							returnString = global.TMP_string.get(i).get(1);
						}
					}
				}
				
				String fullreplace = "{string["+snalt+"]}";
				String firstpart = args.substring(0, args.indexOf(fullreplace));
				String secondpart = args.substring(args.indexOf(fullreplace)+fullreplace.length());
				
				args = firstpart + returnString.replace(",", "stringCommaReplacementF6cyUQp9stringCommaReplacement") + secondpart;
				global.USR_string.clear();
				for (int i=0; i<backupUSR_strings.size(); i++) {
					String first = backupUSR_strings.get(i).get(0);
					String second = backupUSR_strings.get(i).get(1);
					List<String> temporary = new ArrayList<String>();
					temporary.add(first);
					temporary.add(second);
					global.USR_string.add(temporary);
				}
				global.TMP_string.clear();
				for (int i=0; i<backupTMP_strings.size(); i++) {
					String first = backupTMP_strings.get(i).get(0);
					String second = backupTMP_strings.get(i).get(1);
					List<String> temporary = new ArrayList<String>();
					temporary.add(first);
					temporary.add(second);
					global.TMP_string.add(temporary);
				}
			}
		}
		
		while (sn.contains("{string[") && sn.contains("]}")) {
			String testfor = sn.substring(sn.indexOf("]}", sn.indexOf("{string["))+2);
			if (testfor.contains("]}.") && !testfor.contains("{string[")) {
				if (testfor.indexOf("]}.") < testfor.indexOf("(")) {
					testfor = "."+testfor.substring(testfor.indexOf("]}.")+3);
				}
			}
			if (testfor.startsWith(".") && testfor.contains("(") && testfor.contains(")")) {
				if (testfor.substring(testfor.indexOf("."), testfor.indexOf("(")).contains(" ")) {
					testfor = testfor.substring(1);
				}
			}
			if (testfor.startsWith(".") && testfor.contains("(") && testfor.contains(")")) {
				String snalt = sn.substring(sn.indexOf("{string[")+8, sn.indexOf("]}.", sn.indexOf("{string[")));
				String funcalt = sn.substring(sn.indexOf("]}.", sn.indexOf("{string["))+3, sn.indexOf("(", sn.indexOf("]}.", sn.indexOf("{string["))));
				String argsalt = sn.substring(sn.indexOf("(", sn.indexOf("]}.", sn.indexOf("{string[")))+1, sn.indexOf(")",  sn.indexOf("(", sn.indexOf("]}.", sn.indexOf("{string[")))));
				while (snalt.contains("{string[")) {
					snalt = sn.substring(sn.indexOf("{string[")+8, sn.indexOf("]}", sn.indexOf(snalt)+snalt.length()));
					
					String first = snalt.substring(0, snalt.indexOf("{string["));
					String replacement = "stringOpenStringF6cyUQp9stringOpenString";
					String second = snalt.substring(snalt.indexOf("{string[")+8);
					
					String efirst = sn.substring(0, sn.indexOf("{string[", sn.indexOf("{string[")+8));
					String ereplacement = "stringOpenStringF6cyUQp9stringOpenString";
					String esecond = sn.substring(sn.indexOf("{string[", sn.indexOf("{string[")+8)+8);
					
					sn = efirst + replacement + esecond;
					snalt = first + replacement + second;
				}
				sn = sn.replace("stringOpenStringF6cyUQp9stringOpenString", "{string[");
				snalt = snalt.replace("stringOpenStringF6cyUQp9stringOpenString", "{string[");
				
				String efirst = sn.substring(0, sn.indexOf(snalt));
				String esecond = sn.substring(sn.indexOf(snalt)+snalt.length());
				snalt = stringFunctions(snalt);
				
				while (argsalt.contains("(")) {
					argsalt = sn.substring(sn.indexOf(argsalt), sn.indexOf(")", sn.indexOf(")")+1));
					
					String first = "";
					String second = "";
					String argsbefore = "";
					
					first = argsalt.substring(0,argsalt.indexOf("("));
					second = argsalt.substring(argsalt.indexOf("(")+1);
					argsbefore = argsalt;
					argsalt = first + "stringOpenBracketF6cyUQp9stringOpenBracket" + second;
					sn = sn.replace(argsbefore, argsalt);
					
					first = argsalt.substring(0,argsalt.indexOf(")"));
					second = argsalt.substring(argsalt.indexOf(")")+1);
					argsbefore = argsalt;
					args = first + "stringCloseBracketF6cyUQp9stringCloseBracket" + second;
					sn = sn.replace(argsbefore, argsalt);
				}
				argsalt = argsalt.replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(");
				argsalt = argsalt.replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")");
				sn = sn.replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(");
				sn = sn.replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")");
				
				String fullreplace = "{string["+snalt+"]}."+funcalt+"("+argsalt+")";
				String firstpart = sn.substring(0, sn.indexOf(fullreplace));
				String secondpart = sn.substring(sn.indexOf(fullreplace)+fullreplace.length());
				
				sn = firstpart + doStringFunctions(snalt,funcalt,argsalt) + secondpart;
			} else {
				String snalt = sn.substring(sn.indexOf("{string[")+8, sn.indexOf("]}", sn.indexOf("{string[")));
				while (snalt.contains("{string[")) {
					snalt = sn.substring(sn.indexOf("{string[")+8, sn.indexOf("]}", sn.indexOf(snalt)+snalt.length()));
					
					String first = snalt.substring(0, snalt.indexOf("{string["));
					String replacement = "stringOpenStringF6cyUQp9stringOpenString";
					String second = snalt.substring(snalt.indexOf("{string[")+8);
					
					String efirst = sn.substring(0, sn.indexOf("{string[", sn.indexOf("{string[")+8));
					String ereplacement = "stringOpenStringF6cyUQp9stringOpenString";
					String esecond = sn.substring(sn.indexOf("{string[", sn.indexOf("{string[")+8)+8);
					
					sn = efirst + replacement + esecond;
					snalt = first + replacement + second;
				}
				sn = sn.replace("stringOpenStringF6cyUQp9stringOpenString", "{string[");
				snalt = snalt.replace("stringOpenStringF6cyUQp9stringOpenString", "{string[");
				
				if (sn.contains(snalt)) {
					String efirst = sn.substring(0, sn.indexOf(snalt));
					String esecond = sn.substring(sn.indexOf(snalt)+snalt.length());
					snalt = stringFunctions(snalt);
				}
				
				String returnString = "Not a string!";
				
				for (int i=0; i<global.USR_string.size(); i++) {
					if (global.USR_string.get(i).get(0).equals(snalt)) {
						returnString = global.USR_string.get(i).get(1);
					}
				}
				if (returnString == "Not a string!") {
					for (int i=0; i<global.TMP_string.size(); i++) {
						if (global.TMP_string.get(i).get(0).equals(snalt)) {
							returnString = global.TMP_string.get(i).get(1);
						}
					}
				}
				
				String fullreplace = "{string["+snalt+"]}";
				String firstpart = sn.substring(0, sn.indexOf(fullreplace));
				String secondpart = sn.substring(sn.indexOf(fullreplace)+fullreplace.length());
				
				sn = firstpart + returnString.replace(",", "stringCommaReplacementF6cyUQp9stringCommaReplacement") + secondpart;
				
				global.USR_string.clear();
				for (int i=0; i<backupUSR_strings.size(); i++) {
					String first = backupUSR_strings.get(i).get(0);
					String second = backupUSR_strings.get(i).get(1);
					List<String> temporary = new ArrayList<String>();
					temporary.add(first);
					temporary.add(second);
					global.USR_string.add(temporary);
				}
				global.TMP_string.clear();
				for (int i=0; i<backupTMP_strings.size(); i++) {
					String first = backupTMP_strings.get(i).get(0);
					String second = backupTMP_strings.get(i).get(1);
					List<String> temporary = new ArrayList<String>();
					temporary.add(first);
					temporary.add(second);
					global.TMP_string.add(temporary);
				}
			}
		}
		
		for (int i=0; i<global.USR_string.size(); i++) {
			if (global.USR_string.get(i).get(0).equals(sn)) {
				stringnum=i;
			}
		}
		
		if (stringnum==-1) {
			for (int i=0; i<global.TMP_string.size(); i++) {
				if (global.TMP_string.get(i).get(0).equals(sn)) {
					tmpstringnum = i;
				}
			}
			if (tmpstringnum == -1) {
				returnstring = "That is not a string!";
			}
		} 
		
		if (stringnum!=-1 || tmpstringnum!=-1){
			if (func.equalsIgnoreCase("SET")) {
				if (stringnum!=-1) {
					global.USR_string.get(stringnum).set(1, args);
					backupUSR_strings.get(stringnum).set(1, args);
				} else {
					global.TMP_string.get(tmpstringnum).set(1, args);
					backupTMP_strings.get(tmpstringnum).set(1, args);
				}
				returnstring = args;
			} else if (func.equalsIgnoreCase("SAVE")) {
				if (stringnum!=-1) {
					global.USR_string.get(stringnum).set(1, args);
					backupUSR_strings.get(stringnum).set(1, args);
				} else {
					global.TMP_string.get(tmpstringnum).set(1, args);
					backupTMP_strings.get(tmpstringnum).set(1, args);
				}
				try {file.saveAll();} catch (IOException e) {chat.warn(chat.color("red", "Error saving triggers!"));}
				returnstring = args;
			} else if (func.equalsIgnoreCase("ADD") || func.equalsIgnoreCase("PLUS")) {
				try {
					int strnmbr = Integer.parseInt(global.USR_string.get(stringnum).get(1));
					try {
						int argnmbr = Integer.parseInt(args);
						if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, strnmbr + argnmbr + "");} 
						else {global.TMP_string.get(tmpstringnum).set(1, strnmbr + argnmbr + "");}
						returnstring = "{string["+sn+"]}";
					} catch (NumberFormatException e) {returnstring=args+" is not a number!{string["+sn+"]}";}
				} catch (NumberFormatException e) {returnstring=sn+" is not a number!{string["+sn+"]}";}
			} else if (func.equalsIgnoreCase("SUBTRACT") || func.equalsIgnoreCase("MINUS")) {
				try {
					int strnmbr = Integer.parseInt(global.USR_string.get(stringnum).get(1));
					try {
						int argnmbr = Integer.parseInt(args);
						if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, strnmbr - argnmbr + "");} 
						else {global.TMP_string.get(tmpstringnum).set(1, strnmbr - argnmbr + "");}
						returnstring = "{string["+sn+"]}";
					} catch (NumberFormatException e) {returnstring=args+" is not a number!{string["+sn+"]}";}
				} catch (NumberFormatException e) {returnstring=sn+" is not a number!{string["+sn+"]}";}
			} else if (func.equalsIgnoreCase("MULTIPLY") || func.equalsIgnoreCase("MULT") || func.equalsIgnoreCase("TIMES")) {
				try {
					int strnmbr = Integer.parseInt(global.USR_string.get(stringnum).get(1));
					try {
						int argnmbr = Integer.parseInt(args);
						if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, strnmbr * argnmbr + "");} 
						else {global.TMP_string.get(tmpstringnum).set(1, strnmbr * argnmbr + "");}
						returnstring = "{string["+sn+"]}";
					} catch (NumberFormatException e) {returnstring=args+" is not a number!{string["+sn+"]}";}
				} catch (NumberFormatException e) {returnstring=sn+" is not a number!{string["+sn+"]}";}
			} else if (func.equalsIgnoreCase("DIVIDE") || func.equalsIgnoreCase("DIV")) {
				try {
					int strnmbr = Integer.parseInt(global.USR_string.get(stringnum).get(1));
					try {
						int argnmbr = Integer.parseInt(args);
						System.out.println(strnmbr + "/" + argnmbr);
						Float returnNum = ((float) strnmbr)/((float) argnmbr);
						if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, returnNum+"");} 
						else {global.TMP_string.get(tmpstringnum).set(1, returnNum+"");}
						returnstring = "{string["+sn+"]}";
					} catch (NumberFormatException e) {returnstring=args+" is not a number!{string["+sn+"]}";}
				} catch (NumberFormatException e) {returnstring=sn+" is not a number!{string["+sn+"]}";}
			} else if (func.equalsIgnoreCase("POW") || func.equalsIgnoreCase("POWER")) {
				try {
					int strnmbr = Integer.parseInt(global.USR_string.get(stringnum).get(1));
					try {
						int argnmbr = Integer.parseInt(args);
						if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, Math.pow(strnmbr,argnmbr) + "");} 
						else {global.TMP_string.get(tmpstringnum).set(1, Math.pow(strnmbr,argnmbr) + "");}
						returnstring = "{string["+sn+"]}";
					} catch (NumberFormatException e) {returnstring=args+" is not a number!{string["+sn+"]}";}
				} catch (NumberFormatException e) {returnstring=sn+" is not a number!{string["+sn+"]}";}
			} else if (func.equalsIgnoreCase("REPLACE")) {
				if (args.contains(",")) {
					String replaced = args.substring(0, args.indexOf(","));
					String replacer = args.substring(args.indexOf(",")+1);
					if (replaced!=null) {
						if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1).replace(replaced, replacer));} 
						else {global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1).replace(replaced, replacer));}
						returnstring = "{string["+sn+"]}";
					} else {returnstring = "Improper format! use replace(toreplace,replacement){string["+sn+"]}";}
				} else {
					if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1).replace(args, ""));} 
					else {global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1).replace(args, ""));}
					returnstring = "{string["+sn+"]}";
				}
			} else if (func.equalsIgnoreCase("PREFIX")) {
				if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, args + global.USR_string.get(stringnum).get(1));} 
				else {global.TMP_string.get(tmpstringnum).set(1, args + global.TMP_string.get(tmpstringnum).get(1));}
				returnstring = "{string["+sn+"]}";
			} else if (func.equalsIgnoreCase("SUFFIX")) {
				if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, args + global.USR_string.get(stringnum).get(1));} 
				else {global.TMP_string.get(tmpstringnum).set(1, args + global.TMP_string.get(tmpstringnum).get(1));}
				returnstring = "{string["+sn+"]}";
			} else if (func.equalsIgnoreCase("TOUPPER") || func.equalsIgnoreCase("TOUPPERCASE")) {
				if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1).toUpperCase());} 
				else {global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1).toUpperCase());}
				returnstring = "{string["+sn+"]}";
			} else if (func.equalsIgnoreCase("TOLOWER") || func.equalsIgnoreCase("TOLOWERCASE")) {
				if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1).toLowerCase());} 
				else {global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1).toLowerCase());}
				returnstring = "{string["+sn+"]}";
			} else if (func.equalsIgnoreCase("IMPORTJSONFILE")){
				String sj = "Improper format! use importJsonFile(file,node)";
				if (args.contains(",")) {
					sj = file.importJsonFile("string", args.substring(0, args.indexOf(",")), sn + "=>" + args.substring(args.indexOf(",")+1));
					if (stringnum!=-1) {
						global.USR_string.get(stringnum).set(1, sj);
						backupUSR_strings.get(stringnum).set(1, sj);
					} else {
						global.TMP_string.get(tmpstringnum).set(1, sj);
						backupTMP_strings.get(tmpstringnum).set(1, sj);
					}
				} else {
					if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, sj);} 
					else {global.TMP_string.get(tmpstringnum).set(1, sj);}
				}
				returnstring = "{string["+sn+"]}";
			} else if (func.equalsIgnoreCase("IMPORTJSONURL")) {
				String sj = "Improper format! use importJsonFile(file,node)";
				if (args.contains(",")) {
					sj = file.importJsonURL("string", args.substring(0, args.indexOf(",")), sn + "=>" + args.substring(args.indexOf(",")+1));
					if (stringnum!=-1) {
						global.USR_string.get(stringnum).set(1, sj);
						backupUSR_strings.get(stringnum).set(1, sj);
					} else {
						global.TMP_string.get(tmpstringnum).set(1, sj);
						backupTMP_strings.get(tmpstringnum).set(1, sj);
					}
				} else {
					if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, sj);} 
					else {global.TMP_string.get(tmpstringnum).set(1, sj);}
				}
				returnstring = "{string["+sn+"]}";
			} else if (func.equalsIgnoreCase("EQUALS")) {
				if (stringnum!=-1) {
					if (global.USR_string.get(stringnum).get(1).equals(args)) {
						global.USR_string.get(stringnum).set(1, "true");
					} else {global.USR_string.get(stringnum).set(1, "false");}
				} else {
					if (global.TMP_string.get(tmpstringnum).get(1).equals(args)) {
						global.TMP_string.get(tmpstringnum).set(1, "true");
					} else {global.TMP_string.get(tmpstringnum).set(1, "false");}
				}
				returnstring = "{string["+sn+"]}";
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
				returnstring = "{string["+sn+"]}";
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
				returnstring = "{string["+sn+"]}";
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
				returnstring = "{string["+sn+"]}";
			} else {
				returnstring = func + " is not a function!{string["+sn+"]}";
			}
		}
		
		return returnstring;
	}
 
	public static String stringFunctions(String TMP_e) {
		
		while (TMP_e.contains("{string[") && TMP_e.contains("]}")) {
			String testfor = TMP_e.substring(TMP_e.indexOf("]}", TMP_e.indexOf("{string["))+2);
			if (testfor.contains("]}.") && !testfor.contains("{string[")) {
				if (testfor.indexOf("]}.") < testfor.indexOf("(")) {
					testfor = "."+testfor.substring(testfor.indexOf("]}.")+3);
				}
			}
			if (testfor.startsWith(".") && testfor.contains("(") && testfor.contains(")")) {
				if (testfor.substring(testfor.indexOf("."), testfor.indexOf("(")).contains(" ")) {
					testfor = testfor.substring(1);
				}
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
				args = args.replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(");
				args = args.replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")");
				TMP_e = TMP_e.replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(");
				TMP_e = TMP_e.replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")");
				String fullreplace = "{string["+sn+"]}."+func+"("+args+")";
				String firstpart = TMP_e.substring(0, TMP_e.indexOf(fullreplace));
				String secondpart = TMP_e.substring(TMP_e.indexOf(fullreplace)+fullreplace.length());
				
				TMP_e = firstpart + doStringFunctions(sn,func,args) + secondpart;
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
					
					TMP_e = efirst + replacement + esecond;
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
					if (global.USR_string.get(i).get(0).equals(sn)) {
						returnString = global.USR_string.get(i).get(1);
					}
				}
				if (returnString == "Not a string!") {
					for (int i=0; i<global.TMP_string.size(); i++) {
						if (global.TMP_string.get(i).get(0).equals(sn)) {
							returnString = global.TMP_string.get(i).get(1);
						}
					}
				}
				String fullreplace = "{string["+sn+"]}";
				String firstpart = TMP_e.substring(0, TMP_e.indexOf(fullreplace));
				String secondpart = TMP_e.substring(TMP_e.indexOf(fullreplace)+fullreplace.length());
				
				TMP_e = firstpart + returnString.replace(",", "stringCommaReplacementF6cyUQp9stringCommaReplacement") + secondpart;
				
				global.USR_string.clear();
				for (int i=0; i<backupUSR_strings.size(); i++) {
					String first = backupUSR_strings.get(i).get(0);
					String second = backupUSR_strings.get(i).get(1);
					List<String> temporary = new ArrayList<String>();
					temporary.add(first);
					temporary.add(second);
					global.USR_string.add(temporary);
				}
				global.TMP_string.clear();
				for (int i=0; i<backupTMP_strings.size(); i++) {
					String first = backupTMP_strings.get(i).get(0);
					String second = backupTMP_strings.get(i).get(1);
					List<String> temporary = new ArrayList<String>();
					temporary.add(first);
					temporary.add(second);
					global.TMP_string.add(temporary);
				}
			}
		}
		
		return TMP_e;
	}

	
	public static void doEvents(List<String> tmp_tmp_event, ClientChatReceivedEvent chatEvent, String[] toreplace, String[] replacement) {
		
		List<String> tmp_event = new ArrayList<String>(tmp_tmp_event);
		String stringCommaReplace = "stringCommaReplacementF6cyUQp9stringCommaReplacement";
		Boolean hasTempString = false;
		
		if (toreplace != null) {
			for (int i=0; i<toreplace.length; i++) {
				List<String> temporary = new ArrayList<String>();
				temporary.add("TriggerArgument"+i);
				temporary.add(replacement[i]);
				global.TMP_string.add(temporary);
			}
			for (int i=0; i<tmp_event.size(); i++) {
				for (int j=0; j<toreplace.length; j++) {
					tmp_event.set(i, tmp_event.get(i).replace(toreplace[j], "{string[TriggerArgument"+j+"]}"));
				}
			}
		}
		
		
		for (int i=0; i<tmp_event.size(); i++) {
			
		//SETUP
			String TMP_e = tmp_event.get(i);
			String TMP_c = "";
			if (!TMP_e.contains(" ")) {TMP_c = TMP_e; TMP_e="";}
			else {TMP_c = TMP_e.substring(0, TMP_e.indexOf(" ")); TMP_e = TMP_e.substring(TMP_e.indexOf(" ")+1, TMP_e.length());}
			int TMP_t = 50;
			int TMP_p = global.notifySize;
			int TMP_v = 10000;
			int TMP_pi = 1;
			
		//setup backup for functions so strings dont get overwritten
			backupUSR_strings.clear();
			for (int j=0; j<global.USR_string.size(); j++) {
				String first = global.USR_string.get(j).get(0);
				String second = global.USR_string.get(j).get(1);
				List<String> temporary = new ArrayList<String>();
				temporary.add(first);
				temporary.add(second);
				backupUSR_strings.add(temporary);
			}
			
			backupTMP_strings.clear();
			for (int j=0; j<global.TMP_string.size(); j++) {
				String first = global.TMP_string.get(j).get(0);
				String second = global.TMP_string.get(j).get(1);
				List<String> temporary = new ArrayList<String>();
				temporary.add(first);
				temporary.add(second);
				backupTMP_strings.add(temporary);
			}
			
		//built in strings
			if (chatEvent!=null) {
				if (TMP_e.contains("{msg}")) {
					List<String> temporary = new ArrayList<String>();
					temporary.add("DefaultString->MSG-"+(global.TMP_string.size()+1));
					temporary.add(chatEvent.message.getFormattedText());
					global.TMP_string.add(temporary);
					backupTMP_strings.add(temporary);
					TMP_e = TMP_e.replace("{msg}", "{string[DefaultString->MSG-"+global.TMP_string.size()+"]}");
					hasTempString = true;
				}
			}
			if (TMP_e.contains("{trigsize}")) {
				List<String> temporary = new ArrayList<String>();
				temporary.add("DefaultString->TRIGSIZE-"+(global.TMP_string.size()+1));
				temporary.add(global.trigger.size()+"");
				global.TMP_string.add(temporary);
				backupTMP_strings.add(temporary);
				TMP_e = TMP_e.replace("{trigsize}", "{string[DefaultString->TRIGSIZE-"+global.TMP_string.size()+"]}");
				hasTempString = true;
			}
			if (TMP_e.contains("{notifysize}")) {
				List<String> temporary = new ArrayList<String>();
				temporary.add("DefaultString->NOTIFYSIZE-"+(global.TMP_string.size()+1));
				temporary.add(global.notifySize+"");
				global.TMP_string.add(temporary);
				backupTMP_strings.add(temporary);
				TMP_e = TMP_e.replace("{notifysize}", "{string[DefaultString->NOTIFYSIZE-"+global.TMP_string.size()+"]}");
				hasTempString = true;
			}
			if (TMP_e.contains("{me}")) {
				List<String> temporary = new ArrayList<String>();
				temporary.add("DefaultString->ME-"+(global.TMP_string.size()+1));
				temporary.add(Minecraft.getMinecraft().thePlayer.getDisplayNameString());
				global.TMP_string.add(temporary);
				backupTMP_strings.add(temporary);
				TMP_e = TMP_e.replace("{me}", "{string[DefaultString->ME-"+global.TMP_string.size()+"]}");
				hasTempString = true;
			}
			if (TMP_e.contains("{server}")) {
				String current_server = "";
				if (Minecraft.getMinecraft().isSingleplayer()) {current_server = "SinglePlayer";} 
				else {current_server = Minecraft.getMinecraft().getCurrentServerData().serverName;}
				
				List<String> temporary = new ArrayList<String>();
				temporary.add("DefaultString->SERVER-"+(global.TMP_string.size()+1));
				temporary.add(current_server);
				global.TMP_string.add(temporary);
				backupTMP_strings.add(temporary);
				TMP_e = TMP_e.replace("{server}", "{string[DefaultString->SERVER-"+global.TMP_string.size()+"]}");
				hasTempString = true;
			}
			if (TMP_e.contains("{serverMOTD}")) {
				String returnString = "";
				if (Minecraft.getMinecraft().isSingleplayer()) {returnString = "Single Player world";}
				else {returnString = Minecraft.getMinecraft().getCurrentServerData().serverMOTD;}
				
				List<String> temporary = new ArrayList<String>();
				temporary.add("DefaultString->SERVERMOTD-"+(global.TMP_string.size()+1));
				temporary.add(returnString);
				global.TMP_string.add(temporary);
				backupTMP_strings.add(temporary);
				TMP_e = TMP_e.replace("{serverMOTD}", "{string[DefaultString->SERVERMOTD-"+global.TMP_string.size()+"]}");
				hasTempString = true;
			}
			if (TMP_e.contains("{serverIP}")) {
				String returnString = "";
				if (Minecraft.getMinecraft().isSingleplayer()) {returnString = "localhost";}
				else {returnString = Minecraft.getMinecraft().getCurrentServerData().serverIP;}
				
				List<String> temporary = new ArrayList<String>();
				temporary.add("DefaultString->SERVERIP-"+(global.TMP_string.size()+1));
				temporary.add(returnString);
				global.TMP_string.add(temporary);
				backupTMP_strings.add(temporary);
				TMP_e = TMP_e.replace("{serverIP}", "{string[DefaultString->SERVERIP-"+global.TMP_string.size()+"]}");
				hasTempString = true;
			}
			if (TMP_e.contains("{ping}")) {
				String returnString = "";
				if (Minecraft.getMinecraft().isSingleplayer()) {returnString = "5";}
				else {returnString = Minecraft.getMinecraft().getCurrentServerData().pingToServer+"";}
				
				List<String> temporary = new ArrayList<String>();
				temporary.add("DefaultString->SERVERPING-"+(global.TMP_string.size()+1));
				temporary.add(returnString);
				global.TMP_string.add(temporary);
				backupTMP_strings.add(temporary);
				TMP_e = TMP_e.replace("{ping}", "{string[DefaultString->SERVERPING-"+global.TMP_string.size()+"]}");
				hasTempString = true;
			}
			if (TMP_e.contains("{serverversion}")) {
				String returnString = "";
				if (Minecraft.getMinecraft().isSingleplayer()) {returnString = "1.8";}
				else {returnString = Minecraft.getMinecraft().getCurrentServerData().gameVersion;}
				
				List<String> temporary = new ArrayList<String>();
				temporary.add("DefaultString->SERVERVERSION-"+(global.TMP_string.size()+1));
				temporary.add(returnString);
				global.TMP_string.add(temporary);
				backupTMP_strings.add(temporary);
				TMP_e = TMP_e.replace("{serverversion}", "{string[DefaultString->SERVERVERSION-"+global.TMP_string.size()+"]}");
				hasTempString = true;
			}
			if (TMP_e.contains("{debug}")) {
				List<String> temporary = new ArrayList<String>();
				temporary.add("DefaultString->DEBUG-"+(global.TMP_string.size()+1));
				temporary.add(global.debug+"");
				global.TMP_string.add(temporary);
				backupTMP_strings.add(temporary);
				TMP_e = TMP_e.replace("{debug}", "{string[DefaultString->DEBUG-"+global.TMP_string.size()+"]}");
				hasTempString = true;
			}
			
		//user strings and functions
			TMP_e = TMP_e.replace("{string<", "{string[").replace("{array<", "{array[").replace(">}", "]}");
			
			if (TMP_e.contains("{array[")) {hasTempString=true;}
			TMP_e = stringFunctions(TMP_e);
			TMP_e = arrayFunctions(TMP_e);
			TMP_e = stringFunctions(TMP_e);
			
		//tags
			if (TMP_e.contains("<time=") && TMP_e.contains(">")) {
				String TMP_tstring = TMP_e.substring(TMP_e.indexOf("<time=")+6, TMP_e.indexOf(">",TMP_e.indexOf("<time=")));
				try {TMP_t = Integer.parseInt(TMP_tstring);}
				catch (NumberFormatException e) {e.printStackTrace();}
				TMP_e = TMP_e.replace("<time=" + TMP_tstring + ">", "");
			}
			if (TMP_e.contains("<pos=") && TMP_e.contains(">")) {
				String TMP_tstring = TMP_e.substring(TMP_e.indexOf("<pos=")+5, TMP_e.indexOf(">",TMP_e.indexOf("<pos=")));
				try {TMP_p = Integer.parseInt(TMP_tstring);}
				catch (NumberFormatException e) {e.printStackTrace();}
				TMP_e = TMP_e.replace("<pos=" + TMP_tstring + ">", "");
			}
			if (TMP_e.contains("<vol=") && TMP_e.contains(">")) {
				String TMP_tstring = TMP_e.substring(TMP_e.indexOf("<vol=")+5, TMP_e.indexOf(">",TMP_e.indexOf("<vol=")));
				try {TMP_v = Integer.parseInt(TMP_tstring);}
				catch (NumberFormatException e) {e.printStackTrace();}
				TMP_e = TMP_e.replace("<vol=" + TMP_tstring + ">", "");
			}
			if (TMP_e.contains("<pitch=") && TMP_e.contains(">")) {
				String TMP_tstring = TMP_e.substring(TMP_e.indexOf("<pitch=")+7, TMP_e.indexOf(">",TMP_e.indexOf("<pitch=")));
				try {TMP_pi = Integer.parseInt(TMP_tstring);}
				catch (NumberFormatException e) {e.printStackTrace();}
				TMP_e = TMP_e.replace("<pitch=" + TMP_tstring + ">", "");
			}
			
		//add formatting where needed
			if (TMP_c.equalsIgnoreCase("SAY") || TMP_c.equalsIgnoreCase("CHAT") || TMP_c.equalsIgnoreCase("KILLFEED") || TMP_c.equalsIgnoreCase("NOTIFY")) {
				if (TMP_c.equalsIgnoreCase("SAY")) {
					if (Minecraft.getMinecraft().isSingleplayer()==false) {
						TMP_e = chat.addFormatting(TMP_e);
					}
				} else {
					TMP_e = chat.addFormatting(TMP_e);
				}
			}
			
		//non-logic events
			if (TMP_c.equalsIgnoreCase("TRIGGER")) {doTrigger(TMP_e, chatEvent);}
			TMP_e = TMP_e.replace(stringCommaReplace, ",");
			if (TMP_c.equalsIgnoreCase("SAY")) {global.chatQueue.add(TMP_e);}
			if (TMP_c.equalsIgnoreCase("CHAT")) {chat.warn(TMP_e);}
			if (TMP_c.equalsIgnoreCase("SOUND")) {sound.play(TMP_e, TMP_v, TMP_pi);}
			if (TMP_c.equalsIgnoreCase("CANCEL") && chatEvent!=null) {chatEvent.setCanceled(true);}
			if (TMP_c.equalsIgnoreCase("KILLFEED")) {global.killfeed.add(TMP_e); global.killfeedDelay.add(TMP_t);}
			if (TMP_c.equalsIgnoreCase("NOTIFY")) {
				global.notify.add(TMP_e);
				List<Float> temp_list = new ArrayList<Float>();
				temp_list.add((float) 0);temp_list.add((float) -1000);
				temp_list.add((float) TMP_p);temp_list.add((float) TMP_t);
				temp_list.add((float) 0);temp_list.add((float) -1000);
				global.notifyAnimation.add(temp_list);
				global.notifySize++;
			}
			if (TMP_c.equalsIgnoreCase("COMMAND")) {global.commandQueue.add(TMP_e);}
			if (TMP_c.equalsIgnoreCase("COPY")) {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(new StringSelection(TMP_e), null);
			}
			
			
		//logic events
			if (TMP_c.equalsIgnoreCase("ASYNC")) { //TODO
				int tabbed_logic = 0;
				List<String> eventsToAsync = new ArrayList<String>();
				
				if (i+1 < tmp_event.size()-1) {
					for (int j=i; j<tmp_event.size(); j++) {
						if (j != tmp_event.size()) {
							//increase tab
							if (tmp_event.get(j).toUpperCase().startsWith("IF")
							|| tmp_event.get(j).toUpperCase().startsWith("FOR")
							|| tmp_event.get(j).toUpperCase().startsWith("CHOOSE")
							|| tmp_event.get(j).toUpperCase().startsWith("WAIT")
							|| tmp_event.get(j).toUpperCase().startsWith("ASYNC")) {
								tabbed_logic++;
							}
							
							//add to list
							eventsToAsync.add(tmp_event.get(j));
							
							//decrease tab
							if (tmp_event.get(j).toUpperCase().startsWith("END")) {tabbed_logic--;}
						}
					}
					
					i += eventsToAsync.size();
					eventsToAsync.remove(0);
					eventsToAsync.remove(eventsToAsync.size()-1);
					global.asyncEvents.clear();
					global.asyncEvents.addAll(eventsToAsync);
					Thread t1 = new Thread(new Runnable() {
					     public void run() {
					          events.doEvents(global.asyncEvents, null);
					     }
					});
					t1.start();
					
				}
			}
			
			if (TMP_c.equalsIgnoreCase("WAIT")) {
				int tabbed_logic = 0;
				List<String> eventsToWait = new ArrayList<String>();
				
				
				if (i+1 < tmp_event.size()-1) { //check for events after if event
					for (int j=i; j<tmp_event.size(); j++) {
						if (j != tmp_event.size()) {
							if (chatEvent!=null) {tmp_event.set(j, tmp_event.get(j).replace("{msg}", chatEvent.message.getFormattedText()));}
							
							//increase tab
							if (tmp_event.get(j).toUpperCase().startsWith("IF")
							|| tmp_event.get(j).toUpperCase().startsWith("FOR")
							|| tmp_event.get(j).toUpperCase().startsWith("CHOOSE")
							|| tmp_event.get(j).toUpperCase().startsWith("WAIT")
							|| tmp_event.get(j).toUpperCase().startsWith("ASYNC")) {
								tabbed_logic++;
							}
							
							//do functions
							tmp_event.set(j, stringFunctions(tmp_event.get(j)));
							tmp_event.set(j, arrayFunctions(tmp_event.get(j)));
							tmp_event.set(j, stringFunctions(tmp_event.get(j)));
							
							//add to list
							eventsToWait.add(tmp_event.get(j));
							
							//decrease tab
							if (tmp_event.get(j).toUpperCase().startsWith("END")) {tabbed_logic--;}
						}
						
						//check if exit
						if (tabbed_logic==0) {j=tmp_event.size();}
					}
					
					//move i to end of wait
					i += eventsToWait.size();
					eventsToWait.remove(0);
					eventsToWait.remove(eventsToWait.size()-1);
					try {
						global.waitEvents.add(eventsToWait);
						global.waitTime.add(Integer.parseInt(TMP_e));
					} catch (NumberFormatException e) {
						e.printStackTrace();
						chat.warn(chat.color("red", "Malformed WAIT event - skipping"));
					}
				}
			}
			
			if (TMP_c.equalsIgnoreCase("FOR")) {
				//setup
				int tabbed_logic = 0;
				List<String> eventsToFor = new ArrayList<String>();
				String[] tmp_valuefor = TMP_e.split(":");
				String valin = "";
				String valfrom = "";
				List<String> arrayto = new ArrayList<String>();
				if (tmp_valuefor.length==2) {
					valin = tmp_valuefor[0].trim();
					valfrom = tmp_valuefor[1].trim();
				} else {
					chat.warn(chat.color("red", "Malformed FOR loop!"));
				}
				for (int j=0; j<global.USR_array.size(); j++) {
					if (global.USR_array.get(j).get(0).equals(valfrom)) {
						arrayto.addAll(global.USR_array.get(j));
					}
				}
				
				if (i+1 < tmp_event.size()) {
					for (int j=i; j<tmp_event.size(); j++) {
						if (j != tmp_event.size()) {
							
							//increase tab
							if (tmp_event.get(j).toUpperCase().startsWith("IF")
							|| tmp_event.get(j).toUpperCase().startsWith("FOR")
							|| tmp_event.get(j).toUpperCase().startsWith("CHOOSE")
							|| tmp_event.get(j).toUpperCase().startsWith("WAIT")
							|| tmp_event.get(j).toUpperCase().startsWith("ASYNC")) {
								tabbed_logic++;
							}
							
							//add to list
							eventsToFor.add(tmp_event.get(j));
							
							//decrease tab
							if (tmp_event.get(j).toUpperCase().startsWith("END")) {tabbed_logic--;}
						}
						
						//check if exit
						if (tabbed_logic==0) {j=tmp_event.size();}
					}
				}
				
				eventsToFor.remove(0);
				eventsToFor.remove(eventsToFor.size()-1);
				
				
				if (arrayto.size()>0 && eventsToFor.size() > 0) {
					for (int j=1; j<arrayto.size(); j++) {
						String[] first = {valin};
						String[] second = {arrayto.get(j)};
						doEvents(eventsToFor, chatEvent, first, second);
					}
				}
				
				//move i
				i += eventsToFor.size();
			}
			
			if (TMP_c.equalsIgnoreCase("IF")) {
				int tabbed_logic = 0;
				List<String> eventsToIf = new ArrayList<String>();
				List<String> eventsToElse = new ArrayList<String>();
				Boolean gotoElse = false;
				
				if (i+1 < tmp_event.size()-1) { //check for events after if event
					for (int j=i; j<tmp_event.size(); j++) {
						if (j != tmp_event.size()) {
							
							//increase tab
							if (tmp_event.get(j).toUpperCase().startsWith("IF")
							|| tmp_event.get(j).toUpperCase().startsWith("FOR")
							|| tmp_event.get(j).toUpperCase().startsWith("CHOOSE")
							|| tmp_event.get(j).toUpperCase().startsWith("WAIT")
							|| tmp_event.get(j).toUpperCase().startsWith("ASYNC")) {
								tabbed_logic++;
							}
							
							//move to else
							if (tmp_event.get(j).toUpperCase().startsWith("ELSE") && tabbed_logic==1) {gotoElse=true;}
							
							//add to list
							if (gotoElse==false) {eventsToIf.add(tmp_event.get(j));} 
							else {eventsToElse.add(tmp_event.get(j));}
							
							//decrease tab
							if (tmp_event.get(j).toUpperCase().startsWith("END")) {tabbed_logic--;}
						}
						
						//check if exit
						if (tabbed_logic==0) {j=tmp_event.size();}
					}
					
					//move i to end of if
					i += eventsToIf.size()+eventsToElse.size()-2;
					
					//&& || ^
					String[] checkSplit = TMP_e.split(" ");
					for (int j=1; j<checkSplit.length; j++) {
						if (checkSplit[j].equals("&&")) {
							if (checkSplit[j-1].equalsIgnoreCase("TRUE") && checkSplit[j+1].equalsIgnoreCase("TRUE")) {
								checkSplit[j-1] = ""; checkSplit[j] = ""; checkSplit[j+1] = "TRUE";
							} else {
								checkSplit[j-1] = ""; checkSplit[j] = ""; checkSplit[j+1] = "FALSE";
							}
						}
						if (checkSplit[j].equals("||")) {
							if (checkSplit[j-1].equalsIgnoreCase("TRUE") || checkSplit[j+1].equalsIgnoreCase("TRUE")) {
								checkSplit[j-1] = ""; checkSplit[j] = ""; checkSplit[j+1] = "TRUE";
							} else {
								checkSplit[j-1] = ""; checkSplit[j] = ""; checkSplit[j+1] = "FALSE";
							}
						}
						if (checkSplit[j].equals("^")) {
							if (checkSplit[j-1].equalsIgnoreCase("TRUE") ^ checkSplit[j+1].equalsIgnoreCase("TRUE")) {
								checkSplit[j-1] = ""; checkSplit[j] = ""; checkSplit[j+1] = "TRUE";
							} else {
								checkSplit[j-1] = ""; checkSplit[j] = ""; checkSplit[j+1] = "FALSE";
							}
						}
					}
					TMP_e = "";
					for (String value : checkSplit) {TMP_e += value + " ";}
					TMP_e = TMP_e.trim();
					
					//check condition and do events
					if (TMP_e.equalsIgnoreCase("TRUE") || TMP_e.equalsIgnoreCase("NOT FALSE")) {
						if (eventsToIf.size()>0) {
							eventsToIf.remove(0);
							doEvents(eventsToIf, chatEvent);
						}
					} else {
						if (eventsToElse.size()>0) {
							eventsToElse.remove(0);
							doEvents(eventsToElse, chatEvent);
						}
					}
				}
			}
			
			
			if (TMP_c.equalsIgnoreCase("CHOOSE")) {
				int tabbed_logic = 0;
				List<List<String>> eventsToChoose = new ArrayList<List<String>>();
				List<String> eventsToChooseSub = new ArrayList<String>();
				
				if (i+1 < tmp_event.size()-1) { //check for events after choose event
					for (int j=i; j<tmp_event.size(); j++) {

						if (j != tmp_event.size()) {
							
							//increase tab
							if (tmp_event.get(j).toUpperCase().startsWith("IF")
							|| tmp_event.get(j).toUpperCase().startsWith("FOR")
							|| tmp_event.get(j).toUpperCase().startsWith("CHOOSE")
							|| tmp_event.get(j).toUpperCase().startsWith("WAIT")
							|| tmp_event.get(j).toUpperCase().startsWith("ASYNC")) {
								tabbed_logic++;
							}
							
							//check if first level event
							if (tabbed_logic==1) {
								if (eventsToChooseSub.size() > 0) { //add more than first level events
									List<String> tmp_list2nd = new ArrayList<String>(eventsToChooseSub);
									eventsToChoose.add(tmp_list2nd);
									eventsToChooseSub.clear(); //clears sub choice to add first level event
								}
								eventsToChooseSub.add(tmp_event.get(j));
								List<String> tmp_list1st = new ArrayList<String>(eventsToChooseSub);
								eventsToChoose.add(tmp_list1st); //add first level event
								eventsToChooseSub.clear(); //clear sub choose
							}
							
							//check if greater than first level event
							if (tabbed_logic>1) {
								eventsToChooseSub.add(tmp_event.get(j));
							}
							
							
							//check for last event to group and close any leftover sub choose
							if (j == tmp_event.size()-1 && eventsToChoose.size() > 0) {eventsToChoose.add(eventsToChooseSub);}
							
							//decrease tab
							if (tmp_event.get(j).toUpperCase().startsWith("END")) {tabbed_logic--;}
							
							//check again for first level event
							if (tabbed_logic==1) {
								if (eventsToChooseSub.size() > 0) {//add more than first level events
									List<String> tmp_list3rd = new ArrayList<String>(eventsToChooseSub);
									eventsToChoose.add(tmp_list3rd);
									eventsToChooseSub.clear(); //clear sub choose
								}
							}
						}
					
						//check if choose exit
						if (tabbed_logic==0) {j=tmp_event.size();}
					}
					
					//random number
					int rand = randInt(1,eventsToChoose.size()-2);
					
					//do events
					doEvents(eventsToChoose.get(rand), chatEvent);
					
					//move i to closing end
					int moveEvents = 0;
					for (int j=0; j<eventsToChoose.size(); j++) {moveEvents += eventsToChoose.get(j).size();}
					i += moveEvents-1;
				}
			}
		}
	}
	
	public static void doTrigger(String triggerName, ClientChatReceivedEvent chatEvent) {
		try {
			//run trigger by number
			int num = Integer.parseInt(triggerName);
			if (num >= 0 && num < global.trigger.size()) {
				//add all events to temp list
				List<String> TMP_events = new ArrayList<String>();
				for (int i=2; i<global.trigger.get(num).size(); i++) {TMP_events.add(global.trigger.get(num).get(i));}
				
				//do events
				doEvents(TMP_events, chatEvent);
			}
		} catch (NumberFormatException e1) { 
			//run trigger by name
			for (int k=0; k<global.trigger.size(); k++) {
				String TMP_trig = global.trigger.get(k).get(1);
				
				//remove tags
				String TMP_list = "";
				TMP_trig = TMP_trig.replace("<s>", ""); 
				TMP_trig = TMP_trig.replace("<c>", ""); 
				TMP_trig = TMP_trig.replace("<e>", "");
				TMP_trig = TMP_trig.replace("<start>", ""); 
				TMP_trig = TMP_trig.replace("<contain>", ""); 
				TMP_trig = TMP_trig.replace("<end>", "");
				if (TMP_trig.contains("<list=")) {TMP_list = TMP_trig.substring(TMP_trig.indexOf("<list=")+6, TMP_trig.indexOf(">", TMP_trig.indexOf("<list="))); TMP_trig = TMP_trig.replace("<list="+TMP_list+">","");}
				if (TMP_trig.contains("<server=")) {TMP_list = TMP_trig.substring(TMP_trig.indexOf("<server=")+8, TMP_trig.indexOf(">", TMP_trig.indexOf("<server="))); TMP_trig = TMP_trig.replace("<server="+TMP_list+">","");}
				
				//check match
				if (TMP_trig.equals(triggerName)) {
					//add all events to temp list
					List<String> TMP_events = new ArrayList<String>();
					for (int i=2; i<global.trigger.get(k).size(); i++) {TMP_events.add(global.trigger.get(k).get(i));}
					
					//do events
					doEvents(TMP_events, chatEvent);
				} else {
					if (TMP_trig.contains("(") && TMP_trig.endsWith(")")) {
						String TMP_trigtest = TMP_trig.substring(0,TMP_trig.indexOf("("));
						if (triggerName.startsWith(TMP_trigtest) && triggerName.endsWith(")")) {
							String TMP_argsIn = triggerName.substring(triggerName.indexOf("(")+1, triggerName.length()-1);
							String TMP_argsOut = TMP_trig.substring(TMP_trig.indexOf("(")+1, TMP_trig.length()-1);
							String[] argsIn = TMP_argsIn.split(",");
							String[] argsOut = TMP_argsOut.split(",");
							if (argsIn.length == argsOut.length) {
								List<String> TMP_events = new ArrayList<String>();
								for (int j=2; j<global.trigger.get(k).size(); j++) {
									TMP_events.add(global.trigger.get(k).get(j));
								}
								events.doEvents(TMP_events, chatEvent, argsOut, argsIn);
							}
						}
					}
				}
			}
		}
	}
	
	public static void eventTick() {
		for (int i=0; i<global.killfeedDelay.size(); i++) {
			if (global.killfeedDelay.get(i).intValue() == 0) {
				global.killfeed.remove(i);
				global.killfeedDelay.remove(i);
			} else {
				global.killfeedDelay.set(i, global.killfeedDelay.get(i).intValue() - 1);
			}
		}
		
		if (global.waitEvents.size()>0) {
			if (global.waitEvents.size() == global.waitTime.size()) {
				
				for (int i=0; i<global.waitTime.size(); i++) {
					if (global.waitTime.get(i)>0) {
						global.waitTime.set(i, global.waitTime.get(i)-1);
					} else {
						doEvents(global.waitEvents.get(i), null);
						global.waitEvents.remove(i);
						global.waitTime.remove(i);
					}
				}
			} else {
				chat.warn(chat.color("red","SOMETHING WENT WRONG!!!"));
				global.waitEvents.clear();
				global.waitTime.clear();
			}
		}
	}
	
	public static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
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
								temporary.add("TEMP-USER-STRING-"+(global.TMP_string.size())+"->"+stringName);
								temporary.add("");
								global.TMP_string.add(temporary);
								tmpstringnum = global.TMP_string.size()-1;
								global.temporary_replace.add("{string["+stringName+"]");
								global.temporary_replacement.add("{string[TEMP-USER-STRING-"+(global.TMP_string.size()-1)+"->"+stringName+"]"); 
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
								temporary.add("TEMP-USER-STRING-"+(global.TMP_string.size())+"->"+stringName);
								temporary.add("");
								global.TMP_string.add(temporary);
								tmpstringnum = global.TMP_string.size()-1;
								global.temporary_replace.add("{string["+stringName+"]");
								global.temporary_replacement.add("{string[TEMP-USER-STRING-"+(global.TMP_string.size()-1)+"->"+stringName+"]");
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
								temporary.add("TEMP-USER-STRING-"+(global.TMP_string.size())+"->"+stringName);
								temporary.add("");
								global.TMP_string.add(temporary);
								tmpstringnum = global.TMP_string.size()-1;
								global.temporary_replace.add("{string["+stringName+"]");
								global.temporary_replacement.add("{string[TEMP-USER-STRING-"+(global.TMP_string.size()-1)+"->"+stringName+"]");
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
