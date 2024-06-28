package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.IInfoProvider;
import dev.crossvas.jadexic2c.base.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import ic2.api.items.readers.IWrenchTool;
import ic2.core.block.base.features.IWrenchableTile;
import ic2.core.block.base.features.multiblock.IStructureListener;
import ic2.core.inventory.filter.IFilter;
import ic2.core.platform.registries.IC2Items;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class WrenchInfo implements IInfoProvider {

    public static final WrenchInfo THIS = new WrenchInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        ItemStack handHeldStack = player.getMainHandItem();
        ItemStack wrenchStack = IC2Items.WRENCH.getDefaultInstance();
        boolean showInfo;
        if (blockEntity instanceof IWrenchableTile wrenchableTile) {
            double actualRate = ((IWrenchTool) IC2Items.WRENCH.asItem()).getActualLoss(wrenchStack, wrenchableTile.getDropRate(player));
            if (wrenchableTile instanceof IStructureListener) {
                CompoundTag structureTag = helper.getServerData().getCompound("StructureData");
                showInfo = !structureTag.getBoolean("isStructure") && actualRate > 0;
            } else {
                showInfo = actualRate > 0;
            }
            if (showInfo) {
                if (wrenchableTile.isHarvestWrenchRequired(player)) {
                    if (handHeldStack.getItem() instanceof IWrenchTool tool) {
                        int dropChance = Mth.floor(tool.getActualLoss(handHeldStack, wrenchableTile.getDropRate(player)) * 100.0);
                        if (dropChance > 100) dropChance = 100;
                        helper.addItemElement(wrenchStack, Component.literal(dropChance + "% ").withStyle(PluginHelper.getTextColorFromDropChance(dropChance)).append(Component.translatable("ic2.probe.wrenchable.drop_chance.info").withStyle(ChatFormatting.GRAY)));
                    } else {
                        helper.addItemElement(wrenchStack, Component.translatable("ic2.probe.wrenchable.info").withStyle(ChatFormatting.GRAY));
                    }
                } else {
                    helper.addItemElement(wrenchStack, Component.literal(100 + "% ").withStyle(PluginHelper.getTextColorFromDropChance(100)).append(Component.translatable("ic2.probe.wrenchable.drop_chance.info").withStyle(ChatFormatting.GRAY)).append(Component.translatable("ic2.probe.wrenchable.optional.info").withStyle(ChatFormatting.AQUA)));
                }
            }
        }
    }

    @Override
    public IFilter getFilter() {
        return ALWAYS;
    }
}
