package com.kerbybit.chattriggers.triggers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.global;

import com.kerbybit.chattriggers.objects.DisplayHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class TriggerHandler {
    public static void onChat(String fmsg, String msg, ClientChatReceivedEvent e) {
        onChat(fmsg, msg, e, false);
    }

    public static void onChat(String fmsg, String msg, ClientChatReceivedEvent e, Boolean isActionbar) {
        msg = msg.replace("\n", "\\n");
        fmsg = fmsg.replace("\n", "\\n");
        String msgNOEDIT = msg;

        //debug chat
        if (global.debugChat && !isActionbar) {
            String tmp_out = ChatHandler.removeFormatting(fmsg);
            global.copyText.add(tmp_out);
            tmp_out = tmp_out.replace("'", "\\'");
            List<String> TMP_eventout = new ArrayList<>();
            TMP_eventout.add("text:'" + tmp_out + "',clickEvent:{action:'run_command',value:'/t copy CopyFromDebugChat " + (global.copyText.size()-1) + "'},hoverEvent:{action:'show_text',value:'Click to copy\n" + tmp_out + "'}");
            ChatHandler.sendJson(TMP_eventout);
        }

        //get trigger list if chat or actionbar
        List<List<String>> temp;
        if (isActionbar) temp = new ArrayList<>(global.actionTrigger);
        else temp = new ArrayList<>(global.chatTrigger);

        for (List<String> trigger : temp) {
            //setup
            String TMP_trig = trigger.get(1);
            String TMP_w;
            String[] TMP_server;
            String current_server;
            Boolean correct_server = false;
            Boolean TMP_formatted;

            //tags
            TMP_w = TagHandler.getChatPosition(TMP_trig);
            TMP_formatted = TagHandler.getIsFormatted(TMP_trig);
            TMP_server = TagHandler.getServer(TMP_trig);
            TMP_trig = TagHandler.removeTags(TMP_trig);


            //check server stuff
            if (Minecraft.getMinecraft().isSingleplayer()) {
                current_server = "SinglePlayer";
            } else {
                try {
                    current_server = Minecraft.getMinecraft().getCurrentServerData().serverIP;
                } catch (Exception exception) {
                    current_server = "SinglePlayer";
                    //Catch for ReplayMod//
                }
            }

            for (String value : TMP_server) {
                if (current_server.toUpperCase().contains(value.toUpperCase())) {
                    correct_server = true;
                }
            }
            if (TMP_server.length == 0) {
                correct_server = true;
            }

            //check if formatted or nah
            if (TMP_trig.contains("&")) {
                TMP_formatted = true;
            }

            if (TMP_formatted) {
                msg = fmsg;
                msg = ChatHandler.removeFormatting(msg);
            } else {
                msg = msgNOEDIT;
            }

            TMP_trig = getTrigStrings(TMP_trig);


            if (correct_server) {
                TMP_trig = setTrigStrings(msg, TMP_trig);

                switch (TMP_w) {
                    case ("s"):
                        if (msg.startsWith(TMP_trig))
                            doEvents(trigger, e);
                        break;
                    case("c"):
                        if (msg.contains(TMP_trig))
                            doEvents(trigger, e);
                        break;
                    case("e"):
                        if (msg.endsWith(TMP_trig))
                            doEvents(trigger, e);
                        break;
                    default:
                        if (msg.equals(TMP_trig))
                            doEvents(trigger, e);
                        break;
                }
                clearTemporary();
            }
        }
    }

    private static String setTrigStrings(String msg, String TMP_trig) {
        try {
            return StringHandler.setStrings(msg, TMP_trig);
        } catch (Exception e1) {
            e1.printStackTrace();
            ChatHandler.warn(ChatHandler.color("red", "There was a problem setting strings!"));
        }

        return TMP_trig;
    }

    private static String getTrigStrings(String TMP_trig) {
        //read strings
        while (TMP_trig.contains("{string<") && TMP_trig.contains(">}")) {
            Boolean  isString = false;
            String TMP_sn = TMP_trig.substring(TMP_trig.indexOf("{string<") + 8, TMP_trig.indexOf(">}"));
            for (int j = 0; j < global.USR_string.size(); j++) {
                if (global.USR_string.get(j).get(0).equals(TMP_sn)) {
                    String TMP_s = global.USR_string.get(j).get(1);
                    TMP_trig = TMP_trig.replace("{string<" + TMP_sn + ">}", TMP_s);
                    isString = true;
                }
            }
            if (!isString) {
                TMP_trig = TMP_trig.replace("{string<" + TMP_sn + ">}", "not a string!");
            }
        }
        return TMP_trig.replace("{me}", Minecraft.getMinecraft().thePlayer.getDisplayNameString());
    }

    private static void doEvents(List<String> trigger, ClientChatReceivedEvent e) {
        //add all events to temp list
        List<String> TMP_events = new ArrayList<>();
        for (int j = 2; j < trigger.size(); j++) {
            TMP_events.add(trigger.get(j));
        }

        //do events
        if (global.temporary_replace.size() == 0) {
            EventsHandler.doEvents(TMP_events, e);
        } else {
            EventsHandler.doEvents(TMP_events, e, global.temporary_replace.toArray(new String[global.temporary_replace.size()]), global.temporary_replacement.toArray(new String[global.temporary_replacement.size()]));
            clearTemporary();
        }
    }

    private static void clearTemporary() {
        global.temporary_replace.clear();
        global.temporary_replacement.clear();
    }

	public static void onChat(ClientChatReceivedEvent e) {
        if (e.type != 2) {
            //onChat
            for (int i=0; i<global.onChatTrigger.size(); i++) {
                //add all events to temp list
                List<String> TMP_events = new ArrayList<>();
                for (int j=2; j<global.onChatTrigger.get(i).size(); j++) {TMP_events.add(global.onChatTrigger.get(i).get(j));}

                //do events
                EventsHandler.doEvents(TMP_events, e);
            }

            //chat
            String msg = e.message.getUnformattedText();
            String fmsg = e.message.getFormattedText();
            if (global.chatHistory.size() >= 1) {
                if (!global.chatHistory.get(global.chatHistory.size()-1).equals(ChatHandler.removeFormatting(fmsg))) {
                    global.chatHistory.add(ChatHandler.removeFormatting(fmsg));
                }
            } else global.chatHistory.add(ChatHandler.removeFormatting(fmsg));
            if (global.chatHistory.size()>100) global.chatHistory.remove(0);

            onChat(fmsg, msg, e);
        } else {
            String msg = e.message.getUnformattedText();
            String fmsg = e.message.getFormattedText();

            if (global.actionHistory.size() >= 1) {
                if (!global.actionHistory.get(global.actionHistory.size()-1).equals(ChatHandler.removeFormatting(fmsg))) {
                    global.actionHistory.add(ChatHandler.removeFormatting(fmsg));
                }
            } else global.actionHistory.add(ChatHandler.removeFormatting(fmsg));
            if (global.actionHistory.size() > 100) global.actionHistory.remove(0);

            onChat(fmsg, msg, e, true);
        }
	}
	
	public static void onClientTickTriggers() {
		if (global.worldIsLoaded) {
			for (int i=0; i<global.tickTrigger.size(); i++) {
				if (global.ticksElapsed % global.tickTriggerTime.get(i) == 0) {
					//add all events to temp list
					List<String> TMP_events = new ArrayList<>();
					for (int j=2; j<global.tickTrigger.get(i).size(); j++) {TMP_events.add(global.tickTrigger.get(i).get(j));}
					
					//do events
					EventsHandler.doEvents(TMP_events, null);
				}
			}
		}
	}

	public static void onSoundPlay(PlaySoundEvent e) {
        for (int i=0; i<global.onSoundPlayTrigger.size(); i++) {
            //add all events to temp list
            List<String> TMP_events = new ArrayList<>();
            for (int j=2; j<global.onSoundPlayTrigger.get(i).size(); j++) {
                String toAdd = global.onSoundPlayTrigger.get(i).get(j);
                TMP_events.add(toAdd);
            }

            //do events
            String[] extraStrings = new String[]{"{soundName}", "{soundCategory}"};
            String[] extraStringValues = new String[]{e.sound.getSoundLocation().getResourcePath(),
                    e.category.getCategoryName()};
            EventsHandler.doEvents(TMP_events, e, extraStrings, extraStringValues);
        }
    }
	
	public static void onRightClickPlayer(EntityInteractEvent e) {
		for (int i=0; i<global.onRightClickPlayerTrigger.size(); i++) {
			//add all events to temp list
			List<String> TMP_events = new ArrayList<>();
			for (int j=2; j<global.onRightClickPlayerTrigger.get(i).size(); j++) {TMP_events.add(global.onRightClickPlayerTrigger.get(i).get(j).replace("{player}", e.target.getName()));}
			
			//do events
			EventsHandler.doEvents(TMP_events, null);
		}
	}
	
	public static void worldLoadTriggers() {
		if (global.worldLoaded) {
            DisplayHandler.clearDisplays();
			for (int i=0; i<global.onWorldFirstLoadTrigger.size(); i++) {
				if (global.worldFirstLoad) {
					//add all events to temp list
					List<String> TMP_events = new ArrayList<>();
					for (int j=2; j<global.onWorldFirstLoadTrigger.get(i).size(); j++) {TMP_events.add(global.onWorldFirstLoadTrigger.get(i).get(j));}
					
					//do events
					EventsHandler.doEvents(TMP_events, null);
				}
			}
				
			for (int i=0; i<global.onWorldLoadTrigger.size(); i++) {
				//add all events to temp list
				List<String> TMP_events = new ArrayList<>();
				for (int j=2; j<global.onWorldLoadTrigger.get(i).size(); j++) {TMP_events.add(global.onWorldLoadTrigger.get(i).get(j));}
				
				//do events
				EventsHandler.doEvents(TMP_events, null);
			}
			
			for (int i=0; i<global.onServerChangeTrigger.size(); i++) {
				String currentServer;
				if (Minecraft.getMinecraft().isSingleplayer()) {currentServer = "SinglePlayer";}
                else {currentServer = Minecraft.getMinecraft().getCurrentServerData().serverIP;}
				
				if (!currentServer.equals(global.connectedToServer)) {
					//add all events to temp list
					List<String> TMP_events = new ArrayList<>();
					for (int j=2; j<global.onServerChangeTrigger.get(i).size(); j++) {TMP_events.add(global.onServerChangeTrigger.get(i).get(j));}
					
					//do events
					EventsHandler.doEvents(TMP_events, null);
				}
			}
			global.worldFirstLoad = false;

			if (Minecraft.getMinecraft().isSingleplayer()) {global.connectedToServer = "SinglePlayer";} 
			else {global.connectedToServer = Minecraft.getMinecraft().getCurrentServerData().serverIP;}
		}
	}
	
	public static void newDayTriggers() {
		if (global.worldLoaded) {
			DateFormat dateFormat = new SimpleDateFormat("yyy/MM/dd");
			Date date = new Date();
			if (global.currentDate.equals("null")) {global.currentDate = dateFormat.format(date);}
			
			if (!dateFormat.format(date).equals(global.currentDate)) {
				global.currentDate = dateFormat.format(date);
				for (int i=0; i<global.onNewDayTrigger.size(); i++) {
					//add all events to temp list
					List<String> TMP_events = new ArrayList<>();
					for (int j=2; j<global.onNewDayTrigger.get(i).size(); j++) {TMP_events.add(global.onNewDayTrigger.get(i).get(j));}
					
					//do events
					EventsHandler.doEvents(TMP_events, null);
				}
			}
		}
	}
}
