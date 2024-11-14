package dev.xoapp.portals.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import dev.xoapp.portals.portal.Portal;
import dev.xoapp.portals.portal.PortalFactory;
import dev.xoapp.portals.process.SetupPortalProcess;
import dev.xoapp.portals.session.Session;
import dev.xoapp.portals.session.SessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PortalsCommand extends Command {

    public PortalsCommand() {
        super("portals");

        setAliases(new String[]{"portal"});

        setPermission("portals.command");
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!(commandSender instanceof Player player)) {
            return false;
        }

        if (!testPermission(player)) {
            return false;
        }

        if (strings[0].isEmpty()) {
            player.sendMessage(TextFormat.colorize("&6Use /portals help"));
            return false;
        }

        switch (strings[0].toLowerCase()) {
            case "help" -> {
                List<String> messages = List.of(
                        "&6Use /portals create <name>",
                        "&6Use /portals setup <name>",
                        "&6Use /portals delete <name>",
                        "&6Use /portals list"
                );

                for (String message : messages) {
                    player.sendMessage(TextFormat.colorize(message));
                }
            }

            case "create" -> {
                if (strings[1].isEmpty()) {
                    player.sendMessage(TextFormat.colorize("&6Use /portals create <name>"));
                    return false;
                }

                Portal portal = PortalFactory.get(strings[1]);
                if (portal != null) {
                    player.sendMessage(TextFormat.colorize("&6This portal already exists"));
                    return false;
                }

                PortalFactory.create(new Portal(strings[1]));

                player.sendMessage(TextFormat.colorize(
                        "&aYou successfully created the portal &e" + strings[1]
                ));
            }

            case "setup" -> {
                if (strings[1].isEmpty()) {
                    player.sendMessage(TextFormat.colorize("&6Use /portals setup <name>"));
                    return false;
                }

                Portal portal = PortalFactory.get(strings[1]);
                if (portal == null) {
                    player.sendMessage(TextFormat.colorize("&6This portal doesn't exists"));
                    return false;
                }

                Session session = SessionFactory.get(player.getName());
                if (session == null) {
                    return false;
                }

                SetupPortalProcess process = new SetupPortalProcess(portal.getName());

                process.prepare(session);
                session.setProcess(process);
            }

            case "delete" -> {
                if (strings[1].isEmpty()) {
                    player.sendMessage(TextFormat.colorize("&6Use /portals delete <name>"));
                    return false;
                }

                Portal portal = PortalFactory.get(strings[1]);
                if (portal == null) {
                    player.sendMessage(TextFormat.colorize("&6This portal doesn't exists"));
                    return false;
                }

                PortalFactory.delete(portal.getName());
                player.sendMessage(TextFormat.colorize(
                        "&aYou successfully deleted the portal &e" + portal.getName()
                ));
            }

            case "list" -> {
                Set<String> keys = PortalFactory.getPortals().keySet();
                String formatKeys = String.join(", ", keys);

                player.sendMessage(TextFormat.colorize(
                        "&aAvailable Portals &e(" + keys.size() + ")&a: &b" + formatKeys
                ));
            }

            default -> player.sendMessage(TextFormat.colorize("&6Use /portals help"));
        }

        return true;
    }
}