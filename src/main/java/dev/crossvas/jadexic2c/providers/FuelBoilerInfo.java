package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeCommonHandler;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.utils.ColorUtils;
import dev.crossvas.jadexic2c.utils.Formatter;
import ic2.core.block.generator.tile.TileEntityFuelBoiler;
import ic2.core.util.misc.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class FuelBoilerInfo implements IInfoProvider {

    public static final FuelBoilerInfo THIS = new FuelBoilerInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityFuelBoiler) {
            TileEntityFuelBoiler fuelBoiler = (TileEntityFuelBoiler) blockEntity;
            int fuel = (int) fuelBoiler.getFuel();
            int maxFuel = (int) fuelBoiler.getMaxFuel();
            int heat = fuelBoiler.getHeat();
            int maxHeat = fuelBoiler.getMaxHeat();
            if (fuel > 0) {
                bar(helper, fuel, maxFuel, translatable("probe.storage.fuel").appendText(String.valueOf(fuel)), ColorUtils.DARK_GRAY);
            }
            if (StackUtil.hasHotbarItem(player, THERMOMETER) || player.isCreative()) {
                if (heat > 0) {
                    bar(helper, heat, maxHeat, translatable("probe.machine.heat",
                            heat, Formatter.EU_READER_FORMAT.format((double) maxHeat)), ColorUtils.GREEN);
                }
            }
            JadeCommonHandler.addTankInfo(helper, fuelBoiler);
        }
    }
}
