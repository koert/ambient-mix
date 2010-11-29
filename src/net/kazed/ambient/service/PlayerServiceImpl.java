package net.kazed.ambient.service;

import java.io.IOException;

import net.kazed.ambient.AmbientPlayerApplication;
import net.kazed.ambient.AudioFragment;
import net.kazed.ambient.database.AudioFragmentDao;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class PlayerServiceImpl extends Service {
    
    private static final String TAG = "PlayerServiceImpl";

    private AmbientPlayerApplication application;
    
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();
        application = (AmbientPlayerApplication) getApplication();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    private final PlayerService.Stub binder = new PlayerService.Stub() {
        public void pauseAll() {
            // TODO Auto-generated method stub

        }

        public void start(long audioFragmentId) {
            play(getAudioFragment(audioFragmentId));
        }

        public void stop(long audioFragmentId) {
            // TODO Auto-generated method stub

        }

        public void stopAll() {
            application.stopAllPlayers();
        }
    };
    
    private AudioFragment getAudioFragment(long audioFragmentId) {
        Uri itemUri = AudioFragment.CONTENT_URI.buildUpon().appendPath(Long.toString(audioFragmentId)).build();
        return AudioFragmentDao.getInstance().getFragment(getContentResolver(), itemUri);
    }

    private void play(AudioFragment fragment) {
        MediaPlayer player = application.getPlayer(fragment.getId());
        try {
            player.reset();
            player.setDataSource(fragment.getPath());
            player.prepare();
            player.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
}
