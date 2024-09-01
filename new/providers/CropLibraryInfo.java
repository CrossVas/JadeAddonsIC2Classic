package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.base.interfaces.IJadeHelper;
import ic2.api.energy.EnergyNet;
import ic2.api.energy.tile.IEnergySink;
import ic2.core.block.base.tiles.impls.BaseCropLibraryTileEntity;
import ic2.core.block.machines.tiles.ev.UUCropLibraryTileEntity;
import ic2.core.utils.helpers.StackUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class CropLibraryInfo implements IInfoProvider {

    public static final CropLibraryInfo THIS = new CropLibraryInfo();

    @Override
    public void addInfo(IJadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof BaseCropLibraryTileEntity baseCropLibrary) {
            if (baseCropLibrary instanceof IEnergySink sink) {
                defaultText(helper, "ic2.probe.eu.tier.name", EnergyNet.INSTANCE.getDisplayTier(sink.getSinkTier()));
                defaultText(helper, "ic2.probe.eu.max_in.name", EnergyNet.INSTANCE.getPowerFromTier(sink.getSinkTier()));
                defaultText(helper, "ic2.probe.eu.usage.name", 1);
            }

            if (baseCropLibrary instanceof UUCropLibraryTileEntity uum) {
                bar(helper, uum.uu_matter, 512, Component.literal("UU Matter: " + uum.uu_matter + " / " + 512), -5829955);
            }

            int cropCount = baseCropLibrary.syncer.getCropCount();
            int statCount = baseCropLibrary.syncer.getStatCount();
            int sizeLimit = baseCropLibrary.storage.getSizeLimit();
            int typeLimit = baseCropLibrary.storage.getTypeLimit();
            int statLimit = baseCropLibrary.storage.getStatLimit();

            if (typeLimit != -1) {
                defaultText(helper, "ic2.probe.crop_library.type.name", cropCount, typeLimit);
                defaultText(helper, "ic2.probe.crop_library.stat.name", statCount, statLimit * typeLimit);
                defaultText(helper, "ic2.probe.crop_library.size.name", sizeLimit);
            }

            List<ItemStack> stackList = StackUtil.copyNonEmpty(baseCropLibrary.storage.getTypes());
            addGrid(helper, stackList, Component.translatable("ic2.probe.crop_library.name").withStyle(ChatFormatting.YELLOW));
        }
    }
}
