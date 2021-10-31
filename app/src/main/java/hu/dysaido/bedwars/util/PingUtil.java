package hu.dysaido.bedwars.util;

import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PingUtil extends ReflectUtil {

    public static int getPing(Player player) {
        try {
            assert CraftPlayer() != null;
            Object CraftPlayer = CraftPlayer().cast(player);
            Method getHandle = CraftPlayer.getClass().getMethod("getHandle");
            Object EntityPlayer = getHandle.invoke(CraftPlayer);
            Field ping = EntityPlayer.getClass().getDeclaredField("ping");
            return (Integer) ping.get(EntityPlayer);
        } catch (Exception ex) {
            return 0;
        }
    }
}
