package com.kerbybit.chattriggers.triggers;

import java.util.*;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.global;

import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class StringHandler {

	
	public static void resetBackupStrings() {
        global.backupUSR_strings.clear();
        List<List<String>> temp = new ArrayList<List<String>>();
        temp.addAll(global.USR_string);

        for (List<String> backup : temp) {
            try {
                if (backup.size() == 2) {
                    String first = backup.get(0);
                    String second = backup.get(1);
                    List<String> temporary = new ArrayList<String>();
                    temporary.add(first);
                    temporary.add(second);
                    global.backupUSR_strings.add(temporary);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        global.backupTMP_strings.clear();
        temp = new ArrayList<List<String>>();
        temp.addAll(global.TMP_string);

        for (List<String> backup : temp) {
            try {
                if (backup.size() == 2) {
                    String first = backup.get(0);
                    String second = backup.get(1);
                    List<String> temporary = new ArrayList<String>();
                    temporary.add(first);
                    temporary.add(second);
                    global.backupTMP_strings.add(temporary);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        global.backupAsync_string.clear();
        for (Map.Entry<String, String> entry : global.Async_string.entrySet()) {
            global.backupAsync_string.put(entry.getKey(), entry.getValue());
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
	
	public static String stringFunctions(String TMP_e, ClientChatReceivedEvent chatEvent, Boolean isAsync) {
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
                    for (int i = 0; i < global.USR_string.size(); i++) {
                        if (global.USR_string.get(i).get(0).equals(sn)) {
                            returnString = global.USR_string.get(i).get(1);
                        }
                    }
                    if (returnString.equals("Not a string!")) {
                        try {
                            for (int i = 0; i < global.TMP_string.size(); i++) {
                                if (global.TMP_string.get(i).get(0).equals(sn)) {
                                    returnString = global.TMP_string.get(i).get(1);
                                }
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

                global.USR_string.clear();
                List<List<String>> temp = new ArrayList<List<String>>();
                temp.addAll(global.backupUSR_strings);
				for (List<String> backup : temp) {
				    try {
                        if (backup.size() == 2) {
                            String first = backup.get(0);
                            String second = backup.get(1);
                            List<String> temporary = new ArrayList<String>();
                            temporary.add(first);
                            temporary.add(second);
                            global.USR_string.add(temporary);
                        }
                    } catch (NullPointerException e) {
				        e.printStackTrace();
                    }
				}

				global.TMP_string.clear();
				temp = new ArrayList<List<String>>();
				temp.addAll(global.backupTMP_strings);
				for (List<String> backup : temp) {
				    try {
                        if (backup.size() == 2) {
                            String first = backup.get(0);
                            String second = backup.get(1);
                            List<String> temporary = new ArrayList<String>();
                            temporary.add(first);
                            temporary.add(second);
                            global.TMP_string.add(temporary);
                        }
                    } catch (NullPointerException e) {
				        e.printStackTrace();
                    }
				}

				global.Async_string.clear();
				Map<String, String> AsyncTemp = new HashMap<String, String>(global.backupAsync_string);
				for (Map.Entry<String, String> entry : AsyncTemp.entrySet()) {
				    global.Async_string.put(entry.getKey(), entry.getValue());
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
