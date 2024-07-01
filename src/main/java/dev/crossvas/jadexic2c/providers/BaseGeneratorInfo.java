package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.JadeCommonHandler;
import dev.crossvas.jadexic2c.base.IInfoProvider;
import dev.crossvas.jadexic2c.base.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.Formatter;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.impls.BaseGeneratorTileEntity;
import ic2.core.block.generators.tiles.*;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BaseGeneratorInfo implements IInfoProvider {

    public static final BaseGeneratorInfo THIS = new BaseGeneratorInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseGeneratorTileEntity gen) {
            float euProduction = gen.getEUProduction();
            text(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(gen.getSourceTier()));
            text(helper, "ic2.probe.eu.output.current.name", Formatter.formatNumber(euProduction, 3));
            text(helper, "ic2.probe.eu.output.max.name", gen.getMaxEnergyOutput());

            if (gen instanceof SolarTurbineTileEntity solarTurbine) {
                int heat = solarTurbine.getHeat();
                int maxHeat = solarTurbine.getMaxHeat();
                helper.addBarElement(heat, maxHeat, Component.translatable("ic2.probe.heat.name", Formatter.THERMAL_GEN.format((double) ((float) heat / 240.0F))), -295680);
            }
            if (gen instanceof ThermalGeneratorTileEntity thermal) {
                float subProduction = thermal.subProduction.getProduction(2000.0F);
                text(helper, Component.translatable("ic2.probe.production.passive.name", Formatter.THERMAL_GEN.format(subProduction)));
            }
            if (gen instanceof SolarTurbineTileEntity || gen instanceof ThermalGeneratorTileEntity || gen instanceof GeoGenTileEntity || gen instanceof LiquidFuelGenTileEntity) {
                JadeCommonHandler.addTankInfo(helper, gen);
            }

            int fuel = gen.getFuel();
            int maxFuel = gen.getMaxFuel();
            if ((gen instanceof SlagGenTileEntity || gen instanceof FuelGenTileEntity) && fuel > 0) {
                helper.addBarElement(fuel, maxFuel, Component.translatable("ic2.probe.fuel.storage.name").append(String.valueOf(fuel)), ColorUtils.DARK_GRAY);
            }
        }
    }
}
