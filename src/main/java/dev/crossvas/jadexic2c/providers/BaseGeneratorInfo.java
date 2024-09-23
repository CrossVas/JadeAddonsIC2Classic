package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeCommonHandler;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.utils.ColorUtils;
import dev.crossvas.jadexic2c.utils.Formatter;
import ic2.core.block.base.tile.TileEntityFuelGeneratorBase;
import ic2.core.block.base.tile.TileEntityGeneratorBase;
import ic2.core.block.generator.tile.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class BaseGeneratorInfo implements IInfoProvider {

    public static final BaseGeneratorInfo THIS = new BaseGeneratorInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityGeneratorBase) {
            TileEntityGeneratorBase generator = (TileEntityGeneratorBase) blockEntity;
            double euProduction = generator.getOfferedEnergy();
            float maxOutput = generator.getMaxSendingEnergy();
            if (generator instanceof TileEntityGeoGenerator || generator instanceof TileEntityGenerator || generator instanceof TileEntityLiquidFuelGenerator || generator instanceof TileEntityWaveGenerator) {
                maxOutput -= 1;
            }
            text(helper, translatable("probe.energy.tier", getDisplayTier(generator.tier)));
            text(helper, translatable("probe.energy.output", Formatter.formatNumber(euProduction, 4)));
            text(helper, translatable("probe.energy.output.max", Formatter.formatNumber(maxOutput, 3)));

            if (generator instanceof TileEntitySolarTurbine) {
                TileEntitySolarTurbine solarTurbine = (TileEntitySolarTurbine) generator;
                int heat = solarTurbine.heat;
                int maxHeat = 24000;
                bar(helper, heat, maxHeat, translatable("probe.machine.heat", Formatter.THERMAL_GEN.format((float) heat / 240.0F)), ColorUtils.ORANGE);
            }
            if (generator instanceof TileEntityThermalGenerator) {
                TileEntityThermalGenerator thermal = (TileEntityThermalGenerator) generator;
                float subProduction = thermal.heatPoints / (2000.0F * thermal.passiveConfig);
                text(helper, translatable("probe.energy.output.passive", Formatter.THERMAL_GEN.format(subProduction)));
            }
            if (generator instanceof TileEntitySolarTurbine || generator instanceof TileEntityThermalGenerator || generator instanceof TileEntityGeoGenerator || generator instanceof TileEntityLiquidFuelGenerator) {
                JadeCommonHandler.addTankInfo(helper, generator);
            }

            if (generator instanceof TileEntityGenerator || generator instanceof TileEntitySlagGenerator) {
                TileEntityFuelGeneratorBase fuelGenerator = (TileEntityFuelGeneratorBase) generator;
                int fuel = (int) fuelGenerator.getFuel();
                int maxFuel = (int) fuelGenerator.getMaxFuel();
                if (fuel > 0) {
                    bar(helper, fuel, maxFuel, translatable("probe.storage.fuel", fuel), ColorUtils.DARK_GRAY);
                }
            }
        }
    }
}
