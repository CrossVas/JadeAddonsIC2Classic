package dev.crossvas.waila.ic2.base;

import dev.crossvas.waila.ic2.WailaTags;
import dev.crossvas.waila.ic2.base.interfaces.IWailaElementBuilder;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class WailaHelper implements IWailaHelper {

    public NBTTagList DATA = new NBTTagList();
    public static final String ADD_TAG = "add";
    public static final String APPEND_TAG = "append";

    @Override
    public void add(IWailaElementBuilder element) {
        NBTTagCompound addTag = new NBTTagCompound();
        NBTTagCompound elementTag = element.save(new NBTTagCompound());
        elementTag.setBoolean(ADD_TAG, true);
        addTag.setTag(element.getTagId(), elementTag);
        DATA.appendTag(addTag);
    }

    @Override
    public void append(IWailaElementBuilder element) {
        NBTTagCompound elementTag = element.save(new NBTTagCompound());
        NBTTagCompound appendTag = new NBTTagCompound();
        elementTag.setBoolean(APPEND_TAG, true);
        appendTag.setTag(element.getTagId(), elementTag);
        DATA.appendTag(appendTag);
    }

    @Override
    public void transferData(NBTTagCompound serverData) {
        if (this.DATA.tagCount() != 0) {
            serverData.setTag(WailaTags.TAG_DATA, this.DATA);
        }
    }
}
