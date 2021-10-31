package hu.dysaido.bedwars.commands;

import hu.dysaido.bedwars.BedWars;
import hu.dysaido.bedwars.api.command.AbstractCommand;
import hu.dysaido.bedwars.api.game.GameState;
import hu.dysaido.bedwars.util.FileManager;
import hu.dysaido.bedwars.util.LocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Commandlobby extends AbstractCommand {

    private final static ArrayList<String> commands = new ArrayList<>(Arrays.asList("spawn", "setspawn", "gamestate", "removespawn"));

    public Commandlobby(BedWars bedWars) {
        super(bedWars,"lobby");
    }

    @Override
    public void applyCommand(CommandSender sender, List<String> args) {
        FileManager fileManager = bedWars.getConfigUtil().getLocations();
        ConfigurationSection locationsConfig = fileManager.getFile().getConfigurationSection("lobby");
        if (args.get(1).equals(commands.get(0))) {
            sender.sendMessage(ChatColor.GREEN + LocationUtil.getLocation(locationsConfig).toString());
        } else if (args.get(1).equals(commands.get(1))) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                LocationUtil.setLocation(player.getLocation(), locationsConfig);
                fileManager.saveFile();
            }
            else sender.sendMessage("You are not a player!");
        } else if (args.get(1).equals(commands.get(2))) {
            sender.sendMessage(ChatColor.RED + "Lobby mod isAlive!");
            bedWars.getGame().setState(GameState.WAIT);
        }
    }

    @Override
    public List<String> applyTab(CommandSender sender, List<String> args) {
        return commands;
    }
}
