package com.kerbybit.chattriggers.objects;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.gui.IconHandler;
import com.kerbybit.chattriggers.triggers.BuiltInStrings;
import com.kerbybit.chattriggers.triggers.StringFunctions;
import com.kerbybit.chattriggers.triggers.StringHandler;
import com.kerbybit.chattriggers.triggers.TagHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

import java.util.*;
import java.util.List;

import static net.minecraft.realms.RealmsMth.floor;

public class DisplayHandler {
    private static HashMap<String,List<String>> displays = new HashMap<String,List<String>>();
    private static HashMap<String,List<String>> shown_displays = new HashMap<String,List<String>>();
    private static HashMap<String,Double[]> displays_xy = new HashMap<String,Double[]>();
    private static HashMap<String,String> display_settings = new HashMap<String, String>();

    private static String updateDisplay(String display_name, Boolean isAsync) {
        List<String> display;

        if (displays.containsKey(display_name)
                && displays_xy.containsKey(display_name)
                && shown_displays.containsKey(display_name)) {
            display = displays.get(display_name);

            List<String> display_return = new ArrayList<String>();

            for (String value : display) {
                //setup backup for functions so strings don't get overwritten
                StringHandler.resetBackupStrings();

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
            ArrayList<String> display = new ArrayList<String>(displays.get(display_name));
            display.add(value);
            displays.put(display_name, display);
            return "Added " + value + " to " + display_name;
        } else {
            displays.put(display_name, Collections.singletonList(value));
            displays_xy.put(display_name, new Double[]{0.0,0.0,1.0});
            shown_displays.put(display_name, new ArrayList<String>());
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

    private static String getDisplaySettings(String display_name) {
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

    public static void drawDisplays(RenderGameOverlayEvent event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
        GL11.glColor4f(1, 1, 1, 1);
            FontRenderer ren = Minecraft.getMinecraft().fontRendererObj;
            ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
            float width = res.getScaledWidth();
            float height = res.getScaledHeight();

            for (Map.Entry<String, List<String>> display_map : shown_displays.entrySet()) {
                String display_name = display_map.getKey();
                List<String> display = display_map.getValue();
                Double[] display_xy;
                String settings = getDisplaySettings(display_name);
                int max_width = 0;
                int color = 0x00ffffff;

                if (displays_xy.containsKey(display_name)) {
                    display_xy = displays_xy.get(display_name);
                } else {
                    display_xy = new Double[]{0.0,0.0};
                }

                String bg = "none";
                String bgc = "40000000";

                List<String> display_texts = new ArrayList<String>();
                List<Float> display_xs = new ArrayList<Float>();
                List<Float> display_ys = new ArrayList<Float>();
                boolean up = false;
                int align = 0;

                if (settings.contains("<bg=") && settings.contains(">")) {
                    bg = settings.substring(settings.indexOf("<bg=")+4, settings.indexOf(">", settings.indexOf("<bg=")));
                    settings = settings.replace("<bg="+bg+">", "");
                }
                if (settings.contains("<bgc=") && settings.contains(">")) {
                    bgc = settings.substring(settings.indexOf("<bgc=")+5, settings.indexOf(">", settings.indexOf("<bgc=")));
                    settings = settings.replace("<bgc="+bgc+">", "");
                    bgc = bgc.replace("0x", "");
                }

                for (int i=0; i<display.size(); i++) {
                    String display_text = ChatHandler.addFormatting(settings + display.get(i));
                    display_text = display_text.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ",")
                            .replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(")
                            .replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")")
                            .replace("stringOpenBracketReplacementF6cyUQp9stringOpenBracketReplacement", "(")
                            .replace("stringCloseBracketReplacementF6cyUQp9stringCloseBracketReplacement", ")")
                            .replace("AmpF6cyUQp9Amp","&")
                            .replace("TripleDotF6cyUQp9TripleDot","...")
                            .replace("BackslashF6cyUQp9Backslash","\\\\")
                            .replace("NewLineF6cyUQp9NewLine","\\n")
                            .replace("SingleQuoteF6cyUQp9SingleQuote","\\'");
                    float display_x;
                    float display_y;
                    float spacing = 1;
                    if (display_text.contains("<spacing=") && display_text.contains(">")) {
                        String spacing_string = display_text.substring(display_text.indexOf("<spacing=")+9, display_text.indexOf(">", display_text.indexOf("<spacing=")));
                        try {
                            spacing = Float.parseFloat(spacing_string);
                        } catch (NumberFormatException e) {
                            System.out.println("<spacing=$n> - $n must be a number!");
                        }
                        display_text = display_text.replace("<spacing="+spacing_string+">", "");
                    }

                    String rainbow_string = "";
                    if (display_text.contains("<rainbow=") && display_text.contains(">")) {
                        rainbow_string = display_text.substring(display_text.indexOf("<rainbow="), display_text.indexOf(">", display_text.indexOf("<rainbow="))+1);
                    }



                    if (display_text.contains("<up>")) {
                        up = true;
                        display_text = display_text.replace("<up>","");
                        if (display_text.contains("<center>")) {
                            align = 1;
                            display_text = display_text.replace("<center>","");
                            int text_width = ren.getStringWidth(IconHandler.removeIconString(display_text.replace("<rainbow>","").replace(rainbow_string,"")));
                            display_x = ((display_xy[0].floatValue() * width) / 100) - text_width/2;
                            display_y = ((display_xy[1].floatValue() * height) / 100) + (i+1) * -10 * spacing;
                        } else if (display_text.contains("<right>")) {
                            align = 2;
                            display_text = display_text.replace("<right>","");
                            int text_width = ren.getStringWidth(IconHandler.removeIconString(display_text.replace("<rainbow>","").replace(rainbow_string,"")));
                            display_x = ((display_xy[0].floatValue() * width) / 100) - text_width;
                            display_y = ((display_xy[1].floatValue() * height) / 100) + (i+1) * -10 * spacing;
                        } else {
                            display_text = display_text.replace("<left>","");
                            display_x = (display_xy[0].floatValue() * width) / 100;
                            display_y = ((display_xy[1].floatValue() * height) / 100) + (i+1) * -10 * spacing;
                        }
                    } else {
                        display_text = display_text.replace("<down>","");
                        if (display_text.contains("<center>")) {
                            align = 1;
                            display_text = display_text.replace("<center>","");
                            int text_width = ren.getStringWidth(IconHandler.removeIconString(display_text.replace("<rainbow>","").replace(rainbow_string,"")));
                            display_x = ((display_xy[0].floatValue() * width) / 100) - text_width/2;
                            display_y = ((display_xy[1].floatValue() * height) / 100) + i * 10 * spacing;
                        } else if (display_text.contains("<right>")) {
                            align = 2;
                            display_text = display_text.replace("<right>","");
                            int text_width = ren.getStringWidth(IconHandler.removeIconString(display_text.replace("<rainbow>","").replace(rainbow_string,"")));
                            display_x = ((display_xy[0].floatValue() * width) / 100) - text_width;
                            display_y = ((display_xy[1].floatValue() * height) / 100) + i * 10 * spacing;
                        } else {
                            display_text = display_text.replace("<left>","");
                            display_x = (display_xy[0].floatValue() * width) / 100;
                            display_y = ((display_xy[1].floatValue() * height) / 100) + i * 10 * spacing;
                        }
                    }
                    display_text = IconHandler.drawIcons(display_text, floor(display_x), floor(display_y));

                    if (ren.getStringWidth(display_text) > max_width) {
                        max_width = ren.getStringWidth(display_text);
                    }

                    display_texts.add(display_text);
                    display_xs.add(display_x);
                    display_ys.add(display_y);
                }

                if (display_texts.size() > 0) {
                    drawDisplay(display_texts, display_xs, display_ys, color, bg, bgc, max_width, up, align);
                }
            }
        }
    }

    private static void drawDisplay(List<String> display_texts, List<Float> display_xs, List<Float> display_ys, int color, String bg, String bgc, int max_width, boolean up, int align) {
        FontRenderer ren = Minecraft.getMinecraft().fontRendererObj;

        if (bg.equalsIgnoreCase("full")) {
            int bg_y = floor(display_ys.get(0));
            if (up) {
                if (align == 0) {
                    int bg_x = floor(display_xs.get(0));
                    int bg_h = floor(display_ys.get(display_ys.size()-1));
                    try {
                        drawRect(bg_x, bg_y+10, bg_x+max_width, bg_h, (int) Long.parseLong(bgc, 16));
                    } catch (NumberFormatException e) {
                        drawRect(bg_x, bg_y+10, bg_x+max_width, bg_h, 0x40000000);
                    }
                } else if (align == 1) {
                    int bg_x = floor(display_xs.get(0)) + 3;
                    int bg_h = floor(display_ys.get(display_ys.size()-1));
                    try {
                        drawRect(bg_x-max_width/4, bg_y+10, bg_x+(max_width-max_width/4), bg_h, (int) Long.parseLong(bgc, 16));
                    } catch (NumberFormatException e) {
                        drawRect(bg_x-max_width/4, bg_y+10, bg_x+(max_width-max_width/4), bg_h, 0x40000000);
                    }
                } else if (align == 2) {
                    int bg_x = floor(display_xs.get(0)) + 9;
                    int bg_h = floor(display_ys.get(display_ys.size()-1));
                    try {
                        drawRect(bg_x - max_width/2, bg_y+10, bg_x + max_width/2, bg_h, (int) Long.parseLong(bgc, 16));
                    } catch (NumberFormatException e) {
                        drawRect(bg_x - max_width/2, bg_y+10, bg_x + max_width/2, bg_h, 0x40000000);
                    }
                }
            } else {
                if (align == 0) {
                    int bg_x = floor(display_xs.get(0));
                    int bg_h = floor(display_ys.get(display_ys.size()-1))+10;
                    try {
                        drawRect(bg_x, bg_y, bg_x+max_width, bg_h, (int) Long.parseLong(bgc, 16));
                    } catch (NumberFormatException e) {
                        drawRect(bg_x, bg_y, bg_x+max_width, bg_h, 0x40000000);
                    }
                } else if (align == 1) {
                    int bg_x = floor(display_xs.get(0)) + 3;
                    int bg_h = floor(display_ys.get(display_ys.size()-1))+10;
                    try {
                        drawRect(bg_x-max_width/4, bg_y, bg_x+(max_width-max_width/4), bg_h, (int) Long.parseLong(bgc, 16));
                    } catch (NumberFormatException e) {
                        drawRect(bg_x-max_width/4, bg_y, bg_x+(max_width-max_width/4), bg_h, 0x40000000);
                    }
                } else if (align == 2) {
                    int bg_x = floor(display_xs.get(0)) + 9;
                    int bg_h = floor(display_ys.get(display_ys.size()-1)) + 10;
                    try {
                        drawRect(bg_x - max_width/2, bg_y, bg_x + max_width/2, bg_h, (int) Long.parseLong(bgc, 16));
                    } catch (NumberFormatException e) {
                        drawRect(bg_x - max_width/2, bg_y, bg_x + max_width/2, bg_h, 0x40000000);
                    }
                }
            }
        }

        for (int i=0; i<display_texts.size(); i++) {
            String display_text = display_texts.get(i);
            float display_x = display_xs.get(i);
            float display_y = display_ys.get(i);

            if (display_text.contains("<rainbow>") || (display_text.contains("<rainbow=") && display_text.contains(">"))) {
                display_text = display_text.replace("<rainbow>", "");
                float speed = 5;
                if (display_text.contains("<rainbow=") && display_text.contains(">")) {
                    String speed_string = display_text.substring(display_text.indexOf("<rainbow=")+9, display_text.indexOf(">", display_text.indexOf("<rainbow=")));
                    try {
                        speed = Float.parseFloat(speed_string);
                    } catch (NumberFormatException exception) {
                        if (global.debug) {ChatHandler.warn("gray", "<rainbow=&n> - &n must be a number!");}
                    }
                    display_text = display_text.replace("<rainbow="+speed_string+">", "");
                }
                float step = global.ticksElapsed;
                int red = (int) ((Math.sin(step / speed) + 0.75) * 170);
                int green = (int) ((Math.sin(step / speed + ((2 * Math.PI) / 3)) + 0.75) * 170);
                int blue = (int) ((Math.sin(step / speed + ((4 * Math.PI) / 3)) + 0.75) * 170);

                if (red < 0) red = 0;
                if (green < 0) green = 0;
                if (blue < 0) blue = 0;
                if (red > 255) red = 255;
                if (green > 255) green = 255;
                if (blue > 255) blue = 255;

                color = 0xff000000 + (red*0x10000) + (green*0x100) + blue;
            } else {
                color = 0xffffff;
            }

            if (!display_text.equals("") && bg.equalsIgnoreCase("line")) {
                try {
                    drawRect(display_x, display_y, display_x + ren.getStringWidth(display_text), display_y + 10, (int) Long.parseLong(bgc, 16));
                } catch (NumberFormatException e) {
                    drawRect(display_x, display_y, display_x + ren.getStringWidth(display_text), display_y + 10, 0x40000000);
                }
            }

            ren.drawStringWithShadow(display_text, display_x, display_y, color);
        }
    }

    public static void drawRect(double left, double top, double right, double bottom, int color) {
        if (left < right) {
            double i = left;
            left = right;
            right = i;
        }

        if (top < bottom) {
            double j = top;
            top = bottom;
            bottom = j;
        }

        float f3 = (float)(color >> 24 & 255) / 255.0F;
        float f = (float)(color >> 16 & 255) / 255.0F;
        float f1 = (float)(color >> 8 & 255) / 255.0F;
        float f2 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, f3);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left+1, bottom-1, 0.0D).endVertex();
        worldrenderer.pos(right-1, bottom-1, 0.0D).endVertex();
        worldrenderer.pos(right-1, top-1, 0.0D).endVertex();
        worldrenderer.pos(left+1, top-1, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
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

            TMP_e = createDefaultString("settings", get_name, get_value, setDisplaySettings(get_name, get_value), TMP_e, isAsync);
        }

        return TMP_e;
    }

    private static String createDefaultString(String function_name, String display_name, String arguments, String value, String TMP_e, Boolean isAsync) {
        if (isAsync) {
            global.Async_string.put("DisplayToString->" + display_name + function_name.toUpperCase() + "-" + (global.TMP_string.size() + 1), value);
            global.backupAsync_string.put("DisplayToString->" + display_name + function_name.toUpperCase() + "-" + global.TMP_string.size(), value);
            return TMP_e.replace("{display["+display_name+"]}."+function_name+"("+arguments+")","{string[AsyncDisplayToString->"+display_name+function_name.toUpperCase()+"-"+global.TMP_string.size()+"]}");
        } else {
            List<String> temporary = new ArrayList<String>();
            temporary.add("DisplayToString->" + display_name + function_name.toUpperCase() + "-" + (global.TMP_string.size() + 1));
            temporary.add(value);
            global.TMP_string.add(temporary);
            global.backupTMP_strings.add(temporary);
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
