package org.secuso.privacyfriendlypasswordgenerator.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yonjuni on 17.06.16.
 * Structure based on http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
 * accessed at 16th June 2016
 */

public class MetaDataSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "MetadataDB";

    private static final String TABLE_METADATA = "metadata";

    private static final String KEY_ID = "id";
    private static final String KEY_POSITION_ID = "positionId";
    private static final String KEY_DOMAIN = "domain";
    private static final String KEY_LENGTH = "length";
    private static final String KEY_HAS_NUMBERS = "hasNumbers";
    private static final String KEY_HAS_SYMBOLS = "hasSymbols";
    private static final String KEY_HAS_LETTERS = "hasLetters";

    private static final String ITERATION = "interation";

    public MetaDataSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_METADATA_TABLE = "CREATE TABLE " + TABLE_METADATA +
                "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_POSITION_ID + " INTEGER," +
                KEY_DOMAIN + " TEXT NOT NULL," +
                KEY_LENGTH + " INTEGER," +
                KEY_HAS_NUMBERS + " INTEGER," +
                KEY_HAS_SYMBOLS + " INTEGER," +
                KEY_HAS_LETTERS + " INTEGER," +
                ITERATION + " INTEGER);";

        sqLiteDatabase.execSQL(CREATE_METADATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_METADATA);

        onCreate(sqLiteDatabase);
    }

    public void addMetaData(MetaData metaData) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_POSITION_ID, metaData.getPOSITIONID());
        values.put(KEY_DOMAIN, metaData.getDOMAIN());
        values.put(KEY_LENGTH, metaData.getLENGTH());
        values.put(KEY_HAS_NUMBERS, metaData.getHAS_NUMBERS());
        values.put(KEY_HAS_SYMBOLS, metaData.getHAS_SYMBOLS());
        values.put(KEY_HAS_LETTERS, metaData.getHAS_LETTERS());
        values.put(ITERATION, metaData.getITERATION());

        database.insert(TABLE_METADATA, null, values);
        database.close();
    }

    public List<MetaData> getAllmetaData() {
        List<MetaData> metaDataList = new ArrayList<MetaData>();

        String selectQuery = "SELECT  * FROM " + TABLE_METADATA;

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        MetaData metaData = null;

        if (cursor.moveToFirst()) {
            do {
                metaData = new MetaData();
                metaData.setID(Integer.parseInt(cursor.getString(0)));
                metaData.setPOSITIONID(Integer.parseInt(cursor.getString(1)));
                metaData.setDOMAIN(cursor.getString(2));
                metaData.setLENGTH(Integer.parseInt(cursor.getString(3)));
                metaData.setHAS_NUMBERS(Integer.parseInt(cursor.getString(4)));
                metaData.setHAS_SYMBOLS(Integer.parseInt(cursor.getString(5)));
                metaData.setHAS_LETTERS(Integer.parseInt(cursor.getString(6)));
                metaData.setITERATION(Integer.parseInt(cursor.getString(7)));

                metaDataList.add(metaData);
            } while (cursor.moveToNext());
        }

        return metaDataList;
    }

    public int updateMetaData(MetaData metaData) {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_POSITION_ID, metaData.getPOSITIONID());
        values.put(KEY_DOMAIN, metaData.getDOMAIN());
        values.put(KEY_LENGTH, metaData.getLENGTH());
        values.put(KEY_HAS_NUMBERS, metaData.getHAS_NUMBERS());
        values.put(KEY_HAS_SYMBOLS, metaData.getHAS_SYMBOLS());
        values.put(KEY_HAS_LETTERS, metaData.getHAS_LETTERS());
        values.put(ITERATION, metaData.getITERATION());

        return database.update(TABLE_METADATA, values, KEY_ID + " = ?",
                new String[] { String.valueOf(metaData.getID()) });
    }

    public void deleteMetaData(MetaData metaData) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_METADATA, KEY_ID + " = ?",
                new String[] { Integer.toString(metaData.getID()) });
        database.close();
    }

    public MetaData getMetaData(int id) {
        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.query(TABLE_METADATA, new String[]{KEY_ID, KEY_POSITION_ID,
                        KEY_DOMAIN, KEY_LENGTH, KEY_HAS_NUMBERS, KEY_HAS_SYMBOLS, KEY_HAS_LETTERS, ITERATION}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        MetaData metaData = new MetaData();

        if( cursor != null && cursor.moveToFirst() ){
            metaData.setID(Integer.parseInt(cursor.getString(0)));
            metaData.setPOSITIONID(Integer.parseInt(cursor.getString(1)));
            metaData.setDOMAIN(cursor.getString(2));
            metaData.setLENGTH(Integer.parseInt(cursor.getString(3)));
            metaData.setHAS_NUMBERS(Integer.parseInt(cursor.getString(4)));
            metaData.setHAS_SYMBOLS(Integer.parseInt(cursor.getString(5)));
            metaData.setHAS_LETTERS(Integer.parseInt(cursor.getString(6)));
            metaData.setITERATION(Integer.parseInt(cursor.getString(7)));
            cursor.close();
        }

        return metaData;

    }

}
