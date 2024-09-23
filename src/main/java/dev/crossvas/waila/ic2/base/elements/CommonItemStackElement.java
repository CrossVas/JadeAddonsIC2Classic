package dev.crossvas.waila.ic2.base.elements;

import dev.crossvas.waila.ic2.WailaTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;

public class CommonItemStackElement extends CommonElement {

    ItemStack STACK;

    public CommonItemStackElement(ItemStack stack, Vec2 translation, String side) {
        super(translation, side);
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
        tag.putInt("translationX", (int) TRANSLATION.x);
        tag.putInt("translationY", (int) TRANSLATION.y);
        return tag;
    }

    public static CommonItemStackElement load(CompoundTag tag) {
        ItemStack stack = ItemStack.of(tag.getCompound("stack"));
        stack.setCount(tag.getInt("count"));
        Vec2 translation = new Vec2(tag.getInt("translationX"), tag.getInt("translationY"));
        String side = tag.getString("side");
        return new CommonItemStackElement(stack, translation, side);
    }

    @Override
    public String getTagId() {
        return WailaTags.JADE_ADDON_ITEM_TAG;
    }
}
