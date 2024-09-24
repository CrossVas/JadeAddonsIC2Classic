package dev.crossvas.waila.ic2.providers;

import dev.crossvas.waila.ic2.base.WailaCommonHandler;
import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import dev.crossvas.waila.ic2.utils.Formatter;
import ic2.core.block.generator.tileentity.TileEntityBasicTurbine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class SteamTurbineInfo implements IInfoProvider {

    public static final SteamTurbineInfo THIS = new SteamTurbineInfo();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityBasicTurbine) {
            TileEntityBasicTurbine turbine = (TileEntityBasicTurbine) blockEntity;
            text(helper, tier(turbine.getSourceTier()));
            text(helper, translate("probe.energy.output", Formatter.formatNumber(turbine.getEnergyProduction(), 3)));
            text(helper, translate("probe.energy.output.max", turbine.getEnergyProduction()));
            WailaCommonHandler.addTankInfo(helper, turbine);
        }
    }
}
