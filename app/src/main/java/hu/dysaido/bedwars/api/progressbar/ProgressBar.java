package hu.dysaido.bedwars.api.progressbar;

import hu.dysaido.bedwars.api.User;
import org.bukkit.ChatColor;

public interface ProgressBar {

    public void setProgress(int progress);

    public int getProgress();

    public void setColor(ChatColor color);

    public ChatColor getColor();

    public void setMin(int value);

    public int getMin();

    public void setMax(int value);

    public int getMax();

    public void addUser(User user);

    public void removeUser(User user);

    public void setVisibility(Visibility visibility);

    public Visibility getVisibility();

    public void start();

    public void stop();

}
