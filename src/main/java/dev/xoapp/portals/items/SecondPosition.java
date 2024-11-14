package dev.xoapp.portals.items;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.utils.TextFormat;

public class SecondPosition extends Item {

    public SecondPosition() {
        super(ItemID.GOLD_AXE);
        setCustomName(TextFormat.colorize("&6Second Position"));
        getNamedTag().putString("portals", "secondPosition");
    }
}
