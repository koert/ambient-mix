package net.kazed.ambient.database;

import net.kazed.ambient.AudioFragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

/**
 * Definition of AudioFragment table.
 */
public class AudioFragmentTable extends Table {
    
    public static final String TABLE = "audio_fragment";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LOOP = "loop";

    /**
     * Constructor.
     */
    public AudioFragmentTable() {
        super(TABLE, COLUMN_NAME);
        addColumn(COLUMN_ID);
        addColumn(COLUMN_PATH);
        addColumn(COLUMN_NAME);
        addColumn(COLUMN_LOOP);
    }
    
    /**
     * Extract object from cursor.
     * @param cursor Query cursor.
     * @return Extracted object.
     */
    public AudioFragment extract(Cursor cursor) {
        long id = getLong(cursor, COLUMN_ID);
        String path = getString(cursor, COLUMN_PATH);
        String name = getString(cursor, COLUMN_NAME);
        boolean loop = Boolean.TRUE.toString().equals(getString(cursor, COLUMN_LOOP));
        AudioFragment fragment = new AudioFragment(id, path, name);
        fragment.setRepeat(loop);
        return fragment;
    }

    /**
     * Insert fragment values into collection.
     * @param fragment Model object.
     * @return Content values.
     */
    public ContentValues getValues(AudioFragment fragment) {
        ContentValues values = new ContentValues();
//        values.put(COLUMN_ID, fragment.getId());
        values.put(COLUMN_PATH, fragment.getPath());
        values.put(COLUMN_NAME, fragment.getName());
        values.put(COLUMN_LOOP, Boolean.toString(fragment.isRepeat()));
        return values;
    }

}
