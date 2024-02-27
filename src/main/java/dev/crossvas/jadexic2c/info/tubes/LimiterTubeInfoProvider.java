package dev.crossvas.jadexic2c.info.tubes;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.transport.item.tubes.LimiterTubeTileEntity;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.List;

public class LimiterTubeInfoProvider implements IHelper<BlockEntity> {

    public static final LimiterTubeInfoProvider INSTANCE = new LimiterTubeInfoProvider();

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "LimiterTubeInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "LimiterTubeInfo");
        if (blockAccessor.getBlockEntity() instanceof LimiterTubeTileEntity) {
            ListTag filteredItemsTagList = tag.getList("DyeStacks", Tag.TAG_COMPOUND);
            List<ItemStack> dyeStacks = new ObjectArrayList<>();
            filteredItemsTagList.forEach(rawTag -> {
                CompoundTag dyeTag = (CompoundTag) rawTag;
                ItemStack dyeStack = ItemStack.of(dyeTag.getCompound("stack"));
                dyeStacks.add(dyeStack);
            });
            PluginHelper.grid(iTooltip, "ic2.probe.tube.limiter.unblocked", ChatFormatting.GOLD, dyeStacks);
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseTileEntity baseTile) {
            if (baseTile instanceof LimiterTubeTileEntity limiter) {
                CompoundTag tag = new CompoundTag();
                ListTag stacksTagList = new ListTag();
                for (DyeColor color : limiter.usedColors) {
                    ItemStack stack = new ItemStack(DyeItem.byColor(color));
                    CompoundTag stackTag = new CompoundTag();
                    stackTag.put("stack", stack.save(new CompoundTag()));
                    stacksTagList.add(stackTag);
                }
                if (!stacksTagList.isEmpty()) {
                    tag.put("DyeStacks", stacksTagList);
                }
                compoundTag.put("LimiterTubeInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}
