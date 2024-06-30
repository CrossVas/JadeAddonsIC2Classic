package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.core.block.storage.tiles.tank.PushingValveTileEntity;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.utils.helpers.Formatters;
import ic2.core.utils.helpers.StackUtil;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum PushingValveInfoProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!StackUtil.hasHotbarItems(blockAccessor.getPlayer(), SpecialFilters.EU_READER)) {
            return;
        }

        if (blockAccessor.getBlockEntity() instanceof PushingValveTileEntity) {
            TextHelper.text(iTooltip, "ic2.probe.pump.pressure", 100);
            TextHelper.text(iTooltip, "ic2.probe.pump.amount", Formatters.EU_FORMAT.format(2000L));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.TOOLTIP_RENDERER;
    }
}
