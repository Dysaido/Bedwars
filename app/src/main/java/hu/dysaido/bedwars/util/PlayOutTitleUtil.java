package hu.dysaido.bedwars.util;

import hu.dysaido.bedwars.api.User;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class PlayOutTitleUtil extends ReflectUtil {

    private static final String TAG = "PlayOutTitle";

    public static void setTitle(User user, String title) {
        setCommonTitle(user.getPlayer(), title, MessageType.TITLE);
    }

    public static void setSubTitle(User user, String subTitle) {
        setCommonTitle(user.getPlayer(), subTitle, MessageType.SUBTITLE);
    }

    public static void setTitle(Player player, String title) {
        setCommonTitle(player, title, MessageType.TITLE);
    }

    public static void setSubTitle(Player player, String subTitle) {
        setCommonTitle(player, subTitle, MessageType.SUBTITLE);
    }

    private static void setCommonTitle(Player player, String message, MessageType messageType) {
        try {
            Object chatTitle = IChatBaseComponent().getDeclaredClasses()[0].getMethod("a", String.class)
                    .invoke(null, "{\"text\": \"" + message + "\"}");

            Constructor<?> titleConstructor = PacketPlayOutTitle()
                    .getConstructor(PacketPlayOutTitle().getDeclaredClasses()[0], IChatBaseComponent(), int.class, int.class, int.class);

            Object packet = titleConstructor.newInstance(PacketPlayOutTitle()
                    .getDeclaredClasses()[0].getField(messageType.toString()).get(null), chatTitle, 40, 40, 40);
            sendPacket(player, packet);
        } catch (Exception e) {
            Log.error(TAG, "com.dysaido.bedwars.utils.PlayOutTitle::setCommonTitle::" + messageType);
        }
    }

    private enum MessageType {
        TITLE, SUBTITLE
    }
}
