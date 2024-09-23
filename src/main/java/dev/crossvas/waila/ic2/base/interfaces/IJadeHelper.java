package dev.crossvas.waila.ic2.base.interfaces;

import net.minecraft.nbt.CompoundTag;

public interface IJadeHelper {

    void add(IJadeElementBuilder element);
    void append(IJadeElementBuilder element);

    void transferData(CompoundTag serverData);
}
