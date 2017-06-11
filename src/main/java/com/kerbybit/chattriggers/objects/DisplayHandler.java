package com.kerbybit.chattriggers.objects;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.triggers.BuiltInStrings;
import com.kerbybit.chattriggers.triggers.StringFunctions;
import com.kerbybit.chattriggers.triggers.StringHandler;
import com.kerbybit.chattriggers.triggers.TagHandler;

import java.util.*;
import java.util.List;

public class DisplayHandler {
    private static HashMap<String,List<String>> displays = new HashMap<>();
    static HashMap<String,List<String>> shown_displays = new HashMap<>();
    private static HashMap<String,Double[]> displays_xy = new HashMap<>();
    private static HashMap<String,String> display_settings = new HashMap<>();

    private static String updateDisplay(String display_name, Boolean isAsync) {
        List<String> display;

        if (displays.containsKey(display_name)
                && displays_xy.containsKey(display_name)
                && shown_displays.containsKey(display_name)) {
            display = displays.get(display_name);

            List<String> display_return = new ArrayList<>();

            for (String value : display) {
                //setup backup for functions so strings don't get overwritten
                StringHandler.resetBackupStrings(isAsync);

                //built in strings
                value = BuiltInStrings.builtInStrings(value, null, isAsync);

                //user strings and functions
                value = value.replace("{string<", "{string[")
                        .replace("{array<", "{array[")
                        .replace("{display<", "{display[")
                        .replace("{json<", "{json[")
                        .replace("{list<", "{list[")
                        .replace(">}", "]}");

                value = JsonHandler.jsonFunctions(value, isAsync);
                value = StringHandler.stringFunctions(value, null, isAsync);
                value = ListHandler.listFunctions(value, isAsync);
                value = ArrayHandler.arrayFunctions(value, null, isAsync);
                value = StringHandler.stringFunctions(value, null, isAsync);

                display_return.add(TagHandler.removeTags(value));
            }

            shown_displays.put(display_name, display_return);
            return "Updated " + display_name + "'s values";
        } else {
            return "Display " + display_name + " has no values";
        }
    }

    private static String addToDisplay(String display_name, String value) {
        if (displays.containsKey(display_name)
                && displays_xy.containsKey(display_name)
                && shown_displays.containsKey(display_name)) {
            ArrayList<String> display = new ArrayList<>(displays.get(display_name));
            display.add(value);
            displays.put(display_name, display);
            return "Added " + value + " to " + display_name;
        } else {
            displays.put(display_name, Collections.singletonList(value));
            displays_xy.put(display_name, new Double[]{0.0,0.0,1.0});
            shown_displays.put(display_name, new ArrayList<>());
            return "Created and added " + value + " to " + display_name;
        }
    }

    private static String getDisplayX(String display_name) {
        if (displays_xy.containsKey(display_name)) {
            return displays_xy.get(display_name)[0] + "";
        } else {
            return "Display " + display_name + " has no x to get";
        }
    }

    private static String getDisplayY(String display_name) {
        if (displays_xy.containsKey(display_name)) {
            return displays_xy.get(display_name)[1] + "";
        } else {
            return "Display " + display_name + " has no y to get";
        }
    }

    private static String setDisplayX(String display_name, String value) {
        if (displays_xy.containsKey(display_name)) {
            try {
                Double x = Double.parseDouble(value);
                Double y = displays_xy.get(display_name)[1];
                Double a = displays_xy.get(display_name)[2];
                displays_xy.put(display_name, new Double[]{x, y, a});
                return value + "";
            } catch (NumberFormatException e) {
                return "ERR: setDisplayX -> " + value + " is not a valid number";
            }
        } else {
            return "Display " + display_name + " has no x to set";
        }
    }

    private static String setDisplayY(String display_name, String value) {
        if (displays_xy.containsKey(display_name)) {
            try {
                Double x = displays_xy.get(display_name)[0];
                Double y = Double.parseDouble(value);
                Double a = displays_xy.get(display_name)[2];
                displays_xy.put(display_name, new Double[]{x, y, a});
                return value + "";
            } catch (NumberFormatException e) {
                return "ERR: setDisplayY -> " + value + " is not a valid number";
            }
        } else {
            return "Display " + display_name + " has no y to set";
        }
    }

    private static String setDisplaySettings(String display_name, String settings) {
        display_settings.put(display_name, settings);
        return "Added settings " + settings + " to display " + display_name;
    }

    static Double[] getDisplayXY(String display_name) {
        if (DisplayHandler.displays_xy.containsKey(display_name)) {
            return DisplayHandler.displays_xy.get(display_name);
        }
        return new Double[]{0.0,0.0};
    }

    static String getDisplayBackground(String settings) {
        if (settings.contains("<bg=") && settings.contains(">")) {
            return settings.substring(settings.indexOf("<bg=")+4, settings.indexOf(">", settings.indexOf("<bg=")));
        }
        return "none";
    }

    static String getDisplayBackgroundColor(String settings) {
        if (settings.contains("<bgc=") && settings.contains(">")) {
            return settings.substring(settings.indexOf("<bgc=") + 5,
                    settings.indexOf(">", settings.indexOf("<bgc=")))
                    .replace("0x", "");
        } else {
            return "40000000";
        }
    }

    static String getDisplaySettings(String display_name) {
        if (display_settings.containsKey(display_name)) {
            StringBuilder return_string = new StringBuilder();
            for (String value : display_settings.get(display_name).split(",")) {
                return_string.append("<").append(value).append(">");
            }
            return return_string.toString();
        } else {
            return "";
        }
    }

    private static String deleteDisplay(String display_name) {
        displays.remove(display_name);
        displays_xy.remove(display_name);
        shown_displays.remove(display_name);
        display_settings.remove(display_name);
        return "Cleared " + display_name;
    }

    public static void clearDisplays() {
        displays.clear();
        displays_xy.clear();
        shown_displays.clear();
        display_settings.clear();
    }

    public static String displayFunctions(String TMP_e, Boolean isAsync) {
        while (TMP_e.contains("{display[") && TMP_e.contains("]}.update()")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{display[") + 9, TMP_e.indexOf("]}.update()", TMP_e.indexOf("{display[")));
            while (get_name.contains("{display[")) {
                get_name = get_name.substring(get_name.indexOf("{display[") + 9);
            }

            TMP_e = createDefaultString("update", get_name, updateDisplay(get_name, isAsync), TMP_e, isAsync);
        }

        while (TMP_e.contains("{display[") && TMP_e.contains("]}.clear()")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{display[") + 9, TMP_e.indexOf("]}.clear()", TMP_e.indexOf("{display[")));
            while (get_name.contains("{display[")) {
                get_name = get_name.substring(get_name.indexOf("{display[") + 9);
            }

            TMP_e = createDefaultString("clear", get_name, deleteDisplay(get_name), TMP_e, isAsync);
        }

        while (TMP_e.contains("{display[") && TMP_e.contains("]}.getX()")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{display[") + 9, TMP_e.indexOf("]}.getX()", TMP_e.indexOf("{display[")));
            while (get_name.contains("{display[")) {
                get_name = get_name.substring(get_name.indexOf("{display[") + 9);
            }

            TMP_e = createDefaultString("getX", get_name, getDisplayX(get_name), TMP_e, isAsync);
        }

        while (TMP_e.contains("{display[") && TMP_e.contains("]}.getY()")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{display[") + 9, TMP_e.indexOf("]}.getY()", TMP_e.indexOf("{display[")));
            while (get_name.contains("{display[")) {
                get_name = get_name.substring(get_name.indexOf("{display[") + 9);
            }

            TMP_e = createDefaultString("getY", get_name, getDisplayY(get_name), TMP_e, isAsync);
        }

        while (TMP_e.contains("{display[") && TMP_e.contains("]}.setX(") && TMP_e.contains(")")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{display[") + 9, TMP_e.indexOf("]}.setX(", TMP_e.indexOf("{display[")));
            String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.setX(", TMP_e.indexOf("{display[")) + 8, TMP_e.indexOf(")", TMP_e.indexOf("]}.setX(", TMP_e.indexOf("{display["))));
            while (get_name.contains("{display[")) {
                get_name = get_name.substring(get_name.indexOf("{display[") + 9);
            }
            String temp_search = TMP_e.substring(TMP_e.indexOf("]}.setX(", TMP_e.indexOf("{jaon[")) + 8);
            while (get_prevalue.contains("(")) {
                temp_search = temp_search.replaceFirst("\\(", "tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)", "tempCloseBreacketF6cyUQp9tempCloseBracket");
                get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
            }
            get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket", "(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket", ")");
            String get_value = StringFunctions.nestedArgs(get_prevalue, null, isAsync);

            TMP_e = createDefaultString("setX", get_name, get_prevalue, setDisplayX(get_name, get_value), TMP_e, isAsync);
        }

        while (TMP_e.contains("{display[") && TMP_e.contains("]}.setY(") && TMP_e.contains(")")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{display[") + 9, TMP_e.indexOf("]}.setY(", TMP_e.indexOf("{display[")));
            String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.setY(", TMP_e.indexOf("{display[")) + 8, TMP_e.indexOf(")", TMP_e.indexOf("]}.setY(", TMP_e.indexOf("{display["))));
            while (get_name.contains("{display[")) {
                get_name = get_name.substring(get_name.indexOf("{display[") + 9);
            }
            String temp_search = TMP_e.substring(TMP_e.indexOf("]}.setY(", TMP_e.indexOf("{jaon[")) + 9);
            while (get_prevalue.contains("(")) {
                temp_search = temp_search.replaceFirst("\\(", "tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)", "tempCloseBreacketF6cyUQp9tempCloseBracket");
                get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
            }
            get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket", "(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket", ")");
            String get_value = StringFunctions.nestedArgs(get_prevalue, null, isAsync);

            TMP_e = createDefaultString("setY", get_name, get_prevalue, setDisplayY(get_name, get_value), TMP_e, isAsync);
        }

        while (TMP_e.contains("{display[") && TMP_e.contains("]}.add(") && TMP_e.contains(")")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{display[") + 9, TMP_e.indexOf("]}.add(", TMP_e.indexOf("{display[")));
            String get_value = TMP_e.substring(TMP_e.indexOf("]}.add(", TMP_e.indexOf("{display[")) + 7, TMP_e.indexOf(")", TMP_e.indexOf("]}.add(", TMP_e.indexOf("{display["))));
            while (get_name.contains("{display[")) {
                get_name = get_name.substring(get_name.indexOf("{display[") + 9);
            }
            String temp_search = TMP_e.substring(TMP_e.indexOf("]}.add(", TMP_e.indexOf("{display[")) + 7);
            while (get_value.contains("(")) {
                temp_search = temp_search.replaceFirst("\\(", "tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)", "tempCloseBreacketF6cyUQp9tempCloseBracket");
                get_value = temp_search.substring(0, temp_search.indexOf(")"));
            }
            get_value = get_value.replace("tempOpenBracketF6cyUQp9tempOpenBracket", "(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket", ")");

            TMP_e = createDefaultString("add", get_name, get_value, addToDisplay(get_name, get_value), TMP_e, isAsync);
        }

        while (TMP_e.contains("{display[") && TMP_e.contains("]}.settings(") && TMP_e.contains(")")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{display[") + 9, TMP_e.indexOf("]}.settings(", TMP_e.indexOf("{display[")));
            String get_value = TMP_e.substring(TMP_e.indexOf("]}.settings(", TMP_e.indexOf("{display[")) + 12, TMP_e.indexOf(")", TMP_e.indexOf("]}.settings(", TMP_e.indexOf("{display["))));
            while (get_name.contains("{display[")) {
                get_name = get_name.substring(get_name.indexOf("{display[") + 9);
            }
            String temp_search = TMP_e.substring(TMP_e.indexOf("]}.settings(", TMP_e.indexOf("{display[")) + 12);
            while (get_value.contains("(")) {
                temp_search = temp_search.replaceFirst("\\(", "tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)", "tempCloseBreacketF6cyUQp9tempCloseBracket");
                get_value = temp_search.substring(0, temp_search.indexOf(")"));
            }
            get_value = get_value.replace("tempOpenBracketF6cyUQp9tempOpenBracket", "(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket", ")");
            String fin_value = StringFunctions.nestedArgs(get_value, null, isAsync);

            TMP_e = createDefaultString("settings", get_name, get_value, setDisplaySettings(get_name, fin_value), TMP_e, isAsync);
        }

        return TMP_e;
    }

    private static String createDefaultString(String function_name, String display_name, String arguments, String value, String TMP_e, Boolean isAsync) {
        if (isAsync) {
            global.Async_string.put("DisplayToString->" + display_name + function_name.toUpperCase() + "-" + (global.TMP_string.size() + 1), value);
            global.backupAsync_string.put("DisplayToString->" + display_name + function_name.toUpperCase() + "-" + global.TMP_string.size(), value);
            return TMP_e.replace("{display["+display_name+"]}."+function_name+"("+arguments+")","{string[AsyncDisplayToString->"+display_name+function_name.toUpperCase()+"-"+global.TMP_string.size()+"]}");
        } else {
            global.TMP_string.put("DisplayToString->" + display_name + function_name.toUpperCase() + "-" + (global.TMP_string.size() + 1), value);
            global.backupTMP_strings.put("DisplayToString->" + display_name + function_name.toUpperCase() + "-" + global.TMP_string.size(), value);
            return TMP_e.replace("{display["+display_name+"]}."+function_name+"("+arguments+")","{string[DisplayToString->"+display_name+function_name.toUpperCase()+"-"+global.TMP_string.size()+"]}");
        }
    }

    private static String createDefaultString(String function_name, String display_name, String value, String TMP_e, Boolean isAsync) {
        return createDefaultString(function_name, display_name, "", value, TMP_e, isAsync);
    }

    public static void dumpDisplays() {
        if (displays.size() > 0) {
            for (String display_name : displays.keySet()) {
                ChatHandler.warn(display_name);
                ChatHandler.warn(" " + displays_xy.get(display_name)[0] + " " + displays_xy.get(display_name)[1]);
                for (String display_value : displays.get(display_name)) {
                    ChatHandler.warn(" " + ChatHandler.ignoreFormatting(display_value));
                }
            }
        } else {
            ChatHandler.warn(ChatHandler.color("red","There are currently no displays"));
        }
    }
}
