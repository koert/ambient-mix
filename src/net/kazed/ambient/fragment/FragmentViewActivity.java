package net.kazed.ambient.fragment;

import net.kazed.ambient.AmbientPlayerApplication;
import net.kazed.ambient.AudioFragment;
import net.kazed.ambient.R;
import net.kazed.ambient.database.AudioFragmentDao;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * Display document info.
 */
public class FragmentViewActivity extends Activity {
    
    private static final String TAG = "FragmentViewActivity";
    
    private static final int MENU_EDIT = Menu.FIRST;
    private static final int EDIT = 1;

    private AudioFragmentDao.RecordQuery query;
    private Uri itemUri;
    private TextView name;
    private TextView fileName;
    private CheckBox repeat;
    private Button playButton;
    private AudioFragment fragment;
    
    private AmbientPlayerApplication application;
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.fragment_view);
        setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
        itemUri = getIntent().getData();
        name = (TextView) findViewById(R.id.fragment_name);
        fileName = (TextView) findViewById(R.id.fragment_file_name);
        repeat = (CheckBox) findViewById(R.id.repeat);
        
        repeat.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton button, boolean value) {
                fragment.setRepeat(value);
                application.getFragmentPlayer().updateFragment(fragment);
            }
        });
        
        playButton = (Button) findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                application.getFragmentPlayer().play(fragment);
            }
         });
        
        query = AudioFragmentDao.getInstance().queryFragment(this, itemUri);
        
        application = (AmbientPlayerApplication) getApplication();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume");

        fragment = query.getRecord();
        populateFields(fragment);
        
        super.onResume();
    }

    @Override
    protected void onPause() {
        AudioFragmentDao.getInstance().update(this, fragment);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String menuName = getResources().getString(R.string.menu_add_fragment);
        MenuItem item = menu.add(0, MENU_EDIT, 0, menuName).setIcon(android.R.drawable.ic_menu_edit);
        item.setAlphabeticShortcut('e');

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_EDIT:
            Intent fragmentIntent = new Intent(Intent.ACTION_EDIT, itemUri);
            startActivityForResult(fragmentIntent, EDIT);
            break;
        }
        return super.onOptionsItemSelected(item);
    }

   /**
    * Populate UI components with data from fragment.
    * @param fragment Retrieved fragment.
    */
   private void populateFields(AudioFragment fragment) {
      name.setText(fragment.getName());
      fileName.setText(fragment.getPath());
      repeat.setChecked(fragment.isRepeat());
   }

}
