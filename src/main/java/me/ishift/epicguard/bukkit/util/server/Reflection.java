package me.ishift.epicguard.bukkit.util.server;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Reflection {
    private static String version;

    public static void init() {
        version = Bukkit.getServer().getClass().getPackage().getName();
        version = version.substring(version.lastIndexOf(".") + 1);
    }

    public static boolean isOldVersion() {
        return !version.startsWith("v1_13") && !version.startsWith("v1_14") && !version.startsWith("v1_15") && !version.startsWith("v1_16");
    }

    public static String getVersion() {
        return version;
    }

    public static void sendPacket(final Player player, final Object packet) {
        try {
            final Object handle = player.getClass().getMethod("getHandle").invoke(player);
            final Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getNMSClass(final String name) {
        final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
