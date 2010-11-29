package net.kazed.ambient;

import java.util.HashMap;
import java.util.Map;

import net.kazed.ambient.database.ManagedAudioFragmentDao;
import net.kazed.ambient.service.FragmentPlayer;
import net.kazed.android.inject.ApplicationContext;
import net.kazed.android.inject.DirectApplicationContext;
import net.kazed.android.inject.InjectedApplication;
import android.content.Context;
import android.media.MediaPlayer;

/**
 * Application class.
 * @author Koert Zeilstra
 */
public class AmbientPlayerApplication extends InjectedApplication {

    private Map<Long, MediaPlayer> players;
    private FragmentPlayer fragmentPlayer;
    
    @Override
    public void onCreate() {
        super.onCreate();
        players = new HashMap<Long, MediaPlayer>();
    }

    public FragmentPlayer getFragmentPlayer() {
        if (fragmentPlayer == null) {
            fragmentPlayer = new FragmentPlayer();
        }
        return fragmentPlayer;
    }

    public MediaPlayer getPlayer(long id) {
        MediaPlayer player = null;
        if (players.containsKey(id)) {
            player = players.get(id);
        } else {
            player = new MediaPlayer();
            players.put(id, player);
        }
        return player;
    }

    public void stopAllPlayers() {
        for (MediaPlayer player : players.values()) {
            player.stop();
            player.release();
        }
    }
    
    @Override
    public void onTerminate() {
        if (fragmentPlayer != null) {
            fragmentPlayer.stopAllPlayers();
        }
        stopAllPlayers();
        super.onTerminate();
    }

	@Override
	protected void initializeContext(ApplicationContext applicationContext) {
		super.initializeContext(applicationContext);
		applicationContext.bind(FragmentPlayer.class);
		applicationContext.bind(ManagedAudioFragmentDao.class);
	}

}
