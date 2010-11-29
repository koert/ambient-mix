package net.kazed.ambient;

import java.util.List;

import net.kazed.ambient.database.ManagedAudioFragmentDao;
import net.kazed.ambient.fragment.FragmentListItemView;
import net.kazed.ambient.help.HelpContentsActivity;
import net.kazed.ambient.service.FragmentPlayer;
import net.kazed.android.inject.InjectedActivity;
import net.kazed.android.inject.Resource;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class Player extends InjectedActivity {

    private static final int MENU_ADD_FRAGMENT = Menu.FIRST;
    private static final int MENU_HELP = MENU_ADD_FRAGMENT + 1;
    private static final int NEW_FRAGMENT = 2;
    private static final int FRAGMENT_DIALOG = 1;
    private static final int EDIT = 1;
    
	private FragmentPlayer fragmentPlayer;
    private ManagedAudioFragmentDao audioFragmentDao;
    private LinearLayout fragmentList;
	private FragmentListItemView[] views;
	private FragmentListItemView.SelectionListener selectionListener;
	private AudioFragment selectedFragment;
    
    /**
     * @param fragmentPlayer Player to play fragments.
     */
    @Resource("fragmentPlayer")
    public void setFragmentPlayer(FragmentPlayer fragmentPlayer) {
    	this.fragmentPlayer = fragmentPlayer;
    }

    /**
     * @param audioFragmentDao DAO for AudioFragment.
     */
    @Resource("audioFragmentDao")
    public void setAudioFragmentDao(ManagedAudioFragmentDao audioFragmentDao) {
    	this.audioFragmentDao = audioFragmentDao;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.player);
        fragmentList = (LinearLayout) findViewById(R.id.fragment_list);
        
        selectionListener = new FragmentListItemView.SelectionListener() {
			public void select(AudioFragment audioFragment) {
				selectedFragment = audioFragment;
				showDialog(FRAGMENT_DIALOG);
			}
		};
		
		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey("selectedFragment")) {
				selectedFragment = audioFragmentDao.getFragment(savedInstanceState.getLong("selectedFragment"));
			}
		}
    }
    
    @Override
	protected void onResume() {
		super.onResume();
		rebuildFragmentList();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (selectedFragment != null) {
			outState.putLong("selectedFragment", selectedFragment.getId());
		}
	}

	private void rebuildFragmentList() {
		fragmentList.removeAllViews();
        List<AudioFragment> audioFragments = audioFragmentDao.getAllFragments();
		views = new FragmentListItemView[audioFragments.size()];
		int position = 0;
        for (AudioFragment audioFragment : audioFragments) {
        	FragmentListItemView view = new FragmentListItemView(this, R.layout.player_fragment_view, fragmentPlayer);
			view.update(audioFragment);
			view.setSelectionListener(selectionListener);
			view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			fragmentList.addView(view);
			views[position] = view;
			position++;
		}
	}

	@Override
	protected void onPause() {
		for (FragmentListItemView view : views) {
			audioFragmentDao.update(this, view.getAudioFragment());
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		for (FragmentListItemView view : views) {
			view.onDestroy();
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case FRAGMENT_DIALOG:
			dialog = createFragmentDialog();
			break;
		}
		return dialog;
	}

	private Dialog createFragmentDialog() {
		Dialog dialog = null;
		if (selectedFragment != null) {
			dialog = new AlertDialog.Builder(this)
				.setItems(R.array.fragment_menu_items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int which) {
				    	switch(which) {
				    	case 0:
				            Uri itemUri = AudioFragment.CONTENT_URI.buildUpon().appendPath(Long.toString(selectedFragment.getId())).build();
				            Intent fragmentIntent = new Intent(Intent.ACTION_EDIT, itemUri);
				            startActivityForResult(fragmentIntent, EDIT);
				    		break;
				    	case 1:
				    		audioFragmentDao.delete(Player.this, selectedFragment);
				    		rebuildFragmentList();
				    		break;
				    	}
				    }
				}).create();
		}
		return dialog;
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_ADD_FRAGMENT, 0, getResources().getString(R.string.menu_add_fragment))
        	.setIcon(android.R.drawable.ic_menu_add).setAlphabeticShortcut('a');
        menu.add(0, MENU_HELP, 0, getResources().getString(R.string.menu_help))
    		.setIcon(android.R.drawable.ic_menu_help).setAlphabeticShortcut('h');

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_ADD_FRAGMENT:
            Intent fragmentIntent = new Intent(Intent.ACTION_INSERT, AudioFragment.CONTENT_URI);
            startActivityForResult(fragmentIntent, NEW_FRAGMENT);
            break;
        case MENU_HELP:
            startActivity(new Intent(this, HelpContentsActivity.class));
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
        case EDIT:
    		rebuildFragmentList();
            break;
        }
    }
    

}