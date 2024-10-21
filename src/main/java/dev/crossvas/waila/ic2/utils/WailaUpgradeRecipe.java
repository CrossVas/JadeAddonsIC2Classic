package dev.crossvas.waila.ic2.utils;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.AdvRecipe;
import ic2.core.AdvShapelessRecipe;
import ic2.core.item.armor.ItemArmorNanoSuit;
import ic2.core.item.armor.ItemArmorQuantumSuit;
import ic2.core.util.StackUtil;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.Iterator;

public class WailaUpgradeRecipe extends AdvShapelessRecipe {

    public WailaUpgradeRecipe(ItemStack output, Object... args) {
        super(output, args);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting crafting) {
        ArrayList inputs = new ArrayList(this.input);
        int totalCharge = 0;
        ItemStack input = null;

        for(int i = 0; i < crafting.getSizeInventory(); ++i) {
            ItemStack stack = crafting.getStackInSlot(i);
            if (stack != null) {
                boolean inRecipe = false;
                Iterator inputsIterator = inputs.iterator();

                while(inputsIterator.hasNext()) {
                    boolean match = false;
                    Object next = inputsIterator.next();
                    if (next instanceof ItemStack) {
                        ItemStack recipeInput = (ItemStack)next;
                        match = AdvRecipe.ItemsMatch(stack, recipeInput);
                        input = recipeInput;
                    } else if (next instanceof ArrayList) {
                        match = AdvRecipe.ItemsMatch(stack, (ArrayList)next);
                    }

                    if (match) {
                        inRecipe = true;
                        inputs.remove(next);
                        break;
                    }
                }

                if (stack.getItem() instanceof IElectricItem) {
                    totalCharge = (int)((double)totalCharge + ElectricItem.manager.getCharge(stack));
                }

                if (!inRecipe) {
                    return null;
                }
            }
        }

        if (!inputs.isEmpty()) {
            return null;
        } else {
            ItemStack out = this.output.copy();
            out = copyTag(out, input);
            if (out.getItem() instanceof IElectricItem && totalCharge > 0) {
                ElectricItem.manager.charge(out, totalCharge, Integer.MAX_VALUE, true, false);
            }

            return out;
        }
    }

    public static ItemStack copyTag(ItemStack container, ItemStack other) {
        NBTTagCompound tag = StackUtil.getOrCreateNbtData(other);
        System.out.println(StackUtil.getOrCreateNbtData(other).toString());
        if (other != null && !tag.hasNoTags()) {
            container.setTagCompound(tag);
        }
        return container;
    }
}
