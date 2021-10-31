package hu.dysaido.bedwars.commands;

import hu.dysaido.bedwars.BedWars;
import hu.dysaido.bedwars.api.command.AbstractCommand;
import hu.dysaido.bedwars.api.game.GameState;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Commandcreator extends AbstractCommand {

    public Commandcreator(BedWars bedWars) {
        super(bedWars,"creator");
    }

    @Override
    public void applyCommand(CommandSender sender, List<String> args) {
        sender.sendMessage(ChatColor.RED + "CREATOR mod isAlive!");
        bedWars.getGame().setState(GameState.CREATOR);
    }

    @Override
    public List<String> applyTab(CommandSender sender, List<String> args) {
        return new ArrayList<>(Arrays.asList("Test", "Test2", "Test3"));
    }
}
