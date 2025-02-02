package ic2.jadeplugin.helpers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.PacketStats;
import ic2.api.energy.TransferStats;
import ic2.api.energy.tile.IEnergyTile;
import ic2.core.utils.collection.LongAverager;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Straight copy-paste of {@link ic2.probeplugin.info.transport.CableProvider.EnergyContainer}
 * © IC2Classic Dev
 */

public class EnergyContainer {

    static final Cache<BlockPos, EnergyContainer> CACHE;

    static {
        CACHE = CacheBuilder.newBuilder().expireAfterAccess(1L, TimeUnit.SECONDS).maximumSize(128L).build();
    }

    LongAverager energyIn = new LongAverager(5);
    LongAverager energyOut = new LongAverager(5);
    LongAverager packetsIn = new LongAverager(5);
    LongAverager packetsOut = new LongAverager(5);
    long lastTime;
    long lastIn;
    long lastOut;
    long lastPacketsIn;
    long lastPacketsOut;

    public EnergyContainer() {}

    public static EnergyContainer getContainer(IEnergyTile tile) {
        EnergyContainer result = CACHE.getIfPresent(tile.getPosition());
        if (result == null) {
            result = new EnergyContainer();
            CACHE.put(tile.getPosition(), result);
        }

        result.tick(tile.getWorldObj().getGameTime(), EnergyNet.INSTANCE.getStats(tile), EnergyNet.INSTANCE.getPacketStats(tile));
        return result;
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

