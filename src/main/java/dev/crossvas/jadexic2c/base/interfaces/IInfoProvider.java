package dev.crossvas.jadexic2c.base.interfaces;

import dev.crossvas.jadexic2c.JadeCommonHelper;
import dev.crossvas.jadexic2c.base.elements.*;
import dev.crossvas.jadexic2c.helpers.EnergyContainer;
import ic2.core.inventory.filter.IFilter;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.utils.helpers.Formatters;
import ic2.core.utils.helpers.StackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IInfoProvider {

    IFilter READER = SpecialFilters.EU_READER;
    IFilter THERMOMETER = SpecialFilters.THERMOMETER;
    IFilter CROP_ANALYZER = SpecialFilters.CROP_SCANNER;
    IFilter ALWAYS = SpecialFilters.ALWAYS_TRUE;

    void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player);

    default IFilter getFilter() {
        return READER;
    }

    default boolean canHandle(Player player) {
        return StackUtil.hasHotbarItems(player, getFilter());
    }

    default void addAveragesFull(IJadeHelper helper, EnergyContainer container) {
        addAverages(helper, container, true, true, true, true);
    }

    /**
     * common: energyIn, packetIn
     * */
    default void addAveragesIn(IJadeHelper helper, EnergyContainer container) {
        addAverages(helper, container, true, false, true, false);
    }

    /**
     * common: energyOut, packetOut
     * */
    default void addAveragesOut(IJadeHelper helper, EnergyContainer container) {
        addAverages(helper, container, false, true, false, true);
    }

    default void addCableAverages(IJadeHelper helper, int energyFlow, int packetFlow) {
        paddingY(helper, 3);
        if (energyFlow > 0) {
            text(helper, Component.translatable("tooltip.item.ic2.eu_reader.cable_flow", Formatters.EU_FORMAT.format(energyFlow)).withStyle(ChatFormatting.AQUA));
        }
        if (packetFlow > 0) {
            text(helper, Component.translatable("tooltip.item.ic2.eu_reader.packet_flow", Formatters.EU_FORMAT.format(packetFlow)).withStyle(ChatFormatting.AQUA));
        }
    }

    default void addAverages(IJadeHelper helper, EnergyContainer container, boolean energyIn, boolean energyOut, boolean packetIn, boolean packetOut) {
        int avrIn = container.getAverageIn();
        int avrOut = container.getAverageOut();
        int pIn = container.getPacketsIn();
        int pOut = container.getPacketsOut();
        paddingY(helper, 3);
        if (energyIn) {
            if (avrIn > 0) {
                text(helper, Component.translatable("tooltip.item.ic2.eu_reader.cable_flow_in", Formatters.EU_FORMAT.format(avrIn)).withStyle(ChatFormatting.AQUA));
            }
        }
        if (energyOut) {
            if (avrOut > 0) {
                text(helper, Component.translatable("tooltip.item.ic2.eu_reader.cable_flow_out", Formatters.EU_FORMAT.format(avrOut)).withStyle(ChatFormatting.AQUA));
            }
        }
        if (packetIn) {
            if (pIn > 0) {
                text(helper, Component.translatable("tooltip.item.ic2.eu_reader.packet_flow_in", Formatters.EU_FORMAT.format(pIn)).withStyle(ChatFormatting.AQUA));
            }
        }
        if (packetOut) {
            if (pOut > 0) {
                text(helper, Component.translatable("tooltip.item.ic2.eu_reader.packet_flow_out", Formatters.EU_FORMAT.format(pOut)).withStyle(ChatFormatting.AQUA));
            }
        }
    }

    // new

    default void centered(IJadeHelper helper, Component component) {
        CommonTextElement element = new CommonTextElement(component, true);
        add(helper, element);
    }

    default void defaultCentered(IJadeHelper helper, Component component) {
        CommonTextElement element = new CommonTextElement(component.copy().withStyle(JadeCommonHelper.getFormattingStyle()), true);
        add(helper, element);
    }

    default void defaultCenteredText(IJadeHelper helper, String translatable, Object... args) {
        defaultCentered(helper, Component.translatable(translatable, args));
    }

    default void text(IJadeHelper helper, Component text) {
        CommonTextElement element = new CommonTextElement(text, false);
        add(helper, element);
    }

    default void appendText(IJadeHelper helper, Component text) {
        CommonTextElement element = new CommonTextElement(text, false);
        append(helper, element);
    }

    default void defaultText(IJadeHelper helper, String translatable, Object... args) {
        text(helper, Component.translatable(translatable, args).withStyle(JadeCommonHelper.getFormattingStyle()));
    }

    default void appendDefaultText(IJadeHelper helper, String translatable, Object... args) {
        appendText(helper, Component.translatable(translatable, args).withStyle(JadeCommonHelper.getFormattingStyle()));
    }

    default void defaultText(IJadeHelper helper, Component component) {
        text(helper, component.copy().withStyle(JadeCommonHelper.getFormattingStyle()));
    }

    default void appendDefaultText(IJadeHelper helper, Component component) {
        appendText(helper, component.copy().withStyle(JadeCommonHelper.getFormattingStyle()));
    }

    default void bar(IJadeHelper helper, int current, int max, Component text, int color) {
        CommonBarElement element = new CommonBarElement(current, max, text, color);
        add(helper, element);
    }

    default void fluid(IJadeHelper helper, FluidStack fluid, int capacity, boolean ignoreCapacity) {
        CommonFluidBarElement element = new CommonFluidBarElement(fluid, capacity, ignoreCapacity);
        add(helper, element);
    }

    default void fluid(IJadeHelper helper, FluidStack fluid, int capacity) {
        fluid(helper, fluid, capacity, false);
    }

    default void padding(IJadeHelper helper, int x, int y) {
        add(helper, new CommonPaddingElement(x, y));
    }

    default void paddingX(IJadeHelper helper, int x) {
        add(helper, new CommonPaddingElement(x, 0));
    }

    default void paddingY(IJadeHelper helper, int y) {
        add(helper, new CommonPaddingElement(0, y));
    }

    default void appendPadding(IJadeHelper helper, int x, int y) {
        append(helper, new CommonPaddingElement(x, y));
    }

    default void appendPaddingX(IJadeHelper helper, int x) {
        append(helper, new CommonPaddingElement(x, 0));
    }

    default void appendPaddingY(IJadeHelper helper, int y) {
        append(helper, new CommonPaddingElement(0, y));
    }

    default void add(IJadeHelper helper, IJadeElementBuilder element) {
        helper.add(element);
    }

    default void append(IJadeHelper helper, IJadeElementBuilder element) {
        helper.append(element);
    }

    default void item(IJadeHelper helper, ItemStack stack) {
        CommonItemStackElement stackElement = new CommonItemStackElement(stack, new Vec2(16, 16), new Vec2(0, -5), "LEFT");
        helper.add(stackElement);
    }

    default void appendItem(IJadeHelper helper, ItemStack stack) {
        CommonItemStackElement stackElement = new CommonItemStackElement(stack, new Vec2(16, 16), new Vec2(0, -5), "LEFT");
        helper.append(stackElement);
    }

    default void defaultItem(IJadeHelper helper, ItemStack stack) {
        CommonItemStackElement stackElement = new CommonItemStackElement(stack, new Vec2(16, 16), new Vec2(0, 0), "LEFT");
        helper.add(stackElement);
    }

    default void appendDefaultItem(IJadeHelper helper, ItemStack stack) {
        CommonItemStackElement stackElement = new CommonItemStackElement(stack, new Vec2(16, 16), new Vec2(0, 0), "LEFT");
        helper.append(stackElement);
    }

    default void item(IJadeHelper helper, ItemStack stack, Vec2 size, Vec2 translation) {
        CommonItemStackElement stackElement = new CommonItemStackElement(stack, size, translation, "LEFT");
        helper.add(stackElement);
    }

    default void appendItem(IJadeHelper helper, ItemStack stack, Vec2 size, Vec2 translation) {
        CommonItemStackElement stackElement = new CommonItemStackElement(stack, size, translation, "LEFT");
        helper.append(stackElement);
    }

    default void addGrid(IJadeHelper helper, List<ItemStack> stacks, Component component, int size) {
        int counter = 0;
        if (!stacks.isEmpty()) {
            paddingY(helper, 5);
            text(helper, component);
            paddingY(helper, 2);
            for (ItemStack stack : stacks) {
                if (counter < size + 1) {
                    appendDefaultItem(helper, stack);
                    counter++;
                    if (counter == size) {
                        counter = 0;
                        padding(helper, 0, 0);
                    }
                }
            }
            paddingY(helper, 2);
        }
    }

    default void addGrid(IJadeHelper helper, List<ItemStack> stacks, Component component) {
        addGrid(helper, stacks, component, 6);
    }
}
