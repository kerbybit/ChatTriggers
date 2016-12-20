package com.kerbybit.chattriggers.gui;

import com.kerbybit.chattriggers.globalvars.global;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class OverlayHandler {
    private static Minecraft MC = Minecraft.getMinecraft();
    private static FontRenderer ren = MC.fontRendererObj;
    private static ScaledResolution res = new ScaledResolution(MC);
    private static float width = res.getScaledWidth();
    private static float height = res.getScaledHeight();

	public static void drawKillfeed(RenderGameOverlayEvent event) {
		if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            try {
                int x = Integer.parseInt(global.settings.get(3).substring(0, global.settings.get(3).indexOf(" ")));
                int y = Integer.parseInt(global.settings.get(3).substring(global.settings.get(3).indexOf(" ") + 1));
                if (x < 0) {x=0;}
                if (y < 0) {y=0;}
                if (x > width) {x= (int) width;}
                if (y > height) {y= (int) height;}
                if (x < width/2) {
                    if (y < height/2) {
                        for (int i=0; i<global.killfeed.size(); i++) {
                            int col = 0xffffffff;
                            if (global.settings.get(9).equalsIgnoreCase("TRUE")) {
                                if (global.killfeedDelay.get(i) < 50) {
                                    col = col - (50 - global.killfeedDelay.get(i)) * 0x05000000;
                                }
                            }
                            ren.drawStringWithShadow(global.killfeed.get(i), x, y + i*10, col);
                        }
                    } else {
                        for (int i=0; i<global.killfeed.size(); i++) {
                            int col = 0xffffffff;
                            if (global.settings.get(9).equalsIgnoreCase("TRUE")) {
                                if (global.killfeedDelay.get(i) < 50) {
                                    col = col - (50 - global.killfeedDelay.get(i)) * 0x05000000;
                                }
                            }
                            ren.drawStringWithShadow(global.killfeed.get(i), x, y - i*10, col);
                        }
                    }
                } else {
                    if (y < height/2) {
                        for (int i=0; i<global.killfeed.size(); i++) {
                            int col = 0xffffffff;
                            if (global.settings.get(9).equalsIgnoreCase("TRUE")) {
                                if (global.killfeedDelay.get(i) < 50) {
                                    col = col - (50 - global.killfeedDelay.get(i)) * 0x05000000;
                                }
                            }
                            ren.drawStringWithShadow(global.killfeed.get(i), x-ren.getStringWidth(global.killfeed.get(i)), y + i*10, col);
                        }
                    } else {
                        for (int i=0; i<global.killfeed.size(); i++) {
                            int col = 0xffffffff;
                            if (global.settings.get(9).equalsIgnoreCase("TRUE")) {
                                if (global.killfeedDelay.get(i) < 50) {
                                    col = col - (50 - global.killfeedDelay.get(i)) * 0x05000000;
                                }
                            }
                            ren.drawStringWithShadow(global.killfeed.get(i), x-ren.getStringWidth(global.killfeed.get(i)), y - i*10, col);
                        }
                    }
                }
            } catch (Exception e) {
                for (int i=0; i<global.killfeed.size(); i++) {
                    int col = 0xffffffff;
                    if (global.settings.get(3).equalsIgnoreCase("TOP-RIGHT") || global.settings.get(3).equalsIgnoreCase("TR")) {
                        if (global.settings.get(9).equalsIgnoreCase("TRUE")) {
                            if (global.killfeedDelay.get(i)<50) {
                                col = col - (50-global.killfeedDelay.get(i))*0x05000000;
                            }
                        }
                        ren.drawStringWithShadow(global.killfeed.get(i), width - ren.getStringWidth(global.killfeed.get(i)) - 5, i*10 + 5, col);
                    } else {
                        if (global.settings.get(9).equalsIgnoreCase("TRUE")) {
                            if (global.killfeedDelay.get(i)<50) {
                                col = col - (50-global.killfeedDelay.get(i))*0x05000000;
                            }
                        }
                        ren.drawStringWithShadow(global.killfeed.get(i), 5, i*10 + 5, col);
                    }
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
		if (event.type == RenderGameOverlayEvent.ElementType.TEXT) {
			for (int i=0; i<global.notify.size(); i++) {
				ren.drawStringWithShadow(global.notify.get(i), global.notifyAnimation.get(i).get(1), global.notifyAnimation.get(i).get(2), 0xffffff);
			}
		}
	}
	
	public static void tickNotify() {
		for (int i=0; i<global.notify.size(); i++) {
			if (global.notifyAnimation.get(i).get(0)==0) {
				global.notifyAnimation.get(i).set(4, width - ren.getStringWidth(global.notify.get(i)) - 5);
				global.notifyAnimation.get(i).set(5, width);
				float var7 = height-50-(global.notifyAnimation.get(i).get(2)*10);
				global.notifyAnimation.get(i).set(1, width);
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
					if (global.notifyAnimation.get(i).get(2) == height-50 || global.notify.size()==1) {global.notifySize = 0;}
					global.notifyAnimation.remove(i);
					global.notify.remove(i);
				}
			}
		}
	}
}
