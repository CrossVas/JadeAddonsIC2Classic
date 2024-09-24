package dev.crossvas.waila.ic2.providers;

import dev.crossvas.waila.ic2.base.interfaces.IInfoProvider;
import dev.crossvas.waila.ic2.base.interfaces.IWailaHelper;
import dev.crossvas.waila.ic2.utils.ColorUtils;
import dev.crossvas.waila.ic2.utils.SanityHelper;
import ic2.core.block.machine.tileentity.TileEntityUraniumEnricher;
import ic2.core.item.reactor.ItemReactorEnrichUranium;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class UraniumEnricherInfo implements IInfoProvider {

    public static final UraniumEnricherInfo THIS = new UraniumEnricherInfo();

    private  final ItemReactorEnrichUranium.UraniumType[] types = ItemReactorEnrichUranium.UraniumType.values();

    @Override
    public void addInfo(IWailaHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityUraniumEnricher) {
            TileEntityUraniumEnricher enricher = (TileEntityUraniumEnricher) blockEntity;
            text(helper, tier(enricher.getSinkTier()));
            text(helper, maxIn(enricher.maxInput));
            text(helper, usage(enricher.getEnergyUsage()));
            ItemStack fuel = enricher.inventory[1];
            byte itemType = -1;
            ItemReactorEnrichUranium.UraniumType type = null;

            if (fuel != null) {
                itemType = getTypeFromItem(fuel, enricher);
            }
            if (itemType != -1) {
                type = types[itemType];
            }

            if (enricher.itemProgress > 0) {
                int color = type != null ? ColorUtils.rgb(type.getColor().getRed(), type.getColor().getGreen(), type.getColor().getBlue()) : ColorUtils.PROGRESS;
                bar(helper, enricher.itemProgress, 100, translatable("probe.progress.full.name", enricher.itemProgress, 100), color);
            }
            if (enricher.uranProgress > 0) {
                bar(helper, enricher.uranProgress / 20, 1000 / 20, translatable("probe.progress.full_misc.name", enricher.uranProgress / 20, 1000 / 20).appendText("s"), ColorUtils.PROGRESS);
            }

            if (type != null && enricher.amount > 0) {
                bar(helper, enricher.amount, 1000, translatable("probe.uranium.type",
                        SanityHelper.toPascalCase(type.getItem().getDisplayName()), enricher.amount, 1000),
                        ColorUtils.rgb(type.getColor().getRed(), type.getColor().getGreen(), type.getColor().getBlue()));
            }
        }
    }

    private byte getTypeFromItem(ItemStack fuel, TileEntityUraniumEnricher enricher) {
        if (enricher.type != -1) {
            return enricher.type;
        } else {
            for (ItemReactorEnrichUranium.UraniumType uran : types) {
                if (fuel.isItemEqual(uran.getItem())) {
                    return (byte) uran.ordinal();
                }
            }
            return 0;
        }
    }
}
