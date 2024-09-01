package dev.crossvas.jadexic2c.base;

import com.google.gson.JsonObject;
import dev.crossvas.jadexic2c.JadeTags;
import dev.crossvas.jadexic2c.base.elements.*;
import dev.crossvas.jadexic2c.elements.SpecialBoxStyle;
import dev.crossvas.jadexic2c.elements.SpecialProgressStyle;
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
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.fluids.FluidStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.*;
import snownee.jade.impl.ui.ProgressStyle;

import java.util.Locale;

import static dev.crossvas.jadexic2c.JadeTags.*;

public class JadeTooltipRenderer implements IBlockComponentProvider, IServerDataProvider<BlockEntity> {

    public static final JadeTooltipRenderer INSTANCE = new JadeTooltipRenderer();

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        appendTooltips(iTooltip, blockAccessor, iPluginConfig);
    }

    private void appendTooltips(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        boolean forceTOPStyle = config.get(TOP_STYLE);
        ChatFormatting defaultFormatting = config.get(JadeTags.TOP_STYLE) ? ChatFormatting.WHITE : ChatFormatting.GRAY;
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
                    addElement(tooltip, jadeElement, elementTag);
                }
                // text
                if (serverTag.contains(JADE_ADDON_TEXT_TAG)) {
                    CompoundTag elementTag = serverTag.getCompound(JADE_ADDON_TEXT_TAG);
                    CommonTextElement textElement = CommonTextElement.load(elementTag);
                    boolean centered = textElement.isCentered();
                    IElement jadeElement = new SpecialTextElement(refreshComponent(textElement.getText(), defaultFormatting)).centered(centered).translate(textElement.getTranslation()).align(IElement.Align.valueOf(textElement.getSide()));
                    addElement(tooltip, jadeElement, elementTag);
                }
                // bar
                if (serverTag.contains(JADE_ADDON_BAR_TAG)) {
                    CompoundTag elementTag = serverTag.getCompound(JADE_ADDON_BAR_TAG);
                    CommonBarElement barElement = CommonBarElement.load(elementTag);
                    int color = barElement.getColor();
                    int current = barElement.getCurrent();
                    int max = barElement.getMax();
                    BoxStyle boxStyle = forceTOPStyle ? new SpecialBoxStyle(ColorUtils.doubleDarker(color)) : BoxStyle.DEFAULT;
                    IProgressStyle progressStyle = forceTOPStyle ? new SpecialProgressStyle().color(color, ColorUtils.darker(color)) : new ProgressStyle().color(color, ColorUtils.darker(color));
                    Component label = barElement.getText();
                    IElement jadeElement = helper.progress((float) current / max, label, progressStyle, boxStyle, true);
                    addElement(tooltip, jadeElement, elementTag);
                }
                // item
                if (serverTag.contains(JADE_ADDON_ITEM_TAG)) {
                    CompoundTag elementTag = serverTag.getCompound(JADE_ADDON_ITEM_TAG);
                    CommonItemStackElement stackElement = CommonItemStackElement.load(elementTag);
                    ItemStack stack = stackElement.getStack();
                    IElement jadeElement = tooltip.getElementHelper().item(stack).size(new Vec2(16, 16)).translate(stackElement.getTranslation()).align(IElement.Align.valueOf(stackElement.getSide()));
                    addElement(tooltip, jadeElement, elementTag);
                }
                // fluid
                if (serverTag.contains(JADE_ADDON_FLUID_TAG)) {
                    CompoundTag elementTag = serverTag.getCompound(JADE_ADDON_FLUID_TAG);
                    CommonFluidBarElement fluidElement = CommonFluidBarElement.load(elementTag);
                    FluidStack fluid = fluidElement.getFluid();
                    int fluidAmount = fluid.getAmount();
                    int max = fluidElement.getMax();
                    boolean ignoreCapacity = fluidElement.ignoreCapacity();
                    if (fluidAmount > 0) {
                        if (forceTOPStyle) {
                            Component fluidComp = ignoreCapacity ? fluid.getDisplayName().copy().withStyle(defaultFormatting) :
                                    Component.translatable("ic2.barrel.info.fluid", fluid.getDisplayName(), Formatter.formatNumber(fluidAmount, String.valueOf(fluidAmount).length() - 1), Formatter.formatNumber(max, String.valueOf(max).length() - 1)).withStyle(defaultFormatting);
                            IProgressStyle progressStyle = helper.progressStyle().overlay(helper.fluid(fluid));
                            tooltip.add(helper.progress((float) fluid.getAmount() / max, fluidComp, progressStyle,
                                    new SpecialBoxStyle(ColorUtils.doubleDarker(PluginHelper.getColorForFluid(fluid))), true));
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

    public void addElement(ITooltip iTooltip, IElement jadeElement, CompoundTag elementTag) {
        boolean add = elementTag.getBoolean(JadeHelper.ADD_TAG);
        boolean append = elementTag.getBoolean(JadeHelper.APPEND_TAG);
        if (add) {
            iTooltip.add(jadeElement);
        }
        if (append) {
            iTooltip.append(jadeElement);
        }
    }

    public static Component refreshComponent(Component component, ChatFormatting defaultFormatting) {
        ChatFormatting formatting = defaultFormatting;
        JsonObject json = Component.Serializer.toJsonTree(component).getAsJsonObject();
        if (json.has("color")) {
            String color = json.get("color").getAsString();
            if (!color.isEmpty()) {
                formatting = ChatFormatting.valueOf(color.toUpperCase(Locale.ROOT));
            }
        }
        return component.copy().withStyle(formatting);
    }

    @Override
    public ResourceLocation getUid() {
        return JadeTags.INFO_RENDERER;
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        JadeHelper helper = new JadeHelper();
        JadeCommonHandler.addInfo(helper, blockEntity, serverPlayer);
        helper.transferData(compoundTag);
    }
}
