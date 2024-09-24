package dev.crossvas.waila.ic2.base.interfaces;

import net.minecraft.nbt.NBTTagCompound;

public interface IWailaElementBuilder {

    NBTTagCompound save(NBTTagCompound tag);

    String getTagId();
}
