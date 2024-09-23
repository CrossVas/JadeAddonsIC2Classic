package dev.crossvas.waila.ic2.helpers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import ic2.core.block.transport.fluid.graph.FluidNet;
import ic2.core.block.transport.fluid.graph.IFluidPipe;
import ic2.core.utils.collection.LongAverager;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.material.Fluid;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class FluidContainer {

    static final Cache<BlockPos, FluidContainer> CACHE;

    static {
        CACHE = CacheBuilder.newBuilder().expireAfterAccess(1L, TimeUnit.SECONDS).maximumSize(128L).build();
    }

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
