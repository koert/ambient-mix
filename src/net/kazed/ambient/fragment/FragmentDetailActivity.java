package net.kazed.ambient.fragment;

import java.io.IOException;

import net.kazed.ambient.AudioFragment;
import net.kazed.ambient.R;
import net.kazed.ambient.database.AudioFragmentDao;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Display document info.
 */
public class FragmentDetailActivity extends Activity {
    
    private static final int FILE_CHOSEN = 1;

    private static final String TAG = "FragmentDetailActivity";

    private AudioFragmentDao.RecordQuery query;
    private Uri itemUri;
    private boolean newRecord;
    private TextView name;
    private TextView fileName;
    private Button selectButton;
    private Button playButton;
    private Button saveButton;
    private Button cancelButton;
    private AudioFragment fragment;
    
    private MediaPlayer mp = new MediaPlayer();


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.fragment_info);
        setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
        itemUri = getIntent().getData();
        newRecord = AudioFragment.CONTENT_URI.equals(itemUri);
        name = (TextView) findViewById(R.id.fragment_name);
        fileName = (TextView) findViewById(R.id.fragment_file_name);
        selectButton = (Button) findViewById(R.id.select_file_button);
        selectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              Intent intent = new Intent(FragmentDetailActivity.this, FileChooserActivity.class);
              startActivityForResult(intent, FILE_CHOSEN);
            }
         });
        
        playButton = (Button) findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                play();
            }
         });
        
        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                save();
                setResult(RESULT_OK);
                finish();
            }
         });
        
        cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
         });
        
        if (newRecord) {
            fragment = new AudioFragment("", "");
        } else {
            query = AudioFragmentDao.getInstance().queryFragment(this, itemUri);
            fragment = query.getRecord();
        }
        if (icicle == null) {
            populateFields(fragment);
        } else {
        	fileName.setText(icicle.getString("fileName"));
        }
    }

    @Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("name", name.getText().toString());
		outState.putString("fileName", fileName.getText().toString());
	}

	@Override
    protected void onResume() {
        Log.d(TAG, "onResume+");

        super.onResume();
    }

   /**
    * Populate UI components with data from fragment.
    * @param fragment Retrieved fragment.
    */
   private void populateFields(AudioFragment fragment) {
	  name.setText(fragment.getName());
      fileName.setText(fragment.getPath());
   }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if (resultCode == RESULT_OK) {
            switch(requestCode) {
            case FILE_CHOSEN:
                Uri fileUri = data.getData();
                String path = fileUri.getPath();
                fileName.setText(fileUri.getPath());
                fragment = new AudioFragment(fragment.getId(), path, fragment.getName());
                break;
            }
    	}
    }
    
    private Uri save() {
        Uri uri = null;
        AudioFragment newFragment = new AudioFragment(fragment.getId(), fileName.getText().toString(), name.getText().toString());
        if (newRecord) {
            uri = AudioFragmentDao.getInstance().save(this, newFragment);
        } else {
            uri = AudioFragmentDao.getInstance().update(this, newFragment);
        }
        return uri;
    }
    
    private void play() {
        try {
            mp.reset();
            mp.setDataSource(fragment.getPath());
            mp.prepare();
            mp.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
