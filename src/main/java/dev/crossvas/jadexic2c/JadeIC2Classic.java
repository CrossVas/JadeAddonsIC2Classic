package dev.crossvas.jadexic2c;

import dev.crossvas.ic2classicjade.Tags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = Tags.MOD_ID, version = Tags.VERSION, name = Tags.MOD_NAME, acceptedMinecraftVersions = "[1.12.2]")
public class JadeIC2Classic {

    public static final String ID_IC2 = "ic2";

    public JadeIC2Classic() {}

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(ID_IC2, path);
    }
}
