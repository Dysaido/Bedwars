package hu.dysaido.bedwars.listeners;

import hu.dysaido.bedwars.BedWars;
import hu.dysaido.bedwars.api.User;
import hu.dysaido.bedwars.game.Game;
import hu.dysaido.bedwars.team.Team;
import hu.dysaido.bedwars.team.TeamManager;
import hu.dysaido.bedwars.util.PlayOutTitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.function.Consumer;

public class GamePlayerListeners extends AbstractListener {
    public GamePlayerListeners(BedWars bedWars) {
        super(bedWars);
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        TeamManager teamManager = bedWars.getGame().getTeamManager();
        Player player = event.getPlayer();
        User user = BedWars.getUser(player);
        for (Team team : teamManager.getTeams()) {
            if (team.isMember(user)) event.setFormat(team.getTeamChat(user, event.getMessage()));
        }

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        for (User user : BedWars.getOnlineUsers()) {
            if (user.isFrozen()) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        User user = BedWars.getUser(event.getPlayer());
        Game game = bedWars.getGame();
        game.addMember(user);
        PlayOutTitleUtil.setTitle(user, "Welcome " + user.getName());
        PlayOutTitleUtil.setSubTitle(user, "Feel you well");
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        User user = BedWars.getUser(event.getPlayer());
        Game game = bedWars.getGame();
        game.removeMember(user);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        for (Team team : bedWars.getGame().getTeamManager().getTeams()) {
            if (!team.isBedPlaced()) event.getEntity().setGameMode(GameMode.SPECTATOR);
        }
    }

    private void sendMessage(Player player) {
        player.sendMessage("Consumer method reference");
    }
}
