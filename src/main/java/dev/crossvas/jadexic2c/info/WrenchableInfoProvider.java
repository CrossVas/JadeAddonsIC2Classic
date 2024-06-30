package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.api.items.readers.IWrenchTool;
import ic2.core.block.base.features.IWrenchableTile;
import ic2.core.block.base.features.multiblock.IStructureListener;
import ic2.core.platform.registries.IC2Items;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;

public enum WrenchableInfoProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        BlockEntity blockEntity = blockAccessor.getBlockEntity();
        Player player = blockAccessor.getPlayer();
        ItemStack handHeldStack = player.getMainHandItem();
        boolean showInfo;
        IElement wrenchIcon = iTooltip.getElementHelper().item(IC2Items.WRENCH.getDefaultInstance()).size(new Vec2(16, 16)).align(IElement.Align.LEFT).translate(new Vec2(-2, -5));
        if (blockEntity instanceof IWrenchableTile tile) {
            // drop rate with regular wrench
            double actualRate = ((IWrenchTool) IC2Items.WRENCH.asItem()).getActualLoss(IC2Items.WRENCH.getDefaultInstance(), tile.getDropRate(player));
            if (tile instanceof IStructureListener) {
                CompoundTag structureTag = blockAccessor.getServerData().getCompound("structureData");
                showInfo = !structureTag.getBoolean("isStructure") && actualRate > 0;
            } else {
                showInfo = actualRate > 0;
            }
            if (showInfo) {
                PluginHelper.spacerY(iTooltip, 3);
                if (tile.isHarvestWrenchRequired(player)) {
                    iTooltip.add(wrenchIcon);
                    if (handHeldStack.getItem() instanceof IWrenchTool tool) {
                        int dropChance = Mth.floor(tool.getActualLoss(handHeldStack, tile.getDropRate(player)) * 100.0);
                        if (dropChance > 100) dropChance = 100;
                        TextHelper.appendText(iTooltip, Component.literal(dropChance + "% ").withStyle(PluginHelper.getTextColorFromDropChance(dropChance)).append(Component.translatable("ic2.probe.wrenchable.drop_chance.info").withStyle(ChatFormatting.GRAY)));
                    } else {
                        TextHelper.appendText(iTooltip, Component.translatable("ic2.probe.wrenchable.info").withStyle(ChatFormatting.GRAY));
                    }
                } else {
                    TextHelper.appendText(iTooltip, Component.literal(100 + "% ").withStyle(PluginHelper.getTextColorFromDropChance(100)).append(Component.translatable("ic2.probe.wrenchable.drop_chance.info").withStyle(ChatFormatting.GRAY)));
                    IElement air = iTooltip.getElementHelper().item(ItemStack.EMPTY).size(new Vec2(16, 16)).align(IElement.Align.LEFT).translate(new Vec2(-2, -5));
                    PluginHelper.spacerY(iTooltip, 3);
                    iTooltip.add(wrenchIcon);
                    TextHelper.appendText(iTooltip, Component.translatable("ic2.probe.wrenchable.optional.info").withStyle(ChatFormatting.AQUA));
                }
            }
        }
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.TAIL;
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.WRENCHABLE;
    }
}
