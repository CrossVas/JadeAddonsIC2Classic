package dev.crossvas.jadexic2c.base;

import dev.crossvas.jadexic2c.JadeCommonHandler;
import dev.crossvas.jadexic2c.JadeHelper;
import dev.crossvas.jadexic2c.JadeTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import snownee.jade.api.IServerDataProvider;

public class JadeBlockEntityDataProvider implements IServerDataProvider<BlockEntity> {

    public static final JadeBlockEntityDataProvider INSTANCE = new JadeBlockEntityDataProvider();

    @Override
    public void appendServerData(CompoundTag compoundTag, ServerPlayer serverPlayer, Level level, BlockEntity blockEntity, boolean b) {
        JadeHelper helper = new JadeHelper();
        helper.setServerData(compoundTag);
        JadeCommonHandler.addInfo(helper, blockEntity, serverPlayer);
        helper.transferData();
    }

    @Override
    public ResourceLocation getUid() {
        return JadeTags.INFO_RENDERER;
    }
}
