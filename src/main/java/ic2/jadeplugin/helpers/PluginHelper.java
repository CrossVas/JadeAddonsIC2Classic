package ic2.jadeplugin.helpers;

import ic2.api.util.DirectionList;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

public class PluginHelper {

    public static Component getSides(DirectionList directionList) {
        Component component = Component.empty();
        if (directionList != null) {
            String[] sides = directionList.toString().replaceAll("\\[", "").replaceAll("]", "")
                    .replaceAll("north", ChatFormatting.YELLOW + "N")
                    .replaceAll("south", ChatFormatting.BLUE + "S")
                    .replaceAll("east", ChatFormatting.GREEN + "E")
                    .replaceAll("west", ChatFormatting.LIGHT_PURPLE + "W")
                    .replaceAll("down", ChatFormatting.AQUA + "D")
                    .replaceAll("up", ChatFormatting.RED + "U").split(",", -1);

            for (String side : sides) {
                component = component.copy().append(side);
            }
            return component;
        }
        return component;
    }

    public static int getColorForFluid(FluidStack fluid) {
        return fluid.getFluid() == Fluids.LAVA ? -29925 : IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor() | -16777216;
    }
}
