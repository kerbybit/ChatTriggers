package com.kerbybit.chattriggers.triggers;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.global;

import java.util.ArrayList;
import java.util.List;

public class NotifyHandler {
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
}
