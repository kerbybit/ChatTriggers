package com.kerbybit.chattriggers.overlay;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.global;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.ArrayList;
import java.util.List;

public class NotifyHandler {
    private static long sysTime = Minecraft.getSystemTime();
    private static Minecraft MC = Minecraft.getMinecraft();
    private static FontRenderer ren = MC.fontRendererObj;

    public static void addToNotify(String event, int delay, int position) {
        global.notify.add(event
                .replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ",")
                .replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(")
                .replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")"));
        global.notify_history.add(event
                .replace("stringCommaReplacementF6cyUQp9stringCommaReplacement", ",")
                .replace("stringOpenBracketF6cyUQp9stringOpenBracket", "(")
                .replace("stringCloseBracketF6cyUQp9stringCloseBracket", ")"));

        while (global.notify_history.size() > 100) {
            global.notify_history.remove(0);
        }

        List<Float> temp_list = new ArrayList<Float>();
        temp_list.add((float) 0);temp_list.add((float) -1000);
        temp_list.add((float) position);temp_list.add((float) delay);
        temp_list.add((float) 0);temp_list.add((float) -1000);
        global.notifyAnimation.add(temp_list);
        global.notifySize++;
    }

    public static void showNotifyHistory(Boolean formatted) {
        if (global.notify_history.size() > 0) {
            for (String value : global.notify_history) {
                if (formatted) {
                    ChatHandler.warn(value);
                } else {
                    ChatHandler.warn(ChatHandler.ignoreFormatting(value));
                }
            }
        } else {
            ChatHandler.warn(ChatHandler.color("red", "No notify history to show"));
        }
    }

    public static void showNotifyHistory() {
        showNotifyHistory(false);
    }

    public static void drawNotify(RenderGameOverlayEvent event) {
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            for (int i=0; i<global.notify.size(); i++) {
                ren.drawStringWithShadow(global.notify.get(i), global.notifyAnimation.get(i).get(1), global.notifyAnimation.get(i).get(2), 0xffffff);
            }
        }

        while (Minecraft.getSystemTime() >= sysTime + 6L) {
            sysTime += 6L;
            tickNotify();
        }

    }

    private static void tickNotify() {
        Minecraft MC = Minecraft.getMinecraft();
        FontRenderer ren = MC.fontRendererObj;
        ScaledResolution res = new ScaledResolution(MC);
        float width = res.getScaledWidth();
        float height = res.getScaledHeight();
        for (int i=0; i<global.notify.size(); i++) {
            if (global.notifyAnimation.get(i).get(0)==0) {
                global.notifyAnimation.get(i).set(4, width - ren.getStringWidth(global.notify.get(i)) - 5);
                global.notifyAnimation.get(i).set(5, width);
                float var7 = height-50-(global.notifyAnimation.get(i).get(2)*10);
                global.notifyAnimation.get(i).set(1, width);
                global.notifyAnimation.get(i).set(2, var7);
                global.notifyAnimation.get(i).set(0, (float) 1);
            } else if (global.notifyAnimation.get(i).get(0)==1) {
                if (Math.floor(global.notifyAnimation.get(i).get(1)) > global.notifyAnimation.get(i).get(4)) {
                    global.notifyAnimation.get(i).set(1, global.notifyAnimation.get(i).get(1) + (global.notifyAnimation.get(i).get(4)-global.notifyAnimation.get(i).get(1))/(global.settingsNotificationSpeed*3));
                } else {global.notifyAnimation.get(i).set(0, (float) 2);}
            } else if (global.notifyAnimation.get(i).get(0)==2) {
                if (global.notifyAnimation.get(i).get(3)>0) {
                    global.notifyAnimation.get(i).set(3, global.notifyAnimation.get(i).get(3)-1);
                } else {global.notifyAnimation.get(i).set(0, (float) 3);}
            } else if (global.notifyAnimation.get(i).get(0)==3) {
                if (global.notifyAnimation.get(i).get(1) < global.notifyAnimation.get(i).get(5)) {
                    global.notifyAnimation.get(i).set(1, global.notifyAnimation.get(i).get(1) - (global.notifyAnimation.get(i).get(4)-global.notifyAnimation.get(i).get(1))/(global.settingsNotificationSpeed*3));
                } else {
                    if (global.notifyAnimation.get(i).get(2) == height-50 || global.notify.size()==1) {global.notifySize = 0;}
                    global.notifyAnimation.remove(i);
                    global.notify.remove(i);
                }
            }
        }
    }
}
