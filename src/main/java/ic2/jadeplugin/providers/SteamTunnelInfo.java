package ic2.jadeplugin.providers;

import ic2.core.block.base.tiles.BaseLinkingTileEntity;
import ic2.core.block.generators.tiles.SteamTunnelTileEntity;
import ic2.core.utils.math.ColorUtils;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.helpers.Formatter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class SteamTunnelInfo implements IInfoProvider {

    public static final SteamTunnelInfo THIS = new SteamTunnelInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof SteamTunnelTileEntity steamTunnel) {
            addTunnelInfo(helper, steamTunnel);
            if (!steamTunnel.isValid || steamTunnel.isDynamic()) {
                long time = steamTunnel.clockTime(512);
                helper.bar((int) time, 512, translate("ic2.multiblock.reform.next", 512 - time), ColorUtils.GRAY);
            }
        }
        if (blockEntity instanceof BaseLinkingTileEntity linkingTile) {
            BlockEntity master = linkingTile.getMaster();
            if (master instanceof SteamTunnelTileEntity steamTunnel) {
                addTunnelInfo(helper, steamTunnel);
            }
        }
    }

    public void addTunnelInfo(JadeHelper helper, SteamTunnelTileEntity steamTunnel) {
        helper.tier(steamTunnel.getSourceTier());
        helper.defaultText("ic2.probe.eu.output.current.name", Formatter.formatNumber(steamTunnel.getEUProduction(), 3));
        helper.maxOut(steamTunnel.getMaxEnergyOutput());
        helper.addTankInfo(steamTunnel);
    }
}
