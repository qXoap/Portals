package dev.xoapp.portals.portal;

import cn.nukkit.Player;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;

import cn.nukkit.level.Sound;
import dev.xoapp.portals.session.Session;
import dev.xoapp.portals.session.SessionFactory;
import dev.xoapp.portals.utils.Portals;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashMap;

@Getter
public class Portal {

    private final String name;

    @Setter
    private Position firstPosition = null;

    @Setter
    private Position secondPosition = null;

    @Setter
    private Location to = null;

    public Portal(String name) {
        this.name = name;
    }

    public void tryToTeleport(Player player) {
        if (to == null) {
            return;
        }

        Session session = SessionFactory.get(player.getName());
        if (session == null) {
            return;
        }

        player.teleport(to);
        Portals.scheduleDelayed(
                () -> player.getLevel().addSound(player.getPosition(), Sound.MOB_ENDERMEN_PORTAL), 5
        );
    }

    public boolean isInside(Position position) {
        if (firstPosition == null || secondPosition == null) {
            return false;
        }

        double playerX = position.getX();
        double playerY = position.getY();
        double playerZ = position.getZ();

        double minX = Math.min(firstPosition.getX(), secondPosition.getX());
        double maxX = Math.max(firstPosition.getX(), secondPosition.getX());

        double minY = Math.min(firstPosition.getY(), secondPosition.getY());
        double maxY = Math.max(firstPosition.getY(), secondPosition.getY());

        double minZ = Math.min(firstPosition.getZ(), secondPosition.getZ());
        double maxZ = Math.max(firstPosition.getZ(), secondPosition.getZ());

        return playerX >= minX &&
                playerX <= maxX &&
                playerY >= minY &&
                playerY <= maxY &&
                playerZ >= minZ &&
                playerZ <= maxZ;
    }

    public LinkedHashMap<String, String> toLinkedMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put("firstPosition", Portals.posToString(firstPosition));
        map.put("secondPosition", Portals.posToString(secondPosition));
        map.put("finalPosition", Portals.locToString(to));

        return map;
    }
}