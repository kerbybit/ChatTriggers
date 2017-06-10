package com.kerbybit.chattriggers.triggers;

import java.util.*;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.global;

import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class StringHandler {

    //run on main thread every tick
    public static void updateMarkedStrings() {
        for (Map.Entry<String, String> string : global.USR_string_mark.entrySet()) {
            if (!global.USR_string_markdel.containsKey(string.getKey())) {
                if (global.USR_string.containsKey(string.getKey())) {
                    global.USR_string.put(string.getKey(), string.getValue());
                    global.USR_string_markdel.put(string.getKey(), string.getValue());
                }
            }
        }

        for (Map.Entry<String, String> string : global.backupUSR_strings_mark.entrySet()) {
            if (!global.backupUSR_strings_markdel.containsKey(string.getKey())) {
                if (global.backupUSR_strings.containsKey(string.getKey())) {
                    global.backupUSR_strings.put(string.getKey(), string.getValue());
                    global.backupUSR_strings_markdel.put(string.getKey(), string.getValue());
                }
            }
        }

        deleteMarkedStrings();
    }

    private static void deleteMarkedStrings() {
        try {
            for (String key : global.USR_string_markdel.keySet()) {
                global.USR_string_markdel.remove(key);
                global.USR_string_mark.remove(key);
            }
        } catch (ConcurrentModificationException exception) {
            // thrown if async is still messing with strings out of time
            // clearing marked strings will attempt to run next game tick
        }

        try {
            for (String key : global.backupUSR_strings_markdel.keySet()) {
                global.backupUSR_strings_markdel.remove(key);
                global.backupUSR_strings_mark.remove(key);
            }
        } catch (ConcurrentModificationException exception) {
            // thrown if async is still messing with strings out of time
            // clearing marked strings will attempt to run next game tick
        }
    }
	
	public static void resetBackupStrings() {
        global.backupUSR_strings.clear();
        Map<String, String> tmpUSR = new HashMap<>(global.USR_string);
        for (Map.Entry<String, String> entry : tmpUSR.entrySet()) {
            global.backupUSR_strings.put(entry.getKey(), entry.getValue());
        }

        global.backupTMP_strings.clear();
        Map<String, String> tempTMP = new HashMap<>(global.TMP_string);
        for (Map.Entry<String, String> entry : tempTMP.entrySet()) {
            global.backupTMP_strings.put(entry.getKey(), entry.getValue());
        }

        global.backupAsync_string.clear();
        Map<String, String> tempAsync = new HashMap<>(global.Async_string);
        for (Map.Entry<String, String> entry : tempAsync.entrySet()) {
            global.backupAsync_string.put(entry.getKey(), entry.getValue());
        }
		
	}

	public static String stringFunctions(String TMP_e, ClientChatReceivedEvent event, Boolean isAsync) {
        return stringFunctions(TMP_e, event, isAsync, false);
    }
	
	static String stringFunctions(String TMP_e, ClientChatReceivedEvent chatEvent, Boolean isAsync, Boolean nested) {
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
				sn = stringFunctions(sn, chatEvent, isAsync);
				
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
				
				TMP_e = firstpart + StringFunctions.doStringFunctions(sn,func,args, chatEvent, isAsync) + secondpart;
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
				sn = stringFunctions(sn, chatEvent, isAsync);
				
				TMP_e = efirst + sn + esecond;
				
				String returnString = "Not a string!";

				if (isAsync) {
                    if (global.Async_string.containsKey(sn)) {
                        returnString = global.Async_string.get(sn);
                    }
                } else {
				    if (global.USR_string.containsKey(sn)) {
				        returnString = global.USR_string.get(sn);
                    }
                    if (returnString.equals("Not a string!")) {
                        try {
                            if (global.TMP_string.containsKey(sn)) {
                                returnString = global.TMP_string.get(sn);
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            returnString = "Not a string!";
                        }
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

				if (!nested) {
                    global.USR_string.clear();
                    Map<String, String> USRTemp = new HashMap<>(global.backupUSR_strings);
                    for (Map.Entry<String, String> backup : USRTemp.entrySet()) {
                        global.USR_string.put(backup.getKey(), backup.getValue());
                    }

                    global.TMP_string.clear();
                    Map<String, String> TMPTemp = new HashMap<>(global.backupTMP_strings);
                    for (Map.Entry<String, String> backup : TMPTemp.entrySet()) {
                        global.TMP_string.put(backup.getKey(), backup.getValue());
                    }

                    global.Async_string.clear();
                    Map<String, String> AsyncTemp = new HashMap<>(global.backupAsync_string);
                    for (Map.Entry<String, String> entry : AsyncTemp.entrySet()) {
                        global.Async_string.put(entry.getKey(), entry.getValue());
                    }
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
					List<String> stringListSplit = new ArrayList<>();
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

							if (global.USR_string.containsKey(stringName)) {
							    stringnum = 1;
                            }

							if (stringnum==-1) {
								String tmpnum = global.TMP_string.size()+"";
								global.TMP_string.put("TEMP-USER-STRING"+tmpnum+"->"+stringName, "");
								global.temporary_replace.add("{string["+stringName+"]");
								global.temporary_replacement.add("{string[TEMP-USER-STRING"+tmpnum+"->"+stringName+"]"); 
								global.temporary_replace.add("{string<"+stringName+">");
								global.temporary_replacement.add("{string[TEMP-USER-STRING"+tmpnum+"->"+stringName+"]");
								stringName = "TEMP-USER-STRING"+tmpnum+"->"+stringName;
							}
							
							
							String set_string = msg.substring(0, msg.indexOf(split_trig[i+1]));
							if (stringLength > 0) {
								int check_stringLength = set_string.length() - set_string.replace(" ", "").length() + 1;
								if (check_stringLength == stringLength) {
									if (stringnum!=-1) {
										global.USR_string.put(stringName, set_string);
										split_trig[i] = global.USR_string.get(stringName);
									} else {
                                        global.TMP_string.put(stringName, set_string);
                                        split_trig[i] = global.TMP_string.get(stringName);
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
												global.USR_string.put(stringName, set_string);
												split_trig[i] = global.USR_string.get(stringName);
											} else {
                                                global.TMP_string.put(stringName, set_string);
                                                split_trig[i] = global.TMP_string.get(stringName);
											}
										}
									} catch (NumberFormatException e) {
										String[] fromtoSplit = value.split("-");
										try {
											int fromSplit = Integer.parseInt(fromtoSplit[0]);
											int toSplit = Integer.parseInt(fromtoSplit[fromtoSplit.length-1]);
											if (fromSplit <= check_stringLength && toSplit >= check_stringLength) {
												if (stringnum!=-1) {
													global.USR_string.put(stringName, set_string);
													split_trig[i] = global.USR_string.get(stringName);
												} else {
                                                    global.TMP_string.put(stringName, set_string);
                                                    split_trig[i] = global.TMP_string.get(stringName);
												}
											}
										} catch (Exception e1) {/*do nothing*/}
									}
								}
							} else {
								if (stringnum!=-1) {
									global.USR_string.put(stringName, set_string);
									split_trig[i] = global.USR_string.get(stringName);
								} else {
                                    global.TMP_string.put(stringName, set_string);
                                    split_trig[i] = global.TMP_string.get(stringName);
								}
							}
						}
					} else if (i==split_trig.length-1) {
						if (msg.contains(split_trig[i-1])) {
							int stringnum = -1;

                            if (global.USR_string.containsKey(stringName)) {
							    stringnum = 1;
                            }

							if (stringnum==-1) {
								String tmpnum = global.TMP_string.size()+"";
								global.TMP_string.put("TEMP-USER-STRING"+tmpnum+"->"+stringName, "");
								global.temporary_replace.add("{string["+stringName+"]");
								global.temporary_replacement.add("{string[TEMP-USER-STRING"+tmpnum+"->"+stringName+"]");
								global.temporary_replace.add("{string<"+stringName+">");
								global.temporary_replacement.add("{string[TEMP-USER-STRING"+tmpnum+"->"+stringName+"]");
                                stringName = "TEMP-USER-STRING"+tmpnum+"->"+stringName;
							}
							
							String set_string = msg.substring(msg.indexOf(split_trig[i-1])+split_trig[i-1].length(), msg.length());
							if (stringLength > 0) {
								int check_stringLength = set_string.length() - set_string.replace(" ", "").length() + 1;
								if (check_stringLength == stringLength) {
									if (stringnum!=-1) {
										global.USR_string.put(stringName, set_string);
                                        split_trig[i] = global.USR_string.get(stringName);
									} else {
                                        global.TMP_string.put(stringName, set_string);
                                        split_trig[i] = global.TMP_string.get(stringName);
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
											    global.USR_string.put(stringName, set_string);
												split_trig[i] = global.USR_string.get(stringName);
											} else {
                                                global.TMP_string.put(stringName, set_string);
                                                split_trig[i] = global.TMP_string.get(stringName);
											}
										}
									} catch (NumberFormatException e) {
										String[] fromtoSplit = value.split("-");
										try {
											int fromSplit = Integer.parseInt(fromtoSplit[0]);
											int toSplit = Integer.parseInt(fromtoSplit[fromtoSplit.length-1]);
											if (fromSplit <= check_stringLength && toSplit >= check_stringLength) {
												if (stringnum!=-1) {
                                                    global.USR_string.put(stringName, set_string);
                                                    split_trig[i] = global.USR_string.get(stringName);
												} else {
                                                    global.TMP_string.put(stringName, set_string);
                                                    split_trig[i] = global.TMP_string.get(stringName);
												}
											}
										} catch (Exception e1) {/*do nothing*/}
									}
								}
							} else {
								if (stringnum!=-1) {
                                    global.USR_string.put(stringName, set_string);
                                    split_trig[i] = global.USR_string.get(stringName);
								} else {
                                    global.TMP_string.put(stringName, set_string);
                                    split_trig[i] = global.TMP_string.get(stringName);
								}
							}
						}
					} else {
						if (msg.contains(split_trig[i-1]) && msg.contains(split_trig[i+1])) {
							int stringnum = -1;

							if (global.USR_string.containsKey(stringName)) {
							    stringnum = 1;
                            }

							if (stringnum==-1) {
								String tmpnum = global.TMP_string.size()+"";
								global.TMP_string.put("TEMP-USER-STRING"+tmpnum+"->"+stringName, "");
								global.temporary_replace.add("{string["+stringName+"]");
								global.temporary_replacement.add("{string[TEMP-USER-STRING"+tmpnum+"->"+stringName+"]");
								global.temporary_replace.add("{string<"+stringName+">");
								global.temporary_replacement.add("{string[TEMP-USER-STRING"+tmpnum+"->"+stringName+"]");
                                stringName = "TEMP-USER-STRING"+tmpnum+"->"+stringName;
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
                                            global.USR_string.put(stringName, set_string);
                                            split_trig[i] = global.USR_string.get(stringName);
										} else {
                                            global.TMP_string.put(stringName, set_string);
                                            split_trig[i] = global.TMP_string.get(stringName);
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
                                                    global.USR_string.put(stringName, set_string);
                                                    split_trig[i] = global.USR_string.get(stringName);
												} else {
                                                    global.TMP_string.put(stringName, set_string);
                                                    split_trig[i] = global.TMP_string.get(stringName);
												}
											}
										} catch (NumberFormatException e) {
											String[] fromtoSplit = value.split("-");
											try {
												int fromSplit = Integer.parseInt(fromtoSplit[0]);
												int toSplit = Integer.parseInt(fromtoSplit[fromtoSplit.length-1]);
												if (fromSplit <= check_stringLength && toSplit >= check_stringLength) {
													if (stringnum!=-1) {
                                                        global.USR_string.put(stringName, set_string);
                                                        split_trig[i] = global.USR_string.get(stringName);
													} else {
                                                        global.TMP_string.put(stringName, set_string);
                                                        split_trig[i] = global.TMP_string.get(stringName);
													}
												}
											} catch (Exception e1) {/*do nothing*/}
										}
									}
								} else {
									if (stringnum!=-1) {
                                        global.USR_string.put(stringName, set_string);
                                        split_trig[i] = global.USR_string.get(stringName);
									} else {
                                        global.TMP_string.put(stringName, set_string);
										split_trig[i] = global.TMP_string.get(stringName);
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
