package dev.crossvas.jadexic2c.info.expansions;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.BarHelper;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.core.block.base.tiles.impls.BaseExpansionTileEntity;
import ic2.core.block.machines.tiles.nv.UUMatterExpansionTileEntity;
import ic2.core.inventory.filter.SpecialFilters;
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

import java.util.ArrayList;
import java.util.List;

public enum UUMExpansionInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if(!shouldAddInfo(blockAccessor, "UUMExpansionInfo", SpecialFilters.EU_READER)) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "UUMExpansionInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseExpansionTileEntity base) {
            if (base instanceof UUMatterExpansionTileEntity) {
                int currentUUM = tag.getInt("uumLvl");
                int maxUUM = tag.getInt("uumMaxLvl");
                if (currentUUM > 0) {
                    BarHelper.bar(iTooltip, currentUUM, maxUUM, "UU: " + currentUUM / 1000 + " / " + maxUUM / 1000, -5829955);
                } else {
                    TextHelper.text(iTooltip, Component.translatable("ic2.probe.uum_expansion.missing").withStyle(ChatFormatting.RED));
                }
                ListTag itemsTagList = tag.getList("items", Tag.TAG_COMPOUND);
                List<ItemStack> stackList = new ArrayList<>();
                itemsTagList.forEach(stackTag -> {
                    CompoundTag itemTag = (CompoundTag) stackTag;
                    ItemStack stack = ItemStack.of(itemTag.getCompound("stack"));
                    stack.setCount(itemTag.getInt("count"));
                    stackList.add(stack);
                });
                PluginHelper.grid(iTooltip, "ic2.probe.uum.providing.name", ChatFormatting.YELLOW, stackList);
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseExpansionTileEntity base) {
            if (base instanceof UUMatterExpansionTileEntity uum) {
                CompoundTag tag = new CompoundTag();
                tag.putInt("uumLvl", uum.uuMatter);
                tag.putInt("uumMaxLvl", uum.maxUUMatter);
                ListTag itemsList = new ListTag();
                for (int i = 0; i < uum.filter.getSlotCount(); i++) {
                    ItemStack filter = uum.filter.getStackInSlot(i);
                    if (!filter.isEmpty()) {
                        CompoundTag stackTag = new CompoundTag();
                        stackTag.put("stack", filter.save(new CompoundTag()));
                        stackTag.putInt("count", filter.getCount());
                        itemsList.add(stackTag);
                    }
                }
                if (!itemsList.isEmpty()) {
                    tag.put("items", itemsList);
                }
                compoundTag.put("UUMExpansionInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.TOOLTIP_RENDERER;
    }
}
