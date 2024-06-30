package dev.crossvas.jadexic2c.base;

import dev.crossvas.jadexic2c.JadeCommonHandler;
import dev.crossvas.jadexic2c.JadeCommonHelper;
import dev.crossvas.jadexic2c.JadeTags;
import dev.crossvas.jadexic2c.elements.SpecialTextElement;
import dev.crossvas.jadexic2c.helpers.Formatter;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import ic2.core.utils.math.ColorUtils;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.fluids.FluidStack;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.*;
import snownee.jade.impl.ui.ProgressStyle;

import java.util.List;

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
                if (serverTag.contains(JadeTags.TAG_TEXT, Tag.TAG_STRING)) {
                    Component text = Component.Serializer.fromJson(serverTag.getString(JadeTags.TAG_TEXT));
                    boolean append = serverTag.getBoolean("append");
                    boolean center = serverTag.getBoolean("center");
                    ChatFormatting formatting = ChatFormatting.getById(serverTag.getInt("formatting"));
                    ChatFormatting textFormatting = formatting == ChatFormatting.WHITE ? JadeCommonHelper.getFormattingStyle() : formatting;
                    if (append) {
                        tooltip.append(text.copy().withStyle(textFormatting));
                    } else {
                        if (center) {
                            tooltip.add(new SpecialTextElement(text.copy().withStyle(textFormatting)).centered(true));
                        } else {
                            tooltip.add(text.copy().withStyle(textFormatting));
                        }
                    }
                }
                if (serverTag.contains(JadeTags.TAG_ITEM)) {
                    ItemStack stack = ItemStack.of(serverTag.getCompound(JadeTags.TAG_ITEM));
                    Component text = Component.Serializer.fromJson(serverTag.getCompound(JadeTags.TAG_ITEM).getString("stackText"));
                    tooltip.add(helper.spacer(0, 3));
                    tooltip.add(helper.item(stack).align(IElement.Align.LEFT).translate(new Vec2(-2, -5)).size(new Vec2(16, 16)));
                    if (text != null) {
                        tooltip.append(text);
                    }
                }
                if (serverTag.contains(JadeTags.TAG_ENERGY)) {
                    int current = serverTag.getInt(JadeTags.TAG_ENERGY);
                    int max = serverTag.getInt(JadeTags.TAG_MAX);
                    Component label = Component.Serializer.fromJson(serverTag.getString("energyText"));
                    if (JadeCommonHelper.forceTopStyle()) {
                        BoxStyle boxStyle = JadeCommonHelper.getStyle(ColorUtils.CYAN);
                        IProgressStyle progressStyle = JadeCommonHelper.getProgressStyle(ColorUtils.CYAN);
                        tooltip.add(helper.progress((float) current / max, label, progressStyle, boxStyle, true));
                    } else {
                        IProgressStyle progressStyle = helper.progressStyle().color(-5636096, -10092544);
                        tooltip.add(helper.progress((float) current / max, label, progressStyle, BoxStyle.DEFAULT, true));
                    }
                }
                if (serverTag.contains(JadeTags.TAG_FLUID)) {
                    Block block = accessor.getBlock();
                    JadeCommonHandler.TANK_REMOVAL.add(block);
                    FluidStack fluid = FluidStack.loadFluidStackFromNBT(serverTag.getCompound(JadeTags.TAG_FLUID));
                    int max = serverTag.getInt(JadeTags.TAG_MAX);
                    if (fluid.getAmount() > 0) {
                        if (JadeCommonHelper.forceTopStyle()) {
                            IProgressStyle progressStyle = helper.progressStyle().overlay(helper.fluid(fluid));
                            tooltip.add(helper.progress((float) fluid.getAmount() / max, Component.translatable("ic2.barrel.info.fluid", fluid.getDisplayName(), Formatter.formatNumber(fluid.getAmount(), String.valueOf(fluid.getAmount()).length() - 1), Formatter.formatNumber(max, String.valueOf(max).length() - 1)).withStyle(JadeCommonHelper.getFormattingStyle()), progressStyle,
                                    JadeCommonHelper.getStyle(PluginHelper.getColorForFluid(fluid)), true));
                        } else {
                            String current = IDisplayHelper.get().humanReadableNumber(fluid.getAmount(), "B", true);
                            String maxS = IDisplayHelper.get().humanReadableNumber(max, "B", true);
                            Component text;
                            if (accessor.showDetails()) {
                                text = Component.translatable("jade.fluid2", IDisplayHelper.get().stripColor(fluid.getDisplayName()).withStyle(ChatFormatting.WHITE), Component.literal(current).withStyle(ChatFormatting.WHITE), maxS).withStyle(ChatFormatting.GRAY);
                            } else {
                                text = Component.translatable("jade.fluid", IDisplayHelper.get().stripColor(fluid.getDisplayName()), current);
                            }
                            IProgressStyle progressStyle = helper.progressStyle().overlay(helper.fluid(fluid));
                            tooltip.add(helper.progress((float) fluid.getAmount() / max, text, progressStyle, BoxStyle.DEFAULT, true));
                        }
                    }
                }
                if (serverTag.contains(JadeTags.TAG_BAR)) {
                    int color = serverTag.getInt(JadeTags.TAG_BAR_COLOR);
                    int current = serverTag.getInt(JadeTags.TAG_BAR);
                    int max = serverTag.getInt(JadeTags.TAG_MAX);
                    BoxStyle boxStyle = JadeCommonHelper.forceTopStyle() ? JadeCommonHelper.getStyle(color) : BoxStyle.DEFAULT;
                    IProgressStyle progressStyle = JadeCommonHelper.forceTopStyle() ? JadeCommonHelper.getProgressStyle(color) : new ProgressStyle().color(color, ColorUtils.darker(color));
                    Component label = Component.Serializer.fromJson(serverTag.getString("barText"));
                    tooltip.add(helper.progress((float) current / max, label, progressStyle, boxStyle, true));
                }
                if (serverTag.contains(JadeTags.TAG_PADDING)) {
                    int paddingX = serverTag.getInt(JadeTags.TAG_PADDING);
                    int paddingY = serverTag.getInt(JadeTags.TAG_PADDING_Y);
                    tooltip.add(helper.spacer(paddingX, paddingY));
                }
                if (serverTag.contains(JadeTags.TAG_INVENTORY)) {
                    Component label = Component.Serializer.fromJson(serverTag.getString("stacksText"));
                    ChatFormatting formatting = ChatFormatting.getById(serverTag.getInt("stackTextFormat"));
                    ListTag stackListTag = serverTag.getList(JadeTags.TAG_INVENTORY, Tag.TAG_COMPOUND);
                    List<ItemStack> stackList = new ObjectArrayList<>();
                    stackListTag.forEach(tag -> {
                        CompoundTag stackTag = (CompoundTag) tag;
                        ItemStack stack = ItemStack.of(stackTag.getCompound("stack"));
                        stack.setCount(stackTag.getInt("count"));
                        stackList.add(stack);
                    });

                    int counter = 0;
                    if (!stackList.isEmpty()) {
                        tooltip.add(helper.spacer(0, 5));
                        tooltip.add(label.copy().withStyle(formatting));
                        tooltip.add(helper.spacer(0, 2));
                        for (ItemStack stack : stackList) {
                            if (counter < 7) {
                                tooltip.append(helper.item(stack));
                                counter++;
                                if (counter == 6) {
                                    counter = 0;
                                    tooltip.add(helper.spacer(0, 0));
                                }
                            }
                        }
                        tooltip.add(helper.spacer(0, 2));
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
