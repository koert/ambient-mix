package net.kazed.ambient.database;

import java.util.ArrayList;
import java.util.List;

import net.kazed.ambient.AudioFragment;
import net.kazed.android.inject.Autowired;
import net.kazed.android.inject.Component;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * DAO for AudioFragment records.
 */
@Component("audioFragmentDao")
public class ManagedAudioFragmentDao {

    private AudioFragmentTable table = new AudioFragmentTable();
    private Context context;
    
    /**
     * @return Table definition.
     */
    public AudioFragmentTable getTable() {
        return table;
    }

    @Autowired
    public void setContext(Context context) {
		this.context = context;
	}

	/**
     * Query for single record.
     * @param activity Activity.
     * @param itemUri URI of record.
     * @return Record query.
     */
    public RecordQuery queryFragment(Activity activity, Uri itemUri) {
        Cursor cursor = activity.managedQuery(itemUri, table.getColumnNames(), null, null, null);
        return new RecordQuery(cursor);
    }
    
    /**
     * Query for single record.
     * @return List of retrieved records.
     */
    public List<AudioFragment> getAllFragments() {
        List<AudioFragment> audioFragments = new ArrayList<AudioFragment>();
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(AudioFragment.CONTENT_URI, table.getColumnNames(), null, null, null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                audioFragments.add(table.extract(cursor));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return audioFragments;
    }
    
    /**
     * Query for single record.
     * @param activity Activity.
     * @param itemUri URI of record.
     * @return Record query.
     */
    public AudioFragment getFragment(Uri itemUri) {
        AudioFragment audioFragment = null;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(itemUri, table.getColumnNames(), null, null, null);
            cursor.moveToFirst();
            audioFragment = table.extract(cursor);
        } finally {
            cursor.close();
        }
        return audioFragment;
    }
    
    /**
     * Query for single record.
     * @param activity Activity.
     * @param itemUri URI of record.
     * @return Record query.
     */
    public AudioFragment getFragment(long id) {
        Uri itemUri = AudioFragment.CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
        return getFragment(itemUri);
    }
    
    /**
     * Save new record.
     * @param activity Activity.
     * @param fragment Audio fragment.
     * @return Created record.
     */
    public Uri save(Activity activity, AudioFragment fragment) {
        ContentValues values = table.getValues(fragment);
        return activity.getContentResolver().insert(AudioFragment.CONTENT_URI, values);
    }
    
    /**
     * Update existing record.
     * @param activity Activity.
     * @param fragment Audio fragment.
     * @return Updated record.
     */
    public Uri update(Activity activity, AudioFragment fragment) {
        ContentValues values = table.getValues(fragment);
        Uri uri = AudioFragment.CONTENT_URI.buildUpon().appendPath(Long.toString(fragment.getId())).build();
        activity.getContentResolver().update(uri, values, null, null);
        return uri;
    }
    
    /**
     * Delete existing record.
     * @param activity Activity.
     * @param fragment Audio fragment.
     * @return Deleted record.
     */
    public Uri delete(Activity activity, AudioFragment fragment) {
        Uri uri = AudioFragment.CONTENT_URI.buildUpon().appendPath(Long.toString(fragment.getId())).build();
        activity.getContentResolver().delete(uri, null, null);
        return uri;
    }
    
    public class RecordQuery {
        private Cursor cursor;
        public RecordQuery(Cursor cursor) {
            this.cursor = cursor;
        }
        
        /**
         * @return Cursor.
         */
        public Cursor getCursor() {
            return cursor;
        }

        /**
         * @return Record from first cursor position.
         */
        public AudioFragment getRecord() {
            cursor.moveToFirst();
            return table.extract(cursor);
        }

        /**
         * @return Record from first cursor position.
         */
        public AudioFragment getRecord(int position) {
            cursor.moveToPosition(position);
            return table.extract(cursor);
        }
    }
}
