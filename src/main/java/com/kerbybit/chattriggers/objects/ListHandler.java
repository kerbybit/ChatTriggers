package com.kerbybit.chattriggers.objects;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.triggers.StringHandler;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListHandler {
    private static ArrayList<String> getListFromURL(String url) {
        try {
            String listString = "";
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(url).openStream(),"UTF-8"));
            while ((line = bufferedReader.readLine()) != null) {
                listString+=line;
            }
            bufferedReader.close();

            return getListFromValue(listString);
        } catch (MalformedURLException e1) {
            return null;
        } catch (Exception e2) {
            return null;
        }
    }

    private static ArrayList<String> getListFromFile(String dest) {
        try {
            String listString = "";
            String line;
            BufferedReader bufferedReader;
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(dest),"UTF-8"));
            while ((line = bufferedReader.readLine()) != null) {
                listString += line;
            }
            return getListFromValue(listString);
        } catch (Exception e1) {
            return null;
        }
    }

    private static ArrayList<String> getListFromValue(String value) {
        if (value.startsWith("[") && value.endsWith("]")) {
            String[] list = value.substring(1, value.length()-1).split(",");
            return new ArrayList<String>(Arrays.asList(list));
        } else {
            return null;
        }
    }

    private static String getList(String list_name, String list_from) {
        ArrayList<String> list_object = new ArrayList<String>();
        if (list_from.toUpperCase().startsWith("HTTP")) {
            list_object = getListFromURL(list_from);
        } else if (list_from.contains("[")) {
            list_object = getListFromValue(list_from);
        } else {
            list_object = getListFromFile(list_from);
        }

        if (list_object == null) {
            return "Unable to load list!";
        } else {
            clearList(list_name);
            global.lists.put(list_name, list_object);
            String return_string = "[";
            for (String value : list_object) {
                return_string += value + ",";
            }
            return_string = return_string.substring(0, return_string.length()-1) + "]";
            return return_string;
        }
    }

    private static String getList(String list_name) {
        if (global.lists.containsKey(list_name)) {
            String return_string = "[";
            for (String value : global.lists.get(list_name)) {
                return_string += value + ",";
            }
            return_string = return_string.substring(0, return_string.length()-1) + "]";
            return return_string;
        } else {
            return "Not a list";
        }
    }

    private static ArrayList<String> getListObject(String list_name) {
        if (global.lists.containsKey(list_name)) {
            return new ArrayList<String>(global.lists.get(list_name));
        } else {
            return null;
        }
    }

    private static String addToList(String list_name, String value) {
        List<String> list_object = getListObject(list_name);

        if (list_object != null) {
            if (value.startsWith("[") && value.endsWith("]")) {
                String[] values = value.substring(1, value.length()-1).split(",");
                list_object.addAll(Arrays.asList(values));
                global.lists.put(list_name, list_object);
                return getList(list_name);
            } else {
                list_object.add(value);
                global.lists.put(list_name, list_object);
                return getList(list_name);
            }
        } else {
            return "Not a list";
        }
    }

    private static String clearList(String list_name) {
        if (global.lists.containsKey(list_name)) {
            String return_string = getList(list_name);
            global.lists.remove(list_name);
            return return_string;
        } else {
            return "Not a list";
        }
    }

    public static String listFunctions(String TMP_e) {
        while (TMP_e.contains("{list[") && TMP_e.contains("]}.load(") && TMP_e.contains(")")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{list[")+6, TMP_e.indexOf("]}.load(", TMP_e.indexOf("{list[")));
            String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.load(", TMP_e.indexOf("{list["))+8, TMP_e.indexOf(")", TMP_e.indexOf("]}.load(", TMP_e.indexOf("{list["))));
            while (get_prevalue.contains("(")) {
                String temp_search = TMP_e.substring(TMP_e.indexOf("]}.load(", TMP_e.indexOf("{list["))+8);
                temp_search = temp_search.replaceFirst("\\(","tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)","tempCloseBreacketF6cyUQp9tempCloseBracket");
                get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
            }
            get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket","(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket",")");
            String get_value = StringHandler.stringFunctions(get_prevalue, null);
            get_value = listFunctions(get_value);

            List<String> temporary = new ArrayList<String>();
            temporary.add("ListToString->"+get_name+"LOAD"+get_value+"-"+(global.TMP_string.size()+1));
            temporary.add(getList(get_name, get_value));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{list["+get_name+"]}.load("+get_prevalue+")","{string[ListToString->"+get_name+"LOAD"+get_value+"-"+global.TMP_string.size()+"]}");
        }

        while (TMP_e.contains("{list[") && TMP_e.contains("]}.add(") && TMP_e.contains(")")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{list[")+6, TMP_e.indexOf("]}.add(", TMP_e.indexOf("{list[")));
            String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.add(", TMP_e.indexOf("{list["))+7, TMP_e.indexOf(")", TMP_e.indexOf("]}.add(", TMP_e.indexOf("{list["))));
            while (get_prevalue.contains("(")) {
                String temp_search = TMP_e.substring(TMP_e.indexOf("]}.add(", TMP_e.indexOf("{list["))+7);
                temp_search = temp_search.replaceFirst("\\(","tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)","tempCloseBreacketF6cyUQp9tempCloseBracket");
                get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
            }
            get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket","(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket",")");
            String get_value = StringHandler.stringFunctions(get_prevalue, null);
            get_value = listFunctions(get_value);

            List<String> temporary = new ArrayList<String>();
            temporary.add("ListToString->"+get_name+"ADD"+get_value+"-"+(global.TMP_string.size()+1));
            temporary.add(addToList(get_name, get_value));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{list["+get_name+"]}.add("+get_prevalue+")","{string[ListToString->"+get_name+"ADD"+get_value+"-"+global.TMP_string.size()+"]}");
        }

        while (TMP_e.contains("{list[") && TMP_e.contains("]}.clear()")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{list[")+6, TMP_e.indexOf("]}.clear()", TMP_e.indexOf("{list[")));


            List<String> temporary = new ArrayList<String>();
            temporary.add("ListToString->"+get_name+"CLEAR"+"-"+(global.TMP_string.size()+1));
            temporary.add(clearList(get_name));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{list["+get_name+"]}.clear()","{string[ListToString->"+get_name+"CLEAR"+"-"+global.TMP_string.size()+"]}");
        }


        while (TMP_e.contains("{list[") && TMP_e.contains("]}")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{list[")+6, TMP_e.indexOf("]}", TMP_e.indexOf("{list[")));

            List<String> temporary = new ArrayList<String>();
            temporary.add("ListToString->"+get_name+"LITERAL"+"-"+(global.TMP_string.size()+1));
            temporary.add(getList(get_name));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{list["+get_name+"]}", "{string[ListToString->"+get_name+"LITERAL"+"-"+global.TMP_string.size()+"]}");
        }

        return TMP_e;
    }
}
