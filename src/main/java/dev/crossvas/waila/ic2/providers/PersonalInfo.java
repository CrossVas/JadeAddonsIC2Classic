package dev.crossvas.waila.ic2.providers;

import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import dev.crossvas.waila.ic2.utils.TextFormatter;
import ic2.core.block.personal.TileEntityPersonalChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class PersonalInfo implements IInfoProvider {

    public static final PersonalInfo THIS = new PersonalInfo();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        EntityPlayer owner = null;
        if (blockEntity instanceof TileEntityPersonalChest) {
            TileEntityPersonalChest personalStorage = (TileEntityPersonalChest) blockEntity;
            owner = getPlayerEntityByUUID(player.worldObj, personalStorage.owner);
            addOwnerInfo(helper, owner);
        }
//        if (blockEntity instanceof TileEntityTradeOMat) {
//            TileEntityTradeOMat tradeOMat = (TileEntityTradeOMat) blockEntity;
//            owner = getPlayerEntityByUUID(player.worldObj, tradeOMat.owner);
//            addOwnerInfo(helper, owner);
//        }
//        if (blockEntity instanceof TileEntityEnergyOMat) {
//            TileEntityEnergyOMat tradeOMat = (TileEntityEnergyOMat) blockEntity;
//            owner = getPlayerEntityByUUID(player.worldObj, tradeOMat.owner);
//            addOwnerInfo(helper, owner);
//        }
//        if (blockEntity instanceof TileEntityFluidOMat) {
//            TileEntityFluidOMat tradeOMat = (TileEntityFluidOMat) blockEntity;
//            owner = getPlayerEntityByUUID(player.worldObj, tradeOMat.own);
//            addOwnerInfo(helper, owner);
//        }
    }

    public void addOwnerInfo(IWailaHelper helper, EntityPlayer owner) {
        if (owner != null) {
            text(helper, translate(TextFormatter.AQUA, "probe.personal.owner", literal(TextFormatter.GREEN, owner.getDisplayName())));

        }
    }

    @Nullable
    public EntityPlayer getPlayerEntityByUUID(World world, UUID uuid) {
        for (int i = 0; i < world.playerEntities.size(); ++i) {
            EntityPlayer entityplayer = (EntityPlayer) world.playerEntities.get(i);
            if (uuid.equals(entityplayer.getUniqueID())) {
                return entityplayer;
            }
        }
        return null;
    }
}
