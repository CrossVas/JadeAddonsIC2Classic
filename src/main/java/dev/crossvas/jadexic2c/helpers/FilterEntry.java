package dev.crossvas.jadexic2c.helpers;

import ic2.api.util.DirectionList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class FilterEntry {

    ItemStack stack;
    DirectionList directions;

    public FilterEntry(ItemStack stack, DirectionList directions) {
        this.stack = stack;
        this.directions = directions;
    }

    public static FilterEntry read(CompoundTag data) {
        ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(data.getString("item"))));
        return stack.isEmpty() ? null : new FilterEntry(stack, DirectionList.ofNumber(data.getInt("sides")));
    }

    public CompoundTag write() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("item", ForgeRegistries.ITEMS.getKey(this.stack.getItem()).toString());
        nbt.putByte("sides", (byte)this.directions.getCode());
        return nbt;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public DirectionList getSides() {
        return this.directions;
    }
}
