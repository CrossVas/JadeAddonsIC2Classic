package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.BarHelper;
import dev.crossvas.jadexic2c.helpers.Formatter;
import dev.crossvas.jadexic2c.helpers.IHelper;
import ic2.api.tiles.readers.IEUStorage;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.machines.tiles.hv.MassFabricatorTileEntity;
import ic2.core.block.machines.tiles.lv.ElectrolyzerTileEntity;
import ic2.core.block.machines.tiles.mv.ChargedElectrolyzerTileEntity;
import ic2.core.block.storage.tiles.CreativeSourceTileEntity;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum EUStorageInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "EUStorageInfo", SpecialFilters.EU_READER)) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "EUStorageInfo");
        int stored = tag.getInt("storedEnergy");
        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity tile) {
            if (tile instanceof CreativeSourceTileEntity) {
                BarHelper.bar(iTooltip, 1, 1, Component.translatable("ic2.probe.eu.storage.name", "Infinite").withStyle(ChatFormatting.WHITE), ColorUtils.RED);
            } else if (tile instanceof IEUStorage storage && !(tile instanceof ElectrolyzerTileEntity || tile instanceof ChargedElectrolyzerTileEntity || tile instanceof MassFabricatorTileEntity)) {
                BarHelper.bar(iTooltip, stored, storage.getMaxEU(), Component.translatable("ic2.probe.eu.storage.full.name", Formatter.formatNumber(stored, 4), Formatter.formatNumber(storage.getMaxEU(), 4)).withStyle(ChatFormatting.WHITE), ColorUtils.RED);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.TOOLTIP_RENDERER;
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseTileEntity tile) {
            if (tile instanceof IEUStorage storage && !(tile instanceof ElectrolyzerTileEntity || tile instanceof ChargedElectrolyzerTileEntity)) {
                CompoundTag tag = new CompoundTag();
                tag.putInt("storedEnergy", storage.getStoredEU());
                compoundTag.put("EUStorageInfo", tag);
            }
        }
    }
}
