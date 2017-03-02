package com.kerbybit.chattriggers.triggers;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.global;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DisplayHandler {
    private static String updateDisplay(String display_name) {
        List<String> display;

        if (global.displays.containsKey(display_name)
                && global.displays_xy.containsKey(display_name)
                && global.shown_displays.containsKey(display_name)) {
            display = global.displays.get(display_name);

            List<String> display_return = new ArrayList<String>();

            for (String value : display) {
                //setup backup for functions so strings don't get overwritten
                StringHandler.resetBackupStrings();

                //built in strings
                value = StringHandler.builtInStrings(value, null);

                //user strings and functions
                value = value.replace("{string<", "{string[").replace("{array<", "{array[").replace(">}", "]}");

                value = StringHandler.stringFunctions(value, null);
                value = ArrayHandler.arrayFunctions(value, null);
                value = StringHandler.stringFunctions(value, null);

                display_return.add(TagHandler.removeTags(value));
            }

            global.shown_displays.put(display_name, display_return);
            return "Updated " + display_name + "'s values";
        } else {
            return "Display " + display_name + " has no values";
        }
    }

    private static String addToDisplay(String display_name, String value) {
        if (global.displays.containsKey(display_name)
                && global.displays_xy.containsKey(display_name)
                && global.shown_displays.containsKey(display_name)) {
            ArrayList<String> display = new ArrayList<String>(global.displays.get(display_name));
            display.add(value);
            global.displays.put(display_name, display);
            return "Added " + value + " to " + display_name;
        } else {
            global.displays.put(display_name, Arrays.asList(value));
            global.displays_xy.put(display_name, new Double[]{0.0,0.0});
            global.shown_displays.put(display_name, new ArrayList<String>());
            return "Created and added " + value + " to " + display_name;
        }
    }

    private static String getDisplayX(String display_name) {
        if (global.displays_xy.containsKey(display_name)) {
            return global.displays_xy.get(display_name)[0] + "";
        } else {
            return "Display " + display_name + " has no x to get";
        }
    }

    private static String getDisplayY(String display_name) {
        if (global.displays_xy.containsKey(display_name)) {
            return global.displays_xy.get(display_name)[1] + "";
        } else {
            return "Display " + display_name + " has no y to get";
        }
    }

    private static String setDisplayX(String display_name, String value) {
        if (global.displays_xy.containsKey(display_name)) {
            try {
                Double x = Double.parseDouble(value);
                Double y = global.displays_xy.get(display_name)[1];
                global.displays_xy.put(display_name, new Double[]{x, y});
                return value + "";
            } catch (NumberFormatException e) {
                return "ERR: setDisplayX -> " + value + " is not a valid number";
            }
        } else {
            return "Display " + display_name + " has no x to set";
        }
    }

    private static String setDisplayY(String display_name, String value) {
        if (global.displays_xy.containsKey(display_name)) {
            try {
                Double x = global.displays_xy.get(display_name)[0];
                Double y = Double.parseDouble(value);
                global.displays_xy.put(display_name, new Double[]{x, y});
                return value + "";
            } catch (NumberFormatException e) {
                return "ERR: setDisplayY -> " + value + " is not a valid number";
            }
        } else {
            return "Display " + display_name + " has no y to set";
        }
    }

    private static String deleteDisplay(String display_name) {
        global.displays.remove(display_name);
        global.displays_xy.remove(display_name);
        global.shown_displays.remove(display_name);
        return "Cleared " + display_name;
    }

    public static void clearDisplays() {
        global.displays.clear();
        global.displays_xy.clear();
        global.shown_displays.clear();
    }

    public static void drawDisplays(RenderGameOverlayEvent event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            FontRenderer ren = Minecraft.getMinecraft().fontRendererObj;
            ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
            float width = res.getScaledWidth();
            float height = res.getScaledHeight();
            for (Map.Entry<String, List<String>> display_map : global.shown_displays.entrySet()) {
                String display_name = display_map.getKey();
                List<String> display = display_map.getValue();
                Double[] display_xy;

                if (global.displays_xy.containsKey(display_name)) {
                    display_xy = global.displays_xy.get(display_name);
                } else {
                    display_xy = new Double[]{0.0,0.0};
                }

                for (int i=0; i<display.size(); i++) {
                    ren.drawStringWithShadow(ChatHandler.addFormatting(display.get(i)), (display_xy[0].floatValue()*width)/100, ((display_xy[1].floatValue()*height)/100)+i*10, 0xffffff);
                }
            }
        }
    }

    static String displayFunctions(String TMP_e) {
        if (TMP_e.contains("{display[") && TMP_e.contains("]}.update()")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{display[")+9, TMP_e.indexOf("]}.update()", TMP_e.indexOf("{display[")));
            while (get_name.contains("{display[")) {
                get_name = get_name.substring(get_name.indexOf("{display[")+9);
            }

            List<String> temporary = new ArrayList<String>();
            temporary.add("DisplayToString->"+get_name+"UPDATE"+"-"+(global.TMP_string.size()+1));
            temporary.add(updateDisplay(get_name));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{display["+get_name+"]}.update()","{string[DisplayToString->"+get_name+"UPDATE"+"-"+global.TMP_string.size()+"]}");
        }

        if (TMP_e.contains("{display[") && TMP_e.contains("]}.clear()")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{display[")+9, TMP_e.indexOf("]}.clear()", TMP_e.indexOf("{display[")));
            while (get_name.contains("{display[")) {
                get_name = get_name.substring(get_name.indexOf("{display[")+9);
            }

            List<String> temporary = new ArrayList<String>();
            temporary.add("DisplayToString->"+get_name+"CLEAR"+"-"+(global.TMP_string.size()+1));
            temporary.add(deleteDisplay(get_name));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{display["+get_name+"]}.clear()","{string[DisplayToString->"+get_name+"CLEAR"+"-"+global.TMP_string.size()+"]}");
        }

        if (TMP_e.contains("{display[") && TMP_e.contains("]}.getX()")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{display[")+9, TMP_e.indexOf("]}.getX()", TMP_e.indexOf("{display[")));
            while (get_name.contains("{display[")) {
                get_name = get_name.substring(get_name.indexOf("{display[")+9);
            }

            List<String> temporary = new ArrayList<String>();
            temporary.add("DisplayToString->"+get_name+"GETX"+"-"+(global.TMP_string.size()+1));
            temporary.add(getDisplayX(get_name));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{display["+get_name+"]}.getX()","{string[DisplayToString->"+get_name+"GETX"+"-"+global.TMP_string.size()+"]}");
        }

        if (TMP_e.contains("{display[") && TMP_e.contains("]}.getY()")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{display[")+9, TMP_e.indexOf("]}.getY()", TMP_e.indexOf("{display[")));
            while (get_name.contains("{display[")) {
                get_name = get_name.substring(get_name.indexOf("{display[")+9);
            }

            List<String> temporary = new ArrayList<String>();
            temporary.add("DisplayToString->"+get_name+"GETY"+"-"+(global.TMP_string.size()+1));
            temporary.add(getDisplayY(get_name));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{display["+get_name+"]}.getY()","{string[DisplayToString->"+get_name+"GETY"+"-"+global.TMP_string.size()+"]}");
        }

        while (TMP_e.contains("{display[") && TMP_e.contains("]}.setX(") && TMP_e.contains(")")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{display[")+9, TMP_e.indexOf("]}.setX(", TMP_e.indexOf("{display[")));
            String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.setX(", TMP_e.indexOf("{display["))+8, TMP_e.indexOf(")", TMP_e.indexOf("]}.setX(", TMP_e.indexOf("{display["))));
            while (get_name.contains("{display[")) {
                get_name = get_name.substring(get_name.indexOf("{display[")+9);
            }
            while (get_prevalue.contains("(")) {
                String temp_search = TMP_e.substring(TMP_e.indexOf("]}.setX(", TMP_e.indexOf("{display["))+8);
                temp_search = temp_search.replaceFirst("\\(","tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)","tempCloseBreacketF6cyUQp9tempCloseBracket");
                get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
            }
            get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket","(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket",")");
            String get_value = StringHandler.stringFunctions(get_prevalue, null);

            List<String> temporary = new ArrayList<String>();
            temporary.add("DisplayToString->"+get_name+"SETX"+"-"+(global.TMP_string.size()+1));
            temporary.add(setDisplayX(get_name, get_value));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{display["+get_name+"]}.setX("+get_prevalue+")","{string[DisplayToString->"+get_name+"SETX"+"-"+global.TMP_string.size()+"]}");
        }

        while (TMP_e.contains("{display[") && TMP_e.contains("]}.setY(") && TMP_e.contains(")")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{display[")+9, TMP_e.indexOf("]}.setY(", TMP_e.indexOf("{display[")));
            String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.setY(", TMP_e.indexOf("{display["))+8, TMP_e.indexOf(")", TMP_e.indexOf("]}.setY(", TMP_e.indexOf("{display["))));
            while (get_name.contains("{display[")) {
                get_name = get_name.substring(get_name.indexOf("{display[")+9);
            }
            while (get_prevalue.contains("(")) {
                String temp_search = TMP_e.substring(TMP_e.indexOf("]}.setY(", TMP_e.indexOf("{display["))+8);
                temp_search = temp_search.replaceFirst("\\(","tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)","tempCloseBreacketF6cyUQp9tempCloseBracket");
                get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
            }
            get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket","(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket",")");
            String get_value = StringHandler.stringFunctions(get_prevalue, null);

            List<String> temporary = new ArrayList<String>();
            temporary.add("DisplayToString->"+get_name+"SETY"+"-"+(global.TMP_string.size()+1));
            temporary.add(setDisplayY(get_name, get_value));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{display["+get_name+"]}.setY("+get_prevalue+")","{string[DisplayToString->"+get_name+"SETY"+"-"+global.TMP_string.size()+"]}");
        }

        while (TMP_e.contains("{display[") && TMP_e.contains("]}.add(") && TMP_e.contains(")")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{display[")+9, TMP_e.indexOf("]}.add(", TMP_e.indexOf("{display[")));
            String get_value = TMP_e.substring(TMP_e.indexOf("]}.add(", TMP_e.indexOf("{display["))+7, TMP_e.indexOf(")", TMP_e.indexOf("]}.add(", TMP_e.indexOf("{display["))));
            while (get_name.contains("{display[")) {
                get_name = get_name.substring(get_name.indexOf("{display[")+9);
            }
            while (get_value.contains("(")) {
                String temp_search = TMP_e.substring(TMP_e.indexOf("]}.add(", TMP_e.indexOf("{display["))+7);
                temp_search = temp_search.replaceFirst("\\(","tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)","tempCloseBreacketF6cyUQp9tempCloseBracket");
                get_value = temp_search.substring(0, temp_search.indexOf(")"));
            }
            get_value = get_value.replace("tempOpenBracketF6cyUQp9tempOpenBracket","(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket",")");

            List<String> temporary = new ArrayList<String>();
            temporary.add("DisplayToString->"+get_name+"ADD"+get_value+"-"+(global.TMP_string.size()+1));
            temporary.add(addToDisplay(get_name, get_value));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{display["+get_name+"]}.add("+get_value+")","{string[DisplayToString->"+get_name+"ADD"+get_value+"-"+global.TMP_string.size()+"]}");
        }

        return TMP_e;
    }
}
