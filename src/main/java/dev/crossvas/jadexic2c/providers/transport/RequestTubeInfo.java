package dev.crossvas.jadexic2c.providers.transport;

import dev.crossvas.jadexic2c.base.JadeHelper;
import dev.crossvas.jadexic2c.base.interfaces.IInfoProvider;
import dev.crossvas.jadexic2c.helpers.TextFormatter;
import ic2.core.block.transport.item.tubes.RequestTubeTileEntity;
import ic2.core.utils.helpers.StackUtil;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

public class RequestTubeInfo implements IInfoProvider {

    public static final RequestTubeInfo THIS = new RequestTubeInfo();

    @Override
    public void addInfo(JadeHelper helper, BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof RequestTubeTileEntity requester) {
            NonNullList<ItemStack> list;
            // general
            boolean redstoneControl = requester.redstoneRequest;
            helper.text(TextFormatter.GOLD.translate("info.tube.redstone", status(redstoneControl)));
            // requests
            list = NonNullList.create();
            for (RequestTubeTileEntity.RequestEntry entry : requester.filters) {
                list.add(StackUtil.copyWithSize(entry.getStack(), entry.getAmount()));
            }
            if (!list.isEmpty()) {
                helper.addGrid(list, TextFormatter.GOLD.translate("ic2.probe.tube.requests"));
            }
            // missing
            list = NonNullList.create();
            requester.getMissing(list);
            if (!list.isEmpty()) {
                helper.addGrid(list, TextFormatter.GOLD.translate("ic2.probe.tube.missing"));
            }
            // requested
            list = NonNullList.create();
            for (Object2IntMap.Entry<ItemStack> entry : Object2IntMaps.fastIterable(requester.requested)) {
                list.add(StackUtil.copyWithSize(entry.getKey(), entry.getIntValue()));
            }
            if (!list.isEmpty()) {
                helper.addGrid(list, TextFormatter.GOLD.translate("ic2.probe.tube.requesting"));
            }
            // insertion
            list = NonNullList.create();
            if (!requester.toInsert.isEmpty()) {
                list.addAll(requester.toInsert);
            }
            if (!list.isEmpty()) {
                helper.addGrid(list, TextFormatter.GOLD.translate("ic2.probe.tube.stuck"));
            }
        }
    }
}
