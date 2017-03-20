package com.kerbybit.chattriggers.overlay;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.global;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class KillfeedHandler {
    private static Minecraft MC = Minecraft.getMinecraft();
    private static FontRenderer ren = MC.fontRendererObj;

    public static String addToKillfeed(String event, int delay) {
        if (global.settings.get(10).equalsIgnoreCase("FALSE")) {
            global.killfeed.add(event
                    .replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ",")
                    .replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(")
                    .replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")"));
            global.killfeed_history.add(event
                    .replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ",")
                    .replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(")
                    .replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")"));

            while (global.killfeed_history.size() > 100) {
                global.killfeed_history.remove(0);
            }

            global.killfeedDelay.add(delay);
            return "killfeed";
        } else {
            return "notify";
        }
    }

    public static void showKillfeedHistory(Boolean formatted) {
        if (global.killfeed_history.size() > 0) {
            for (String value : global.killfeed_history) {
                if (formatted) {
                    ChatHandler.warn(value);
                } else {
                    ChatHandler.warn(ChatHandler.ignoreFormatting(value));
                }
            }
        } else {
            ChatHandler.warn(ChatHandler.color("red", "No killfeed history to show"));
        }
    }

    public static void showKillfeedHistory() {
        showKillfeedHistory(false);
    }

    public static void drawKillfeed(RenderGameOverlayEvent event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            global.worldIsLoaded=true;
            ScaledResolution res = new ScaledResolution(MC);
            float width = res.getScaledWidth();
            float height = res.getScaledHeight();

            int x = (int)(global.killfeed_x * width);
            int y = (int)(global.killfeed_y * height);
            if (x < 0) {
                x = 0;
            }
            if (y < 0) {
                y = 0;
            }
            if (x > width) {
                x = (int) width;
            }
            if (y > height) {
                y = (int) height;
            }
            if (x < width / 3) {
                if (y < height / 2) {
                    for (int i = 0; i < global.killfeed.size(); i++) {
                        int col = 0xffffffff;
                        if (global.settings.get(9).equalsIgnoreCase("TRUE")) {
                            if (global.killfeedDelay.get(i) < 50) {
                                col = col - (50 - global.killfeedDelay.get(i)) * 0x05000000;
                            }
                        }
                        ren.drawStringWithShadow(global.killfeed.get(i), x, y + i * 10, col);
                    }
                } else {
                    for (int i = 0; i < global.killfeed.size(); i++) {
                        int col = 0xffffffff;
                        if (global.settings.get(9).equalsIgnoreCase("TRUE")) {
                            if (global.killfeedDelay.get(i) < 50) {
                                col = col - (50 - global.killfeedDelay.get(i)) * 0x05000000;
                            }
                        }
                        ren.drawStringWithShadow(global.killfeed.get(i), x, y - i * 10, col);
                    }
                }
            } else if (x > width - width/3) {
                if (y < height / 2) {
                    for (int i = 0; i < global.killfeed.size(); i++) {
                        int col = 0xffffffff;
                        if (global.settings.get(9).equalsIgnoreCase("TRUE")) {
                            if (global.killfeedDelay.get(i) < 50) {
                                col = col - (50 - global.killfeedDelay.get(i)) * 0x05000000;
                            }
                        }
                        ren.drawStringWithShadow(global.killfeed.get(i), x - ren.getStringWidth(global.killfeed.get(i)), y + i * 10, col);
                    }
                } else {
                    for (int i = 0; i < global.killfeed.size(); i++) {
                        int col = 0xffffffff;
                        if (global.settings.get(9).equalsIgnoreCase("TRUE")) {
                            if (global.killfeedDelay.get(i) < 50) {
                                col = col - (50 - global.killfeedDelay.get(i)) * 0x05000000;
                            }
                        }
                        ren.drawStringWithShadow(global.killfeed.get(i), x - ren.getStringWidth(global.killfeed.get(i)), y - i * 10, col);
                    }
                }
            } else {
                if (y < height / 2) {
                    for (int i = 0; i < global.killfeed.size(); i++) {
                        int col = 0xffffffff;
                        if (global.settings.get(9).equalsIgnoreCase("TRUE")) {
                            if (global.killfeedDelay.get(i) < 50) {
                                col = col - (50 - global.killfeedDelay.get(i)) * 0x05000000;
                            }
                        }
                        ren.drawStringWithShadow(global.killfeed.get(i), x - ren.getStringWidth(global.killfeed.get(i))/2, y + i * 10, col);
                    }
                } else {
                    for (int i = 0; i < global.killfeed.size(); i++) {
                        int col = 0xffffffff;
                        if (global.settings.get(9).equalsIgnoreCase("TRUE")) {
                            if (global.killfeedDelay.get(i) < 50) {
                                col = col - (50 - global.killfeedDelay.get(i)) * 0x05000000;
                            }
                        }
                        ren.drawStringWithShadow(global.killfeed.get(i), x - ren.getStringWidth(global.killfeed.get(i))/2, y - i * 10, col);
                    }
                }
            }
        }
    }

    public static void tickKillfeed() {
        for (int i=0; i<global.killfeedDelay.size(); i++) {
            if (global.killfeedDelay.get(i) == 0) {
                global.killfeed.remove(i);
                global.killfeedDelay.remove(i);
            } else {
                global.killfeedDelay.set(i, global.killfeedDelay.get(i) - 1);
            }
        }
    }
}
