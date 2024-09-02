package dev.crossvas.jadexic2c.base.interfaces;

import dev.crossvas.jadexic2c.base.elements.CommonBarElement;
import dev.crossvas.jadexic2c.base.elements.CommonTextElement;
import ic2.core.inventory.filters.CommonFilters;
import ic2.core.inventory.filters.IFilter;
import ic2.core.util.misc.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public interface IInfoProvider {

    IFilter READER = CommonFilters.euReaderActive;
    IFilter THERMOMETER = CommonFilters.thermometerActive;
    IFilter CROP_ANALYZER = CommonFilters.cropAnalyzerActive;
    IFilter ALWAYS = CommonFilters.Anything;

    default IFilter getFilter() {
        return READER;
    }

    default boolean canHandle(EntityPlayer player) {
        return StackUtil.hasHotbarItem(player, getFilter()) || player.isCreative();
    }

    void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player);

    default void bar(IJadeHelper helper, int current, int max, ITextComponent text, int color) {
        CommonBarElement element = new CommonBarElement(current, max, text, color);
        add(helper, element);
    }

    default void text(IJadeHelper helper, ITextComponent text, boolean centered) {
        CommonTextElement element = new CommonTextElement(text, centered);
        add(helper, element);
    }

    default void text(IJadeHelper helper, ITextComponent text) {
        CommonTextElement element = new CommonTextElement(text, false);
        add(helper, element);
    }

    default void textCentered(IJadeHelper helper, ITextComponent text) {
        CommonTextElement element = new CommonTextElement(text, true);
        add(helper, element);
    }

    default void add(IJadeHelper helper, IJadeElementBuilder element) {
        helper.add(element);
    }

    default ITextComponent translatable(String translatable, Object... args) {
        return new TextComponentTranslation(translatable, args);
    }
}
