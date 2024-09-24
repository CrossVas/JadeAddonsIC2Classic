package dev.crossvas.waila.ic2.utils;

public class BlockPos {

    int X, Y, Z;

    public BlockPos(BlockPos pos) {
        this(pos.X, pos.Y, pos.Z);
    }

    public BlockPos(int x, int y, int z) {
        this.X = x;
        this.Y = y;
        this.Z = z;
    }

    public int getX() {
        return this.X;
    }

    public int getY() {
        return this.Y;
    }

    public int getZ() {
        return this.Z;
    }

    @Override
    public String toString() {
        return "BlockPos{" + "X=" + X + ", Y=" + Y + ", Z=" + Z + '}';
    }
}
