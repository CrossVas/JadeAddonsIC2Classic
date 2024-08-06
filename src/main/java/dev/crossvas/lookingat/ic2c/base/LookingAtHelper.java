package dev.crossvas.lookingat.ic2c.base;

import dev.crossvas.lookingat.ic2c.LookingAtTags;
import dev.crossvas.lookingat.ic2c.base.interfaces.IElementBuilder;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class LookingAtHelper implements IHelper {

    private final ListTag DATA = new ListTag();

    public static final String ADD_TAG = "add";
    public static final String APPEND_TAG = "append";

    @Override
    public void add(IElementBuilder element) {
        CompoundTag addTag = new CompoundTag();
        CompoundTag elementTag = element.save(new CompoundTag());
        elementTag.putBoolean(ADD_TAG, true);
        addTag.put(element.getTagId(), elementTag);
        DATA.add(addTag);
    }

    @Override
    public void append(IElementBuilder element) {
        CompoundTag elementTag = element.save(new CompoundTag());
        CompoundTag appendTag = new CompoundTag();
        elementTag.putBoolean(APPEND_TAG, true);
        appendTag.put(element.getTagId(), elementTag);
        DATA.add(appendTag);
    }

    @Override
    public void transferData(CompoundTag serverData) {
        if (!this.DATA.isEmpty()) {
            serverData.put(LookingAtTags.TAG_DATA, this.DATA);
        }
    }
}
