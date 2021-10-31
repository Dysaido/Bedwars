package hu.dysaido.bedwars.commands;

import hu.dysaido.bedwars.BedWars;
import hu.dysaido.bedwars.api.User;
import hu.dysaido.bedwars.api.command.AbstractCommand;
import hu.dysaido.bedwars.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class Commandfrozen extends AbstractCommand {


    public Commandfrozen(BedWars bedWars) {
        super(bedWars, "frozen");
    }

    @Override
    public void applyCommand(CommandSender sender, List<String> args) {
        if (args.size() > 2) {
            setCommandHelper(sender, getName(), Collections.singletonList("player"));
            return;
        }
        User target = BedWars.getUser(Bukkit.getServer().getPlayer(args.get(1)));
        if (target == null) {
            sender.sendMessage("Sender cannot be null.");
            return;
        }
        if (!target.isFrozen()) {
            target.setFrozen(true);
            sender.sendMessage("You have successfully added " + target.getName());
            target.sendMessage("You have successfully frozen by " + sender.getName());

        } else {
            target.setFrozen(false);
            sender.sendMessage("You have successfully removed " + target.getName());
            target.sendMessage("You have successfully melted by " + sender.getName());

        }

    }

    @Override
    public List<String> applyTab(CommandSender sender, List<String> args) {
        return null;
    }
}
