package dev.crossvas.waila.ic2.base.interfaces;

import net.minecraft.item.ItemStack;

public class InfoTools {

    public interface IEUReader {
        boolean isReader(ItemStack stack);
    }

    public interface ICropnalyzer {
        boolean isAnalyzer(ItemStack stack);
    }

    public interface ITreeTap {
        boolean isBrewer(ItemStack stack);
    }
}
