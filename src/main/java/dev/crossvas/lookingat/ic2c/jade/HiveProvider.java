package dev.crossvas.lookingat.ic2c.jade;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

public class HiveProvider implements IEntityComponentProvider {

    @Override
    public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        if (entityAccessor.getEntity() instanceof Sheep sheep) {
            iTooltip.add(Component.literal("Color: " + sheep.getColor().getSerializedName()));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return new ResourceLocation("id", "path");
    }
}
