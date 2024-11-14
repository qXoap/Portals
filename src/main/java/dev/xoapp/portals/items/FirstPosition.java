package dev.xoapp.portals.items;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.utils.TextFormat;

public class FirstPosition extends Item {

    public FirstPosition() {
        super(ItemID.IRON_AXE);
        setCustomName(TextFormat.colorize("&aFirst Position"));
        getNamedTag().putString("portals", "firstPosition");
    }
}
