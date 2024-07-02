package dev.crossvas.jadexic2c.base.elements;

import dev.crossvas.jadexic2c.JadeTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec2;

public class CommonPaddingElement extends CommonElement {

    int X;
    int Y;

    public CommonPaddingElement(int x, int y) {
        super(new Vec2(0, 0), new Vec2(0, 0), "LEFT");
        this.X = x;
        this.Y = y;
    }

    public int getX() {
        return this.X;
    }

    public int getY() {
        return this.Y;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("x", this.X);
        tag.putInt("y", this.Y);
        return tag;
    }

    public static CommonPaddingElement load(CompoundTag tag) {
        int x = tag.getInt("x");
        int y = tag.getInt("y");
        return new CommonPaddingElement(x, y);
    }

    @Override
    public String getTagId() {
        return JadeTags.JADE_ADDON_PADDING_TAG;
    }
}
