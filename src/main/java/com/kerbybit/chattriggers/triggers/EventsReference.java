package com.kerbybit.chattriggers.triggers;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.global;

class EventsReference {
    static String calculateLogic(String logic) {
        //nested logic
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
            try {
                if (split[i].equals("<")) {
                    String beforeBefore = "";
                    Double before;
                    String afterAfter = "";
                    Double after;
                    if (split[i - 1].trim().contains("logicF6cyUQp9logic")) {
                        before = Double.parseDouble(split[i-1].trim().substring(split[i-1].trim().lastIndexOf("logicF6cyUQp9logic")+18).trim());
                        beforeBefore = split[i-1].trim().substring(0, split[i-1].trim().lastIndexOf("logicF6cyUQp9logic")+18);
                    } else {before = Double.parseDouble(split[i - 1].trim());}
                    if (split[i+1].trim().contains("logicF6cyUQp9logic")) {
                        after = Double.parseDouble(split[i+1].trim().substring(0, split[i+1].trim().indexOf("logicF6cyUQp9logic")).trim());
                        afterAfter = split[i+1].trim().substring(split[i+1].trim().indexOf("logicF6cyUQp9logic"));
                    } else {after = Double.parseDouble(split[i+1].trim());}

                    if (before < after) {
                        split[i-1] = "";
                        split[i] = "";
                        split[i+1] = beforeBefore+"true"+afterAfter;
                    } else {
                        split[i-1] = "";
                        split[i] = "";
                        split[i+1] = beforeBefore+"false"+afterAfter;
                    }
                } else if (split[i].equals(">")) {
                    String beforeBefore = "";
                    Double before;
                    String afterAfter = "";
                    Double after;
                    if (split[i - 1].trim().contains("logicF6cyUQp9logic")) {
                        before = Double.parseDouble(split[i-1].trim().substring(split[i-1].trim().lastIndexOf("logicF6cyUQp9logic")+18).trim());
                        beforeBefore = split[i-1].trim().substring(0, split[i-1].trim().lastIndexOf("logicF6cyUQp9logic")+18);
                    } else {before = Double.parseDouble(split[i - 1].trim());}
                    if (split[i+1].trim().contains("logicF6cyUQp9logic")) {
                        after = Double.parseDouble(split[i+1].trim().substring(0, split[i+1].trim().indexOf("logicF6cyUQp9logic")).trim());
                        afterAfter = split[i+1].trim().substring(split[i+1].trim().indexOf("logicF6cyUQp9logic"));
                    } else {after = Double.parseDouble(split[i+1].trim());}

                    if (before > after) {
                        split[i-1] = "";
                        split[i] = "";
                        split[i+1] = beforeBefore+"true"+afterAfter;
                    } else {
                        split[i-1] = "";
                        split[i] = "";
                        split[i+1] = beforeBefore+"false"+afterAfter;
                    }
                } else if (split[i].equals("<=")) {
                    String beforeBefore = "";
                    Double before;
                    String afterAfter = "";
                    Double after;
                    if (split[i - 1].trim().contains("logicF6cyUQp9logic")) {
                        before = Double.parseDouble(split[i-1].trim().substring(split[i-1].trim().lastIndexOf("logicF6cyUQp9logic")+18).trim());
                        beforeBefore = split[i-1].trim().substring(0, split[i-1].trim().lastIndexOf("logicF6cyUQp9logic")+18);
                    } else {before = Double.parseDouble(split[i - 1].trim());}
                    if (split[i+1].trim().contains("logicF6cyUQp9logic")) {
                        after = Double.parseDouble(split[i+1].trim().substring(0, split[i+1].trim().indexOf("logicF6cyUQp9logic")).trim());
                        afterAfter = split[i+1].trim().substring(split[i+1].trim().indexOf("logicF6cyUQp9logic"));
                    } else {after = Double.parseDouble(split[i+1].trim());}

                    if (before <= after) {
                        split[i-1] = "";
                        split[i] = "";
                        split[i+1] = beforeBefore+"true"+afterAfter;
                    } else {
                        split[i-1] = "";
                        split[i] = "";
                        split[i+1] = beforeBefore+"false"+afterAfter;
                    }
                } else if (split[i].equals(">=")) {
                    String beforeBefore = "";
                    Double before;
                    String afterAfter = "";
                    Double after;
                    if (split[i - 1].trim().contains("logicF6cyUQp9logic")) {
                        before = Double.parseDouble(split[i-1].trim().substring(split[i-1].trim().lastIndexOf("logicF6cyUQp9logic")+18).trim());
                        beforeBefore = split[i-1].trim().substring(0, split[i-1].trim().lastIndexOf("logicF6cyUQp9logic")+18);
                    } else {before = Double.parseDouble(split[i - 1].trim());}
                    if (split[i+1].trim().contains("logicF6cyUQp9logic")) {
                        after = Double.parseDouble(split[i+1].trim().substring(0, split[i+1].trim().indexOf("logicF6cyUQp9logic")).trim());
                        afterAfter = split[i+1].trim().substring(split[i+1].trim().indexOf("logicF6cyUQp9logic"));
                    } else {after = Double.parseDouble(split[i+1].trim());}

                    if (before >= after) {
                        split[i-1] = "";
                        split[i] = "";
                        split[i+1] = beforeBefore+"true"+afterAfter;
                    } else {
                        split[i-1] = "";
                        split[i] = "";
                        split[i+1] = beforeBefore+"false"+afterAfter;
                    }
                } else if (split[i].equals("==")) {
                    String beforeBefore = "";
                    String before;
                    String afterAfter = "";
                    String after;
                    if (split[i - 1].trim().contains("logicF6cyUQp9logic")) {
                        before = split[i-1].trim().substring(split[i-1].trim().lastIndexOf("logicF6cyUQp9logic")+18).trim();
                        beforeBefore = split[i-1].trim().substring(0, split[i-1].trim().lastIndexOf("logicF6cyUQp9logic")+18);
                    } else {before = split[i - 1].trim();}
                    if (split[i+1].trim().contains("logicF6cyUQp9logic")) {
                        after = split[i+1].trim().substring(0, split[i+1].trim().indexOf("logicF6cyUQp9logic")).trim();
                        afterAfter = split[i+1].trim().substring(split[i+1].trim().indexOf("logicF6cyUQp9logic"));
                    } else {after = split[i+1].trim();}
                    if (before.equals(after)) {
                        split[i-1] = "";
                        split[i] = "";
                        split[i+1] = beforeBefore+"true"+afterAfter;
                    } else {
                        split[i-1] = "";
                        split[i] = "";
                        split[i+1] = beforeBefore+"false"+afterAfter;
                    }
                } else if (split[i].equals("!=")) {
                    String beforeBefore = "";
                    String before;
                    String afterAfter = "";
                    String after;
                    if (split[i - 1].trim().contains("logicF6cyUQp9logic")) {
                        before = split[i-1].trim().substring(split[i-1].trim().lastIndexOf("logicF6cyUQp9logic")+18).trim();
                        beforeBefore = split[i-1].trim().substring(0, split[i-1].trim().lastIndexOf("logicF6cyUQp9logic")+18);
                    } else {before = split[i - 1].trim();}
                    if (split[i+1].trim().contains("logicF6cyUQp9logic")) {
                        after = split[i+1].trim().substring(0, split[i+1].trim().indexOf("logicF6cyUQp9logic")).trim();
                        afterAfter = split[i+1].trim().substring(split[i+1].trim().indexOf("logicF6cyUQp9logic"));
                    } else {after = split[i+1].trim();}
                    if (before.equals(after)) {
                        split[i-1] = "";
                        split[i] = "";
                        split[i+1] = beforeBefore+"false"+afterAfter;
                    } else {
                        split[i-1] = "";
                        split[i] = "";
                        split[i+1] = beforeBefore+"true"+afterAfter;
                    }
                }
            } catch(NumberFormatException exception) {
                if (global.debug) {
                    ChatHandler.warn("red", "Error in logic: One of two numbers where not numbers -> " + split[i-1] + "-" + split[i+1]);
                }
            }
        }

        //rebuild logic
        StringBuilder logicBuilder = new StringBuilder();
        for (String value : split) {
            logicBuilder.append(value);
        }
        logic = logicBuilder.toString().trim();
        logic = logic.replace("!true", "false")
                .replace("!false", "true");



        //split logic
        split = logic.split("logicF6cyUQp9logic");
        for (int i=1; i<split.length-1; i++) {
            if (split[i].equals("&&")) {
                if (split[i-1].trim().equalsIgnoreCase("true")
                        && split[i+1].trim().equalsIgnoreCase("true")) {
                    split[i-1] = "";
                    split[i] = "";
                    split[i+1] = "true";
                } else {
                    split[i-1] = "";
                    split[i] = "";
                    split[i+1] = "false";
                }
            } else if (split[i].equals("||")) {
                if (split[i-1].trim().equalsIgnoreCase("true")
                        || split[i+1].trim().equalsIgnoreCase("true")) {
                    split[i-1] = "";
                    split[i] = "";
                    split[i+1] = "true";
                } else {
                    split[i-1] = "";
                    split[i] = "";
                    split[i+1] = "false";
                }
            } else if (split[i].equals("^")) {
                if (split[i-1].trim().equalsIgnoreCase("true")
                        ^ split[i+1].trim().equalsIgnoreCase("true")) {
                    split[i-1] = "";
                    split[i] = "";
                    split[i+1] = "true";
                } else {
                    split[i-1] = "";
                    split[i] = "";
                    split[i+1] = "false";
                }
            }
        }

        //rebuild logic
        logicBuilder = new StringBuilder();
        for (String value : split) {
            logicBuilder.append(value);
        }
        logic = logicBuilder.toString().trim();
        logic = logic.replace("!true", "false")
                .replace("!false", "true");

        return logic;
    }
}
