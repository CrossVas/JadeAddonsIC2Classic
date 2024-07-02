package dev.crossvas.jadexic2c.base.elements;

import dev.crossvas.jadexic2c.JadeTags;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;

public class CommonTextElement extends CommonElement {

    Component TEXT;
    boolean CENTERED;

    public CommonTextElement(Component text, boolean centered) {
        this(text, new Vec2(Minecraft.getInstance().font.width(text.getString()), 10),
                new Vec2(0, 0),
                "LEFT", centered);
    }

    public CommonTextElement(Component text, Vec2 size, Vec2 translation, String side, boolean centered) {
        super(size, translation, side);
        this.TEXT = text;
        this.CENTERED = centered;
    }

    public Component getText() {
        return TEXT;
    }

    public boolean isCentered() {
        return this.CENTERED;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putString("text", Component.Serializer.toStableJson(TEXT));
        tag.putString("side", SIDE);
        tag.putInt("sizeX", (int) SIZE.x);
        tag.putInt("sizeY", (int) SIZE.y);
        tag.putInt("translationX", (int) TRANSLATION.x);
        tag.putInt("translationY", (int) TRANSLATION.y);
        tag.putBoolean("centered", this.CENTERED);
        return tag;
    }

    public static CommonTextElement load(CompoundTag tag) {
        Component text = Component.Serializer.fromJson(tag.getString("text"));
        Vec2 size = new Vec2(tag.getInt("sizeX"), tag.getInt("sizeY"));
        Vec2 translation = new Vec2(tag.getInt("translationX"), tag.getInt("translationY"));
        String side = tag.getString("side");
        boolean centered = tag.getBoolean("centered");
        return new CommonTextElement(text, size, translation, side, centered);
    }

    @Override
    public String getTagId() {
        return JadeTags.JADE_ADDON_TEXT_TAG;
    }
}
