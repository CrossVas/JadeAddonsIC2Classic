package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.ColorMix;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseElectricTileEntity;
import ic2.core.block.machines.tiles.hv.OreScannerTileEntity;
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

public enum OreScannerInfoProvider implements IHelper {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "OreScannerInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "OreScannerInfo");
        if (blockAccessor.getBlockEntity() instanceof OreScannerTileEntity tile) {
            Helpers.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(tile.getTier()));
            Helpers.text(iTooltip, "ic2.probe.eu.max_in.name", tile.getMaxInput());
            Helpers.text(iTooltip, "ic2.probe.eu.usage.name", 1000);

            int blocks = tag.getInt("blocks");
            int maxBlocks = tag.getInt("maxBlocks");

            if (blocks > 0) {
                Helpers.barLiteral(iTooltip, blocks, maxBlocks, Component.translatable("ic2.probe.progress.full.name", blocks / 25 / 20, maxBlocks / 25 / 20).append("s").withStyle(ChatFormatting.WHITE), ColorMix.BLUE);
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        CompoundTag tag = new CompoundTag();
        if (blockEntity instanceof BaseElectricTileEntity tile) {
            if (tile instanceof OreScannerTileEntity scanner) {
                tag.putInt("blocks", scanner.blocks);
                tag.putInt("maxBlocks", scanner.maxBlocks);
            }
        }
        compoundTag.put("OreScannerInfo", tag);
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}
