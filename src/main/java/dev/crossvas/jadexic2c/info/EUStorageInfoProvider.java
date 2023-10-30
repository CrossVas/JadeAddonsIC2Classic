package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.ColorMix;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.tiles.readers.IEUStorage;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.utils.helpers.StackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum EUStorageInfoProvider implements IBlockComponentProvider, IServerDataProvider<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if(!StackUtil.hasHotbarItems(blockAccessor.getPlayer(), SpecialFilters.EU_READER)) {
            return;
        }

        if (!blockAccessor.getServerData().contains("EnergyStorageInfo")) {
            return;
        }

        CompoundTag tag = blockAccessor.getServerData().getCompound("EnergyStorageInfo");

        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity tile) {
            if (tile instanceof IEUStorage storage) {
                if (storage.getMaxEU() > 1000000) {
                    Helpers.barLiteral(iTooltip, tag.getInt("energy"), storage.getMaxEU(), Component.translatable("ic2.probe.eu.storage.name", tag.getInt("energy")).withStyle(ChatFormatting.WHITE), ColorMix.RED);
                } else {
                    Helpers.bar(iTooltip, tag.getInt("energy"), storage.getMaxEU(), "ic2.probe.eu.storage.full.name", ColorMix.RED);
                }

            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_STORAGE_INFO;
    }

    public int formatEnergy(int max) {
        return 0;
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseTileEntity tile) {
            if (tile instanceof IEUStorage storage) {
                CompoundTag tag = new CompoundTag();
                tag.putInt("energy", storage.getStoredEU());
                compoundTag.put("EnergyStorageInfo", tag);
            }
        }
    }
}
