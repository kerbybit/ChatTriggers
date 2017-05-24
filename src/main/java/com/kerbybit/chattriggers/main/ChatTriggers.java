package com.kerbybit.chattriggers.main;

import java.io.IOException;

import com.kerbybit.chattriggers.commands.CommandReference;
import com.kerbybit.chattriggers.gui.DisplayOverlay;
import com.kerbybit.chattriggers.references.AsyncHandler;
import com.kerbybit.chattriggers.references.BugTracker;
import com.kerbybit.chattriggers.objects.DisplayHandler;
import com.kerbybit.chattriggers.overlay.KillfeedHandler;
import com.kerbybit.chattriggers.overlay.NotifyHandler;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import org.lwjgl.input.Keyboard;

import com.kerbybit.chattriggers.chat.ChatHandler;
import com.kerbybit.chattriggers.commands.CommandT;
import com.kerbybit.chattriggers.commands.CommandTR;
import com.kerbybit.chattriggers.commands.CommandTrigger;
import com.kerbybit.chattriggers.file.FileHandler;
import com.kerbybit.chattriggers.globalvars.global;
import com.kerbybit.chattriggers.gui.GuiTriggerList;
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
	private static KeyBinding altGuiKey;
    private static KeyBinding displayKey;
	
	@EventHandler
	public void init(FMLInitializationEvent event) throws ClassNotFoundException, IOException {
		MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);

        ClientCommandHandler.instance.registerCommand(new CommandTrigger());
        ClientCommandHandler.instance.registerCommand(new CommandT());
        ClientCommandHandler.instance.registerCommand(new CommandTR());
        
        altGuiKey = new KeyBinding("Trigger GUI", Keyboard.KEY_L, "ChatTriggers");
        displayKey = new KeyBinding("Killfeed position", Keyboard.KEY_K, "ChatTriggers");

        ClientRegistry.registerKeyBinding(altGuiKey);
        ClientRegistry.registerKeyBinding(displayKey);
	}
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (global.canUse) {
			if (altGuiKey.isPressed()) {
				GuiTriggerList.inMenu = -1;
				global.showAltInputGui = true;
			}
            if (displayKey.isPressed()) {
                global.showDisplayGui = true;
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
            BugTracker.show(exception, "onRightClickPlayer");
        }
	}

	@SubscribeEvent
    public void onSoundPlay(PlaySoundEvent e) {
	    try {
            if (global.canUse) {
                TriggerHandler.onSoundPlay(e);
            }
        } catch (Exception exception) {
	        BugTracker.show(exception, "onSoundPlay");
        }
    }
	
	@SubscribeEvent
	public void onChat(ClientChatReceivedEvent e) throws IOException, ClassNotFoundException {
        try {
            if (global.canUse) {
                TriggerHandler.onChat(e);
            }
        } catch (Exception exception) {
            BugTracker.show(exception, "chat");
        }
	}
	
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load e) {
        if (global.canUse) {
            global.worldLoaded=true;
            NotifyHandler.systimeResetNotify();
        }
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload e) {
		if (global.canUse) {
			global.worldIsLoaded=false;
            DisplayHandler.clearDisplays();
		}
	}

	@SubscribeEvent
	public void RenderGameOverlayEvent(RenderGameOverlayEvent event) {
		if (global.canUse) {
		    try {
                CommandReference.clickCalc();

                KillfeedHandler.drawKillfeed(event);
                NotifyHandler.drawNotify(event);

                DisplayHandler.drawDisplays(event);


                GuiTriggerList.openGui();
                DisplayOverlay.openGui();

                FileHandler.firstFileLoad();

                try {
                    TriggerHandler.worldLoadTriggers();
                } catch (NullPointerException e) {
                    //Catch for replay mod
                }

                TriggerHandler.newDayTriggers();
                global.worldLoaded=false;
		    } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

	
	@SubscribeEvent
	public void onClientTick(ClientTickEvent e) throws ClassNotFoundException {
	    global.playedSounds.clear();

		if (global.canUse) {

			KillfeedHandler.tickKillfeed();

			FileHandler.tickImports();

            try {
			    TriggerHandler.onClientTickTriggers();
            } catch (Exception exception) {
                BugTracker.show(exception, "onClientTick");
            }

            try {
                AsyncHandler.asyncTick();
            } catch (Exception exception) {
                BugTracker.show(exception, "async");
            }

            EventsHandler.eventTick();
            global.ticksElapsed++;

			ChatHandler.onClientTick();
		} else {
            Minecraft.getMinecraft().gameSettings.invertMouse = global.inverted;
        }
	}

	@SubscribeEvent
    public void onMouseClicked(MouseEvent e) {
	    if (e.button == 0 && e.buttonstate) {
            global.clicks.add(20);
        }
        if (e.button == 1 && e.buttonstate) {
	        global.rclicks++;
        }
    }
}