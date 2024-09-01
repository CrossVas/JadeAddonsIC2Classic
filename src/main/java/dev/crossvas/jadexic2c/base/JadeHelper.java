package dev.crossvas.jadexic2c.base;

import dev.crossvas.jadexic2c.JadeTags;
import dev.crossvas.jadexic2c.base.interfaces.IJadeElementBuilder;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class JadeHelper implements IJadeHelper {

    public NBTTagList DATA = new NBTTagList();
    public static final String ADD_TAG = "add";
    public static final String APPEND_TAG = "append";

    @Override
    public void add(IJadeElementBuilder element) {
        NBTTagCompound addTag = new NBTTagCompound();
        NBTTagCompound elementTag = element.save(new NBTTagCompound());
        elementTag.setBoolean(ADD_TAG, true);
        addTag.setTag(element.getTagId(), elementTag);
        DATA.appendTag(addTag);
    }

    @Override
    public void append(IJadeElementBuilder element) {
        NBTTagCompound elementTag = element.save(new NBTTagCompound());
        NBTTagCompound appendTag = new NBTTagCompound();
        elementTag.setBoolean(APPEND_TAG, true);
        appendTag.setTag(element.getTagId(), elementTag);
        DATA.appendTag(appendTag);
    }

    @Override
    public void transferData(NBTTagCompound serverData) {
        if (!this.DATA.isEmpty()) {
            serverData.setTag(JadeTags.TAG_DATA, this.DATA);
        }
    }
}
