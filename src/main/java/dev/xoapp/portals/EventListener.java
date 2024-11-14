package dev.xoapp.portals;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3f;
import dev.xoapp.portals.portal.Portal;
import dev.xoapp.portals.portal.PortalFactory;
import dev.xoapp.portals.session.Session;
import dev.xoapp.portals.session.SessionFactory;

public class EventListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        SessionFactory.create(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        Session session = SessionFactory.get(player.getName());
        if (session == null) {
            return;
        }

        if (session.getProcess() != null) {
            session.returnInventory();
        }

        SessionFactory.delete(player.getName());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Vector3f from = event.getFrom().asVector3f();
        Vector3f to = event.getTo().asVector3f();

        if (from.equals(to)) {
            return;
        }

        Session session = SessionFactory.get(player.getName());
        if (session == null) {
            return;
        }

        Position position = player.getPosition();
        Portal portal = PortalFactory.getByPosition(position);

        if (portal == null) {
            return;
        }

        portal.tryToTeleport(player);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Session session = SessionFactory.get(player.getName());
        if (session == null) {
            return;
        }

        if (session.getProcess() == null) {
            return;
        }

        session.getProcess().handleInteract(event);
    }
}