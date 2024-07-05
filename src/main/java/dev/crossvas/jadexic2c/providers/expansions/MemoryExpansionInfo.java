package dev.crossvas.jadexic2c.providers.expansions;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.core.block.machines.tiles.nv.MemoryExpansionTileEntity;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class MemoryExpansionInfo implements IInfoProvider {

    public static final MemoryExpansionInfo THIS = new MemoryExpansionInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof MemoryExpansionTileEntity memoryExpansion) {
            List<ItemStack> firstStick = new ObjectArrayList<>();
            List<ItemStack> secondStick = new ObjectArrayList<>();
            List<ItemStack> common = new ObjectArrayList<>();
            int j = memoryExpansion.crafting.getSlotCount();
            for (int i = 0; i < j; i++) {
                ItemStack recipeOutput = memoryExpansion.crafting.getStackInSlot(i);
                if (!recipeOutput.isEmpty()) {
                    if (i < 9) {
                        firstStick.add(recipeOutput);
                    } else {
                        secondStick.add(recipeOutput);
                    }
                    common.add(recipeOutput);
                }
            }
            boolean firstSlot = !memoryExpansion.inventory.getStackInSlot(0).isEmpty();
            boolean secondSlot = !memoryExpansion.inventory.getStackInSlot(1).isEmpty();
            if (firstSlot) {
                bar(helper, firstStick.size(), 9, Component.translatable("ic2.probe.memory_expansion.name", firstStick.size(), 9), -16733185);
            }
            if (secondSlot) {
                bar(helper, secondStick.size(), 9, Component.translatable("ic2.probe.memory_expansion.name", secondStick.size(), 9), -16733185);
            }
            addGrid(helper, common, Component.translatable("ic2.probe.memory_expansion.can_craft.name").withStyle(ChatFormatting.YELLOW));
        }
    }
}
