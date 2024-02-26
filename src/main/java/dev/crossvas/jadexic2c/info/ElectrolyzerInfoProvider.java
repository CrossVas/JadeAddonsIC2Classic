package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.BarHelper;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import dev.crossvas.jadexic2c.utils.ColorMix;
import ic2.core.block.base.tiles.BaseInventoryTileEntity;
import ic2.core.block.machines.tiles.lv.ElectrolyzerTileEntity;
import ic2.core.block.machines.tiles.mv.ChargedElectrolyzerTileEntity;
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

public enum ElectrolyzerInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "ElectrolyzerInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "ElectrolyzerInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseInventoryTileEntity tile) {
            if (tile instanceof ElectrolyzerTileEntity ele) {
                boolean discharging = tag.getBoolean("canPower");
                boolean charging = tag.getBoolean("shouldDrain");
                int transfer = tag.getInt("transferRate");
                int energy = tag.getInt("energy");
                int maxEnergy = tag.getInt("maxEnergy");
                TextHelper.text(iTooltip, "ic2.probe.electrolyzer.transferrate.name", transfer);
                TextHelper.text(iTooltip, "ic2.probe.electrolyzer." + (discharging ? (charging ? "transfer" : "discharging") : (charging ? "charging" : "nothing")) + ".name");

                if (energy > 0) {
                    BarHelper.bar(iTooltip, energy, maxEnergy,
                            Component.translatable("ic2.probe.progress.full.name", energy, maxEnergy).append(" EU").withStyle(ChatFormatting.WHITE), ColorMix.RED);
                }
            }

            if (tile instanceof ChargedElectrolyzerTileEntity) {
                boolean discharging = tag.getBoolean("canPowerC");
                boolean charging = tag.getBoolean("shouldDrainC");
                int transfer = tag.getInt("transferRateC");
                int energy = tag.getInt("energyC");
                int maxEnergy = tag.getInt("maxEnergyC");
                TextHelper.text(iTooltip, "ic2.probe.electrolyzer.transferrate.name", transfer);
                TextHelper.text(iTooltip, "ic2.probe.electrolyzer." + (discharging ? (charging ? "transfer" : "discharging") : (charging ? "charging" : "nothing")) + ".name");

                if (energy > 0) {
                    BarHelper.bar(iTooltip, energy, maxEnergy,
                            Component.translatable("ic2.probe.progress.full.name", energy, maxEnergy).append(" EU").withStyle(ChatFormatting.WHITE), ColorMix.RED);
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseInventoryTileEntity tile) {
            CompoundTag tag = new CompoundTag();
            if (tile instanceof ElectrolyzerTileEntity ele) {
                tag.putInt("energy", ele.energy);
                tag.putInt("maxEnergy", ele.maxEnergy);
                tag.putInt("transferRate", ele.getTransferrate());
                tag.putBoolean("canPower", ele.canPower());
                tag.putBoolean("shouldDrain", ele.shouldDrain());
            } else if (tile instanceof ChargedElectrolyzerTileEntity ele) {
                tag.putInt("energyC", ele.energy);
                tag.putInt("maxEnergyC", ele.maxEnergy);
                tag.putInt("transferRateC", ele.getTransferrate());
                tag.putBoolean("canPowerC", ele.canPower());
                tag.putBoolean("shouldDrainC", ele.shouldDrain());
            }
            compoundTag.put("ElectrolyzerInfo", tag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}
