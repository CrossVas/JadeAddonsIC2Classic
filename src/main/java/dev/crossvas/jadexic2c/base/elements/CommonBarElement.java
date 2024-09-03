package dev.crossvas.jadexic2c.base.elements;

import dev.crossvas.jadexic2c.JadeTags;
import dev.crossvas.jadexic2c.base.interfaces.IJadeElementBuilder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;

public class CommonBarElement implements IJadeElementBuilder {

    int CURRENT;
    int MAX;
    int COLOR;
    ITextComponent TEXT;
    String TEXTURE_DATA;

    public CommonBarElement(int current, int max, ITextComponent text, int color, String textureData) {
        this.CURRENT = current;
        this.MAX = max;
        this.TEXT = text;
        this.COLOR = color;
        this.TEXTURE_DATA = textureData;
    }

    public ITextComponent getText() {
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

    public String getTextureData() {
        return this.TEXTURE_DATA;
    }

    @Override
    public NBTTagCompound save(NBTTagCompound tag) {
        tag.setString("text", ITextComponent.Serializer.componentToJson(this.TEXT));
        tag.setInteger("current", this.CURRENT);
        tag.setInteger("max", this.MAX);
        tag.setInteger("color", this.COLOR);
        tag.setString("texture", this.TEXTURE_DATA);
        return tag;
    }

    public static CommonBarElement load(NBTTagCompound tag) {
        ITextComponent text = ITextComponent.Serializer.jsonToComponent(tag.getString("text"));
        int current = tag.getInteger("current");
        int max = tag.getInteger("max");
        int color = tag.getInteger("color");
        String texture = tag.getString("texture");
        return new CommonBarElement(current, max, text, color, texture);
    }

    @Override
    public String getTagId() {
        return JadeTags.JADE_ADDON_BAR_TAG;
    }
}
