package dev.crossvas.jadexic2c.base.interfaces;

import ic2.core.inventory.filters.CommonFilters;
import ic2.core.inventory.filters.IFilter;
import ic2.core.util.misc.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public interface IInfoProvider {

    IFilter READER = CommonFilters.euReaderActive;
    IFilter THERMOMETER = CommonFilters.thermometerActive;
    IFilter CROP_ANALYZER = CommonFilters.cropAnalyzerActive;
    IFilter ALWAYS = CommonFilters.Anything;

    default IFilter getFilter() {
        return READER;
    }

    default boolean canHandle(EntityPlayer player) {
        return StackUtil.hasHotbarItem(player, getFilter()) || player.isCreative();
    }

    void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player);
}
