package com.kerbybit.chattriggers.triggers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.kerbybit.chattriggers.file.JsonHandler;
import com.kerbybit.chattriggers.globalvars.global;

class ArrayHandler {
	static String arrayFunctions(String TMP_e) {
		while (TMP_e.contains("{array[") && TMP_e.contains("]}.setSplit(") && TMP_e.contains(",") && TMP_e.contains(")")) {
			String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.setSplit(", TMP_e.indexOf("{array[")));
			String checkTo = TMP_e.substring(TMP_e.indexOf("]}.setSplit(")+12, TMP_e.indexOf(")", TMP_e.indexOf("]}.setSplit(")));
			String returnString = "Something went wrong with parsing setSplit!";
			Boolean isArray = false;
			
			if (checkFrom.contains("{string[") && checkFrom.contains("]}")) {
				checkFrom = StringHandler.stringFunctions(checkFrom);
			}
			
			String[] args = checkTo.split(",");
			if (args.length==2) {
				for (int j=0; j<global.USR_array.size(); j++) {
					if (global.USR_array.get(j).get(0).equals(checkFrom)) {
						String[] moreargs = args[0].split(args[1]);
						List<String> temporary = new ArrayList<String>();
						temporary.addAll(Arrays.asList(moreargs));
						returnString = "[";
						for (String value : temporary) {returnString+=value + " ";}
						returnString = returnString.trim().replace(" ",",")+"]";
						global.USR_array.get(j).addAll(temporary);
						isArray = true;
					}
				}
				if (!isArray) {
					String[] moreargs = args[0].split(args[1]);
					List<String> temporary = new ArrayList<String>();
					temporary.add(checkFrom);
					List<String> temp = new ArrayList<String>();
					temporary.addAll(Arrays.asList(moreargs));
					returnString = "[";
					for (String value : temp) {returnString+=value + " ";}
					returnString = returnString.trim().replace(" ",",")+"]";
					temporary.addAll(temp);
					global.USR_array.add(temporary);
				}
			} else {returnString = "setSplit formatted wrong! use .setSplit(value,split)";}
			
			List<String> temporary = new ArrayList<String>();
			temporary.add("ArrayToString->"+checkFrom+"SETSPLIT"+checkTo+"-"+(global.TMP_string.size()+1));
			temporary.add(returnString);
			global.TMP_string.add(temporary);
			global.backupTMP_strings.add(temporary);
			TMP_e = TMP_e.replace("{array[" + checkFrom + "]}.setSplit(" + checkTo + ")", "{string[ArrayToString->"+checkFrom+"SETSPLIT"+checkTo+"-"+global.TMP_string.size()+"]}");
		}
		
		while (TMP_e.contains("{array[") && TMP_e.contains("]}.add(") && TMP_e.contains(")")) {
			String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.add(", TMP_e.indexOf("{array[")));
			String checkTo = TMP_e.substring(TMP_e.indexOf("]}.add(")+7, TMP_e.indexOf(")", TMP_e.indexOf("]}.add(")));
			Boolean isArray = false;
			
			if (checkFrom.contains("{string[") && checkFrom.contains("]}")) {
				checkFrom = StringHandler.stringFunctions(checkFrom);
			}
			
			for (int j=0; j<global.USR_array.size(); j++) {
				if (global.USR_array.get(j).get(0).equals(checkFrom)) {
					global.USR_array.get(j).add(checkTo);
					isArray = true;
				}
			}
			
			if (!isArray) {
				List<String> prearray = new ArrayList<String>();
				prearray.add(checkFrom);
				prearray.add(checkTo);
				global.USR_array.add(prearray);
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
                checkFrom = StringHandler.stringFunctions(checkFrom);
            }

            for (int j=0; j<global.USR_array.size(); j++) {
                if (global.USR_array.get(j).get(0).equals(checkFrom)) {
                    global.USR_array.get(j).add(1, checkTo);
                    isArray = true;
                }
            }

            if (!isArray) {
                List<String> prearray = new ArrayList<String>();
                prearray.add(checkFrom);
                prearray.add(checkTo);
                global.USR_array.add(prearray);
            }

            List<String> temporary = new ArrayList<String>();
            temporary.add("ArrayToString->"+checkFrom+"PREPEND"+checkTo+"-"+(global.TMP_string.size()+1));
            temporary.add(checkTo);
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
            TMP_e = TMP_e.replace("{array[" + checkFrom + "]}.add(" + checkTo + ")", "{string[ArrayToString->"+checkFrom+"PREPEND"+checkTo+"-"+global.TMP_string.size()+"]}");
        }
		
		while (TMP_e.contains("{array[") && TMP_e.contains("]}.clear()")) {
			String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.clear()", TMP_e.indexOf("{array[")));
			String returnString = checkFrom + " is not an array!";
			
			if (checkFrom.contains("{string[") && checkFrom.contains("]}")) {
				checkFrom = StringHandler.stringFunctions(checkFrom);
			}
			
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
			global.backupTMP_strings.add(temporary);
			TMP_e = TMP_e.replace("{array[" + checkFrom + "]}.clear()", "{string[ArrayToString->"+checkFrom+"CLEAR"+"-"+global.TMP_string.size()+"]}");
		}
		
		while (TMP_e.contains("{array[") && TMP_e.contains("]}.has(") && TMP_e.contains(")")) {
			String checkFrom = TMP_e.substring(TMP_e.indexOf("{array[")+7, TMP_e.indexOf("]}.has(", TMP_e.indexOf("{array[")));
			String checkTo = TMP_e.substring(TMP_e.indexOf("]}.has(")+7, TMP_e.indexOf(")", TMP_e.indexOf("]}.has(")));
			String checkThis = "false";
			
			if (checkFrom.contains("{string[") && checkFrom.contains("]}")) {
				checkFrom = StringHandler.stringFunctions(checkFrom);
			}
			
			for (int j=0; j<global.USR_array.size(); j++) {
				if (global.USR_array.get(j).get(0).equals(checkFrom)) {
					for (int k=1; k<global.USR_array.get(j).size(); k++) {
						if (global.USR_array.get(j).get(k).equals(checkTo)) {checkThis = "true";}
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
				checkFrom = StringHandler.stringFunctions(checkFrom);
			}
			
			for (int j=0; j<global.USR_array.size(); j++) {
				if (global.USR_array.get(j).get(0).equals(checkFrom)) {
					for (int k=1; k<global.USR_array.get(j).size(); k++) {
						if (global.USR_array.get(j).get(k).equalsIgnoreCase(checkTo)) {checkThis = "true";}
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
				checkFrom = StringHandler.stringFunctions(checkFrom);
			}
			
			try {
				toRemove = Integer.parseInt(checkTo);
				if (toRemove > 0) {
					for (int j=0; j<global.USR_array.size(); j++) {
						if (global.USR_array.get(j).get(0).equals(checkFrom)) {
							if (toRemove < global.USR_array.get(j).size()) {
								removed = global.USR_array.get(j).remove(toRemove);
								returnString = removed;
								if (global.USR_array.get(j).size()==1) {toRemoveArray = j;}
							} else {returnString = "Value over bounds! (index "+toRemove+" - expecting "+global.USR_array.size()+")";}
						}
					}
				} else {returnString = "Value under bounds! (index "+toRemove+" - expecting 1)";}
			} catch (NumberFormatException e) {
				for (int j=0; j<global.USR_array.size(); j++) {
					if (global.USR_array.get(j).get(0).equals(checkFrom)) {
						for (int k=1; k<global.USR_array.get(j).size(); k++) {
							if (global.USR_array.get(j).get(k).equals(checkTo)) {
								removed = global.USR_array.get(j).remove(k);
								returnString = removed;
								if (global.USR_array.get(j).size()==1) {toRemoveArray = j;}
							}
						}
					}
				}
			}
			
			if (toRemoveArray != -1) {global.USR_array.remove(toRemoveArray);}
			
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
				checkFrom = StringHandler.stringFunctions(checkFrom);
			}
			
			try {
				toGet = Integer.parseInt(checkTo);
				if (toGet > 0) {
					for (int j=0; j<global.USR_array.size(); j++) {
						if (global.USR_array.get(j).get(0).equals(checkFrom)) {
							if (toGet < global.USR_array.get(j).size()) {
								got = global.USR_array.get(j).get(toGet);
								returnString = got;
							} else {returnString = "Value over bounds! (index "+toGet+" - expecting "+global.USR_array.size()+")";}
						}
					}
				} else {returnString = "Value under bounds! (index "+toGet+" - expecting 1)";}
			} catch (NumberFormatException e) {
				for (int j=0; j<global.USR_array.size(); j++) {
					if (global.USR_array.get(j).get(0).equals(checkFrom)) {
						returnString = "-1";
						for (int k=1; k<global.USR_array.get(j).size(); k++) {
							if (global.USR_array.get(j).get(k).equals(checkTo)) {
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
				checkFrom = StringHandler.stringFunctions(checkFrom);
			}
			
			for (int j=0; j<global.USR_array.size(); j++) {
				if (global.USR_array.get(j).get(0).equals(checkFrom)) {arraysize = global.USR_array.get(j).size()-1;}
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
				checkFrom = StringHandler.stringFunctions(checkFrom);
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
				checkFrom = StringHandler.stringFunctions(checkFrom);
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
				checkFrom = StringHandler.stringFunctions(checkFrom);
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
