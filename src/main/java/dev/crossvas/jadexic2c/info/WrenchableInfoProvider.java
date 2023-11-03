package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.utils.Helpers;
import ic2.api.items.readers.IWrenchTool;
import ic2.core.block.base.features.IWrenchableTile;
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

public enum WrenchableInfoProvider implements IBlockComponentProvider {
    INSTANCE;

    public final ResourceLocation ID = new ResourceLocation("ic2", "wrenchable_info");

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        BlockEntity blockEntity = blockAccessor.getBlockEntity();
        Player player = blockAccessor.getPlayer();
        ItemStack handHeldStack = player.getMainHandItem();

        IElement wrenchIcon = iTooltip.getElementHelper().item(IC2Items.WRENCH.getDefaultInstance()).size(new Vec2(16, 16)).align(IElement.Align.LEFT).translate(new Vec2(-2, -5));
        if (blockEntity instanceof IWrenchableTile tile) {
            // drop rate with regular wrench
            double actualRate = ((IWrenchTool) IC2Items.WRENCH.asItem()).getActualLoss(IC2Items.WRENCH.getDefaultInstance(), tile.getDropRate(player));
            if (actualRate > 0) { // if it's actually wrenchable. Blame IWrenchableTile.
                Helpers.space_y(iTooltip, 10);
                iTooltip.add(wrenchIcon);
                if (handHeldStack.getItem() instanceof IWrenchTool tool) {
                    double dropChance = tool.getActualLoss(handHeldStack, tile.getDropRate(player));
                    Helpers.appendText(iTooltip, Component.literal(String.valueOf(Mth.floor(dropChance * 100.0))).append(" % ").append(Component.translatable("ic2.probe.wrenchable.drop_chance.info")).withStyle(ChatFormatting.GRAY));
                } else {
                    Helpers.appendText(iTooltip, Component.translatable("ic2.probe.wrenchable.info").withStyle(ChatFormatting.GRAY));
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
        return ID;
    }
}
