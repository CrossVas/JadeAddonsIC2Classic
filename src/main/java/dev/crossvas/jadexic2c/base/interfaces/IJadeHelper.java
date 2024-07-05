package dev.crossvas.jadexic2c.base.interfaces;

import net.minecraft.nbt.CompoundTag;

public interface IJadeHelper {

    void add(IJadeElementBuilder element);
    void append(IJadeElementBuilder element);

    void transferData(CompoundTag serverData);
}
