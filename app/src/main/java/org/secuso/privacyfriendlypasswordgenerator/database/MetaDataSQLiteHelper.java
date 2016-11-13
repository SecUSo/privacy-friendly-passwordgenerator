package org.secuso.privacyfriendlypasswordgenerator.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by yonjuni on 17.06.16.
 */

public class MetaDataSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "MetadataDB";

    private static final String TABLE_METADATA = "metadata";

    private static final String KEY_ID = "id";
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
                KEY_DOMAIN + " TEXT NOT NULL," +
                KEY_LENGTH + " INTEGER," +
                KEY_HAS_NUMBERS + " INTEGER," +
                KEY_HAS_SYMBOLS + " INTEGER," +
                KEY_HAS_LETTERS + " INTEGER," +
                ITERATION + " INTEGER);";

        sqLiteDatabase.execSQL(CREATE_METADATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

//    public void addMetaData(MetaData metaData) {
//
//    }
//
//    public List<MetaData> getAllmetaData() {
//
//    }
//
//    public int updateMetaData (MetaData metaData) {
//
//    }
//
//    public void deleteMetaData (MetaData metaData) {
//
//    }
//
//    public MetaData getMetaData(int id) {
//
//    }

}
