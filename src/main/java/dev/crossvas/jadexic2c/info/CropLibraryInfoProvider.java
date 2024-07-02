package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadePluginHandler;
import dev.crossvas.jadexic2c.base.interfaces.IHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergySink;
import ic2.core.block.base.tiles.BaseInventoryTileEntity;
import ic2.core.block.base.tiles.impls.BaseCropLibraryTileEntity;
import ic2.core.utils.helpers.StackUtil;
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

public enum CropLibraryInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "CropLibraryInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "CropLibraryInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseInventoryTileEntity tile) {
            if (tile instanceof BaseCropLibraryTileEntity lib) {
                if (lib instanceof IEnergySink sink) {
                    TextHelper.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(sink.getSinkTier()));
                    TextHelper.text(iTooltip, "ic2.probe.eu.max_in.name", EnergyNet.INSTANCE.getPowerFromTier(sink.getSinkTier()));
                    TextHelper.text(iTooltip, "ic2.probe.eu.usage.name", 1);
                }

                int cropCount = tag.getInt("cropCount");
                int statCount = tag.getInt("statCount");
                int sizeLimit = tag.getInt("sizeLimit");
                int typeLimit = tag.getInt("typeLimit");
                int statLimit = tag.getInt("statLimit");

                if (typeLimit != -1) {
                    TextHelper.text(iTooltip, "ic2.probe.crop_library.type.name", cropCount, typeLimit);
                    TextHelper.text(iTooltip, "ic2.probe.crop_library.stat.name", statCount, statLimit * typeLimit);
                    TextHelper.text(iTooltip, "ic2.probe.crop_library.size.name", sizeLimit);
                }

                ListTag itemsTagList = tag.getList("items", Tag.TAG_COMPOUND);
                List<ItemStack> stackList = new ArrayList<>();
                itemsTagList.forEach(stackTag -> {
                    CompoundTag itemTag = (CompoundTag) stackTag;
                    ItemStack stack = ItemStack.of(itemTag.getCompound("stack"));
                    stack.setCount(itemTag.getInt("count"));
                    stackList.add(stack);
                });
                PluginHelper.grid(iTooltip, "ic2.probe.crop_library.name", ChatFormatting.YELLOW, stackList);
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseInventoryTileEntity tile) {
            if (tile instanceof BaseCropLibraryTileEntity lib) {
                CompoundTag tag = new CompoundTag();
                tag.putInt("cropCount", lib.syncer.getCropCount());
                tag.putInt("statCount", lib.syncer.getStatCount());
                tag.putInt("sizeLimit", lib.storage.getSizeLimit());
                tag.putInt("typeLimit", lib.storage.getTypeLimit());
                tag.putInt("statLimit", lib.storage.getStatLimit());
                ListTag itemsList = new ListTag();
                for (ItemStack stack : StackUtil.copyNonEmpty(lib.storage.getTypes())) {
                    CompoundTag stackTag = new CompoundTag();
                    stackTag.put("stack", stack.save(new CompoundTag()));
                    stackTag.putInt("count", stack.getCount());
                    itemsList.add(stackTag);
                }
                if (!itemsList.isEmpty()) {
                    tag.put("items", itemsList);
                }
                compoundTag.put("CropLibraryInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadePluginHandler.EU_READER_INFO;
    }
}
