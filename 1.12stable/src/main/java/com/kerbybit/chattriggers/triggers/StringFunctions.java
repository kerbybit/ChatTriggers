package com.kerbybit.chattriggers.triggers;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.file.FileHandler;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.objects.ArrayHandler;
import com.kerbybit.chattriggers.objects.DisplayHandler;
import com.kerbybit.chattriggers.objects.JsonHandler;
import com.kerbybit.chattriggers.objects.ListHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import org.apache.commons.lang3.text.WordUtils;

import java.io.*;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.kerbybit.chattriggers.triggers.StringHandler.stringFunctions;

public class StringFunctions {
    static String doStringFunctions(String stringName, String func, String args, ClientChatReceivedEvent chatEvent, Boolean isAsync) {
        func = func.toUpperCase();

        args = nestedArgs(args, chatEvent, isAsync);

        String stringValue = getStringValue(stringName, isAsync);
        if (stringValue == null) {
            stringValue = createTempString(stringName, isAsync);
        }
        Double stringPos = getStringPos(stringName, isAsync);

        String returnString;

        returnString = doStringSetFunctions(stringPos, stringName, func, args, isAsync);

        if (returnString == null)
            returnString = doStringModifyFunctions(stringName, stringValue, func, args, isAsync);
        if (returnString == null)
            returnString = doStringComparatorFunctions(stringValue, func, args);
        if (returnString == null)
            returnString = doStringMathFunctions(stringValue, func, args);
        if (returnString == null)
            returnString = doStringUserFunctions(stringValue, func, args, chatEvent);

        if (returnString != null && getIsObject(returnString)) {
            return returnString;
        } else {
            if (returnString != null) {
                if (stringPos == null) {
                    global.Async_string.put(stringName, returnString);
                } else {
                    if (stringPos >= 0) {
                        if (isAsync) {
                            global.USR_string_mark.put(stringName, returnString);
                            global.Async_string.put(stringName, returnString);
                        } else {
                            global.USR_string.put(stringName, returnString);
                        }
                    } else {
                        global.TMP_string.put(stringName, returnString);
                    }
                }
            }
        }
        return "{string[" + stringName + "]}";
    }

    private static Boolean getIsObject(String in) {
        return in.startsWith("{list[") || in.startsWith("{array[")
                || in.startsWith("{display[") || in.startsWith("{json[");
    }

    private static String doStringSetFunctions(Double stringPos,String stringName, String func, String args, Boolean isAsync) {
        args = addExtras(args);

        if (func.equals("SET")) {
            if (args.equals("~")) {
                if (stringPos == null) {
                    String set = addExtras(global.Async_string.get(stringName));
                    global.backupAsync_string.put(stringName, set);
                    return set;
                } else {
                    if (stringPos >= 0) {
                        String set = addExtras(global.USR_string.get(stringName));
                        if (isAsync) global.backupUSR_strings_mark.put(stringName, set);
                        else global.backupUSR_strings.put(stringName, set);

                        return set;
                    } else {
                        String set = addExtras(global.TMP_string.get(stringName));
                        global.backupTMP_strings.put(stringName, set);
                        return set;
                    }
                }
            } else {
                if (stringPos == null) {
                    global.Async_string.put(stringName, args);
                    global.backupAsync_string.put(stringName, args);
                    return args;
                } else {
                    if (stringPos >= 0) {
                        if (isAsync) {
                            global.USR_string_mark.put(stringName, args);
                            global.backupUSR_strings_mark.put(stringName, args);
                        } else {
                            global.USR_string.put(stringName, args);
                            global.backupUSR_strings.put(stringName, args);
                        }
                        return args;
                    } else {
                        global.TMP_string.put(stringName, args);
                        global.backupTMP_strings.put(stringName, args);
                        return args;
                    }
                }
            }
        } else if (func.equals("SAVE")) {
            String ret;
            if (args.equals("~")) {
                if (stringPos == null) {
                    String set = addExtras(global.Async_string.get(stringName));
                    global.backupAsync_string.put(stringName, set);
                    ret = set;
                } else {
                    if (stringPos >= 0) {
                        String set = addExtras(global.USR_string.get(stringName));
                        if (isAsync) global.backupUSR_strings_mark.put(stringName, set);
                        else global.backupUSR_strings.put(stringName, set);
                        ret = set;
                    } else {
                        String set = addExtras(global.TMP_string.get(stringName));
                        global.backupTMP_strings.put(stringName, set);
                        ret = set;
                    }
                }
            } else {
                if (stringPos == null) {
                    global.Async_string.put(stringName, args);
                    global.backupAsync_string.put(stringName, args);
                    ret = args;
                } else {
                    if (stringPos >= 0) {
                        if (isAsync) {
                            global.USR_string_mark.put(stringName, args);
                            global.backupUSR_strings_mark.put(stringName, args);
                        } else {
                            global.USR_string.put(stringName, args);
                            global.backupUSR_strings.put(stringName, args);
                        }
                        ret = args;
                    } else {
                        global.TMP_string.put(stringName, args);
                        global.backupTMP_strings.put(stringName, args);
                        ret = args;
                    }
                }
            }
            try {
                FileHandler.saveAll();
            } catch (IOException e) {
                ChatHandler.warn(ChatHandler.color("red", "Error saving triggers!"));
            }
            return ret;
        }

        return null;
    }

    private static String removeExtras(String in) {
        return in.replace("(","stringOpenBracketF6cyUQp9stringOpenBracket")
                .replace(")","stringCloseBracketF6cyUQp9stringCloseBracket")
                .replace(",", "stringCommaReplacementF6cyUQp9stringCommaReplacement");
    }

    private static String removeExcludedExtras(String in) {
        return in.replace("'('", "stringOpenBracketF6cyUQp9stringOpenBracket")
                .replace("')'", "stringCloseBracketF6cyUQp9stringCloseBracket")
                .replace("','", "stringCommaReplacementF6cyUQp9stringCommaReplacement");
    }

    private static String addExtras(String in) {
        return in.replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(")
                .replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")")
                .replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ",");
    }

    private static String doStringModifyFunctions(String stringName, String stringValue, String func, String args, Boolean isAsync) {
        switch (func) {
            case("REPLACE"):
                args = removeExcludedExtras(args);
                if (args.contains(",") && args.split(",").length == 2) {
                    return stringValue.replace(args.split(",")[0], args.split(",")[1]);
                } else {
                    return stringValue.replace(args,"");
                }
            case("REPLACEIGNORECASE"):
                args = removeExcludedExtras(args);
                if (args.contains(",") && args.split(",").length == 2) {
                    return stringValue.replaceAll("(?i)"+args.split(",")[0], args.split(",")[1]);
                } else {
                    return stringValue.replaceAll("(?i)"+args, "");
                }
            case("TRIM"):
                return stringValue.trim();
            case("PREFIX"):
                return args + stringValue;
            case("SUFFIX"):
                return stringValue + args;
            case("TOUPPER"):
            case("TOUPPERCASE"):
                return stringValue.toUpperCase();
            case("TOLOWER"):
            case("TOLOWERCASE"):
                return stringValue.toLowerCase();
            case("REMOVEFORMATTING"):
            case("REMFORM"):
                return ChatHandler.deleteFormatting(stringValue);
            case("IGNOREFORMATTING"):
            case("IGNOREFORM"):
                return ChatHandler.ignoreFormatting(stringValue);
            case("ADDCOMMAS"):
                return NumberFormat.getNumberInstance().format(Long.parseLong(stringValue));
            case("CAPITALIZEFIRSTWORD"):
            case("CAPFIRST"):
                if (stringValue.equals("")) return stringValue;
                else return stringValue.substring(0,1).toUpperCase()+stringValue.substring(1);
            case("CAPITALIZEALLWORDS"):
            case("CAPALL"):
                return WordUtils.capitalizeFully(stringValue);
            case("IGNOREESCAPE"):
                return stringValue.replace("\\", "\\\\");
            case("FIXLINKS"):
                for (String value : stringValue.split(" ")) {
                    String newvalue = ChatHandler.deleteFormatting(value);
                    value = value.replace("...","TripleDotF6cyUQp9TripleDot");
                    if (value.contains(".")) {
                        if (!(newvalue.toUpperCase().startsWith("HTTP://") || newvalue.toUpperCase().startsWith("HTTPS://"))) {
                            newvalue = "http://"+value;
                        }
                        stringValue = stringValue.replace(value.replace("...","TripleDotF6cyUQp9TripleDot"), "{link[" + value + "],[" + newvalue + "]}");
                    }
                }
                return stringValue;
            case("SPLIT"):
                String[] splitString = stringValue.split(args);
                StringBuilder list = new StringBuilder("[");
                for (String value : splitString) {
                    list.append(value).append(",");
                }
                list = new StringBuilder(list.substring(0, list.length()-1) + "]");

                String list_name;
                if (isAsync)
                    list_name = "AsyncStringToList->"+stringName+"SPLIT-"+(ListHandler.getListsSize()+1);
                else
                    list_name = "StringToList->"+stringName+"SPLIT-"+(ListHandler.getListsSize()+1);
                ListHandler.getList(list_name, list.toString());
                return "{list[" + list_name + "]}";
            case("SUBSTRING"):
                args = removeExcludedExtras(args);
                String[] subargs = args.split(",");
                if (subargs.length == 2) {
                    int first = -1;
                    int last = -1;
                    Boolean getStart = false;
                    Boolean startContain = false;
                    Boolean startAlwaysNumber = false;
                    Boolean startAlwaysText = false;
                    Boolean getEnd = false;
                    Boolean endContain = false;
                    Boolean endAlwaysNumber = false;
                    Boolean endAlwaysText = false;
                    if (subargs[0].toUpperCase().contains("<START>") || subargs[0].toUpperCase().contains("<S>"))
                        getStart = true;
                    if (subargs[0].toUpperCase().contains("<INCLUDE>") || subargs[0].toUpperCase().contains("<I>"))
                        startContain = true;
                    if (subargs[0].toUpperCase().contains("<NUMBER>") || subargs[0].toUpperCase().contains("<N>"))
                        startAlwaysNumber = true;
                    if (subargs[0].toUpperCase().contains("<TEXT>") || subargs[0].toUpperCase().contains("<T>"))
                        startAlwaysText = true;
                    subargs[0] = subargs[0].replaceAll("(?i)<start>|<s>|<include>|<i>|<number>|<n>|<text>|<t>", "");

                    if (subargs[1].toUpperCase().contains("<END>") || subargs[1].toUpperCase().contains("<E>"))
                        getEnd = true;
                    if (subargs[1].toUpperCase().contains("<INCLUDE>") || subargs[1].toUpperCase().contains("<I>"))
                        endContain = true;
                    if (subargs[1].toUpperCase().contains("<NUMBER>") || subargs[1].toUpperCase().contains("<N>"))
                        endAlwaysNumber = true;
                    if (subargs[1].toUpperCase().contains("<TEXT>") || subargs[1].toUpperCase().contains("<T>"))
                        endAlwaysText = true;
                    subargs[1] = subargs[1].replaceAll("(?i)<end>|<e>|<include>|<i>|<number>|<n>|<text>|<t>", "");

                    String temp = removeExtras(stringValue);
                    if (getStart) {
                        first = 0;
                        if (!subargs[0].equals("")) {
                            try {
                                int indexFromStart = Integer.parseInt(subargs[0]);
                                if (indexFromStart > 0) first = indexFromStart;
                            } catch (NumberFormatException exception) {
                                // do nothing //
                            }
                        }
                    }
                    if (getEnd) {
                        last = temp.length();
                        if (!subargs[1].equals("")) {
                            try {
                                int indexFromEnd = Integer.parseInt(subargs[1]);
                                if (indexFromEnd < 0) last = temp.length()+indexFromEnd;
                            } catch (NumberFormatException exception) {
                                // do nothing //
                            }
                        }
                    }
                    if (first == -1) {
                        if (!startAlwaysText) {
                            try {
                                first = Integer.parseInt(subargs[0]);
                            } catch (NumberFormatException e) {
                                if (global.debug) ChatHandler.warn(ChatHandler.color("gray", "Did not find " + subargs[0] + " in string"));
                                return null;
                            }

                        } else if (temp.contains(subargs[0]) && !startAlwaysNumber) {
                            if (startContain) first = temp.indexOf(subargs[0]);
                            else first = temp.indexOf(subargs[0]) + subargs[0].length();
                        }
                    }
                    if (last == -1) {
                        if (!endAlwaysText) {
                            try {
                                last = Integer.parseInt(subargs[1]);
                            } catch (NumberFormatException e) {
                                if (global.debug) ChatHandler.warn(ChatHandler.color("gray", "Did not find " + subargs[1] + " in string"));
                                return null;
                            }
                        } else if (temp.contains(subargs[1]) && !endAlwaysNumber) {
                            if (endContain) last = temp.indexOf(subargs[1]) + subargs[1].length();
                            else last = temp.indexOf(subargs[1]);
                        }
                    }

                    if (first != -1 && last != -1) {
                        if (first <= last) {
                            if (last < temp.length()) {
                                temp = temp.substring(first, last);
                                return addExtras(temp);
                            }
                        }
                        if (global.debug) ChatHandler.warn("red", "Error in .substring() - Index out of bounds");
                        return null;
                    }
                }
            case("LOAD"):
                if (args.toUpperCase().startsWith("HTTP"))
                    return getStringFromURL(args);
                else
                    return getStringFromFile(args);
            case("EXPORT"):
                return saveStringToFile(stringValue, args);
            default:
                return null;
        }
    }

    private static String saveStringToFile(String value, String dest) {
        dest = dest.replace("./mods/ChatTriggers/", "./");
        dest = dest.replace("./", "./mods/ChatTriggers/");

        if (!dest.contains("/")) {
            dest = "./mods/ChatTriggers/"+dest;
        }

        try {
            PrintWriter writer = new PrintWriter(dest, "UTF-8");
            writer.println(value);
            writer.close();
        } catch (FileNotFoundException exception) {
            dest = dest.replace("./mods/ChatTriggers/", "");
            String check_str = dest.substring(0, dest.lastIndexOf("/"));

            if (check_str.contains("/")) {
                StringBuilder folder = new StringBuilder("./mods/ChatTriggers/");
                for (String dir : check_str.split("/")) {
                    folder = folder.append(dir).append("/");
                    File check = new File(folder.toString());
                    if (!check.mkdir()) {
                        break;
                    }
                }
                if (new File(folder.toString()).exists()) {
                    saveStringToFile(value, "./" + dest);
                }
            } else {
                File check = new File("./mods/ChatTriggers/" + check_str);
                if (!check.mkdir())
                    ChatHandler.warn("red", "Unable to save json to file!");
                else {
                    saveStringToFile(value, "./" + dest);
                }
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            return "Unable to save string to file! IOException";
        }

        return value;
    }

    private static String getStringFromURL(String url) {
        try {
            StringBuilder listString = new StringBuilder();
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(url).openStream(),"UTF-8"));
            while ((line = bufferedReader.readLine()) != null) {
                listString.append(line);
            }
            bufferedReader.close();

            return listString.toString();
        } catch (Exception exception) {
            return null;
        }
    }

    private static String getStringFromFile(String dest) {
        try {
            if (!dest.contains("/")) {
                dest = "./mods/ChatTriggers/"+dest;
            }

            StringBuilder listString = new StringBuilder();
            String line;
            BufferedReader bufferedReader;
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(dest),"UTF-8"));
            while ((line = bufferedReader.readLine()) != null) {
                listString.append(line.trim());
            }
            return listString.toString();
        } catch (Exception exception) {
            return null;
        }
    }

    private static String doStringComparatorFunctions(String stringValue, String func, String args) {
        switch (func) {
            case "EQUALS":
            case "=":
            case "==":
                return trimBool(stringValue.equals(args));
            case "EQUALSIGNORECASE":
                return trimBool(stringValue.equalsIgnoreCase(args));
            case "STARTSWITH":
                return trimBool(stringValue.startsWith(args));
            case "STARTSWITHIGNORECASE":
                return trimBool(stringValue.toUpperCase().startsWith(args.toUpperCase()));
            case "CONTAINS":
                return trimBool(stringValue.contains(args));
            case "CONTAINSIGNORECASE":
                return trimBool(stringValue.toUpperCase().contains(args.toUpperCase()));
            case "ENDSWITH":
                return trimBool(stringValue.endsWith(args));
            case "ENDSWITHIGNORECASE":
                return trimBool(stringValue.toUpperCase().endsWith(args.toUpperCase()));
            case "MATCHESREGEX":
            case "HASREGEX":
                return trimBool(stringValue.matches(args));
            case "CALCINSIDE":
                String[] argList = args.split(",");
                int x = Integer.parseInt(argList[0]);
                int y = Integer.parseInt(argList[1]);
                int z = Integer.parseInt(argList[2]);
                int x1 = Integer.parseInt(argList[3]);
                int y1 = Integer.parseInt(argList[4]);
                int z1 = Integer.parseInt(argList[5]);
                int x2 = Integer.parseInt(argList[6]);
                int y2 = Integer.parseInt(argList[7]);
                int z2 = Integer.parseInt(argList[8]);

                if (((x <= x1 && x >= x2) || (x >= x1 && x <= x2)) && ((y <= y1 && y >= y2) || (y >= y1 && y <= y2)) && ((z <= z1 && z >= z2) || (z >= z1 && z <= z2))) {
                    return "true";
                } else {
                    return "false";
                }
        }

        Double stringValueNumber;
        Double argsValueNumber;
        try {
            stringValueNumber = Double.parseDouble(stringValue.replace(",", ""));
            argsValueNumber = Double.parseDouble(args.replace(",", ""));
        } catch (NumberFormatException e) {
            return null;
        }

        switch (func) {
            case "LESSTHAN":
            case "LT":
            case "<":
                return trimBool(stringValueNumber < argsValueNumber);
            case "LESSTHANOREQUALTO":
            case "LTE":
            case "<=":
                return trimBool(stringValueNumber <= argsValueNumber);
            case "GREATERTHAN":
            case "GT":
            case ">":
                return trimBool(stringValueNumber > argsValueNumber);
            case "GREATERTHANOREQUALTO":
            case "GTE":
            case ">=":
                return trimBool(stringValueNumber >= argsValueNumber);
        }

        return null;
    }

    private static String trimBool(Boolean in) {
        return in ? "true" : "false";
    }

    private static String doStringMathFunctions(String stringValue,String func, String args) {
        if (func.equals("LENGTH")) {
            return trimNumber(stringValue.length());
        } else if (func.equals("SIZE")) {
            return trimNumber(Minecraft.getMinecraft().fontRenderer.getStringWidth(stringValue));
        }

        Double stringValueNumber;
        Double argsValueNumber;
        try {
            stringValueNumber = Double.parseDouble(stringValue.replace(",", ""));
        } catch (NumberFormatException e) {
            return null;
        }

        try {
            argsValueNumber = Double.parseDouble(args.replace(",", ""));
        } catch (NumberFormatException e) {
            argsValueNumber = null;
        }

        if (argsValueNumber != null) {
            switch (func) {
                case "ADD":
                case "PLUS":
                case "+":
                    return trimNumber(stringValueNumber + argsValueNumber);
                case "SUBTRACT":
                case "MINUS":
                case "-":
                    return trimNumber(stringValueNumber - argsValueNumber);
                case "MULTIPLY":
                case "TIMES":
                case "*":
                    return trimNumber(stringValueNumber * argsValueNumber);
                case "DIVIDE":
                case "/":
                    return trimNumber(stringValueNumber / argsValueNumber);
                case "DIVIDEGETPERCENT":
                case "DIVPERCENT":
                case "/%":
                    return trimNumber((stringValueNumber / argsValueNumber) * 100);
                case "POW":
                case "POWER":
                case "^":
                    return trimNumber(Math.pow(stringValueNumber, argsValueNumber));
                case "MOD":
                case "MODULUS":
                case "%":
                    return trimNumber(stringValueNumber % argsValueNumber);
				case "ATAN2":
				case "ATANTWO":
					return trimNumber(Math.toDegrees(Math.atan2(stringValueNumber, argsValueNumber)));
                case "ROUND":
                    return trimNumber(argsValueNumber * (Math.round(stringValueNumber / argsValueNumber)));
                case "FLOOR":
                    return trimNumber(argsValueNumber * (Math.floor(stringValueNumber / argsValueNumber)));
                case "CEIL":
                    return trimNumber(argsValueNumber * (Math.ceil(stringValueNumber / argsValueNumber)));
            }
        }

        switch (func) {
            case "ROUND":
                if (argsValueNumber == null) {
                    return trimNumber(Math.round(stringValueNumber));
                } else {
                    return trimNumber(Math.round(stringValueNumber * Math.pow(10, argsValueNumber)) / Math.pow(10, argsValueNumber));
                }
            case "FLOOR":
                return trimNumber(Math.floor(stringValueNumber));
            case "CEIL":
                return trimNumber(Math.ceil(stringValueNumber));
			case "SIN":
				return trimNumber(Math.sin(Math.toRadians(stringValueNumber)));
			case "COS":
				return trimNumber(Math.cos(Math.toRadians(stringValueNumber)));
			case "TAN":
				return trimNumber(Math.tan(Math.toRadians(stringValueNumber)));
			case "ASIN":
				return trimNumber(Math.toDegrees(Math.asin(stringValueNumber)));
			case "ACOS":
				return trimNumber(Math.toDegrees(Math.acos(stringValueNumber)));
			case "ATAN":
				return trimNumber(Math.toDegrees(Math.atan(stringValueNumber)));
			case "ABSOLUTE":
			case "ABS":
				return trimNumber(Math.abs(stringValueNumber));
			case "SQRT":
				return trimNumber(Math.sqrt(stringValueNumber));
        }

        return null;
    }

    private static String trimNumber(Double number) {
        String numberValue = number + "";
        if (numberValue.endsWith(".0")) numberValue = numberValue.substring(0, numberValue.length()-2);
        return numberValue;
    }

    private static String trimNumber(Integer number) {
        return number+"";
    }
    private static String trimNumber(Long number) {return trimNumber((double) number);}

    private static String doStringUserFunctions(String stringValue, String func, String args, ClientChatReceivedEvent chatEvent) {
        for (List<String> function : global.function) {
            if (function.size() > 2) {
                String func_define = function.get(1);
                if (func_define.contains(".") && func_define.contains("(") && func_define.contains(")")) {
                    String func_name = func_define.substring(func_define.indexOf(".")+1, func_define.indexOf("(", func_define.indexOf("."))).toUpperCase();
                    if (func_name.equalsIgnoreCase(func)) {
                        String func_to = TagHandler.removeTags(func_define.substring(0, func_define.indexOf(".")));
                        String func_arg = func_define.substring(func_define.indexOf("(")+1, func_define.indexOf(")", func_define.indexOf(")")));
                        if (!func_arg.equals("")) {
                            String[] func_args = func_arg.split(",");
                            String[] func_args_to = args.split(",");
                            if (func_args.length == func_args_to.length) {
                                List<String> TMP_events = new ArrayList<>();
                                for (int j = 2; j < function.size(); j++) {
                                    TMP_events.add(function.get(j));
                                }
                                return EventsHandler.doEvents(TMP_events, chatEvent, global.append(func_args, func_to), global.append(func_args_to, stringValue));
                            }
                        } else {
                            if (args.equals("")) {
                                List<String> TMP_events = new ArrayList<>();
                                for (int j = 2; j < function.size(); j++) {
                                    TMP_events.add(function.get(j));
                                }
                                return EventsHandler.doEvents(TMP_events, chatEvent, new String[] {func_to}, new String[] {stringValue});
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private static String getStringValue(String stringName, Boolean isAsync) {
        if (isAsync) {
            for (Map.Entry<String, String> string : global.Async_string.entrySet()) {
                if (stringName.equals(string.getKey())) {
                    return string.getValue();
                }
            }
        } else {
            if (global.USR_string.containsKey(stringName)) {
                return global.USR_string.get(stringName);
            }

            if (global.TMP_string.containsKey(stringName)) {
                return global.TMP_string.get(stringName);
            }
        }

        return null;
    }

    private static Double getStringPos(String stringName, Boolean isAsync) {
        if (global.USR_string.containsKey(stringName)) {
            return 1.0;
        }

        if (!isAsync) {
            return -1.0;
        }
        return null;
    }

    private static String createTempString(String stringName, Boolean isAsync) {
        if (isAsync) {
            global.Async_string.put(stringName, "");
            global.backupAsync_string.put(stringName, "");
            return "";
        } else {
            global.TMP_string.put(stringName, "");
            global.backupTMP_strings.put(stringName, "");
            return "";
        }
    }

    public static String nestedArgs(String args, ClientChatReceivedEvent chatEvent, Boolean isAsync) {
        args = stringFunctions(args, chatEvent, isAsync);
        while (args.contains("{array[") && args.contains("]}")) {
            args = ArrayHandler.arrayFunctions(args, chatEvent, isAsync);
            args = stringFunctions(args, chatEvent, isAsync);
        }
        while (args.contains("{display[") && args.contains("]}")) {
            args = DisplayHandler.displayFunctions(args, isAsync);
            args = stringFunctions(args, chatEvent, isAsync);
        }
        while (args.contains("{list[") && args.contains("]}")) {
            args = ListHandler.listFunctions(args, isAsync);
            args = stringFunctions(args, chatEvent, isAsync);
        }
        while (args.contains("{json[") && args.contains("]}")) {
            args = JsonHandler.jsonFunctions(args, isAsync);
            args = stringFunctions(args, chatEvent, isAsync);
        }
        while (args.contains("{string[") && args.contains("]}")) {
            args = stringFunctions(args, chatEvent, isAsync);
        }
        return args;
    }
}
