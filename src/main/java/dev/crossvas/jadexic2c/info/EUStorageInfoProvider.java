package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.ColorMix;
import dev.crossvas.jadexic2c.utils.Formatter;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.tiles.readers.IEUStorage;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.storage.tiles.CreativeSourceTileEntity;
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

public enum EUStorageInfoProvider implements IHelper {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "EUStorageInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "EUStorageInfo");
        int stored = tag.getInt("storedEnergy");
        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity tile) {
            if (tile instanceof CreativeSourceTileEntity) {
                Helpers.barLiteral(iTooltip, 1, 1, Component.translatable("ic2.probe.eu.storage.name", "Infinite").withStyle(ChatFormatting.WHITE), ColorMix.RED);
            } else if (tile instanceof IEUStorage storage) {
                Helpers.barLiteral(iTooltip, stored, storage.getMaxEU(), Component.translatable("ic2.probe.eu.storage.name", Formatter.formatNumber(stored, 5)).withStyle(ChatFormatting.WHITE), ColorMix.RED);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_STORAGE_INFO;
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        CompoundTag tag = new CompoundTag();
        if (blockEntity instanceof BaseTileEntity tile) {
            if (tile instanceof IEUStorage storage) {
                tag.putInt("storedEnergy", storage.getStoredEU());
            }
        }
        compoundTag.put("EUStorageInfo", tag);
    }
}
