package net.kazed.ambient.database;

import net.kazed.ambient.AudioFragment;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * DAO for AudioFragment records.
 */
public class AudioFragmentDao {

    private static AudioFragmentDao instance;

    private AudioFragmentTable table = new AudioFragmentTable();

    public static AudioFragmentDao getInstance() {
        if (instance == null) {
            instance = new AudioFragmentDao();
        }
        return instance;
    }
    
    /**
     * @return Table definition.
     */
    public AudioFragmentTable getTable() {
        return table;
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
     * @param activity Activity.
     * @param itemUri URI of record.
     * @return Record query.
     */
    public AudioFragment getFragment(ContentResolver contentResolver, Uri itemUri) {
        AudioFragment audioFragment = null;
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(itemUri, table.getColumnNames(), null, null, null);
            cursor.moveToFirst();
            audioFragment = table.extract(cursor);
        } finally {
            cursor.close();
        }
        return audioFragment;
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
