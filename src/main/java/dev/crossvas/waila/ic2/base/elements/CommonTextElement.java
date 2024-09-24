package dev.crossvas.waila.ic2.base.elements;

import dev.crossvas.waila.ic2.WailaTags;
import dev.crossvas.waila.ic2.base.interfaces.IWailaElementBuilder;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;

public class CommonTextElement implements IWailaElementBuilder {

    IChatComponent TEXT;
    boolean CENTERED;

    public CommonTextElement(IChatComponent text, boolean centered) {
        this.TEXT = text;
        this.CENTERED = centered;
    }


    public IChatComponent getText() {
        return TEXT;
    }

    public boolean isCentered() {
        return this.CENTERED;
    }

    @Override
    public NBTTagCompound save(NBTTagCompound tag) {
        tag.setString("text", IChatComponent.Serializer.func_150696_a(this.TEXT));
        tag.setBoolean("centered", this.CENTERED);
        return tag;
    }

    public static CommonTextElement load(NBTTagCompound tag) {
        IChatComponent text = IChatComponent.Serializer.func_150699_a(tag.getString("text"));
        boolean centered = tag.getBoolean("centered");
        return new CommonTextElement(text, centered);
    }

    @Override
    public String getTagId() {
        return WailaTags.JADE_ADDON_TEXT_TAG;
    }
}
