package dev.crossvas.waila.ic2.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import org.lwjgl.opengl.GL11;

public class GuiHelper {

    public static final GuiHelper THIS = new GuiHelper();

    /**
     * Modified copy of: <a href="https://github.com/McJtyMods/TheOneProbe/blob/f4797f1a7f1349ab71ac85e667517117a8a8d51a/src/main/java/mcjty/theoneprobe/apiimpl/client/ElementProgressRender.java#L15">ElementProgressRender#render</a>
     */
    public void render(long current, long max, int x, int y, int w, int h, int mainColor, String fluidIcon) {
        drawThickBeveledBox(x, y, x + w + 1, y + h, 1, ColorUtils.WHITE, ColorUtils.WHITE, ColorUtils.DARK_GRAY);
        if (!fluidIcon.equals("0")) {
            renderTank(x, y, w, h, mainColor, (int) current, (int) max, fluidIcon);
        } else {
            if (current > 0 && max > 0) {
                int dx = (int) Math.min((current * (w - 2) / max), w - 2);
                int secondColor = ColorUtils.darker(mainColor);
                if (mainColor == secondColor) {
                    if (dx > 0) {
                        drawThickBeveledBox(x + 1, y + 1, x + dx + 1, y + h - 1, 1, mainColor, mainColor, mainColor);
                    }
                } else {
                    for (int xx = x + 1; xx <= x + dx + 1; xx++) {
                        int color = (xx & 1) == 0 ? mainColor : secondColor;
                        drawVerticalLine(xx, y + 1, y + h - 1, color);
                    }
                }
            }
        }
    }

    /**
     * Copy of: <a href="https://github.com/McJtyMods/TheOneProbe/blob/f4797f1a7f1349ab71ac85e667517117a8a8d51a/src/main/java/mcjty/theoneprobe/rendering/RenderHelper.java#L209">RenderHelper#drawVerticalLine</a>
     * */
    public void drawVerticalLine(int x1, int y1, int y2, int color) {
        Gui.drawRect(x1, y1, x1 + 1, y2, color);
    }

    /**
     * Copy of: <a href="https://github.com/McJtyMods/TheOneProbe/blob/f4797f1a7f1349ab71ac85e667517117a8a8d51a/src/main/java/mcjty/theoneprobe/rendering/RenderHelper.java#L275">RenderHelper#drawThickBeveledBox</a>
     * */
    public void drawThickBeveledBox(int x1, int y1, int x2, int y2, int thickness, int topleftcolor, int botrightcolor, int fillcolor) {
        if (fillcolor != -1) {
            Gui.drawRect(x1 + 1, y1 + 1, x2 - 1, y2 - 1, fillcolor);
        }
        Gui.drawRect(x1, y1, x2 - 1, y1 + thickness, topleftcolor);
        Gui.drawRect(x1, y1, x1 + thickness, y2 - 1, topleftcolor);
        Gui.drawRect(x2 - thickness, y1, x2, y2 - 1, botrightcolor);
        Gui.drawRect(x1, y2 - thickness, x2, y2, botrightcolor);
    }

    public static void renderTank(int x, int y, int width, int height, int color, int current, int capacity, String fluidIcon) {
        TextureMap map = Minecraft.getMinecraft().getTextureMapBlocks();
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        int start = 1;
        width -= 1;
        TextureAtlasSprite liquidIcon = map.getAtlasSprite(fluidIcon);
        int lvl = (int) (((double) current / capacity) * width);
        GL11.glColor4f((float)(color >> 16 & 255) / 255.0F, (float)(color >> 8 & 255) / 255.0F, (float)(color & 255) / 255.0F, (float)(color >> 24 & 255) / 255.0F);
        while (lvl > 0) {
            int maxX = Math.min(16, lvl);
            lvl -= maxX;
            drawTexturedModalRect(x + start, y + 1, liquidIcon, maxX, height - 2);
            start += maxX;
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public static void drawTexturedModalRect(int x, int y, TextureAtlasSprite sprite, int width, int height) {
        float zLevel = 0.01f;
        float u1 = sprite.getMinU();
        float v1 = sprite.getMinV();
        float u2 = sprite.getMaxU();
        float v2 = sprite.getMaxV();

        GL11.glDisable(3553); // GL_TEXTURE_2D
        GL11.glEnable(3042); // GL_BLEND
        GL11.glDisable(3008); // GL_ALPHA_TEST
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(7425); // GL_SMOOTH
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, zLevel, u1, v1);
        tessellator.addVertexWithUV(x + width, y + height, zLevel, u1, v2);
        tessellator.addVertexWithUV(x + width, y, zLevel, u2, v2);
        tessellator.addVertexWithUV(x, y, zLevel, u2, v1);
        tessellator.draw();
        GL11.glShadeModel(7424); // GL_FLAT
        GL11.glDisable(3042); // GL_BLEND
        GL11.glEnable(3008); // GL_ALPHA_TEST
        GL11.glEnable(3553); // GL_TEXTURE_2D
    }
}
