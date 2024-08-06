package dev.crossvas.lookingat.ic2c.base.providers;

import dev.crossvas.lookingat.ic2c.base.LookingAtCommonHandler;
import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import dev.crossvas.lookingat.ic2c.helpers.Formatter;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseLinkingTileEntity;
import ic2.core.block.generators.tiles.SteamTunnelTileEntity;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SteamTunnelInfo implements IInfoProvider {

    public static final SteamTunnelInfo THIS = new SteamTunnelInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof SteamTunnelTileEntity steamTunnel) {
            addTunnelInfo(helper, steamTunnel);
            if (!steamTunnel.isValid || steamTunnel.isDynamic()) {
                long time = steamTunnel.clockTime(512);
                bar(helper, (int) time, 512, Component.translatable("ic2.multiblock.reform.next", 512 - time), ColorUtils.GRAY);
            }
        }
        if (blockEntity instanceof BaseLinkingTileEntity linkingTile) {
            BlockEntity master = linkingTile.getMaster();
            if (master instanceof SteamTunnelTileEntity steamTunnel) {
                addTunnelInfo(helper, steamTunnel);
            }
        }
    }

    public void addTunnelInfo(IHelper helper, SteamTunnelTileEntity steamTunnel) {
        defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(steamTunnel.getSourceTier()));
        defaultText(helper, "ic2.probe.eu.output.current.name", Formatter.formatNumber(steamTunnel.getEUProduction(), 3));
        defaultText(helper, "ic2.probe.eu.output.max.name", steamTunnel.getMaxEnergyOutput());
        LookingAtCommonHandler.addTankInfo(helper, steamTunnel);
    }
}
