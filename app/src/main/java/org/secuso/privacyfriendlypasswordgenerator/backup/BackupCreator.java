package org.secuso.privacyfriendlypasswordgenerator.backup;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.JsonWriter;
import android.util.Log;

import androidx.sqlite.db.SupportSQLiteDatabase;

import org.jetbrains.annotations.NotNull;
import org.secuso.privacyfriendlybackup.api.backup.DatabaseUtil;
import org.secuso.privacyfriendlybackup.api.backup.PreferenceUtil;
import org.secuso.privacyfriendlybackup.api.pfa.IBackupCreator;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.secuso.privacyfriendlypasswordgenerator.database.MetaDataSQLiteHelper.DATABASE_NAME;
import static org.secuso.privacyfriendlypasswordgenerator.database.MetaDataSQLiteHelper.DATABASE_VERSION;

public class BackupCreator implements IBackupCreator {

    @Override
    public void writeBackup(@NotNull Context context, @NotNull OutputStream outputStream) {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, UTF_8);
        JsonWriter writer = new JsonWriter(outputStreamWriter);
        writer.setIndent("");

        try {
            writer.beginObject();

            SupportSQLiteDatabase dataBase = DatabaseUtil.getSupportSQLiteOpenHelper(context, DATABASE_NAME, DATABASE_VERSION).getReadableDatabase();

            writer.name("database");
            DatabaseUtil.writeDatabase(writer, dataBase);
            dataBase.close();

            writer.name("preferences");
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
            PreferenceUtil.writePreferences(writer, pref);

            writer.endObject();
            writer.close();
        } catch (Exception e) {
            Log.e("PFA BackupCreator", "Error occurred", e);
            e.printStackTrace();
        }
    }


}
