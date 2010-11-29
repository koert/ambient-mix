package net.kazed.ambient.database;

import java.util.Arrays;

import net.kazed.ambient.AmbientPlayer;
import net.kazed.ambient.AudioFragment;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Provides access to database for all data.
 */
public class Provider extends ContentProvider {
    
    public static final String BACKUP_SAVE_TO_SD_CARD = "saveToSdCard";
    public static final String BACKUP_IMPORT_FROM_SD_CARD = "importFromSdCard";
    public static final String DATABASE_NAME = "ambientplayer.db";

    private static final String TAG = "Provider";
	
    private static final int AUDIO_FRAGMENTS = 1;
    private static final int AUDIO_FRAGMENT_ID = 2;
    
    private static final UriMatcher URI_MATCHER;

    private DatabaseHelper dbHelper;
    
    @Override
    public boolean onCreate() {
    	Log.i(TAG, "+onCreate");
    	dbHelper = new DatabaseHelper(getContext());
        return true;
    }
    
    @Override
    public String getType(Uri uri) {
       String type = null;
       int match = URI_MATCHER.match(uri);
        switch (match) {
        case AUDIO_FRAGMENTS:
           type = AudioFragment.CONTENT_TYPE;
           break;
        case AUDIO_FRAGMENT_ID:
           type = AudioFragment.CONTENT_ITEM_TYPE;
           break;
        default:
            throw new IllegalArgumentException("Unknown Uri " + uri);
        }
        return type;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sort) {
        Cursor cursor = null;
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        switch (URI_MATCHER.match(uri)) {
        case AUDIO_FRAGMENTS:
            cursor = DatabaseHelper.AUDIO_FRAGMENT.query(db, uri, projection, selection, selectionArgs, sort);
//            qb.setTables(DatabaseHelper.TABLE_AUDIO_FRAGMENT);
//            qb.setProjectionMap(DatabaseHelper.TASK_LIST_PROJECT_MAP);
//            cursor = executeQuery(db, qb, uri, projection, selection, selectionArgs, sort, Task.DEFAULT_SORT_ORDER);
            break;
        case AUDIO_FRAGMENT_ID:
            cursor = DatabaseHelper.AUDIO_FRAGMENT.queryItem(db, uri, projection, selection, selectionArgs, sort);
//            qb.setTables(DatabaseHelper.TABLE_AUDIO_FRAGMENT);
//            qb.setProjectionMap(DatabaseHelper.TASK_LIST_PROJECT_MAP);
//            qb.appendWhere(DatabaseHelper.TABLE_AUDIO_FRAGMENT + "._id=" + uri.getPathSegments().get(1));
//            cursor = executeQuery(db, qb, uri, projection, selection, selectionArgs, sort, Task.DEFAULT_SORT_ORDER);
            break;
        default:
            throw new IllegalArgumentException("Unknown URL " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }
    
    private Cursor executeQuery(SQLiteDatabase db, SQLiteQueryBuilder qb, Uri uri, String[] projection, String selection,
                    String[] selectionArgs, String orderBy, String defaultOrderBy) {
        String orderByClause = orderBy;
        if (TextUtils.isEmpty(orderByClause)) {
            orderByClause = defaultOrderBy;
        }
        if (Log.isLoggable(TAG, Log.INFO)) {
            Log.i(TAG, "Executing " + selection + " with args " + Arrays.toString(selectionArgs) +
                    " ORDER BY " + orderBy);
            Log.i(TAG, "Query builder: " + qb.toString());
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderByClause);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }
    
    @Override
    public Uri insert(Uri url, ContentValues initialValues) {
        Uri insertedUri = null;
        long rowID;
        ContentValues values;
        Resources r;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (URI_MATCHER.match(url)) {
        case AUDIO_FRAGMENTS:
//            Long now = Long.valueOf(System.currentTimeMillis());
//            r = getContext().getResources();
//
//            // Make sure that the fields are all set
//            if (! values.containsKey(Task.Column.CREATED_DATE)) {
//                values.put(Task.Column.CREATED_DATE, now);
//            }
//            if (! values.containsKey(Task.Column.MODIFIED_DATE)) {
//                values.put(Task.Column.MODIFIED_DATE, now);
//            }
//            if (! values.containsKey(Task.Column.DESCRIPTION)) {
//                values.put(Task.Column.DESCRIPTION, r.getString(R.string.initial_text));
//            }
//            if (! values.containsKey(Task.Column.DETAILS)) {
//                values.put(Task.Column.DETAILS, "");
//            }
//            if (! values.containsKey(Task.Column.DISPLAY_ORDER)) {
//            	values.put(Task.Column.DISPLAY_ORDER, 0);
//            }
//            if (! values.containsKey(Task.Column.COMPLETE)) {
//            	values.put(Task.Column.COMPLETE, 0);
//            }
//
            rowID = DatabaseHelper.AUDIO_FRAGMENT.insert(db, values);
            insertedUri = AudioFragment.CONTENT_URI.buildUpon().appendPath(Long.toString(rowID)).build();
//            rowID = db.insert(table.getTableName(), DatabaseHelper.TASK_LIST_PROJECT_MAP.get(Task.Column.DESCRIPTION), values);
//            if (rowID > 0) {
//                changeContextTask(db, (int) rowID, 1);
//                insertedUri = ContentUris.withAppendedId(Task.CONTENT_URI, rowID);
//                updateContextNumberOfTasks(db, values.getAsInteger(Task.Column.CONTEXT_ID), 0);
//                SqlTemplate template = new SqlTemplate(db);
//                updateTaskNumberOfTasks(db, template, values.getAsInteger(Task.Column.PARENT_TASK_ID));
//                getContext().getContentResolver().notifyChange(insertedUri, null);
//            } else {
//                getContext().getContentResolver().notifyChange(Task.CONTENT_URI, null);
//            }
//            checkDeferredTasks(db);
        	break;
        	
    	default:
            throw new IllegalArgumentException("Unknown URL " + url);
        }

        return insertedUri;

//        throw new SQLException("Failed to insert row into " + url);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
    	
        int count = 0;
        String segment;
        switch (URI_MATCHER.match(uri)) {
        case AUDIO_FRAGMENTS:
//            count = db.delete(DatabaseHelper.TABLE_TASK, where, whereArgs);
//            getContext().getContentResolver().notifyChange(Task.CONTENT_URI, null);
            break;
        case AUDIO_FRAGMENT_ID:
            DatabaseHelper.AUDIO_FRAGMENT.delete(db, uri);
            break;
        default:
            throw new IllegalArgumentException("Unknown uri " + uri);
        }

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = 0;
        String segment;
        switch (URI_MATCHER.match(uri)) {
        case AUDIO_FRAGMENTS:
//            count = db.update(DatabaseHelper.TABLE_TASK, values, where, whereArgs);
            break;
        case AUDIO_FRAGMENT_ID: {
            count = genericUpdate(db, AudioFragmentTable.TABLE, uri, values, where, whereArgs);
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown URL " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
    
    /**
     * Update a generic update.
     * @param db Database.
     * @param uri Database item URI.
     * @param values Updated values.
     * @param where Where clause.
     * @param whereArgs Where clause arguments.
     * @return Number of rows updated.
     */
    private int genericUpdate(SQLiteDatabase db, String tableName, Uri uri, ContentValues values, String where, String[] whereArgs) {
        String segment = uri.getPathSegments().get(1);
        int count = db.update(tableName, values, "_id=" + segment
                        + (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
        return count;
    }
    
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AmbientPlayer.PACKAGE, "audiofragment", AUDIO_FRAGMENTS);
        URI_MATCHER.addURI(AmbientPlayer.PACKAGE, "audiofragment/#", AUDIO_FRAGMENT_ID);
    }
}
