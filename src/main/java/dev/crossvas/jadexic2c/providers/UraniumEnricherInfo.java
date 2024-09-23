package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import dev.crossvas.jadexic2c.utils.ColorUtils;
import dev.crossvas.jadexic2c.utils.SanityHelper;
import ic2.core.block.machine.high.TileEntityUraniumEnricher;
import ic2.core.item.reactor.uranTypes.IUranium;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class UraniumEnricherInfo implements IInfoProvider {

    public static final UraniumEnricherInfo THIS = new UraniumEnricherInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityUraniumEnricher) {
            TileEntityUraniumEnricher enricher = (TileEntityUraniumEnricher) blockEntity;
            text(helper, tier(enricher.getTier()));
            text(helper, maxIn(enricher.maxInput));
            IUranium type = enricher.getType();
            int cost = 0;
            if (type != null) {
                if (enricher.getSecondaryProgress() > 0 && enricher.getProgress() <= 0) {
                    cost = 100;
                } else if (enricher.getSecondaryProgress() > 0 && enricher.getProgress() > 0) {
                    cost = 100 + type.getIngridientCost();
                } else if (enricher.getProgress() > 0 && enricher.getSecondaryProgress() <= 0) {
                    cost = type.getIngridientCost();
                }
            }
            text(helper, usage(cost));
            if (enricher.getSecondaryProgress() > 0) {
                int color = type != null ? ColorUtils.rgb(type.getReEnrichedColor().getRed(), type.getReEnrichedColor().getGreen(), type.getReEnrichedColor().getBlue()) : ColorUtils.PROGRESS;
                bar(helper, (int) enricher.getSecondaryProgress(), 100, translatable("probe.progress.full.name", (int) enricher.getSecondaryProgress(), 100), color);
            }
            if (enricher.getProgress() > 0) {
                bar(helper, (int) (enricher.getProgress() / 20), 1000 / 20, translatable("probe.progress.full_misc.name", (int) enricher.getProgress() / 20, 1000 / 20).appendText("s"), ColorUtils.PROGRESS);
            }

            if (type != null && enricher.amount > 0) {
                bar(helper, enricher.amount, 1000, translatable("probe.uranium.type",
                        SanityHelper.toPascalCase(type.getIngridient().getDisplayName()), enricher.amount, 1000),
                        ColorUtils.rgb(type.getReEnrichedColor().getRed(), type.getReEnrichedColor().getGreen(), type.getReEnrichedColor().getBlue()));
            }
        }
    }
}
