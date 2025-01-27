package dev.crossvas.jadexic2c.base;

import dev.crossvas.jadexic2c.JadeTags;
import dev.crossvas.jadexic2c.base.elements.*;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeElementBuilder;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.EnergyContainer;
import dev.crossvas.jadexic2c.helpers.TextFormatter;
import ic2.core.utils.helpers.Formatters;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import snownee.jade.Jade;

import java.util.List;

public class JadeHelper implements IJadeHelper {

    public static final List<BlockEntity> TANK_REMOVAL = new ObjectArrayList<>();
    private final ListTag DATA = new ListTag();

    public static final String ADD_TAG = "add";
    public static final String APPEND_TAG = "append";

    @Override
    public void add(IJadeElementBuilder element) {
        CompoundTag addTag = new CompoundTag();
        CompoundTag elementTag = element.save(new CompoundTag());
        elementTag.putBoolean(ADD_TAG, true);
        addTag.put(element.getTagId(), elementTag);
        DATA.add(addTag);
    }

    @Override
    public void append(IJadeElementBuilder element) {
        CompoundTag elementTag = element.save(new CompoundTag());
        CompoundTag appendTag = new CompoundTag();
        elementTag.putBoolean(APPEND_TAG, true);
        appendTag.put(element.getTagId(), elementTag);
        DATA.add(appendTag);
    }

    @Override
    public void transferData(CompoundTag serverData) {
        if (!this.DATA.isEmpty()) {
            serverData.put(JadeTags.TAG_DATA, this.DATA);
        }
    }

    public void addTankInfo(BlockEntity blockEntity) {
        TANK_REMOVAL.add(blockEntity);
        if (blockEntity instanceof IFluidHandler fluidHandler) {
            loadTankData(fluidHandler);
        } else {
            blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(this::loadTankData);
        }
    }

    public void loadTankData(IFluidHandler fluidHandler) {
        for (int i = 0; i < fluidHandler.getTanks(); i++) {
            FluidStack fluid = fluidHandler.getFluidInTank(i);
            if (fluid.getAmount() > 0) {
                add(new CommonFluidBarElement(fluid, fluidHandler.getTankCapacity(i), false));
            }
        }
    }

    public void addAveragesFull(EnergyContainer container) {
        addAveragesIn(container, 3);
        addAveragesOut(container, 0);
    }

    /**
     * common: energyIn, packetIn
     * */

    public void addAveragesIn(EnergyContainer container) {
        addAveragesIn(container, 3);
    }

    public void addAveragesIn(EnergyContainer container, int padding) {
        int averageIn = container.getAverageIn();
        int packetsIn = container.getPacketsIn();
        MutableComponent full = getFullStatus(averageIn, packetsIn);
        if (averageIn > 0) {
            paddingY(padding);
            text(TextFormatter.AQUA.translate("info.energy.in", Formatters.EU_FORMAT.format(averageIn)));
            if (packetsIn <= 0) packetsIn = 1;
            text(full.append(TextFormatter.AQUA.translate("info.packet.in", Formatters.EU_FORMAT.format(packetsIn))));
        }
    }

    /**
     * common: energyOut, packetOut
     * */

    public void addAveragesOut(EnergyContainer container) {
        addAveragesOut(container, 3);
    }

    public void addAveragesOut(EnergyContainer container, int padding) {
        int averageOut = container.getAverageOut();
        int packetsOut = container.getPacketsOut();
        MutableComponent full = getFullStatus(averageOut, packetsOut);
        if (averageOut > 0) {
            paddingY(padding);
            text(TextFormatter.AQUA.translate("info.energy.out", Formatters.EU_FORMAT.format(averageOut)));
            if (packetsOut <= 0) packetsOut = 1;
            text(full.append(TextFormatter.AQUA.translate("info.packet.out", Formatters.EU_FORMAT.format(packetsOut))));
        }
    }

    public void addCableAverages(int energyFlow, int packetFlow) {
        if (energyFlow > 0) {
            MutableComponent full = getFullStatus(energyFlow, packetFlow);
            paddingY(3);
            text(TextFormatter.AQUA.translate("info.energy.flow", Formatters.EU_FORMAT.format(energyFlow)));
            if (packetFlow <= 0) packetFlow = 1;
            text(full.append(TextFormatter.AQUA.translate("info.packet.flow", Formatters.EU_FORMAT.format(packetFlow))));
        }
    }

    public void centered(Component component) {
        CommonTextElement element = new CommonTextElement(component, true);
        add(element);
    }

    public void defaultCentered(Component component) {
        CommonTextElement element = new CommonTextElement(component.copy(), true);
        add(element);
    }

    public void defaultCenteredText(String translatable, Object... args) {
        defaultCentered(Component.translatable(translatable, args));
    }

    public void text(Component text) {
        CommonTextElement element = new CommonTextElement(text, false);
        add(element);
    }

    public void appendText(Component text) {
        CommonTextElement element = new CommonTextElement(text, false);
        append(element);
    }

    public void defaultText(String translatable, Object... args) {
        text(TextFormatter.WHITE.translate(translatable, args));
    }

    public void appendDefaultText(String translatable, Object... args) {
        appendText(Component.translatable(translatable, args));
    }

    public void defaultText(Component component) {
        text(component);
    }

    public void appendDefaultText(Component component) {
        appendText(component);
    }

    public void bar(int current, int max, Component text, int color) {
        CommonBarElement element = new CommonBarElement(current, max, text, color);
        add(element);
    }

    public void fluid(FluidStack fluid, int capacity, boolean ignoreCapacity) {
        CommonFluidBarElement element = new CommonFluidBarElement(fluid, capacity, ignoreCapacity);
        add(element);
    }

    public void fluid(FluidStack fluid, int capacity) {
        fluid(fluid, capacity, false);
    }

    public void padding(int x, int y) {
        add(new CommonPaddingElement(x, y));
    }

    public void paddingX(int x) {
        add(new CommonPaddingElement(x, 0));
    }

    public void paddingY(int y) {
        add(new CommonPaddingElement(0, y));
    }

    public void appendPadding(int x, int y) {
        append(new CommonPaddingElement(x, y));
    }

    public void appendPaddingX(int x) {
        append(new CommonPaddingElement(x, 0));
    }

    public void appendPaddingY(int y) {
        append(new CommonPaddingElement(0, y));
    }

    public void item(ItemStack stack) {
        CommonItemStackElement stackElement = new CommonItemStackElement(stack, new Vec2(0, -5), "LEFT");
        add(stackElement);
    }

    public void appendItem(ItemStack stack) {
        CommonItemStackElement stackElement = new CommonItemStackElement(stack, new Vec2(0, -5), "LEFT");
        append(stackElement);
    }

    public void defaultItem(ItemStack stack) {
        CommonItemStackElement stackElement = new CommonItemStackElement(stack, new Vec2(0, 0), "LEFT");
        add(stackElement);
    }

    public void appendDefaultItem(ItemStack stack) {
        CommonItemStackElement stackElement = new CommonItemStackElement(stack, new Vec2(0, 0), "LEFT");
        append(stackElement);
    }

    public void item(ItemStack stack, Vec2 translation) {
        CommonItemStackElement stackElement = new CommonItemStackElement(stack, translation, "LEFT");
        add(stackElement);
    }

    public void appendItem(ItemStack stack, Vec2 translation) {
        CommonItemStackElement stackElement = new CommonItemStackElement(stack, translation, "LEFT");
        append(stackElement);
    }

    public void addGrid(List<ItemStack> stacks, Component component, int size) {
        int counter = 0;
        if (!stacks.isEmpty()) {
            text(component);
            paddingY(2);
            for (ItemStack stack : stacks) {
                if (counter < size + 1) {
                    appendDefaultItem(stack);
                    counter++;
                    if (counter == size) {
                        counter = 0;
                        padding(0, 0);
                    }
                }
            }
            paddingY(2);
        }
    }

    public void addGrid(List<ItemStack> stacks, Component component) {
        addGrid(stacks, component, 6);
    }

    public MutableComponent getFullStatus(int energy, int packet) {
        return (energy > 0 && packet <= 0) ? Component.literal("~").withStyle(ChatFormatting.GREEN) : Component.empty();
    }

    public void addStats(Player player, IInfoProvider.IStatProvider stats) {
        boolean isRequired = Jade.CONFIG.get().getPlugin().get(JadeTags.SNEAK_FOR_DETAILS);
        boolean condition = !isRequired || player.isCrouching();
        if (condition) {
            stats.addStats();
        } else {
            centered(TextFormatter.AQUA.translate("info.sneak.details"));
        }
    }
}
