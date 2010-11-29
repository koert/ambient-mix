package net.kazed.ambient.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.kazed.ambient.AudioFragment;
import net.kazed.android.inject.Component;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

/**
 * Player for AudioFragments.
 */
@Component("fragmentPlayer")
public class FragmentPlayer {

    private Map<Long, MediaPlayer> players;
    private Map<MediaPlayer, Long> playerToFragmentId;
    private Map<Long, AudioFragment> fragments;
    private Map<Long, FragmentListener> fragmentListeners;
    private OnCompletionListener onCompletionListener;
    
    public FragmentPlayer() {
        players = new HashMap<Long, MediaPlayer>();
        playerToFragmentId = new HashMap<MediaPlayer, Long>();
        fragments = new HashMap<Long, AudioFragment>();
        fragmentListeners = new HashMap<Long, FragmentListener>();
        onCompletionListener = new PlayerOnCompletionListener();
    }

    public MediaPlayer getPlayer(AudioFragment fragment) {
        MediaPlayer player = null;
        if (players.containsKey(fragment.getId())) {
            player = players.get(fragment.getId());
        } else {
            player = new MediaPlayer();
            player.setOnCompletionListener(onCompletionListener);
            players.put(fragment.getId(), player);
            playerToFragmentId.put(player, fragment.getId());
            player.reset();
            try {
	            player.setDataSource(fragment.getPath());
	            player.setLooping(fragment.isRepeat());
	            player.prepare();
            } catch (IllegalArgumentException e) {
            	throw new AudioException("Failed to prepare", e);
            } catch (IllegalStateException e) {
            	throw new AudioException("Failed to prepare", e);
            } catch (IOException e) {
            	throw new AudioException("Failed to prepare", e);
            }
        }
        return player;
    }
    
    public void removePlayer(AudioFragment fragment) {
        MediaPlayer player = null;
        if (players.containsKey(fragment.getId())) {
            player = players.get(fragment.getId());
            players.remove(fragment.getId());
            playerToFragmentId.remove(player);
            player.release();
        }
    	
    }
    
    public void updateFragment(AudioFragment fragment) {
        fragments.put(fragment.getId(), fragment);
        MediaPlayer player = players.get(fragment.getId());
        if (player != null) {
            player.setLooping(fragment.isRepeat());
        }
    }

    public void setListener(AudioFragment fragment, FragmentListener listener) {
    	fragmentListeners.put(fragment.getId(), listener);
    }
    
    public void removeListener(AudioFragment fragment) {
    	fragmentListeners.remove(fragment);
    }
    
    public void play(AudioFragment fragment) {
        fragments.put(fragment.getId(), fragment);
        MediaPlayer player = getPlayer(fragment);
        try {
            player.start();
        } catch (IllegalArgumentException e) {
            throw new AudioException("Failed to start", e);
        } catch (IllegalStateException e) {
            throw new AudioException("Failed to start", e);
        }
    }
    
    public void pause(AudioFragment fragment) {
        fragments.put(fragment.getId(), fragment);
        MediaPlayer player = getPlayer(fragment);
        try {
            player.pause();
        } catch (IllegalArgumentException e) {
        	throw new AudioException("Failed to pause", e);
        } catch (IllegalStateException e) {
        	throw new AudioException("Failed to pause", e);
        }
    }

    public boolean isPlaying(AudioFragment fragment) {
    	boolean playing = false;
        if (players.containsKey(fragment.getId())) {
            MediaPlayer player = players.get(fragment.getId());
            playing = player.isPlaying();
        }
    	return playing;
    }
    
    public void stopAllPlayers() {
        for (MediaPlayer player : players.values()) {
            player.stop();
        }
    }
    
    public void releaseAllPlayers() {
        for (MediaPlayer player : players.values()) {
            player.release();
        }
        players.clear();
    }
    
    public static interface FragmentListener {
    	void onCompletion(AudioFragment fragment);
    }
    
    private class PlayerOnCompletionListener implements OnCompletionListener {

        public void onCompletion(MediaPlayer player) {
        	Long fragmentId = playerToFragmentId.get(player);
        	if (fragmentId != null) {
            	FragmentListener listener = fragmentListeners.get(fragmentId);
            	if (listener != null) {
            		listener.onCompletion(fragments.get(fragmentId));
            	}
        	}
        }
        
    }
    
}
