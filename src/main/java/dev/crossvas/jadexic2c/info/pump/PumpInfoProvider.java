package dev.crossvas.jadexic2c.info.pump;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.ColorMix;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseElectricTileEntity;
import ic2.core.block.machines.tiles.lv.PumpTileEntity;
import ic2.core.utils.helpers.Formatters;
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

public enum PumpInfoProvider implements IHelper {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!canHandle(blockAccessor.getPlayer())) {
            return;
        }

        if (!blockAccessor.getServerData().contains("PumpInfo")) {
            return;
        }

        CompoundTag tag = blockAccessor.getServerData().getCompound("PumpInfo");
        if (blockAccessor.getBlockEntity() instanceof PumpTileEntity pump) {
            Helpers.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(pump.getTier()));
            Helpers.text(iTooltip, "ic2.probe.eu.max_in.name", pump.getMaxInput());
            Helpers.text(iTooltip, "ic2.probe.eu.usage.name", pump.getPumpCost());
            Helpers.text(iTooltip, "ic2.probe.pump.pressure", 100);
            Helpers.text(iTooltip, "ic2.probe.pump.amount", Formatters.EU_FORMAT.format(800L));

            int pumpProgress = tag.getInt("progress");

            if (pumpProgress > 0) {
                Helpers.barLiteral(iTooltip, pumpProgress, pump.getPumpMaxProgress(),
                        Component.translatable("ic2.probe.progress.full.name", pumpProgress, pump.getPumpMaxProgress()).append(" t").withStyle(ChatFormatting.WHITE), ColorMix.BLUE);
            }
            Helpers.addClientTankFromTag(iTooltip, blockAccessor);
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        CompoundTag tag = new CompoundTag();
        if (blockEntity instanceof BaseElectricTileEntity tile) {
            if (tile instanceof PumpTileEntity pump) {
                tag.putInt("progress", pump.getPumpProgress());
                Helpers.loadTankData(pump.tank, compoundTag);
            }
        }
        compoundTag.put("PumpInfo", tag);
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}
