package hu.dysaido.bedwars.game;

import hu.dysaido.bedwars.api.User;
import hu.dysaido.bedwars.api.game.GameState;
import hu.dysaido.bedwars.util.Log;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameStartTask extends BukkitRunnable {
    private final Game game;
    private final BossBar bossBar;
    private final String message;
    private final int title_message_start;
    private final String title_message;
    private final double finalTimer;
    private double timer;
    private final static String TAG = "GameStartTask";

    public GameStartTask(Game game) {
        this.game = game;
        ConfigurationSection progressbar = game.getPlugin().getConfig().getConfigurationSection("starting").getConfigurationSection("progressbar");
        this.timer = progressbar.getInt("countdown");
        this.finalTimer = timer;
        this.message = progressbar.getString("message").replaceAll("&", "§");
        this.title_message_start = progressbar.getInt("title-message-start");
        this.title_message = progressbar.getString("title-message").replaceAll("&", "§");
        bossBar = Bukkit.getServer().createBossBar(message.replace("{timer}", String.valueOf(timer)),
                BarColor.valueOf(progressbar.getString("color")), BarStyle.SEGMENTED_20);
        bossBar.setProgress(1.0);
        game.getMembers().forEach(user -> bossBar.addPlayer(user.getPlayer()));
    }

    @Override
    public void run() {
        if (timer <= 0) {
            game.setState(GameState.ACTIVE);
            cancel();
        }
        if (!game.hasEnoughMember()) cancel();
        bossBar.setProgress(timer / finalTimer);
        bossBar.setTitle(message.replace("{timer}", String.valueOf(timer)));
        if (timer <= title_message_start) {
            Bukkit.getServer().broadcastMessage(message.replace("{timer}", String.valueOf(timer)));
            for (User user : game.getMembers()) {
                Player player = user.getPlayer();
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);
                player.sendTitle("", title_message.replace("{timer}", String.valueOf(timer)), 20, 20, 20);
            }

        }
        timer--;
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        Log.information(TAG, "Game starting progressbar is death!");
        bossBar.removeAll();
    }
}
