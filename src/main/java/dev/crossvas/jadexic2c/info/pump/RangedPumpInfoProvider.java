package dev.crossvas.jadexic2c.info.pump;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.BarHelper;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.TankHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
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

public enum RangedPumpInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "RangedPumpInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "RangedPumpInfo");
        if (blockAccessor.getBlockEntity() instanceof RangedPumpTileEntity pump) {
            TextHelper.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(pump.getTier()));
            TextHelper.text(iTooltip, "ic2.probe.eu.max_in.name", pump.getMaxInput());
            TextHelper.text(iTooltip, "ic2.probe.eu.usage.name", 10);

            boolean isOperating = tag.getBoolean("isOperating");
            TextHelper.text(iTooltip, isOperating ? "ic2.probe.miner.mining.name" : "ic2.probe.miner.retracting.name");
            int y = pump.getPipeTip().getY();
            BarHelper.bar(iTooltip, y, pump.getPosition().getY(), Component.translatable("ic2.probe.pump.progress.name", y).withStyle(ChatFormatting.WHITE), -10996205);
            TankHelper.addClientTankFromTag(iTooltip, blockAccessor);
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseElectricTileEntity tile) {
            if (tile instanceof RangedPumpTileEntity pump) {
                CompoundTag tag = new CompoundTag();
                tag.putBoolean("isOperating", pump.isOperating());
                TankHelper.loadTankData(compoundTag, pump);
                compoundTag.put("RangedPumpInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}
