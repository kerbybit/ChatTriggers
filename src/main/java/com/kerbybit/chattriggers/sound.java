package com.kerbybit.chattriggers;

import net.minecraft.client.Minecraft;

public class sound {
	public static void play(String snd) {
		Minecraft.getMinecraft().thePlayer.playSound(snd, 1000, 1);
	}
}
