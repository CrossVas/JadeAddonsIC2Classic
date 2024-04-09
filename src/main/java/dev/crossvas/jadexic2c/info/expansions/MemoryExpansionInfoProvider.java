package dev.crossvas.jadexic2c.info.expansions;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.core.block.base.tiles.impls.BaseExpansionTileEntity;
import ic2.core.block.machines.tiles.nv.MemoryExpansionTileEntity;
import ic2.core.platform.registries.IC2Items;
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
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.ArrayList;
import java.util.List;

public enum MemoryExpansionInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "MemoryExpansionInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "MemoryExpansionInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseExpansionTileEntity base) {
            if (base instanceof MemoryExpansionTileEntity memory) {
                ListTag itemsTagList = tag.getList("items", Tag.TAG_COMPOUND);
                List<ItemStack> stackList = new ArrayList<>();
                itemsTagList.forEach(stackTag -> {
                    CompoundTag itemTag = (CompoundTag) stackTag;
                    ItemStack stack = ItemStack.of(itemTag.getCompound("stack"));
                    stackList.add(stack);
                });

                ListTag craftsTagList = tag.getList("crafts", Tag.TAG_COMPOUND);
                List<ItemStack> firstStick = new ObjectArrayList<>();
                List<ItemStack> secondStick = new ObjectArrayList<>();
                craftsTagList.forEach(craftTag -> {
                    CompoundTag recipeTag = (CompoundTag) craftTag;
                    ItemStack stack = ItemStack.of(recipeTag.getCompound("recipeOutput"));
                    int slotIndex = recipeTag.getInt("slotIndex");
                    if (slotIndex < 9) {
                        firstStick.add(stack);
                    } else {
                        secondStick.add(stack);
                    }
                });

                boolean firstSlot = tag.getBoolean("firstSlot");
                boolean secondSlot = tag.getBoolean("secondSlot");
                if (firstSlot) {
                    addInfo(iTooltip, firstStick);
                }
                if (secondSlot) {
                    addInfo(iTooltip, secondStick);
                }
            }
        }
    }

    public static void addInfo(ITooltip iTooltip, List<ItemStack> stacks) {
        TextHelper.text(iTooltip, Component.translatable("ic2.probe.memory_expansion.slot.name").withStyle(ChatFormatting.GOLD));
        iTooltip.append(iTooltip.getElementHelper().item(IC2Items.MEMORY_STICK.getDefaultInstance()).translate(new Vec2(0, -5)));
        iTooltip.append(iTooltip.getElementHelper().spacer(0, 5));
        TextHelper.appendText(iTooltip, Component.literal("→").withStyle(ChatFormatting.AQUA));
        if (!stacks.isEmpty()) {
            for (ItemStack recipeOutput : stacks) {
                iTooltip.append(iTooltip.getElementHelper().item(recipeOutput).translate(new Vec2(0, -5)));
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
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}
