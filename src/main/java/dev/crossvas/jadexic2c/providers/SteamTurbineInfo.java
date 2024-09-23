package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeCommonHandler;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.utils.Formatter;
import ic2.core.block.generator.tile.TileEntityBasicSteamTurbine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class SteamTurbineInfo implements IInfoProvider {

    public static final SteamTurbineInfo THIS = new SteamTurbineInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityBasicSteamTurbine) {
            TileEntityBasicSteamTurbine turbine = (TileEntityBasicSteamTurbine) blockEntity;
            text(helper, tier(turbine.getSourceTier()));
            text(helper, translatable("probe.energy.output", Formatter.formatNumber(turbine.getOutput(), 3)));
            text(helper, translatable("probe.energy.output.max", turbine.getMaxSendingEnergy()));
            JadeCommonHandler.addTankInfo(helper, turbine);
        }
    }
}
