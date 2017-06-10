package com.kerbybit.chattriggers.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.potion.Potion;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class IconHandler {
    private static ResourceLocation resourceLocation = new ResourceLocation("textures/gui/container/inventory.png");
    private static void drawPotionIcon(int x, int y, Potion potion) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);

        if(potion.hasStatusIcon()) {
            int iconIndex = potion.getStatusIconIndex();
            int u = iconIndex % 8 * 18;
            int v = 198 + iconIndex / 8 * 18;
            int width = 18;
            int height = 18;
            float scaler = 0.5f;

            GL11.glColor4f(1f, 1f, 1f, 1f);

            renderCustomTexture(x, y, u, v, width, height, scaler);
        }
    }

    private static void renderCustomTexture(int x, int y, int u, int v, int width, int height, float scale) {
        x /= scale;
        y /= scale;

        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);

        Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x, y, u, v, width, height);

        GL11.glPopMatrix();
    }

    private static void drawItemIcon(int x, int y, ItemStack itemStack) {
        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderHelper.enableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        itemRenderer.zLevel = 200.0F;

        itemRenderer.renderItemIntoGUI(itemStack, x, y);
    }

    public static String drawIcons(String input, int x, int y) {
        if (input.contains("{icon[") && input.contains("]}")) {
            String get_name = input.substring(input.indexOf("{icon[")+6, input.indexOf("]}", input.indexOf("{icon[")));
            String before_value = input.substring(0, input.indexOf("{icon["+get_name+"]}"));
            if (isPotion(get_name.toLowerCase())) {
                Potion potion = getPotionByName(get_name.toLowerCase());
                if (potion != null) {
                    if (get_name.equalsIgnoreCase("health boost")) {
                        drawPotionIcon(x + Minecraft.getMinecraft().fontRendererObj.getStringWidth(before_value)+4, y-1, Potion.regeneration);
                        drawPotionIcon(x + Minecraft.getMinecraft().fontRendererObj.getStringWidth(before_value)+2, y-1, Potion.regeneration);
                        drawPotionIcon(x + Minecraft.getMinecraft().fontRendererObj.getStringWidth(before_value), y-1, Potion.regeneration);
                    } else if (get_name.equalsIgnoreCase("saturation")) {
                        return drawIcons(input.replace("{icon["+get_name+"]}", "{icon[golden_carrot]}"), x, y);
                    } else {
                        drawPotionIcon(x + Minecraft.getMinecraft().fontRendererObj.getStringWidth(before_value)+2, y-1, potion);
                    }
                }
            } else {
                String get_final_name = get_name;
                int meta = 0;
                String color = null;
                if (get_name.contains(":")) {
                    get_final_name = get_name.substring(0, get_name.indexOf(":"));
                    try {
                    	if (get_name.contains("{")) {
							meta = Integer.parseInt(get_name.substring(get_name.indexOf(":")+1, get_name.indexOf("{")));
						} else {
							meta = Integer.parseInt(get_name.substring(get_name.indexOf(":")+1));
						}
                    } catch (NumberFormatException e) {
                        meta = 0;
                        color = get_name.substring(get_name.indexOf(":")+1);
                    }
                }

                String skullOwner = null;
                if (get_name.contains("{SkullOwner:\"")) {
                	skullOwner = get_name.substring(get_name.indexOf("{SkullOwner:\"") + 13, get_name.indexOf("\"}"));

                	input = input.replace("{SkullOwner:\"" + skullOwner + "\"}", "");
				}

                Item item = Item.getByNameOrId(get_final_name);
                ItemStack itemStack = new ItemStack(item,1, meta);

                NBTTagCompound nbt = itemStack.getTagCompound();
                if (nbt == null) {
                    nbt = new NBTTagCompound();
                    itemStack.setTagCompound(nbt);
                }

                if (color != null) {
                    try {
                        NBTTagCompound colorCompound = new NBTTagCompound();
                        colorCompound.setInteger("color", Integer.parseInt(color.substring(1)));
                        nbt.setTag("display", colorCompound);
                    } catch (NumberFormatException e) {
                        //do nothing
                    }
                }

                if (skullOwner != null && item instanceof ItemSkull) {
                	nbt.setTag("SkullOwner", new NBTTagString(skullOwner));
					itemStack.getItem().updateItemStackNBT(nbt);
				}

                if (item != null) {
                    drawItemIcon(x + Minecraft.getMinecraft().fontRendererObj.getStringWidth(before_value), y-4, itemStack);
                    return drawIcons(input.replaceFirst("\\{icon\\["+get_name+"]}", "    "), x, y);
                }
            }
            return drawIcons(input.replace("{icon["+get_name+"]}", "   "), x, y);
        }
        return input;
    }

    public static String removeIconString(String input) {
        if (input.contains("{icon[") && input.contains("]}")) {
            String get_name = input.substring(input.indexOf("{icon[")+6, input.indexOf("]}", input.indexOf("{icon[")));
            if (isPotion(get_name)) {
                return removeIconString(input.replace("{icon["+get_name+"]}", "   "));
            }
            return removeIconString(input.replace("{icon["+get_name+"]}", "    "));
        } else {
            return input;
        }
    }

    private static Boolean isPotion(String name) {
        return (name.equals("speed") || name.equals("slowness")
                ||name.equals("stength") || name.equals("weakness")
                || name.equals("regeneration") || name.equals("fire resistance")
                || name.equals("poison") || name.equals("night vision")
                || name.equals("jump boost") || name.equals("water breathing")
                || name.equals("invisibility") || name.equals("hunger")
                || name.equals("blindness") || name.equals("saturation")
                || name.equals("absorption") || name.equals("health boost")
                || name.equals("mining fatigue") || name.equals("wither")
                || name.equals("resistance"));
    }

    private static Potion getPotionByName(String name) {
        switch (name) {
            case "speed":
                return Potion.moveSpeed;
            case "slowness":
                return Potion.moveSlowdown;
            case "regeneration":
                return Potion.regeneration;
            case "fire resistance":
                return Potion.fireResistance;
            case "poison":
                return Potion.poison;
            case "night vision":
                return Potion.nightVision;
            case "weakness":
                return Potion.weakness;
            case "strength":
                return Potion.damageBoost;
            case "jump boost":
                return Potion.jump;
            case "water breathing":
                return Potion.waterBreathing;
            case "invisibility":
                return Potion.invisibility;
            case "hunger":
                return Potion.hunger;
            case "blindness":
                return Potion.blindness;
            case "saturation":
                return Potion.saturation;
            case "absorption":
                return Potion.absorption;
            case "health boost":
                return Potion.healthBoost;
            case "mining fatigue":
                return Potion.digSlowdown;
            case "wither":
                return Potion.wither;
            case "resistance":
                return Potion.resistance;
        }
        return null;
    }

    public static String getPotionColors(String name) {
        switch (name) {
            case "speed":
                return "&b";
            case "slowness":
                return "&9";
            case "strength":
                return "&4";
            case "weakness":
                return "&5";
            case "jump boost":
                return "&a";
            case "poison":
                return "&2";
            case "fire resistance":
            case "health boost":
                return "&c";
            case "water breathing":
                return "&3";
            case "regeneration":
                return "&d";
            case "night vision":
                return "&1";
            case "invisibility":
            case "resistance":
                return "&7";
            case "hunger":
                return "&2";
            case "nausea":
            case "wither":
            case "mining fatigue":
                return "&8";
            case "saturation":
            case "absorption":
                return "&6";
        }
        return "&r";
    }

    public static String simplifyPotionName(String name) {
        switch (name) {
            case "moveSpeed":
                return "speed";
            case "fireResistance":
                return "fire resistance";
            case "nightVision":
                return "night vision";
            case "moveSlowdown":
                return "slowness";
            case "damageBoost":
                return "strength";
            case "jump":
                return "jump boost";
            case "waterBreathing":
                return "water breathing";
            case "confusion":
                return "nausea";
            case "digSlowDown":
                return "mining fatigue";
            case "healthBoost":
                return "health boost";
        }
        return name;
    }
}
