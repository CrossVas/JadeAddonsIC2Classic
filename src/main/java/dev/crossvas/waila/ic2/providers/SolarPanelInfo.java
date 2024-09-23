package dev.crossvas.waila.ic2.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.utils.Formatter;
import ic2.core.block.generator.tile.TileEntitySolarPanel;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class SolarPanelInfo implements IInfoProvider {

    public static final SolarPanelInfo THIS = new SolarPanelInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntitySolarPanel) {
            TileEntitySolarPanel solarPanel = (TileEntitySolarPanel) blockEntity;
            text(helper, tier(solarPanel.getSourceTier()));
            text(helper, translatable("probe.energy.output", Formatter.formatNumber(solarPanel.getOutput(), 3)));
            text(helper, translatable("probe.energy.output.max", solarPanel.getOutput()));
        }
    }
}
