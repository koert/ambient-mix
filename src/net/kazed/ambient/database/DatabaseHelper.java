package net.kazed.ambient.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    
    public static final AudioFragmentTable AUDIO_FRAGMENT = new AudioFragmentTable();

    /**
     * Constructor.
     * @param context Android context.
     */
    public DatabaseHelper(Context context) {
        super(context, Provider.DATABASE_NAME, null, DatabaseHelper.DATABASE_VERSION);
    }
	
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + AudioFragmentTable.TABLE + " ("
        		+ AudioFragmentTable.COLUMN_ID + " INTEGER PRIMARY KEY,"
                + AudioFragmentTable.COLUMN_PATH + " TEXT," 
                + AudioFragmentTable.COLUMN_NAME + " TEXT," 
                + AudioFragmentTable.COLUMN_LOOP + " TEXT" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade: " + oldVersion + " - " + newVersion);
        switch (oldVersion) {
//        case 10:
//            db.execSQL("ALTER TABLE " + DatabaseHelper.TABLE_TASK + " ADD COLUMN " + Task.Column.TYPE + " INTEGER");
        }
    }
    
//    static {
//        AUDIO_FRAGMENT_PROJECT_MAP = new HashMap<String, String>();
//        AUDIO_FRAGMENT_PROJECT_MAP.put(AudioFragmentColumn.ID, DatabaseHelper.TABLE_AUDIO_FRAGMENT + "." + AudioFragmentColumn.ID);
//        AUDIO_FRAGMENT_PROJECT_MAP.put(AudioFragmentColumn.PATH, DatabaseHelper.TABLE_AUDIO_FRAGMENT + "." + AudioFragmentColumn.PATH);
//        AUDIO_FRAGMENT_PROJECT_MAP.put(AudioFragmentColumn.FAVORITE, DatabaseHelper.TABLE_AUDIO_FRAGMENT + "." + AudioFragmentColumn.FAVORITE);
//    }
    
}
