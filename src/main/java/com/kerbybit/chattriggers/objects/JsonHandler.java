package com.kerbybit.chattriggers.objects;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.triggers.StringFunctions;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonHandler {
    private static HashMap<String, JsonObject> jsons = new HashMap<>();

    public static String getForJson(String in) {
        return in.replace("\"", "\\\"")
                .replace("'", "\\'");
    }

    private static JsonObject getJsonFromURL(String url) {
        try {
            StringBuilder jsonString = new StringBuilder();
            String line;
            URLConnection conn = new URL(url).openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            while ((line = bufferedReader.readLine()) != null) {
                jsonString.append(line);
            }
            bufferedReader.close();

            System.out.println(jsonString.toString());
            return new JsonParser().parse(jsonString.toString()).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
            ChatHandler.warn("Unable to load Json value! IOException");
            return new JsonParser().parse("{}").getAsJsonObject();
        }
    }

    private static JsonObject getJsonFromFile(String dest) {
        try {
            if (!dest.contains("/")) {
                dest = "./mods/ChatTriggers/"+dest;
            }

            StringBuilder jsonString = new StringBuilder();
            String line;
            BufferedReader bufferedReader;
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(dest),"UTF-8"));
            while ((line = bufferedReader.readLine()) != null) {
                jsonString.append(line);
            }
            return new JsonParser().parse(jsonString.toString()).getAsJsonObject();
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
            jsons.put(json_name, json_object);
            return json_object + "";
        }
    }

    private static void saveJsonToFile(String json_name, String dest) {
        dest = dest.replace("./mods/ChatTriggers/", "./");
        dest = dest.replace("./", "./mods/ChatTriggers/");

        if (!dest.contains("/")) {
            dest = "./mods/ChatTriggers/"+dest;
        }

        try {
            PrintWriter writer = new PrintWriter(dest, "UTF-8");
            if (jsons.containsKey(json_name)) {
                writer.println(jsons.get(json_name).toString());
                writer.close();
            }
        } catch (FileNotFoundException exception) {
            dest = dest.replace("./mods/ChatTriggers/", "");
            String check_str = dest.substring(0, dest.lastIndexOf("/"));
            System.out.println(check_str);

            if (check_str.contains("/")) {
                StringBuilder folder = new StringBuilder("./mods/ChatTriggers/");
                for (String value : check_str.split("/")) {
                    folder = folder.append(value).append("/");
                    System.out.println(folder);
                    File check = new File(folder.toString());
                    if (!check.mkdir()) {
                        break;
                    }
                }
                if (new File(folder.toString()).exists()) {
                    saveJsonToFile(json_name, "./" + dest);
                }
            } else {
                File check = new File("./mods/ChatTriggers/" + check_str);
                if (!check.mkdir())
                    ChatHandler.warn("red", "Unable to save json to file!");
                else {
                    saveJsonToFile(json_name, "./" + dest);
                }
            }
        } catch (IOException exception) {
            ChatHandler.warn("red", "Unable to save json to file! IOException");
            exception.printStackTrace();
        }
    }

    private static void setValue(String json_name, String key, String value) {
        if (!jsons.containsKey(json_name)) {
            jsons.put(json_name, new JsonObject());
        }

        if (jsons.containsKey(json_name)) {
            try {
                JsonObject obj = jsons.get(json_name);
                String[] seg = key.split("\\.");
                int index = 1;

                for (String element : seg) {
                    if (obj != null) {
                        if (!obj.has(element)) {
                            if (index < seg.length) {
                                obj.add(element, new JsonObject());
                            } else {
                                obj.addProperty(element, value);
                            }
                        }

                        JsonElement ele = obj.get(element);
                        if (!ele.isJsonObject()) {
                            obj.addProperty(element, value);
                        } else {
                            obj = ele.getAsJsonObject();
                        }
                    }
                    index++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Not a json"));
        }
    }

    private static String getValue(String key, String value) {
        if (jsons.containsKey(key)) {
            try {
                JsonObject obj = jsons.get(key);
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
                //e.printStackTrace();
                return "null";
            }
        } else {
            return "Not a json";
        }
    }

    private static String getValue(String key) {
        if (jsons.containsKey(key)) {
            return jsons.get(key) + "";
        }
        return "Not a json";
    }

    private static JsonObject getObject(String key) {
        if (jsons.containsKey(key)) {
            return jsons.get(key);
        }
        return null;
    }

    private static String getKeys(String key, String value) {
        StringBuilder returnString = new StringBuilder("Not a json");
        JsonObject obj = null;
        if (value.equals("")) {
            obj = getObject(key);
        } else {
            returnString = new StringBuilder(getValue(key, value));
            if (!returnString.toString().equals("Not a json")) {
                obj = new JsonParser().parse(returnString.toString()).getAsJsonObject();
            }
        }
        List<String> keys = new ArrayList<>();
        if (obj != null) {
            for (Map.Entry<String, JsonElement> ele : obj.entrySet()) {
                keys.add(ele.getKey());
            }

            returnString = new StringBuilder("[");
            for (String element : keys) {
                returnString.append(element).append(",");
            }
            if (returnString.toString().equals("[")) {
                returnString.append("]");
            } else {
                returnString = new StringBuilder(returnString.substring(0, returnString.length() - 1) + "]");
            }
        }

        return returnString.toString();
    }

    private static String getValues(String key, String value) {
        StringBuilder returnString = new StringBuilder(getValue(key, value));
        JsonObject obj = new JsonParser().parse(returnString.toString()).getAsJsonObject();
        List<String> keys = new ArrayList<>();
        for (Map.Entry<String, JsonElement> eles : obj.entrySet()) {
            String ele = eles.getValue() + "";
            if (ele.startsWith("\"") && ele.endsWith("\"")) {
                ele = ele.substring(1, ele.length()-1);
            }
            keys.add(ele);
        }

        returnString = new StringBuilder("[");
        for (String element : keys) {
            returnString.append(element).append(",");
        }
        returnString = new StringBuilder(returnString.substring(0, returnString.length()-1) + "]");

        return returnString.toString();
    }

    private static String clearJson(String name) {
        if (jsons.containsKey(name)) {
            jsons.remove(name);
            return "Cleared json " + name;
        } else {
            return "No json by the name of " + name + " to clear";
        }
    }

    public static String jsonFunctions(String TMP_e, Boolean isAsync) {
        while (TMP_e.contains("{json[") && TMP_e.contains("]}.clear()")) {
            int second = TMP_e.indexOf("]}.clear()", TMP_e.indexOf("{json["));
            if (second > -1) {
                String get_name = TMP_e.substring(TMP_e.indexOf("{json[") + 6, second);
                while (get_name.contains("{json[")) {
                    get_name = get_name.substring(get_name.indexOf("{json[") + 6);
                }

                TMP_e = createDefaultString(get_name, clearJson(get_name), TMP_e, isAsync);
            } else break;
        }

        while (TMP_e.contains("{json[") && TMP_e.contains("]}.set(") && TMP_e.contains(")")) {
            int second = TMP_e.indexOf("]}.set(", TMP_e.indexOf("{json["));
            if (second > -1) {
                String get_name = TMP_e.substring(TMP_e.indexOf("{json[") + 6, second);
                String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.set(", TMP_e.indexOf("{json[")) + 7, TMP_e.indexOf(")", TMP_e.indexOf("]}.set(", TMP_e.indexOf("{json["))));
                String temp_search = TMP_e.substring(TMP_e.indexOf("]}.set(", TMP_e.indexOf("{json[")) + 7);
                while (get_name.contains("{json[")) {
                    get_name = get_name.substring(get_name.indexOf("{json[") + 6);
                }
                while (get_prevalue.contains("(")) {
                    temp_search = temp_search.replaceFirst("\\(", "tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)", "tempCloseBreacketF6cyUQp9tempCloseBracket");
                    get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
                }
                get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket", "(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket", ")");
                String get_value = StringFunctions.nestedArgs(get_prevalue, null, isAsync);

                String key = get_value.split(",")[0];
                String value = get_value.split(",")[1];
                setValue(get_name, key, value);

                TMP_e = TMP_e.replace("{json[" + get_name + "]}.set(" + get_prevalue + ")", "{json[" + get_name + "]}");
            } else break;
        }

        while (TMP_e.contains("{json[") && TMP_e.contains("]}.load(") && TMP_e.contains(")")) {
            int second = TMP_e.indexOf("]}.load(", TMP_e.indexOf("{json["));
            if (second > -1) {
                String get_name = TMP_e.substring(TMP_e.indexOf("{json[") + 6, second);
                String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.load(", TMP_e.indexOf("{json[")) + 8, TMP_e.indexOf(")", TMP_e.indexOf("]}.load(", TMP_e.indexOf("{json["))));
                String temp_search = TMP_e.substring(TMP_e.indexOf("]}.load(", TMP_e.indexOf("{json[")) + 8);
                while (get_name.contains("{json[")) {
                    get_name = get_name.substring(get_name.indexOf("{json[") + 6);
                }
                while (get_prevalue.contains("(")) {
                    temp_search = temp_search.replaceFirst("\\(", "tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)", "tempCloseBreacketF6cyUQp9tempCloseBracket");
                    get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
                }
                get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket", "(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket", ")");
                String get_value = StringFunctions.nestedArgs(get_prevalue, null, isAsync);

               get_value = get_value.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ",");

                TMP_e = createDefaultString("load", get_name, get_prevalue, getJson(get_name, get_value), TMP_e, isAsync);
            } else break;
        }

        while (TMP_e.contains("{json[") && TMP_e.contains("]}.export(") && TMP_e.contains(")")) {
            int second = TMP_e.indexOf("]}.export(", TMP_e.indexOf("{json["));
            if (second > -1) {
                String get_name = TMP_e.substring(TMP_e.indexOf("{json[") + 6, second);
                String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.export(", TMP_e.indexOf("{json[")) + 10, TMP_e.indexOf(")", TMP_e.indexOf("]}.export(", TMP_e.indexOf("{json["))));
                String temp_search = TMP_e.substring(TMP_e.indexOf("]}.export(", TMP_e.indexOf("{json[")) + 10);
                while (get_name.contains("{json[")) {
                    get_name = get_name.substring(get_name.indexOf("{json[") + 6);
                }
                while (get_prevalue.contains("(")) {
                    temp_search = temp_search.replaceFirst("\\(", "tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)", "tempCloseBreacketF6cyUQp9tempCloseBracket");
                    get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
                }
                get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket", "(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket", ")");
                String get_value = StringFunctions.nestedArgs(get_prevalue, null, isAsync);

                saveJsonToFile(get_name, get_value);

                TMP_e = TMP_e.replace("{json[" + get_name + "]}.export(" + get_prevalue + ")", "{json[" + get_name + "]}");
            } else break;
        }

        while (TMP_e.contains("{json[") && TMP_e.contains("]}.get(") && TMP_e.contains(")")) {
            int second = TMP_e.indexOf("]}.get(", TMP_e.indexOf("{json["));
            if (second > -1) {
                String get_name = TMP_e.substring(TMP_e.indexOf("{json[") + 6, second);
                while (get_name.contains("{json[")) {
                    get_name = get_name.substring(get_name.indexOf("{json[") + 6);
                }
                String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.get(", TMP_e.indexOf("{json[")) + 7, TMP_e.indexOf(")", TMP_e.indexOf("]}.get(", TMP_e.indexOf("{json[") + 6) + 7));
                String temp_search = TMP_e.substring(TMP_e.indexOf("]}.get(", TMP_e.indexOf("{json[")) + 7);
                while (get_prevalue.contains("(")) {
                    temp_search = temp_search.replaceFirst("\\(", "tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)", "tempCloseBreacketF6cyUQp9tempCloseBracket");
                    get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
                }
                get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket", "(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket", ")");
                String get_value = StringFunctions.nestedArgs(get_prevalue, null, isAsync);

                TMP_e = createDefaultString("get", get_name, get_prevalue, getValue(get_name, get_value), TMP_e, isAsync);
            } else break;
        }

        while (TMP_e.contains("{json[") && TMP_e.contains("]}.getKeys(") && TMP_e.contains(")")) {
            int second = TMP_e.indexOf("]}.getKeys(", TMP_e.indexOf("{json["));
            if (second > -1) {
                String get_name = TMP_e.substring(TMP_e.indexOf("{json[") + 6, second);
                String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.getKeys(", TMP_e.indexOf("{json[")) + 11, TMP_e.indexOf(")", TMP_e.indexOf("]}.getKeys(", TMP_e.indexOf("{json["))));
                String temp_search = TMP_e.substring(TMP_e.indexOf("]}.getKeys(", TMP_e.indexOf("{json[")) + 11);
                while (get_name.contains("{json[")) {
                    get_name = get_name.substring(get_name.indexOf("{json[") + 6);
                }
                while (get_prevalue.contains("(")) {
                    temp_search = temp_search.replaceFirst("\\(", "tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)", "tempCloseBreacketF6cyUQp9tempCloseBracket");
                    get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
                }
                get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket", "(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket", ")");
                String get_value = StringFunctions.nestedArgs(get_prevalue, null, isAsync);

                String list_name;
                if (isAsync)
                    list_name = "AsyncJsonToList->" + get_name + "GETKEYS" + get_value + "-" + (ListHandler.getListsSize() + 1);
                else
                    list_name = "JsonToList->" + get_name + "GETKEYS" + get_value + "-" + (ListHandler.getListsSize() + 1);
                ListHandler.getList(list_name, getKeys(get_name, get_value));

                TMP_e = TMP_e.replace("{json[" + get_name + "]}.getKeys(" + get_prevalue + ")", "{list[" + list_name + "]}");
            } else break;
        }

        while (TMP_e.contains("{json[") && TMP_e.contains("]}.getValues(") && TMP_e.contains(")")) {
            int second = TMP_e.indexOf("]}.getValues(", TMP_e.indexOf("{json["));
            if (second > -1) {
                String get_name = TMP_e.substring(TMP_e.indexOf("{json[") + 6, second);
                String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.getValues(", TMP_e.indexOf("{json[")) + 13, TMP_e.indexOf(")", TMP_e.indexOf("]}.getValues(", TMP_e.indexOf("{json["))));
                String temp_search = TMP_e.substring(TMP_e.indexOf("]}.getValues(", TMP_e.indexOf("{json[")) + 13);
                while (get_name.contains("{json[")) {
                    get_name = get_name.substring(get_name.indexOf("{json[") + 6);
                }
                while (get_prevalue.contains("(")) {
                    temp_search = temp_search.replaceFirst("\\(", "tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)", "tempCloseBreacketF6cyUQp9tempCloseBracket");
                    get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
                }
                get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket", "(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket", ")");
                String get_value = StringFunctions.nestedArgs(get_prevalue, null, isAsync);

                TMP_e = createDefaultString("getValues", get_name, get_prevalue, getValues(get_name, get_value), TMP_e, isAsync);
            } else break;
        }

        while (TMP_e.contains("{json[") && TMP_e.contains("]}")) {
            int second = TMP_e.indexOf("]}", TMP_e.indexOf("{json["));
            if (second > -1) {
                String get_name = TMP_e.substring(TMP_e.indexOf("{json[") + 6, second);
                while (get_name.contains("{json[")) {
                    get_name = get_name.substring(get_name.indexOf("{json[") + 6);
                }

                if (isAsync) {
                    global.Async_string.put("AsyncJsonToString->" + get_name + "-" + (global.Async_string.size() + 1), getValue(get_name));
                    global.backupAsync_string.put("AsyncJsonToString->" + get_name + "-" + global.Async_string.size(), getValue(get_name));
                    TMP_e = TMP_e.replace("{json[" + get_name + "]}", "{string[AsyncJsonToString->" + get_name + "-" + global.Async_string.size() + "]}");
                } else {
                    global.TMP_string.put("JsonToString->" + get_name + "-" + (global.TMP_string.size() + 1), getValue(get_name));
                    global.backupTMP_strings.put("JsonToString->" + get_name + "-" + global.TMP_string.size(), getValue(get_name));
                    TMP_e = TMP_e.replace("{json[" + get_name + "]}", "{string[JsonToString->" + get_name + "-" + global.TMP_string.size() + "]}");
                }
            } else break;
        }

        return TMP_e;
    }

    private static String createDefaultString(String function, String json_name, String arguments, String value, String TMP_e, Boolean isAsync) {
        if (isAsync) {
            global.Async_string.put("AsyncJsonToString->"+json_name+function.toUpperCase()+"-"+(global.Async_string.size()+1), value);
            global.backupAsync_string.put("AsyncJsonToString->"+json_name+function.toUpperCase()+"-"+global.Async_string.size(), value);
            return TMP_e.replace("{json["+json_name+"]}."+function+"("+arguments+")","{string[AsyncJsonToString->"+json_name+function.toUpperCase()+"-"+global.Async_string.size()+"]}");
        } else {
            global.TMP_string.put("JsonToString->"+json_name+function.toUpperCase()+"-"+(global.TMP_string.size()+1), value);
            global.backupTMP_strings.put("JsonToString->"+json_name+function.toUpperCase()+"-"+global.TMP_string.size(), value);
            return TMP_e.replace("{json["+json_name+"]}."+function+"("+arguments+")","{string[JsonToString->"+json_name+function.toUpperCase()+"-"+global.TMP_string.size()+"]}");
        }
    }

    private static String createDefaultString(String json_name, String value, String TMP_e, Boolean isAsync) {
        return createDefaultString("clear", json_name, "", value, TMP_e, isAsync);
    }

    public static int getJsonsSize() {
        return jsons.size();
    }

    public static void dumpJsons() {
        if (jsons.size() > 0) {
            for (Map.Entry<String, JsonObject> entry : jsons.entrySet()) {
                ChatHandler.warn(entry.getKey());
                ChatHandler.warn("  " + entry.getValue());
            }
        } else ChatHandler.warn("red", "There are currently no jsons.");
    }

    public static void trimJsons() {
        HashMap<String, JsonObject> jsons_copy = new HashMap<>(jsons);

        for (String key : jsons_copy.keySet()) {
            if (key.startsWith("DefaultJson")) {
                jsons.remove(key);
            }

            if (global.asyncMap.size() == 0) {
                if (key.startsWith("AsyncDefaultJson")) {
                    jsons.remove(key);
                }
            }
        }
    }
}
