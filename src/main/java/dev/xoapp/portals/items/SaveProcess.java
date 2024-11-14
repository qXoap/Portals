package dev.xoapp.portals.items;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.utils.TextFormat;

public class SaveProcess extends Item {

    public SaveProcess() {
        super(ItemID.DYE, 11);
        setCustomName(TextFormat.colorize("&aSave"));
        getNamedTag().putString("portals", "save");
    }
}
