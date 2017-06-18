package com.kerbybit.chattriggers.objects;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.gui.IconHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.minecraft.realms.RealmsMth.floor;

public class DisplayRenderer {
    private static String display_text;

    public static void drawDisplays(RenderGameOverlayEvent event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            GL11.glColor4f(1, 1, 1, 1);

            FontRenderer ren = Minecraft.getMinecraft().fontRendererObj;
            ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
            float width = res.getScaledWidth();
            float height = res.getScaledHeight();

            for (Map.Entry<String, List<String>> display_map : DisplayHandler.shown_displays.entrySet()) {
                String display_name = display_map.getKey();
                List<String> display = display_map.getValue();

                String settings = DisplayHandler.getDisplaySettings(display_name);
                int max_width = 0;

                Double[] display_xy = DisplayHandler.getDisplayXY(display_name);

                String bg = DisplayHandler.getDisplayBackground(settings);
                String bgc_str = DisplayHandler.getDisplayBackgroundColor(settings);
                long bgc = 0x40000000;
                try {
                    bgc = Long.parseLong(bgc_str, 16);
                } catch (NumberFormatException exception) { /* do nothing */ }
                settings = settings.replace("<bg="+bg+">", "").replace("<bgc="+bgc_str+">", "");

                List<String> display_texts = new ArrayList<>();
                List<Float> display_xs = new ArrayList<>();
                List<Float> display_ys = new ArrayList<>();

                int align = 0;

                boolean up = false;

                for (int i=0; i<display.size(); i++) {
                    display_text = removeStringExtras(ChatHandler.addFormatting(settings + display.get(i)));

                    float display_x;
                    float display_y;
                    float spacing = getSpacing();

                    String rainbow_string = getRainbow();
                    String shadow_string = getShadow();

                    if (display_text.contains("<up>")) {
                        up = true;
                        display_text = display_text.replace("<up>","");
                        if (display_text.contains("<center>")) {
                            align = 1;
                            display_text = display_text.replace("<center>","");
                            int text_width = ren.getStringWidth(removeExtras(IconHandler.removeIconString(display_text), "<rainbow>", rainbow_string, shadow_string));
                            display_x = ((display_xy[0].floatValue() * width) / 100) - text_width/2;
                            display_y = ((display_xy[1].floatValue() * height) / 100) + (i+1) * -10 * spacing;
                        } else if (display_text.contains("<right>")) {
                            align = 2;
                            display_text = display_text.replace("<right>","");
                            int text_width = ren.getStringWidth(removeExtras(IconHandler.removeIconString(display_text), "<rainbow>", rainbow_string, shadow_string));
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
                            int text_width = ren.getStringWidth(removeExtras(IconHandler.removeIconString(display_text), "<rainbow>", rainbow_string, shadow_string));
                            display_x = ((display_xy[0].floatValue() * width) / 100) - text_width/2;
                            display_y = ((display_xy[1].floatValue() * height) / 100) + i * 10 * spacing;
                        } else if (display_text.contains("<right>")) {
                            align = 2;
                            display_text = display_text.replace("<right>","");
                            int text_width = ren.getStringWidth(removeExtras(IconHandler.removeIconString(display_text), "<rainbow>", rainbow_string, shadow_string));
                            display_x = ((display_xy[0].floatValue() * width) / 100) - text_width;
                            display_y = ((display_xy[1].floatValue() * height) / 100) + i * 10 * spacing;
                        } else {
                            display_text = display_text.replace("<left>","");
                            display_x = (display_xy[0].floatValue() * width) / 100;
                            display_y = ((display_xy[1].floatValue() * height) / 100) + i * 10 * spacing;
                        }
                    }

                    if (bg.equalsIgnoreCase("full")) {
                        String trimmed_display_text = removeExtras(display_text, "<rainbow>", rainbow_string, shadow_string);
                        int get_width = ren.getStringWidth(IconHandler.removeIconString(ChatHandler.addFormatting(trimmed_display_text)));
                        if (get_width > max_width) {
                            max_width = get_width;
                        }
                    }

                    display_texts.add(display_text);
                    display_xs.add(display_x);
                    display_ys.add(display_y);
                }

                if (display_texts.size() > 0) {
                    if (bg.equalsIgnoreCase("full")) {
                        int display_bg_x = floor((display_xy[0].floatValue() * width) / 100);
                        int display_bg_y = floor(display_ys.get(0));
                        int display_bg_h = getDisplayHight(display_ys.get(display_ys.size()-1), up);
                        drawDisplayBackgroundFull(display_bg_x, display_bg_y, display_bg_h, bgc, max_width, up, align);
                    }
                    drawDisplay(display_texts, display_xs, display_ys, bg, bgc);
                }
            }
        }
    }

    private static void drawDisplayBackgroundFull(int bg_x, int bg_y, int bg_h, long bgc, int max_width, Boolean up, int align) {
        if (align == 2) max_width = -max_width;
        if (align == 1) {
            if (up) drawRect(bg_x - max_width/2, bg_y + 10, bg_x + max_width/2, bg_h, (int) bgc);
            else drawRect(bg_x - max_width/2, bg_y, bg_x + max_width/2, bg_h, (int) bgc);
        } else {
            if (up) drawRect(bg_x, bg_y + 10, bg_x + max_width, bg_h, (int) bgc);
            else drawRect(bg_x, bg_y, bg_x + max_width, bg_h, (int) bgc);
        }
    }

    private static void drawDisplay(List<String> display_texts, List<Float> display_xs, List<Float> display_ys, String bg, long bgc) {
        FontRenderer ren = Minecraft.getMinecraft().fontRendererObj;

        for (int i=0; i<display_texts.size(); i++) {
            String display_text = display_texts.get(i);
            float display_x = display_xs.get(i);
            float display_y = display_ys.get(i);

            int color;
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

            Boolean shadow = true;
            if (display_text.contains("<shadow=") && display_text.contains(">")) {
                String shadow_string = display_text.substring(display_text.indexOf("<shadow=")+8, display_text.indexOf(">", display_text.indexOf("<shadow=")));
                display_text = display_text.replace("<shadow="+shadow_string+">", "");
                shadow = shadow_string.equals("true");
            }

            if (!display_text.equals("") && bg.equalsIgnoreCase("line")) {
                drawRect(display_x, display_y, display_x + ren.getStringWidth(IconHandler.removeIconString(ChatHandler.addFormatting(display_text))), display_y + 10, (int) bgc);
            }

            display_text = IconHandler.drawIcons(display_text, floor(display_x), floor(display_y));
            ren.drawString(display_text, display_x, display_y, color, shadow);
        }
    }

    private static int getDisplayHight(float display_y, Boolean up) {
        if (up) {
            return floor(display_y);
        }
        return floor(display_y)+10;
    }

    private static float getSpacing() {
        if (display_text.contains("<spacing=") && display_text.contains(">")) {
            String spacing_string = display_text.substring(display_text.indexOf("<spacing=")+9, display_text.indexOf(">", display_text.indexOf("<spacing=")));
            try {
                return Float.parseFloat(spacing_string);
            } catch (NumberFormatException e) {
                System.out.println("<spacing=$n> - $n must be a number!");
            }
            display_text = display_text.replace("<spacing="+spacing_string+">", "");
        }
        return 1;
    }

    private static String getRainbow() {
        if (display_text.contains("<rainbow=") && display_text.contains(">")) {
            return display_text.substring(display_text.indexOf("<rainbow="), display_text.indexOf(">", display_text.indexOf("<rainbow="))+1);
        }
        return "";
    }

    private static String getShadow() {
        if (display_text.contains("<shadow=") && display_text.contains(">")) {
            return display_text.substring(display_text.indexOf("<shadow="), display_text.indexOf(">", display_text.indexOf("<shadow="))+1);
        }
        return "";
    }

    private static String removeStringExtras(String in) {
        return in.replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ",")
                .replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(")
                .replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")")
                .replace("stringOpenBracketReplacementF6cyUQp9stringOpenBracketReplacement", "(")
                .replace("stringCloseBracketReplacementF6cyUQp9stringCloseBracketReplacement", ")")
                .replace("AmpF6cyUQp9Amp","&")
                .replace("TripleDotF6cyUQp9TripleDot","...")
                .replace("BackslashF6cyUQp9Backslash","\\\\")
                .replace("NewLineF6cyUQp9NewLine","\\n")
                .replace("SingleQuoteF6cyUQp9SingleQuote","\\'");
    }

    private static String removeExtras(String in, String... replace) {
        for (String string : replace) {
            in = in.replace(string, "");
        }
        return in;
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
        worldrenderer.pos(right-2, bottom-1, 0.0D).endVertex();
        worldrenderer.pos(right-2, top-1, 0.0D).endVertex();
        worldrenderer.pos(left+1, top-1, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
