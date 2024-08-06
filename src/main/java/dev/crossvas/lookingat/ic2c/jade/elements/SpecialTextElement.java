package dev.crossvas.lookingat.ic2c.jade.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.ui.Element;
import snownee.jade.impl.ui.TextElement;
import snownee.jade.overlay.DisplayHelper;
import snownee.jade.overlay.OverlayRenderer;

public class SpecialTextElement extends TextElement {

    private float scale = 1;
    private int zOffset;
    private boolean centered;

    public SpecialTextElement(Component component) {
        super(component);
    }

    @Override
    public Vec2 getSize() {
        Font font = Minecraft.getInstance().font;
        return new Vec2(font.width(text) * scale, font.lineHeight * scale + 1);
    }

    @Override
    public void render(PoseStack matrixStack, float x, float y, float maxX, float maxY) {
        matrixStack.pushPose();
        Font font = Minecraft.getInstance().font;
        if (centered) {
            x += (maxX - x - font.width(text) * scale) / 2;
        }
        matrixStack.translate(x, y + scale, zOffset);
        matrixStack.scale(scale, scale, 1);
        DisplayHelper.INSTANCE.drawText(matrixStack, text, 0, 0, OverlayRenderer.normalTextColorRaw);
        matrixStack.popPose();
    }

    public SpecialTextElement toSpecial() {
        return this;
    }

    public Element scale(float scale) {
        this.scale = scale;
        return this;
    }

    public Element zOffset(int zOffset) {
        this.zOffset = zOffset;
        return this;
    }

    public Element centered(boolean centered) {
        this.centered = centered;
        return this;
    }
}
