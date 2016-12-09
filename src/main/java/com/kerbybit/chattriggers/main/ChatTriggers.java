package com.kerbybit.chattriggers.main;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.commands.CommandT;
import com.kerbybit.chattriggers.commands.CommandTR;
import com.kerbybit.chattriggers.commands.CommandTrigger;
import com.kerbybit.chattriggers.file.FileHandler;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.gui.GuiTriggerList;
import com.kerbybit.chattriggers.gui.OverlayHandler;
import com.kerbybit.chattriggers.references.Reference;
import com.kerbybit.chattriggers.triggers.EventsHandler;
import com.kerbybit.chattriggers.triggers.TriggerHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
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
		if (global.canUse) {
			if (altGuiKey.isPressed()) {
				GuiTriggerList.inMenu = -1;
				global.showAltInputGui = true;
			}
		}
	}
	
	@SubscribeEvent
	public void onRightClickPlayer(EntityInteractEvent e) {
        try {
            if (global.canUse) {
                if (e.entity.equals(Minecraft.getMinecraft().thePlayer)) {
                    if (e.target instanceof EntityPlayer) {
                        TriggerHandler.onRightClickPlayer(e);
                    }
                }
            }
        } catch (Exception exception) {
            ChatHandler.warn("&4An unknown error has occured while executing \"&conRightClickPlayer&4\"!");
            exception.printStackTrace();
            for (StackTraceElement stack : exception.getStackTrace()) {
                if (stack.toString().toUpperCase().contains("CHATTRIGGERS")) {
                    global.bugReport.add(stack.toString());
                }
            }
            ChatHandler.warn("&4Click clickable(&c[HERE],run_command,/trigger submitbugreport,Send a bug report) &4to submit a bug report");
        }
	}
	
	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent e) throws IOException, ClassNotFoundException {
        try {
            if (global.canUse) {
                TriggerHandler.onChat(e);
            }
        } catch (Exception exception) {
            ChatHandler.warn("&4An unknown error has occured while executing \"&cchat&4\"!");
            exception.printStackTrace();
            global.bugReport.clear();
            for (StackTraceElement stack : exception.getStackTrace()) {
                if (stack.toString().toUpperCase().contains("CHATTRIGGERS")) {
                    global.bugReport.add(stack.toString());
                }
            }
            ChatHandler.warn("&4Click clickable(&c[HERE],run_command,/trigger submitbugreport,Send a bug report) &4to submit a bug report");
        }
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
        try {
            if (global.canUse) {
                global.worldLoaded=true;
                global.worldIsLoaded=true;
            }
        } catch (Exception exception) {
            ChatHandler.warn("&4An unknown error has occured while executing \"&conWorldLoad&4\"!");
            exception.printStackTrace();
            for (StackTraceElement stack : exception.getStackTrace()) {
                if (stack.toString().toUpperCase().contains("CHATTRIGGERS")) {
                    global.bugReport.add(stack.toString());
                }
            }
            ChatHandler.warn("&4Click clickable(&c[HERE],run_command,/trigger submitbugreport,Send a bug report) &4to submit a bug report");
        }
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload e) {
		if (global.canUse) {
			global.worldIsLoaded=false;
		}
	}
		
	@SubscribeEvent
	public void RenderGameOverlayEvent(RenderGameOverlayEvent event) {
		if (global.canUse) {
			OverlayHandler.drawKillfeed(event);
			OverlayHandler.drawNotify(event);
			
			GuiTriggerList.openGui();
			
			FileHandler.firstFileLoad();
			
			TriggerHandler.worldLoadTriggers();
			TriggerHandler.newDayTriggers();
			global.worldLoaded=false;
		}
	}

	
	@SubscribeEvent
	public void onClientTick(ClientTickEvent e) throws ClassNotFoundException {
		if (global.canUse) {
			OverlayHandler.tickKillfeed();
			OverlayHandler.tickNotify();
			
			FileHandler.tickImports();

            try {
			    TriggerHandler.onClientTickTriggers();
            } catch (Exception exception) {
                ChatHandler.warn("&4An unknown error has occured while executing \"&conClientTick&4\"!");
                exception.printStackTrace();
                for (StackTraceElement stack : exception.getStackTrace()) {
                    if (stack.toString().toUpperCase().contains("CHATTRIGGERS")) {
                        global.bugReport.add(stack.toString());
                    }
                }
                ChatHandler.warn("&4Click clickable(&c[HERE],run_command,/trigger submitbugreport,Send a bug report) &4to submit a bug report");
            }
			
			ChatHandler.onClientTick();
			EventsHandler.eventTick();
			global.ticksElapsed += 1;
		}
	}
}