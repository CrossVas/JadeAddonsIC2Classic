package dev.crossvas.jadexic2c;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod("jadexic2c")
public class JadeIC2Classic {

    public static final String ID_IC2 = "ic2";

    public JadeIC2Classic() {}

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID_IC2, path);
    }
}
