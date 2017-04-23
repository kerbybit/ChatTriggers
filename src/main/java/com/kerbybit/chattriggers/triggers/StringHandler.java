package com.kerbybit.chattriggers.triggers;

import java.io.*;
import java.util.*;

import com.kerbybit.chattriggers.objects.ArrayHandler;
import com.kerbybit.chattriggers.objects.DisplayHandler;
import com.kerbybit.chattriggers.objects.ListHandler;
import org.apache.commons.lang3.text.WordUtils;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.commands.CommandReference;
import com.kerbybit.chattriggers.file.FileHandler;
import com.kerbybit.chattriggers.file.JsonHandler;
import com.kerbybit.chattriggers.globalvars.global;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class StringHandler {

	
	public static void resetBackupStrings() {
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
        while (args.contains("{display[") && args.contains("]}")) {
            args = DisplayHandler.displayFunctions(args);
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
                    if (stringnum!=-1) {global.USR_string.get(stringnum).set(1,rtnmbr+"");}
                    else {global.TMP_string.get(tmpstringnum).set(1,rtnmbr+"");}
                } catch (NumberFormatException e) {
                    if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", args + " is not a number!"));}
                }
            } catch (NumberFormatException e) {
                if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", sn+" is not a number!"));}
            }
            return "{string["+sn+"]}";
        } else if (func.equalsIgnoreCase("FLOOR")) {
            try {
                Float strnmbr;
                if (stringnum != 1) {strnmbr = Float.parseFloat(global.USR_string.get(stringnum).get(1).replace(",",""));}
                else {strnmbr = Float.parseFloat(global.TMP_string.get(tmpstringnum).get(1).replace(",", ""));}
                Double rtnmbr = Math.floor(strnmbr);
                if (stringnum!=-1) {global.USR_string.get(stringnum).set(1,rtnmbr+"");}
                else {global.TMP_string.get(tmpstringnum).set(1,rtnmbr+"");}
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
		} else if (func.equalsIgnoreCase("REPLACEIGNORECASE")) {
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
                        .replaceAll("(?i)"+replaced, replacer));}
                else {global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1)
                        .replace("'('", "stringOpenBracketF6cyUQp9stringOpenBracket")
                        .replace("')'", "stringCloseBracketF6cyUQp9stringCloseBracket")
                        .replace("','", "stringCommaReplacementF6cyUQp9stringCommaReplacement")
                        .replaceAll("(?i)"+replaced, replacer));}
                return "{string["+sn+"]}";
            } else {
                if (stringnum!=-1) {global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1)
                        .replace("(","stringOpenBracketF6cyUQp9stringOpenBracket")
                        .replace(")","stringCloseBracketF6cyUQp9stringCloseBracket")
                        .replace(",", "stringCommaReplacementF6cyUQp9stringCommaReplacement")
                        .replaceAll("(?i)"+args, ""));}
                else {global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1)
                        .replace("(","stringOpenBracketF6cyUQp9stringOpenBracket")
                        .replace(")","stringCloseBracketF6cyUQp9stringCloseBracket")
                        .replace(",", "stringCommaReplacementF6cyUQp9stringCommaReplacement")
                        .replaceAll("(?i)"+args, ""));}
                return "{string["+sn+"]}";
            }
        } else if (func.equalsIgnoreCase("SUBSTRING")) {
            args = args
					.replace("'('", "stringOpenBracketF6cyUQp9stringOpenBracket")
					.replace("')'", "stringCloseBracketF6cyUQp9stringCloseBracket")
					.replace("','", "stringCommaReplacementF6cyUQp9stringCommaReplacement");
            String[] subargs = args.split(",");
            if (subargs.length == 2) {
                int first = -1;
                int last = -1;
                Boolean getStart = false;
                Boolean startContain = false;
                Boolean getEnd = false;
                Boolean endContain = false;
                if (subargs[0].toUpperCase().contains("<START>") || subargs[0].toUpperCase().contains("<S>")) {
                    getStart = true;
                    subargs[0] = subargs[0].replaceAll("(?i)<start>","").replaceAll("(?i)<s>","");
                }
                if (subargs[0].toUpperCase().contains("<INCLUDE>") || subargs[0].toUpperCase().contains("<I>")) {
                    startContain = true;
                    subargs[0] = subargs[0].replaceAll("(?i)<include>","").replaceAll("(?i)<i>","");
                }
                if (subargs[1].toUpperCase().contains("<END>") || subargs[1].toUpperCase().contains("<E>")) {
                    getEnd = true;
                    subargs[1] = subargs[1].replaceAll("(?i)<end>","").replaceAll("(?i)<e>","");
                }
                if (subargs[1].toUpperCase().contains("<INCLUDE>") || subargs[1].toUpperCase().contains("<I>")) {
                    endContain = true;
                    subargs[1] = subargs[1].replaceAll("(?i)<include>","").replaceAll("(?i)<i>","");
                }

                if (stringnum!=-1) {
                    String temp = global.USR_string.get(stringnum).get(1)
                            .replace("(","stringOpenBracketF6cyUQp9stringOpenBracket")
                            .replace(")","stringCloseBracketF6cyUQp9stringCloseBracket")
                            .replace(",", "stringCommaReplacementF6cyUQp9stringCommaReplacement");
                    if (getStart) {first = 0;}
                    if (getEnd) {last = temp.length();}
                    if (first == -1) {
                        if (temp.contains(subargs[0])) {
                            if (startContain) {
                                first = temp.indexOf(subargs[0]);
                            } else {
                                first = temp.indexOf(subargs[0]) + subargs[0].length();
                            }
                        } else {
                            try {
                                first = Integer.parseInt(subargs[0]);
                            } catch (NumberFormatException e) {
                                global.USR_string.get(stringnum).set(1,"false");
                                if (global.debug) {ChatHandler.warn(ChatHandler.color("gray","Did not find " + subargs[0] + " in string"));}
                            }
                        }
                    }
                    if (last == -1) {
                        if (temp.contains(subargs[1])) {
                            if (endContain) {
                                last = temp.indexOf(subargs[1]) + subargs[1].length();
                            } else {
                                last = temp.indexOf(subargs[1]);
                            }
                        } else {
                            try {
                                last = Integer.parseInt(subargs[1]);
                            } catch (NumberFormatException e) {
                                global.USR_string.get(stringnum).set(1,"false");
                                if (global.debug) {ChatHandler.warn(ChatHandler.color("gray","Did not find " + subargs[1] + " in string"));}
                            }
                        }
                    }

                    if (first!=-1 && last!=-1) {
                        temp = temp.substring(first, last);
                        global.USR_string.get(stringnum).set(1, temp
                                .replace("stringOpenBracketF6cyUQp9stringOpenBracket","(")
                                .replace("stringCloseBracketF6cyUQp9stringCloseBracket",")")
                                .replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
                    }
                } else {
                    String temp = global.TMP_string.get(tmpstringnum).get(1)
                            .replace("(","stringOpenBracketF6cyUQp9stringOpenBracket")
                            .replace(")","stringCloseBracketF6cyUQp9stringCloseBracket")
                            .replace(",", "stringCommaReplacementF6cyUQp9stringCommaReplacement");
                    if (getStart) {first = 0;}
                    if (getEnd) {last = temp.length();}
                    if (first == -1) {
                        if (temp.contains(subargs[0])) {
                            if (startContain) {
                                first = temp.indexOf(subargs[0]);
                            } else {
                                first = temp.indexOf(subargs[0]) + subargs[0].length();
                            }
                        } else {
                            try {
                                last = Integer.parseInt(subargs[0]);
                            } catch (NumberFormatException e) {
                                global.TMP_string.get(tmpstringnum).set(1, "false");
                                if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", "Did not find " + subargs[0] + " in string"));}
                            }
                        }
                    }
                    if (last == -1) {
                        if (temp.contains(subargs[1])) {
                            if (endContain) {
                                last = temp.indexOf(subargs[1]) + subargs[1].length();
                            } else {
                                last = temp.indexOf(subargs[1]);
                            }
                        } else {
                            try {
                                last = Integer.parseInt(subargs[1]);
                            } catch (NumberFormatException e) {
                                global.TMP_string.get(tmpstringnum).set(1, "false");
                                if (global.debug) {ChatHandler.warn(ChatHandler.color("gray", "Did not find " + subargs[1] + " in string"));}
                            }
                        }
                    }

                    if (first!=-1 && last!=-1) {
                        temp = temp.substring(first, last);
                        global.TMP_string.get(tmpstringnum).set(1, temp
                                .replace("stringOpenBracketF6cyUQp9stringOpenBracket","(")
                                .replace("stringCloseBracketF6cyUQp9stringCloseBracket",")")
                                .replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ","));
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
			else {global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1) + args);}
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
				Double strnmbr;
				if (stringnum!=-1) {strnmbr = Double.parseDouble(global.USR_string.get(stringnum).get(1));}
				else {strnmbr = Double.parseDouble(global.TMP_string.get(tmpstringnum).get(1));}
				try {
                    Double argnmbr = Double.parseDouble(args);
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
                Double strnmbr;
				if (stringnum!=-1) {strnmbr = Double.parseDouble(global.USR_string.get(stringnum).get(1));}
				else {strnmbr = Double.parseDouble(global.TMP_string.get(tmpstringnum).get(1));}
				try {
                    Double argnmbr = Double.parseDouble(args);
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
                Double strnmbr;
				if (stringnum!=-1) {strnmbr = Double.parseDouble(global.USR_string.get(stringnum).get(1));}
				else {strnmbr = Double.parseDouble(global.TMP_string.get(tmpstringnum).get(1));}
				try {
                    Double argnmbr = Double.parseDouble(args);
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
                Double strnmbr;
				if (stringnum!=-1) {strnmbr = Double.parseDouble(global.USR_string.get(stringnum).get(1));}
				else {strnmbr = Double.parseDouble(global.TMP_string.get(tmpstringnum).get(1));}
				try {
                    Double argnmbr = Double.parseDouble(args);
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
		} else if (func.equalsIgnoreCase("CAPITALIZEFIRSTWORD") || func.equalsIgnoreCase("CAPFIRST")) {
			if (stringnum!=-1) {
                try {
                    global.USR_string.get(stringnum).set(1, global.USR_string.get(stringnum).get(1).substring(0, 1).toUpperCase() + global.USR_string.get(stringnum).get(1).substring(1));
                } catch (Exception e) {
                    //do nothing
                    //string is empty
                }
            } else {
                try {
                    global.TMP_string.get(tmpstringnum).set(1, global.TMP_string.get(tmpstringnum).get(1).substring(0, 1).toUpperCase() + global.TMP_string.get(tmpstringnum).get(1).substring(1));
                } catch (Exception e) {
                    //do nothing
                    //string is empty
                }
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
                value = value.replace("...","TripleDotF6cyUQp9TripleDot");
                if (value.contains(".")) {
                    if (!(newvalue.toUpperCase().startsWith("HTTP://") || newvalue.toUpperCase().startsWith("HTTPS://"))) {
                        newvalue = "http://"+value;
                    }
                    tmp_string = tmp_string.replace(value.replace("...","TripleDotF6cyUQp9TripleDot"), "{link[" + value + "],[" + newvalue + "]}");
                }
            }

            if (stringnum!=-1) {
                global.USR_string.get(stringnum).set(1,tmp_string);
            } else {
                global.TMP_string.get(tmpstringnum).set(1,tmp_string);
            }
            return "{string["+sn+"]}";
        } else if (func.equalsIgnoreCase("SPLIT")) {
		    StringBuilder tmp_string;
		    if (stringnum!=-1) {
		        tmp_string = new StringBuilder(global.USR_string.get(stringnum).get(1));
            } else {
		        tmp_string = new StringBuilder(global.TMP_string.get(tmpstringnum).get(1));
            }

            String[] split_tmp_string = tmp_string.toString().split(args);
		    tmp_string = new StringBuilder("[");
		    for (String value : split_tmp_string) {
		        tmp_string.append(value).append(",");
            }
            tmp_string = new StringBuilder(tmp_string.substring(0, tmp_string.length()-1) + "]");

            ListHandler.getList("StringToList->"+sn+"SPLIT-"+(ListHandler.getListsSize()+1), tmp_string.toString());
		    return "{list[StringToList->"+sn+"SPLIT-"+ListHandler.getListsSize()+"]}";
        } else {
            for (List<String> function : global.function) {
                if (function.size() > 2) {
                    String func_define = function.get(1);
                    if (func_define.contains(".") && func_define.contains("(") && func_define.contains(")")) {
                        String func_name = func_define.substring(func_define.indexOf(".")+1, func_define.indexOf("(", func_define.indexOf(".")));
                        if (func_name.equals(func)) {
                            String func_to = TagHandler.removeTags(func_define.substring(0, func_define.indexOf(".")));
                            String func_arg = func_define.substring(func_define.indexOf("(")+1, func_define.indexOf(")", func_define.indexOf(")")));
                            if (!func_arg.equals("")) {
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
                                if (args.equals("")) {
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
            }
            if (global.debug) {
                ChatHandler.warn(ChatHandler.color("gray", func + " is not a function!"));
            }
            return "{string[" + sn + "]}";
		}
	}
	
	public static String stringFunctions(String TMP_e, ClientChatReceivedEvent chatEvent) {
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
				    System.out.println(TMP_e);
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
				    try {
                        for (int i=0; i<global.TMP_string.size(); i++) {
                            if (global.TMP_string.get(i).get(0).equals(sn)) {
                                returnString = global.TMP_string.get(i).get(1);
                            }
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        returnString = "Not a string!";
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
				StringBuilder trigSB = new StringBuilder();
				for (String value : split_trig) {trigSB.append(value);}
				trig = trigSB.toString();
			}
		}
		return trig;
	}
}
