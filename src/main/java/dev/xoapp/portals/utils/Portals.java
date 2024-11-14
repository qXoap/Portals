package dev.xoapp.portals.utils;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.scheduler.Task;
import dev.xoapp.portals.Loader;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.Callable;

public class Portals {

    public static LinkedHashMap<String, Object> stringToMap(String data) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        Stack<Map<String, Object>> stack = new Stack<>();

        stack.push(map);

        int i = 1;

        String key = null;
        StringBuilder value = new StringBuilder();

        boolean insideNestedMap = false;

        while (i < data.length() - 1) {
            char c = data.charAt(i);

            i++;

            switch (c) {
                case '=' -> {
                    if (key != null) {
                        continue;
                    }

                    key = value.toString().trim();
                    value.setLength(0);
                }

                case '{' -> {
                    LinkedHashMap<String, Object> nestedMap = new LinkedHashMap<>();

                    stack.peek().put(key, nestedMap);
                    stack.push(nestedMap);

                    key = null;

                    value.setLength(0);
                    insideNestedMap = true;
                }

                case '}' -> {
                    if (!value.isEmpty()) {
                        stack.peek().put(key, value.toString().trim());
                        value.setLength(0);
                    }

                    stack.pop();
                    insideNestedMap = !stack.isEmpty();

                    key = null;
                }

                case ',' -> {
                    if (insideNestedMap) {
                        continue;
                    }

                    if (key != null) {
                        stack.peek().put(key, value.toString().trim());
                        key = null;
                    }

                    value.setLength(0);
                }

                default -> value.append(c);
            }
        }

        if (key != null && !value.isEmpty()) {
            stack.peek().put(key, value.toString().trim());
        }

        return map;
    }

    public static String posToString(Position position) {
        if (position == null) {
            return null;
        }

        double x = position.getX();
        double y = position.getY();
        double z = position.getZ();

        String world = position.getLevel().getFolderName();

        return x + ":" + y + ":" + z + ":" + world;
    }

    public static Position stringToPos(String pos) {
        if (pos == null) {
            return null;
        }

        String[] split = pos.split(":");

        double x = Double.parseDouble(split[0]);
        double y = Double.parseDouble(split[1]);
        double z = Double.parseDouble(split[2]);

        Level level = Server.getInstance().getLevelByName(split[3]);

        return new Position(x, y, z, level);
    }

    public static String locToString(Location location) {
        if (location == null) {
            return null;
        }

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        double yaw = location.getYaw();
        double pitch = location.getPitch();

        String world = location.getLevel().getFolderName();

        return x + ":" + y + ":" + z + ":" + world + ":" + yaw + ":" + pitch;
    }

    public static Location stringToLoc(String location) {
        if (location == null) {
            return null;
        }

        String[] split = location.split(":");

        double x = Double.parseDouble(split[0]);
        double y = Double.parseDouble(split[1]);
        double z = Double.parseDouble(split[2]);

        double yaw = Double.parseDouble(split[4]);
        double pitch = Double.parseDouble(split[5]);

        Level level = Server.getInstance().getLevelByName(split[3]);

        return new Location(x, y, z, yaw, pitch, level);
    }

    public static void scheduleDelayed(Runnable runnable, int timeout) {
        Server.getInstance().getScheduler().scheduleDelayedTask(Loader.getInstance(), runnable, timeout);
    }
}