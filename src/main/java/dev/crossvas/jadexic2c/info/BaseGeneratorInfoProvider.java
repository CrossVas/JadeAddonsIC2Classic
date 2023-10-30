package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseInventoryTileEntity;
import ic2.core.block.base.tiles.impls.BaseGeneratorTileEntity;
import ic2.core.block.generators.tiles.*;
import ic2.core.utils.math.ColorUtils;
import ic2.probeplugin.base.ProbePluginHelper;
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

public enum BaseGeneratorInfoProvider implements IHelper {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "BaseGeneratorInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "BaseGeneratorInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseInventoryTileEntity tile) {
            if (tile instanceof BaseGeneratorTileEntity gen) {
                float euProduction = tag.getFloat("euProduction");
                Helpers.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(gen.getSourceTier()));
                Helpers.text(iTooltip, "ic2.probe.eu.output.current.name", ProbePluginHelper.formatNumber((double) euProduction, 5));

                Helpers.text(iTooltip, "ic2.probe.eu.output.max.name", gen.getMaxEnergyOutput());
                if (gen instanceof SolarTurbineTileEntity) {
                    int heat = tag.getInt("heat");
                    Helpers.text(iTooltip, Component.translatable("ic2.probe.heat.name", ProbePluginHelper.THERMAL_GEN.format((double) ((float) heat / 240.0F))).withStyle(ChatFormatting.WHITE));
                    Helpers.addClientTankFromTag(iTooltip, blockAccessor);
                }
                if (gen instanceof ThermalGeneratorTileEntity) {
                    float subProduction = tag.getFloat("subProduction");
                    Helpers.text(iTooltip, Component.translatable("ic2.probe.production.passive.name", ProbePluginHelper.THERMAL_GEN.format((double) subProduction)).withStyle(ChatFormatting.WHITE));
                    Helpers.addClientTankFromTag(iTooltip, blockAccessor);
                }
                if (gen instanceof GeoGenTileEntity) {
                    Helpers.addClientTankFromTag(iTooltip, blockAccessor);
                }
                if (gen instanceof LiquidFuelGenTileEntity) {
                    Helpers.addClientTankFromTag(iTooltip, blockAccessor);
                }

                int fuel = tag.getInt("fuel");
                if ((gen instanceof SlagGenTileEntity || gen instanceof FuelGenTileEntity) && fuel > 0) {
                    Helpers.barLiteral(iTooltip, fuel, gen.getMaxFuel(), Component.translatable("ic2.probe.fuel.storage.name").append(String.valueOf(fuel)), ColorUtils.DARK_GRAY);
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        CompoundTag tag = new CompoundTag();
        if (blockEntity instanceof BaseInventoryTileEntity tile) {
            if (tile instanceof BaseGeneratorTileEntity generator) {
                tag.putFloat("euProduction", generator.getEUProduction());
                tag.putInt("fuel", generator.fuel);
                if (generator instanceof SolarTurbineTileEntity solarTurbine) {
                    Helpers.loadTankData(compoundTag, solarTurbine);
                    tag.putInt("heat", solarTurbine.heat);
                } else if (generator instanceof ThermalGeneratorTileEntity thermal) {
                    Helpers.loadTankData(compoundTag, thermal);
                    tag.putFloat("subProduction", thermal.subProduction.getProduction(2000.0F));
                } else if (generator instanceof GeoGenTileEntity geothermal) {
                    Helpers.loadTankData(compoundTag, geothermal);
                } else if (generator instanceof LiquidFuelGenTileEntity liquidGen) {
                    Helpers.loadTankData(compoundTag, liquidGen);
                }
            }
        }
        compoundTag.put("BaseGeneratorInfo", tag);
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}
