package dev.crossvas.waila.ic2.providers;

import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import ic2.core.block.machine.tileentity.TileEntityTeleporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;

public class TeleporterInfo implements IInfoProvider {

    public static final TeleporterInfo THIS = new TeleporterInfo();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityTeleporter) {
            TileEntityTeleporter teleport = (TileEntityTeleporter) blockEntity;
            text(helper, new ChatComponentText("Target Set: " + status(teleport.targetSet)));
            if (teleport.targetSet) {
                text(helper, translatable("probe.teleporter.target", teleport.targetX, teleport.targetY, teleport.targetZ));
            }
        }
    }
}
