package com.kerbybit.chattriggers;

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
				TMP_trig = TMP_trig.replace("{s}", ""); 
				TMP_trig = TMP_trig.replace("{c}", ""); 
				TMP_trig = TMP_trig.replace("{e}", "");
				if (TMP_trig.contains("{list=")) {TMP_list = TMP_trig.substring(TMP_trig.indexOf("=")+1, TMP_trig.indexOf("}")); TMP_trig = TMP_trig.replace("{list="+TMP_list+"}","");}
				
				//check match
				if (TMP_trig.equals(triggerName)) {
					//add all events to temp list
					List<String> TMP_events = new ArrayList<String>();
					for (int i=2; i<global.trigger.get(k).size(); i++) {TMP_events.add(global.trigger.get(k).get(i));}
					
					//do events
					doEvents(TMP_events, chatEvent);
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
		for (int i=0; i<tmp_event.size(); i++) {
			
		//SETUP
			String TMP_e = tmp_event.get(i);
			String TMP_c = "";
			if (!TMP_e.contains(" ")) {TMP_c = TMP_e; TMP_e="";}
			else {TMP_c = TMP_e.substring(0, TMP_e.indexOf(" ")); TMP_e = TMP_e.substring(TMP_e.indexOf(" ")+1, TMP_e.length());}
			int TMP_t = 50;
			if (TMP_e.contains("{time=") && TMP_e.contains("}")) {
				String TMP_tstring = TMP_e.substring(TMP_e.indexOf("{time=")+6, TMP_e.indexOf("}"));
				try {TMP_t = Integer.parseInt(TMP_tstring);}
				catch (NumberFormatException e) {e.printStackTrace();}
				TMP_e = TMP_e.replace("{time=" + TMP_tstring + "}", "");
			}
			if (toreplace != null) {
				for (int j=0; j<toreplace.length; j++) {
					TMP_e = TMP_e.replace(toreplace[j], replacement[j]);
				}
			}
			
		//built in strings
			if (chatEvent!=null) {TMP_e = TMP_e.replace("{msg}", chatEvent.message.getFormattedText());}
			TMP_e = TMP_e.replace("{trigsize}", global.trigger.size()+"");
			TMP_e = TMP_e.replace("{me}", Minecraft.getMinecraft().thePlayer.getDisplayNameString());
			
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
				TMP_e = TMP_e.replace("{string[" + TMP_s + "]}", TMP_sn);
				TMP_e = TMP_e.replace("{string<", "{string[");
				TMP_e = TMP_e.replace(">}", "]}");
			}
			
		//add formatting where needed
			if (TMP_c.equalsIgnoreCase("SAY") || TMP_c.equalsIgnoreCase("CHAT") || TMP_c.equalsIgnoreCase("KILLFEED")) {
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
				if (checkTo.length==2) {
					TMP_e = TMP_e.replace(checkFrom + ".replace(" + checkTo[0] + "," + checkTo[1] + ")", checkFrom.replace(checkTo[0], checkTo[1]));
				}
			}
			
			if (TMP_e.contains(".equals(") && TMP_e.contains(")")) {
				String checkFrom = TMP_e.substring(0, TMP_e.indexOf(".equals("));
				String checkTo = TMP_e.substring(TMP_e.indexOf(".equals(")+8, TMP_e.indexOf(")", TMP_e.indexOf(".equals(")));
				if (checkFrom.equals(checkTo)) {
					TMP_e = TMP_e.replace(checkFrom + ".equals(" + checkTo + ")", "true");
				} else {
					TMP_e = TMP_e.replace(checkFrom + ".equals(" + checkTo + ")", "false");
				}
			} else if (TMP_e.contains(".startsWith(") && TMP_e.contains(")")) {
				String checkFrom = TMP_e.substring(0, TMP_e.indexOf(".startsWith("));
				String checkTo = TMP_e.substring(TMP_e.indexOf(".startsWith(")+12, TMP_e.indexOf(")", TMP_e.indexOf(".equals(")));
				if (checkFrom.startsWith(checkTo)) {
					TMP_e = TMP_e.replace(checkFrom + ".startsWith(" + checkTo + ")", "true");
				} else {
					TMP_e = TMP_e.replace(checkFrom + ".startsWith(" + checkTo + ")", "false");
				}
			} else if (TMP_e.contains(".contains(") && TMP_e.contains(")")) {
				String checkFrom = TMP_e.substring(0, TMP_e.indexOf(".contains("));
				String checkTo = TMP_e.substring(TMP_e.indexOf(".contains(")+10, TMP_e.indexOf(")", TMP_e.indexOf(".equals(")));
				if (checkFrom.contains(checkTo)) {
					TMP_e = TMP_e.replace(checkFrom + ".contains(" + checkTo + ")", "true");
				} else {
					TMP_e = TMP_e.replace(checkFrom + ".contains(" + checkTo + ")", "false");
				}
			} else if (TMP_e.contains(".endsWith(") && TMP_e.contains(")")) {
				String checkFrom = TMP_e.substring(0, TMP_e.indexOf(".endsWith("));
				String checkTo = TMP_e.substring(TMP_e.indexOf(".endsWith(")+10, TMP_e.indexOf(")", TMP_e.indexOf(".equals(")));
				if (checkFrom.endsWith(checkTo)) {
					TMP_e = TMP_e.replace(checkFrom + ".endsWith(" + checkTo + ")", "true");
				} else {
					TMP_e = TMP_e.replace(checkFrom + ".endsWith(" + checkTo + ")", "false");
				}
			}
			
			while (TMP_e.contains("string.set(") && TMP_e.contains(")")) {
				String[] args = TMP_e.substring(TMP_e.indexOf("string.set(") + 11,TMP_e.indexOf(")",TMP_e.indexOf("string.set("))).split(",");
				if (args.length==2) {
					try {
						int num = Integer.parseInt(args[0]);
						if (num>0 && num<global.USR_string.size()) {
							global.USR_string.get(num).set(1, args[1]);
							TMP_e = TMP_e.replace("string.set(" + args[0] + "," + args[1] + ")", args[1]);
						}
					} catch (NumberFormatException e) {
						for (int j=0; j<global.USR_string.size(); j++) {
							System.out.println(global.USR_string.get(j) + " -> " + args[0] + " " + args[1]);
							if (global.USR_string.get(j).get(0).equals(args[0])) {
								global.USR_string.get(j).set(1, args[1]);
								TMP_e = TMP_e.replace("string.set(" + args[0] + "," + args[1] + ")", args[1]);
							}
						}
					}
				}
			}
			
		//non-logic events
			if (TMP_c.equalsIgnoreCase("SAY")) {global.chatQueue.add(TMP_e);}
			if (TMP_c.equalsIgnoreCase("CHAT")) {chat.warn(TMP_e);}
			if (TMP_c.equalsIgnoreCase("SOUND")) {sound.play(TMP_e);}
			if (TMP_c.equalsIgnoreCase("CANCEL") && chatEvent!=null) {chatEvent.setCanceled(true);}
			if (TMP_c.equalsIgnoreCase("KILLFEED")) {global.killfeed.add(TMP_e); global.killfeedDelay.add(TMP_t);}
			if (TMP_c.equalsIgnoreCase("COMMAND")) {global.commandQueue.add(TMP_e);}
			if (TMP_c.equalsIgnoreCase("TRIGGER")) {doTrigger(TMP_e, chatEvent);}
			
		//logic events
			
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
							|| tmp_event.get(j).toUpperCase().startsWith("CHOOSE")) {
								tabbed_logic++;
							}
							
							//move to else
							if (tmp_event.get(j).toUpperCase().startsWith("ELSE") && tabbed_logic==1) {gotoElse=true;}
							
							if (gotoElse==false) {eventsToIf.add(tmp_event.get(j));} 
							else {eventsToElse.add(tmp_event.get(j));}
							
							//decrease tab
							if (tmp_event.get(j).toUpperCase().startsWith("END")) {tabbed_logic--;}
							
							
						}
						
						//check if choose exit
						if (tabbed_logic==0) {j=tmp_event.size();}
					}
					
					//move i to end of if
					i += eventsToIf.size()+eventsToElse.size()-2;
					
					//check condition and do events
					if (TMP_e.equalsIgnoreCase("TRUE")) {
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
							|| tmp_event.get(j).toUpperCase().startsWith("CHOOSE")) {
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
	}
	
	public static int randInt(int min, int max) {
	    Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
	    return randomNum;
	}
	
	public static String setStrings(String msg, String trig) {
		//some magic shit is going on here
		//trust me, this used to be a LOT worse and only sort of kinda worked
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
									int checkafter = msg.substring(checkbefore, msg.length()).indexOf(split_trig[i+1]) + checkbefore;
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
				trig = "";
				for (String value : split_trig) {trig += value;}
			}
		}
		return trig;
	}
	
	
	
}
