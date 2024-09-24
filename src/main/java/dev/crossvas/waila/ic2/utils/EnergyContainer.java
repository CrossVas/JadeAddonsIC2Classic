package dev.crossvas.waila.ic2.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.NodeStats;
import net.minecraft.tileentity.TileEntity;

import java.util.concurrent.TimeUnit;

/**
 * Straight copy-paste of CableProvider.EnergyContainer
 * © IC2Classic Dev
 */

public class EnergyContainer {

    static final Cache<BlockPos, EnergyContainer> CACHE;

    static {
        CACHE = CacheBuilder.newBuilder().expireAfterAccess(1L, TimeUnit.SECONDS).maximumSize(128L).build();
    }

    static LongAverager energyIn = new LongAverager(5);
    static LongAverager energyOut = new LongAverager(5);
    static long lastTime;
    static long lastIn;
    static long lastOut;

    public EnergyContainer() {}

    public static EnergyContainer getContainer(TileEntity tile) {
        BlockPos pos = new BlockPos(tile.xCoord, tile.yCoord, tile.zCoord);
        EnergyContainer result = CACHE.getIfPresent(pos);
        if (result == null) {
            result = new EnergyContainer();
            CACHE.put(pos, result);
        }

        tick(tile.getWorldObj().getTotalWorldTime(), EnergyNet.instance.getNodeStats(tile));
        return result;
    }

    public static void tick(long time, NodeStats stats) {
        if (lastTime == 0L) {
            lastTime = time;
            lastIn = (long) stats.getEnergyIn();
            lastOut = (long) stats.getEnergyOut();
        } else {
            long diff = time - lastTime;
            if (diff <= 0L) {
                return;
            }
            energyIn.addEntry((long) ((stats.getEnergyIn() - lastIn) / diff));
            energyOut.addEntry((long) ((stats.getEnergyOut() - lastOut) / diff));
            lastTime = time;
            lastIn = (long) stats.getEnergyIn();
            lastOut = (long) stats.getEnergyOut();

        }
    }

    public int getAverageIn() {
        return (int) energyIn.getAverage();
    }

    public int getAverageOut() {
        return (int) energyOut.getAverage();
    }
}
