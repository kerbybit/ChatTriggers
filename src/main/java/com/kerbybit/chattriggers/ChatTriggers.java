package com.kerbybit.chattriggers;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;

import commands.CommandT;
import commands.CommandTR;
import commands.CommandTrigger;
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
        ClientCommandHandler.instance.registerCommand(new CommandT());
        ClientCommandHandler.instance.registerCommand(new CommandTR());
        
        altGuiKey = new KeyBinding("Trigger GUI", Keyboard.KEY_GRAVE, "ChatTriggers");
        ClientRegistry.registerKeyBinding(altGuiKey);
	}
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (altGuiKey.isPressed()) {
			GuiTriggerList.inMenu = -1;
			global.showAltInputGui = true;
		}
	}
	
	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent e) throws IOException, ClassNotFoundException {
		TriggerHandler.onChat(e);
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
		global.worldLoaded=true;
		global.worldIsLoaded=true;
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload e) {
		global.worldIsLoaded=false;
	}
		
	@SubscribeEvent
	public void RenderGameOverlayEvent(RenderGameOverlayEvent event) {
		OverlayHandler.drawKillfeed(event);
		OverlayHandler.drawNotify(event);
		
		GuiTriggerList.openGui();
		
		FileHandler.firstFileLoad();
		
		TriggerHandler.worldLoadTriggers();
	}

	
	@SubscribeEvent
	public void onClientTick(ClientTickEvent e) throws ClassNotFoundException {
		OverlayHandler.tickKillfeed();
		OverlayHandler.tickNotify();
		
		FileHandler.tickImports();
		
		TriggerHandler.newDayTriggers();
		ChatHandler.onClientTick();
		EventsHandler.eventTick();
	}
}