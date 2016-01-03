package com.kerbybit.chattriggers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class events {
	
	
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
	
	public static void doEvents(List<String> tmp_tmp_event, ClientChatReceivedEvent chatEvent) {
		List<String> tmp_event = new ArrayList<String>(tmp_tmp_event);
		String[] snull = null;
		doEvents(tmp_event, chatEvent, snull, snull);
	}
	
	public static void doEvents(List<String> tmp_tmp_event, ClientChatReceivedEvent chatEvent, String[] toreplace, String[] replacement) {
		
		List<String> tmp_event = new ArrayList<String>(tmp_tmp_event);
		String stringInterrupt = "stringInterruptorF6cyUQp9stringInterruptor";
		String stringCommaReplace = "stringCommaReplacementF6cyUQp9stringCommaReplacement";
		for (int i=0; i<tmp_event.size(); i++) {
			
		//SETUP
			String TMP_e = tmp_event.get(i);
			String TMP_c = "";
			if (!TMP_e.contains(" ")) {TMP_c = TMP_e; TMP_e="";}
			else {TMP_c = TMP_e.substring(0, TMP_e.indexOf(" ")); TMP_e = TMP_e.substring(TMP_e.indexOf(" ")+1, TMP_e.length());}
			int TMP_t = 50;
			int TMP_p = 0;
			int TMP_v = 10000;
			int TMP_pi = 1;
			if (toreplace != null) {
				for (int j=0; j<toreplace.length; j++) {
					TMP_e = TMP_e.replace(toreplace[j], stringInterrupt+replacement[j].replace(",",stringCommaReplace));
				}
			}
			
		//built in strings
			if (chatEvent!=null) {TMP_e = TMP_e.replace("{msg}", chatEvent.message.getFormattedText());}
			TMP_e = TMP_e.replace("{trigsize}", stringInterrupt+global.trigger.size()+"");
			TMP_e = TMP_e.replace("{notifysize}", stringInterrupt+global.notifySize+"");
			TMP_e = TMP_e.replace("{me}", stringInterrupt+Minecraft.getMinecraft().thePlayer.getDisplayNameString());
			if (TMP_e.contains("{server}")) {
				String current_server = "";
				if (Minecraft.getMinecraft().isSingleplayer()) {current_server = "SinglePlayer";} 
				else {current_server = Minecraft.getMinecraft().getCurrentServerData().serverIP;}
				TMP_e = TMP_e.replace("{server}", stringInterrupt+current_server);
			}
			
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
			
		//user strings
			TMP_e = TMP_e.replace("{string<", "{string[");
			TMP_e = TMP_e.replace(">}", "]}");
			while (TMP_e.contains("{string[") && TMP_e.contains("]}")) {
				String TMP_test = TMP_e.substring(0, TMP_e.indexOf("]}"));
				String TMP_s = TMP_test.substring(TMP_e.indexOf("{string[") + 8, TMP_e.indexOf("]}"));
				String TMP_sn = "";
				for (int k=0; k<global.USR_string.size(); k++) {
					if (TMP_s.equals(global.USR_string.get(k).get(0))) {
						TMP_sn = global.USR_string.get(k).get(1);
					}
				}
				TMP_e = TMP_e.replace("{string[" + TMP_s + "]}", stringInterrupt + TMP_sn.replace(",", stringCommaReplace));
				TMP_e = TMP_e.replace("{string<", "{string[");
				TMP_e = TMP_e.replace(">}", "]}");
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
			
		//functions
			while (TMP_e.contains(".replace(") && TMP_e.contains(")")) {
				String checkFrom = TMP_e.substring(0, TMP_e.indexOf(".replace("));
				String[] checkTo = TMP_e.substring(TMP_e.indexOf(".replace(")+9, TMP_e.indexOf(")", TMP_e.indexOf(".replace("))).split(",");
				
				while (checkFrom.contains(stringInterrupt)) {
					checkFrom = checkFrom.substring(checkFrom.indexOf(stringInterrupt) + stringInterrupt.length(),checkFrom.length());
				}
				
				if (checkTo.length==2) {
					TMP_e = TMP_e.replace(checkFrom + ".replace(" + checkTo[0] + "," + checkTo[1] + ")", checkFrom.replace(checkTo[0], checkTo[1]));
				} else if (checkTo.length==1) {
					TMP_e = TMP_e.replace(checkFrom + ".replace(" + checkTo[0] + ",)", checkFrom.replace(checkTo[0], ""));
					TMP_e = TMP_e.replace(checkFrom + ".replace(" + checkTo[0] + ")", checkFrom.replace(checkTo[0], ""));
				} else if (checkTo.length==0) {
					chat.warn(chat.color("red", "Malformed .replace(toReplace,replacement) function - skipping"));
					TMP_e = TMP_e.replace(checkFrom + ".replace(,)", checkFrom.replace(checkTo[0], ""));
					TMP_e = TMP_e.replace(checkFrom + ".replace()", checkFrom.replace(checkTo[0], ""));
				}
			}
			
			while (TMP_e.contains(".toUpper()")) {
				String checkFrom = TMP_e.substring(0, TMP_e.indexOf(".toUpper()"));
				
				while (checkFrom.contains(stringInterrupt)) {
					checkFrom = checkFrom.substring(checkFrom.indexOf(stringInterrupt) + stringInterrupt.length(),checkFrom.length());
				}
				
				TMP_e = TMP_e.replace(checkFrom + ".toUpper()", checkFrom.toUpperCase().replace(stringInterrupt.toUpperCase(), stringInterrupt).replace(stringCommaReplace.toUpperCase(), stringCommaReplace));
			}
			
			while (TMP_e.contains(".toLower()")) {
				String checkFrom = TMP_e.substring(0, TMP_e.indexOf(".toLower()"));
				
				while (checkFrom.contains(stringInterrupt)) {
					checkFrom = checkFrom.substring(checkFrom.indexOf(stringInterrupt) + stringInterrupt.length(),checkFrom.length());
				}
				
				TMP_e = TMP_e.replace(checkFrom + ".toLower()", checkFrom.toLowerCase().replace(stringInterrupt.toLowerCase(), stringInterrupt).replace(stringCommaReplace.toLowerCase(), stringCommaReplace));
			}
			
			while (TMP_e.contains(".prefix(") && TMP_e.contains(")")) {
				String checkFrom = TMP_e.substring(0, TMP_e.indexOf(".prefix("));
				String checkTo = TMP_e.substring(TMP_e.indexOf(".prefix(")+8, TMP_e.indexOf(")"));
				
				while (checkFrom.contains(stringInterrupt)) {
					checkFrom = checkFrom.substring(checkFrom.indexOf(stringInterrupt) + stringInterrupt.length(),checkFrom.length());
				}
				
				TMP_e = TMP_e.replace(checkFrom + ".prefix(" + checkTo + ")", checkTo.replace(stringInterrupt, "") + checkFrom);
			}
			
			while (TMP_e.contains(".suffix(") && TMP_e.contains(")")) {
				String checkFrom = TMP_e.substring(0, TMP_e.indexOf(".suffix("));
				String checkTo = TMP_e.substring(TMP_e.indexOf(".suffix(")+8, TMP_e.indexOf(")"));
				
				while (checkFrom.contains(stringInterrupt)) {
					checkFrom = checkFrom.substring(checkFrom.indexOf(stringInterrupt) + stringInterrupt.length(),checkFrom.length());
				}
				
				TMP_e = TMP_e.replace(checkFrom + ".suffix(" + checkTo + ")", checkFrom + checkTo.replace(stringInterrupt, ""));
			}
			
			while (TMP_e.contains("{array[") && TMP_e.contains("]}.add(") && TMP_e.contains(")")) {
				String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.add(", TMP_e.indexOf("{array[")));
				String checkTo = TMP_e.substring(TMP_e.indexOf("]}.add(")+7, TMP_e.indexOf(")", TMP_e.indexOf("]}.add(")));
				Boolean isArray = false;
				
				for (int j=0; j<global.USR_array.size(); j++) {
					if (global.USR_array.get(j).get(0).equals(checkFrom)) {
						global.USR_array.get(j).add(checkTo.replace(stringInterrupt, ""));
						isArray = true;
					}
				}
				
				if (isArray == false) {
					List<String> prearray = new ArrayList<String>();
					prearray.add(checkFrom);
					prearray.add(checkTo);
					global.USR_array.add(prearray);
				}
				
				TMP_e = TMP_e.replace("{array[" + checkFrom + "]}.add(" + checkTo + ")", stringInterrupt+checkTo.replace(stringInterrupt, ""));
			}
			
			while (TMP_e.contains("{array[") && TMP_e.contains("]}.clear()")) {
				String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.clear()", TMP_e.indexOf("{array[")));
				
				for (int j=0; j<global.USR_array.size(); j++) {
					if (global.USR_array.get(j).get(0).equals(checkFrom)) {
						global.USR_array.remove(j);
					}
				}
				
				TMP_e = TMP_e.replace("{array[" + checkFrom + "]}.clear()", stringInterrupt+checkFrom + " cleared.");
			}
			
			while (TMP_e.contains("{array[") && TMP_e.contains("]}.has(") && TMP_e.contains(")")) {
				String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.has(", TMP_e.indexOf("{array[")));
				String checkTo = TMP_e.substring(TMP_e.indexOf("]}.has(")+7, TMP_e.indexOf(")", TMP_e.indexOf("]}.has(")));
				String checkThis = "false";
				
				for (int j=0; j<global.USR_array.size(); j++) {
					if (global.USR_array.get(j).get(0).equals(checkFrom)) {
						for (int k=1; k<global.USR_array.get(j).size(); k++) {
							if (global.USR_array.get(j).get(k).equals(checkTo.replace(stringInterrupt, ""))) {
								checkThis = "true";
							}
						}
					}
				}
				
				TMP_e = TMP_e.replace("{array["+checkFrom+"]}.has("+checkTo+")", checkThis);
			}
			
			while (TMP_e.contains("{array[") && TMP_e.contains("]}.remove(") && TMP_e.contains(")")) {
				String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.remove(", TMP_e.indexOf("{array[")));
				String checkTo = TMP_e.substring(TMP_e.indexOf("]}.remove(")+10, TMP_e.indexOf(")", TMP_e.indexOf("]}.remove(")));
				String removed = "";
				int toRemove = -1;
				int toRemoveArray = -1;
				
				try {
					toRemove = Integer.parseInt(checkTo.replace(stringInterrupt, ""));
					if (toRemove > 0) {
						for (int j=0; j<global.USR_array.size(); j++) {
							if (global.USR_array.get(j).get(0).equals(checkFrom)) {
								if (toRemove < global.USR_array.get(j).size()) {
									removed = global.USR_array.get(j).remove(toRemove);
									if (global.USR_array.get(j).size()==1) {
										toRemoveArray = j;
									}
								} else {
									chat.warn(chat.color("red", "{array}.remove($value) - $value over bounds (index " + toRemove + ")"));
								}
							}
						}
					} else {
						chat.warn(chat.color("red", "{array}.remove($value) - $value under bounds (index " + toRemove + ")"));
					}
				} catch (NumberFormatException e) {
					chat.warn(chat.color("red", "{array}.remove($value) - $value must be an integer!"));
				}
				
				if (toRemoveArray != -1) {
					global.USR_array.remove(toRemoveArray);
				}
				
				TMP_e = TMP_e.replace("{array[" + checkFrom + "]}.remove(" + checkTo + ")", stringInterrupt+removed);
			}
			
			while (TMP_e.contains("{array[") && TMP_e.contains("]}.size()")) {
				String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.size()", TMP_e.indexOf("{array[")));
				int arraysize = 0;
				
				for (int j=0; j<global.USR_array.size(); j++) {
					if (global.USR_array.get(j).get(0).equals(checkFrom)) {
						arraysize = global.USR_array.get(j).size()-1;
					}
				}
				
				TMP_e = TMP_e.replace("{array[" + checkFrom + "]}.size()", stringInterrupt+arraysize);
			}
			
			while (TMP_e.contains("{array[") && TMP_e.contains("]}.importJsonFile(") && TMP_e.contains(",") && TMP_e.contains(")")) {
				String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.importJsonFile(", TMP_e.indexOf("{array[")));
				String checkFile = TMP_e.substring(TMP_e.indexOf("]}.importJsonFile(")+18, TMP_e.indexOf(",", TMP_e.indexOf("{array["+checkFrom+"]}.importJsonFile(")));
				String checkTo = TMP_e.substring(TMP_e.indexOf(",", TMP_e.indexOf("{array["+checkFrom+"]}.importJsonFile("))+1, TMP_e.indexOf(")", TMP_e.indexOf("{array["+checkFrom+"]}.importJsonFile("+checkFile+",")));
				
				file.importJsonFile(checkFile.replace(stringInterrupt, ""), checkFrom + "=>" + checkTo.replace(stringInterrupt, ""));
				
				TMP_e = TMP_e.replace("{array["+checkFrom+"]}.importJsonFile("+checkFile+","+checkTo+")", "Imported json from "+checkFile.replace(stringInterrupt, ""));
			}
			
			while (TMP_e.contains("{array[") && TMP_e.contains("]}.importJsonURL(") && TMP_e.contains(",") && TMP_e.contains(")")) {
				String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.importJsonURL(", TMP_e.indexOf("{array[")));
				String checkFile = TMP_e.substring(TMP_e.indexOf("]}.importJsonURL(")+17, TMP_e.indexOf(",", TMP_e.indexOf("{array["+checkFrom+"]}.importJsonURL(")));
				String checkTo = TMP_e.substring(TMP_e.indexOf(",", TMP_e.indexOf("{array["+checkFrom+"]}.importJsonURL("))+1, TMP_e.indexOf(")", TMP_e.indexOf("{array["+checkFrom+"]}.importJsonURL("+checkFile+",")));
				
				file.importJsonURL(checkFile.replace(stringInterrupt, ""), checkFrom + "=>" + checkTo.replace(stringInterrupt, ""));
				
				TMP_e = TMP_e.replace("{array["+checkFrom+"]}.importJsonURL("+checkFile+","+checkTo+")", "Imported json from "+checkFile.replace(stringInterrupt, ""));
			}
			
			while (TMP_e.contains(".equals(") && TMP_e.contains(")")) {
				String checkFrom = TMP_e.substring(0, TMP_e.indexOf(".equals("));
				String checkTo = TMP_e.substring(TMP_e.indexOf(".equals(")+8, TMP_e.indexOf(")", TMP_e.indexOf(".equals(")));
				
				while (checkFrom.contains(stringInterrupt)) {
					checkFrom = checkFrom.substring(checkFrom.indexOf(stringInterrupt) + stringInterrupt.length(),checkFrom.length());
				}
				
				if (checkFrom.equals(checkTo.replace(stringInterrupt, ""))) {TMP_e = TMP_e.replace(checkFrom + ".equals(" + checkTo + ")", "true");} 
				else {TMP_e = TMP_e.replace(checkFrom + ".equals(" + checkTo + ")", "false");}
			}
			
			while (TMP_e.contains(".startsWith(") && TMP_e.contains(")")) {
				String checkFrom = TMP_e.substring(0, TMP_e.indexOf(".startsWith("));
				String checkTo = TMP_e.substring(TMP_e.indexOf(".startsWith(")+12, TMP_e.indexOf(")", TMP_e.indexOf(".startsWith(")));
				
				while (checkFrom.contains(stringInterrupt)) {
					checkFrom = checkFrom.substring(checkFrom.indexOf(stringInterrupt) + stringInterrupt.length(),checkFrom.length());
				}
				
				if (checkFrom.startsWith(checkTo.replace(stringInterrupt, ""))) {TMP_e = TMP_e.replace(checkFrom + ".startsWith(" + checkTo + ")", "true");} 
				else {TMP_e = TMP_e.replace(checkFrom + ".startsWith(" + checkTo + ")", "false");}
			}
			
			while (TMP_e.contains(".contains(") && TMP_e.contains(")")) {
				String checkFrom = TMP_e.substring(0, TMP_e.indexOf(".contains("));
				String checkTo = TMP_e.substring(TMP_e.indexOf(".contains(")+10, TMP_e.indexOf(")", TMP_e.indexOf(".contains(")));
				
				while (checkFrom.contains(stringInterrupt)) {
					checkFrom = checkFrom.substring(checkFrom.indexOf(stringInterrupt) + stringInterrupt.length(),checkFrom.length());
				}
				
				if (checkFrom.contains(checkTo.replace(stringInterrupt, ""))) {TMP_e = TMP_e.replace(checkFrom + ".contains(" + checkTo + ")", "true");} 
				else {TMP_e = TMP_e.replace(checkFrom + ".contains(" + checkTo + ")", "false");}
			}
			
			while (TMP_e.contains(".endsWith(") && TMP_e.contains(")")) {
				String checkFrom = TMP_e.substring(0, TMP_e.indexOf(".endsWith("));
				String checkTo = TMP_e.substring(TMP_e.indexOf(".endsWith(")+10, TMP_e.indexOf(")", TMP_e.indexOf(".endsWith(")));
				
				while (checkFrom.contains(stringInterrupt)) {
					checkFrom = checkFrom.substring(checkFrom.indexOf(stringInterrupt) + stringInterrupt.length(),checkFrom.length());
				}
				
				if (checkFrom.endsWith(checkTo.replace(stringInterrupt, ""))) {TMP_e = TMP_e.replace(checkFrom + ".endsWith(" + checkTo + ")", "true");} 
				else {TMP_e = TMP_e.replace(checkFrom + ".endsWith(" + checkTo + ")", "false");}
			}
			
			while (TMP_e.contains("string.set(") && TMP_e.contains(")")) {
				String[] args = TMP_e.substring(TMP_e.indexOf("string.set(") + 11,TMP_e.indexOf(")",TMP_e.indexOf("string.set("))).split(",");
				if (args.length==2) {
					try {
						int num = Integer.parseInt(args[0].replace(stringInterrupt, ""));
						if (num>0 && num<global.USR_string.size()) {
							global.USR_string.get(num).set(1, args[1].replace(stringInterrupt, ""));
							TMP_e = TMP_e.replace("string.set(" + args[0] + "," + args[1] + ")", args[1]);
						}
					} catch (NumberFormatException e) {
						for (int j=0; j<global.USR_string.size(); j++) {
							if (global.USR_string.get(j).get(0).equals(args[0].replace(stringInterrupt, ""))) {
								global.USR_string.get(j).set(1, args[1].replace(stringInterrupt, ""));
								TMP_e = TMP_e.replace("string.set(" + args[0] + "," + args[1] + ")", args[1]);
							}
						}
					}
				}
			}
			
			while (TMP_e.contains("string.save(") && TMP_e.contains(")")) {
				String[] args = TMP_e.substring(TMP_e.indexOf("string.save(") + 12,TMP_e.indexOf(")",TMP_e.indexOf("string.save("))).split(",");
				if (args.length==2) {
					try {
						int num = Integer.parseInt(args[0].replace(stringInterrupt, ""));
						if (num>0 && num<global.USR_string.size()) {
							global.USR_string.get(num).set(1, args[1].replace(stringInterrupt, ""));
							try {file.saveAll();} catch (IOException e) {chat.warn(chat.color("red", "Error saving triggers!"));}
							TMP_e = TMP_e.replace("string.save(" + args[0] + "," + args[1] + ")", args[1]);
						}
					} catch (NumberFormatException e) {
						for (int j=0; j<global.USR_string.size(); j++) {
							if (global.USR_string.get(j).get(0).equals(args[0].replace(stringInterrupt, ""))) {
								global.USR_string.get(j).set(1, args[1].replace(stringInterrupt, ""));
								try {file.saveAll();} catch (IOException e1) {chat.warn(chat.color("red", "Error saving triggers!"));}
								TMP_e = TMP_e.replace("string.save(" + args[0] + "," + args[1] + ")", args[1]);
							}
						}
					}
				}
			}
			
			
			
		//clear out interrupt
			TMP_e = TMP_e.replace(stringInterrupt, "");
			
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
				temp_list.add((float) 0);
				temp_list.add((float) 0);
				temp_list.add((float) TMP_p);
				temp_list.add((float) TMP_t);
				temp_list.add((float) 0);
				temp_list.add((float) 0);
				global.notifyAnimation.add(temp_list);
				global.notifySize++;
			}
			if (TMP_c.equalsIgnoreCase("COMMAND")) {global.commandQueue.add(TMP_e);}
			
			
		//logic events
			
			if (TMP_c.equalsIgnoreCase("WAIT")) {
				int tabbed_logic = 0;
				List<String> eventsToWait = new ArrayList<String>();
				Boolean gotoElse = false;
				
				if (i+1 < tmp_event.size()-1) { //check for events after if event
					for (int j=i; j<tmp_event.size(); j++) {
						if (j != tmp_event.size()) {
							//arguments
							if (toreplace != null) {
								for (int k=0; k<toreplace.length; k++) {
									tmp_event.set(j,tmp_event.get(j).replace(toreplace[k], stringInterrupt+replacement[k]));
								}
							}
							if (chatEvent!=null) {tmp_event.set(j, tmp_event.get(j).replace("{msg}", chatEvent.message.getFormattedText()));}
							
							//increase tab
							if (tmp_event.get(j).toUpperCase().startsWith("IF")
							|| tmp_event.get(j).toUpperCase().startsWith("FOR")
							|| tmp_event.get(j).toUpperCase().startsWith("CHOOSE")
							|| tmp_event.get(j).toUpperCase().startsWith("WAIT")) {
								tabbed_logic++;
							}
							
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
							//arguments
							if (toreplace != null) {
								for (int k=0; k<toreplace.length; k++) {
									tmp_event.set(j,tmp_event.get(j).replace(toreplace[k], stringInterrupt+replacement[k]));
								}
							}
							
							//increase tab
							if (tmp_event.get(j).toUpperCase().startsWith("IF")
							|| tmp_event.get(j).toUpperCase().startsWith("FOR")
							|| tmp_event.get(j).toUpperCase().startsWith("CHOOSE")
							|| tmp_event.get(j).toUpperCase().startsWith("WAIT")) {
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
						List<String> tmp_eventsToFor = new ArrayList<String>(eventsToFor);
						for (int k=0; k<eventsToFor.size(); k++) {
							tmp_eventsToFor.set(k, tmp_eventsToFor.get(k).replace(valin,arrayto.get(j)));
						}
						doEvents(tmp_eventsToFor, chatEvent);
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
							
							//arguments
							if (toreplace != null) {
								for (int k=0; k<toreplace.length; k++) {
									tmp_event.set(j,tmp_event.get(j).replace(toreplace[k], stringInterrupt+replacement[k]));
								}
							}
							
							//increase tab
							if (tmp_event.get(j).toUpperCase().startsWith("IF")
							|| tmp_event.get(j).toUpperCase().startsWith("FOR")
							|| tmp_event.get(j).toUpperCase().startsWith("CHOOSE")
							|| tmp_event.get(j).toUpperCase().startsWith("WAIT")) {
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
							
							//arguments
							if (toreplace != null) {
								for (int k=0; k<toreplace.length; k++) {
									tmp_event.set(j,tmp_event.get(j).replace(toreplace[k], stringInterrupt+replacement[k]));
								}
							}
							
							//increase tab
							if (tmp_event.get(j).toUpperCase().startsWith("IF")
							|| tmp_event.get(j).toUpperCase().startsWith("FOR")
							|| tmp_event.get(j).toUpperCase().startsWith("CHOOSE")
							|| tmp_event.get(j).toUpperCase().startsWith("WAIT")) {
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
							for (int j=0; j<global.USR_string.size(); j++) {
								if (global.USR_string.get(j).get(0).equals(stringName)) {
									String set_string = msg.substring(0, msg.indexOf(split_trig[i+1]));
									if (stringLength > 0) {
										int check_stringLength = set_string.length() - set_string.replace(" ", "").length() + 1;
										if (check_stringLength == stringLength) {
											global.USR_string.get(j).set(1, set_string);
											split_trig[i] = global.USR_string.get(j).get(1);
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
													global.USR_string.get(j).set(1, set_string);
													split_trig[i] = global.USR_string.get(j).get(1);
												}
											} catch (NumberFormatException e) {
												String[] fromtoSplit = value.split("-");
												try {
													int fromSplit = Integer.parseInt(fromtoSplit[0]);
													int toSplit = Integer.parseInt(fromtoSplit[fromtoSplit.length-1]);
													if (fromSplit <= check_stringLength && toSplit >= check_stringLength) {
														global.USR_string.get(j).set(1, set_string);
														split_trig[i] = global.USR_string.get(j).get(1);
													}
												} catch (NumberFormatException e1) {} catch (ArrayIndexOutOfBoundsException e1) {}
											}
										}
									} else {
										global.USR_string.get(j).set(1, set_string);
										split_trig[i] = global.USR_string.get(j).get(1);
									}
								}
							}
						}
					} else if (i==split_trig.length-1) {
						if (msg.contains(split_trig[i-1])) {
							for (int j=0; j<global.USR_string.size(); j++) {
								if (global.USR_string.get(j).get(0).equals(stringName)) {
									String set_string = msg.substring(msg.indexOf(split_trig[i-1])+split_trig[i-1].length(), msg.length());
									if (stringLength > 0) {
										int check_stringLength = set_string.length() - set_string.replace(" ", "").length() + 1;
										if (check_stringLength == stringLength) {
											global.USR_string.get(j).set(1, set_string);
											split_trig[i] = global.USR_string.get(j).get(1);
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
													global.USR_string.get(j).set(1, set_string);
													split_trig[i] = global.USR_string.get(j).get(1);
												}
											} catch (NumberFormatException e) {
												String[] fromtoSplit = value.split("-");
												try {
													int fromSplit = Integer.parseInt(fromtoSplit[0]);
													int toSplit = Integer.parseInt(fromtoSplit[fromtoSplit.length-1]);
													if (fromSplit <= check_stringLength && toSplit >= check_stringLength) {
														global.USR_string.get(j).set(1, set_string);
														split_trig[i] = global.USR_string.get(j).get(1);
													}
												} catch (NumberFormatException e1) {} catch (ArrayIndexOutOfBoundsException e1) {}
											}
										}
									} else {
										global.USR_string.get(j).set(1, set_string);
										split_trig[i] = global.USR_string.get(j).get(1);
									}
								}
							}
						}
					} else {
						if (msg.contains(split_trig[i-1]) && msg.contains(split_trig[i+1])) {
							for (int j=0; j<global.USR_string.size(); j++) {
								if (global.USR_string.get(j).get(0).equals(stringName)) {
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
												global.USR_string.get(j).set(1, set_string);
												split_trig[i] = global.USR_string.get(j).get(1);
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
														global.USR_string.get(j).set(1, set_string);
														split_trig[i] = global.USR_string.get(j).get(1);
													}
												} catch (NumberFormatException e) {
													String[] fromtoSplit = value.split("-");
													try {
														int fromSplit = Integer.parseInt(fromtoSplit[0]);
														int toSplit = Integer.parseInt(fromtoSplit[fromtoSplit.length-1]);
														if (fromSplit <= check_stringLength && toSplit >= check_stringLength) {
															global.USR_string.get(j).set(1, set_string);
															split_trig[i] = global.USR_string.get(j).get(1);
														}
													} catch (NumberFormatException e1) {} catch (ArrayIndexOutOfBoundsException e1) {}
												}
											}
										} else {
											global.USR_string.get(j).set(1, set_string);
											split_trig[i] = global.USR_string.get(j).get(1);
										}
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
