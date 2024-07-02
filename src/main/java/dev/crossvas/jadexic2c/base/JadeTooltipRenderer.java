package dev.crossvas.jadexic2c.base;

import dev.crossvas.jadexic2c.JadeCommonHelper;
import dev.crossvas.jadexic2c.JadeHelper;
import dev.crossvas.jadexic2c.JadeTags;
import dev.crossvas.jadexic2c.base.elements.*;
import dev.crossvas.jadexic2c.elements.SpecialTextElement;
import dev.crossvas.jadexic2c.helpers.Formatter;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import ic2.core.utils.math.ColorUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.*;
import snownee.jade.impl.ui.ProgressStyle;

import static dev.crossvas.jadexic2c.JadeTags.*;

public class JadeTooltipRenderer implements IBlockComponentProvider {

    public static final JadeTooltipRenderer INSTANCE = new JadeTooltipRenderer();

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        appendTooltips(iTooltip, blockAccessor);
    }

    private void appendTooltips(ITooltip tooltip, BlockAccessor accessor) {
        CompoundTag serverData = accessor.getServerData();
        IElementHelper helper = tooltip.getElementHelper();
        if (serverData.contains(JadeTags.TAG_DATA, Tag.TAG_LIST)) {
            ListTag tagList = serverData.getList(JadeTags.TAG_DATA, Tag.TAG_COMPOUND);
            for (int i = 0; i < tagList.size(); i++) {
                CompoundTag serverTag = tagList.getCompound(i);
                // padding
                if (serverTag.contains(JADE_ADDON_PADDING_TAG)) {
                    CompoundTag elementTag = serverTag.getCompound(JADE_ADDON_PADDING_TAG);
                    CommonPaddingElement paddingElement = CommonPaddingElement.load(elementTag);
                    int x = paddingElement.getX();
                    int y = paddingElement.getY();
                    IElement jadeElement = tooltip.getElementHelper().spacer(x, y);
                    boolean add = elementTag.getBoolean(JadeHelper.ADD_TAG);
                    boolean append = elementTag.getBoolean(JadeHelper.APPEND_TAG);
                    if (add) {
                        tooltip.add(jadeElement);
                    }
                    if (append) {
                        tooltip.append(jadeElement);
                    }
                }
                // text
                if (serverTag.contains(JADE_ADDON_TEXT_TAG)) {
                    CompoundTag elementTag = serverTag.getCompound(JADE_ADDON_TEXT_TAG);
                    CommonTextElement textElement = CommonTextElement.load(elementTag);
                    boolean centered = textElement.isCentered();
                    IElement jadeElement = new SpecialTextElement(textElement.getText()).centered(centered).size(textElement.getSize()).translate(textElement.getTranslation()).align(IElement.Align.valueOf(textElement.getSide()));
                    boolean add = elementTag.getBoolean(JadeHelper.ADD_TAG);
                    boolean append = elementTag.getBoolean(JadeHelper.APPEND_TAG);
                    if (add) {
                        tooltip.add(jadeElement);
                    }
                    if (append) {
                        tooltip.append(jadeElement);
                    }
                }
                // bar
                if (serverTag.contains(JADE_ADDON_BAR_TAG)) {
                    CompoundTag elementTag = serverTag.getCompound(JADE_ADDON_BAR_TAG);
                    CommonBarElement barElement = CommonBarElement.load(elementTag);
                    int color = barElement.getColor();
                    int current = barElement.getCurrent();
                    int max = barElement.getMax();
                    BoxStyle boxStyle = JadeCommonHelper.forceTopStyle() ? JadeCommonHelper.getStyle(color) : BoxStyle.DEFAULT;
                    IProgressStyle progressStyle = JadeCommonHelper.forceTopStyle() ? JadeCommonHelper.getProgressStyle(color) : new ProgressStyle().color(color, ColorUtils.darker(color));
                    Component label = barElement.getText();
                    IElement jadeElement = helper.progress((float) current / max, label, progressStyle, boxStyle, true);
                    boolean add = elementTag.getBoolean(JadeHelper.ADD_TAG);
                    boolean append = elementTag.getBoolean(JadeHelper.APPEND_TAG);
                    if (add) {
                        tooltip.add(jadeElement);
                    }
                    if (append) {
                        tooltip.append(jadeElement);
                    }
                }
                // item
                if (serverTag.contains(JADE_ADDON_ITEM_TAG)) {
                    CompoundTag elementTag = serverTag.getCompound(JADE_ADDON_ITEM_TAG);
                    CommonItemStackElement stackElement = CommonItemStackElement.load(elementTag);
                    ItemStack stack = stackElement.getStack();
                    boolean add = elementTag.getBoolean(JadeHelper.ADD_TAG);
                    boolean append = elementTag.getBoolean(JadeHelper.APPEND_TAG);
                    IElement jadeElement = tooltip.getElementHelper().item(stack).size(stackElement.getSize()).translate(stackElement.getTranslation()).align(IElement.Align.valueOf(stackElement.getSide()));
                    if (add) {
                        tooltip.add(jadeElement);
                    }
                    if (append) {
                        tooltip.append(jadeElement);
                    }
                }
                // fluid
                if (serverTag.contains(JADE_ADDON_FLUID_TAG)) {
                    CompoundTag elementTag = serverTag.getCompound(JADE_ADDON_FLUID_TAG);
                    CommonFluidBarElement fluidElement = CommonFluidBarElement.load(elementTag);
                    FluidStack fluid = fluidElement.getFluid();
                    int fluidAmount = fluid.getAmount();
                    int max = fluidElement.getMax();
                    boolean ignoreCapacity = fluidElement.ignoreCapacity();
                    if (ignoreCapacity) {
                        fluidAmount = 1;
                        max = 1;
                    }

                    if (fluidAmount > 0) {
                        if (JadeCommonHelper.forceTopStyle()) {
                            Component fluidComp = ignoreCapacity ? fluid.getDisplayName().copy().withStyle(JadeCommonHelper.getFormattingStyle()) :
                                    Component.translatable("ic2.barrel.info.fluid", fluid.getDisplayName(), Formatter.formatNumber(fluidAmount, String.valueOf(fluidAmount).length() - 1), Formatter.formatNumber(max, String.valueOf(max).length() - 1)).withStyle(JadeCommonHelper.getFormattingStyle());
                            IProgressStyle progressStyle = helper.progressStyle().overlay(helper.fluid(fluid));
                            tooltip.add(helper.progress((float) fluid.getAmount() / max, fluidComp, progressStyle,
                                    JadeCommonHelper.getStyle(PluginHelper.getColorForFluid(fluid)), true));
                        } else {
                            String current = IDisplayHelper.get().humanReadableNumber(fluid.getAmount(), "B", true);
                            String maxS = IDisplayHelper.get().humanReadableNumber(max, "B", true);
                            Component text;
                            if (ignoreCapacity) {
                                text = fluid.getDisplayName();
                            } else {
                                if (accessor.showDetails()) {
                                    text = Component.translatable("jade.fluid2", IDisplayHelper.get().stripColor(fluid.getDisplayName()).withStyle(ChatFormatting.WHITE), Component.literal(current).withStyle(ChatFormatting.WHITE), maxS).withStyle(ChatFormatting.GRAY);
                                } else {
                                    text = Component.translatable("jade.fluid", IDisplayHelper.get().stripColor(fluid.getDisplayName()), current);
                                }
                            }
                            IProgressStyle progressStyle = helper.progressStyle().overlay(helper.fluid(fluid));
                            tooltip.add(helper.progress((float) fluidAmount / max, text, progressStyle, BoxStyle.DEFAULT, true));
                        }
                    }
                }
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeTags.INFO_RENDERER;
    }
}
