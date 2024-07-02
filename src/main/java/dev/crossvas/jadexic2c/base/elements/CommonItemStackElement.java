package dev.crossvas.jadexic2c.base.elements;

import dev.crossvas.jadexic2c.JadeTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;

public class CommonItemStackElement extends CommonElement {

    ItemStack STACK;

    public CommonItemStackElement(ItemStack stack, Vec2 size, Vec2 translation, String side) {
        super(size, translation, side);
        this.STACK = stack;
    }

    public ItemStack getStack() {
        return this.STACK;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.put("stack", this.STACK.save(new CompoundTag()));
        tag.putInt("count", this.STACK.getCount());
        tag.putString("side", SIDE);
        tag.putInt("sizeX", (int) SIZE.x);
        tag.putInt("sizeY", (int) SIZE.y);
        tag.putInt("translationX", (int) TRANSLATION.x);
        tag.putInt("translationY", (int) TRANSLATION.y);
        return tag;
    }

    public static CommonItemStackElement load(CompoundTag tag) {
        ItemStack stack = ItemStack.of(tag.getCompound("stack"));
        stack.setCount(tag.getInt("count"));
        Vec2 size = new Vec2(tag.getInt("sizeX"), tag.getInt("sizeY"));
        Vec2 translation = new Vec2(tag.getInt("translationX"), tag.getInt("translationY"));
        String side = tag.getString("side");
        return new CommonItemStackElement(stack, size, translation, side);
    }

    @Override
    public String getTagId() {
        return JadeTags.JADE_ADDON_ITEM_TAG;
    }
}
