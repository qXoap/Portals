package dev.xoapp.portals.portal;

import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.utils.TextFormat;
import dev.xoapp.portals.Loader;
import dev.xoapp.portals.data.DataCreator;
import dev.xoapp.portals.utils.Portals;
import lombok.Getter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class PortalFactory {

    @Getter
    private static final Map<String, Portal> portals = new HashMap<>();

    private static DataCreator dataCreator = null;

    public static void initialize() {
        dataCreator = new DataCreator("portals.json");

        dataCreator.getSavedData().forEach((key, value) -> {
            if (value.toString().isEmpty()) {
                return;
            }

            Portal portal = new Portal(key);
            LinkedHashMap<String, Object> data = Portals.stringToMap(value.toString());

            Position firstPosition = Portals.stringToPos(data.get("firstPosition").toString());
            Position secondPosition = Portals.stringToPos(data.get("secondPosition").toString());

            Location finalPosition = Portals.stringToLoc(data.get("finalPosition").toString());

            portal.setFirstPosition(firstPosition);
            portal.setSecondPosition(secondPosition);

            portal.setTo(finalPosition);

            portals.put(key, portal);
        });

        Loader.getInstance().getLogger().info(TextFormat.colorize(
                "&aSuccessfully loaded &e" + portals.size() + "&a Portals"
        ));
    }

    public static void create(Portal portal) {
        portals.put(portal.getName(), portal);
    }

    public static Portal get(String name) {
        return portals.get(name);
    }

    public static Portal getByPosition(Position position) {
        AtomicReference<Portal> findPortal = new AtomicReference<>();

        portals.forEach((name, portal) -> {
            if (!portal.isInside(position)) {
                return;
            }

            findPortal.set(portal);
        });

        return findPortal.get();
    }

    public static void delete(String name) {
        portals.remove(name);
        dataCreator.delete(name);
    }

    public static void save() {
        portals.forEach((name, portal) -> dataCreator.set(name, portal.toLinkedMap()));
    }
}