package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeCommonHandler;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.utils.ColorUtils;
import dev.crossvas.jadexic2c.utils.Formatter;
import ic2.api.classic.reactor.ISteamReactor;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorChamber;
import ic2.core.block.generator.tile.TileEntityNuclearReactorElectric;
import ic2.core.util.misc.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class NuclearInfo implements IInfoProvider {

    public static final NuclearInfo THIS = new NuclearInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof IReactor) {
            addTooltips(helper, blockEntity, player);
        }
        if (blockEntity instanceof IReactorChamber) {
            IReactorChamber chamber = (IReactorChamber) blockEntity;
            addTooltips(helper, (TileEntity) chamber.getReactorInstance(), player);
        }
    }

    public void addTooltips(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
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
                JadeCommonHandler.addTankInfo(helper, blockEntity);
            }

            if (StackUtil.hasHotbarItem(player, THERMOMETER) || player.isCreative()) {
                bar(helper, reactor.getHeat(), reactor.getMaxHeat(), translatable("probe.reactor.heat",
                        Formatter.formatNumber(reactor.getHeat(), 4), Formatter.formatNumber(reactor.getMaxHeat(), 2)), getReactorColor(reactor.getHeat(), reactor.getMaxHeat()));
            }
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
