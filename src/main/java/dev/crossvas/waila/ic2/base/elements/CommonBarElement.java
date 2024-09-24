package dev.crossvas.waila.ic2.base.elements;

import dev.crossvas.waila.ic2.WailaTags;
import dev.crossvas.waila.ic2.base.interfaces.IWailaElementBuilder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;

public class CommonBarElement implements IWailaElementBuilder {

    int CURRENT;
    int MAX;
    int COLOR;
    IChatComponent TEXT;
    String TEXTURE_DATA;

    public CommonBarElement(int current, int max, IChatComponent text, int color, String textureData) {
        this.CURRENT = current;
        this.MAX = max;
        this.TEXT = text;
        this.COLOR = color;
        this.TEXTURE_DATA = textureData;
    }

    public IChatComponent getText() {
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
        tag.setString("text", IChatComponent.Serializer.func_150696_a(this.TEXT));
        tag.setInteger("current", this.CURRENT);
        tag.setInteger("max", this.MAX);
        tag.setInteger("color", this.COLOR);
        tag.setString("texture", this.TEXTURE_DATA);
        return tag;
    }

    public static CommonBarElement load(NBTTagCompound tag) {
        IChatComponent text = IChatComponent.Serializer.func_150699_a(tag.getString("text"));
        int current = tag.getInteger("current");
        int max = tag.getInteger("max");
        int color = tag.getInteger("color");
        String texture = tag.getString("texture");
        return new CommonBarElement(current, max, text, color, texture);
    }

    @Override
    public String getTagId() {
        return WailaTags.JADE_ADDON_BAR_TAG;
    }
}
