package dev.crossvas.jadexic2c.info.tubes;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import ic2.api.tiles.tubes.TransportedItem;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.machines.recipes.ItemStackStrategy;
import ic2.core.block.transport.item.TubeTileEntity;
import ic2.core.utils.helpers.StackUtil;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenCustomHashMap;
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
import snownee.jade.api.TooltipPosition;
import snownee.jade.api.config.IPluginConfig;

import java.util.ArrayList;
import java.util.List;

public class BasicTubeInfoProvider implements IHelper<BlockEntity> {

    public static BasicTubeInfoProvider INSTANCE = new BasicTubeInfoProvider();

    public BasicTubeInfoProvider() {}

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "BasicTubeInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "BasicTubeInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity tile) {
            if (tile instanceof TubeTileEntity) {
                ListTag itemsList = tag.getList("TransportItems", Tag.TAG_COMPOUND);
                List<ItemStack> transportedList = new ArrayList<>();
                itemsList.forEach(listTag -> {
                    CompoundTag itemTag = (CompoundTag) listTag;
                    ItemStack stack = ItemStack.of(itemTag.getCompound("stack"));
                    stack.setCount(itemTag.getInt("count"));
                    transportedList.add(stack);
                });
                PluginHelper.spacerY(iTooltip, 3);
                PluginHelper.grid(iTooltip, "ic2.probe.tube.transported", ChatFormatting.GOLD, transportedList);
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof TubeTileEntity tube) {
            CompoundTag tag = new CompoundTag();
            ListTag itemsList = new ListTag();
            NonNullList<ItemStack> list = NonNullList.create();
            if (!tube.items.isEmpty()) {
                Object2IntLinkedOpenCustomHashMap<ItemStack> mapped = new Object2IntLinkedOpenCustomHashMap<>(ItemStackStrategy.INSTANCE);
                int i = 0;

                for(int m = tube.items.size(); i < m; ++i) {
                    TransportedItem item = tube.items.get(i);
                    mapped.addTo(StackUtil.copyWithSize(item.getServerStack(), 1), item.getServerStack().getCount());
                }

                mapped.forEach((K, V) -> list.add(StackUtil.copyWithSize(K, V)));

                for (ItemStack stack : list) {
                    CompoundTag stackTag = new CompoundTag();
                    stackTag.put("stack", stack.save(new CompoundTag()));
                    stackTag.putInt("count", stack.getCount());
                    itemsList.add(stackTag);
                }
                if (!itemsList.isEmpty()) {
                    tag.put("TransportItems", itemsList);
                }
            }
            compoundTag.put("BasicTubeInfo", tag);
        }
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.TAIL;
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}
