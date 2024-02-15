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

    public static final ModNameRelocator MOD_NAME_REMOVER = new ModNameRelocator(REMOVER, TooltipPosition.TAIL);
    public static final ModNameRelocator MOD_NAME_RELOCATOR = new ModNameRelocator(RELOCATE, TooltipPosition.HEAD);


    public static class ModNameRelocator implements IBlockComponentProvider {

        ResourceLocation ID;
        int PRIORITY;

        public ModNameRelocator(ResourceLocation id, int priority) {
            this.ID = id;
            this.PRIORITY = priority;
        }

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
            if (getUid() == REMOVER) {
                if (ModIdentification.getModName(blockAccessor.getBlock()).equals(NAME)) {
                    iTooltip.remove(Identifiers.CORE_MOD_NAME);
                }
            } else if (getUid() == RELOCATE) {
                // TODO: make sure to relocate addons name as well
                if (ModIdentification.getModName(blockAccessor.getBlock()).equals(NAME)) {
                    String modName = String.format(iPluginConfig.getWailaConfig().getFormatting().getModName(), NAME);
                    iTooltip.add(Component.literal(modName));
                }
            }
        }

        @Override
        public ResourceLocation getUid() {
            return this.ID;
        }

        @Override
        public int getDefaultPriority() {
            return this.PRIORITY;
        }
    }
}
