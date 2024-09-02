package dev.crossvas.jadexic2c.base.interfaces;

import net.minecraft.nbt.NBTTagCompound;

public interface IJadeElementBuilder {

    NBTTagCompound save(NBTTagCompound tag);

    String getTagId();
}
