package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.machines.tiles.mv.BaseTeleporterTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Set;

public class BaseTeleporterInfo implements IInfoProvider {

    public static final BaseTeleporterInfo THIS = new BaseTeleporterInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseTeleporterTileEntity tp) {
            defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(tp.getTier()));
            defaultText(helper, "ic2.probe.eu.max_in.name", EnergyNet.INSTANCE.getPowerFromTier(tp.getSinkTier()));
            Set<BaseTeleporterTileEntity.LocalTarget> targets = tp.getTargets();
            String name = tp.name;
            String networkID = tp.networkID;
            if (!targets.isEmpty()) {
                defaultText(helper, Component.translatable("gui.ic2.base_teleporter.name").append(": ").append(name));
                defaultText(helper, Component.translatable("gui.ic2.base_teleporter.network").append(": ").append(networkID));
                paddingY(helper, 3);
                text(helper, Component.translatable("ic2.probe.base_teleporter.connections").withStyle(ChatFormatting.GOLD));
                for (BaseTeleporterTileEntity.LocalTarget target : targets) {
                    if (target.getPos() != tp.getPosition()) {
                        defaultText(helper, Component.literal(" - " + target.getName()));
                    }
                }
            }
        }
    }
}
