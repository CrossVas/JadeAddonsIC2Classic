package dev.crossvas.jadexic2c;

import dev.crossvas.jadexic2c.base.interfaces.IJadeElementBuilder;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class JadeHelper implements IJadeHelper {

    private final ListTag DATA = new ListTag();

    public static final String ADD_TAG = "add";
    public static final String APPEND_TAG = "append";

    @Override
    public void add(IJadeElementBuilder element) {
        CompoundTag addTag = new CompoundTag();
        CompoundTag elementTag = element.save(new CompoundTag());
        elementTag.putBoolean(ADD_TAG, true);
        addTag.put(element.getTagId(), elementTag);
        DATA.add(addTag);
    }

    @Override
    public void append(IJadeElementBuilder element) {
        CompoundTag elementTag = element.save(new CompoundTag());
        CompoundTag appendTag = new CompoundTag();
        elementTag.putBoolean(APPEND_TAG, true);
        appendTag.put(element.getTagId(), elementTag);
        DATA.add(appendTag);
    }

    @Override
    public void transferData(CompoundTag serverData) {
        if (!this.DATA.isEmpty()) {
            serverData.put(JadeTags.TAG_DATA, this.DATA);
        }
    }
}
