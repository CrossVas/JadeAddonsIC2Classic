package dev.crossvas.jadexic2c.base.elements;

import dev.crossvas.jadexic2c.base.interfaces.IJadeElementBuilder;
import net.minecraft.world.phys.Vec2;

public abstract class CommonElement implements IJadeElementBuilder {

    Vec2 SIZE;
    Vec2 TRANSLATION;
    String SIDE;

    public CommonElement(Vec2 size, Vec2 translation, String side) {
        this.SIZE = size;
        this.TRANSLATION = translation;
        this.SIDE = side;
    }

    @Override
    public IJadeElementBuilder size(Vec2 size) {
        SIZE = size;
        return this;
    }

    @Override
    public IJadeElementBuilder align(String side) {
        SIDE = side;
        return this;
    }

    @Override
    public IJadeElementBuilder translate(Vec2 translation) {
        TRANSLATION = translation;
        return this;
    }

    public Vec2 getSize() {
        return SIZE;
    }

    public Vec2 getTranslation() {
        return TRANSLATION;
    }

    public String getSide() {
        return SIDE;
    }
}
