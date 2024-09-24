package dev.crossvas.waila.ic2.providers;

import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import ic2.core.block.machine.tileentity.TileEntityTeleporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class TeleporterInfo implements IInfoProvider {

    public static final TeleporterInfo THIS = new TeleporterInfo();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityTeleporter) {
            TileEntityTeleporter teleport = (TileEntityTeleporter) blockEntity;
            text(helper, translate("probe.teleporter.target.set", status(teleport.targetSet)));
            if (teleport.targetSet) {
                text(helper, translate("probe.teleporter.target", teleport.targetX, teleport.targetY, teleport.targetZ));
            }
        }
    }
}
