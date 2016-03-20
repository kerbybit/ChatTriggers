package com.kerbybit.chattriggers.triggers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.globalvars.global;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class TriggerHandler {
	public static void onChat(ClientChatReceivedEvent e) {
		String msg = e.message.getUnformattedText();
		String fmsg = e.message.getFormattedText();
		global.chatHistory.add(ChatHandler.removeFormatting(fmsg));
		if (global.chatHistory.size()>100) {global.chatHistory.remove(0);}
		
		String msgNOEDIT = msg;
		
		//debug chat
		if (global.debug==true) {ChatHandler.warnUnformatted(ChatHandler.removeFormatting(fmsg));}
		
		for (int i=0; i<global.trigger.size(); i++) {
			String TMP_type = global.trigger.get(i).get(0);
			String TMP_trig = global.trigger.get(i).get(1);
			
			if (TMP_type.equalsIgnoreCase("CHAT")) {
				//setup
				String TMP_w = "";
				String[] TMP_server = {};
				String current_server = "";
				Boolean correct_server = false;
				Boolean TMP_formatted = false;
				
				//tags
				if (TMP_trig.contains("<s>")) {TMP_w = "s"; TMP_trig = TMP_trig.replace("<s>", "");}
				if (TMP_trig.contains("<c>")) {TMP_w = "c"; TMP_trig = TMP_trig.replace("<c>", "");}
				if (TMP_trig.contains("<e>")) {TMP_w = "e"; TMP_trig = TMP_trig.replace("<e>", "");}
				if (TMP_trig.contains("<start>")) {TMP_w = "s"; TMP_trig = TMP_trig.replace("<start>", "");}
				if (TMP_trig.contains("<contain>")) {TMP_w = "c"; TMP_trig = TMP_trig.replace("<contain>", "");}
				if (TMP_trig.contains("<end>")) {TMP_w = "e"; TMP_trig = TMP_trig.replace("<end>", "");}
				if (TMP_trig.contains("<list=") && TMP_trig.contains(">")) {TMP_trig = TMP_trig.replace(TMP_trig.substring(TMP_trig.indexOf("<list="), TMP_trig.indexOf(">", TMP_trig.indexOf("<list="))+1), "");}
				if (TMP_trig.contains("<imported>")) {TMP_trig = TMP_trig.replace("<imported>", "");}
				if (TMP_trig.contains("<formatted>")) {TMP_trig = TMP_trig.replace("<formatted>", ""); TMP_formatted = true;}
				
				//check server stuff
				if (TMP_trig.contains("<server=") && TMP_trig.contains(">")) {
					TMP_server = TMP_trig.substring(TMP_trig.indexOf("<server=")+8, TMP_trig.indexOf(">", TMP_trig.indexOf("<server="))).split(",");
					TMP_trig = TMP_trig.replace(TMP_trig.substring(TMP_trig.indexOf("<server="), TMP_trig.indexOf(">", TMP_trig.indexOf("<server="))+1),  "");
				}
				
				if (Minecraft.getMinecraft().isSingleplayer()) {current_server = "SinglePlayer";} 
				else {current_server = Minecraft.getMinecraft().getCurrentServerData().serverIP;}
				
				for (String value : TMP_server) {if (current_server.contains(value)) {correct_server = true;}}
				if (TMP_server.length == 0) {correct_server = true;}
				
				//check if formatted or nah
				if (TMP_trig.contains("&")) {TMP_formatted=true;}  
				
				if (TMP_formatted) {
					msg = fmsg;
					msg = ChatHandler.removeFormatting(msg);
				} else {msg = msgNOEDIT;}
				
				//read strings
				if (TMP_trig.contains("{string<") && TMP_trig.contains(">}")) {
					String TMP_sn = TMP_trig.substring(TMP_trig.indexOf("{string<") + 8, TMP_trig.indexOf(">}"));
					for (int j=0; j<global.USR_string.size(); j++) {
						if (global.USR_string.get(j).get(0).equals(TMP_sn)) {
							String TMP_s = global.USR_string.get(j).get(1);
							TMP_trig = TMP_trig.replace("{string<" + TMP_sn + ">}", TMP_s);
						}
					}
				}
				TMP_trig = TMP_trig.replace("{me}", Minecraft.getMinecraft().thePlayer.getDisplayNameString());
				
				if (correct_server) {
					if (TMP_w.equals("s")) { //startWith
						try {TMP_trig = StringHandler.setStrings(msg, TMP_trig);}
						catch (Exception e1) {e1.printStackTrace(); ChatHandler.warn(ChatHandler.color("red", "There was a problem setting strings!"));}
						if (msg.startsWith(TMP_trig)) { //check
							//add all events to temp list
							List<String> TMP_events = new ArrayList<String>();
							for (int j=2; j<global.trigger.get(i).size(); j++) {TMP_events.add(global.trigger.get(i).get(j));}
							
							//do events
							if (global.temporary_replace.size()==0) {
								EventsHandler.doEvents(TMP_events, e);
							} else {
								EventsHandler.doEvents(TMP_events, e, global.temporary_replace.toArray(new String[global.temporary_replace.size()]), global.temporary_replacement.toArray(new String[global.temporary_replacement.size()]));
								global.temporary_replace.clear();
								global.temporary_replacement.clear();
							}
						} else {
							global.temporary_replace.clear();
							global.temporary_replacement.clear();
						}
					} else if (TMP_w.equals("c")) { //contains
						try {TMP_trig = StringHandler.setStrings(msg, TMP_trig);}
						catch (Exception e1) {e1.printStackTrace(); ChatHandler.warn(ChatHandler.color("red", "There was a problem setting strings!"));}
						if (msg.contains(TMP_trig)) { //check
							//add all events to temp list
							List<String> TMP_events = new ArrayList<String>();
							for (int j=2; j<global.trigger.get(i).size(); j++) {TMP_events.add(global.trigger.get(i).get(j));}
							
							//do events
							if (global.temporary_replace.size()==0) {
								EventsHandler.doEvents(TMP_events, e);
							} else {
								EventsHandler.doEvents(TMP_events, e, global.temporary_replace.toArray(new String[global.temporary_replace.size()]), global.temporary_replacement.toArray(new String[global.temporary_replacement.size()]));
								global.temporary_replace.clear();
								global.temporary_replacement.clear();
							}
						} else {
							global.temporary_replace.clear();
							global.temporary_replacement.clear();
						}
					} else if (TMP_w.equals("e")) { //endsWith
						try {TMP_trig = StringHandler.setStrings(msg, TMP_trig);}
						catch (Exception e1) {e1.printStackTrace(); ChatHandler.warn(ChatHandler.color("red", "There was a problem setting strings!"));}
						if (msg.endsWith(TMP_trig)) {
							//add all events to temp list
							List<String> TMP_events = new ArrayList<String>();
							for (int j=2; j<global.trigger.get(i).size(); j++) {TMP_events.add(global.trigger.get(i).get(j));}
							
							//do events
							if (global.temporary_replace.size()==0) {
								EventsHandler.doEvents(TMP_events, e);
							} else {
								EventsHandler.doEvents(TMP_events, e, global.temporary_replace.toArray(new String[global.temporary_replace.size()]), global.temporary_replacement.toArray(new String[global.temporary_replacement.size()]));
								global.temporary_replace.clear();
								global.temporary_replacement.clear();
							}
						} else {
							global.temporary_replace.clear();
							global.temporary_replacement.clear();
						}
					} else { //equals
						try {TMP_trig = StringHandler.setStrings(msg, TMP_trig);}
						catch (Exception e1) {e1.printStackTrace(); ChatHandler.warn(ChatHandler.color("red", "There was a problem setting strings!"));}
						if (msg.equals(TMP_trig)) { 
							//add all events to temp list
							List<String> TMP_events = new ArrayList<String>();
							for (int j=2; j<global.trigger.get(i).size(); j++) {TMP_events.add(global.trigger.get(i).get(j));}
							
							//do events
							if (global.temporary_replace.size()==0) {
								EventsHandler.doEvents(TMP_events, e);
							} else {
								EventsHandler.doEvents(TMP_events, e, global.temporary_replace.toArray(new String[global.temporary_replace.size()]), global.temporary_replacement.toArray(new String[global.temporary_replacement.size()]));
								global.temporary_replace.clear();
								global.temporary_replacement.clear();
							}
						} else {
							global.temporary_replace.clear();
							global.temporary_replacement.clear();
						}
					}
				}
			}
		}
	}
	
	public static void onClientTickTriggers() {
		if (global.worldIsLoaded==true) {
			for (int i=0; i<global.trigger.size(); i++) {
				String TMP_type = global.trigger.get(i).get(0);
				String TMP_trig = global.trigger.get(i).get(1);
				if (TMP_type.equalsIgnoreCase("ONCLIENTTICK")) {
					//add all events to temp list
					List<String> TMP_events = new ArrayList<String>();
					for (int j=2; j<global.trigger.get(i).size(); j++) {TMP_events.add(global.trigger.get(i).get(j));}
					
					//do events
					ClientChatReceivedEvent e1 = null;
					EventsHandler.doEvents(TMP_events, e1);
				}
			}
		}
	}
	
	public static void onRightClickPlayer(EntityInteractEvent e) {
		for (int i=0; i<global.trigger.size(); i++) {
			String TMP_type = global.trigger.get(i).get(0);
			String TMP_trig = global.trigger.get(i).get(1);
			if (TMP_type.equalsIgnoreCase("ONRIGHTCLICKPLAYER")) {
				//add all events to temp list
				List<String> TMP_events = new ArrayList<String>();
				for (int j=2; j<global.trigger.get(i).size(); j++) {TMP_events.add(global.trigger.get(i).get(j).replace("{player}", e.target.getName()));}
				
				//do events
				ClientChatReceivedEvent e1 = null;
				EventsHandler.doEvents(TMP_events, e1);
			}
		}
	}
	
	public static void worldLoadTriggers() {
		if (global.worldLoaded==true) {
			for (int i=0; i<global.trigger.size(); i++) {
				String TMP_type = global.trigger.get(i).get(0);
				String TMP_trig = global.trigger.get(i).get(1);
				
				if (global.worldFirstLoad==true) {
					System.out.println(global.worldFirstLoad);
					if (TMP_type.equalsIgnoreCase("ONWORLDFIRSTLOAD")) {
						//add all events to temp list
						List<String> TMP_events = new ArrayList<String>();
						for (int j=2; j<global.trigger.get(i).size(); j++) {TMP_events.add(global.trigger.get(i).get(j));}
						
						//do events
						ClientChatReceivedEvent e1 = null;
						EventsHandler.doEvents(TMP_events, e1);
					}
				}
				
				if (TMP_type.equalsIgnoreCase("ONWORLDLOAD")) {
					//add all events to temp list
					List<String> TMP_events = new ArrayList<String>();
					for (int j=2; j<global.trigger.get(i).size(); j++) {TMP_events.add(global.trigger.get(i).get(j));}
					
					//do events
					ClientChatReceivedEvent e1 = null;
					EventsHandler.doEvents(TMP_events, e1);
				}
				
				if (TMP_type.equalsIgnoreCase("ONSERVERCHANGE")) {
					String currentServer = "";
					if (Minecraft.getMinecraft().isSingleplayer()) {currentServer = "SinglePlayer";} 
					else {currentServer = Minecraft.getMinecraft().getCurrentServerData().serverIP;}
					
					if (!currentServer.equals(global.connectedToServer)) {
						//add all events to temp list
						List<String> TMP_events = new ArrayList<String>();
						for (int j=2; j<global.trigger.get(i).size(); j++) {TMP_events.add(global.trigger.get(i).get(j));}
						
						//do events
						ClientChatReceivedEvent e1 = null;
						EventsHandler.doEvents(TMP_events, e1);
					}
				}
			}
			global.worldFirstLoad = false;
			if (Minecraft.getMinecraft().isSingleplayer()) {global.connectedToServer = "SinglePlayer";} 
			else {global.connectedToServer = Minecraft.getMinecraft().getCurrentServerData().serverIP;}
		}
	}
	
	public static void newDayTriggers() {
		if (global.worldLoaded==true) {
			DateFormat dateFormat = new SimpleDateFormat("yyy/MM/dd");
			Date date = new Date();
			if (global.currentDate=="null") {global.currentDate = dateFormat.format(date);}
			
			if (!dateFormat.format(date).equals(global.currentDate)) {
				global.currentDate = dateFormat.format(date);
				for (int i=0; i<global.trigger.size(); i++) {
					String TMP_type = global.trigger.get(i).get(0);
					String TMP_trig = global.trigger.get(i).get(1);
					
					if (TMP_type.equalsIgnoreCase("ONNEWDAY")) {
						//add all events to temp list
						List<String> TMP_events = new ArrayList<String>();
						for (int j=2; j<global.trigger.get(i).size(); j++) {TMP_events.add(global.trigger.get(i).get(j));}
						
						//do events
						ClientChatReceivedEvent e1 = null;
						EventsHandler.doEvents(TMP_events, e1);
					}
				}
			}
		}
	}
}
