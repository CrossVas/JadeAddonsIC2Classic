package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.IJadeHelper;
import dev.crossvas.jadexic2c.helpers.Formatter;
import ic2.api.tiles.teleporter.TeleporterTarget;
import ic2.core.IC2;
import ic2.core.block.machines.tiles.hv.TeleporterTileEntity;
import ic2.core.utils.helpers.Formatters;
import ic2.core.utils.helpers.SanityHelper;
import ic2.core.utils.helpers.TeleportUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TeleporterInfo extends EUReaderInfoProvider {

    public static final TeleporterInfo THIS = new TeleporterInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof TeleporterTileEntity teleport) {
            text(helper, Component.translatable("ic2.probe.teleporter.type", Component.translatable("ic2.probe.teleporter.type." + teleport.getProbeSendType().name().toLowerCase())).withStyle(ChatFormatting.WHITE));
            TeleporterTarget target = teleport.target;
            int cost = teleport.getBaseCost();
            long availableEnergy = teleport.getAvailableEnergy();
            if (target == null) {
                text(helper, "ic2.probe.teleporter.no_target");
            } else if (!teleport.hasMatchingType()) {
                text(helper, "ic2.probe.teleporter.invalid_target");
            } else {
                text(helper, "ic2.probe.teleporter.target", SanityHelper.toPascalCase(target.getDimension().location().getPath()), target.getTargetPosition().getX(), target.getTargetPosition().getY(), target.getTargetPosition().getZ());
            }
            if (cost > 0) {
                switch (teleport.getProbeSendType()) {
                    case ENTITY:
                        text(helper, "ic2.probe.teleporter.cost", Formatters.EU_FORMAT.format((long) TeleportUtil.getWeightOfEntity(player, IC2.CONFIG.teleporterKeepItems.get()) * cost * 5));
                        break;
                    case ENERGY:
                        text(helper, "ic2.probe.teleporter.cost", Formatters.EU_FORMAT.format(cost));
                        break;
                    case FLUID:
                        text(helper, "ic2.probe.teleporter.cost", Formatters.EU_FORMAT.format(cost));
                        text(helper, "ic2.probe.teleporter.capacity", Component.translatable("ic2.probe.teleporter.capacity.fluid", Formatter.formatNumber((double) (availableEnergy / (long) cost * 10L), 6)));
                        break;
                    case ITEM:
                        text(helper, "ic2.probe.teleporter.cost", Formatters.EU_FORMAT.format(cost));
                        text(helper, "ic2.probe.teleporter.capacity", Component.translatable("ic2.probe.teleporter.capacity.item", Formatter.formatNumber((double) (availableEnergy / (long) cost / 100L * 64L), 6)));
                        break;
                    case SPAWNER:
                        text(helper, "ic2.probe.teleporter.cost", Formatters.EU_FORMAT.format(cost * 25000L));
                }
            }
        }
    }
}
