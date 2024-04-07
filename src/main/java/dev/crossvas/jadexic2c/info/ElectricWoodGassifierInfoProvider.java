package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.BarHelper;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.TankHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseElectricTileEntity;
import ic2.core.block.machines.tiles.lv.WoodGassifierTileEntity;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum ElectricWoodGassifierInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "GassificatorInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "GassificatorInfo");
        if (blockAccessor.getBlockEntity() instanceof WoodGassifierTileEntity gas) {
            TextHelper.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(gas.getTier()));
            TextHelper.text(iTooltip, "ic2.probe.eu.max_in.name", gas.getMaxInput());
            TextHelper.text(iTooltip, "ic2.probe.eu.usage.name", 1);
            TextHelper.text(iTooltip, "ic2.probe.pump.pressure", 25);
            TextHelper.text(iTooltip, "ic2.probe.pump.amount", Formatters.EU_FORMAT.format(1800L));
            float progress = tag.getFloat("progress");
            if (progress > 0) {
                BarHelper.bar(iTooltip, (int) progress, (int) gas.getMaxProgress(), "ic2.probe.progress.full.name", -16733185);
            }
            TankHelper.addClientTankFromTag(iTooltip, blockAccessor);
        }
    }


    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseElectricTileEntity tile) {
            if (tile instanceof WoodGassifierTileEntity gas) {
                CompoundTag tag = new CompoundTag();
                tag.putFloat("progress", gas.getProgress());
                TankHelper.loadTankData(compoundTag, gas);
                compoundTag.put("GassificatorInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}
