package dev.crossvas.lookingat.ic2c.base.providers;

import dev.crossvas.lookingat.ic2c.base.LookingAtCommonHandler;
import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import dev.crossvas.lookingat.ic2c.helpers.Formatter;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.impls.BaseGeneratorTileEntity;
import ic2.core.block.generators.tiles.*;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.text.DecimalFormat;

public class BaseGeneratorInfo implements IInfoProvider {

    public static final BaseGeneratorInfo THIS = new BaseGeneratorInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseGeneratorTileEntity gen) {
            float euProduction = gen.getEUProduction();
            defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(gen.getSourceTier()));
            defaultText(helper, "ic2.probe.eu.output.current.name", Formatter.formatNumber(euProduction, 3));
            defaultText(helper, "ic2.probe.eu.output.max.name", gen.getMaxEnergyOutput());

            if (gen instanceof SolarTurbineTileEntity solarTurbine) {
                int heat = solarTurbine.getHeat();
                int maxHeat = solarTurbine.getMaxHeat();
                Component heatComp = Component.translatable("info.block.ic2.induction_furnace.heat").append(": " + Formatter.SOLAR_TURBINE.format((float) heat / 240) + "%");
                bar(helper, heat, maxHeat, heatComp, -295680);
            }
            if (gen instanceof ThermalGeneratorTileEntity thermal) {
                float subProduction = thermal.subProduction.getProduction(2000.0F);
                text(helper, Component.translatable("ic2.probe.production.passive.name", Formatter.THERMAL_GEN.format(subProduction)));
            }
            if (gen instanceof SolarTurbineTileEntity || gen instanceof ThermalGeneratorTileEntity || gen instanceof GeoGenTileEntity || gen instanceof LiquidFuelGenTileEntity) {
                LookingAtCommonHandler.addTankInfo(helper, gen);
            }

            int fuel = gen.getFuel();
            int maxFuel = gen.getMaxFuel();
            if ((gen instanceof SlagGenTileEntity || gen instanceof FuelGenTileEntity) && fuel > 0) {
                bar(helper, fuel, maxFuel, Component.translatable("ic2.probe.fuel.storage.name").append(String.valueOf(fuel)), ColorUtils.DARK_GRAY);
            }
        }
    }
}
