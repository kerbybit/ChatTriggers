package com.kerbybit.chattriggers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION)
public class ChatTriggers {
	
	@EventHandler
	public void init(FMLInitializationEvent event) throws ClassNotFoundException, IOException {
		MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);

        ClientCommandHandler.instance.registerCommand(new CommandTrigger());
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
				String TMP_w = "";
				
				//tags
				if (TMP_trig.contains("{s}")) {TMP_w = "s"; TMP_trig = TMP_trig.replace("{s}", "");}
				if (TMP_trig.contains("{c}")) {TMP_w = "c"; TMP_trig = TMP_trig.replace("{c}", "");}
				if (TMP_trig.contains("{e}")) {TMP_w = "e"; TMP_trig = TMP_trig.replace("{e}", "");}
				TMP_trig = TMP_trig.replace("{me}", Minecraft.getMinecraft().thePlayer.getDisplayNameString());
				if (TMP_trig.contains("{list=")) {TMP_trig = TMP_trig.replace(TMP_trig.substring(TMP_trig.indexOf("{"), TMP_trig.indexOf("}")+1), "");}
				
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
				
				
				//chat check
				if (TMP_w.equals("s")) { //startWith
					TMP_trig = events.setStrings(msg, TMP_trig);
					if (msg.startsWith(TMP_trig)) { //check
						//add all events to temp list
						List<String> TMP_events = new ArrayList<String>();
						for (int j=2; j<global.trigger.get(i).size(); j++) {TMP_events.add(global.trigger.get(i).get(j));}
						
						//do events
						events.doEvents(TMP_events, e);
					}
				} else if (TMP_w.equals("c")) { //contains
					TMP_trig = events.setStrings(msg, TMP_trig);
					if (msg.contains(TMP_trig)) { //check
						//add all events to temp list
						List<String> TMP_events = new ArrayList<String>();
						for (int j=2; j<global.trigger.get(i).size(); j++) {TMP_events.add(global.trigger.get(i).get(j));}
						
						//do events
						events.doEvents(TMP_events, e);
					}
				} else if (TMP_w.equals("e")) { //endsWith
					TMP_trig = events.setStrings(msg, TMP_trig);
					if (msg.endsWith(TMP_trig)) {
						//add all events to temp list
						List<String> TMP_events = new ArrayList<String>();
						for (int j=2; j<global.trigger.get(i).size(); j++) {TMP_events.add(global.trigger.get(i).get(j));}
						
						//do events
						events.doEvents(TMP_events, e);
					}
				} else { //equals
					TMP_trig = events.setStrings(msg, TMP_trig);
					if (msg.equals(TMP_trig)) { 
						//add all events to temp list
						List<String> TMP_events = new ArrayList<String>();
						for (int j=2; j<global.trigger.get(i).size(); j++) {TMP_events.add(global.trigger.get(i).get(j));}
						
						//do events
						events.doEvents(TMP_events, e);
					}
				}
			}
			//////////////////////////////////////////
		}
		
		//oooh fancy
		chat.append(e, fmsg);
	}
		
		
	@SubscribeEvent
	public void RenderGameOverlayEvent(RenderGameOverlayEvent event) {
		
		//draw killfeed
		if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
			for (int i=0; i<global.killfeed.size(); i++) {
				Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(global.killfeed.get(i), 5, i*10 + 5, 0xffffff);
			}
		}
		
		//first file load
		if (global.tick==0) {
			try {file.startup();
			} catch (ClassNotFoundException e) {e.printStackTrace();}
			global.tick++;
		}
	}

	
	@SubscribeEvent
	public void onClientTick(ClientTickEvent e) throws ClassNotFoundException {
		chat.onClientTick();
		events.eventTick();
	}
}