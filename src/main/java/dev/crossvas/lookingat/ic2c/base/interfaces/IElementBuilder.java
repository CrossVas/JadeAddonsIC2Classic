package dev.crossvas.lookingat.ic2c.base.interfaces;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec2;

public interface IElementBuilder {

    /**
     * @param side left, right
     *
     * */
    IElementBuilder align(String side);

    IElementBuilder translate(Vec2 translation);

    CompoundTag save(CompoundTag tag);

    String getTagId();
}
