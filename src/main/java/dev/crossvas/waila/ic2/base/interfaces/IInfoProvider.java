package dev.crossvas.waila.ic2.base.interfaces;

import dev.crossvas.waila.ic2.base.elements.CommonBarElement;
import dev.crossvas.waila.ic2.base.elements.CommonTextElement;
import dev.crossvas.waila.ic2.utils.EnergyContainer;
import dev.crossvas.waila.ic2.utils.Formatter;
import ic2.core.block.inventory.IItemTransporter;
import ic2.core.item.tool.ItemCropnalyzer;
import ic2.core.item.tool.ItemToolMeter;
import ic2.core.util.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public interface IInfoProvider {

    IItemTransporter.IFilter READER = stack -> stack.getItem() instanceof ItemToolMeter;
    IItemTransporter.IFilter CROP_ANALYZER = stack -> stack.getItem() instanceof ItemCropnalyzer;
    IItemTransporter.IFilter ALWAYS = itemStack -> true;

    default IItemTransporter.IFilter getFilter() {
        return READER;
    }

    default boolean canHandle(EntityPlayer player) {
        return hasHotbarItem(player, getFilter()) || player.capabilities.isCreativeMode;
    }

    default boolean hasHotbarItem(EntityPlayer player, ItemStack filter) {
        if (player == null) {
            return false;
        }
        InventoryPlayer inventoryPlayer = player.inventory;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inventoryPlayer.getStackInSlot(i);
            if (StackUtil.isStackEqual(stack, filter)) {
                return true;
            }
        }
        return false;
    }

    default boolean hasHotbarItem(EntityPlayer player, IItemTransporter.IFilter filter) {
        if (player == null) {
            return false;
        }
        InventoryPlayer inventoryPlayer = player.inventory;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inventoryPlayer.getStackInSlot(i);
            if (filter.matches(stack)) {
                return true;
            }
        }
        return false;
    }

    default String status(boolean status) {
        return status ? EnumChatFormatting.GREEN + String.valueOf(true) : EnumChatFormatting.RED + String.valueOf(false);
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

    default void addAveragesFull(IWailaHelper helper, EnergyContainer container) {
        addAveragesIn(helper, container);
        addAveragesOut(helper, container);
    }

    default void addAveragesIn(IWailaHelper helper, EnergyContainer container) {
        int averageIn = container.getAverageIn();
        if (averageIn > 0) {
            text(helper, translatable("tooltip.item.ic2.eu_reader.cable_flow_in", Formatter.EU_FORMAT.format(averageIn)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
        }
    }

    /**
     * common: energyOut, packetOut
     * */

    default void addAveragesOut(IWailaHelper helper, EnergyContainer container) {
        int averageOut = container.getAverageOut();
        if (averageOut > 0) {
            text(helper, translatable("tooltip.item.ic2.eu_reader.cable_flow_out", Formatter.EU_FORMAT.format(averageOut)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
        }
    }

    default void addCableOut(IWailaHelper helper, EnergyContainer container) {
        int averageOut = container.getAverageOut();
        if (averageOut > 0) {
            text(helper, translatable("tooltip.item.ic2.eu_reader.cable_flow", Formatter.EU_FORMAT.format(averageOut)).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA)));
        }
    }

    void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player);

    default void bar(IWailaHelper helper, int current, int max, IChatComponent text, int color) {
        this.bar(helper, current, max, text, color, "0");
        new ChatComponentTranslation("aaa");
    }

    default void bar(IWailaHelper helper, int current, int max, IChatComponent text, int color, String textureData) {
        CommonBarElement element = new CommonBarElement(current, max, text, color, textureData);
        add(helper, element);
    }

    default void text(IWailaHelper helper, IChatComponent text, boolean centered) {
        CommonTextElement element = new CommonTextElement(text, centered);
        add(helper, element);
    }

    default void text(IWailaHelper helper, IChatComponent text) {
        CommonTextElement element = new CommonTextElement(text, false);
        add(helper, element);
    }

    default void textCentered(IWailaHelper helper, IChatComponent text) {
        CommonTextElement element = new CommonTextElement(text, true);
        add(helper, element);
    }

    default void add(IWailaHelper helper, IWailaElementBuilder element) {
        helper.add(element);
    }

    default IChatComponent translatable(String translatable, Object... args) {
        return new ChatComponentTranslation(translatable, args);
    }

    default IChatComponent tier(int tier) {
        return translatable("probe.energy.tier", getDisplayTier(tier));
    }

    default IChatComponent maxIn(int maxIn) {
        return translatable("probe.energy.input.max", maxIn);
    }

    default IChatComponent usage(int usage) {
        return translatable("probe.energy.usage", usage);
    }
}
