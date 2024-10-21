package dev.crossvas.waila.ic2.base.interfaces;

import akka.util.Helpers;
import dev.crossvas.waila.ic2.WailaIC2Classic;
import dev.crossvas.waila.ic2.base.elements.CommonBarElement;
import dev.crossvas.waila.ic2.base.elements.CommonTextElement;
import dev.crossvas.waila.ic2.utils.EnergyContainer;
import dev.crossvas.waila.ic2.utils.Formatter;
import dev.crossvas.waila.ic2.utils.TextFormatter;
import ic2.core.block.inventory.IItemTransporter;
import ic2.core.item.armor.ItemArmorNanoSuit;
import ic2.core.item.armor.ItemArmorQuantumSuit;
import ic2.core.item.tool.ItemCropnalyzer;
import ic2.core.item.tool.ItemToolMeter;
import ic2.core.item.tool.ItemTreetap;
import ic2.core.item.tool.ItemTreetapElectric;
import ic2.core.util.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;

public interface IInfoProvider {

    IItemTransporter.IFilter READER = stack -> stack != null && stack.getItem() instanceof ItemToolMeter;
    IItemTransporter.IFilter ANALYZER = stack -> stack != null && stack.getItem() instanceof ItemCropnalyzer;
    IItemTransporter.IFilter TREETAP = stack -> stack != null && (stack.getItem() instanceof ItemTreetap || stack.getItem() instanceof ItemTreetapElectric);
    IItemTransporter.IFilter ALWAYS = itemStack -> true;
    IItemTransporter.IFilter OMNI = stack -> stack != null && stack.getItem() == WailaIC2Classic.PROBE;


    default IItemTransporter.IFilter getFilter() {
        return READER;
    }

    default boolean canHandle(EntityPlayer player) {
        return (hasHotbarItem(player, getFilter()) || hasProbe(player)) || player.capabilities.isCreativeMode;
    }

    default boolean hasProbe(EntityPlayer player) {
        ItemStack helmet = player.getCurrentArmor(3);
        boolean helmetProbe = false;
        if (helmet != null) {
            if (helmet.getItem() instanceof ItemArmorNanoSuit || helmet.getItem() instanceof ItemArmorQuantumSuit) {
                NBTTagCompound tag = StackUtil.getOrCreateNbtData(helmet);
                if (tag.hasKey(WailaIC2Classic.PROBE_ID)) {
                    helmetProbe = tag.getBoolean(WailaIC2Classic.PROBE_ID);
                }
            }
        }
        return helmetProbe || hasHotbarItem(player, OMNI);
    }

    default boolean hasHotbarItem(EntityPlayer player, IItemTransporter.IFilter filter) {
        if (player == null) {
            return false;
        } else {
            InventoryPlayer inventoryPlayer = player.inventory;
            for (int i = 0; i < 9; i++) {
                ItemStack stack = inventoryPlayer.getStackInSlot(i);
                if (filter.matches(stack)) {
                    return true;
                }
            }
            return false;
        }
    }

    default IChatComponent status(boolean status) {
        return status ? TextFormatter.GREEN.literal("" + true) : TextFormatter.RED.literal("" + false);
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
            text(helper, translate(TextFormatter.AQUA, "tooltip.item.eu_reader.cable_flow_in", Formatter.EU_FORMAT.format(averageIn)));
        }
    }

    /**
     * common: energyOut, packetOut
     * */

    default void addAveragesOut(IWailaHelper helper, EnergyContainer container) {
        int averageOut = container.getAverageOut();
        if (averageOut > 0) {
            text(helper, translate(TextFormatter.AQUA, "tooltip.item.eu_reader.cable_flow_out", Formatter.EU_FORMAT.format(averageOut)));
        }
    }

    default void addCableOut(IWailaHelper helper, EnergyContainer container) {
        int averageOut = container.getAverageOut();
        if (averageOut > 0) {
            text(helper, translate(TextFormatter.AQUA, "tooltip.item.eu_reader.cable_flow", Formatter.EU_FORMAT.format(averageOut)));
        }
    }

    void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player);

    default void bar(IWailaHelper helper, int current, int max, IChatComponent text, int color) {
        this.bar(helper, current, max, text, color, "0");
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

    default IChatComponent translate(String translatable) {
        return new ChatComponentTranslation(translatable);
    }

    default IChatComponent translate(String translatable, Object... args) {
        return new ChatComponentTranslation(translatable, args);
    }

    default IChatComponent literal(String translatable) {
        return new ChatComponentText(translatable);
    }

    default IChatComponent translate(TextFormatter formatter, String translatable) {
        return formatter.translate(translatable);
    }

    default IChatComponent translate(TextFormatter formatter, String translatable, Object... args) {
        return formatter.translate(translatable, args);
    }

    default IChatComponent literal(TextFormatter formatter, String translatable) {
        return formatter.literal(translatable);
    }

    default IChatComponent tier(int tier) {
        return translate("probe.energy.tier", getDisplayTier(tier));
    }

    default IChatComponent maxIn(int maxIn) {
        return translate("probe.energy.input.max", maxIn);
    }

    default IChatComponent usage(int usage) {
        return translate("probe.energy.usage", usage);
    }

    default void addStats(IWailaHelper helper, EntityPlayer player, IStatProvider stats) {
        if (player.isSneaking()) {
            text(helper, translate(TextFormatter.GREEN, "probe.energy.stats.info"), true);
            stats.addTooltips();
        } else {
            text(helper, translate(TextFormatter.AQUA, "probe.sneak.info"), true);
        }
    }

    interface IStatProvider {
        void addTooltips();
    }
}
