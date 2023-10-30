package dev.crossvas.jadexic2c;

import ic2.core.inventory.filter.IFilter;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.utils.helpers.StackUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;

public interface IHelper extends IBlockComponentProvider, IServerDataProvider<BlockEntity> {

    default IFilter getFilter(ResourceLocation id) {
        return switch (id.getPath()) {
            case "eu_reader" -> SpecialFilters.EU_READER;
            case "thermometer" -> SpecialFilters.THERMOMETER;
            case "crop" -> SpecialFilters.CROP_SCANNER;
            default -> throw new IllegalStateException("Unexpected value: " + id +
                    ". Expected: " + JadeIC2CPluginHandler.EU_READER_INFO.getPath() + JadeIC2CPluginHandler.THERMOMETER_INFO.getPath() + JadeIC2CPluginHandler.CROP_INFO.getPath());
        };
    }

    default boolean canHandle(Player player) {
        return StackUtil.hasHotbarItems(player, getFilter(getUid()));
    }

    default boolean canHandle(BlockAccessor accessor) {
        return StackUtil.hasHotbarItems(accessor.getPlayer(), getFilter(getUid()));
    }
    default boolean hasData(BlockAccessor accessor, String tagName) {
        return accessor.getServerData().contains(tagName);
    }

    default boolean shouldAddInfo(BlockAccessor accessor, String tagName) {
        return canHandle(accessor) && hasData(accessor, tagName);
    }


    default CompoundTag getData(BlockAccessor accessor, String tagID) {
        return accessor.getServerData().getCompound(tagID);
    }
}
