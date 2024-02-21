package dev.crossvas.jadexic2c.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.ui.Element;
import snownee.jade.overlay.DisplayHelper;

public class SpecialItemStackElement extends Element {

    private float scale = 1;
    private final Align align;
    private final ItemStack stack;
    private final int x;
    private final int y;

    private boolean centered;

    public SpecialItemStackElement(ItemStack stack, Align align, int x, int y) {
        this.stack = stack;
        this.align = align;
        this.x = x;
        this.y = y;
    }

    @Override
    public Vec2 getSize() {
        int size = Mth.floor(18.0F * this.scale);
        return new Vec2((float)size, (float)size);
    }

    @Override
    public void render(PoseStack poseStack, float x, float y, float maxX, float maxY) {
        if (!this.stack.isEmpty()) {
            if (centered) { // align value is ignored
                x += (maxX - x - 16 * this.scale) / 2;
                y = this.y;
            } else {
                switch (this.align) {
                    case LEFT -> {
                        x = this.x;
                        y = this.y;
                    }
                    case RIGHT -> {
                        x = maxX - this.x;
                        y = this.y;
                    }
                }
            }
            DisplayHelper.INSTANCE.drawItem(poseStack, x, y, this.stack, this.scale, Component.empty().getString());
        }
    }

    public Element scale(float scale) {
        this.scale = scale;
        return this;
    }

    public Element centered(boolean centered) {
        this.centered = centered;
        return this;
    }
}
