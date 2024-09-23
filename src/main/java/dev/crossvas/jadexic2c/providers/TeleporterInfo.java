package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.core.block.machine.high.TileEntityTeleporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class TeleporterInfo implements IInfoProvider {

    public static final TeleporterInfo THIS = new TeleporterInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityTeleporter) {
            TileEntityTeleporter teleport = (TileEntityTeleporter) blockEntity;
            if (teleport.targetSet) {
                text(helper, translatable("probe.teleporter.type", translatable("probe.teleporter.type." + teleport.getSendingType().name().toLowerCase())));
                text(helper, translatable("probe.teleporter.target", teleport.targetPos.getX(), teleport.targetPos.getY(), teleport.targetPos.getZ()));
            }
        }
    }
}
