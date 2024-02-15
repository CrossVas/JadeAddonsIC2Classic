package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.BarHelper;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import dev.crossvas.jadexic2c.utils.ColorMix;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseMultiElectricTileEntity;
import ic2.core.block.machines.tiles.ev.ElectricFisherTileEntity;
import ic2.core.utils.helpers.Formatters;
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

public enum ElectricFisherInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "ElectricFisherInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "ElectricFisherInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseMultiElectricTileEntity tile) {
            if (tile instanceof ElectricFisherTileEntity fisher) {
                TextHelper.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(fisher.getSinkTier()));
                TextHelper.text(iTooltip, "ic2.probe.eu.max_in.name", fisher.getMaxInput());
                TextHelper.text(iTooltip, "ic2.probe.eu.usage.name", 150);
                int progress = (int) tag.getFloat("progress");
                int max = (int) fisher.getMaxProgress();
                BarHelper.bar(iTooltip, progress, max, Component.translatable("ic2.probe.progress.full.name", Formatters.EU_READER_FORMAT.format(progress), max), ColorMix.BLUE);
            }

            if (!tile.isValid || tile.isDynamic()) {
                long time = tile.clockTime(512);
                BarHelper.bar(iTooltip, (int) time, 512, Component.literal("Next Reform: ").append(String.valueOf(512 - time)).append(" Ticks").withStyle(ChatFormatting.WHITE), ColorUtils.GRAY);
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseMultiElectricTileEntity tile) {
            if (tile instanceof ElectricFisherTileEntity fisher) {
                CompoundTag tag = new CompoundTag();
                tag.putFloat("progress", fisher.getProgress());
                compoundTag.put("ElectricFisherInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}
