package dev.crossvas.jadexic2c.providers.expansions;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.core.block.machines.tiles.nv.UUMatterExpansionTileEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

public class UUMExpansionInfo implements IInfoProvider {

    public static final UUMExpansionInfo THIS = new UUMExpansionInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof UUMatterExpansionTileEntity uumExpansion) {
            int currentUUM = uumExpansion.uuMatter;
            int maxUUM = uumExpansion.maxUUMatter;
            if (currentUUM > 0) {
                bar(helper, currentUUM, maxUUM, Component.literal("UU: " + currentUUM / 1000 + " / " + maxUUM / 1000), -5829955);
            } else {
                text(helper, Component.translatable("ic2.probe.uum_expansion.missing").withStyle(ChatFormatting.RED));
            }
            List<ItemStack> providing = new ArrayList<>();
            for (int i = 0; i < uumExpansion.filter.getSlotCount(); i++) {
                ItemStack filter = uumExpansion.filter.getStackInSlot(i);
                if (!filter.isEmpty()) {
                    providing.add(filter);
                }
            }
            if (!providing.isEmpty()) {
                addGrid(helper, providing, Component.translatable("ic2.probe.uum.providing.name").withStyle(ChatFormatting.YELLOW));
            }
        }
    }
}
