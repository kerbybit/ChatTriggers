package com.kerbybit.chattriggers.triggers;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.global;

class EventsReference {
    private static Double before;
    private static String beforeString;
    private static String beforeBefore;
    private static Double after;
    private static String afterString;
    private static String afterAfter;

    private static void getBeforeAndAfter(String last, String next) {
        try {
            beforeBefore = "";
            afterAfter = "";
            if (last.trim().contains("logicF6cyUQp9logic")) {
                before = Double.parseDouble(last.trim().substring(last.trim().lastIndexOf("logicF6cyUQp9logic") + 18).trim());
                beforeBefore = last.trim().substring(0, last.trim().lastIndexOf("logicF6cyUQp9logic") + 18);
            } else {
                before = Double.parseDouble(last.trim());
            }
            if (next.trim().contains("logicF6cyUQp9logic")) {
                after = Double.parseDouble(next.trim().substring(0, next.trim().indexOf("logicF6cyUQp9logic")).trim());
                afterAfter = next.trim().substring(next.trim().indexOf("logicF6cyUQp9logic"));
            } else {
                after = Double.parseDouble(next.trim());
            }
        } catch (NumberFormatException exception) {
            if (global.debug) {
                ChatHandler.warn("red", "Error in logic: One of two numbers where not numbers -> " + last + "-" + next);
            }
        }
    }

    private static void getBeforeAndAfterString(String last, String next) {
        beforeBefore = "";
        afterAfter = "";
        if (last.trim().contains("logicF6cyUQp9logic")) {
            beforeString = last.trim().substring(last.trim().lastIndexOf("logicF6cyUQp9logic")+18).trim();
            beforeBefore = last.trim().substring(0, last.trim().lastIndexOf("logicF6cyUQp9logic")+18);
        } else {beforeString = last.trim();}
        if (next.trim().contains("logicF6cyUQp9logic")) {
            afterString = next.trim().substring(0, next.trim().indexOf("logicF6cyUQp9logic")).trim();
            afterAfter = next.trim().substring(next.trim().indexOf("logicF6cyUQp9logic"));
        } else {afterString = next.trim();}
    }

    private static String rebuildLogic(String[] split) {
        StringBuilder logicBuilder = new StringBuilder();
        for (String value : split) {
            logicBuilder.append(value);
        }
        return logicBuilder.toString().trim()
                .replace("!true", "false")
                .replace("!false", "true");
    }

    private static String tieredLogic(String logic) {
        while (logic.contains("(") && logic.contains(")")) {
            String in = logic.substring(logic.indexOf("(")+1, logic.indexOf(")", logic.indexOf("(")));
            String search = logic.substring(logic.indexOf("(")+1);
            while (in.contains("(")) {
                search = search.replaceFirst("\\(", "tempOpenBracketF6cyUQp9tempOpenBracket")
                        .replaceFirst("\\)", "tempCloseBreacketF6cyUQp9tempCloseBracket");
                in = search.substring(0, search.indexOf(")"));
            }
            in = in.replace("tempOpenBracketF6cyUQp9tempOpenBracket", "(")
                    .replace("tempCloseBreacketF6cyUQp9tempCloseBracket", ")");
            logic = logic.replace("("+in+")", calculateLogic(in));
        }
        return logic;
    }

    static String calculateLogic(String logic) {
        //tiered logic
        logic = tieredLogic(logic);

        //logic
        //  <  >  <= >= ==
        //  && || ^

        //add spacing for split
        logic = logic.replace("<", "compareF6cyUQp9compare<compareF6cyUQp9compare")
                .replace(">", "compareF6cyUQp9compare>compareF6cyUQp9compare")
                .replace("<=", "compareF6cyUQp9compare<=compareF6cyUQp9compare")
                .replace(">=", "compareF6cyUQp9compare>=compareF6cyUQp9compare")
                .replace("==", "compareF6cyUQp9compare==compareF6cyUQp9compare")
                .replace("!=", "compareF6cyUQp9compare!=compareF6cyUQp9compare")

                .replace("&&", "logicF6cyUQp9logic&&logicF6cyUQp9logic")
                .replace("||", "logicF6cyUQp9logic||logicF6cyUQp9logic")
                .replace("^", "logicF6cyUQp9logic^logicF6cyUQp9logic")
                .replace("!true", "false")
                .replace("!false", "true");

        //split logic
        String[] split = logic.split("compareF6cyUQp9compare");
        for (int i=1; i<split.length-1; i++) {
            if (split[i].equals("<")) {
                getBeforeAndAfter(split[i-1], split[i+1]);

                if (before < after) {
                    split[i-1] = ""; split[i] = "";
                    split[i+1] = beforeBefore+"true"+afterAfter;
                } else {
                    split[i-1] = ""; split[i] = "";
                    split[i+1] = beforeBefore+"false"+afterAfter;
                }
            } else if (split[i].equals(">")) {
                getBeforeAndAfter(split[i-1], split[i+1]);

                if (before > after) {
                    split[i-1] = ""; split[i] = "";
                    split[i+1] = beforeBefore+"true"+afterAfter;
                } else {
                    split[i-1] = ""; split[i] = "";
                    split[i+1] = beforeBefore+"false"+afterAfter;
                }
            } else if (split[i].equals("<=")) {
                getBeforeAndAfter(split[i-1], split[i+1]);

                if (before <= after) {
                    split[i-1] = ""; split[i] = "";
                    split[i+1] = beforeBefore+"true"+afterAfter;
                } else {
                    split[i-1] = ""; split[i] = "";
                    split[i+1] = beforeBefore+"false"+afterAfter;
                }
            } else if (split[i].equals(">=")) {
                getBeforeAndAfter(split[i-1], split[i+1]);

                if (before >= after) {
                    split[i-1] = ""; split[i] = "";
                    split[i+1] = beforeBefore+"true"+afterAfter;
                } else {
                    split[i-1] = ""; split[i] = "";
                    split[i+1] = beforeBefore+"false"+afterAfter;
                }
            } else if (split[i].equals("==")) {
                getBeforeAndAfterString(split[i-1], split[i+1]);

                if (beforeString.equals(afterString)) {
                    split[i-1] = ""; split[i] = "";
                    split[i+1] = beforeBefore+"true"+afterAfter;
                } else {
                    split[i-1] = ""; split[i] = "";
                    split[i+1] = beforeBefore+"false"+afterAfter;
                }
            } else if (split[i].equals("!=")) {
                getBeforeAndAfterString(split[i-1], split[i+1]);

                if (beforeString.equals(afterString)) {
                    split[i-1] = ""; split[i] = "";
                    split[i+1] = beforeBefore+"false"+afterAfter;
                } else {
                    split[i-1] = ""; split[i] = "";
                    split[i+1] = beforeBefore+"true"+afterAfter;
                }
            }
        }

        //rebuild logic
        logic = rebuildLogic(split);

        //split logic
        split = logic.split("logicF6cyUQp9logic");
        for (int i=1; i<split.length-1; i++) {
            if (split[i].equals("&&")) {
                if (split[i-1].trim().equalsIgnoreCase("true")
                        && split[i+1].trim().equalsIgnoreCase("true")) {
                    split[i-1] = ""; split[i] = "";
                    split[i+1] = "true";
                } else {
                    split[i-1] = ""; split[i] = "";
                    split[i+1] = "false";
                }
            } else if (split[i].equals("||")) {
                if (split[i-1].trim().equalsIgnoreCase("true")
                        || split[i+1].trim().equalsIgnoreCase("true")) {
                    split[i-1] = ""; split[i] = "";
                    split[i+1] = "true";
                } else {
                    split[i-1] = ""; split[i] = "";
                    split[i+1] = "false";
                }
            } else if (split[i].equals("^")) {
                if (split[i-1].trim().equalsIgnoreCase("true")
                        ^ split[i+1].trim().equalsIgnoreCase("true")) {
                    split[i-1] = "";  split[i] = "";
                    split[i+1] = "true";
                } else {
                    split[i-1] = ""; split[i] = "";
                    split[i+1] = "false";
                }
            }
        }

        //rebuild logic
        logic = rebuildLogic(split);

        return logic;
    }
}
