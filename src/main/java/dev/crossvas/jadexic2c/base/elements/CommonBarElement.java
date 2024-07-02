package dev.crossvas.jadexic2c.base.elements;

import dev.crossvas.jadexic2c.JadeTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;

public class CommonBarElement extends CommonElement {

    int CURRENT;
    int MAX;
    int COLOR;
    Component TEXT;

    public CommonBarElement(int current, int max, Component text, int color) {
        super(new Vec2(0, 0), "LEFT");
        this.CURRENT = current;
        this.MAX = max;
        this.TEXT = text;
        this.COLOR = color;
    }

    public Component getText() {
        return this.TEXT;
    }

    public int getCurrent() {
        return this.CURRENT;
    }

    public int getMax() {
        return this.MAX;
    }

    public int getColor() {
        return this.COLOR;
    }

    public static CommonBarElement load(CompoundTag tag) {
        Component text = Component.Serializer.fromJson(tag.getString("text"));
        int current = tag.getInt("current");
        int max = tag.getInt("max");
        int color = tag.getInt("color");
        return new CommonBarElement(current, max, text, color);
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putString("text", Component.Serializer.toStableJson(this.TEXT));
        tag.putInt("current", this.CURRENT);
        tag.putInt("max", this.MAX);
        tag.putInt("color", this.COLOR);
        return tag;
    }

    @Override
    public String getTagId() {
        return JadeTags.JADE_ADDON_BAR_TAG;
    }
}
