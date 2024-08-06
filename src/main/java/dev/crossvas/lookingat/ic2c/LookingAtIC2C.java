package dev.crossvas.lookingat.ic2c;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod("jadexic2c")
public class LookingAtIC2C {

    public static final String ID_IC2 = "ic2";

    public LookingAtIC2C() {}

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID_IC2, path);
    }
}
