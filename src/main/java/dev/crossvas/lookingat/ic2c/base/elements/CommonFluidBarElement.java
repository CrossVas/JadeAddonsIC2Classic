package dev.crossvas.lookingat.ic2c.base.elements;

import dev.crossvas.lookingat.ic2c.LookingAtTags;
import dev.crossvas.lookingat.ic2c.helpers.PluginHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fluids.FluidStack;

public class CommonFluidBarElement extends CommonBarElement {

    FluidStack FLUID;
    int CAPACITY;
    boolean IGNORE_CAPACITY;

    public CommonFluidBarElement(FluidStack fluidStack, int capacity, boolean ignoreCapacity) {
        super(fluidStack.getAmount(), capacity, Component.empty(), PluginHelper.getColorForFluid(fluidStack));
        this.FLUID = fluidStack;
        this.CAPACITY = capacity;
        this.IGNORE_CAPACITY = ignoreCapacity;
    }

    public FluidStack getFluid() {
        return this.FLUID;
    }

    public int getCapacity() {
        return this.CAPACITY;
    }

    public boolean ignoreCapacity() {
        return this.IGNORE_CAPACITY;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.put("fluid", this.FLUID.writeToNBT(new CompoundTag()));
        tag.putInt("capacity", this.CAPACITY);
        tag.putBoolean("ignoreCapacity", this.IGNORE_CAPACITY);
        return tag;
    }

    public static CommonFluidBarElement load(CompoundTag tag) {
        FluidStack fluid = FluidStack.loadFluidStackFromNBT(tag.getCompound("fluid"));
        int capacity = tag.getInt("capacity");
        boolean ignoreCapacity = tag.getBoolean("ignoreCapacity");
        return new CommonFluidBarElement(fluid, capacity, ignoreCapacity);
    }

    @Override
    public String getTagId() {
        return LookingAtTags.LOOKING_AT_FLUID_TAG;
    }
}
