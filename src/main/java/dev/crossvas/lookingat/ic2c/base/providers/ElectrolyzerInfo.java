package dev.crossvas.lookingat.ic2c.base.providers;

import dev.crossvas.lookingat.ic2c.base.interfaces.IInfoProvider;
import dev.crossvas.lookingat.ic2c.base.interfaces.IHelper;
import ic2.core.block.machines.tiles.lv.ElectrolyzerTileEntity;
import ic2.core.block.machines.tiles.mv.ChargedElectrolyzerTileEntity;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ElectrolyzerInfo implements IInfoProvider {

    public static final ElectrolyzerInfo THIS = new ElectrolyzerInfo();

    @Override
    public void addInfo(IHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof ElectrolyzerTileEntity electrolyzer) {
            addElectrolyzerInfo(helper, electrolyzer.shouldDrain(), electrolyzer.canPower(), electrolyzer.getTransferrate(), electrolyzer.energy, electrolyzer.maxEnergy);
        }

        if (blockEntity instanceof ChargedElectrolyzerTileEntity electrolyzer) {
            addElectrolyzerInfo(helper, electrolyzer.shouldDrain(), electrolyzer.canPower(), electrolyzer.getTransferrate(), electrolyzer.energy, electrolyzer.maxEnergy);
        }
    }

    public void addElectrolyzerInfo(IHelper helper, boolean charging, boolean discharging, int transfer, int energy, int maxEnergy) {
        defaultText(helper, "ic2.probe.electrolyzer.transferrate.name", transfer);
        defaultText(helper, "ic2.probe.electrolyzer." + (discharging ? (charging ? "transfer" : "discharging") : (charging ? "charging" : "nothing")) + ".name");
        if (energy > 0) {
            bar(helper, energy, maxEnergy, Component.translatable("ic2.probe.progress.full.name", energy, maxEnergy).append(" EU"), ColorUtils.RED);
        }
    }
}
