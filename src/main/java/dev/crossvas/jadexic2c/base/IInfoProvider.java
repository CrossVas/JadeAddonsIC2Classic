package dev.crossvas.jadexic2c.base;

import ic2.core.inventory.filter.IFilter;
import ic2.core.inventory.filter.SpecialFilters;
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

    IFilter getFilter();

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
}
