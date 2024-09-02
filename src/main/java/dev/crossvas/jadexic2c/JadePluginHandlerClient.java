package dev.crossvas.jadexic2c;

import dev.crossvas.jadexic2c.base.tooltiprenderers.BaseProgressBarRenderer;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin(JadeIC2Classic.ID_IC2)
public class JadePluginHandlerClient implements IWailaPlugin {

    @Override
    public void register(IWailaRegistrar registration) {
        registration.registerTooltipRenderer("jade.progress", new BaseProgressBarRenderer());
//        registration.registerTooltipRenderer("jade.text", new BaseProgressBarRenderer());
    }
}
