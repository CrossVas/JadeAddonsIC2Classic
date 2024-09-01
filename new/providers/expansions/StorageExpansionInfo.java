package dev.crossvas.jadexic2c.providers.expansions;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.core.block.machines.tiles.nv.BufferStorageExpansionTileEntity;
import ic2.core.block.machines.tiles.nv.StorageExpansionTileEntity;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class StorageExpansionInfo implements IInfoProvider {

    public static final StorageExpansionInfo THIS = new StorageExpansionInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BufferStorageExpansionTileEntity buffer) {
            List<ItemStack> stacks = new ObjectArrayList<>();
            for (int i = 0; i < buffer.inventory.getSlotCount(); i++) {
                ItemStack stack = buffer.inventory.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    stacks.add(stack);
                }
            }
            if (!stacks.isEmpty()) {
                addGrid(helper, stacks, Component.translatable("ic2.probe.storage_expansion.slot.name").withStyle(ChatFormatting.YELLOW), 9);
            }
        }
        if (blockEntity instanceof StorageExpansionTileEntity storage) {
            List<ItemStack> stacks = new ObjectArrayList<>();
            for (int i = 0; i < storage.inventory.getSlotCount(); i++) {
                ItemStack stack = storage.inventory.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    stacks.add(stack);
                }
            }
            if (!stacks.isEmpty()) {
                addGrid(helper, stacks, Component.translatable("ic2.probe.storage_expansion.slot.name").withStyle(ChatFormatting.YELLOW));
            }
        }
    }
}
