package org.secuso.privacyfriendlypasswordgenerator.backup;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.JsonReader;

import androidx.annotation.NonNull;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.jetbrains.annotations.NotNull;
import org.secuso.privacyfriendlybackup.api.backup.DatabaseUtil;
import org.secuso.privacyfriendlybackup.api.backup.FileUtil;
import org.secuso.privacyfriendlybackup.api.pfa.IBackupRestorer;
import org.secuso.privacyfriendlypasswordgenerator.PassGenApplication;
import org.secuso.privacyfriendlypasswordgenerator.tutorial.PrefManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.secuso.privacyfriendlypasswordgenerator.database.MetaDataSQLiteHelper.DATABASE_NAME;
import static org.secuso.privacyfriendlypasswordgenerator.database.MetaDataSQLiteHelper.DATABASE_VERSION;

public class BackupRestorer implements IBackupRestorer {


    @Override
    public boolean restoreBackup(@NotNull Context context, @NotNull InputStream restoreData) {
        try {
            InputStreamReader isReader = new InputStreamReader(restoreData);
            JsonReader reader = new JsonReader(isReader);

            reader.beginObject();

            while(reader.hasNext()) {
                String type = reader.nextName();

                switch(type) {
                    case "database":
                        readDatabase(reader, context);
                        break;
                    case "preferences":
                        readPreferences(reader, context);
                        break;
                    default:
                        throw new RuntimeException("Can not parse type "+type);
                }
            }

            reader.endObject();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void readPreferences(@NonNull JsonReader reader, @NonNull Context context) throws IOException {
        reader.beginObject();

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        while(reader.hasNext()) {
            String name = reader.nextName();

            switch(name) {
                case "bindToDevice_enabled":
                case "clipboard_enabled":
                    editor.putBoolean(name, reader.nextBoolean());
                    break;
                case "hash_algorithm":
                case "hash_iterations":
                    editor.putString(name, reader.nextString());
                    break;
                default:
                    throw new RuntimeException("Unknown preference "+name);
            }
        }

        editor.commit();

        PrefManager pref2 = new PrefManager(context);
        pref2.setFirstTimeLaunch(false);

        reader.endObject();
    }

    private void readDatabase(JsonReader reader, Context context) throws IOException {
        reader.beginObject();

        String n1 = reader.nextName();
        if(!n1.equals("version")) {
            throw new RuntimeException("Unknown value " + n1);
        }
        int version = reader.nextInt();

        String n2 = reader.nextName();
        if(!n2.equals("content")) {
            throw new RuntimeException("Unknown value " + n2);
        }

        SupportSQLiteDatabase db = DatabaseUtil.getSupportSQLiteOpenHelper(context, "restoreDatabase", version).getWritableDatabase();
        db.beginTransaction();
        db.setVersion(version);

        DatabaseUtil.readDatabaseContent(reader, db);

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

        reader.endObject();

        // copy file to correct location
        File databaseFile = context.getDatabasePath("restoreDatabase");
        FileUtil.copyFile(databaseFile, context.getDatabasePath(DATABASE_NAME));
        databaseFile.delete();
    }

}
