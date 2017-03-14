package com.kerbybit.chattriggers.objects;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.triggers.StringHandler;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TODO bad class name. pls change
public class NewJsonHandler {
    private static JsonObject getJsonFromURL(String url) {
        try {
            String jsonString = "";
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(url).openStream(),"UTF-8"));
            while ((line = bufferedReader.readLine()) != null) {
                jsonString+=line;
            }
            bufferedReader.close();

            return new JsonParser().parse(jsonString).getAsJsonObject();
        } catch (MalformedURLException e1) {
            return null;
        } catch (Exception e2) {
            return null;
        }
    }

    private static JsonObject getJsonFromFile(String dest) {
        try {
            String jsonString = "";
            String line;
            BufferedReader bufferedReader;
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(dest),"UTF-8"));
            while ((line = bufferedReader.readLine()) != null) {
                jsonString += line;
            }
            return new JsonParser().parse(jsonString).getAsJsonObject();
        } catch (Exception e1) {
            return null;
        }
    }

    private static JsonObject getJsonFromValue(String value) {
        try {
            return new JsonParser().parse(value).getAsJsonObject();
        } catch (Exception e1) {
            return null;
        }
    }

    public static String getJson(String json_name, String json_from) {
        JsonObject json_object;
        if (json_from.toUpperCase().startsWith("HTTP")) {
            json_object = getJsonFromURL(json_from);
        } else if (json_from.contains("{")) {
            json_object = getJsonFromValue(json_from);
        } else {
            json_object = getJsonFromFile(json_from);
        }

        if (json_object == null) {
            return "Unable to load Json!";
        } else {
            clearJson(json_name);
            global.jsons.put(json_name, json_object);
            return json_object + "";
        }
    }

    private static String getValue(String key, String value) {
        if (global.jsons.containsKey(key)) {
            try {
                JsonObject obj = global.jsons.get(key);
                String[] seg = value.split("\\.");
                String returnString = "null";
                for (String element : seg) {
                    if (obj != null) {
                        JsonElement ele = obj.get(element);
                        if (!ele.isJsonObject()) {
                            returnString = ele + "";
                        } else {
                            obj = ele.getAsJsonObject();
                            returnString = obj + "";
                        }
                    } else {
                        return "null";
                    }
                }
                if (returnString.startsWith("\"") && returnString.endsWith("\"")) {
                    returnString = returnString.substring(1, returnString.length()-1);
                }
                return returnString;
            } catch (Exception e) {
                e.printStackTrace();
                return "null";
            }
        } else {
            return "Not a json";
        }
    }

    private static String getValue(String key) {
        if (global.jsons.containsKey(key)) {
            return global.jsons.get(key) + "";
        } else {
            return "Not a json";
        }
    }

    private static String getKeys(String key, String value) {
        String returnString;
        JsonObject obj;
        if (value.equals("")) {
            returnString = getValue(key);
            obj = new JsonParser().parse(returnString).getAsJsonObject();
        } else {
            returnString = getValue(key, value);
            obj = new JsonParser().parse(returnString).getAsJsonObject();
        }
        List<String> keys = new ArrayList<String>();
        for (Map.Entry<String, JsonElement> ele : obj.entrySet()) {
            keys.add(ele.getKey());
        }

        returnString = "[";
        for (String element : keys) {
            returnString += element + ",";
        }
        if (returnString.equals("[")) {
            returnString += "]";
        } else {
            returnString = returnString.substring(0, returnString.length()-1) + "]";
        }


        return returnString;
    }

    private static String getValues(String key, String value) {
        String returnString = getValue(key, value);
        JsonObject obj = new JsonParser().parse(returnString).getAsJsonObject();
        List<String> keys = new ArrayList<String>();
        for (Map.Entry<String, JsonElement> eles : obj.entrySet()) {
            String ele = eles.getValue() + "";
            if (ele.startsWith("\"") && ele.endsWith("\"")) {
                ele = ele.substring(1, ele.length()-1);
            }
            keys.add(ele);
        }

        returnString = "[";
        for (String element : keys) {
            returnString += element + ",";
        }
        returnString = returnString.substring(0, returnString.length()-1) + "]";

        return returnString;
    }

    private static void clearJsons() {
        global.jsons.clear();
    }

    private static String clearJson(String name) {
        if (global.jsons.containsKey(name)) {
            global.jsons.remove(name);
            return "Cleared json " + name;
        } else {
            return "No json by the name of " + name + " to clear";
        }
    }

    public static String jsonFunctions(String TMP_e) {
        while (TMP_e.contains("{json[") && TMP_e.contains("]}.clear()")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{json[")+6, TMP_e.indexOf("]}.clear()", TMP_e.indexOf("{json[")));
            while (get_name.contains("{json[")) {
                get_name = get_name.substring(get_name.indexOf("{json[")+6);
            }

            List<String> temporary = new ArrayList<String>();
            temporary.add("JsonToString->"+get_name+"CLEAR-"+(global.TMP_string.size()+1));
            temporary.add(clearJson(get_name));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{json["+get_name+"]}.clear()","{string[JsonToString->"+get_name+"CLEAR-"+global.TMP_string.size()+"]}");
        }

        while (TMP_e.contains("{json[") && TMP_e.contains("]}.load(") && TMP_e.contains(")")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{json[")+6, TMP_e.indexOf("]}.load(", TMP_e.indexOf("{json[")));
            String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.load(", TMP_e.indexOf("{json["))+8, TMP_e.indexOf(")", TMP_e.indexOf("]}.load(", TMP_e.indexOf("{json["))));
            String temp_search = TMP_e.substring(TMP_e.indexOf("]}.load(", TMP_e.indexOf("{json["))+8);
            while (get_name.contains("{json[")) {
                get_name = get_name.substring(get_name.indexOf("{json[")+6);
            }
            while (get_prevalue.contains("(")) {
                temp_search = temp_search.replaceFirst("\\(","tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)","tempCloseBreacketF6cyUQp9tempCloseBracket");
                get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
            }
            get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket","(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket",")");
            String get_value = StringHandler.stringFunctions(get_prevalue, null);

            List<String> temporary = new ArrayList<String>();
            temporary.add("JsonToString->"+get_name+"LOAD"+get_value+"-"+(global.TMP_string.size()+1));
            temporary.add(getJson(get_name, get_value));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{json["+get_name+"]}.load("+get_prevalue+")","{string[JsonToString->"+get_name+"LOAD"+get_value+"-"+global.TMP_string.size()+"]}");
        }

        while (TMP_e.contains("{json[") && TMP_e.contains("]}.get(") && TMP_e.contains(")")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{json[")+6, TMP_e.indexOf("]}.get(", TMP_e.indexOf("{json[")));
            while (get_name.contains("{json[")) {
                get_name = get_name.substring(get_name.indexOf("{json[")+6);
            }
            String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.get(", TMP_e.indexOf("{json["))+7, TMP_e.indexOf(")", TMP_e.indexOf("]}.get(", TMP_e.indexOf("{json[")+6)+7));
            //System.out.println(get_prevalue);
            String temp_search = TMP_e.substring(TMP_e.indexOf("]}.get(", TMP_e.indexOf("{json["))+7);
            while (get_prevalue.contains("(")) {
                System.out.println(get_name + "->" + get_prevalue);
                System.out.println("SEARCH STRING->"+temp_search);
                System.out.println("TMP_E->"+TMP_e);
                temp_search = temp_search.replaceFirst("\\(","tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)","tempCloseBreacketF6cyUQp9tempCloseBracket");
                get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
            }
            get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket","(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket",")");
            String get_value = StringHandler.stringFunctions(get_prevalue, null);

            List<String> temporary = new ArrayList<String>();
            temporary.add("JsonToString->"+get_name+"GET"+get_value+"-"+(global.TMP_string.size()+1));
            temporary.add(getValue(get_name, get_value));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{json["+get_name+"]}.get("+get_prevalue+")","{string[JsonToString->"+get_name+"GET"+get_value+"-"+global.TMP_string.size()+"]}");
        }

        while (TMP_e.contains("{json[") && TMP_e.contains("]}.getKeys(") && TMP_e.contains(")")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{json[")+6, TMP_e.indexOf("]}.getKeys(", TMP_e.indexOf("{json[")));
            String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.getKeys(", TMP_e.indexOf("{json["))+11, TMP_e.indexOf(")", TMP_e.indexOf("]}.getKeys(", TMP_e.indexOf("{json["))));
            String temp_search = TMP_e.substring(TMP_e.indexOf("]}.getKeys(", TMP_e.indexOf("{json["))+11);
            while (get_name.contains("{json[")) {
                get_name = get_name.substring(get_name.indexOf("{json[")+6);
            }
            while (get_prevalue.contains("(")) {
                temp_search = temp_search.replaceFirst("\\(","tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)","tempCloseBreacketF6cyUQp9tempCloseBracket");
                get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
            }
            get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket","(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket",")");
            String get_value = StringHandler.stringFunctions(get_prevalue, null);

            ListHandler.getList("JsonToList->"+get_name+"GETKEYS"+get_value+"-"+(global.lists.size()+1), getKeys(get_name, get_value));

            TMP_e = TMP_e.replace("{json["+get_name+"]}.getKeys("+get_prevalue+")","{list[JsonToList->"+get_name+"GETKEYS"+get_value+"-"+global.lists.size()+"]}");
        }

        while (TMP_e.contains("{json[") && TMP_e.contains("]}.getValues(") && TMP_e.contains(")")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{json[")+6, TMP_e.indexOf("]}.getValues(", TMP_e.indexOf("{json[")));
            String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.getValues(", TMP_e.indexOf("{json["))+13, TMP_e.indexOf(")", TMP_e.indexOf("]}.getValues(", TMP_e.indexOf("{json["))));
            String temp_search = TMP_e.substring(TMP_e.indexOf("]}.getValues(", TMP_e.indexOf("{json["))+13);
            while (get_name.contains("{json[")) {
                get_name = get_name.substring(get_name.indexOf("{json[")+6);
            }
            while (get_prevalue.contains("(")) {
                temp_search = temp_search.replaceFirst("\\(","tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)","tempCloseBreacketF6cyUQp9tempCloseBracket");
                get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
            }
            get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket","(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket",")");
            String get_value = StringHandler.stringFunctions(get_prevalue, null);

            List<String> temporary = new ArrayList<String>();
            temporary.add("JsonToString->"+get_name+"GETVALUES"+get_value+"-"+(global.TMP_string.size()+1));
            temporary.add(getValues(get_name, get_value));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{json["+get_name+"]}.getValues("+get_prevalue+")","{string[JsonToString->"+get_name+"GETVALUES"+get_value+"-"+global.TMP_string.size()+"]}");
        }




        while (TMP_e.contains("{json[") && TMP_e.contains("]}")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{json[")+6, TMP_e.indexOf("]}", TMP_e.indexOf("{json[")));
            while (get_name.contains("{json[")) {
                get_name = get_name.substring(get_name.indexOf("{json[")+6);
            }

            List<String> temporary = new ArrayList<String>();
            temporary.add("JsonToString->"+get_name+"GETVALUE-"+(global.TMP_string.size()+1));
            temporary.add(getValue(get_name));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{json["+get_name+"]}", "{string[JsonToString->"+get_name+"GETVALUE-"+global.TMP_string.size()+"]}");
        }

        return TMP_e;
    }
}
