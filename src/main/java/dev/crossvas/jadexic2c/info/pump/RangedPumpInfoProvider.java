package dev.crossvas.jadexic2c.info.pump;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.ColorMix;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseElectricTileEntity;
import ic2.core.block.machines.tiles.mv.RangedPumpTileEntity;
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

public enum RangedPumpInfoProvider implements IHelper {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "RangedPumpInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "RangedPumpInfo");
        if (blockAccessor.getBlockEntity() instanceof RangedPumpTileEntity pump) {
            Helpers.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(pump.getTier()));
            Helpers.text(iTooltip, "ic2.probe.eu.max_in.name", pump.getMaxInput());
            Helpers.text(iTooltip, "ic2.probe.eu.usage.name", 10);

            boolean isOperating = tag.getBoolean("isOperating");
            Helpers.text(iTooltip, isOperating ? "ic2.probe.miner.mining.name" : "ic2.probe.miner.retracting.name");
            int y = pump.getPipeTip().getY();
            Helpers.barLiteral(iTooltip, y, pump.getPosition().getY(), Component.translatable("ic2.probe.pump.progress.name", y).withStyle(ChatFormatting.WHITE), ColorMix.BROWN);
            Helpers.addClientTankFromTag(iTooltip, blockAccessor);
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        CompoundTag tag = new CompoundTag();
        if (blockEntity instanceof BaseElectricTileEntity tile) {
            if (tile instanceof RangedPumpTileEntity pump) {
                tag.putBoolean("isOperating", pump.isOperating());
                Helpers.loadTankData(compoundTag, pump);
            }
        }
        compoundTag.put("RangedPumpInfo", tag);
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}
