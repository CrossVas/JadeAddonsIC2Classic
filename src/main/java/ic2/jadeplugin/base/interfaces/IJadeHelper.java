package ic2.jadeplugin.base.interfaces;

import net.minecraft.nbt.CompoundTag;

public interface IJadeHelper {

    void add(IJadeElementBuilder element);
    void append(IJadeElementBuilder element);

    void transferData(CompoundTag serverData);
}
