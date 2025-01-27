package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import ic2.core.block.machines.tiles.lv.ElectrolyzerTileEntity;
import ic2.core.block.machines.tiles.mv.ChargedElectrolyzerTileEntity;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ElectrolyzerInfo implements IInfoProvider {

    public static final ElectrolyzerInfo THIS = new ElectrolyzerInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof ElectrolyzerTileEntity electrolyzer) {
            addElectrolyzerInfo(helper, electrolyzer.shouldDrain(), electrolyzer.canPower(), electrolyzer.getTransferrate(), electrolyzer.energy, electrolyzer.maxEnergy);
        }

        if (blockEntity instanceof ChargedElectrolyzerTileEntity electrolyzer) {
            addElectrolyzerInfo(helper, electrolyzer.shouldDrain(), electrolyzer.canPower(), electrolyzer.getTransferrate(), electrolyzer.energy, electrolyzer.maxEnergy);
        }
    }

    public void addElectrolyzerInfo(JadeHelper helper, boolean charging, boolean discharging, int transfer, int energy, int maxEnergy) {
        helper.defaultText("ic2.probe.electrolyzer.transferrate.name", transfer);
        helper.defaultText("ic2.probe.electrolyzer." + (discharging ? (charging ? "transfer" : "discharging") : (charging ? "charging" : "nothing")) + ".name");
        if (energy > 0) {
            helper.bar(energy, maxEnergy, translate("ic2.probe.progress.full.name", energy, maxEnergy).append(" EU"), ColorUtils.RED);
        }
    }
}
