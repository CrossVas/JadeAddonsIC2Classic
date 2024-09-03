package dev.crossvas.jadexic2c.base;

import dev.crossvas.jadexic2c.JadeTags;
import dev.crossvas.jadexic2c.base.elements.CommonBarElement;
import dev.crossvas.jadexic2c.base.elements.CommonTextElement;
import dev.crossvas.jadexic2c.utils.IToolTipHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class JadeTooltipRenderer implements IWailaDataProvider, IToolTipHelper {

    public static final JadeTooltipRenderer THIS = new JadeTooltipRenderer();

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        this.addTooltips(tooltip, accessor, config);
        return tooltip;
    }

    public void addTooltips(List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        NBTTagCompound serverData = accessor.getNBTData();
        if (serverData.hasKey(JadeTags.TAG_DATA, 9)) {
            NBTTagList tagList = serverData.getTagList(JadeTags.TAG_DATA, 10);
            for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound serverTag = tagList.getCompoundTagAt(i);
                // text
                if (serverTag.hasKey(JadeTags.JADE_ADDON_TEXT_TAG)) {
                    NBTTagCompound elementTag = serverTag.getCompoundTag(JadeTags.JADE_ADDON_TEXT_TAG);
                    CommonTextElement textElement = CommonTextElement.load(elementTag);
                    ITextComponent text = textElement.getText();
                    boolean centered = textElement.isCentered();
                    text(tooltip, text, centered);
                }
                // bar
                if (serverTag.hasKey(JadeTags.JADE_ADDON_BAR_TAG)) {
                    NBTTagCompound elementTag = serverTag.getCompoundTag(JadeTags.JADE_ADDON_BAR_TAG);
                    CommonBarElement barElement = CommonBarElement.load(elementTag);
                    int color = barElement.getColor();
                    int current = barElement.getCurrent();
                    int max = barElement.getMax();
                    ITextComponent text = barElement.getText();
                    String texture = barElement.getTextureData();
                    bar(tooltip, current, max, color, text, texture);
                }
            }
        }
    }

    @Nonnull
    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity tileEntity, NBTTagCompound tag, World world, BlockPos pos) {
        JadeHelper helper = new JadeHelper();
        JadeCommonHandler.addInfo(helper, tileEntity, player);
        helper.transferData(tag);
        return tag;
    }
}
