package org.secuso.privacyfriendlypasswordgenerator.activities;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import org.secuso.privacyfriendlypasswordgenerator.MetaDataAdapter;
import org.secuso.privacyfriendlypasswordgenerator.R;
import org.secuso.privacyfriendlypasswordgenerator.database.MetaData;
import org.secuso.privacyfriendlypasswordgenerator.database.MetaDataSQLiteHelper;

import java.util.List;

/**
 * Code for displaying cards according to the tutorial from https://code.tutsplus.com/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465
 * accessed on 20th June 2016
 */

public class MainActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private MetaDataAdapter adapter;
    private List<MetaData> metadatalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        MetaDataSQLiteHelper database = new MetaDataSQLiteHelper(this);

        /**
         * CRUD Operations
         * */
        // Inserting Contacts
//        Log.d("Insert: ", "Inserting ..");
//        database.addMetaData(new MetaData(1, "google.de", 13, 0, 0, 0, 1));
//        database.addMetaData(new MetaData(1, "google.de", 14, 0, 0, 0, 1));
//        database.addMetaData(new MetaData(1, "google.de", 14, 0, 0, 0, 1));
//        database.addMetaData(new MetaData(1, "google.de", 16, 0, 0, 0, 1));

        // Reading all contacts
        Log.d("Reading: ", "Reading all data..");
        List<MetaData> metadatalist = database.getAllmetaData();

//        for (MetaData cn : contacts) {
//            String log = "Id: "+cn.getID()+" ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber();
//            // Writing Contacts to log
//            Log.d("Name: ", log);

        adapter = new MetaDataAdapter(metadatalist);

        recyclerView.setAdapter(adapter);

        FloatingActionButton addFab = (FloatingActionButton) findViewById(R.id.add_fab);
        if (addFab != null) {


            addFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    AddMetaDataDialog addMetaDataDialog = new AddMetaDataDialog();
                    addMetaDataDialog.show(fragmentManager, "AddMetaDataDialog");
                }
            });

        }

        overridePendingTransition(0, 0);
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_example;
    }


//    public static class WelcomeDialog extends DialogFragment {
//
//        @Override
//        public void onAttach(Activity activity) {
//            super.onAttach(activity);
//        }
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//            LayoutInflater i = getActivity().getLayoutInflater();
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setView(i.inflate(R.layout.welcome_dialog, null));
//            builder.setIcon(R.mipmap.icon);
//            builder.setTitle(getActivity().getString(R.string.welcome));
//            builder.setPositiveButton(getActivity().getString(R.string.okay), null);
//            builder.setNegativeButton(getActivity().getString(R.string.viewhelp), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    ((MainActivity)getActivity()).goToNavigationItem(R.id.nav_help);
//                }
//            });
//
//            return builder.create();
//        }
//    }

    public static class AddMetaDataDialog extends DialogFragment {

        Activity activity;

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            this.activity = activity;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            LayoutInflater i = getActivity().getLayoutInflater();
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(i.inflate(R.layout.dialog_add_metadata, null));
            builder.setIcon(R.mipmap.ic_drawer);
            builder.setTitle(getActivity().getString(R.string.add_metadata_heading));
            builder.setPositiveButton(getActivity().getString(R.string.add), null);
            builder.setNegativeButton(getActivity().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((MainActivity) getActivity()).goToNavigationItem(R.id.nav_help);
                }
            });

            return builder.create();
        }
    }

}
