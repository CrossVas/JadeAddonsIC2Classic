package dev.crossvas.lookingat.ic2c.waila.individual;

import dev.crossvas.lookingat.ic2c.LookingAtTags;
import dev.crossvas.lookingat.ic2c.helpers.PluginHelper;
import dev.crossvas.lookingat.ic2c.waila.WailaHelper;
import dev.crossvas.lookingat.ic2c.waila.elements.CustomWailaItemComponent;
import ic2.api.items.readers.IWrenchTool;
import ic2.core.block.base.features.IWrenchableTile;
import ic2.core.platform.registries.IC2Items;
import mcp.mobius.waila.api.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;

public class WrenchInfo implements IBlockComponentProvider{

    public static final WrenchInfo THIS = new WrenchInfo();

    @Override
    public void appendTail(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        if (!config.getBoolean(LookingAtTags.INFO_RENDERER)) {
            return;
        }
        BlockEntity blockEntity = accessor.getBlockEntity();
        Player player = accessor.getPlayer();
        ItemStack handHeldStack = player.getMainHandItem();
        boolean showInfo;
        ITooltipComponent wrenchIcon = new CustomWailaItemComponent(IC2Items.WRENCH.getDefaultInstance()).size(new Vec2(16, 16)).translate(new Vec2(0, -1));
        if (blockEntity instanceof IWrenchableTile tile) {
            double actualRate = ((IWrenchTool) IC2Items.WRENCH.asItem()).getActualLoss(IC2Items.WRENCH.getDefaultInstance(), tile.getDropRate(player));
            showInfo = actualRate > 0;
            if (showInfo) {
                WailaHelper.paddingY(tooltip, 1);
                if (tile.isHarvestWrenchRequired(player)) {
                    tooltip.addLine(wrenchIcon);
                    if (handHeldStack.getItem() instanceof IWrenchTool tool) {
                        int dropChance = Mth.floor(tool.getActualLoss(handHeldStack, tile.getDropRate(player)) * 100.0);
                        if (dropChance > 100) dropChance = 100;
                        tooltip.getLine(tooltip.getLineCount() - 1).with(Component.literal(dropChance + "% ").withStyle(PluginHelper.getTextColorFromDropChance(dropChance)).append(Component.translatable("ic2.probe.wrenchable.drop_chance.info").withStyle(ChatFormatting.GRAY)));
                    } else {
                        tooltip.getLine(tooltip.getLineCount() - 1).with(Component.translatable("ic2.probe.wrenchable.info"));
                    }
                } else {
                    WailaHelper.paddingY(tooltip, 3);
                    tooltip.getLine(tooltip.getLineCount() - 1).with(Component.literal(100 + "% ").withStyle(PluginHelper.getTextColorFromDropChance(100)).append(Component.translatable("ic2.probe.wrenchable.drop_chance.info").withStyle(ChatFormatting.GRAY)));
                    WailaHelper.paddingY(tooltip, 1);
                    tooltip.addLine(wrenchIcon);
                    tooltip.getLine(tooltip.getLineCount() - 1).with(Component.translatable("ic2.probe.wrenchable.optional.info").withStyle(ChatFormatting.AQUA));
                }
            }
        }
    }
}
