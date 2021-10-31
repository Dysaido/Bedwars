package hu.dysaido.bedwars;

import hu.dysaido.bedwars.api.User;
import hu.dysaido.bedwars.api.command.AbstractCommand;
import hu.dysaido.bedwars.game.Game;
import hu.dysaido.bedwars.listeners.GameInventoryListeners;
import hu.dysaido.bedwars.listeners.GamePlayerListeners;
import hu.dysaido.bedwars.listeners.GameWorldListeners;
import hu.dysaido.bedwars.util.ConfigUtil;
import hu.dysaido.bedwars.util.Log;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.*;
import java.util.stream.Collectors;

public class BedWars extends JavaPlugin implements PluginMessageListener {

    private final static String TAG = "Main";
    private static final Map<Player, User> userMap = new HashMap<>();
    private final String commandPath = "hu.dysaido.bedwars.commands.Command";
    private ConfigUtil configUtil;
    private Game game;

    public static User getUser(Player player) {
        if (player == null) return null;
        if (userMap.containsKey(player)) return userMap.get(player);
        userMap.put(player, new UserImpl(getPlugin(BedWars.class), player));
        return userMap.get(player);
    }

    public static User getUser(String name) {
        if (name == null) return null;
        Player player = Bukkit.getPlayer(name);
        return getUser(player);
    }

    public static User getUser(UUID playerUUID) {
        if (playerUUID == null) return null;
        Player player = Bukkit.getPlayer(playerUUID);
        return getUser(player);
    }

    public static void removeUser(String name) {
        if (name == null) return;
        Player player = Bukkit.getPlayer(name);
        removeUser(player);
    }

    public static void removeUser(UUID playerUUID) {
        if (playerUUID == null) return;
        Player player = Bukkit.getPlayer(playerUUID);
        removeUser(player);
    }

    public static void removeUser(User user) {
        if (user == null) return;
        removeUser(user.getPlayer());
    }

    public static void removeUser(Player player) {
        if (player == null) return;
        userMap.remove(player);
    }

    public static Collection<User> getOnlineUsers() {
        return Bukkit.getOnlinePlayers().stream().map(BedWars::getUser).collect(Collectors.toList());
    }

    public void kickAll() {
        getOnlineUsers().forEach(User::kickUser);
        userMap.clear();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        Log.debug(TAG, "Plugin starting...");
        getServer().getMessenger().registerIncomingPluginChannel(this, "WDL|INIT", this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "WDL|CONTROL");
        configUtil = new ConfigUtil(this);
        game = new Game(this);
        initGameEvents();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Log.debug(TAG, "Plugin disabling...");
        getServer().getScheduler().cancelTasks(this);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (channel.equals("WDL|INIT"))
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "§7[§cSS§7]§b§l >§7 (§eConsole§7)§7 :§b WDL is not authorized on this server!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("bedwars")) {
            if (args.length == 0) setCommandHelper(sender, findCommands().toArray(new String[0]));
            else setCommandExecute(sender, Arrays.asList(args));
        } else Log.error(TAG, "Plugin's command invalid::onCommand -- " + command.getName());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("bedwars")) {
            return setTabExecute(sender, Arrays.asList(args));
        } else {
            Log.error(TAG, "Plugin's tab invalid::onTabComplete -- " + command.getName());
            return null;
        }
    }

    private void initGameEvents() {
        Log.information(TAG, "initGameEvents");
        new GameWorldListeners(this);
        new GameInventoryListeners(this);
        new GamePlayerListeners(this);
    }

    public Game getGame() {
        return game;
    }


    private void setCommandExecute(CommandSender sender, List<String> strings) {
        AbstractCommand abstractCommand;
        try {
            abstractCommand = (AbstractCommand) getClassLoader().loadClass(commandPath + strings.get(0)).getDeclaredConstructor(BedWars.class).newInstance(this);
            abstractCommand.init(sender, strings, AbstractCommand.ExtensionType.COMMAND);
        } catch (Exception e) {
            setCommandHelper(sender, findCommands().toArray(new String[0]));
            e.printStackTrace();
        }

    }

    private List<String> setTabExecute(CommandSender sender, List<String> strings) {
        AbstractCommand abstractCommand;
        try {
            abstractCommand = (AbstractCommand) getClassLoader().loadClass(commandPath + strings.get(0)).getDeclaredConstructor(BedWars.class).newInstance(this);
            abstractCommand.init(sender, strings, AbstractCommand.ExtensionType.TAB);
            return abstractCommand.getTabExecutor();
        } catch (Exception e) {
            return findCommands();
        }
    }

    private void setCommandHelper(CommandSender sender, String[] args) {
        sender.sendMessage("§a/Bedwars §7 " + Arrays.toString(args));
    }

    private List<String> findCommands() {
        ArrayList<String> commands = new ArrayList<>();
        commands.add("creator");
        commands.add("frozen");
        commands.add("generator");
        commands.add("lobby");
        commands.add("ping");
        commands.add("reload");
        return commands;
    }

    public ConfigUtil getConfigUtil() {
        return configUtil;
    }
}
