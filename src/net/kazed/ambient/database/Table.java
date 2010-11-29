package net.kazed.ambient.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

/**
 * Table definition.
 */
public abstract class Table {

    public static final String PACKAGE = Table.class.getPackage().getName();

    private String tableName;
    private String nullColumnHack;
    private Map<String, String> projectionMap = new HashMap<String, String>();
    private List<String> columnNames = new ArrayList<String>();
    private Map<String, Integer> nameToPosition;

    /**
     * Constructor.
     * @param tableName Table name.
     * @param nullColumnHack Name of a column that may contain a null value.
     */
    public Table(String tableName, String nullColumnHack) {
        super();
        this.tableName = tableName;
        this.nullColumnHack = nullColumnHack;
        nameToPosition = new HashMap<String, Integer>();
    }
    
    /**
     * @return Name of table.
     */
    public String getTableName() {
        return tableName;
    }
    
    /**
     * Add column
     * @param columnName Name of column.
     */
    public void addColumn(String columnName) {
        projectionMap.put(columnName, tableName + "." + columnName);
        nameToPosition.put(columnName, columnNames.size());
        columnNames.add(columnName);
    }
    
    /**
     * Add column
     * @param columnName Name of column.
     */
    public void addColumn(String tableName, String columnName) {
        projectionMap.put(columnName, tableName + "." + columnName);
        nameToPosition.put(columnName, columnNames.size());
        columnNames.add(columnName);
    }
    
    /**
     * Add column
     * @param columnName Name of column.
     */
    public void addColumn(String tableName, String columnName, String columnAlias) {
        projectionMap.put(columnAlias, tableName + "." + columnName);
        nameToPosition.put(columnAlias, columnNames.size());
        columnNames.add(columnAlias);
    }
    
    /**
     * @return List of column names.
     */
    public String[] getColumnNames() {
        String[] names = new String[columnNames.size()];
        return (String[]) columnNames.toArray(names);
    }
    
    /**
     * Get Integer value of field.
     * @param cursor Database cursor.
     * @param fieldName Name of field.
     * @return Retrieved value, null if field value is null.
     */
    public Integer getInteger(Cursor cursor, String fieldName) {
       Integer result = null;
       
       int columnIndex = nameToPosition.get(fieldName);
       if (!cursor.isNull(columnIndex)) {
          result = cursor.getInt(columnIndex);
       }
       return result;
    }
    
    /**
     * Get Integer value of field.
     * @param cursor Database cursor.
     * @param fieldName Name of field.
     * @param defaultValue Default value if database value is null.
     * @return Retrieved value, defaultValue if field value is null.
     */
    public Integer getInteger(Cursor cursor, String fieldName, int defaultValue) {
       Integer result = null;
       
       int columnIndex = nameToPosition.get(fieldName);
       if (cursor.isNull(columnIndex)) {
           result = defaultValue;
       } else {
           result = cursor.getInt(columnIndex);
       }
       return result;
    }
    
    /**
     * Get Long value of field.
     * @param cursor Database cursor.
     * @param fieldName Name of field.
     * @return Retrieved value, null if field value is null.
     */
    public Long getLong(Cursor cursor, String fieldName) {
        Long result = null;
       
       int columnIndex = nameToPosition.get(fieldName);
       if (!cursor.isNull(columnIndex)) {
          result = cursor.getLong(columnIndex);
       }
       return result;
    }
    
    /**
     * Get String value of field.
     * @param cursor Database cursor.
     * @param fieldName Name of field.
     * @param defaultValue Default value.
     * @return Retrieved value, defaultValue if field value is null.
     */
    public String getString(Cursor cursor, String fieldName, String defaultValue) {
       String result = null;
       
       int columnIndex = nameToPosition.get(fieldName);
       if (!cursor.isNull(columnIndex)) {
          result = cursor.getString(columnIndex);
       } else {
          result = defaultValue;
       }
       return result;
    }

    /**
     * Get String value of field.
     * @param cursor Database cursor.
     * @param fieldName Name of field.
     * @return Retrieved value, null if field value is null.
     */
    public String getString(Cursor cursor, String fieldName) {
       String result = null;
       
       int columnIndex = nameToPosition.get(fieldName);
       if (!cursor.isNull(columnIndex)) {
          result = cursor.getString(columnIndex);
       }
       return result;
    }

    /**
     * Query database.
     * @param db Database.
     * @param uri Item URI.
     * @param projection Selected columns.
     * @param selection Where clause.
     * @param selectionArgs Where clause.
     * @param orderByClause
     * @return
     */
    public Cursor query(SQLiteDatabase db, Uri uri, String[] projection, String selection,
                    String[] selectionArgs, String orderByClause) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(tableName);
        qb.setProjectionMap(projectionMap);
        return qb.query(db, projection, selection, selectionArgs, null, null, orderByClause);
    }
    
    /**
     * Query database.
     * @param db Database.
     * @param uri Item URI.
     * @param projection Selected columns.
     * @param selection Where clause.
     * @param selectionArgs Where clause.
     * @param orderByClause
     * @return
     */
    public Cursor queryItem(SQLiteDatabase db, Uri uri, String[] projection, String selection,
                    String[] selectionArgs, String orderByClause) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(tableName);
        qb.setProjectionMap(projectionMap);
        String where = null;
        String[] whereArgs = null;
        String idSegment = uri.getLastPathSegment();
        if (selection != null && selection.length() > 0) {
            where = selection + " and ";
        }
        if (selectionArgs == null) {
            whereArgs = new String[1];
        } else {
            whereArgs = new String[selectionArgs.length + 1];
            for (int i = 0; i < selectionArgs.length; i++) {
                whereArgs[i] = selectionArgs[i];
            }
        }
        where = BaseColumns._ID + " = ?";
        whereArgs[whereArgs.length-1] = idSegment;
        return qb.query(db, projection, where, whereArgs, null, null, orderByClause);
    }
    
    /**
     * Insert values into db.
     * @param db Database.
     * @param values Values of new record.
     * @return ID of created record.
     */
    public long insert(SQLiteDatabase db, ContentValues values) {
        return db.insert(tableName, nullColumnHack, values);
    }

    /**
     * Insert values into db.
     * @param db Database.
     * @param values Values of new record.
     */
    public void delete(SQLiteDatabase db, Uri uri) {
    	String whereClause = BaseColumns._ID + " = ?";
        String idSegment = uri.getLastPathSegment();
        db.delete(tableName, whereClause, new String[] {idSegment});
    }


}
