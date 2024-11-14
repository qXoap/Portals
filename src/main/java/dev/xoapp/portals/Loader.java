package dev.xoapp.portals;

import cn.nukkit.plugin.PluginBase;
import dev.xoapp.portals.commands.PortalsCommand;
import dev.xoapp.portals.portal.PortalFactory;
import dev.xoapp.portals.session.SessionFactory;
import lombok.Getter;

public class Loader extends PluginBase {

    @Getter
    private static Loader instance;

    @Override
    public void onEnable() {
        instance = this;

        PortalFactory.initialize();

        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getServer().getCommandMap().register("portals", new PortalsCommand());
    }

    @Override
    public void onDisable() {
        PortalFactory.save();
        SessionFactory.close();
    }
}
