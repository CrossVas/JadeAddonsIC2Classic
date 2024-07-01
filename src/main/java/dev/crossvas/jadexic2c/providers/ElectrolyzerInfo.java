package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.IInfoProvider;
import dev.crossvas.jadexic2c.base.IJadeHelper;
import ic2.core.block.machines.tiles.lv.ElectrolyzerTileEntity;
import ic2.core.block.machines.tiles.mv.ChargedElectrolyzerTileEntity;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ElectrolyzerInfo implements IInfoProvider {

    public static final ElectrolyzerInfo THIS = new ElectrolyzerInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof ElectrolyzerTileEntity electrolyzer) {
            addElectrolyzerInfo(helper, electrolyzer.shouldDrain(), electrolyzer.canPower(), electrolyzer.getTransferrate(), electrolyzer.energy, electrolyzer.maxEnergy);
        }

        if (blockEntity instanceof ChargedElectrolyzerTileEntity electrolyzer) {
            addElectrolyzerInfo(helper, electrolyzer.shouldDrain(), electrolyzer.canPower(), electrolyzer.getTransferrate(), electrolyzer.energy, electrolyzer.maxEnergy);
        }
    }

    public void addElectrolyzerInfo(IJadeHelper helper, boolean charging, boolean discharging, int transfer, int energy, int maxEnergy) {
        text(helper, "ic2.probe.electrolyzer.transferrate.name", transfer);
        text(helper, "ic2.probe.electrolyzer." + (discharging ? (charging ? "transfer" : "discharging") : (charging ? "charging" : "nothing")) + ".name");
        if (energy > 0) {
            helper.addBarElement(energy, maxEnergy, Component.translatable("ic2.probe.progress.full.name", energy, maxEnergy).append(" EU"), ColorUtils.RED);
        }
    }
}
