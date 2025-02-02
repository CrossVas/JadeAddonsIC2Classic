package ic2.jadeplugin.base.interfaces;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec2;

public interface IJadeElementBuilder {

    /**
     * @param side left, right
     *
     * */
    IJadeElementBuilder align(String side);

    IJadeElementBuilder translate(Vec2 translation);

    CompoundTag save(CompoundTag tag);

    String getTagId();
}
