package dev.crossvas.jadexic2c.info.expansions;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.BarHelper;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import ic2.core.block.base.tiles.impls.BaseExpansionTileEntity;
import ic2.core.block.machines.tiles.nv.MemoryExpansionTileEntity;
import ic2.core.inventory.filter.SpecialFilters;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.List;

public enum MemoryExpansionInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "MemoryExpansionInfo", SpecialFilters.EU_READER)) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "MemoryExpansionInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseExpansionTileEntity base) {
            if (base instanceof MemoryExpansionTileEntity) {
                ListTag craftsTagList = tag.getList("crafts", Tag.TAG_COMPOUND);
                List<ItemStack> firstStick = new ObjectArrayList<>();
                List<ItemStack> secondStick = new ObjectArrayList<>();
                List<ItemStack> common = new ObjectArrayList<>();
                craftsTagList.forEach(craftTag -> {
                    CompoundTag recipeTag = (CompoundTag) craftTag;
                    ItemStack stack = ItemStack.of(recipeTag.getCompound("recipeOutput"));
                    int slotIndex = recipeTag.getInt("slotIndex");
                    if (slotIndex < 9) {
                        firstStick.add(stack);
                    } else {
                        secondStick.add(stack);
                    }
                    common.add(stack);
                });

                boolean firstSlot = tag.getBoolean("firstSlot");
                boolean secondSlot = tag.getBoolean("secondSlot");
                if (firstSlot) {
                    BarHelper.bar(iTooltip, firstStick.size(), 9, Component.translatable("ic2.probe.memory_expansion.name", firstStick.size(), 9), -16733185);
                }
                if (secondSlot) {
                    BarHelper.bar(iTooltip, secondStick.size(), 9, Component.translatable("ic2.probe.memory_expansion.name", secondStick.size(), 9), -16733185);
                }
                PluginHelper.grid(iTooltip, "ic2.probe.memory_expansion.can_craft.name", ChatFormatting.YELLOW, common);
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseExpansionTileEntity base) {
            if (base instanceof MemoryExpansionTileEntity memory) {
                CompoundTag tag = new CompoundTag();
                ListTag craftsList = new ListTag();
                for (int i = 0; i < memory.crafting.getSlotCount(); i++) {
                    ItemStack recipeOutput = memory.crafting.getStackInSlot(i);
                    if (!recipeOutput.isEmpty()) {
                        CompoundTag recipeTag = new CompoundTag();
                        recipeTag.put("recipeOutput", recipeOutput.save(new CompoundTag()));
                        recipeTag.putInt("slotIndex", i);
                        craftsList.add(recipeTag);
                    }
                }
                if (!craftsList.isEmpty()) {
                    tag.put("crafts", craftsList);
                }
                tag.putBoolean("firstSlot", !memory.inventory.getStackInSlot(0).isEmpty());
                tag.putBoolean("secondSlot", !memory.inventory.getStackInSlot(1).isEmpty());
                compoundTag.put("MemoryExpansionInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.TOOLTIP_RENDERER;
    }
}
