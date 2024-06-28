package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.JadeCommonHandler;
import dev.crossvas.jadexic2c.base.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.Formatter;
import ic2.api.energy.EnergyNet;
import ic2.core.block.base.tiles.BaseLinkingTileEntity;
import ic2.core.block.generators.tiles.SteamTunnelTileEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SteamTunnelInfo extends EUReaderInfoProvider {

    public static final SteamTunnelInfo THIS = new SteamTunnelInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof SteamTunnelTileEntity steamTunnel) {
            addTunnelInfo(helper, steamTunnel);
        }
        if (blockEntity instanceof BaseLinkingTileEntity linkingTile) {
            BlockEntity master = linkingTile.getMaster();
            if (master instanceof SteamTunnelTileEntity steamTunnel) {
                addTunnelInfo(helper, steamTunnel);
            }
        }
    }

    public void addTunnelInfo(IJadeHelper helper, SteamTunnelTileEntity steamTunnel) {
        text(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(steamTunnel.getSourceTier()));
        text(helper, "ic2.probe.eu.output.current.name", Formatter.formatNumber(steamTunnel.getEUProduction(), 3));
        text(helper, "ic2.probe.eu.output.max.name", steamTunnel.getMaxEnergyOutput());
        JadeCommonHandler.addTankInfo(helper, steamTunnel);
    }
}
