package dev.xoapp.portals.process;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.TextFormat;
import dev.xoapp.portals.items.FirstPosition;
import dev.xoapp.portals.items.SaveProcess;
import dev.xoapp.portals.items.SecondPosition;
import dev.xoapp.portals.items.SetTo;
import dev.xoapp.portals.portal.Portal;
import dev.xoapp.portals.portal.PortalFactory;
import dev.xoapp.portals.session.Session;
import dev.xoapp.portals.session.SessionFactory;

public class SetupPortalProcess {

    private final String name;

    private Portal basePortal = null;

    public SetupPortalProcess(String name) {
        this.name = name;
    }

    public void prepare(Session session) {
        Player player = session.getPlayer();
        if (player == null) {
            return;
        }

        basePortal = new Portal(name);
        session.saveInventories();

        Inventory inventory = player.getInventory();
        inventory.clearAll();

        inventory.setItem(0, new FirstPosition());
        inventory.setItem(1, new SecondPosition());
        inventory.setItem(4, new SetTo());
        inventory.setItem(8, new SaveProcess());

        player.sendMessage(TextFormat.colorize(
                "&aYou successfully entered in &e" + name + " Portal&a setup process"
        ));
    }

    public void handleInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Item item = event.getItem();
        Block block = event.getBlock();

        Session session = SessionFactory.get(player.getName());
        if (session == null) {
            return;
        }

        CompoundTag namedTag = item.getNamedTag();
        if (namedTag == null) {
            return;
        }

        String compoundName = namedTag.getString("portals");
        if (compoundName == null) {
            return;
        }

        event.setCancelled();

        switch (compoundName) {
            case "firstPosition" -> {
                basePortal.setFirstPosition(block.getLocation());
                player.sendMessage(TextFormat.colorize("&aYou successfully putted the first position"));
            }

            case "secondPosition" -> {
                basePortal.setSecondPosition(block.getLocation());
                player.sendMessage(TextFormat.colorize("&aYou successfully putted the second position"));
            }

            case "setTo" -> {
                basePortal.setTo(player.getLocation());
                player.sendMessage(TextFormat.colorize("&aYou successfully putted the final position"));
            }

            case "save" -> saveProcess(session);
        }
    }

    private void saveProcess(Session session) {
        Player player = session.getPlayer();
        if (player == null) {
            return;
        }

        session.setProcess(null);
        session.returnInventory();

        Portal portal = PortalFactory.get(name);
        if (portal == null) {
            player.sendMessage(TextFormat.colorize("&6Error while saving portal"));
            return;
        }

        portal.setTo(basePortal.getTo());

        portal.setFirstPosition(basePortal.getFirstPosition());
        portal.setSecondPosition(basePortal.getSecondPosition());

        player.sendMessage(TextFormat.colorize(
                "&aYou successfully saved this portal"
        ));
    }
}