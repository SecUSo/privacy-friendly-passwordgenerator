package org.secuso.privacyfriendlypasswordgenerator.backup;

import static org.secuso.privacyfriendlypasswordgenerator.database.MetaDataSQLiteHelper.DATABASE_NAME;
import static org.secuso.privacyfriendlypasswordgenerator.database.MetaDataSQLiteHelper.DATABASE_VERSION;
import static java.nio.charset.StandardCharsets.UTF_8;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.JsonWriter;
import android.util.Log;

import androidx.sqlite.db.SupportSQLiteDatabase;

import org.jetbrains.annotations.NotNull;
import org.secuso.privacyfriendlybackup.api.backup.DatabaseUtil;
import org.secuso.privacyfriendlybackup.api.backup.PreferenceUtil;
import org.secuso.privacyfriendlybackup.api.pfa.IBackupCreator;
import org.secuso.privacyfriendlypasswordgenerator.helpers.SaltHelper;
import org.secuso.privacyfriendlypasswordgenerator.tutorial.PrefManager;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class BackupCreator implements IBackupCreator {

    @Override
    public boolean writeBackup(@NotNull Context context, @NotNull OutputStream outputStream) {
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

            writer.name("pfa_pw_generator_preferences");
            SharedPreferences pfa_pref = new PrefManager(context).getPref();
            PreferenceUtil.writePreferences(writer, pfa_pref);

            writer.name("salt_preferences");
            SharedPreferences salt_pref = new SaltHelper.EncryptedSaltPreference().initPreference(context);
            PreferenceUtil.writePreferences(writer, salt_pref);

            writer.endObject();
            writer.close();
        } catch (Exception e) {
            Log.e("PFA BackupCreator", "Error occurred", e);
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
