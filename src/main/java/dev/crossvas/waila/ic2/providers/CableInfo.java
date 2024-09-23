package dev.crossvas.waila.ic2.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.utils.EnergyContainer;
import dev.crossvas.jadexic2c.utils.Formatter;
import ic2.core.block.wiring.tile.TileEntityCable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

public class CableInfo implements IInfoProvider {

    public static final CableInfo THIS = new CableInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityCable) {
            TileEntityCable cable = (TileEntityCable) blockEntity;
            text(helper, translatable("probe.energy.limit", cable.getConductorBreakdownEnergy() - 1));
            text(helper, translatable("probe.energy.loss", Formatter.CABLE_LOSS_FORMAT.format(cable.getConductionLoss())));
            EnergyContainer container = EnergyContainer.getContainer(cable);
            if (player.isSneaking()) {
                text(helper, translatable("probe.energy.stats.info").setStyle(new Style().setColor(TextFormatting.GREEN)), true);
                addCableOut(helper, container);
            } else {
                text(helper, translatable("probe.sneak.info").setStyle(new Style().setColor(TextFormatting.AQUA)), true);
            }
        }
    }
}
