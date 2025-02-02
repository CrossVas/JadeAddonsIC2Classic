package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.helpers.Formatter;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.impls.BaseGeneratorTileEntity;
import ic2.core.block.generators.tiles.*;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class BaseGeneratorInfo implements IInfoProvider {

    public static final BaseGeneratorInfo THIS = new BaseGeneratorInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseGeneratorTileEntity gen) {
            float euProduction = gen.getEUProduction();
            helper.tier(gen.getSourceTier());
            helper.defaultText("ic2.probe.eu.output.current.name", Formatter.formatNumber(euProduction, 3));
            helper.maxOut(gen.getMaxEnergyOutput());

            if (gen instanceof SolarTurbineTileEntity solarTurbine) {
                int heat = solarTurbine.getHeat();
                int maxHeat = solarTurbine.getMaxHeat();
                helper.bar(heat, maxHeat, translate("ic2.probe.heat.name", Formatter.THERMAL_GEN.format((float) heat / 240.0F)), -295680);
            }
            if (gen instanceof ThermalGeneratorTileEntity thermal) {
                float subProduction = thermal.subProduction.getProduction(2000.0F);
                helper.text(translate("ic2.probe.production.passive.name", Formatter.THERMAL_GEN.format(subProduction)));
            }
            if (gen instanceof SolarTurbineTileEntity || gen instanceof ThermalGeneratorTileEntity || gen instanceof GeoGenTileEntity || gen instanceof LiquidFuelGenTileEntity) {
                helper.addTankInfo(gen);
            }

            int fuel = gen.getFuel();
            int maxFuel = gen.getMaxFuel();
            if ((gen instanceof SlagGenTileEntity || gen instanceof FuelGenTileEntity) && fuel > 0) {
                helper.bar(fuel, maxFuel, translate("ic2.probe.fuel.storage.name").append(String.valueOf(fuel)), ColorUtils.DARK_GRAY);
            }
        }
    }
}
