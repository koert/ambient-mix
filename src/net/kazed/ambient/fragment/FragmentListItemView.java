package net.kazed.ambient.fragment;

import net.kazed.ambient.AudioFragment;
import net.kazed.ambient.R;
import net.kazed.ambient.service.FragmentPlayer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FragmentListItemView extends LinearLayout {
	
	private FragmentPlayer fragmentPlayer;
    private TextView name;
    private ToggleButton repeat;
    private ToggleButton playButton;
    private Button menuButton;
    private AudioFragment fragment;
    private SelectionListener selectionListener;

	public FragmentListItemView(Context androidContext, int viewResourceId, FragmentPlayer player) {
		super(androidContext);
		this.fragmentPlayer = player;
		
        LayoutInflater vi = (LayoutInflater) androidContext.
        		getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        vi.inflate(viewResourceId, this, true); 

        name = (TextView) findViewById(R.id.fragment_name);
        repeat = (ToggleButton) findViewById(R.id.repeat);
        repeat.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton button, boolean value) {
                fragment.setRepeat(value);
                fragmentPlayer.updateFragment(fragment);
            }
        });
        
        playButton = (ToggleButton) findViewById(R.id.play_button);
        playButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton button, boolean state) {
				if (state) {
	            	fragmentPlayer.play(fragment);
				} else {
	            	fragmentPlayer.pause(fragment);
				}
			}
        });

        menuButton = (Button) findViewById(R.id.menu_button);
        menuButton.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				if (selectionListener != null) {
					selectionListener.select(fragment);
				}
				
			}
        });
	}
	
	public void onDestroy() {
		fragmentPlayer.removeListener(fragment);
	}

	public void update(AudioFragment audioFragment) {
		if (this.fragment != null) {
			fragmentPlayer.removeListener(fragment);
		}
		this.fragment = audioFragment;
		name.setText(audioFragment.getName());
		repeat.setChecked(audioFragment.isRepeat());
        PlayerListener playerListener = new PlayerListener();
        fragmentPlayer.setListener(fragment, playerListener);
        playButton.setChecked(fragmentPlayer.isPlaying(audioFragment));
	}

	/**
	 * @return Audio fragment.
	 */
	public AudioFragment getAudioFragment() {
		return fragment;
	}
	
	public class PlayerListener implements FragmentPlayer.FragmentListener {

		public void onCompletion(AudioFragment fragment) {
			playButton.setChecked(false);
			fragmentPlayer.removeListener(fragment);
			fragmentPlayer.removePlayer(fragment);
			
//			MediaPlayer player = fragmentPlayer.getPlayer(fragment);
//			player.seekTo(0);
//			player.stop();
//			try {
//				player.prepare();
//			} catch (IllegalStateException e) {
//	            throw new AudioException("Failed to prepare", e);
//			} catch (IOException e) {
//	            throw new AudioException("Failed to prepare", e);
//			}
		}
		
	}
	
	public void setSelectionListener(SelectionListener selectionListener) {
		this.selectionListener = selectionListener;
	}

	public static interface SelectionListener {
		void select(AudioFragment audioFragment);
	}
	
}
