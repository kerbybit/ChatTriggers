package com.kerbybit.chattriggers.gui;

import com.kerbybit.chattriggers.file.FileHandler;
import com.kerbybit.chattriggers.globalvars.global;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class DisplayOverlay extends GuiScreen {
    private static Minecraft MC = Minecraft.getMinecraft();
    private static Boolean grabbed = false;
    private static Boolean can_grab = true;

    public static void openGui() {
        if (global.showDisplayGui) {
            global.showDisplayGui = false;
            MC.displayGuiScreen(new DisplayOverlay());
        }
    }

    @Override
    public void mouseClicked(int x, int y, int button) throws IOException {
        float mouseX = (float)x;
        float mouseY = (float)y;

        ScaledResolution var5 = new ScaledResolution(MC);
        float width = var5.getScaledWidth();
        float height = var5.getScaledHeight();

        if (mouseX < 0) {mouseX = 0;}
        if (mouseY < 0) {mouseY = 0;}
        if (mouseX > width) {mouseX = width;}
        if (mouseY > height) {mouseY = height;}
        int kx = (int)(global.killfeed_x*width);
        int ky = (int)(global.killfeed_y*height);

        if (kx < 0) {kx = 0;}
        if (ky < 0) {ky = 0;}
        if (kx > width) {kx = (int)width;}
        if (ky > height) {ky = (int)height;}

        if (grabbed) {
            grabbed = false; can_grab = false;
            global.settings.set(3, global.killfeed_x + " " + global.killfeed_y);
            FileHandler.saveAll();
        }
        if (can_grab) {
            if (kx < width /2) {
                if (ky < height/2) {
                    if (mouseX > kx && mouseX < kx + MC.fontRendererObj.getStringWidth("KILLFEED")
                            && mouseY > ky && mouseY < ky + 100) {
                        grabbed = true;
                    }
                } else {
                    if (mouseX > kx && mouseX < kx + MC.fontRendererObj.getStringWidth("KILLFEED")
                            && mouseY > ky && mouseY < ky + 100) {
                        grabbed = true;
                    }
                }
            } else {
                if (ky < height/2) {
                    if (mouseX < kx && mouseX > kx - MC.fontRendererObj.getStringWidth("KILLFEED")
                            && mouseY > ky && mouseY < ky + 100) {
                        grabbed = true;
                    }
                } else {
                    if (mouseX < kx && mouseX > kx - MC.fontRendererObj.getStringWidth("KILLFEED")
                            && mouseY > ky && mouseY < ky + 100) {
                        grabbed = true;
                    }
                }
            }
        }
        can_grab = true;

        super.mouseClicked(x, y, button);
    }

    @Override
    public void drawScreen(int x, int y, float ticks) {
        GL11.glColor4f(1, 1, 1, 1);
        drawDefaultBackground();

        int kx = (int)(global.killfeed_x*width);
        int ky = (int)(global.killfeed_y*height);

        ScaledResolution var5 = new ScaledResolution(MC);
        float width = var5.getScaledWidth();
        float height = var5.getScaledHeight();

        if (grabbed) {
            global.killfeed_x = x/width;
            global.killfeed_y = y/height;
            drawVerticalLine(5, 0, (int)height, 0xffff0000);
            drawVerticalLine((int)width - 5, 0, (int)height, 0xffff0000);
            drawVerticalLine((int)width / 2, 0, (int)height, 0xffff0000);
            drawHorizontalLine(0, (int)width, 5, 0xffff0000);
            drawHorizontalLine(0, (int)width, (int)height - 5, 0xffff0000);
            if (x < 7) {global.killfeed_x = 5/width;}
            if (y < 7) {global.killfeed_y = 5/height;}
            if (x > (int)width - 7) {global.killfeed_x = ((int)width - 5)/width;}
            if (y > (int)height - 7) {global.killfeed_y = ((int)height - 5)/height;}
            if ((x > ((int)width / 2) - 3) && (x < ((int)width / 2) + 3)) {global.killfeed_x = ((int)width / 2)/width;}
            MC.fontRendererObj.drawStringWithShadow(global.killfeed_x + ", " + global.killfeed_y,0, 0,  0xffffffff);
        }

        if (kx < width / 3) {
            if (ky < height / 2) {
                MC.fontRendererObj.drawStringWithShadow("KILLFEED", kx, ky, 0xffffffff);
                drawRect(kx, ky, kx + MC.fontRendererObj.getStringWidth("KILLFEED"), ky + 100, 0x50ffffff);
            } else {
                MC.fontRendererObj.drawStringWithShadow("KILLFEED", kx, ky, 0xffffffff);
                drawRect(kx, ky+10, kx + MC.fontRendererObj.getStringWidth("KILLFEED"), ky - 100, 0x50ffffff);
            }
        } else if (kx > (width - width/3)){
            if (ky < height / 2) {
                MC.fontRendererObj.drawStringWithShadow("KILLFEED", kx - MC.fontRendererObj.getStringWidth("KILLFEED"), ky, 0x50ffffff);
                drawRect(kx - MC.fontRendererObj.getStringWidth("KILLFEED"), ky, kx, ky + 100, 0x50ffffff);
            } else {
                MC.fontRendererObj.drawStringWithShadow("KILLFEED", kx - MC.fontRendererObj.getStringWidth("KILLFEED"), ky, 0x50ffffff);
                drawRect(kx - MC.fontRendererObj.getStringWidth("KILLFEED"), ky+10, kx, ky - 100, 0x50ffffff);
            }
        } else {
            if (ky < height / 2) {
                MC.fontRendererObj.drawStringWithShadow("KILLFEED", kx - MC.fontRendererObj.getStringWidth("KILLFEED")/2, ky, 0x50ffffff);
                drawRect(kx - MC.fontRendererObj.getStringWidth("KILLFEED")/2, ky, kx + MC.fontRendererObj.getStringWidth("KILLFEED")/2, ky + 100, 0x50ffffff);
            } else {
                MC.fontRendererObj.drawStringWithShadow("KILLFEED", kx - MC.fontRendererObj.getStringWidth("KILLFEED")/2, ky, 0x50ffffff);
                drawRect(kx - MC.fontRendererObj.getStringWidth("KILLFEED")/2, ky+10, kx + MC.fontRendererObj.getStringWidth("KILLFEED")/2, ky - 100, 0x50ffffff);
            }
        }

        super.drawScreen(x, y, ticks);
    }
}
