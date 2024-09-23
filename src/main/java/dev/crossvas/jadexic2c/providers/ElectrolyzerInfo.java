package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.utils.ColorUtils;
import ic2.api.classic.tile.IElectrolyzerProvider;
import ic2.core.Direction;
import ic2.core.block.machine.low.TileEntityElectrolyzer;
import ic2.core.block.machine.med.TileEntityChargedElectrolyzer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class ElectrolyzerInfo implements IInfoProvider {

    public static final ElectrolyzerInfo THIS = new ElectrolyzerInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityElectrolyzer) {
            TileEntityElectrolyzer electrolyzer = (TileEntityElectrolyzer) blockEntity;
            IElectrolyzerProvider provider = electrolyzer.provider;
            int transfer = 0;
            if (provider != null) {
                transfer = provider.getProcessRate();
            }
            addElectrolyzerInfo(helper, electrolyzer.shouldDrain(), electrolyzer.canPower(), transfer, electrolyzer.energy, electrolyzer.maxEnergy);
        }
        if (blockEntity instanceof TileEntityChargedElectrolyzer) {
            TileEntityChargedElectrolyzer electrolyzer = (TileEntityChargedElectrolyzer) blockEntity;
            addElectrolyzerInfo(helper, electrolyzer.shouldDrain(), electrolyzer.canPower(), getTransferRate(electrolyzer), electrolyzer.energy, electrolyzer.maxEnergy);
        }
    }

    public void addElectrolyzerInfo(IJadeHelper helper, boolean charging, boolean discharging, int transfer, int energy, int maxEnergy) {
        text(helper, translatable("probe.energy.transfer", transfer));
        text(helper, translatable("probe.electrolyzer." + (discharging ? (charging ? "transfer" : "discharging") : (charging ? "charging" : "nothing"))));
        if (energy > 0) {
            bar(helper, energy, maxEnergy, translatable("probe.progress.full_misc.name", energy, maxEnergy).appendText(" EU"), ColorUtils.RED);
        }
    }

    public int getTransferRate(TileEntityChargedElectrolyzer electrolyzer) {
        int value = 0;
        for (Direction direction : Direction.directions) {
            IElectrolyzerProvider provider = electrolyzer.providers[direction.toSideValue()];
            if (provider != null) {
                value += provider.getProcessRate();
            }
        }
        return value;
    }
}
