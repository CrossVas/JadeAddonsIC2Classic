package dev.crossvas.jadexic2c.info.pipes;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.crossvas.jadexic2c.JadePluginHandler;
import dev.crossvas.jadexic2c.elements.SpecialBoxStyle;
import dev.crossvas.jadexic2c.helpers.IHelper;
import dev.crossvas.jadexic2c.info.removals.TankRender;
import ic2.core.block.transport.fluid.graph.FluidNet;
import ic2.core.block.transport.fluid.graph.IFluidPipe;
import ic2.core.utils.collection.LongAverager;
import ic2.core.utils.math.ColorUtils;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.IElementHelper;
import snownee.jade.api.ui.IProgressStyle;

import java.util.Map;
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
        IElementHelper helper = iTooltip.getElementHelper();
        if (blockAccessor.getBlockEntity() instanceof IFluidPipe) {
            TankRender.TANK_REMOVAL.add(blockAccessor.getBlock());
            ListTag fluidTagList = tag.getList("FluidStacks", Tag.TAG_COMPOUND);
            for (Tag fluidTag : fluidTagList) {
                CompoundTag fluidStackTag = (CompoundTag) fluidTag;
                Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidStackTag.getString("fluid")));
                int avr = fluidStackTag.getInt("average");
                if (avr > 0 && fluid != null) {
                    FluidStack fluidStack = new FluidStack(fluid, 1000);
                    IProgressStyle tankStyle = helper.progressStyle().overlay(helper.fluid(fluidStack));
                    iTooltip.add(helper.progress(1f, fluidStack.getDisplayName().copy().withStyle(ChatFormatting.WHITE), tankStyle,
                            new SpecialBoxStyle(fluidStack.getFluid() == Fluids.LAVA ? ColorUtils.doubleDarker(-29925) :
                                    ColorUtils.doubleDarker(IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor())), false));
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        if (blockEntity instanceof IFluidPipe pipe) {
            CompoundTag tag = new CompoundTag();
            FluidNet.TransportStats stats = FluidNet.INSTANCE.getStats(pipe);
            ListTag fluidTagList = new ListTag();
            FluidContainer container = FluidContainer.getContainer(pipe);
            for (Fluid fluid : stats.getTransfered().keySet()) {
                int avg = container.getAverage(fluid);
                if (avg <= 0) continue;
                CompoundTag fluidStackTag = new CompoundTag();
                fluidStackTag.putString("fluid", ForgeRegistries.FLUIDS.getKey(fluid).toString());
                fluidStackTag.putInt("average", avg);
                fluidTagList.add(fluidStackTag);
            }
            if (!fluidTagList.isEmpty()) {
                tag.put("FluidStacks", fluidTagList);
            }

            compoundTag.put("BasicPipeInfo", tag);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return JadePluginHandler.EU_READER_INFO;
    }

    static {
        CACHE = CacheBuilder.newBuilder().expireAfterAccess(1L, TimeUnit.SECONDS).maximumSize(128L).build();
    }

    public static class FluidContainer {

        Object2LongMap<Fluid> lastFluids = new Object2LongOpenHashMap<>();
        Map<Fluid, LongAverager> averages = new Object2ObjectLinkedOpenHashMap<>();

        long lastTimeIn = -1;

        public FluidContainer() {}

        public static FluidContainer getContainer(IFluidPipe tile) {
            FluidContainer result = CACHE.getIfPresent(tile.getPosition());
            if (result == null) {
                result = new FluidContainer();
                CACHE.put(tile.getPosition(), result);
            }

            result.process(tile);
            return result;
        }

        public void process(IFluidPipe pipe) {
            long currentTime = pipe.getWorldObj().getGameTime();
            FluidNet.TransportStats stats = FluidNet.INSTANCE.getStats(pipe);
            if(lastTimeIn == -1) {
                lastFluids.putAll(stats.getTransfered());
                lastTimeIn = currentTime;
                return;
            }
            double diff = currentTime - lastTimeIn;
            if(diff <= 0) return;
            lastTimeIn = currentTime;
            for(Object2LongMap.Entry<Fluid> entry : stats.getTransfered().object2LongEntrySet()) {
                Fluid fluid = entry.getKey();
                long current = entry.getLongValue();
                averages.computeIfAbsent(fluid, T -> new LongAverager(20)).addEntry((int)((current - lastFluids.getLong(fluid)) / diff));
                lastFluids.put(fluid, current);
            }
        }

        public int getAverage(Fluid fluid) {
            LongAverager avg = this.averages.get(fluid);
            return avg == null ? 0 : (int) avg.getAverage();
        }
    }
}
