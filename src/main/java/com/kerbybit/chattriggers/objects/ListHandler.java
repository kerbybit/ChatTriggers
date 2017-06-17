package com.kerbybit.chattriggers.objects;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.triggers.EventsHandler;
import com.kerbybit.chattriggers.triggers.StringFunctions;

import java.io.*;
import java.net.URL;
import java.util.*;

public class ListHandler {
    private static HashMap<String, List<String>> lists = new HashMap<>();

    private static ArrayList<String> getListFromURL(String url) {
        try {
            StringBuilder listString = new StringBuilder();
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(url).openStream(),"UTF-8"));
            while ((line = bufferedReader.readLine()) != null) {
                listString.append(line);
            }
            bufferedReader.close();

            return getListFromValue(listString.toString());
        } catch (Exception e2) {
            return null;
        }
    }

    private static ArrayList<String> getListFromFile(String dest) {
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
            return getListFromValue(listString.toString());
        } catch (Exception e1) {
            return null;
        }
    }

    private static void saveListToFile(String list_name, String dest) {
        Boolean compact = false;

        if (!dest.contains("/")) {
            dest = "./mods/ChatTriggers/"+dest;
        }

        if (dest.contains("<compact>")) {
            dest = dest.replace("<compact>", "");
            compact = true;
        }

        try {
            PrintWriter writer = new PrintWriter(dest, "UTF-8");
            if (compact) {
                StringBuilder write = new StringBuilder("[");
                if (lists.containsKey(list_name)) {
                    for (int i = 0; i < lists.get(list_name).size()-1; i++) {
                        write.append(lists.get(list_name).get(i)).append(",");
                    }
                    write.append(lists.get(list_name).get(lists.get(list_name).size()-1));
                }
                write.append("]");
                writer.println(write);
                writer.close();
            } else {
                writer.println("[");
                if (lists.containsKey(list_name)) {
                    for (int i = 0; i < lists.get(list_name).size()-1; i++) {
                        writer.println("  " + lists.get(list_name).get(i) + ",");
                    }
                    writer.println("  " + lists.get(list_name).get(lists.get(list_name).size()-1));
                }
                writer.println("]");
                writer.close();
            }
        } catch (FileNotFoundException exception) {
            File check = new File(dest.substring(0, dest.lastIndexOf("/")));
            if (!check.mkdir())
                ChatHandler.warn("red", "Unable to save list to file!");
            else {
                if (compact) saveListToFile(list_name, "<compact>"+dest);
                else saveListToFile(list_name, dest);
            }
        } catch (IOException exception) {
            ChatHandler.warn("red", "Unable to save list to file! IOException");
            exception.printStackTrace();
        }
    }

    private static ArrayList<String> getListFromValue(String value) {
        if (value.startsWith("[") && value.endsWith("]")) {
            if (value.equals("[]")) {
                return null;
            } else {
                value = value.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ",");
                String[] list = value.substring(1, value.length()-1).split(",");
                return new ArrayList<>(Arrays.asList(list));
            }
        } else {
            return null;
        }
    }

    public static void getList(String list_name, String list_from) {
        ArrayList<String> list_object;
        if (list_from.toUpperCase().startsWith("HTTP")) {
            list_object = getListFromURL(list_from);
        } else if (list_from.contains("[")) {
            list_object = getListFromValue(list_from);
        } else {
            list_object = getListFromFile(list_from);
        }

        if (list_object != null) {
            clearList(list_name);
            lists.put(list_name, list_object);
        }
    }

    private static String getList(String list_name) {
        if (lists.containsKey(list_name)) {
            StringBuilder return_string = new StringBuilder("[");
            for (String value : lists.get(list_name)) {
                return_string.append(value).append(",");
            }
            return_string = new StringBuilder(return_string.substring(0, return_string.length()-1) + "]");
            return return_string.toString();
        } else {
            return "[]";
        }
    }

    private static ArrayList<String> getListObject(String list_name) {
        if (lists.containsKey(list_name)) {
            return new ArrayList<>(lists.get(list_name));
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
                lists.put(list_name, list_object);
                return getList(list_name);
            } else if (value.contains(",")) {
                String position_string = value.substring(0, value.indexOf(","));
                String new_value = value.substring(value.indexOf(","));

                try {
                    list_object.add(Integer.parseInt(position_string), new_value);
                    lists.put(list_name, list_object);
                    return getList(list_name);
                } catch(NumberFormatException e) {
                    list_object.add(value);
                    lists.put(list_name, list_object);
                    return getList(list_name);
                }
            } else {
                list_object.add(value);
                lists.put(list_name, list_object);
                return getList(list_name);
            }
        } else {
            lists.put(list_name, Collections.singletonList(value));
            return getList(list_name);
        }
    }

    private static String setInList(String list_name, String value) {
        List<String> list_object = getListObject(list_name);

        if (list_object != null) {

            if (value.contains(",")) {
                String position_string = value.substring(0, value.indexOf(","));
                String new_value = value.substring(value.indexOf(",")+1);

                try {
                    list_object.set(Integer.parseInt(position_string), new_value);
                    lists.put(list_name, list_object);
                    return getList(list_name);
                } catch(NumberFormatException e) {
                    return getList(list_name);
                }
            } else {
                list_object.set(list_object.size()-1, value);
                lists.put(list_name, list_object);
                return getList(list_name);
            }
        } else {
            lists.put(list_name, Collections.singletonList(value));
            return getList(list_name);
        }
    }

    private static String getValue(String list_name, int position) throws NumberFormatException {
        List<String> entries = lists.get(list_name);
        if (position < entries.size() && position >= 0) {
            return entries.get(position);
        } else {
            throw new NumberFormatException();
        }
    }

    private static String getHasValue(String list_name, String value) {
        return getHasValue(list_name, value, false);
    }
    private static String getHasValue(String list_name, String value, Boolean ignoreCase) {
        if (lists.containsKey(list_name)) {
            if (ignoreCase) {
                for (String element : lists.get(list_name)) {
                    if (element.equalsIgnoreCase(value)) {
                        return "true";
                    }
                }
            } else {
                if (lists.get(list_name).contains(value)) {
                    return "true";
                }
            }
        }
        return "false";
    }

    private static String getValue(String list_name, String value) {
        if (lists.containsKey(list_name)) {
            try {
                int position = Integer.parseInt(value);
                return getValue(list_name, position);
            } catch (NumberFormatException e) {
                int position = -1;
                int i = 0;
                for (String entry : lists.get(list_name)) {
                    if (value.equals(entry)) {
                        position = i;
                        break;
                    }
                    i++;
                }
                if (position == -1) {
                    return "Index out of bounds";
                } else {
                    return position+"";
                }
            }
        } else {
            return "Empty list";
        }
    }

    private static String getRandomValue(String list_name) {
        if (lists.containsKey(list_name)) {
            List<String> list = lists.get(list_name);
            int randInt = EventsHandler.randInt(0, list.size()-1);
            return list.get(randInt);
        } else {
            return "Empty list";
        }
    }

    private static String getSize(String list_name) {
        if (lists.containsKey(list_name)) {
            return lists.get(list_name).size()+"";
        } else {
            return "0";
        }
    }

    private static String removeValue(String list_name, int position) throws NumberFormatException {
        List<String> entries = lists.get(list_name);
        if (position < entries.size() && position >= 0) {
            String removed = entries.remove(position);
            if (entries.size() == 0) {
                lists.remove(list_name);
            } else {
                lists.put(list_name, entries);
            }
            return removed;
        } else {
            throw new NumberFormatException();
        }
    }

    private static String removeValue(String list_name, String value) {
        if (lists.containsKey(list_name)) {
            try {
                int position = Integer.parseInt(value);
                return removeValue(list_name, position);
            } catch (NumberFormatException e) {
                int position = -1;
                int i = 0;
                List<String> entries = lists.get(list_name);
                for (String entry : entries) {
                    if (value.equals(entry)) {
                        position = i;
                        break;
                    }
                    i++;
                }
                if (position == -1) {
                    return "Index out of bounds";
                } else {
                    if (entries.size() == 1) {
                        lists.remove(list_name);
                    } else {
                        entries.remove(position);
                        lists.put(list_name, entries);
                    }
                    return position+"";
                }
            }
        } else {
            return "Empty list";
        }
    }

    private static void sortList(String list_name) {
        if (lists.containsKey(list_name)) {
            List<String> list = lists.get(list_name);
            Collections.sort(list);
        }
    }

    private static void reverseList(String list_name) {
        if (lists.containsKey(list_name)) {
            List<String> list = lists.get(list_name);
            Collections.reverse(list);
        }
    }

    private static String clearList(String list_name) {
        if (lists.containsKey(list_name)) {
            String return_string = getList(list_name);
            lists.remove(list_name);
            return return_string;
        } else {
            return "Empty list";
        }
    }

    public static String listFunctions(String TMP_e, Boolean isAsync) {
        while (TMP_e.contains("{list[") && TMP_e.contains("]}.load(") && TMP_e.contains(")")) {
            int second = TMP_e.indexOf("]}.load(", TMP_e.indexOf("{list["));
            if (second > -1) {
                String get_name = TMP_e.substring(TMP_e.indexOf("{list[") + 6, second);
                String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.load(", TMP_e.indexOf("{list[")) + 8, TMP_e.indexOf(")", TMP_e.indexOf("]}.load(", TMP_e.indexOf("{list["))));
                while (get_name.contains("{list[")) {
                    get_name = get_name.substring(get_name.indexOf("{list[") + 6);
                }
                String temp_search = TMP_e.substring(TMP_e.indexOf("]}.load(", TMP_e.indexOf("{list[")) + 8);
                while (get_prevalue.contains("(")) {
                    temp_search = temp_search.replaceFirst("\\(", "tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)", "tempCloseBreacketF6cyUQp9tempCloseBracket");
                    get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
                }
                get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket", "(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket", ")");
                String get_value = StringFunctions.nestedArgs(get_prevalue, null, isAsync);

                getList(get_name, get_value);

                TMP_e = TMP_e.replace("{list[" + get_name + "]}.load(" + get_prevalue + ")", "{list[" + get_name + "]}");
            } else break;
        }

        while (TMP_e.contains("{list[") && TMP_e.contains("]}.export(") && TMP_e.contains(")")) {
            int second = TMP_e.indexOf("]}.export(", TMP_e.indexOf("{list["));
            if (second > -1) {
                String get_name = TMP_e.substring(TMP_e.indexOf("{list[") + 6, second);
                String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.export(", TMP_e.indexOf("{list[")) + 10, TMP_e.indexOf(")", TMP_e.indexOf("]}.export(", TMP_e.indexOf("{list["))));
                while (get_name.contains("{list[")) {
                    get_name = get_name.substring(get_name.indexOf("{list[") + 6);
                }
                String temp_search = TMP_e.substring(TMP_e.indexOf("]}.export(", TMP_e.indexOf("{list[")) + 10);
                while (get_prevalue.contains("(")) {
                    temp_search = temp_search.replaceFirst("\\(", "tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)", "tempCloseBreacketF6cyUQp9tempCloseBracket");
                    get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
                }
                get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket", "(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket", ")");
                String get_value = StringFunctions.nestedArgs(get_prevalue, null, isAsync);

                saveListToFile(get_name, get_value);

                TMP_e = TMP_e.replace("{list[" + get_name + "]}.export(" + get_prevalue + ")", "{list[" + get_name + "]}");
            } else break;
        }

        while(TMP_e.contains("{list[") && TMP_e.contains("]}.size()")) {
            int second = TMP_e.indexOf("]}.size()", TMP_e.indexOf("{list["));
            if (second > -1) {
                String get_name = TMP_e.substring(TMP_e.indexOf("{list[") + 6, second);
                while (get_name.contains("{list[")) {
                    get_name = get_name.substring(get_name.indexOf("{list[") + 6);
                }

                TMP_e = createDefaultString("size", get_name, getSize(get_name), TMP_e, isAsync);
            } else break;
        }

        while(TMP_e.contains("{list[") && TMP_e.contains("]}.sort()")) {
            int second = TMP_e.indexOf("]}.sort()", TMP_e.indexOf("{list["));
            if (second > -1) {
                String get_name = TMP_e.substring(TMP_e.indexOf("{list[") + 6, second);
                while (get_name.contains("{list[")) {
                    get_name = get_name.substring(get_name.indexOf("{list[") + 6);
                }

                sortList(get_name);

                TMP_e = TMP_e.replace("{list[" + get_name + "]}.sort()", "{list[" + get_name + "]}");
            } else break;
        }
        while(TMP_e.contains("{list[") && TMP_e.contains("]}.reverse()")) {
            int second = TMP_e.indexOf("]}.sort()", TMP_e.indexOf("{list["));
            if (second > -1) {
                String get_name = TMP_e.substring(TMP_e.indexOf("{list[") + 6, second);
                while (get_name.contains("{list[")) {
                    get_name = get_name.substring(get_name.indexOf("{list[") + 6);
                }

                reverseList(get_name);

                TMP_e = TMP_e.replace("{list[" + get_name + "]}.sort()", "{list[" + get_name + "]}");
            } else break;
        }

        while (TMP_e.contains("{list[") && TMP_e.contains("]}.add(") && TMP_e.contains(")")) {
            int second = TMP_e.indexOf("]}.add(", TMP_e.indexOf("{list["));
            if (second > -1) {
                String get_name = TMP_e.substring(TMP_e.indexOf("{list[") + 6, second);
                String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.add(", TMP_e.indexOf("{list[")) + 7, TMP_e.indexOf(")", TMP_e.indexOf("]}.add(", TMP_e.indexOf("{list["))));
                while (get_name.contains("{list[")) {
                    get_name = get_name.substring(get_name.indexOf("{list[") + 6);
                }
                String temp_search = TMP_e.substring(TMP_e.indexOf("]}.add(", TMP_e.indexOf("{list[")) + 7);
                while (get_prevalue.contains("(")) {
                    temp_search = temp_search.replaceFirst("\\(", "tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)", "tempCloseBreacketF6cyUQp9tempCloseBracket");
                    get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
                }
                get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket", "(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket", ")");
                String get_value = StringFunctions.nestedArgs(get_prevalue, null, isAsync);

                TMP_e = createDefaultString("add", get_name, get_prevalue, addToList(get_name, get_value), TMP_e, isAsync);
            } else break;
        }

        while (TMP_e.contains("{list[") && TMP_e.contains("]}.set(") && TMP_e.contains(")")) {
            int second = TMP_e.indexOf("]}.set(", TMP_e.indexOf("{list["));
            if (second > -1) {
                String get_name = TMP_e.substring(TMP_e.indexOf("{list[") + 6, second);
                String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.set(", TMP_e.indexOf("{list[")) + 7, TMP_e.indexOf(")", TMP_e.indexOf("]}.set(", TMP_e.indexOf("{list["))));
                while (get_name.contains("{list[")) {
                    get_name = get_name.substring(get_name.indexOf("{list[") + 6);
                }
                String temp_search = TMP_e.substring(TMP_e.indexOf("]}.set(", TMP_e.indexOf("{list[")) + 7);
                while (get_prevalue.contains("(")) {
                    temp_search = temp_search.replaceFirst("\\(", "tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)", "tempCloseBreacketF6cyUQp9tempCloseBracket");
                    get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
                }
                get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket", "(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket", ")");
                String get_value = StringFunctions.nestedArgs(get_prevalue, null, isAsync);

                TMP_e = createDefaultString("set", get_name, get_prevalue, setInList(get_name, get_value), TMP_e, isAsync);
            } else break;
        }

        while (TMP_e.contains("{list[") && TMP_e.contains("]}.get(") && TMP_e.contains(")")) {
            int second = TMP_e.indexOf("]}.get(", TMP_e.indexOf("{list["));
            if (second > -1) {
                String get_name = TMP_e.substring(TMP_e.indexOf("{list[") + 6, second);
                String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.get(", TMP_e.indexOf("{list[")) + 7, TMP_e.indexOf(")", TMP_e.indexOf("]}.get(", TMP_e.indexOf("{list["))));
                while (get_name.contains("{list[")) {
                    get_name = get_name.substring(get_name.indexOf("{list[") + 6);
                }
                String temp_search = TMP_e.substring(TMP_e.indexOf("]}.get(", TMP_e.indexOf("{list[")) + 7);
                while (get_prevalue.contains("(")) {
                    temp_search = temp_search.replaceFirst("\\(", "tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)", "tempCloseBreacketF6cyUQp9tempCloseBracket");
                    get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
                }
                get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket", "(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket", ")");
                String get_value = StringFunctions.nestedArgs(get_prevalue, null, isAsync);

                TMP_e = createDefaultString("get", get_name, get_prevalue, getValue(get_name, get_value), TMP_e, isAsync);
            } else break;
        }

        while (TMP_e.contains("{list[") && TMP_e.contains("]}.has(") && TMP_e.contains(")")) {
            int second = TMP_e.indexOf("]}.has(", TMP_e.indexOf("{list["));
            if (second > -1) {
                String get_name = TMP_e.substring(TMP_e.indexOf("{list[") + 6, second);
                String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.has(", TMP_e.indexOf("{list[")) + 7, TMP_e.indexOf(")", TMP_e.indexOf("]}.has(", TMP_e.indexOf("{list["))));
                while (get_name.contains("{list[")) {
                    get_name = get_name.substring(get_name.indexOf("{list[") + 6);
                }
                String temp_search = TMP_e.substring(TMP_e.indexOf("]}.has(", TMP_e.indexOf("{list[")) + 7);
                while (get_prevalue.contains("(")) {
                    temp_search = temp_search.replaceFirst("\\(", "tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)", "tempCloseBreacketF6cyUQp9tempCloseBracket");
                    get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
                }
                get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket", "(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket", ")");
                String get_value = StringFunctions.nestedArgs(get_prevalue, null, isAsync);

                TMP_e = createDefaultString("has", get_name, get_prevalue, getHasValue(get_name, get_value), TMP_e, isAsync);
            } else break;
        }
        while (TMP_e.contains("{list[") && TMP_e.contains("]}.hasIgnoreCase(") && TMP_e.contains(")")) {
            int second = TMP_e.indexOf("]}.hasIgnoreCase(", TMP_e.indexOf("{list["));
            if (second > -1) {
                String get_name = TMP_e.substring(TMP_e.indexOf("{list[") + 6, second);
                String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.hasIgnoreCase(", TMP_e.indexOf("{list[")) + 17, TMP_e.indexOf(")", TMP_e.indexOf("]}.hasIgnoreCase(", TMP_e.indexOf("{list["))));
                while (get_name.contains("{list[")) {
                    get_name = get_name.substring(get_name.indexOf("{list[") + 6);
                }
                String temp_search = TMP_e.substring(TMP_e.indexOf("]}.hasIgnoreCase(", TMP_e.indexOf("{list[")) + 17);
                while (get_prevalue.contains("(")) {
                    temp_search = temp_search.replaceFirst("\\(", "tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)", "tempCloseBreacketF6cyUQp9tempCloseBracket");
                    get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
                }
                get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket", "(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket", ")");
                String get_value = StringFunctions.nestedArgs(get_prevalue, null, isAsync);

                TMP_e = createDefaultString("hasIgnoreCase", get_name, get_prevalue, getHasValue(get_name, get_value, true), TMP_e, isAsync);
            } else break;
        }

        while (TMP_e.contains("{list[") && TMP_e.contains("]}.getRandom()")) {
            int second = TMP_e.indexOf("]}.getRandom()", TMP_e.indexOf("{list["));
            if (second > -1) {
                String get_name = TMP_e.substring(TMP_e.indexOf("{list[") + 6, second);
                while (get_name.contains("{list[")) {
                    get_name = get_name.substring(get_name.indexOf("{list[") + 6);
                }

                TMP_e = createDefaultString("getRandom", get_name, getRandomValue(get_name), TMP_e, isAsync);
            } else break;
        }

        while (TMP_e.contains("{list[") && TMP_e.contains("]}.remove(") && TMP_e.contains(")")) {
            int second = TMP_e.indexOf("]}.remove(", TMP_e.indexOf("{list["));
            if (second > -1) {
                String get_name = TMP_e.substring(TMP_e.indexOf("{list[") + 6, second);
                String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.remove(", TMP_e.indexOf("{list[")) + 10, TMP_e.indexOf(")", TMP_e.indexOf("]}.remove(", TMP_e.indexOf("{list["))));
                while (get_name.contains("{list[")) {
                    get_name = get_name.substring(get_name.indexOf("{list[") + 6);
                }
                String temp_search = TMP_e.substring(TMP_e.indexOf("]}.remove(", TMP_e.indexOf("{list[")) + 10);
                while (get_prevalue.contains("(")) {
                    temp_search = temp_search.replaceFirst("\\(", "tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)", "tempCloseBreacketF6cyUQp9tempCloseBracket");
                    get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
                }
                get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket", "(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket", ")");
                String get_value = StringFunctions.nestedArgs(get_prevalue, null, isAsync);

                TMP_e = createDefaultString("remove", get_name, get_prevalue, removeValue(get_name, get_value), TMP_e, isAsync);
            } else break;
        }

        while (TMP_e.contains("{list[") && TMP_e.contains("]}.clear()")) {
            int second = TMP_e.indexOf("]}.clear()", TMP_e.indexOf("{list["));
            if (second > -1) {
                String get_name = TMP_e.substring(TMP_e.indexOf("{list[") + 6, second);
                while (get_name.contains("{list[")) {
                    get_name = get_name.substring(get_name.indexOf("{list[") + 6);
                }

                TMP_e = createDefaultString("clear", get_name, clearList(get_name), TMP_e, isAsync);
            } else break;
        }


        while (TMP_e.contains("{list[") && TMP_e.contains("]}")) {
            int second = TMP_e.indexOf("]}", TMP_e.indexOf("{list["));
            if (second > -1) {
                String get_name = TMP_e.substring(TMP_e.indexOf("{list[") + 6, second);
                while (get_name.contains("{list[")) {
                    get_name = get_name.substring(get_name.indexOf("{list[") + 6);
                }

                TMP_e = createDefaultString(get_name, TMP_e, isAsync);
            } else break;
        }

        return TMP_e;
    }

    private static String createDefaultString(String function_name, String list_name, String arguments, String value, String TMP_e, Boolean isAsync) {
        if (isAsync) {
            global.Async_string.put("AsyncListToString->" + list_name + function_name.toUpperCase() + "-" + (global.Async_string.size() + 1), value);
            global.backupAsync_string.put("AsyncListToString->" + list_name + function_name.toUpperCase() + "-" + global.Async_string.size(), value);
            return TMP_e.replace("{list["+list_name+"]}."+function_name+"("+arguments+")", "{string[AsyncListToString->"+list_name+function_name.toUpperCase()+"-"+global.Async_string.size()+"]}");
        } else {
            global.TMP_string.put("ListToString->" + list_name + function_name.toUpperCase() + "-" + (global.TMP_string.size() + 1), value);
            global.backupTMP_strings.put("ListToString->" + list_name + function_name.toUpperCase() + "-" + global.TMP_string.size(), value);
            return TMP_e.replace("{list["+list_name+"]}."+function_name+"("+arguments+")", "{string[ListToString->"+list_name+function_name.toUpperCase()+"-"+global.TMP_string.size()+"]}");
        }
    }

    private static String createDefaultString(String function_name, String list_name, String value, String TMP_e, Boolean isAsync) {
        return createDefaultString(function_name, list_name, "", value, TMP_e, isAsync);
    }

    private static String createDefaultString(String list_name, String TMP_e, Boolean isAsync) {
        if (isAsync) {
            global.Async_string.put("AsyncListToString->" + list_name + "LITERAL" + "-" + (global.Async_string.size() + 1), getList(list_name));
            global.backupAsync_string.put("AsyncListToString->" + list_name + "LITERAL" + "-" + global.Async_string.size(), getList(list_name));
            return TMP_e.replace("{list[" + list_name + "]}", "{string[AsyncListToString->" + list_name + "LITERAL" + "-" + global.Async_string.size() + "]}");
        } else {
            global.TMP_string.put("ListToString->" + list_name + "LITERAL" + "-" + (global.TMP_string.size() + 1), getList(list_name));
            global.backupTMP_strings.put("ListToString->" + list_name + "LITERAL" + "-" + global.TMP_string.size(), getList(list_name));
            return TMP_e.replace("{list[" + list_name + "]}", "{string[ListToString->" + list_name + "LITERAL" + "-" + global.TMP_string.size() + "]}");
        }
    }

    public static int getListsSize() {
        return lists.size();
    }

    public static void trimLists() {
        HashMap<String, List<String>> lists_copy = new HashMap<>(lists);

        for (String key : lists_copy.keySet()) {
            if (key.startsWith("JsonToList->") || key.startsWith("StringToList->")) {
               lists.remove(key);
            }
        }
    }

    public static void dumpLists() {
        if (lists.size() > 0) {
            for (Map.Entry<String, List<String>> entry : lists.entrySet()) {
                ChatHandler.warn(entry.getKey());
                ChatHandler.warn("  " + entry.getValue());
            }
        } else ChatHandler.warn("red", "There are currently no lists.");
    }
}
