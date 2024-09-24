package dev.crossvas.waila.ic2.providers;

import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import ic2.api.tile.IWrenchable;
import ic2.core.block.inventory.IItemTransporter;
import ic2.core.item.tool.ItemToolWrench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

public class WrenchableInfo implements IInfoProvider {

    public static final WrenchableInfo THIS = new WrenchableInfo();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof IWrenchable) {
            IWrenchable wrenchableTile = (IWrenchable) blockEntity;
            int actualRate = (int) (wrenchableTile.getWrenchDropRate() * 100);
            ItemStack handItem = player.getHeldItem();
            if (actualRate > 0) {
                if (handItem != null && handItem.getItem() instanceof ItemToolWrench) {
                    text(helper, translatable("probe.wrenchable.drop_chance.info", new ChatComponentText(actualRate + "%").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA))).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
                } else {
                    text(helper, translatable("probe.wrenchable.info").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
                }
            }
        }
    }

    @Override
    public IItemTransporter.IFilter getFilter() {
        return ALWAYS;
    }
}
