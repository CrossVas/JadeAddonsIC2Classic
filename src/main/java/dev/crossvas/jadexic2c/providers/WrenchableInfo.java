package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.core.block.base.tile.TileEntityBlock;
import ic2.core.block.base.util.info.misc.IWrench;
import ic2.core.block.personal.base.misc.IOwnerBlock;
import ic2.core.inventory.filters.IFilter;
import ic2.core.util.obj.IWrenchableTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class WrenchableInfo implements IInfoProvider {

    public static final WrenchableInfo THIS = new WrenchableInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof IWrenchableTile) {
            IWrenchableTile wrenchableTile = (IWrenchableTile) blockEntity;
            int actualRate = (int) (wrenchableTile.getWrenchDropRate() * 100);
            ItemStack handItem = player.getHeldItemMainhand();
            boolean show;
            if (wrenchableTile instanceof TileEntityBlock) {
                TileEntityBlock machines = (TileEntityBlock) wrenchableTile;
                show = machines.canRemoveBlockProbe(player);
            } else {
                show = actualRate > 0;
            }
            if (show) {
                if (handItem.getItem() instanceof IWrench) {
                    text(helper, translatable("probe.wrenchable.drop_chance.info", new TextComponentString(actualRate + "%").setStyle(new Style().setColor(TextFormatting.AQUA))).setStyle(new Style().setColor(TextFormatting.GRAY)));
                } else {
                    text(helper, translatable("probe.wrenchable.info").setStyle(new Style().setColor(TextFormatting.GOLD)));
                }
            }
        }
    }

    @Override
    public IFilter getFilter() {
        return ALWAYS;
    }
}
