package dev.crossvas.jadexic2c.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class GuiHelper {

    public static final GuiHelper THIS = new GuiHelper();

    /**
     * Modified copy of: <a href="https://github.com/McJtyMods/TheOneProbe/blob/f4797f1a7f1349ab71ac85e667517117a8a8d51a/src/main/java/mcjty/theoneprobe/apiimpl/client/ElementProgressRender.java#L15">ElementProgressRender#render</a>
     */
    public void render(long current, long max, int x, int y, int w, int h, Colors colors, int borderColor) {
        drawThickBeveledBox(x, y, x + w + 1, y + h, 1, borderColor, borderColor, colors.back);
        if (current > 0 && max > 0) {
            int dx = (int) Math.min((current * (w - 2) / max), w - 2);

            if (colors.aColor == colors.bColor) {
                if (dx > 0) {
                    drawThickBeveledBox(x + 1, y + 1, x + dx + 1, y + h - 1, 1, colors.aColor, colors.aColor, colors.aColor);
                }
            } else {
                for (int xx = x + 1; xx <= x + dx + 1; xx++) {
                    int color = (xx & 1) == 0 ? colors.aColor : colors.bColor;
                    drawVerticalLine(xx, y + 1, y + h - 1, color);
                }
            }
        }
    }

    public void renderTank(long current, long max, int x, int y, int width, int height, int borderColor, String fluidName) {
        drawThickBeveledBox(x, y, x + width + 1, y + height, 1, borderColor, borderColor, Color.GRAY.getRGB());
        int start = 1;
        Fluid fluidStack = FluidRegistry.getFluid(fluidName);
        TextureAtlasSprite fluidTexture = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(fluidStack.getStill().toString());
        while (current > 0 && max > 0) {
            int maxX = (int) Math.min(16, current);
            current -= maxX;
            drawTexturedModalRect(x + start, y + 1, fluidTexture, maxX, height - 2);
            start += maxX;
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

    /**
     * Draws a textured rectangle at the stored z-value. Args: x, y, u, v, width, height
     */
    public void drawTexturedModalRect(int x, int y, int u, int v, int width, int height, int twidth, int theight) {
        float zLevel = 0.01f;
        float f = (1.0f/twidth);
        float f1 = (1.0f/theight);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        buffer.pos((x + 0), (y + height), zLevel).tex(((u + 0) * f), ((v + height) * f1)).endVertex();
        buffer.pos((x + width), (y + height), zLevel).tex(((u + width) * f), ((v + height) * f1)).endVertex();
        buffer.pos((x + width), (y + 0), zLevel).tex(((u + width) * f), ((v + 0) * f1)).endVertex();
        buffer.pos((x + 0), (y + 0), zLevel).tex(((u + 0) * f), ((v + 0) * f1)).endVertex();
        tessellator.draw();
    }

    public void drawTexturedModalRect(int x, int y, TextureAtlasSprite sprite, int width, int height) {
        float zLevel = 0.01f;
        float f = (1/256.0f);
        float f1 = (1/256.0f);

        float u1 = sprite.getMinU();
        float v1 = sprite.getMinV();
        float u2 = sprite.getMaxU();
        float v2 = sprite.getMaxV();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos((x + 0), (y + height), zLevel).tex(u1, v1).endVertex();
        buffer.pos((x + width), (y + height), zLevel).tex(u1, v2).endVertex();
        buffer.pos((x + width), (y + 0), zLevel).tex(u2, v2).endVertex();
        buffer.pos((x + 0), (y + 0), zLevel).tex(u2, v1).endVertex();
        tessellator.draw();
    }
}
