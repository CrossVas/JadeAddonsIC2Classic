package dev.crossvas.jadexic2c.info.removals;

import dev.crossvas.jadexic2c.JadeXIC2C;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

import java.util.ArrayList;
import java.util.List;

public enum TankRender implements IBlockComponentProvider {
    INSTANCE;

    public static final ResourceLocation FORGE_FLUID = new ResourceLocation("fluid");
    public static final List<Block> TANK_REMOVAL = new ArrayList<>();

    public static final ResourceLocation ID = JadeXIC2C.rl("remove_renders_fluid");

    @Override
    public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        if (TANK_REMOVAL.contains(blockAccessor.getBlock())) {
            iTooltip.remove(FORGE_FLUID);
        }
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.TAIL;
    }

    @Override
    public ResourceLocation getUid() {
        return ID;
    }
}
