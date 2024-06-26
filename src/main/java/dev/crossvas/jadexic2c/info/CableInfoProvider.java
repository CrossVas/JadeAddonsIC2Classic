package dev.crossvas.jadexic2c.info;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import dev.crossvas.jadexic2c.info.removals.ModNameRender;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.PacketStats;
import ic2.api.energy.TransferStats;
import ic2.api.energy.tile.IEnergyTile;
import ic2.core.block.base.tiles.BaseTileEntity;
import ic2.core.block.cables.CableBlock;
import ic2.core.block.cables.CableTileEntity;
import ic2.core.inventory.filter.SpecialFilters;
import ic2.core.utils.collection.LongAverager;
import ic2.core.utils.helpers.Formatters;
import ic2.core.utils.helpers.StackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.Identifiers;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElement;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.impl.ui.TextElement;
import snownee.jade.util.ModIdentification;

import java.util.List;
import java.util.concurrent.TimeUnit;

public enum CableInfoProvider implements IHelper<BlockEntity> {
    INSTANCE;

    static final Cache<BlockPos, EnergyContainer> CACHE;

    @Override
    public @Nullable IElement getIcon(BlockAccessor blockAccessor, IPluginConfig config, IElement currentIcon) {
        ItemStack icon = ItemStack.EMPTY;
        Block block = blockAccessor.getBlock();
        currentIcon.message(Component.literal("a"));
        if (block instanceof CableBlock cableBlock) {
            BlockState state = blockAccessor.getBlockState();
            boolean foamed = state.getValue(CableBlock.FOAMED) == 2;
            if (foamed) {
                icon = cableBlock.getCloneItemStack(state, blockAccessor.getHitResult(), blockAccessor.getLevel(), blockAccessor.getPosition(), blockAccessor.getPlayer());
            }
        }
        return IElementHelper.get().item(icon);
    }

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (blockAccessor.getBlock() instanceof CableBlock cableBlock) {
            BlockState state = blockAccessor.getBlockState();
            boolean foamed = state.getValue(CableBlock.FOAMED) == 2;
            if (foamed) {
                ItemStack fakeStack = cableBlock.getCloneItemStack(state, blockAccessor.getHitResult(), blockAccessor.getLevel(), blockAccessor.getPosition(), blockAccessor.getPlayer());
                iTooltip.remove(Identifiers.CORE_MOD_NAME);
                iTooltip.remove(Identifiers.CORE_OBJECT_NAME);
                iTooltip.remove(ModNameRender.RELOCATE);
                Component fakeNameComponent = fakeStack.getHoverName().copy().withStyle(ChatFormatting.WHITE);
                iTooltip.add(new TextElement(fakeNameComponent).size(new Vec2(Minecraft.getInstance().font.width(fakeNameComponent.getString()) + 15, Minecraft.getInstance().font.lineHeight)));
                String fakeModName = ModIdentification.getModName(fakeStack);
                String fakeModNameFormatted = String.format(iPluginConfig.getWailaConfig().getFormatting().getModName(), fakeModName);
                iTooltip.add(Component.literal(fakeModNameFormatted));
            }
        }

        if (!shouldAddInfo(blockAccessor, "CableInfo", SpecialFilters.EU_READER)) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "CableInfo");
        if (blockAccessor.getBlockEntity() instanceof BaseTileEntity tile) {
            if (tile instanceof CableTileEntity cable) {
                TextHelper.text(iTooltip, "tooltip.item.ic2.eu_reader.cable_limit", cable.getConductorBreakdownEnergy() - 1);
                TextHelper.text(iTooltip, "tooltip.item.ic2.eu_reader.cable_loss", Formatters.CABLE_LOSS_FORMAT.format(cable.getConductionLoss()));

                int averageOut = tag.getInt("averageOut");
                int averageOutPacket = tag.getInt("packetOut");

                if (averageOut > 0) {
                    PluginHelper.spacerY(iTooltip, 3);
                    TextHelper.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.cable_flow", Formatters.EU_FORMAT.format(averageOut)).withStyle(ChatFormatting.AQUA));
                    TextHelper.text(iTooltip, Component.translatable("tooltip.item.ic2.eu_reader.packet_flow", Formatters.EU_FORMAT.format(averageOutPacket)).withStyle(ChatFormatting.AQUA));
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof BaseTileEntity tile) {
            if (tile instanceof CableTileEntity cable) {
                CompoundTag tag = new CompoundTag();
                EnergyContainer result = getContainer(cable);
                if (result.getAverageOut() > 0) {
                    tag.putInt("averageOut", result.getAverageOut());
                    tag.putInt("packetOut", result.getPacketsOut());
                }
                compoundTag.put("CableInfo", tag);
            }
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.TOOLTIP_RENDERER;
    }

    public static EnergyContainer getContainer(IEnergyTile tile) {
        EnergyContainer result = CACHE.getIfPresent(tile.getPosition());
        if (result == null) {
            result = new EnergyContainer();
            CACHE.put(tile.getPosition(), result);
        }

        result.tick(tile.getWorldObj().getGameTime(), EnergyNet.INSTANCE.getStats(tile), EnergyNet.INSTANCE.getPacketStats(tile));
        return result;
    }

    static {
        CACHE = CacheBuilder.newBuilder().expireAfterAccess(1L, TimeUnit.SECONDS).maximumSize(128L).build();
    }

    /**
     * Straight copy-paste of {@link ic2.probeplugin.info.transport.CableProvider.EnergyContainer}
     * © IC2Classic Dev
     */

    public static class EnergyContainer {
        LongAverager energyIn = new LongAverager(5);
        LongAverager energyOut = new LongAverager(5);
        LongAverager packetsIn = new LongAverager(5);
        LongAverager packetsOut = new LongAverager(5);
        long lastTime;
        long lastIn;
        long lastOut;
        long lastPacketsIn;
        long lastPacketsOut;

        public EnergyContainer() {
        }

        public void tick(long time, TransferStats stat, List<PacketStats> stats) {
            if (this.lastTime == 0L) {
                this.lastTime = time;
                this.lastIn = stat.getEnergyIn();
                this.lastOut = stat.getEnergyOut();
                this.lastPacketsIn = this.countPackets(stats, false);
                this.lastPacketsOut = this.countPackets(stats, true);
            } else {
                long diff = time - this.lastTime;
                if (diff <= 0L) {
                    return;
                }

                long in = this.countPackets(stats, false);
                long out = this.countPackets(stats, true);
                this.energyIn.addEntry((stat.getEnergyIn() - this.lastIn) / diff);
                this.energyOut.addEntry((stat.getEnergyOut() - this.lastOut) / diff);
                this.packetsIn.addEntry((in - this.lastPacketsIn) / diff);
                this.packetsOut.addEntry((out - this.lastPacketsOut) / diff);
                this.lastTime = time;
                this.lastIn = stat.getEnergyIn();
                this.lastOut = stat.getEnergyOut();
                this.lastPacketsIn = in;
                this.lastPacketsOut = out;
            }

        }

        private long countPackets(List<PacketStats> stats, boolean in) {
            long result = 0L;
            int i = 0;

            for (int m = stats.size(); i < m; ++i) {
                PacketStats stat = (PacketStats) stats.get(i);
                if (stat.isAccepting() != in) {
                    result += stat.getPackets();
                }
            }

            return result;
        }

        public int getPacketsIn() {
            return (int) this.packetsIn.getAverage();
        }

        public int getPacketsOut() {
            return (int) this.packetsOut.getAverage();
        }

        public int getAverageIn() {
            return (int) this.energyIn.getAverage();
        }

        public int getAverageOut() {
            return (int) this.energyOut.getAverage();
        }
    }
}
