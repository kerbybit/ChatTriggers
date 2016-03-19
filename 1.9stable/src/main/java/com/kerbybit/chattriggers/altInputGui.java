package com.kerbybit.chattriggers;

import java.io.IOException;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class altInputGui extends GuiScreen {
	@Override
	public void drawScreen(int x, int y, float ticks) {
		GL11.glColor4f(1, 1, 1, 1);
		
		
		
		super.drawScreen(x, y, ticks);
	}
}
