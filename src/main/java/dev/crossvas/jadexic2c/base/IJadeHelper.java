package dev.crossvas.jadexic2c.base;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IJadeHelper {

    void addItemElement(ItemStack stack, Component component);
    void addItemGridElement(List<ItemStack> stacks, Component component, ChatFormatting formatting);
    void addTextElement(Component text, ChatFormatting formatting, boolean append, boolean centered);
    void addEnergyElement(int currentEnergy, int maxEnergy, Component text);
    void addBarElement(int current, int max, Component text, int color);
    void addFluidElement(FluidStack stored, int maxCapacity);
    void addFluidGridElement(List<FluidStack> fluids, Component component, ChatFormatting formatting);
    void addPaddingElement(int x, int y);
    void transferData(CompoundTag serverData);
}
