package dev.crossvas.jadexic2c.info.pipes;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.crossvas.jadexic2c.JadeIC2CPluginHandler;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.helpers.PluginHelper;
import dev.crossvas.jadexic2c.helpers.TextHelper;
import ic2.core.block.transport.fluid.graph.FluidNet;
import ic2.core.block.transport.fluid.tiles.PipeTileEntity;
import ic2.core.utils.collection.LongAverager;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import java.util.concurrent.TimeUnit;

public class BasicPipeInfoProvider implements IHelper<BlockEntity> {

    public static final BasicPipeInfoProvider INSTANCE = new BasicPipeInfoProvider();
    static final Cache<BlockPos, FluidContainer> CACHE;

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (!shouldAddInfo(blockAccessor, "BasicPipeInfo")) {
            return;
        }

        CompoundTag tag = getData(blockAccessor, "BasicPipeInfo");
        if (blockAccessor.getBlockEntity() instanceof PipeTileEntity) {
            ListTag fluidTagList = tag.getList("FluidStacks", Tag.TAG_COMPOUND);
            Object2IntOpenHashMap<Fluid> mappedFluid = new Object2IntOpenHashMap<>();
            TextHelper.text(iTooltip, Component.translatable("ic2.probe.pipe.transported").withStyle(ChatFormatting.GOLD));
            for (Tag fluidTag : fluidTagList) {
                CompoundTag fluidStackTag = (CompoundTag) fluidTag;
                Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidStackTag.getString("fluid")));
                int avr = fluidStackTag.getInt("average");
                if (avr > 0) {
                    mappedFluid.put(fluid, avr);
                }
            }

            PluginHelper.spacerY(iTooltip, 0);
            mappedFluid.object2IntEntrySet().forEach(entry -> {
                Fluid fluid = entry.getKey();
                int avr = entry.getIntValue();
                System.out.println(entry);
                TextHelper.text(iTooltip, ForgeRegistries.FLUIDS.getKey(fluid).toString());
                if (avr > 0) {

                    iTooltip.add(iTooltip.getElementHelper().fluid(new FluidStack(fluid, 1000)));
                    TextHelper.text(iTooltip, Component.literal("Average: " + avr));
                }
            });
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof PipeTileEntity pipe) {
            CompoundTag tag = new CompoundTag();
            FluidNet.TransportStats stats = FluidNet.INSTANCE.getStats(pipe);
            ListTag fluidTagList = new ListTag();
            for (Fluid fluid : stats.getTransfered().keySet()) {
                if (fluid != null) {
                    FluidContainer container = FluidContainer.getContainer(pipe, fluid);
                    CompoundTag fluidStackTag = new CompoundTag();
                    fluidStackTag.putString("fluid", ForgeRegistries.FLUIDS.getKey(fluid).toString());
                    fluidStackTag.putInt("average", container.getAverage(fluid));
                    fluidTagList.add(fluidStackTag);
                }
            }
            if (!fluidTagList.isEmpty()) {
                tag.put("FluidStacks", fluidTagList);
            }

            compoundTag.put("BasicPipeInfo", tag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadeIC2CPluginHandler.EU_READER_INFO;
    }

    static {
        CACHE = CacheBuilder.newBuilder().expireAfterAccess(1L, TimeUnit.SECONDS).maximumSize(128L).build();
    }

    public static class FluidContainer {

        Object2IntOpenHashMap<Fluid> mappedFluids = new Object2IntOpenHashMap<>();
        LongAverager fluidIn = new LongAverager(5);
        Fluid fluid;

        long lastTime;
        long lastIn;

        public FluidContainer(Fluid fluid) {
            this.fluid = fluid;
        }

        public static FluidContainer getContainer(PipeTileEntity tile, Fluid fluid) {
            FluidContainer result = CACHE.getIfPresent(tile.getPosition());
            if (result == null) {
                result = new FluidContainer(fluid);
                CACHE.put(tile.getPosition(), result);
            }

            result.tick(tile.getWorldObj().getGameTime(), tile, fluid);
            return result;
        }

        public void tick(long time, PipeTileEntity pipe, Fluid fluid) {
            FluidNet.TransportStats stat = FluidNet.INSTANCE.getStats(pipe);
            if (this.lastTime != 0L) {
                long diff = time - this.lastTime;
                if (diff <= 0L) {
                    return;
                }
                this.fluidIn.addEntry((stat.getTransfered().getLong(fluid) - this.lastIn) / diff);
            }
            this.lastTime = time;
            this.lastIn = stat.getTransfered().getLong(fluid);
            this.mappedFluids.put(fluid, (int) fluidIn.getAverage());
        }

        public int getAverage(Fluid fluid) {
            return this.mappedFluids.getInt(fluid);
        }
    }
}
