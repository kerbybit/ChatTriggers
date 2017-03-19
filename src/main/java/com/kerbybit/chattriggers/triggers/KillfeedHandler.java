package com.kerbybit.chattriggers.triggers;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.global;

public class KillfeedHandler {
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
}
