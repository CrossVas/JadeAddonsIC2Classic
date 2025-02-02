package ic2.jadeplugin.providers;

import ic2.core.block.base.tiles.BaseLinkingTileEntity;
import ic2.core.block.machines.tiles.luv.FusionReactorTileEntity;
import ic2.core.platform.player.PlayerHandler;
import ic2.core.utils.helpers.Formatters;
import ic2.core.utils.math.ColorUtils;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ThermonuclearReactorInfo implements IInfoProvider {

    public static final ThermonuclearReactorInfo THIS = new ThermonuclearReactorInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof FusionReactorTileEntity reactor) {
            addReactorInfo(helper, reactor, player);
            if (!reactor.isValid || reactor.isDynamic()) {
                long time = reactor.clockTime(512);
                helper.bar((int) time, 512, translate("ic2.multiblock.reform.next", 512 - time), ColorUtils.GRAY);
            }
        }
        if (blockEntity instanceof BaseLinkingTileEntity linking) {
            BlockEntity master = linking.getMaster();
            if (master instanceof FusionReactorTileEntity reactor) {
                addReactorInfo(helper, reactor, player);
            }
        }
    }

    public void addReactorInfo(JadeHelper helper, FusionReactorTileEntity reactor, Player player) {
        if (PlayerHandler.getHandler(player).hasThermometer()) {
            helper.bar(reactor.heat, 48000, translate("ic2.probe.reactor.heat.name",
                    Formatters.EU_READER_FORMAT.format(reactor.heat), Formatters.EU_READER_FORMAT.format((double) 48000)), getBarColor(reactor.heat, 48000));
        }
        int material = reactor.material;
        if (material > 0) {
            helper.bar(material, 10000, translate("ic2.progress.material.name", Formatters.EU_READER_FORMAT.format(material), Formatters.EU_READER_FORMAT.format(10000)), ColorUtils.GRAY);
        }
        helper.addTankInfo(reactor);
    }

    public static int getBarColor(int heat, int maxHeat) {
        float value = (float) heat / maxHeat;
        if (value < 0.60) {
            return ColorUtils.BLUE;
        } else if (value < 0.80) {
            return -4441721;
        } else {
            return ColorUtils.RED;
        }
    }
}
