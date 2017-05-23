package com.kerbybit.chattriggers.overlay;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.Settings;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.objects.DisplayHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import static net.minecraft.realms.RealmsMth.floor;

public class KillfeedHandler {
    private static Minecraft MC = Minecraft.getMinecraft();
    private static FontRenderer ren = MC.fontRendererObj;

    public static String addToKillfeed(String event, int delay) {
        if (Settings.killfeedInNotify) {
            return "notify";
        } else {
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
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            global.worldIsLoaded=true;
            ScaledResolution res = new ScaledResolution(MC);
            float width = res.getScaledWidth();
            float height = res.getScaledHeight();

            int x = (int)(Settings.killfeedPosition[0] * width);
            int y = (int)(Settings.killfeedPosition[1] * height);
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
                        int bgcol = 0x40000000;
                        if (Settings.killfeedFade) {
                            if (global.killfeedDelay.get(i) < 50) {
                                col = col - (50 - global.killfeedDelay.get(i)) * 0x05000000;
                                bgcol = bgcol - (50 - global.killfeedDelay.get(i)) * 0x01000000;
                            }
                        }
                        if (Settings.killfeedBackground) {
                            DisplayHandler.drawRect(x, y+i*10, x+ren.getStringWidth(global.killfeed.get(i)), (y+i*10)+10, bgcol);
                        }
                        GlStateManager.enableBlend();
                        ren.drawStringWithShadow(global.killfeed.get(i), x, y + i * 10, col);
                    }
                } else {
                    for (int i = 0; i < global.killfeed.size(); i++) {
                        int col = 0xffffffff;
                        int bgcol = 0x40000000;
                        if (Settings.killfeedFade) {
                            if (global.killfeedDelay.get(i) < 50) {
                                col = col - (50 - global.killfeedDelay.get(i)) * 0x05000000;
                                bgcol = bgcol - (50 - global.killfeedDelay.get(i)) * 0x01000000;
                            }
                        }
                        if (Settings.killfeedBackground) {
                            DisplayHandler.drawRect(x, y-i*10, x+ren.getStringWidth(global.killfeed.get(i)), (y-i*10)+10, bgcol);
                        }
                        GlStateManager.enableBlend();
                        ren.drawStringWithShadow(global.killfeed.get(i), x, y - i * 10, col);
                    }
                }
            } else if (x > width - width/3) {
                if (y < height / 2) {
                    for (int i = 0; i < global.killfeed.size(); i++) {
                        int col = 0xffffffff;
                        int bgcol = 0x40000000;
                        if (Settings.killfeedFade) {
                            if (global.killfeedDelay.get(i) < 50) {
                                col = col - (50 - global.killfeedDelay.get(i)) * 0x05000000;
                                bgcol = bgcol - (50 - global.killfeedDelay.get(i)) * 0x01000000;
                            }
                        }
                        if (Settings.killfeedBackground) {
                            DisplayHandler.drawRect((x-ren.getStringWidth(global.killfeed.get(i))), y+i*10, (x-ren.getStringWidth(global.killfeed.get(i)))+ren.getStringWidth(global.killfeed.get(i)), (y+i*10)+10, bgcol);
                        }
                        GlStateManager.enableBlend();
                        ren.drawStringWithShadow(global.killfeed.get(i), x - ren.getStringWidth(global.killfeed.get(i)), y + i * 10, col);
                    }
                } else {
                    for (int i = 0; i < global.killfeed.size(); i++) {
                        int col = 0xffffffff;
                        int bgcol = 0x40000000;
                        if (Settings.killfeedFade) {
                            if (global.killfeedDelay.get(i) < 50) {
                                col = col - (50 - global.killfeedDelay.get(i)) * 0x05000000;
                                bgcol = bgcol - (50 - global.killfeedDelay.get(i)) * 0x01000000;
                            }
                        }
                        if (Settings.killfeedBackground) {
                            DisplayHandler.drawRect((x-ren.getStringWidth(global.killfeed.get(i))), y-i*10, (x-ren.getStringWidth(global.killfeed.get(i)))+ren.getStringWidth(global.killfeed.get(i)), (y-i*10)+10, bgcol);
                        }
                        GlStateManager.enableBlend();
                        ren.drawStringWithShadow(global.killfeed.get(i), x - ren.getStringWidth(global.killfeed.get(i)), y - i * 10, col);
                    }
                }
            } else {
                if (y < height / 2) {
                    for (int i = 0; i < global.killfeed.size(); i++) {
                        int col = 0xffffffff;
                        int bgcol = 0x40000000;
                        if (Settings.killfeedFade) {
                            if (global.killfeedDelay.get(i) < 50) {
                                col = col - (50 - global.killfeedDelay.get(i)) * 0x05000000;
                                bgcol = bgcol - (50 - global.killfeedDelay.get(i)) * 0x01000000;
                            }
                        }
                        if (Settings.killfeedBackground) {
                            DisplayHandler.drawRect((x-ren.getStringWidth(global.killfeed.get(i))/2), y+i*10, (x-ren.getStringWidth(global.killfeed.get(i))/2)+ren.getStringWidth(global.killfeed.get(i)), (y+i*10)+10, bgcol);
                        }
                        GlStateManager.enableBlend();
                        ren.drawStringWithShadow(global.killfeed.get(i), x - ren.getStringWidth(global.killfeed.get(i))/2, y + i * 10, col);
                    }
                } else {
                    for (int i = 0; i < global.killfeed.size(); i++) {
                        int col = 0xffffffff;
                        int bgcol = 0x40000000;
                        if (Settings.killfeedFade) {
                            if (global.killfeedDelay.get(i) < 50) {
                                col = col - (50 - global.killfeedDelay.get(i)) * 0x05000000;
                                bgcol = bgcol - (50 - global.killfeedDelay.get(i)) * 0x01000000;
                            }
                        }
                        if (Settings.killfeedBackground) {
                            DisplayHandler.drawRect((x-ren.getStringWidth(global.killfeed.get(i))/2), y-i*10, (x-ren.getStringWidth(global.killfeed.get(i))/2)+ren.getStringWidth(global.killfeed.get(i)), (y-i*10)+10, bgcol);
                        }
                        GlStateManager.enableBlend();
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
