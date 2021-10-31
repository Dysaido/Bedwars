package hu.dysaido.bedwars.util;

import hu.dysaido.bedwars.api.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ReflectUtil {

    private static final String TAG = "ReflectUtils";
    private static final String PATH = Bukkit.getServer().getClass().getPackage().getName();

    public static Class<?> getOBCClass(String name) {
        String localPath = "org.bukkit.craftbukkit.";
        try {
            return Class.forName(localPath + PATH.split("\\.")[3] + ".entity" + "." + name);
        } catch (ClassNotFoundException e) {
            Log.error(TAG, localPath + PATH + name);
        }
        return null;
    }

    public static Class<?> getNMSClass(String name) {
        String localPath = "net.minecraft.server.";
        try {
            return Class.forName(localPath + PATH.split("\\.")[3] + "." + name);
        } catch (ClassNotFoundException e) {
            Log.error(TAG, localPath + PATH + name);
        }
        return null;
    }


    public static void sendPacket(User user, Object packet) {
        sendPacket(user.getPlayer(), packet);
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object getHandle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = getHandle.getClass().getField("playerConnection").get(getHandle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            Log.error(TAG, "com.dysaido.bedwars.utils.ReflectUtil::sendPacket");
        }
    }

    public static Class<?> CraftPlayer() {
        return getOBCClass("CraftPlayer");
    }

    public static Class<?> PacketPlayOutTitle() {
        return getNMSClass("PacketPlayOutTitle");
    }

    public static Class<?> IChatBaseComponent() {
        return getNMSClass("IChatBaseComponent");
    }

}
