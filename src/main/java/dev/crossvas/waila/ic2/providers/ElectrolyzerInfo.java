package dev.crossvas.waila.ic2.providers;

import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import dev.crossvas.waila.ic2.utils.ColorUtils;
import ic2.api.Direction;
import ic2.core.block.machine.tileentity.TileEntityCharged;
import ic2.core.block.machine.tileentity.TileEntityElectrolyzer;
import ic2.core.block.wiring.IElectrolyzerProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class ElectrolyzerInfo implements IInfoProvider {

    public static final ElectrolyzerInfo THIS = new ElectrolyzerInfo();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityElectrolyzer) {
            TileEntityElectrolyzer electrolyzer = (TileEntityElectrolyzer) blockEntity;
            IElectrolyzerProvider provider = electrolyzer.mfe;
            int transfer = 0;
            if (provider != null) {
                transfer = provider.getProcessRate();
            }
            addElectrolyzerInfo(helper, electrolyzer.shouldDrain(), electrolyzer.canPower(), transfer, electrolyzer.energy, electrolyzer.maxEnergy);
        }
        if (blockEntity instanceof TileEntityCharged) {
            TileEntityCharged electrolyzer = (TileEntityCharged) blockEntity;
            addElectrolyzerInfo(helper, electrolyzer.shouldDrain(), electrolyzer.canPower(), getTransferRate(electrolyzer), electrolyzer.energy, electrolyzer.maxEnergy);
        }
    }

    public void addElectrolyzerInfo(IWailaHelper helper, boolean charging, boolean discharging, int transfer, int energy, int maxEnergy) {
        text(helper, translate("probe.energy.transfer", transfer));
        text(helper, translate("probe.electrolyzer." + (discharging ? (charging ? "transfer" : "discharging") : (charging ? "charging" : "nothing"))));
        if (energy > 0) {
            bar(helper, energy, maxEnergy, translate("probe.progress.full_misc.name", energy, maxEnergy).appendText(" EU"), ColorUtils.RED);
        }
    }

    public int getTransferRate(TileEntityCharged electrolyzer) {
        int value = 0;
        for (Direction direction : Direction.directions) {
            IElectrolyzerProvider provider = electrolyzer.getProviders()[direction.toSideValue()];
            if (provider != null) {
                value += provider.getProcessRate();
            }
        }
        return value;
    }
}
