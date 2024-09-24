package dev.crossvas.waila.ic2.providers;


import dev.crossvas.waila.ic2.base.WailaCommonHandler;
import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import dev.crossvas.waila.ic2.utils.ColorUtils;
import dev.crossvas.waila.ic2.utils.Formatter;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorChamber;
import ic2.api.reactor.ISteamReactor;
import ic2.core.block.generator.tileentity.TileEntityNuclearReactorElectric;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class NuclearInfo implements IInfoProvider {

    public static final NuclearInfo THIS = new NuclearInfo();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof IReactor) {
            addTooltips(helper, blockEntity, player);
        }
        if (blockEntity instanceof IReactorChamber) {
            IReactorChamber chamber = (IReactorChamber) blockEntity;
            addTooltips(helper, (TileEntity) chamber.getReactor(), player);
        }
    }

    public void addTooltips(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof IReactor) {
            IReactor reactor = (IReactor) blockEntity;
            if (blockEntity instanceof TileEntityNuclearReactorElectric) {
                TileEntityNuclearReactorElectric nuclearReactor = (TileEntityNuclearReactorElectric) blockEntity;
                text(helper, translatable("probe.energy.output", Formatter.formatNumber(nuclearReactor.getReactorEUEnergyOutput(), 3)));
                text(helper, translatable("probe.reactor.breeding", reactor.getHeat() / 3000 + 1));
            } else if (blockEntity instanceof ISteamReactor) {
                ISteamReactor steamReactor = (ISteamReactor) blockEntity;
                text(helper, translatable("probe.steam.output", Formatter.THERMAL_GEN.format(steamReactor.getReactorEUEnergyOutput() * 3.200000047683716)));
                text(helper, translatable("probe.water.consumption", Formatter.THERMAL_GEN.format(steamReactor.getReactorEnergyOutput() / 50.0)));
                WailaCommonHandler.addTankInfo(helper, blockEntity);
            }

            bar(helper, reactor.getHeat(), reactor.getMaxHeat(), translatable("probe.reactor.heat",
                Formatter.formatNumber(reactor.getHeat(), 4), Formatter.formatNumber(reactor.getMaxHeat(), 2)), getReactorColor(reactor.getHeat(), reactor.getMaxHeat()));
        }
    }

    public static int getReactorColor(int current, int max) {
        float progress = (float) current / max;
        if ((double) progress < 0.25) {
            return ColorUtils.GREEN;
        } else if ((double) progress < 0.5) {
            return -1189115;
        } else {
            return (double) progress < 0.75 ? -1203707 : ColorUtils.RED;
        }
    }
}
