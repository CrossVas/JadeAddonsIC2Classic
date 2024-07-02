package dev.crossvas.jadexic2c.info.expansions;

import dev.crossvas.jadexic2c.JadePluginHandler;
import dev.crossvas.jadexic2c.base.interfaces.IHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import ic2.core.block.base.tiles.impls.BaseExpansionTileEntity;
import ic2.core.block.machines.tiles.nv.BufferStorageExpansionTileEntity;
import ic2.core.block.machines.tiles.nv.StorageExpansionTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.ArrayList;
import java.util.List;

public enum StorageExpansionInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "StorageExpansionInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "StorageExpansionInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseExpansionTileEntity) {
            ListTag itemsTagList = tag.getList("storageList", Tag.TAG_COMPOUND);
            List<ItemStack> stackList = new ArrayList<>();
            itemsTagList.forEach(stackTag -> {
                CompoundTag itemTag = (CompoundTag) stackTag;
                ItemStack stack = ItemStack.of(itemTag.getCompound("stack"));
                stack.setCount(itemTag.getInt("count"));
                stackList.add(stack);
            });
            PluginHelper.grid(iTooltip, "ic2.probe.storage_expansion.slot.name", ChatFormatting.YELLOW, stackList);
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseExpansionTileEntity base) {
            CompoundTag tag = new CompoundTag();
            ListTag storageList = null;
            if (base instanceof BufferStorageExpansionTileEntity buffer) {
                storageList = new ListTag();
                for (int i = 0; i < buffer.inventory.getSlotCount(); i++) {
                    ItemStack filter = buffer.inventory.getStackInSlot(i);
                    if (!filter.isEmpty()) {
                        CompoundTag stackTag = new CompoundTag();
                        stackTag.put("stack", filter.save(new CompoundTag()));
                        stackTag.putInt("count", filter.getCount());
                        storageList.add(stackTag);
                    }
                }
            }
            if (base instanceof StorageExpansionTileEntity storage) {
                storageList = new ListTag();
                for (int i = 0; i < storage.inventory.getSlotCount(); i++) {
                    ItemStack filter = storage.inventory.getStackInSlot(i);
                    if (!filter.isEmpty()) {
                        CompoundTag stackTag = new CompoundTag();
                        stackTag.put("stack", filter.save(new CompoundTag()));
                        stackTag.putInt("count", filter.getCount());
                        storageList.add(stackTag);
                    }
                }
            }
            if (!storageList.isEmpty()) {
                tag.put("storageList", storageList);
            }
            compoundTag.put("StorageExpansionInfo", tag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadePluginHandler.EU_READER_INFO;
    }
}
