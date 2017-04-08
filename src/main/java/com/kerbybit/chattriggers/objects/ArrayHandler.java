package com.kerbybit.chattriggers.objects;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kerbybit.chattriggers.file.JsonHandler;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.triggers.EventsHandler;
import com.kerbybit.chattriggers.triggers.StringHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;

public class ArrayHandler {
    public static List<List<String>> USR_array = new ArrayList<List<String>>();

    public static List<List<String>> getArrays() {
        return USR_array;
    }

    public static int getArraysSize() {
        return USR_array.size();
    }

	public static String arrayFunctions(String TMP_e, ClientChatReceivedEvent chatEvent) {
	    while (TMP_e.contains("{array[") && TMP_e.contains("]}.getRandom()")) {
	        String get_name = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.getRandom()", TMP_e.indexOf("{array[")));
	        while (get_name.contains("{array[")) {
                get_name = get_name.substring(get_name.indexOf("{array[")+7);
            }
	        Boolean isArray = false;

	        for (List<String> value : USR_array) {
	            if (value.get(0).equals(get_name)) {
                    List<String> temporary = new ArrayList<String>();
                    temporary.add("ArrayToString->"+get_name+"GETR"+"-"+(global.TMP_string.size()+1));
                    temporary.add(value.get(EventsHandler.randInt(1, value.size()-1)));
                    global.TMP_string.add(temporary);
                    global.backupTMP_strings.add(temporary);

                    TMP_e = TMP_e.replace("{array["+get_name+"]}.getRandom()","{string[ArrayToString->"+get_name+"GETR"+"-"+global.TMP_string.size()+"]}");

	                isArray = true;
                }
            }

            if (!isArray) {
                List<String> temporary = new ArrayList<String>();
                temporary.add("ArrayToString->"+get_name+"GETR"+"-"+(global.TMP_string.size()+1));
                temporary.add(get_name + " is not currently an array");
                global.TMP_string.add(temporary);
                global.backupTMP_strings.add(temporary);

                TMP_e = TMP_e.replace("{array["+get_name+"]}.getRandom()","{string[ArrayToString->"+get_name+"GETR"+"-"+global.TMP_string.size()+"]}");
            }
        }

		while (TMP_e.contains("{array[") && TMP_e.contains("]}.setSplit(") && TMP_e.contains(",") && TMP_e.contains(")")) {
			String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.setSplit(", TMP_e.indexOf("{array[")));
			String checkTo = TMP_e.substring(TMP_e.indexOf("]}.setSplit(")+12, TMP_e.indexOf(")", TMP_e.indexOf("]}.setSplit(")));
			StringBuilder returnString = new StringBuilder("Something went wrong with parsing setSplit!");
			Boolean isArray = false;
			
			if (checkFrom.contains("{string[") && checkFrom.contains("]}")) {
				checkFrom = StringHandler.stringFunctions(checkFrom, chatEvent);
			}
			
			String[] args = checkTo.split(",");
			if (args.length==2) {
				for (int j=0; j<USR_array.size(); j++) {
					if (USR_array.get(j).get(0).equals(checkFrom)) {
						String[] moreargs = args[0].split(args[1]);
						List<String> temporary = new ArrayList<String>();
						temporary.addAll(Arrays.asList(moreargs));
						returnString = new StringBuilder("[");
						for (String value : temporary) {returnString.append(value).append(" ");}
						returnString = new StringBuilder(returnString.toString().trim().replace(" ",",")+"]");
						USR_array.get(j).addAll(temporary);
						isArray = true;
					}
				}
				if (!isArray) {
					String[] moreargs = args[0].split(args[1]);
					List<String> temporary = new ArrayList<String>();
					temporary.add(checkFrom);
					List<String> temp = new ArrayList<String>();
					temporary.addAll(Arrays.asList(moreargs));
					returnString = new StringBuilder("[");
					for (String value : temp) {returnString.append(value).append(" ");}
					returnString = new StringBuilder(returnString.toString().trim().replace(" ",",")+"]");
					temporary.addAll(temp);
					USR_array.add(temporary);
				}
			} else {returnString = new StringBuilder("setSplit formatted wrong! use .setSplit(value,split)");}
			
			List<String> temporary = new ArrayList<String>();
			temporary.add("ArrayToString->"+checkFrom+"SETSPLIT"+checkTo+"-"+(global.TMP_string.size()+1));
			temporary.add(returnString.toString());
			global.TMP_string.add(temporary);
			global.backupTMP_strings.add(temporary);
			TMP_e = TMP_e.replace("{array[" + checkFrom + "]}.setSplit(" + checkTo + ")", "{string[ArrayToString->"+checkFrom+"SETSPLIT"+checkTo+"-"+global.TMP_string.size()+"]}");
		}
		
		while (TMP_e.contains("{array[") && TMP_e.contains("]}.add(") && TMP_e.contains(")")) {
			String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.add(", TMP_e.indexOf("{array[")));
			String checkTo = TMP_e.substring(TMP_e.indexOf("]}.add(")+7, TMP_e.indexOf(")", TMP_e.indexOf("]}.add(")));
            String fin_checkTo = checkTo;
			Boolean isArray = false;
            int where = -1;

			if (checkFrom.contains("{string[") && checkFrom.contains("]}")) {
				checkFrom = StringHandler.stringFunctions(checkFrom, chatEvent);
			}

            if (checkTo.contains(",")) {
                try {
                    where = Integer.parseInt(checkTo.split(",")[0]);
                    fin_checkTo = checkTo.substring(checkTo.indexOf(",") + 1);
                } catch (NumberFormatException e) {
                    where = -1;
                }
            }

			for (int j=0; j<USR_array.size(); j++) {
				if (USR_array.get(j).get(0).equals(checkFrom)) {
                    if (where == -1) {
                        USR_array.get(j).add(fin_checkTo);
                    } else {
                        USR_array.get(j).add(where, fin_checkTo);
                    }
					isArray = true;
				}
			}
			
			if (!isArray) {
				List<String> prearray = new ArrayList<String>();
				prearray.add(checkFrom);
				prearray.add(fin_checkTo);
                USR_array.add(prearray);
			}
			
			List<String> temporary = new ArrayList<String>();
			temporary.add("ArrayToString->"+checkFrom+"ADD"+checkTo+"-"+(global.TMP_string.size()+1));
			temporary.add(checkTo);
			global.TMP_string.add(temporary);
			global.backupTMP_strings.add(temporary);
			TMP_e = TMP_e.replace("{array[" + checkFrom + "]}.add(" + checkTo + ")", "{string[ArrayToString->"+checkFrom+"ADD"+checkTo+"-"+global.TMP_string.size()+"]}");
		}

        while (TMP_e.contains("{array[") && TMP_e.contains("]}.prepend(") && TMP_e.contains(")")) {
            String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.prepend(", TMP_e.indexOf("{array[")));
            String checkTo = TMP_e.substring(TMP_e.indexOf("]}.prepend(")+7, TMP_e.indexOf(")", TMP_e.indexOf("]}.prepend(")));
            Boolean isArray = false;

            if (checkFrom.contains("{string[") && checkFrom.contains("]}")) {
                checkFrom = StringHandler.stringFunctions(checkFrom, chatEvent);
            }

            for (int j=0; j<USR_array.size(); j++) {
                if (USR_array.get(j).get(0).equals(checkFrom)) {
                    USR_array.get(j).add(1, checkTo);
                    isArray = true;
                }
            }

            if (!isArray) {
                List<String> prearray = new ArrayList<String>();
                prearray.add(checkFrom);
                prearray.add(checkTo);
                USR_array.add(prearray);
            }

            List<String> temporary = new ArrayList<String>();
            temporary.add("ArrayToString->"+checkFrom+"PREPEND"+checkTo+"-"+(global.TMP_string.size()+1));
            temporary.add(checkTo);
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{array[" + checkFrom + "]}.prepend(" + checkTo + ")", "{string[ArrayToString->"+checkFrom+"PREPEND"+checkTo+"-"+global.TMP_string.size()+"]}");
        }
		
		while (TMP_e.contains("{array[") && TMP_e.contains("]}.clear()")) {
			String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.clear()", TMP_e.indexOf("{array[")));
			String returnString = checkFrom + " is not an array!";
			
			if (checkFrom.contains("{string[") && checkFrom.contains("]}")) {
				checkFrom = StringHandler.stringFunctions(checkFrom, chatEvent);
			}
			
			for (int j=0; j<USR_array.size(); j++) {
				if (USR_array.get(j).get(0).equals(checkFrom)) {
					USR_array.remove(j);
					returnString = checkFrom + " cleared.";
				}
			}
			
			List<String> temporary = new ArrayList<String>();
			temporary.add("ArrayToString->"+checkFrom+"CLEAR"+"-"+(global.USR_string.size()+1));
			temporary.add(returnString);
			global.TMP_string.add(temporary);
			global.backupTMP_strings.add(temporary);
			TMP_e = TMP_e.replace("{array[" + checkFrom + "]}.clear()", "{string[ArrayToString->"+checkFrom+"CLEAR"+"-"+global.TMP_string.size()+"]}");
		}
		
		while (TMP_e.contains("{array[") && TMP_e.contains("]}.has(") && TMP_e.contains(")")) {
			String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.has(", TMP_e.indexOf("{array[")));
			String checkTo = TMP_e.substring(TMP_e.indexOf("]}.has(")+7, TMP_e.indexOf(")", TMP_e.indexOf("]}.has(")));
			String checkThis = "false";
			
			if (checkFrom.contains("{string[") && checkFrom.contains("]}")) {
				checkFrom = StringHandler.stringFunctions(checkFrom, chatEvent);
			}
			
			for (int j=0; j<USR_array.size(); j++) {
				if (USR_array.get(j).get(0).equals(checkFrom)) {
					for (int k=1; k<USR_array.get(j).size(); k++) {
						if (USR_array.get(j).get(k).equals(checkTo)) {checkThis = "true";}
					}
				}
			}
			
			List<String> temporary = new ArrayList<String>();
			temporary.add("ArrayToString->"+checkFrom+"HAS"+checkTo+"-"+(global.TMP_string.size()+1));
			temporary.add(checkThis);
			global.TMP_string.add(temporary);
			global.backupTMP_strings.add(temporary);
			TMP_e = TMP_e.replace("{array["+checkFrom+"]}.has("+checkTo+")", "{string[ArrayToString->"+checkFrom+"HAS"+checkTo+"-"+global.TMP_string.size()+"]}");
		}
		
		while (TMP_e.contains("{array[") && TMP_e.contains("]}.hasIgnoreCase(") && TMP_e.contains(")")) {
			String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.hasIgnoreCase(", TMP_e.indexOf("{array[")));
			String checkTo = TMP_e.substring(TMP_e.indexOf("]}.hasIgnoreCase(")+17, TMP_e.indexOf(")", TMP_e.indexOf("]}.hasIgnoreCase(")));
			String checkThis = "false";
			
			if (checkFrom.contains("{string[") && checkFrom.contains("]}")) {
				checkFrom = StringHandler.stringFunctions(checkFrom, chatEvent);
			}
			
			for (int j=0; j<USR_array.size(); j++) {
				if (USR_array.get(j).get(0).equals(checkFrom)) {
					for (int k=1; k<USR_array.get(j).size(); k++) {
						if (USR_array.get(j).get(k).equalsIgnoreCase(checkTo)) {checkThis = "true";}
					}
				}
			}
			
			List<String> temporary = new ArrayList<String>();
			temporary.add("ArrayToString->"+checkFrom+"HASIGNORECASE"+checkTo+"-"+(global.TMP_string.size()+1));
			temporary.add(checkThis);
			global.TMP_string.add(temporary);
			global.backupTMP_strings.add(temporary);
			TMP_e = TMP_e.replace("{array["+checkFrom+"]}.hasIgnoreCase("+checkTo+")", "{string[ArrayToString->"+checkFrom+"HASIGNORECASE"+checkTo+"-"+global.TMP_string.size()+"]}");
		}
		
		while (TMP_e.contains("{array[") && TMP_e.contains("]}.remove(") && TMP_e.contains(")")) {
			String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.remove(", TMP_e.indexOf("{array[")));
			String checkTo = TMP_e.substring(TMP_e.indexOf("]}.remove(")+10, TMP_e.indexOf(")", TMP_e.indexOf("]}.remove(")));
			String removed;
			int toRemove;
			int toRemoveArray = -1;
			String returnString = checkFrom + " is not an array!";
			
			if (checkFrom.contains("{string[") && checkFrom.contains("]}")) {
				checkFrom = StringHandler.stringFunctions(checkFrom, chatEvent);
			}
			
			try {
				toRemove = Integer.parseInt(checkTo);
				if (toRemove > 0) {
					for (int j=0; j<USR_array.size(); j++) {
						if (USR_array.get(j).get(0).equals(checkFrom)) {
							if (toRemove < USR_array.get(j).size()) {
								removed = USR_array.get(j).remove(toRemove);
								returnString = removed;
								if (USR_array.get(j).size()==1) {toRemoveArray = j;}
							} else {returnString = "Value over bounds! (index "+toRemove+" - expecting "+USR_array.size()+")";}
						}
					}
				} else {returnString = "Value under bounds! (index "+toRemove+" - expecting 1)";}
			} catch (NumberFormatException e) {
				for (int j=0; j<USR_array.size(); j++) {
					if (USR_array.get(j).get(0).equals(checkFrom)) {
						for (int k=1; k<USR_array.get(j).size(); k++) {
							if (USR_array.get(j).get(k).equals(checkTo)) {
								removed = USR_array.get(j).remove(k);
								returnString = removed;
								if (USR_array.get(j).size()==1) {toRemoveArray = j;}
							}
						}
					}
				}
			}
			
			if (toRemoveArray != -1) {USR_array.remove(toRemoveArray);}
			
			List<String> temporary = new ArrayList<String>();
			temporary.add("ArrayToString->"+checkFrom+"REMOVE"+checkTo+"-"+(global.TMP_string.size()+1));
			temporary.add(returnString);
			global.TMP_string.add(temporary);
			global.backupTMP_strings.add(temporary);
			TMP_e = TMP_e.replace("{array[" + checkFrom + "]}.remove(" + checkTo + ")", "{string[ArrayToString->"+checkFrom+"REMOVE"+checkTo+"-"+global.TMP_string.size()+"]}");
		}
		
		while (TMP_e.contains("{array[") && TMP_e.contains("]}.get(") && TMP_e.contains(")")) {
			String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.get(", TMP_e.indexOf("{array[")));
			String checkTo = TMP_e.substring(TMP_e.indexOf("]}.get(")+7, TMP_e.indexOf(")", TMP_e.indexOf("]}.get(")));
			String got;
			int toGet;
			String returnString = checkFrom + " is not an array!";
			
			if (checkFrom.contains("{string[") && checkFrom.contains("]}")) {
				checkFrom = StringHandler.stringFunctions(checkFrom, chatEvent);
			}
			
			try {
				toGet = Integer.parseInt(checkTo);
				if (toGet > 0) {
					for (int j=0; j<USR_array.size(); j++) {
						if (USR_array.get(j).get(0).equals(checkFrom)) {
							if (toGet < USR_array.get(j).size()) {
								got = USR_array.get(j).get(toGet);
								returnString = got;
							} else {returnString = "Value over bounds! (index "+toGet+" - expecting "+USR_array.size()+")";}
						}
					}
				} else {returnString = "Value under bounds! (index "+toGet+" - expecting 1)";}
			} catch (NumberFormatException e) {
				for (int j=0; j<USR_array.size(); j++) {
					if (USR_array.get(j).get(0).equals(checkFrom)) {
						returnString = "-1";
						for (int k=1; k<USR_array.get(j).size(); k++) {
							if (USR_array.get(j).get(k).equals(checkTo)) {
								returnString = k +"";
							}
						}
					}
				}
			}
			
			List<String> temporary = new ArrayList<String>();
			temporary.add("ArrayToString->"+checkFrom+"GET"+checkTo+"-"+(global.TMP_string.size()+1));
			temporary.add(returnString);
			global.TMP_string.add(temporary);
			global.backupTMP_strings.add(temporary);
			TMP_e = TMP_e.replace("{array[" + checkFrom + "]}.get(" + checkTo + ")", "{string[ArrayToString->"+checkFrom+"GET"+checkTo+"-"+global.TMP_string.size()+"]}");
		}
		
		while (TMP_e.contains("{array[") && TMP_e.contains("]}.size()")) {
			String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.size()", TMP_e.indexOf("{array[")));
			int arraysize = 0;
			
			if (checkFrom.contains("{string[") && checkFrom.contains("]}")) {
				checkFrom = StringHandler.stringFunctions(checkFrom, chatEvent);
			}
			
			for (int j=0; j<USR_array.size(); j++) {
				if (USR_array.get(j).get(0).equals(checkFrom)) {arraysize = USR_array.get(j).size()-1;}
			}
			
			List<String> temporary = new ArrayList<String>();
			temporary.add("ArrayToString->"+checkFrom+"SIZE"+"-"+(global.TMP_string.size()+1));
			temporary.add(arraysize+"");
			global.TMP_string.add(temporary);
			global.backupTMP_strings.add(temporary);
			TMP_e = TMP_e.replace("{array[" + checkFrom + "]}.size()", "{string[ArrayToString->"+checkFrom+"SIZE"+"-"+global.TMP_string.size()+"]}");
		}
		
		while (TMP_e.contains("{array[") && TMP_e.contains("]}.importJsonFile(") && TMP_e.contains(",") && TMP_e.contains(")")) {
			String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.importJsonFile(", TMP_e.indexOf("{array[")));
			String checkFile = TMP_e.substring(TMP_e.indexOf("]}.importJsonFile(")+18, TMP_e.indexOf(",", TMP_e.indexOf("{array["+checkFrom+"]}.importJsonFile(")));
			String checkTo = TMP_e.substring(TMP_e.indexOf(",", TMP_e.indexOf("{array["+checkFrom+"]}.importJsonFile("))+1, TMP_e.indexOf(")", TMP_e.indexOf("{array["+checkFrom+"]}.importJsonFile("+checkFile+",")));
			
			if (checkFrom.contains("{string[") && checkFrom.contains("]}")) {
				checkFrom = StringHandler.stringFunctions(checkFrom, chatEvent);
			}
			
			String checkJson = JsonHandler.importJsonFile("array",checkFile, checkFrom+"=>"+checkTo);
			
			List<String> temporary = new ArrayList<String>();
			temporary.add("ArrayToString->"+checkFrom+"IMPORTJSONFILE"+checkTo+"FROM"+checkFile+"-"+(global.TMP_string.size()+1));
			temporary.add(checkJson);
			global.TMP_string.add(temporary);
			global.backupTMP_strings.add(temporary);
			TMP_e = TMP_e.replace("{array["+checkFrom+"]}.importJsonFile("+checkFile+","+checkTo+")", "{string[ArrayToString->"+checkFrom+"IMPORTJSONFILE"+checkTo+"FROM"+checkFile+"-"+global.TMP_string.size()+"]}");
		}
		
		while (TMP_e.contains("{array[") && TMP_e.contains("]}.importJsonURL(") && TMP_e.contains(",") && TMP_e.contains(")")) {
			String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.importJsonURL(", TMP_e.indexOf("{array[")));
			String checkFile = TMP_e.substring(TMP_e.indexOf("]}.importJsonURL(")+17, TMP_e.indexOf(",", TMP_e.indexOf("{array["+checkFrom+"]}.importJsonURL(")));
			String checkTo = TMP_e.substring(TMP_e.indexOf(",", TMP_e.indexOf("{array["+checkFrom+"]}.importJsonURL("))+1, TMP_e.indexOf(")", TMP_e.indexOf("{array["+checkFrom+"]}.importJsonURL("+checkFile+",")));
			
			if (checkFrom.contains("{string[") && checkFrom.contains("]}")) {
				checkFrom = StringHandler.stringFunctions(checkFrom, chatEvent);
			}
			
			String checkJson = JsonHandler.importJsonURL("array",checkFile, checkFrom + "=>" + checkTo);
			
			List<String> temporary = new ArrayList<String>();
			temporary.add("ArrayToString->"+checkFrom+"IMPORTJSONURL"+checkTo+"FROM"+checkFile+"-"+(global.TMP_string.size()+1));
			temporary.add(checkJson);
			global.TMP_string.add(temporary);
			global.backupTMP_strings.add(temporary);
			TMP_e = TMP_e.replace("{array["+checkFrom+"]}.importJsonURL("+checkFile+","+checkTo+")", "{string[ArrayToString->"+checkFrom+"IMPORTJSONURL"+checkTo+"FROM"+checkFile+"-"+global.TMP_string.size()+"]}");
		}
		
		while (TMP_e.contains("{array[") && TMP_e.contains("]}.exportJson(") && TMP_e.contains(")")) {
			String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.exportJson(", TMP_e.indexOf("{array[")));
			String checkTo = TMP_e.substring(TMP_e.indexOf("]}.exportJson(")+14, TMP_e.indexOf(")", TMP_e.indexOf("]}.exportJson(")));
			String returnString;
			
			if (checkFrom.contains("{string[") && checkFrom.contains("]}")) {
				checkFrom = StringHandler.stringFunctions(checkFrom, chatEvent);
			}
			
			if (checkTo.contains(",")) {
				try {returnString = JsonHandler.exportJsonFile(checkTo.substring(0, checkTo.indexOf(",")), checkFrom, checkTo.substring(checkTo.indexOf(",")+1));} 
				catch (FileNotFoundException e) {returnString = "File not found and could not be created!";} 
				catch (UnsupportedEncodingException e) {returnString = "File could not be saved!";} 
				catch (IOException e) {returnString = "File could not be saved!";}
			} else {returnString = "Invalid arguments! expected .exportJson(fileName,nodeName)";}
			
			List<String> temporary = new ArrayList<String>();
			temporary.add("ArrayToString->"+checkFrom+"EXPORTJSON"+checkTo+"-"+(global.TMP_string.size()+1));
			temporary.add(returnString);
			global.TMP_string.add(temporary);
			global.backupTMP_strings.add(temporary);
			TMP_e = TMP_e.replace("{array["+checkFrom+"]}.exportJson("+checkTo+")", "{string[ArrayToString->"+checkFrom+"EXPORTJSON"+checkTo+"-"+global.TMP_string.size()+"]}");
		}
		
		return TMP_e;
	}
}
