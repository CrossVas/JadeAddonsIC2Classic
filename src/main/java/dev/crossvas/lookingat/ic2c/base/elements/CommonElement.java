package dev.crossvas.lookingat.ic2c.base.elements;

import dev.crossvas.lookingat.ic2c.base.interfaces.IElementBuilder;
import net.minecraft.world.phys.Vec2;

public abstract class CommonElement implements IElementBuilder {

    Vec2 TRANSLATION;
    String SIDE;

    public CommonElement(Vec2 translation, String side) {
        this.TRANSLATION = translation;
        this.SIDE = side;
    }

    @Override
    public IElementBuilder align(String side) {
        SIDE = side;
        return this;
    }

    @Override
    public IElementBuilder translate(Vec2 translation) {
        TRANSLATION = translation;
        return this;
    }

    public Vec2 getTranslation() {
        return TRANSLATION;
    }

    public String getSide() {
        return SIDE;
    }
}
