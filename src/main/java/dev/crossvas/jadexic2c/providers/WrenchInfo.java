package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.JadeTags;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import ic2.api.items.readers.IWrenchTool;
import ic2.core.block.base.features.IWrenchableTile;
import ic2.core.block.base.features.multiblock.IStructureListener;
import ic2.core.platform.registries.IC2Items;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.TooltipPosition;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;

public class WrenchInfo implements IBlockComponentProvider {

    public static final WrenchInfo THIS = new WrenchInfo();

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!iPluginConfig.get(JadeTags.INFO_RENDERER)) {
            return;
        }
        BlockEntity blockEntity = blockAccessor.getBlockEntity();
        Player player = blockAccessor.getPlayer();
        ItemStack handHeldStack = player.getMainHandItem();
        boolean showInfo;
        IElement wrenchIcon = iTooltip.getElementHelper().item(IC2Items.WRENCH.getDefaultInstance()).size(new Vec2(16, 16)).align(IElement.Align.LEFT).translate(new Vec2(-2, -5));
        if (blockEntity instanceof IWrenchableTile tile) {
            // drop rate with regular wrench
            double actualRate = ((IWrenchTool) IC2Items.WRENCH.asItem()).getActualLoss(IC2Items.WRENCH.getDefaultInstance(), tile.getDropRate(player));
            if (tile instanceof IStructureListener) {
                boolean structureTag = blockAccessor.getServerData().contains(JadeTags.TAG_STRUCTURE);
                showInfo = !structureTag && actualRate > 0;
            } else {
                showInfo = actualRate > 0;
            }
            if (showInfo) {
                PluginHelper.spacerY(iTooltip, 5);
                if (tile.isHarvestWrenchRequired(player)) {
                    iTooltip.add(wrenchIcon);
                    if (handHeldStack.getItem() instanceof IWrenchTool tool) {
                        int dropChance = Mth.floor(tool.getActualLoss(handHeldStack, tile.getDropRate(player)) * 100.0);
                        if (dropChance > 100) dropChance = 100;
                        iTooltip.append(iTooltip.getElementHelper().text(Component.literal(dropChance + "% ").withStyle(PluginHelper.getTextColorFromDropChance(dropChance)).append(Component.translatable("ic2.probe.wrenchable.drop_chance.info").withStyle(ChatFormatting.GRAY))));
                    } else {
                        iTooltip.append(Component.translatable("ic2.probe.wrenchable.info").withStyle(ChatFormatting.GRAY));
                    }
                } else {
                    PluginHelper.spacerY(iTooltip, 5);
                    iTooltip.append(Component.literal(100 + "% ").withStyle(PluginHelper.getTextColorFromDropChance(100)).append(Component.translatable("ic2.probe.wrenchable.drop_chance.info").withStyle(ChatFormatting.GRAY)));
                    PluginHelper.spacerY(iTooltip, 5);
                    iTooltip.add(wrenchIcon);
                    iTooltip.append(Component.translatable("ic2.probe.wrenchable.optional.info").withStyle(ChatFormatting.AQUA));
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
        return JadeTags.WRENCHABLE;
    }
}
