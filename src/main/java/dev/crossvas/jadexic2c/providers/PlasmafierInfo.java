package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.core.block.machine.high.TileEntityPlasmafier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class PlasmafierInfo implements IInfoProvider {

    public static final PlasmafierInfo THIS = new PlasmafierInfo();

    @Override
    public void addInfo(IJadeHelper helper, TileEntity blockEntity, EntityPlayer player) {
        if (blockEntity instanceof TileEntityPlasmafier) {
            TileEntityPlasmafier plasmafier = (TileEntityPlasmafier) blockEntity;
            text(helper, tier(plasmafier.getTier()));
            text(helper, maxIn(plasmafier.maxInput));
            text(helper, usage(10240));

            int plasma = plasmafier.getPumpCharge();
            int maxPlasma = plasmafier.getMaxPumpCharge();
            int uuMatter = plasmafier.uuMatter;

            if (plasma > 0) {
                bar(helper, plasma, 10000, translatable("probe.plasma", plasma, maxPlasma), -5829955);
            }
            if (uuMatter > 0) {
                bar(helper, uuMatter, 100, translatable("probe.matter", uuMatter), -5829955);
            }
        }
    }
}
