package dev.crossvas.jadexic2c.utils.removals;

import dev.crossvas.jadexic2c.JadeXIC2C;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.util.ModIdentification;

public class ModNameRender {

    public static final String NAME = "IC2 Classic";

    public static final ResourceLocation REMOVER = JadeXIC2C.rl("remove_modid");
    public static final ResourceLocation RELOCATE = JadeXIC2C.rl("relocate_modid");

    public enum ModNameRemover implements IBlockComponentProvider {
        INSTANCE;


        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
            if (ModIdentification.getModName(blockAccessor.getBlock()).equals(NAME)) {
                iTooltip.remove(Identifiers.CORE_MOD_NAME);
            }
        }

        @Override
        public ResourceLocation getUid() {
            return REMOVER;
        }

        @Override
        public int getDefaultPriority() {
            return TooltipPosition.TAIL;
        }
    }

    public enum ModNameRelocator implements IBlockComponentProvider {
        INSTANCE;

        @Override
        public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
            if (ModIdentification.getModName(accessor.getBlock()).equals(NAME)) {
                String modName = String.format(config.getWailaConfig().getFormatting().getModName(), NAME);
                tooltip.add(Component.literal(modName));
            }
        }

        @Override
        public ResourceLocation getUid() {
            return RELOCATE;
        }

        @Override
        public int getDefaultPriority() {
            return TooltipPosition.HEAD;
        }
    }
}
