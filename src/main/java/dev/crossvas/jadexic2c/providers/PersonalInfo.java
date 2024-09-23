package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.core.block.personal.base.TileEntityPersonalStorageBase;
import ic2.core.block.personal.base.misc.IOwnerBlock;
import ic2.core.block.personal.tile.TileEntityEnergyOMat;
import ic2.core.block.personal.tile.TileEntityIridiumStone;
import ic2.core.block.personal.tile.TileEntityTradeOMat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

public class PersonalInfo implements IInfoProvider {

    public static final PersonalInfo THIS = new PersonalInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof IOwnerBlock) {
            IOwnerBlock personal = (IOwnerBlock) blockEntity;
            EntityPlayer owner = null;
            if (personal instanceof TileEntityPersonalStorageBase) {
                TileEntityPersonalStorageBase personalStorage = (TileEntityPersonalStorageBase) personal;
                owner = player.world.getPlayerEntityByUUID(personalStorage.owner);
                addOwnerInfo(helper, owner);
                text(helper, translatable("probe.personal.view", status(personalStorage.allowView)).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)));
                text(helper, translatable("probe.personal.import", status(personalStorage.allowInjection)).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)));
                text(helper, translatable("probe.personal.export", status(personalStorage.allowExtraction)).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)));
                text(helper, translatable("probe.personal.team", status(personalStorage.allowTeam)).setStyle(new Style().setColor(TextFormatting.LIGHT_PURPLE)));
            }
            if (personal instanceof TileEntityIridiumStone) {
                TileEntityIridiumStone personalStone = (TileEntityIridiumStone) personal;
                owner = player.world.getPlayerEntityByUUID(personalStone.owner);
                addOwnerInfo(helper, owner);
            }
            if (personal instanceof TileEntityTradeOMat) {
                TileEntityTradeOMat tradeOMat = (TileEntityTradeOMat) personal;
                owner = player.world.getPlayerEntityByUUID(tradeOMat.owner);
                addOwnerInfo(helper, owner);
            }
            if (personal instanceof TileEntityEnergyOMat) {
                TileEntityEnergyOMat tradeOMat = (TileEntityEnergyOMat) personal;
                owner = player.world.getPlayerEntityByUUID(tradeOMat.owner);
                addOwnerInfo(helper, owner);
            }
        }
    }

    public void addOwnerInfo(IJadeHelper helper, EntityPlayer owner) {
        if (owner != null) {
            text(helper, translatable("ic2.probe.personal.owner", owner.getDisplayName().setStyle(new Style().setColor(TextFormatting.GREEN))).setStyle(new Style().setColor(TextFormatting.AQUA)));
        }
    }
}
