package dev.crossvas.jadexic2c.base.interfaces;

import dev.crossvas.jadexic2c.base.elements.CommonBarElement;
import dev.crossvas.jadexic2c.base.elements.CommonTextElement;
import dev.crossvas.jadexic2c.utils.EnergyContainer;
import dev.crossvas.jadexic2c.utils.Formatter;
import ic2.core.inventory.filters.CommonFilters;
import ic2.core.inventory.filters.IFilter;
import ic2.core.util.misc.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

public interface IInfoProvider {

    IFilter READER = CommonFilters.euReaderActive;
    IFilter THERMOMETER = CommonFilters.thermometerActive;
    IFilter CROP_ANALYZER = CommonFilters.cropAnalyzerActive;
    IFilter ALWAYS = itemStack -> true;

    default IFilter getFilter() {
        return READER;
    }

    default boolean canHandle(EntityPlayer player) {
        return StackUtil.hasHotbarItem(player, getFilter()) || player.isCreative();
    }

    default String getDisplayTier(int tier) {
        switch (tier) {
            case 1: return "LV";
            case 2: return "MV";
            case 3: return "HV";
            case 4: return "EV";
            case 5: return "IV";
            case 6: return "LuV";
            default: return String.valueOf(tier);
        }
    }

    default void addAveragesFull(IJadeHelper helper, EnergyContainer container) {
        addAveragesIn(helper, container);
        addAveragesOut(helper, container);
    }

    default void addAveragesIn(IJadeHelper helper, EnergyContainer container) {
        int averageIn = container.getAverageIn();
        if (averageIn > 0) {
            text(helper, translatable("tooltip.item.ic2.eu_reader.cable_flow_in", Formatter.EU_FORMAT.format(averageIn)).setStyle(new Style().setColor(TextFormatting.AQUA)));
        }
    }

    /**
     * common: energyOut, packetOut
     * */

    default void addAveragesOut(IJadeHelper helper, EnergyContainer container) {
        int averageOut = container.getAverageOut();
        if (averageOut > 0) {
            text(helper, translatable("tooltip.item.ic2.eu_reader.cable_flow_out", Formatter.EU_FORMAT.format(averageOut)).setStyle(new Style().setColor(TextFormatting.AQUA)));
        }
    }

    default void addCableOut(IJadeHelper helper, EnergyContainer container) {
        int averageOut = container.getAverageOut();
        if (averageOut > 0) {
            text(helper, translatable("tooltip.item.ic2.eu_reader.cable_flow", Formatter.EU_FORMAT.format(averageOut)).setStyle(new Style().setColor(TextFormatting.AQUA)));
        }
    }

    void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player);

    default void bar(IJadeHelper helper, int current, int max, ITextComponent text, int color) {
        this.bar(helper, current, max, text, color, "0");
    }

    default void bar(IJadeHelper helper, int current, int max, ITextComponent text, int color, String textureData) {
        CommonBarElement element = new CommonBarElement(current, max, text, color, textureData);
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
