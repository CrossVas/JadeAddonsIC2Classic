package dev.crossvas.jadexic2c.info;

import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.api.energy.EnergyNet;
import ic2.core.block.transport.fluid.tiles.ElectricPipePumpTileEntity;
import ic2.core.block.transport.fluid.tiles.SimplePipePumpTileEntity;
import ic2.core.utils.helpers.Formatters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum PipePumpInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "PipePumpInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "PipePumpInfo");

        if (blockAccessor.getBlockEntity() instanceof SimplePipePumpTileEntity pump) {
            if (pump instanceof ElectricPipePumpTileEntity electricPump) {
                TextHelper.text(iTooltip, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(electricPump.getTier()));
                TextHelper.text(iTooltip, "ic2.probe.eu.max_in.name", EnergyNet.INSTANCE.getPowerFromTier(electricPump.getTier()));
            }
            TextHelper.text(iTooltip, "ic2.probe.pump.pressure", pump.getPressure());
            TextHelper.text(iTooltip, "ic2.probe.pump.amount", Formatters.EU_FORMAT.format((long)(tag.getInt("drainAmount") / 20)));
        }


    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof ElectricPipePumpTileEntity pump) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("drainAmount", pump.getDrainAmount());
            compoundTag.put("PipePumpInfo", tag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }
}
