package dev.crossvas.jadexic2c.helpers;

import ic2.core.inventory.filter.IFilter;
import ic2.core.utils.helpers.StackUtil;
import net.minecraft.nbt.CompoundTag;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;

public interface IHelper<T> extends IBlockComponentProvider, IServerDataProvider<T> {

    default boolean hasData(BlockAccessor accessor, String tagName) {
        return accessor.getServerData().contains(tagName);
    }

    default CompoundTag getData(BlockAccessor accessor, String tagID) {
        return accessor.getServerData().getCompound(tagID);
    }

    default boolean canHandle(BlockAccessor accessor, IFilter filter) {
        return StackUtil.hasHotbarItems(accessor.getPlayer(), filter);
    }

    default boolean shouldAddInfo(BlockAccessor accessor, String tagName, IFilter filter) {
        return canHandle(accessor, filter) && hasData(accessor, tagName);
    }
}
