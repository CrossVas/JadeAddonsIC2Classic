package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import ic2.core.block.base.features.personal.IPersonalTile;
import ic2.core.block.base.tiles.impls.BasePersonalTileEntity;
import ic2.core.block.machines.tiles.mv.ChunkloaderTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.UUID;

public class PersonalInfo implements IInfoProvider {

    public static final PersonalInfo THIS = new PersonalInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof IPersonalTile personal) {
            UUID ownerUUID = personal.getOwner();
            Player owner = player.level.getPlayerByUUID(ownerUUID);
            if (owner != null) {
                helper.text(translate("ic2.probe.personal.owner", owner.getDisplayName().copy().withStyle(ChatFormatting.GREEN)).withStyle(ChatFormatting.AQUA));
            }
            if (personal instanceof BasePersonalTileEntity basePersonal) {
                addAccessInfo(helper, basePersonal.mode);
            }
            if (personal instanceof ChunkloaderTileEntity chunkloader) {
                addAccessInfo(helper, chunkloader.mode);
            }
        }
    }

    public void addAccessInfo(JadeHelper helper, int mode) {
        Component[] modes = new Component[]{translate("gui.ic2.personal.mode.public"),
                translate("gui.ic2.personal.mode.protected"),
                translate("gui.ic2.personal.mode.private")};
        helper.text(translate("gui.ic2.personal.mode", modes[mode].copy().withStyle(mode == 0 ? ChatFormatting.GREEN : mode == 1 ? ChatFormatting.GOLD : ChatFormatting.RED)).withStyle(ChatFormatting.LIGHT_PURPLE));
    }
}
