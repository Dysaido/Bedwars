package hu.dysaido.bedwars.sound;

import hu.dysaido.bedwars.BedWars;
import hu.dysaido.bedwars.util.Log;

public class SoundManager {
    private final static String TAG = "SoundService";
    private final BedWars bedWars;

    public SoundManager(BedWars bedWars) {
        Log.information(TAG, "initialisation");
        this.bedWars = bedWars;
    }

    public BedWars getBedWars() {
        return bedWars;
    }
}
