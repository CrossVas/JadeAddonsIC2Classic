package dev.crossvas.jadexic2c.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.config.IWailaConfig;
import snownee.jade.impl.ui.ProgressStyle;
import snownee.jade.overlay.DisplayHelper;
import snownee.jade.overlay.OverlayRenderer;

import java.util.Objects;

public class SpecialProgressStyle extends ProgressStyle {

    @Override
    public void render(PoseStack matrixStack, float x, float y, float width, float height, float progress, Component text) {
        progress *= this.choose(true, width, height);
        float progressY = y;
        if (this.vertical) {
            progressY = y + (height - progress);
        }

        int color;
        if (progress > 0.0F) {
            if (this.overlay != null) {
                Vec2 size = new Vec2(this.choose(true, progress, width), this.choose(false, progress, height));
                this.overlay.size(size);
                this.overlay.render(matrixStack, x, progressY, size.x, size.y);
            } else {
                int alpha = (int) ((float) (this.color >> 24 & 255) * 0.7F);
                color = this.color & 16777215 | alpha << 24;
                float half = choose(true, height, width) / 2.0F;
                DisplayHelper.INSTANCE.drawGradientRect(matrixStack, x, progressY, this.choose(true, progress, half), this.choose(false, progress, half), color, this.color, this.vertical);
                DisplayHelper.INSTANCE.drawGradientRect(matrixStack, x + this.choose(false, half, 0.0F), progressY + this.choose(true, half, 0.0F), this.choose(true, progress, half), this.choose(false, progress, half), this.color, color, this.vertical);
                if (this.color != this.color2) {
                    float yy;
                    float fy;
                    if (this.vertical) {
                        for (yy = y + height; yy > progressY; yy -= 2.0F) {
                            fy = Math.max(progressY, yy + 1.0F);
                            DisplayHelper.fill(matrixStack, x, yy, x + width, fy, this.color2);
                        }
                    } else {
                        for (yy = x + 1.0F; yy < x + progress; yy += 2.0F) {
                            fy = Math.min(x + width, yy + 1.0F);
                            DisplayHelper.fill(matrixStack, yy, y, fy, y + height, this.color2);
                        }
                    }
                }
            }
        }

        if (text != null) {
            Font font = Minecraft.getInstance().font;
            if (this.autoTextColor) {
                this.autoTextColor = false;
                if (this.overlay == null && RGBtoHSV(this.color2).z() > 0.75F) {
                    this.textColor = -16777216;
                } else {
                    this.textColor = -1;
                }
            }

            Objects.requireNonNull(font);
            y += height - 9.0F;
            if (this.vertical) {
                Objects.requireNonNull(font);
                if (9.0F < progress) {
                    y -= progress;
                    Objects.requireNonNull(font);
                    y += (float) (9 + 2);
                }
            }

            color = IWailaConfig.IConfigOverlay.applyAlpha(this.textColor, OverlayRenderer.alpha);
            if (this.glowText) {
                MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
                font.drawInBatch8xOutline(text.getVisualOrderText(), x + 1.0F, y, -1, -13421773, matrixStack.last().pose(), multibuffersource$buffersource, 15728880);
                multibuffersource$buffersource.endBatch();
            } else if (this.shadow) {
                font.drawShadow(matrixStack, text, x + 1.0F, y, color);
            } else {
                font.draw(matrixStack, text, x + 1.0F, y, color);
            }
        }
    }

    private float choose(boolean expand, float x, float y) {
        return this.vertical ^ expand ? x : y;
    }

    private static Vector3f RGBtoHSV(int rgb) {
        int r = rgb >> 16 & 255;
        int g = rgb >> 8 & 255;
        int b = rgb & 255;
        int max = Math.max(r, Math.max(g, b));
        int min = Math.min(r, Math.min(g, b));
        float v = (float) max;
        float delta = (float) (max - min);
        float h;
        float s;
        if (max != 0) {
            s = delta / (float) max;
            if (r == max) {
                h = (float) (g - b) / delta;
            } else if (g == max) {
                h = 2.0F + (float) (b - r) / delta;
            } else {
                h = 4.0F + (float) (r - g) / delta;
            }

            h /= 6.0F;
            if (h < 0.0F) {
                ++h;
            }

            return new Vector3f(h, s, v / 255.0F);
        } else {
            s = 0.0F;
            h = -1.0F;
            return new Vector3f(h, s, 0.0F);
        }
    }
}
