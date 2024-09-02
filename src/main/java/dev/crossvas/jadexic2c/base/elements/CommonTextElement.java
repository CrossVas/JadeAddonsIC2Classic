package dev.crossvas.jadexic2c.base.elements;

import dev.crossvas.jadexic2c.JadeTags;
import dev.crossvas.jadexic2c.base.interfaces.IJadeElementBuilder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;

public class CommonTextElement implements IJadeElementBuilder {

    ITextComponent TEXT;
    boolean CENTERED;

    public CommonTextElement(ITextComponent text, boolean centered) {
        this.TEXT = text;
        this.CENTERED = centered;
    }


    public ITextComponent getText() {
        return TEXT;
    }

    public boolean isCentered() {
        return this.CENTERED;
    }

    @Override
    public NBTTagCompound save(NBTTagCompound tag) {
        tag.setString("text", ITextComponent.Serializer.componentToJson(TEXT));
        tag.setBoolean("centered", this.CENTERED);
        return tag;
    }

    public static CommonTextElement load(NBTTagCompound tag) {
        ITextComponent text = ITextComponent.Serializer.jsonToComponent(tag.getString("text"));
        boolean centered = tag.getBoolean("centered");
        return new CommonTextElement(text, centered);
    }

    @Override
    public String getTagId() {
        return JadeTags.JADE_ADDON_TEXT_TAG;
    }
}
