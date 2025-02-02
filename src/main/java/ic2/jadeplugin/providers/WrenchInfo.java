package ic2.jadeplugin.providers;

import ic2.api.items.readers.IWrenchTool;
import ic2.core.block.base.features.IWrenchableTile;
import ic2.core.block.base.features.multiblock.IStructureListener;
import ic2.core.platform.registries.IC2Items;
import ic2.jadeplugin.JadeTags;
import ic2.jadeplugin.helpers.TextFormatter;
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
                spacerY(iTooltip, 5);
                if (tile.isHarvestWrenchRequired(player)) {
                    iTooltip.add(wrenchIcon);
                    if (handHeldStack.getItem() instanceof IWrenchTool tool) {
                        int dropChance = Math.min(Mth.floor(tool.getActualLoss(handHeldStack, tile.getDropRate(player)) * 100.0), 100);
                        iTooltip.append(TextFormatter.WHITE.translate("ic2.probe.wrenchable.drop_chance.info", TextFormatter.formatPercentage(dropChance).literal(dropChance + "")));
                    } else {
                        iTooltip.append(TextFormatter.GOLD.translate("ic2.probe.wrenchable.info"));
                    }
                } else {
                    iTooltip.add(wrenchIcon);
                    iTooltip.append(TextFormatter.WHITE.translate("ic2.probe.wrenchable.drop_chance.info", TextFormatter.formatPercentage(100).literal(100 + "")));
                    iTooltip.append(TextFormatter.AQUA.translate("ic2.probe.wrenchable.optional.info"));
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

    public static void spacerY(ITooltip tooltip, int y) {
        tooltip.add(tooltip.getElementHelper().spacer(0, y));
    }
}
