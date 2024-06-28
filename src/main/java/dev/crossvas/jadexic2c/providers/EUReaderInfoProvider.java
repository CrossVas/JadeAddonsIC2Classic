package dev.crossvas.jadexic2c.providers;

import dev.crossvas.jadexic2c.base.IInfoProvider;
import ic2.core.inventory.filter.IFilter;

public abstract class EUReaderInfoProvider implements IInfoProvider {

    @Override
    public IFilter getFilter() {
        return READER;
    }
}
