package com.kerbybit.chattriggers.gui;

import com.kerbybit.chattriggers.globalvars.global;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class OverlayHandler {
	
	public static void drawKillfeed(RenderGameOverlayEvent event) {
		Minecraft MC = Minecraft.getMinecraft();
		if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
			for (int i=0; i<global.killfeed.size(); i++) {
				if (global.settings.get(3).equalsIgnoreCase("TOP-RIGHT") || global.settings.get(3).equalsIgnoreCase("TR")) {
					ScaledResolution var5 = new ScaledResolution(MC, MC.displayWidth, MC.displayHeight);
					float var6 = var5.getScaledWidth();
					int col = 0xffffffff;
                    if (global.settings.get(9).equalsIgnoreCase("TRUE")) {
                        if (global.killfeedDelay.get(i)<50) {col = col - (50-global.killfeedDelay.get(i))*0x05000000;}
                    }
					MC.fontRendererObj.drawStringWithShadow(global.killfeed.get(i), var6 - MC.fontRendererObj.getStringWidth(global.killfeed.get(i)) - 5, i*10 + 5, col);
				} else {
					int col = 0xffffffff;
                    if (global.settings.get(9).equalsIgnoreCase("TRUE")) {
                        if (global.killfeedDelay.get(i)<50) {col = col - (50-global.killfeedDelay.get(i))*0x05000000;}
                    }
					MC.fontRendererObj.drawStringWithShadow(global.killfeed.get(i), 5, i*10 + 5, col);
				}
			}
		}
	}
	
	public static void tickKillfeed() {
		for (int i=0; i<global.killfeedDelay.size(); i++) {
			if (global.killfeedDelay.get(i) == 0) {
				global.killfeed.remove(i);
				global.killfeedDelay.remove(i);
			} else {
				global.killfeedDelay.set(i, global.killfeedDelay.get(i) - 1);
			}
		}
	}
	
	public static void drawNotify(RenderGameOverlayEvent event) {
		Minecraft MC = Minecraft.getMinecraft();
		if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
			for (int i=0; i<global.notify.size(); i++) {
				MC.fontRendererObj.drawStringWithShadow(global.notify.get(i), global.notifyAnimation.get(i).get(1), global.notifyAnimation.get(i).get(2), 0xffffff);
			}
		}
	}
	
	public static void tickNotify() {
		Minecraft MC = Minecraft.getMinecraft();
		for (int i=0; i<global.notify.size(); i++) {
			if (global.notifyAnimation.get(i).get(0)==0) {
				ScaledResolution var5 = new ScaledResolution(MC, MC.displayWidth, MC.displayHeight);
				float var6 = var5.getScaledWidth(); 
				global.notifyAnimation.get(i).set(4, var6 - MC.fontRendererObj.getStringWidth(global.notify.get(i)) - 5);
				global.notifyAnimation.get(i).set(5, var6);
				float var7 = var5.getScaledHeight()-50-(global.notifyAnimation.get(i).get(2)*10);
				global.notifyAnimation.get(i).set(1, var6);
				global.notifyAnimation.get(i).set(2, var7);
				global.notifyAnimation.get(i).set(0, (float) 1);
			} else if (global.notifyAnimation.get(i).get(0)==1) {
				if (Math.floor(global.notifyAnimation.get(i).get(1)) > global.notifyAnimation.get(i).get(4)) {
					global.notifyAnimation.get(i).set(1, global.notifyAnimation.get(i).get(1) + (global.notifyAnimation.get(i).get(4)-global.notifyAnimation.get(i).get(1))/global.settingsNotificationSpeed);
				} else {global.notifyAnimation.get(i).set(0, (float) 2);}
			} else if (global.notifyAnimation.get(i).get(0)==2) {
				if (global.notifyAnimation.get(i).get(3)>0) {
					global.notifyAnimation.get(i).set(3, global.notifyAnimation.get(i).get(3)-1);
				} else {global.notifyAnimation.get(i).set(0, (float) 3);}
			} else if (global.notifyAnimation.get(i).get(0)==3) {
				if (global.notifyAnimation.get(i).get(1) < global.notifyAnimation.get(i).get(5)) {
					global.notifyAnimation.get(i).set(1, global.notifyAnimation.get(i).get(1) - (global.notifyAnimation.get(i).get(4)-global.notifyAnimation.get(i).get(1))/global.settingsNotificationSpeed);
				} else {
					ScaledResolution var5 = new ScaledResolution(MC, MC.displayWidth, MC.displayHeight);
					float var6 = var5.getScaledHeight(); 
					if (global.notifyAnimation.get(i).get(2) == var6-50 || global.notify.size()==1) {global.notifySize = 0;}
					global.notifyAnimation.remove(i);
					global.notify.remove(i);
				}
			}
		}
	}
}
