package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import ic2.core.block.generators.tiles.FuelBoilerTileEntity;
import ic2.core.platform.player.PlayerHandler;
import ic2.core.utils.helpers.Formatters;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class FuelBoilerInfo implements IInfoProvider {

    public static final FuelBoilerInfo THIS = new FuelBoilerInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof FuelBoilerTileEntity fuelBoiler) {
            int fuel = fuelBoiler.getFuel();
            int maxFuel = fuelBoiler.getMaxFuel();
            int heat = fuelBoiler.getHeat();
            int maxHeat = fuelBoiler.getMaxHeat();

            helper.bar(fuel, maxFuel, Component.translatable("ic2.probe.fuel.storage.name").append(String.valueOf(fuel)), ColorUtils.DARK_GRAY);
            if (PlayerHandler.getHandler(player).hasThermometer()) {
                helper.bar(heat, maxHeat, Component.translatable("ic2.probe.reactor.heat.name",
                        heat / 30, Formatters.EU_READER_FORMAT.format((double) maxHeat / 30)), ColorUtils.GREEN);
            }
            helper.addTankInfo(fuelBoiler);
            if (!fuelBoiler.isValid) {
                long time = fuelBoiler.clockTime(512);
                helper.bar((int) time, 512, translate("ic2.multiblock.reform.next", 512 - time), ColorUtils.GRAY);
            }
        }
    }
}
