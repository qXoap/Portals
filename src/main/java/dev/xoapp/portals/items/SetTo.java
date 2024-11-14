package dev.xoapp.portals.items;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.utils.TextFormat;

public class SetTo extends Item {

    public SetTo() {
        super(ItemID.IRON_AXE);
        setCustomName(TextFormat.colorize("&6Final Position"));
        getNamedTag().putString("portals", "setTo");
    }
}