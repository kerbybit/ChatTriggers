package com.kerbybit.chattriggers.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
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
                        meta = Integer.parseInt(get_name.substring(get_name.indexOf(":")+1));
                    } catch (NumberFormatException e) {
                        meta = 0;
                        color = get_name.substring(get_name.indexOf(":")+1);
                    }
                }
                Item item = Item.getByNameOrId(get_final_name);
                ItemStack itemStack = new ItemStack(item,1, meta);
                NBTTagCompound nbt = itemStack.getTagCompound();
                if (nbt==null) {
                    nbt = new NBTTagCompound();
                    itemStack.setTagCompound(nbt);
                }
                if (color!=null) {
                    try {
                        NBTTagCompound colorCompound = new NBTTagCompound();
                        colorCompound.setInteger("color", Integer.parseInt(color.substring(1)));
                        nbt.setTag("display", colorCompound);
                    } catch (NumberFormatException e) {
                        //do nothing
                    }
                }
                if (item != null) {
                    drawItemIcon(x + Minecraft.getMinecraft().fontRendererObj.getStringWidth(before_value), y-4, itemStack);
                    return drawIcons(input.replace("{icon["+get_name+"]}", "    "), x, y);
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
                || name.equals("mining fatigue") || name.equals("wither"));
    }

    private static Potion getPotionByName(String name) {
        if (name.equals("speed")) {
            return Potion.moveSpeed;
        } else if (name.equals("slowness")) {
            return Potion.moveSlowdown;
        } else if (name.equals("regeneration")) {
            return Potion.regeneration;
        } else if (name.equals("fire resistance")) {
            return Potion.fireResistance;
        } else if (name.equals("poison")) {
            return Potion.poison;
        } else if (name.equals("night vision")) {
            return Potion.nightVision;
        } else if (name.equals("weakness")) {
            return Potion.weakness;
        } else if (name.equals("strength")) {
            return Potion.damageBoost;
        } else if (name.equals("jump boost")) {
            return Potion.jump;
        } else if (name.equals("water breathing")) {
            return Potion.waterBreathing;
        } else if (name.equals("invisibility")) {
            return Potion.invisibility;
        } else if (name.equals("hunger")) {
            return Potion.hunger;
        } else if (name.equals("blindness")) {
            return Potion.blindness;
        } else if (name.equals("saturation")) {
            return Potion.saturation;
        } else if (name.equals("absorption")) {
            return Potion.absorption;
        } else if (name.equals("health boost")) {
            return Potion.healthBoost;
        } else if (name.equals("mining fatigue")) {
            return Potion.digSlowdown;
        } else if (name.equals("wither")) {
            return Potion.wither;
        }
        return null;
    }

    public static String getPotionColors(String name) {
        if (name.equals("speed")) {
            return "&b";
        } else if (name.equals("slowness")) {
            return "&9";
        } else if (name.equals("strength")) {
            return "&4";
        } else if (name.equals("weakness")) {
            return "&5";
        } else if (name.equals("jump boost")) {
            return "&a";
        } else if (name.equals("poison")) {
            return "&2";
        } else if (name.equals("fire resistance")
                || name.equals("health boost")) {
            return "&c";
        } else if (name.equals("water breathing")) {
            return "&3";
        } else if (name.equals("regeneration")) {
            return "&d";
        } else if (name.equals("night vision")) {
            return "&1";
        } else if (name.equals("invisibility")) {
            return "&7";
        } else if (name.equals("hunger")) {
            return "&2";
        } else if (name.equals("nausea")
                || name.equals("wither")
                || name.equals("mining fatigue")) {
            return "&8";
        } else if (name.equals("saturation")
                || name.equals("absorption")) {
            return "&6";
        }
        return "&r";
    }

    public static String simplifyPotionName(String name) {
        if (name.equals("moveSpeed")) {
            return "speed";
        } else if (name.equals("fireResistance")) {
            return "fire resistance";
        } else if (name.equals("nightVision")) {
            return "night vision";
        } else if (name.equals("moveSlowdown")) {
            return "slowness";
        } else if (name.equals("damageBoost")) {
            return "strength";
        } else if (name.equals("jump")) {
            return "jump boost";
        } else if (name.equals("waterBreathing")) {
            return "water breathing";
        } else if (name.equals("confusion")) {
            return "nausea";
        } else if (name.equals("digSlowDown")) {
            return "mining fatigue";
        } else if (name.equals("healthBoost")) {
            return "health boost";
        }
        return name;
    }
}
