package dev.crossvas.jadexic2c.base;

import dev.crossvas.jadexic2c.helpers.EnergyContainer;
import ic2.core.inventory.filter.IFilter;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.utils.helpers.Formatters;
import ic2.core.utils.helpers.StackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

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

    default void text(IJadeHelper helper, String text, Object... args) {
        helper.addTextElement(Component.translatable(text, args), ChatFormatting.WHITE, false, false);
    }

    default void centeredText(IJadeHelper helper, String text, Object... args) {
        helper.addTextElement(Component.translatable(text, args), ChatFormatting.WHITE, false, true);
    }

    default void appendText(IJadeHelper helper, String text, Object... args) {
        helper.addTextElement(Component.translatable(text, args), ChatFormatting.WHITE, true, false);
    }

    default void text(IJadeHelper helper, boolean append, boolean centered, String text, Object... args) {
        helper.addTextElement(Component.translatable(text, args), ChatFormatting.WHITE, append, centered);
    }

    default void text(IJadeHelper helper, ChatFormatting formatting, String text, Object... args) {
        helper.addTextElement(Component.translatable(text, args), formatting, false, false);
    }

    default void centeredText(IJadeHelper helper, ChatFormatting formatting, String text, Object... args) {
        helper.addTextElement(Component.translatable(text, args), formatting, false, true);
    }

    default void appendText(IJadeHelper helper, ChatFormatting formatting, String text, Object... args) {
        helper.addTextElement(Component.translatable(text, args), formatting, true, false);
    }

    default void text(IJadeHelper helper, boolean append, boolean centered, ChatFormatting formatting, String text, Object... args) {
        helper.addTextElement(Component.translatable(text, args), formatting, append, centered);
    }

    default void text(IJadeHelper helper, Component text) {
        helper.addTextElement(text, ChatFormatting.WHITE, false, false);
    }

    default void centeredText(IJadeHelper helper, Component text) {
        helper.addTextElement(text, ChatFormatting.WHITE, false, true);
    }

    default void appendText(IJadeHelper helper, Component text) {
        helper.addTextElement(text, ChatFormatting.WHITE, true, false);
    }

    default void text(IJadeHelper helper, ChatFormatting formatting, Component text) {
        helper.addTextElement(text, formatting, false, false);
    }

    default void centeredText(IJadeHelper helper, ChatFormatting formatting, Component text) {
        helper.addTextElement(text, formatting, false, true);
    }

    default void appendText(IJadeHelper helper, ChatFormatting formatting, Component text) {
        helper.addTextElement(text, formatting, true, false);
    }

    default void text(IJadeHelper helper, boolean append, boolean centered, Component text) {
        helper.addTextElement(text, ChatFormatting.WHITE, append, centered);
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
        helper.addPaddingElement(0, 3);
        if (energyFlow > 0) {
            text(helper, ChatFormatting.AQUA, Component.translatable("tooltip.item.ic2.eu_reader.cable_flow", Formatters.EU_FORMAT.format(energyFlow)));
        }
        if (packetFlow > 0) {
            text(helper, ChatFormatting.AQUA, Component.translatable("tooltip.item.ic2.eu_reader.packet_flow", Formatters.EU_FORMAT.format(packetFlow)));
        }
    }

    default void addAverages(IJadeHelper helper, EnergyContainer container, boolean energyIn, boolean energyOut, boolean packetIn, boolean packetOut) {
        int avrIn = container.getAverageIn();
        int avrOut = container.getAverageOut();
        int pIn = container.getPacketsIn();
        int pOut = container.getPacketsOut();
        helper.addPaddingElement(0, 3);
        if (energyIn) {
            if (avrIn > 0) {
                text(helper, ChatFormatting.AQUA, Component.translatable("tooltip.item.ic2.eu_reader.cable_flow_in", Formatters.EU_FORMAT.format(avrIn)));
            }
        }
        if (energyOut) {
            if (avrOut > 0) {
                text(helper, ChatFormatting.AQUA, Component.translatable("tooltip.item.ic2.eu_reader.cable_flow_out", Formatters.EU_FORMAT.format(avrOut)));
            }
        }
        if (packetIn) {
            if (pIn > 0) {
                text(helper, ChatFormatting.AQUA, Component.translatable("tooltip.item.ic2.eu_reader.packet_flow_in", Formatters.EU_FORMAT.format(pIn)));
            }
        }
        if (packetOut) {
            if (pOut > 0) {
                text(helper, ChatFormatting.AQUA, Component.translatable("tooltip.item.ic2.eu_reader.packet_flow_out", Formatters.EU_FORMAT.format(pOut)));
            }
        }
    }
}
