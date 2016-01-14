package com.kerbybit.chattriggers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class gui extends GuiScreen {
	
	private float mouseX;
	private float mouseY;
	private Minecraft MC = Minecraft.getMinecraft();
	
	private String backString = "< Back";
	private List<Float> backOffset = new ArrayList<Float>();
	
	private String indevString = "GUI is still in development";
	
	//-1 = setup menu
	//0  = lists
	//1  = setup triggers
	//2  = triggers
	//3  = setup events
	//4  = events
	public static int inMenu = -1;
	
	private String gotoList = "";
	private String gotoTrig = "";
	
	private List<String> tempList = new ArrayList<String>();
	private List<List<Float>> tempListOffset = new ArrayList<List<Float>>();
	
	private List<String> tempTrig = new ArrayList<String>();
	private List<List<Float>> tempTrigOffset = new ArrayList<List<Float>>();
	
	private List<String> tempEvent = new ArrayList<String>();
	private List<List<Float>> tempEventOffset = new ArrayList<List<Float>>();
	
	
	@Override
	public void mouseClicked(int x, int y, int button) throws IOException {
		this.mouseX = (float)x;
		this.mouseY = (float)y;
		ScaledResolution var5 = new ScaledResolution(MC, MC.displayWidth, MC.displayHeight);
		float sWidth = var5.getScaledWidth();
		float sHeight = var5.getScaledHeight();
		
		if (inMenu==0) {
			for (int i=0; i<tempList.size(); i++) {
				if (mouseX>=5 && mouseX<fontRendererObj.getStringWidth(tempList.get(i))+10 && mouseY>=(i*10)+5 && mouseY<(i*10)+15) {
					gotoList=tempList.get(i);
					for (int j=0; j<tempList.size(); j++) {
						if (j<i) {tempListOffset.get(j).set(2, (float)-10);} 
						else if (j>i) {tempListOffset.get(j).set(2, (float)sHeight);} 
						else {tempListOffset.get(j).set(2, (float) 5);}
					}
					inMenu=1;
					backOffset.set(0, sWidth - fontRendererObj.getStringWidth(backString) - 5);
				}
			}
		}
		
		if (inMenu==2) {
			for (int i=0; i<tempTrig.size(); i++) {
				if (mouseX>=5 && mouseX<fontRendererObj.getStringWidth(tempTrig.get(i))+10 && mouseY>=(i*10)+25 && mouseY<(i*10)+35) {
					gotoTrig=tempTrig.get(i);
					for (int j=0; j<tempTrig.size(); j++) {
						if (j<i) {tempTrigOffset.get(j).set(2, (float)-10);} 
						else if (j>i) {tempTrigOffset.get(j).set(2, (float)sHeight);} 
						else {tempTrigOffset.get(j).set(2, (float)5);}
					}
					inMenu=3;
				}
			}
		}
		
		if (mouseX>=sWidth-fontRendererObj.getStringWidth(backString)-5 && mouseX<sWidth-5 && mouseY>=sHeight-15 && mouseY<sHeight-5) {
			if (inMenu == 4) {
				inMenu = 2;
				for (int i=0; i<tempTrig.size(); i++) {
					tempTrigOffset.get(i).set(2, (float)(i*10)+25);
				}
				for (int i=0; i<tempList.size(); i++) {
					if (gotoList==tempList.get(i)) {
						tempListOffset.get(i).set(2, (float)5);
					}
				}
				for (int i=0; i<tempEvent.size(); i++) {
					tempEventOffset.get(i).set(0, (float)-fontRendererObj.getStringWidth(tempEvent.get(i))-5);
				}
			} else if (inMenu == 2){
				for (int i=0; i<tempList.size(); i++) {
					tempListOffset.get(i).set(2, (float)(i*10)+5);
				}
				for (int i=0; i<tempTrig.size(); i++) {
					tempTrigOffset.get(i).set(0, (float) -fontRendererObj.getStringWidth(tempTrig.get(i))-5);
				}
				backOffset.set(0, sWidth);
				inMenu = 0;
			}
		}
		
		super.mouseClicked(x, y, button);
	}

	@Override
	public void drawScreen(int x, int y, float ticks) { 
		this.mouseX = (float)x;
		this.mouseY = (float)y;
		ScaledResolution var5 = new ScaledResolution(MC, MC.displayWidth, MC.displayHeight);
		float sWidth = var5.getScaledWidth();
		float sHeight = var5.getScaledHeight();
		
		GL11.glColor4f(1, 1, 1, 1);
		drawDefaultBackground();
		
		if (inMenu == -1) {
			inMenu = 0;

			backOffset.add(sWidth); 
			backOffset.add(sWidth);
			
			int i = 0;
			for (List<String> value : global.trigger) {
				String whatList = "no list";
				Boolean isInList = false;
				if (value.get(1).contains("<list=") && value.get(1).contains(">")) {
					whatList = value.get(1).substring(value.get(1).indexOf("<list=")+6, value.get(1).indexOf(">",value.get(1).indexOf("<list=")));
				}
				
				for (String listvalue : tempList) {
					if (listvalue.equals(whatList)) {isInList = true;}
				}
				
				if (!isInList) {
					tempList.add(whatList);
					
					List<Float> temporary = new ArrayList<Float>();
					temporary.add((float) 5); temporary.add((float) 0); temporary.add((float) (i*10)+5); temporary.add((float) (i*10)+5);
					tempListOffset.add(temporary);
					i++;
				}
			}
		}
		
		if (inMenu == 1) {
			inMenu = 2;
			tempTrig.clear();
			tempTrigOffset.clear();
			
			int i=0;
			for (List<String> value : global.trigger) {
				String whatList = "no list";
				Boolean isInList = false;
				if (value.get(1).contains("<list=") && value.get(1).contains(">")) {
					whatList = value.get(1).substring(value.get(1).indexOf("<list=")+6, value.get(1).indexOf(">",value.get(1).indexOf("<list=")));
				}
				
				if (whatList.equals(gotoList)) {
					tempTrig.add(value.get(1).replace("<list=" + whatList + ">", ""));
					List<Float> temporary = new ArrayList<Float>();
					temporary.add((float) 5); temporary.add((float) -fontRendererObj.getStringWidth(value.get(1).replace("<list=" + whatList + ">", ""))); temporary.add((float) (i*10)+25); temporary.add((float) (i*10)+25);
					tempTrigOffset.add(temporary);
					i++;
				}
			}
		}
		
		if (inMenu == 3) {
			inMenu = 4;
			tempEvent.clear();
			tempEventOffset.clear();
			
			for (int i=0; i<tempList.size(); i++) {
				if (gotoList==tempList.get(i)) {
					tempListOffset.get(i).set(2, (float)-10);
				}
			}
			
			int i=0;
			for (List<String> value : global.trigger) {
				String whatList = "no list";
				if (value.get(1).contains("<list=") && value.get(1).contains(">")) {
					whatList = value.get(1).substring(value.get(1).indexOf("<list=")+6, value.get(1).indexOf(">",value.get(1).indexOf("<list=")));
				}
				
				if (value.get(1).replace("<list=" + whatList + ">", "").equals(gotoTrig)) {
					int tabbedLogic = 0;
					for (int j=2; j<value.size(); j++) {
						
						if (value.get(j).toUpperCase().startsWith("END")
						|| value.get(j).toUpperCase().startsWith("ELSE")) {
							tabbedLogic--;
						}
						
						tempEvent.add(value.get(j));
						List<Float> temporary = new ArrayList<Float>();
						temporary.add((float) 5 + (tabbedLogic*5)); temporary.add((float) -fontRendererObj.getStringWidth(value.get(j))); temporary.add((float) (i*10)+25); temporary.add((float) (i*10)+25);
						tempEventOffset.add(temporary);
						i++;
						
						if (value.get(j).toUpperCase().startsWith("IF")
						|| value.get(j).toUpperCase().startsWith("CHOOSE")
						|| value.get(j).toUpperCase().startsWith("FOR")
						|| value.get(j).toUpperCase().startsWith("WAIT")
						|| value.get(j).toUpperCase().startsWith("ELSE")) {
							tabbedLogic++;
						}
						
						
					}
				}
			}
		}
		
		fontRendererObj.drawStringWithShadow(backString, backOffset.get(1), sHeight-15, 0xffffff);
		fontRendererObj.drawString(indevString, (int) (sWidth/2 - fontRendererObj.getStringWidth(indevString)/2), 5, 0x000000);
		
		for (int i=0; i<tempList.size(); i++) {
			fontRendererObj.drawStringWithShadow(tempList.get(i), tempListOffset.get(i).get(1), tempListOffset.get(i).get(3), 0xffffff);
			
			if (inMenu==0) {
				if (mouseX>=5 && mouseX<fontRendererObj.getStringWidth(tempList.get(i))+10 && mouseY>=(i*10)+5 && mouseY<(i*10)+15) {
					tempListOffset.get(i).set(0, (float) 10);
				} else {
					tempListOffset.get(i).set(0, (float) 5);
				}
			}
		}
		
		for (int i=0; i<tempTrig.size(); i++) {
			fontRendererObj.drawStringWithShadow(tempTrig.get(i), tempTrigOffset.get(i).get(1), tempTrigOffset.get(i).get(3), 0xffffff);
			
			if (inMenu==2) {
				if (mouseX>=5 && mouseX<fontRendererObj.getStringWidth(tempTrig.get(i))+10 && mouseY>=(i*10)+25 && mouseY<(i*10)+35) {
					tempTrigOffset.get(i).set(0, (float) 10);
				} else {
					tempTrigOffset.get(i).set(0, (float) 5);
				}
			}
		}
		
		for (int i=0; i<tempEvent.size(); i++) {
			fontRendererObj.drawStringWithShadow(tempEvent.get(i), tempEventOffset.get(i).get(1), tempEventOffset.get(i).get(3), 0xffffff);
		}
		
		for (List<Float> value : tempListOffset) {
			if (Math.floor(Math.abs(value.get(1)-value.get(0))) > 0) {
				value.set(1, value.get(1) - (value.get(1)-value.get(0))/10);
			} else {value.set(1, value.get(0));}
			if (Math.floor(Math.abs(value.get(3)-value.get(2))) > 0) {
				value.set(3, value.get(3) - (value.get(3)-value.get(2))/10);
			} else {value.set(3, value.get(2));}
		}
		
		for (List<Float> value : tempTrigOffset) {
			if (Math.floor(Math.abs(value.get(1)-value.get(0))) > 0) {
				value.set(1, value.get(1) - (value.get(1)-value.get(0))/10);
			} else {value.set(1, value.get(0));}
			if (Math.floor(Math.abs(value.get(3)-value.get(2))) > 0) {
				value.set(3, value.get(3) - (value.get(3)-value.get(2))/10);
			} else {value.set(3, value.get(2));}
		}
		
		for (List<Float> value : tempEventOffset) {
			if (Math.floor(Math.abs(value.get(1)-value.get(0))) > 0) {
				value.set(1, value.get(1) - (value.get(1)-value.get(0))/10);
			} else {value.set(1, value.get(0));}
			if (Math.floor(Math.abs(value.get(3)-value.get(2))) > 0) {
				value.set(3, value.get(3) - (value.get(3)-value.get(2))/10);
			} else {value.set(3, value.get(2));}
		}
		
		if (Math.floor(Math.abs(backOffset.get(1)-backOffset.get(0))) > 0) {
			backOffset.set(1, backOffset.get(1) - (backOffset.get(1)-backOffset.get(0))/10);
		} else {backOffset.set(1, backOffset.get(0));}
		
		super.drawScreen(x, y, ticks);
	}
}
