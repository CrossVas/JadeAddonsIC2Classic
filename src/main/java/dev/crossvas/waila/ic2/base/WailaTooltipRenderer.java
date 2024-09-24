package dev.crossvas.waila.ic2.base;

import dev.crossvas.waila.ic2.WailaTags;
import dev.crossvas.waila.ic2.base.elements.CommonBarElement;
import dev.crossvas.waila.ic2.base.elements.CommonTextElement;
import dev.crossvas.waila.ic2.utils.IToolTipHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class WailaTooltipRenderer implements IWailaDataProvider, IToolTipHelper {

    public static final WailaTooltipRenderer THIS = new WailaTooltipRenderer();

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        this.addTooltips(tooltip, accessor, config);
        return tooltip;
    }

    public void addTooltips(List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        NBTTagCompound serverData = accessor.getNBTData();
        if (serverData == null) {
            return;
        }
        if (serverData.hasKey(WailaTags.TAG_DATA, 9)) {
            NBTTagList tagList = serverData.getTagList(WailaTags.TAG_DATA, 10);
            for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound serverTag = tagList.getCompoundTagAt(i);
                // text
                if (serverTag.hasKey(WailaTags.JADE_ADDON_TEXT_TAG)) {
                    NBTTagCompound elementTag = serverTag.getCompoundTag(WailaTags.JADE_ADDON_TEXT_TAG);
                    CommonTextElement textElement = CommonTextElement.load(elementTag);
                    IChatComponent text = textElement.getText();
                    boolean centered = textElement.isCentered();
                    text(tooltip, text, centered);
                }
                // bar
                if (serverTag.hasKey(WailaTags.JADE_ADDON_BAR_TAG)) {
                    NBTTagCompound elementTag = serverTag.getCompoundTag(WailaTags.JADE_ADDON_BAR_TAG);
                    CommonBarElement barElement = CommonBarElement.load(elementTag);
                    int color = barElement.getColor();
                    int current = barElement.getCurrent();
                    int max = barElement.getMax();
                    IChatComponent text = barElement.getText();
                    String texture = barElement.getTextureData();
                    bar(tooltip, current, max, color, text, texture);
                }
            }
        }
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tileEntity, NBTTagCompound tag, World world, int x, int y, int z) {
        WailaHelper helper = new WailaHelper();
        WailaCommonHandler.addInfo(helper, tileEntity, player);
        helper.transferData(tag);
        return tag;
    }

    // ####################

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        return list;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        return list;
    }
}
