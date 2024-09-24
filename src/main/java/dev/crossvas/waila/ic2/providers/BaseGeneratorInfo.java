package dev.crossvas.waila.ic2.providers;

import dev.crossvas.waila.ic2.base.WailaCommonHandler;
import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import dev.crossvas.waila.ic2.utils.ColorUtils;
import dev.crossvas.waila.ic2.utils.Formatter;
import ic2.core.block.generator.tileentity.TileEntityBaseGenerator;
import ic2.core.block.generator.tileentity.TileEntityGenerator;
import ic2.core.block.generator.tileentity.TileEntityGeoGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class BaseGeneratorInfo implements IInfoProvider {

    public static final BaseGeneratorInfo THIS = new BaseGeneratorInfo();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityBaseGenerator) {
            TileEntityBaseGenerator generator = (TileEntityBaseGenerator) blockEntity;
            double euProduction = generator.getOfferedEnergy();
            double maxOutput = generator.getOfferedEnergy();
            if (generator instanceof TileEntityGeoGenerator || generator instanceof TileEntityGenerator) {
                maxOutput -= 1;
            }
            text(helper, tier(generator.getSourceTier()));
            text(helper, translatable("probe.energy.output", Formatter.formatNumber(euProduction, 4)));
            text(helper, translatable("probe.energy.output.max", Formatter.formatNumber(maxOutput, 3)));

            if (generator instanceof TileEntityGeoGenerator) {
                WailaCommonHandler.addTankInfo(helper, generator);
            }

            int fuel = generator.fuel;
            int maxFuel = generator.storage;
            if (fuel > 0) {
                bar(helper, fuel, maxFuel, translatable("probe.storage.fuel", fuel), ColorUtils.DARK_GRAY);
            }
        }
    }
}
