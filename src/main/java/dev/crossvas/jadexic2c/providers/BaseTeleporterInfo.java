package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
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
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseTeleporterTileEntity tp) {
            helper.defaultText("ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(tp.getTier()));
            helper.defaultText("ic2.probe.eu.max_in.name", EnergyNet.INSTANCE.getPowerFromTier(tp.getSinkTier()));
            Set<BaseTeleporterTileEntity.LocalTarget> targets = tp.getTargets();
            String name = tp.name;
            String networkID = tp.networkID;
            if (!targets.isEmpty()) {
                helper.defaultText(translate("gui.ic2.base_teleporter.name").append(": ").append(name));
                helper.defaultText(translate("gui.ic2.base_teleporter.network").append(": ").append(networkID));
                helper.paddingY(3);
                helper.text(translate("ic2.probe.base_teleporter.connections").withStyle(ChatFormatting.GOLD));
                for (BaseTeleporterTileEntity.LocalTarget target : targets) {
                    if (target.getPos() != tp.getPosition()) {
                        helper.defaultText(Component.literal(" - " + target.getName()));
                    }
                }
            }
        }
    }
}
