package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.BarHelper;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseElectricTileEntity;
import ic2.core.block.machines.tiles.hv.OreScannerTileEntity;
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

public enum OreScannerInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "OreScannerInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "OreScannerInfo");
        if (blockAccessor.getBlockEntity() instanceof OreScannerTileEntity tile) {
            TextHelper.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(tile.getTier()));
            TextHelper.text(iTooltip, "ic2.probe.eu.max_in.name", tile.getMaxInput());
            TextHelper.text(iTooltip, "ic2.probe.eu.usage.name", 1000);

            int blocks = tag.getInt("blocks");
            int maxBlocks = tag.getInt("maxBlocks");

            if (blocks > 0) {
                BarHelper.bar(iTooltip, blocks, maxBlocks, Component.translatable("ic2.probe.progress.full.name", blocks / 25 / 20, maxBlocks / 25 / 20).append("s").withStyle(ChatFormatting.WHITE), ColorUtils.BLUE);
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseElectricTileEntity tile) {
            if (tile instanceof OreScannerTileEntity scanner) {
                CompoundTag tag = new CompoundTag();
                tag.putInt("blocks", scanner.blocks);
                tag.putInt("maxBlocks", scanner.maxBlocks);
                compoundTag.put("OreScannerInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}
