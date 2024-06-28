package dev.crossvas.jadexic2c;

import dev.crossvas.jadexic2c.base.IJadeHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class JadeHelper implements IJadeHelper {

    private final ListTag DATA = new ListTag();

    public CompoundTag SERVER_DATA;

    @Override
    public void addItemElement(ItemStack stack, Component component) {
        CompoundTag stackData = new CompoundTag();
        CompoundTag stackTag = stack.save(new CompoundTag());
        stackTag.putString("stackText", Component.Serializer.toJson(component));
        stackData.put(JadeTags.TAG_ITEM, stackTag);
        DATA.add(stackData);
    }

    @Override
    public void addItemGridElement(List<ItemStack> stacks, Component component, ChatFormatting formatting) {
        CompoundTag stackListTag = new CompoundTag();
        ListTag stackList = new ListTag();
        for (ItemStack stack : stacks) {
            CompoundTag stackTag = new CompoundTag();
            stackTag.put("stack", stack.save(new CompoundTag()));
            stackTag.putInt("count", stack.getCount());
            stackList.add(stackTag);
        }
        if (!stackList.isEmpty()) {
            stackListTag.put(JadeTags.TAG_INVENTORY, stackList);
        }
        stackListTag.putString("stacksText", Component.Serializer.toJson(component));
        stackListTag.putInt("stackTextFormat", formatting.getId());
        DATA.add(stackListTag);
    }

    @Override
    public void addTextElement(Component text, ChatFormatting formatting, boolean append, boolean centered) {
        CompoundTag tag = new CompoundTag();
        tag.putString(JadeTags.TAG_TEXT, Component.Serializer.toJson(text));
        tag.putInt("formatting", formatting.getId());
        tag.putBoolean("append", append);
        tag.putBoolean("center", centered);
        DATA.add(tag);
    }

    @Override
    public void addEnergyElement(int currentEnergy, int maxEnergy, Component text) {
        CompoundTag energyData = new CompoundTag();
        energyData.putInt(JadeTags.TAG_ENERGY, currentEnergy);
        energyData.putInt(JadeTags.TAG_MAX, maxEnergy);
        energyData.putString("energyText", Component.Serializer.toJson(text));
        DATA.add(energyData);
    }

    @Override
    public void addBarElement(int current, int max, Component text, int color) {
        CompoundTag barTag = new CompoundTag();
        barTag.putInt(JadeTags.TAG_BAR, current);
        barTag.putInt(JadeTags.TAG_MAX, max);
        barTag.putString("barText", Component.Serializer.toJson(text));
        barTag.putInt(JadeTags.TAG_BAR_COLOR, color);
        DATA.add(barTag);
    }

    @Override
    public void addFluidElement(FluidStack stored, int maxCapacity) {
        CompoundTag fluidData = new CompoundTag();
        fluidData.put(JadeTags.TAG_FLUID, stored.writeToNBT(new CompoundTag()));
        fluidData.putInt(JadeTags.TAG_MAX, maxCapacity);
//        fluidData.putString(TagRefs.TAG_TEXT, Component.Serializer.toJson(text));
        DATA.add(fluidData);
    }

    @Override
    public void addFluidGridElement(List<FluidStack> fluids, Component component, ChatFormatting formatting) {
        ListTag fluidList = new ListTag();
        CompoundTag stackListTag = new CompoundTag();
        for (FluidStack stack : fluids) {
            CompoundTag stackTag = new CompoundTag();
            stackTag.put("fluid", stack.writeToNBT(new CompoundTag()));
            fluidList.add(stackTag);
        }
        if (!fluidList.isEmpty()) {
            stackListTag.put(JadeTags.TAG_INVENTORY_FLUID, fluidList);
        }
        stackListTag.putString("fluidsText", Component.Serializer.toJson(component));
        stackListTag.putInt("fluidsTextFormat", formatting.getId());
        DATA.add(stackListTag);
    }

    @Override
    public void addPaddingElement(int x, int y) {
        CompoundTag paddingTag = new CompoundTag();
        paddingTag.putInt(JadeTags.TAG_PADDING, x);
        paddingTag.putInt(JadeTags.TAG_PADDING_Y, y);
        DATA.add(paddingTag);
    }

    @Override
    public void transferData(CompoundTag serverData) {
        if (!this.DATA.isEmpty()) {
            serverData.put(JadeTags.TAG_DATA, this.DATA);
        }
    }
}
