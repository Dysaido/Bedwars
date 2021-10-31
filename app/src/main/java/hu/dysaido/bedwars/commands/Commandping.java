package hu.dysaido.bedwars.commands;

import hu.dysaido.bedwars.BedWars;
import hu.dysaido.bedwars.api.command.AbstractCommand;
import hu.dysaido.bedwars.util.PingUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Commandping extends AbstractCommand {

    public Commandping(BedWars bedWars) {
        super(bedWars, "ping");
    }

    @Override
    public void applyCommand(CommandSender sender, List<String> args) {
        if (args.size() == 1) {
            sender.sendMessage(String.valueOf(PingUtil.getPing((Player) sender)));
            return;
        }
        Player target = Bukkit.getServer().getPlayer(args.get(1));
        if (target != null) {
            sender.sendMessage(String.valueOf(PingUtil.getPing(target)));
        } else {
            sender.sendMessage("Target cannot be null.");
        }
    }

    @Override
    public List<String> applyTab(CommandSender sender, List<String> args) {
        return null;
    }
}
