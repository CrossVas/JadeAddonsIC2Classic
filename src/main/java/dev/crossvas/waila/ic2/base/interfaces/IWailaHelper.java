package dev.crossvas.waila.ic2.base.interfaces;

import net.minecraft.nbt.NBTTagCompound;

public interface IWailaHelper {

    void add(IWailaElementBuilder element);
    void append(IWailaElementBuilder element);

    void transferData(NBTTagCompound serverData);
}
