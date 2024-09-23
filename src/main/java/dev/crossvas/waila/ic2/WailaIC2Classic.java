package dev.crossvas.waila.ic2;

import cpw.mods.fml.common.Mod;
import net.minecraft.util.ResourceLocation;

@Mod(modid = Refs.ID, name = Refs.NAME, version = Refs.VERSION, acceptableRemoteVersions = Refs.MC, dependencies = Refs.DEPS)
public class WailaIC2Classic {

    public static final String ID_IC2 = "ic2";

    public WailaIC2Classic() {}

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID_IC2, path);
    }
}
