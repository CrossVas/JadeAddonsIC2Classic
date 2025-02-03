package ic2.jadeplugin.providers;

import ic2.core.block.base.tiles.impls.BaseChargePadTileEntity;
import ic2.jadeplugin.base.JadeHelper;
import ic2.jadeplugin.base.interfaces.IInfoProvider;
import ic2.jadeplugin.helpers.EnergyContainer;
import ic2.jadeplugin.helpers.TextFormatter;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ChargePadInfo implements IInfoProvider {

    public static final ChargePadInfo THIS = new ChargePadInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseChargePadTileEntity chargePad) {
            int maxIn = chargePad.maxInput;
            int transfer = chargePad.transferLimit;
            float range = chargePad.range;

            helper.maxIn(maxIn);
            helper.defaultText("ic2.probe.chargepad.transferrate.name", TextFormatter.GREEN.literal(transfer + ""));
            helper.defaultText("ic2.probe.chargepad.radius.name", range + 1.0F);
            EnergyContainer container = EnergyContainer.getContainer(chargePad);
            helper.addAveragesIn(container);
        }
    }
}
