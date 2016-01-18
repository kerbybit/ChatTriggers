package com.kerbybit.chattriggers;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class ChatTriggers {
	
	Minecraft MC = Minecraft.getMinecraft();
	public static KeyBinding altGuiKey;
	
	@EventHandler
	public void init(FMLInitializationEvent event) throws ClassNotFoundException, IOException {
		MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);

        ClientCommandHandler.instance.registerCommand(new CommandTrigger());
        
        altGuiKey = new KeyBinding("Trigger GUI", Keyboard.KEY_GRAVE, "ChatTriggers");
        ClientRegistry.registerKeyBinding(altGuiKey);
	}
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (altGuiKey.isPressed()) {
			gui.inMenu = -1;
			global.showAltInputGui = true;
		}
	}
	
	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent e) throws IOException, ClassNotFoundException {
		String msg = e.message.getUnformattedText();
		String fmsg = e.message.getFormattedText();
		
		String msgNOEDIT = msg;
		
		//debug chat
		if (global.debug==true) {
			msg = fmsg;
			msg = chat.removeFormatting(msg);
			chat.warnUnformatted(msg);
			msg = msgNOEDIT;
		}
		
		////////////////////////////////////////////////////////////////
		
		for (int i=0; i<global.trigger.size(); i++) {
			String TMP_type = global.trigger.get(i).get(0);
			String TMP_trig = global.trigger.get(i).get(1);
			
			
			//////////////chat trigger////////////////////
			if (TMP_type.equalsIgnoreCase("CHAT")) {
				//setup
				String TMP_w = "";
				String[] TMP_server = {};
				String current_server = "";
				Boolean correct_server = false;
				
				//tags
				if (TMP_trig.contains("<s>")) {TMP_w = "s"; TMP_trig = TMP_trig.replace("<s>", "");}
				if (TMP_trig.contains("<c>")) {TMP_w = "c"; TMP_trig = TMP_trig.replace("<c>", "");}
				if (TMP_trig.contains("<e>")) {TMP_w = "e"; TMP_trig = TMP_trig.replace("<e>", "");}
				if (TMP_trig.contains("<start>")) {TMP_w = "s"; TMP_trig = TMP_trig.replace("<start>", "");}
				if (TMP_trig.contains("<contain>")) {TMP_w = "c"; TMP_trig = TMP_trig.replace("<contain>", "");}
				if (TMP_trig.contains("<end>")) {TMP_w = "e"; TMP_trig = TMP_trig.replace("<end>", "");}
				if (TMP_trig.contains("<list=") && TMP_trig.contains(">")) {TMP_trig = TMP_trig.replace(TMP_trig.substring(TMP_trig.indexOf("<list="), TMP_trig.indexOf(">", TMP_trig.indexOf("<list="))+1), "");}
				
				
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
				if (TMP_trig.contains("&")) {
					msg = fmsg;
					msg = chat.removeFormatting(msg);
				} else {
					msg = msgNOEDIT;
				}
				
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
				
				
				//chat check
				if (correct_server) {
					if (TMP_w.equals("s")) { //startWith
						try {TMP_trig = events.setStrings(msg, TMP_trig);}
						catch (StringIndexOutOfBoundsException e1) {e1.printStackTrace(); chat.warn(chat.color("red", "There was a problem setting strings!"));}
						if (msg.startsWith(TMP_trig)) { //check
							//add all events to temp list
							List<String> TMP_events = new ArrayList<String>();
							for (int j=2; j<global.trigger.get(i).size(); j++) {TMP_events.add(global.trigger.get(i).get(j));}
							
							//do events
							events.doEvents(TMP_events, e);
						}
					} else if (TMP_w.equals("c")) { //contains
						try {TMP_trig = events.setStrings(msg, TMP_trig);}
						catch (StringIndexOutOfBoundsException e1) {e1.printStackTrace(); chat.warn(chat.color("red", "There was a problem setting strings!"));}
						if (msg.contains(TMP_trig)) { //check
							//add all events to temp list
							List<String> TMP_events = new ArrayList<String>();
							for (int j=2; j<global.trigger.get(i).size(); j++) {TMP_events.add(global.trigger.get(i).get(j));}
							
							//do events
							events.doEvents(TMP_events, e);
						}
					} else if (TMP_w.equals("e")) { //endsWith
						try {TMP_trig = events.setStrings(msg, TMP_trig);}
						catch (StringIndexOutOfBoundsException e1) {e1.printStackTrace(); chat.warn(chat.color("red", "There was a problem setting strings!"));}
						if (msg.endsWith(TMP_trig)) {
							//add all events to temp list
							List<String> TMP_events = new ArrayList<String>();
							for (int j=2; j<global.trigger.get(i).size(); j++) {TMP_events.add(global.trigger.get(i).get(j));}
							
							//do events
							events.doEvents(TMP_events, e);
						}
					} else { //equals
						try {TMP_trig = events.setStrings(msg, TMP_trig);}
						catch (StringIndexOutOfBoundsException e1) {e1.printStackTrace(); chat.warn(chat.color("red", "There was a problem setting strings!"));}
						if (msg.equals(TMP_trig)) { 
							//add all events to temp list
							List<String> TMP_events = new ArrayList<String>();
							for (int j=2; j<global.trigger.get(i).size(); j++) {TMP_events.add(global.trigger.get(i).get(j));}
							
							//do events
							events.doEvents(TMP_events, e);
						}
					}
				}
				
			}
			//////////////////////////////////////////
		}
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		global.worldLoaded=true;
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload e) {
		global.waitEvents.clear();
		global.waitTime.clear();
	}
		
		
	@SubscribeEvent
	public void RenderGameOverlayEvent(RenderGameOverlayEvent event) {
		
		
		if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
			//draw killfeed
			for (int i=0; i<global.killfeed.size(); i++) {
				if (global.settings.get(3).equalsIgnoreCase("TOP-RIGHT") || global.settings.get(3).equalsIgnoreCase("TR")) {
					ScaledResolution var5 = new ScaledResolution(MC, MC.displayWidth, MC.displayHeight);
					float var6 = var5.getScaledWidth();
					MC.fontRendererObj.drawStringWithShadow(global.killfeed.get(i), var6 - MC.fontRendererObj.getStringWidth(global.killfeed.get(i)) - 5, i*10 + 5, 0xffffff);
				} else {
					MC.fontRendererObj.drawStringWithShadow(global.killfeed.get(i), 5, i*10 + 5, 0xffffff);
				}
			}
			//draw notify
			for (int i=0; i<global.notify.size(); i++) {
				MC.fontRendererObj.drawStringWithShadow(global.notify.get(i), global.notifyAnimation.get(i).get(1), global.notifyAnimation.get(i).get(2), 0xffffff);
			}
		}
		
		if (global.showGUI) {
			global.showGUI = false;
			MC.displayGuiScreen(new gui());
		}
		
		if (global.showAltInputGui) {
			global.showAltInputGui = false;
			MC.displayGuiScreen(new gui());
		}
		
		//first file load
		if (global.tick==0) {
			try {file.startup();
			} catch (ClassNotFoundException e) {e.printStackTrace();}
			
			file.loadVersion("http://bfgteam.com:88/ChatTriggers/download/versions/version.txt");
			
			global.tick++;
		}
		
		//onWorldLoad trigger type
		if (global.worldLoaded==true) {
			global.worldLoaded = false;
			for (int i=0; i<global.trigger.size(); i++) {
				String TMP_type = global.trigger.get(i).get(0);
				String TMP_trig = global.trigger.get(i).get(1);
				
				if (global.worldFirstLoad==true) {
					if (TMP_type.equalsIgnoreCase("ONWORLDFIRSTLOAD")) {
						global.worldFirstLoad = false;
						//add all events to temp list
						List<String> TMP_events = new ArrayList<String>();
						for (int j=2; j<global.trigger.get(i).size(); j++) {TMP_events.add(global.trigger.get(i).get(j));}
						
						//do events
						ClientChatReceivedEvent e1 = null;
						events.doEvents(TMP_events, e1);
					}
				}
				
				if (TMP_type.equalsIgnoreCase("ONWORLDLOAD")) {
					//add all events to temp list
					List<String> TMP_events = new ArrayList<String>();
					for (int j=2; j<global.trigger.get(i).size(); j++) {TMP_events.add(global.trigger.get(i).get(j));}
					
					//do events
					ClientChatReceivedEvent e1 = null;
					events.doEvents(TMP_events, e1);
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
						events.doEvents(TMP_events, e1);
					}
				}
			}
			if (Minecraft.getMinecraft().isSingleplayer()) {global.connectedToServer = "SinglePlayer";} 
			else {global.connectedToServer = Minecraft.getMinecraft().getCurrentServerData().serverIP;}
		}
	}

	
	@SubscribeEvent
	public void onClientTick(ClientTickEvent e) throws ClassNotFoundException {
		for (int i=0; i<global.notify.size(); i++) {
			if (global.notifyAnimation.get(i).get(0)==0) {
				ScaledResolution var5 = new ScaledResolution(MC, MC.displayWidth, MC.displayHeight);
				float var6 = var5.getScaledWidth(); 
				global.notifyAnimation.get(i).set(4, var6 - MC.fontRendererObj.getStringWidth(global.notify.get(i)) - 5);
				global.notifyAnimation.get(i).set(5, var6);
				float var7 = var5.getScaledHeight()-50-(global.notifyAnimation.get(i).get(2)*15);
				global.notifyAnimation.get(i).set(1, var6);
				global.notifyAnimation.get(i).set(2, var7);
				global.notifyAnimation.get(i).set(0, (float) 1);
			} else if (global.notifyAnimation.get(i).get(0)==1) {
				if (Math.floor(global.notifyAnimation.get(i).get(1)) > global.notifyAnimation.get(i).get(4)) {
					global.notifyAnimation.get(i).set(1, global.notifyAnimation.get(i).get(1) + (global.notifyAnimation.get(i).get(4)-global.notifyAnimation.get(i).get(1))/10);
				} else {
					global.notifyAnimation.get(i).set(0, (float) 2);
				}
			} else if (global.notifyAnimation.get(i).get(0)==2) {
				if (global.notifyAnimation.get(i).get(3)>0) {
					global.notifyAnimation.get(i).set(3, global.notifyAnimation.get(i).get(3)-1);
				} else {
					global.notifyAnimation.get(i).set(0, (float) 3);
				}
			} else if (global.notifyAnimation.get(i).get(0)==3) {
				if (global.notifyAnimation.get(i).get(1) < global.notifyAnimation.get(i).get(5)) {
					global.notifyAnimation.get(i).set(1, global.notifyAnimation.get(i).get(1) - (global.notifyAnimation.get(i).get(4)-global.notifyAnimation.get(i).get(1))/10);
				} else {
					ScaledResolution var5 = new ScaledResolution(MC, MC.displayWidth, MC.displayHeight);
					float var6 = var5.getScaledHeight(); 
					if (global.notifyAnimation.get(i).get(2) == var6-50 || global.notify.size()==1) {
						global.notifySize = 0;
					}
					global.notifyAnimation.remove(i);
					global.notify.remove(i);
				}
			}
		}
		
		chat.onClientTick();
		events.eventTick();
	}
}