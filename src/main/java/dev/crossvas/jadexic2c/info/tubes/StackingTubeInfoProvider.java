package dev.crossvas.jadexic2c.info.tubes;

import dev.crossvas.jadexic2c.IHelper;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.core.block.transport.item.tubes.StackingTubeTileEntity;
import ic2.core.utils.helpers.StackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
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

public class StackingTubeInfoProvider implements IHelper<BlockEntity> {

    public static StackingTubeInfoProvider INSTANCE = new StackingTubeInfoProvider();

    public StackingTubeInfoProvider() {}

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "StackingTubeInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "StackingTubeInfo");
        if (blockAccessor.getBlockEntity() instanceof StackingTubeTileEntity) {
            ListTag stackedItemsTagList = tag.getList("StackedItems", Tag.TAG_COMPOUND);
            List<ItemStack> cachedList = new ArrayList<>();
            stackedItemsTagList.forEach(stacked -> {
                CompoundTag stackedTag = (CompoundTag) stacked;
                ItemStack stack = ItemStack.of(stackedTag.getCompound("stacked"));
                stack.setCount(stackedTag.getInt("count"));
                cachedList.add(stack);
            });
            Helpers.grid(iTooltip, "ic2.probe.tube.cached", ChatFormatting.GOLD, cachedList);
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof StackingTubeTileEntity stacking) {
            CompoundTag tag = new CompoundTag();
            if (!stacking.cached.isEmpty()) {
                ListTag itemsList = new ListTag();
                NonNullList<ItemStack> list = NonNullList.create();
                for (StackingTubeTileEntity.StackingStack item : stacking.cached) {
                    list.add(StackUtil.copyWithSize(item.getStack(), item.getAmount()));
                }

                for (ItemStack stack : list) {
                    CompoundTag stackedTag = new CompoundTag();
                    stackedTag.put("stacked", stack.save(new CompoundTag()));
                    stackedTag.putInt("count", stack.getCount());
                    itemsList.add(stackedTag);
                }
                tag.put("StackedItems", itemsList);
            }
            compoundTag.put("StackingTubeInfo", tag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}
