package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.JadeTags;
import dev.crossvas.jadexic2c.helpers.BarHelper;
import ic2.core.block.misc.TreeTapAndBucketBlock;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.utils.helpers.StackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class TreetapAndBucketInfo implements IBlockComponentProvider {

    public static final TreetapAndBucketInfo THIS = new TreetapAndBucketInfo();

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!StackUtil.hasHotbarItems(blockAccessor.getPlayer(), SpecialFilters.EU_READER)) {
            return;
        }

        if (blockAccessor.getBlock() instanceof TreeTapAndBucketBlock) {
            BlockState state = blockAccessor.getBlockState();
            int current = state.getValue(TreeTapAndBucketBlock.FILL_STAGE);
            if (current > 0) {
                BarHelper.bar(iTooltip, current, 5, Component.translatable("ic2.probe.progress.full.name", current, 5).withStyle(ChatFormatting.WHITE), -10996205);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeTags.INFO_RENDERER;
    }
}
