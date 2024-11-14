package dev.xoapp.portals.session;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import dev.xoapp.portals.process.SetupPortalProcess;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Session {

    private final String name;

    @Setter
    private SetupPortalProcess process = null;

    @Setter
    private Map<Integer, Item> inventory = new HashMap<>();

    public Session(String name) {
        this.name = name;
    }

    public Player getPlayer() {
        return Server.getInstance().getPlayerExact(name);
    }

    public void saveInventories() {
        Player player = getPlayer();
        if (player == null) {
            return;
        }

        inventory = player.getInventory().getContents();
    }

    public void returnInventory() {
        Player player = getPlayer();
        if (player == null) {
            return;
        }

        player.getInventory().setContents(inventory);
    }
}
