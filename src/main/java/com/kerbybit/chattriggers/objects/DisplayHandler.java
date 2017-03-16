package com.kerbybit.chattriggers.objects;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.commands.CommandReference;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.objects.ArrayHandler;
import com.kerbybit.chattriggers.triggers.StringHandler;
import com.kerbybit.chattriggers.triggers.TagHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static net.minecraft.realms.RealmsMth.floor;

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
                value = value.replace("{string<", "{string[")
                        .replace("{array<", "{array[")
                        .replace("{display<", "{display[")
                        .replace("{json<", "{json[")
                        .replace("{list<", "{list[")
                        .replace(">}", "]}");

                value = NewJsonHandler.jsonFunctions(value);
                value = StringHandler.stringFunctions(value, null);
                value = ListHandler.listFunctions(value);
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
            global.displays_xy.put(display_name, new Double[]{0.0,0.0,1.0});
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

    private static String getDisplayA(String display_name) {
        if (global.displays_xy.containsKey(display_name)) {
            return global.displays_xy.get(display_name)[2] + "";
        } else {
            return "Display " + display_name + " has no alpha to get";
        }
    }

    private static String setDisplayX(String display_name, String value) {
        if (global.displays_xy.containsKey(display_name)) {
            try {
                Double x = Double.parseDouble(value);
                Double y = global.displays_xy.get(display_name)[1];
                Double a = global.displays_xy.get(display_name)[2];
                global.displays_xy.put(display_name, new Double[]{x, y, a});
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
                Double a = global.displays_xy.get(display_name)[2];
                global.displays_xy.put(display_name, new Double[]{x, y, a});
                return value + "";
            } catch (NumberFormatException e) {
                return "ERR: setDisplayY -> " + value + " is not a valid number";
            }
        } else {
            return "Display " + display_name + " has no y to set";
        }
    }

    private static String setDisplayA(String display_name, String value) {
        /*if (global.displays_xy.containsKey(display_name)) {
            try {
                Double x = global.displays_xy.get(display_name)[0];
                Double y = global.displays_xy.get(display_name)[1];
                Double a = Double.parseDouble(value);
                global.displays_xy.put(display_name, new Double[]{x, y, a});
                return value + "";
            } catch (NumberFormatException e) {
                return "ERR: setDisplayY -> " + value + " is not a valid number";
            }
        } else {*/
            return "Display " + display_name + " has no alpha to set";
        /*}*/
    }

    private static String setDisplaySettings(String display_name, String settings) {
        global.display_settings.put(display_name, settings);
        return "Added settings " + settings + " to display " + display_name;
    }

    private static String getDisplaySettings(String display_name) {
        if (global.display_settings.containsKey(display_name)) {
            String return_string = "";
            for (String value : global.display_settings.get(display_name).split(",")) {
                return_string+="<"+value+">";
            }
            return return_string;
        } else {
            return "";
        }
    }

    private static String deleteDisplay(String display_name) {
        global.displays.remove(display_name);
        global.displays_xy.remove(display_name);
        global.shown_displays.remove(display_name);
        global.display_settings.remove(display_name);
        return "Cleared " + display_name;
    }

    public static void clearDisplays() {
        global.displays.clear();
        global.displays_xy.clear();
        global.shown_displays.clear();
    }

    public static void drawDisplays(RenderGameOverlayEvent event) {
        GL11.glColor4f(1, 1, 1, 1);

        FontRenderer ren = Minecraft.getMinecraft().fontRendererObj;
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        float width = res.getScaledWidth();
        float height = res.getScaledHeight();

        for (Map.Entry<String, List<String>> display_map : global.shown_displays.entrySet()) {
            String display_name = display_map.getKey();
            List<String> display = display_map.getValue();
            Double[] display_xy;
            String settings = getDisplaySettings(display_name);
            int color = 0x00ffffff;
            /*color = color + (global.displays_xy.get(display_name)[2].intValue() * 0x01000000);
            System.out.println(color);*/

            if (global.displays_xy.containsKey(display_name)) {
                display_xy = global.displays_xy.get(display_name);
            } else {
                display_xy = new Double[]{0.0,0.0};
            }

            for (int i=0; i<display.size(); i++) {
                if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
                    String display_text = ChatHandler.addFormatting(settings + display.get(i));
                    float display_x;
                    float display_y;
                    float spacing = 1;
                    if (display_text.contains("<spacing=") && display_text.contains(">")) {
                        try {
                            String spacing_string = display_text.substring(display_text.indexOf("<spacing=")+9, display_text.indexOf(">", display_text.indexOf("<spacing=")));
                            spacing = Float.parseFloat(spacing_string);
                            display_text = display_text.replace("<spacing="+spacing_string+">", "");
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            System.out.println("<spacing=$n> - $n must be a number!");
                        }
                    }

                    if (display_text.contains("<up>")) {
                        display_text = display_text.replace("<up>","");
                        if (display_text.contains("<center>")) {
                            display_text = display_text.replace("<center>","");
                            display_x = ((display_xy[0].floatValue() * width) / 100) - (ren.getStringWidth(CommandReference.removeIconString(display_text))/2);
                            display_y = ((display_xy[1].floatValue() * height) / 100) + (i+1) * -10 * spacing;
                        } else if (display_text.contains("<right>")) {
                            display_text = display_text.replace("<right>","");
                            display_x = ((display_xy[1].floatValue() * height) / 100) + (i+1) * -10 * spacing;
                            display_y = ((display_xy[0].floatValue() * width) / 100) - ren.getStringWidth(CommandReference.removeIconString(display_text));
                        } else {
                            display_text = display_text.replace("<left>","");
                            display_x = (display_xy[0].floatValue() * width) / 100;
                            display_y = ((display_xy[1].floatValue() * height) / 100) + (i+1) * -10 * spacing;
                        }
                    } else {
                        display_text = display_text.replace("<down>","");
                        if (display_text.contains("<center>")) {
                            display_text = display_text.replace("<center>","");
                            display_x = ((display_xy[0].floatValue() * width) / 100) - (ren.getStringWidth(CommandReference.removeIconString(display_text))/2);
                            display_y = ((display_xy[1].floatValue() * height) / 100) + i * 10 * spacing;
                        } else if (display_text.contains("<right>")) {
                            display_text = display_text.replace("<right>","");
                            display_x = ((display_xy[0].floatValue() * width) / 100) - ren.getStringWidth(CommandReference.removeIconString(display_text));
                            display_y = ((display_xy[1].floatValue() * height) / 100) + i * 10 * spacing;
                        } else {
                            display_text = display_text.replace("<left>","");
                            display_x = (display_xy[0].floatValue() * width) / 100;
                            display_y = ((display_xy[1].floatValue() * height) / 100) + i * 10 * spacing;
                        }
                    }
                    display_text = CommandReference.drawIcons(display_text, floor(display_x), floor(display_y));
                    ren.drawStringWithShadow(display_text, display_x, display_y, color);
                }
            }
        }
    }

    public static String displayFunctions(String TMP_e) {
        while (TMP_e.contains("{display[") && TMP_e.contains("]}.update()")) {
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

        while (TMP_e.contains("{display[") && TMP_e.contains("]}.clear()")) {
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

        while (TMP_e.contains("{display[") && TMP_e.contains("]}.getX()")) {
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

        while (TMP_e.contains("{display[") && TMP_e.contains("]}.getY()")) {
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

        while (TMP_e.contains("{display[") && TMP_e.contains("]}.getA()")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{display[")+9, TMP_e.indexOf("]}.getA()", TMP_e.indexOf("{display[")));
            while (get_name.contains("{display[")) {
                get_name = get_name.substring(get_name.indexOf("{display[")+9);
            }

            List<String> temporary = new ArrayList<String>();
            temporary.add("DisplayToString->"+get_name+"GETA"+"-"+(global.TMP_string.size()+1));
            temporary.add(getDisplayA(get_name));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{display["+get_name+"]}.getA()","{string[DisplayToString->"+get_name+"GETA"+"-"+global.TMP_string.size()+"]}");
        }

        while (TMP_e.contains("{display[") && TMP_e.contains("]}.setX(") && TMP_e.contains(")")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{display[")+9, TMP_e.indexOf("]}.setX(", TMP_e.indexOf("{display[")));
            String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.setX(", TMP_e.indexOf("{display["))+8, TMP_e.indexOf(")", TMP_e.indexOf("]}.setX(", TMP_e.indexOf("{display["))));
            while (get_name.contains("{display[")) {
                get_name = get_name.substring(get_name.indexOf("{display[")+9);
            }
            String temp_search = TMP_e.substring(TMP_e.indexOf("]}.setX(", TMP_e.indexOf("{jaon["))+8);
            while (get_prevalue.contains("(")) {
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
            String temp_search = TMP_e.substring(TMP_e.indexOf("]}.setY(", TMP_e.indexOf("{jaon["))+9);
            while (get_prevalue.contains("(")) {
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

        while (TMP_e.contains("{display[") && TMP_e.contains("]}.setA(") && TMP_e.contains(")")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{display[")+9, TMP_e.indexOf("]}.setA(", TMP_e.indexOf("{display[")));
            String get_prevalue = TMP_e.substring(TMP_e.indexOf("]}.setA(", TMP_e.indexOf("{display["))+8, TMP_e.indexOf(")", TMP_e.indexOf("]}.setA(", TMP_e.indexOf("{display["))));
            while (get_name.contains("{display[")) {
                get_name = get_name.substring(get_name.indexOf("{display[")+9);
            }
            String temp_search = TMP_e.substring(TMP_e.indexOf("]}.setA(", TMP_e.indexOf("{jaon["))+8);
            while (get_prevalue.contains("(")) {
                temp_search = temp_search.replaceFirst("\\(","tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)","tempCloseBreacketF6cyUQp9tempCloseBracket");
                get_prevalue = temp_search.substring(0, temp_search.indexOf(")"));
            }
            get_prevalue = get_prevalue.replace("tempOpenBracketF6cyUQp9tempOpenBracket","(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket",")");
            String get_value = StringHandler.stringFunctions(get_prevalue, null);

            List<String> temporary = new ArrayList<String>();
            temporary.add("DisplayToString->"+get_name+"SETA"+"-"+(global.TMP_string.size()+1));
            temporary.add(setDisplayA(get_name, get_value));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{display["+get_name+"]}.setA("+get_prevalue+")","{string[DisplayToString->"+get_name+"SETA"+"-"+global.TMP_string.size()+"]}");
        }

        while (TMP_e.contains("{display[") && TMP_e.contains("]}.add(") && TMP_e.contains(")")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{display[")+9, TMP_e.indexOf("]}.add(", TMP_e.indexOf("{display[")));
            String get_value = TMP_e.substring(TMP_e.indexOf("]}.add(", TMP_e.indexOf("{display["))+7, TMP_e.indexOf(")", TMP_e.indexOf("]}.add(", TMP_e.indexOf("{display["))));
            while (get_name.contains("{display[")) {
                get_name = get_name.substring(get_name.indexOf("{display[")+9);
            }
            String temp_search = TMP_e.substring(TMP_e.indexOf("]}.add(", TMP_e.indexOf("{display["))+7);
            while (get_value.contains("(")) {
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

        while (TMP_e.contains("{display[") && TMP_e.contains("]}.settings(") && TMP_e.contains(")")) {
            String get_name = TMP_e.substring(TMP_e.indexOf("{display[")+9, TMP_e.indexOf("]}.settings(", TMP_e.indexOf("{display[")));
            String get_value = TMP_e.substring(TMP_e.indexOf("]}.settings(", TMP_e.indexOf("{display["))+12, TMP_e.indexOf(")", TMP_e.indexOf("]}.settings(", TMP_e.indexOf("{display["))));
            while (get_name.contains("{display[")) {
                get_name = get_name.substring(get_name.indexOf("{display[")+9);
            }
            String temp_search = TMP_e.substring(TMP_e.indexOf("]}.settings(", TMP_e.indexOf("{display["))+12);
            while (get_value.contains("(")) {
                temp_search = temp_search.replaceFirst("\\(","tempOpenBracketF6cyUQp9tempOpenBracket").replaceFirst("\\)","tempCloseBreacketF6cyUQp9tempCloseBracket");
                get_value = temp_search.substring(0, temp_search.indexOf(")"));
            }
            get_value = get_value.replace("tempOpenBracketF6cyUQp9tempOpenBracket","(").replace("tempCloseBreacketF6cyUQp9tempCloseBracket",")");

            List<String> temporary = new ArrayList<String>();
            temporary.add("DisplayToString->"+get_name+"SETTINGS"+get_value+"-"+(global.TMP_string.size()+1));
            temporary.add(setDisplaySettings(get_name, get_value));
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);

            TMP_e = TMP_e.replace("{display["+get_name+"]}.settings("+get_value+")","{string[DisplayToString->"+get_name+"SETTINGS"+get_value+"-"+global.TMP_string.size()+"]}");
        }

        return TMP_e;
    }
}
