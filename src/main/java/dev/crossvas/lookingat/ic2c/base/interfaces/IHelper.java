package dev.crossvas.lookingat.ic2c.base.interfaces;

import net.minecraft.nbt.CompoundTag;

public interface IHelper {

    void add(IElementBuilder element);
    void append(IElementBuilder element);

    void transferData(CompoundTag serverData);
}
