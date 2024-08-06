package dev.crossvas.lookingat.ic2c.waila;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.component.SpacingComponent;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Map;

public class WailaHelper {


    public static Map<String, String> MAPPED_LANG = new Object2ObjectLinkedOpenHashMap<>();
    public static List<Component> SPEED_COMP = new ObjectArrayList<>();

    public static void paddingY(ITooltip tooltip, int y) {
        tooltip.addLine(new SpacingComponent(0, y));
    }

    static {
        MAPPED_LANG.put("ic2.probe.eu.storage.full.name", of("energy"));
        MAPPED_LANG.put("ic2.probe.barrel.beer.sugar_cane.name", of("sugar_cane"));
        MAPPED_LANG.put("ic2.probe.barrel.whisky.grist.name", of("grist"));
        MAPPED_LANG.put("ic2.probe.barrel.whisky.years.name", of("years"));
        MAPPED_LANG.put("ic2.probe.barrel.beer.redstone.name", of("redstone"));
        MAPPED_LANG.put("ic2.probe.barrel.beer.glowstone.name", of("glowstone"));
        MAPPED_LANG.put("ic2.probe.barrel.beer.wheat.name", of("wheat"));
        MAPPED_LANG.put("ic2.probe.barrel.beer.hops.name", of("hops"));
        MAPPED_LANG.put("ic2.probe.progress.material.name", of("material"));
        MAPPED_LANG.put("info.block.ic2.induction_furnace.heat", "info.block.ic2.induction_furnace.heat");
        MAPPED_LANG.put("info.block.ic2.rotary_macerator.speed", "info.block.ic2.rotary_macerator.speed");
        MAPPED_LANG.put("info.block.ic2.singularity_compressor.pressure", "info.block.ic2.singularity_compressor.pressure");
        MAPPED_LANG.put("info.block.ic2.centrifugal_extractor.speed", "info.block.ic2.centrifugal_extractor.speed");
        MAPPED_LANG.put("info.block.ic2.compacting_recycler.speed", "info.block.ic2.compacting_recycler.speed");
        MAPPED_LANG.put("info.block.ic2.rare_earth_centrifuge.speed", "info.block.ic2.rare_earth_centrifuge.speed");
        MAPPED_LANG.put("info.block.ic2.vacuum_canner.speed", "info.block.ic2.vacuum_canner.speed");
        MAPPED_LANG.put("ic2.probe.progress.full.name", of("progress"));
        MAPPED_LANG.put("ic2.probe.crop.info.stage", of("crop.stage"));
        MAPPED_LANG.put("ic2.probe.crop.info.points", of("crop.points"));
        MAPPED_LANG.put("ic2.probe.crop.info.growth", of("crop.growth"));
        MAPPED_LANG.put("ic2.probe.crop.info.gain", of("crop.gain"));
        MAPPED_LANG.put("ic2.probe.crop.info.resistance", of("crop.resistance"));
        MAPPED_LANG.put("ic2.probe.crop.info.needs", of("crop.needs"));
        MAPPED_LANG.put("ic2.probe.crop.info.scan", of("crop.scan"));
        MAPPED_LANG.put("ic2.probe.crop.info.fertilizer", of("crop.fertilizer"));
        MAPPED_LANG.put("ic2.probe.crop.info.water", of("crop.water"));
        MAPPED_LANG.put("ic2.probe.crop.info.weedex", of("crop.weedex"));
        MAPPED_LANG.put("ic2.probe.crop.info.nutrients", of("crop.nutrients"));
        MAPPED_LANG.put("ic2.probe.crop.info.humidity", of("crop.humidity"));
        MAPPED_LANG.put("ic2.probe.crop.info.env", of("crop.env"));
        MAPPED_LANG.put("ic2.probe.crop.info.light", of("crop.light"));
        MAPPED_LANG.put("ic2.probe.memory_expansion.name", of("memory_expansion"));
        MAPPED_LANG.put("ic2.probe.reactor.heat.name", of("reactor.heat"));
        MAPPED_LANG.put("ic2.multiblock.reform.next", of("reform.next"));
        MAPPED_LANG.put("ic2.probe.xp.prefix.name", of("sored_xp"));
        MAPPED_LANG.put("ic2.probe.waila.progress", "ic2.probe.waila.progress");
        MAPPED_LANG.put("ic2.probe.matter.amplifier.name", of("amplifier"));
        MAPPED_LANG.put("ic2.progress.material.name", of("fusion.material"));
        MAPPED_LANG.put("ic2.probe.discharging.eta.name", of("discharging"));
        MAPPED_LANG.put("ic2.probe.villager_o_mat.next", of("villager_o_mat.next"));
        MAPPED_LANG.put("ic2.probe.plasma.name", of("plasma"));
        MAPPED_LANG.put("ic2.probe.matter.name", of("matter"));
        MAPPED_LANG.put("ic2.probe.fuel.storage.name", of("fuel"));
    }

    public static Component getWailaComp(Component from) {
        Component toReturn = from;
        JsonObject json = Component.Serializer.toJsonTree(from).getAsJsonObject();
        if (json.has("translate")) {
            String key = json.get("translate").getAsString();
            if (!key.isEmpty()) {
                if (key.equals("ic2.probe.uranium.type.name")) {
                    return Component.literal(json.getAsJsonArray("with").get(0).getAsString());
                }
                String newKey = MAPPED_LANG.getOrDefault(key, "");
                if (!newKey.isEmpty()) {
                    toReturn = Component.translatable(newKey);
                }
                if (newKey.equals(key)) {
                    SPEED_COMP.add(from);
                }
            }
        } else {
            // Return an empty component, meaning it has no translation key
            // these components we create them later for Waila to display using the current / max values
            return Component.empty();
        }
        return toReturn;
    }

    public static String of(String info) {
        return "waila.info." + info;
    }
}
