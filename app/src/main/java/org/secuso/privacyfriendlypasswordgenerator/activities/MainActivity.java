package org.secuso.privacyfriendlypasswordgenerator.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import org.secuso.privacyfriendlypasswordgenerator.helpers.RecyclerItemClickListener;
import org.secuso.privacyfriendlypasswordgenerator.dialogs.AddMetaDataDialog;
import org.secuso.privacyfriendlypasswordgenerator.helpers.MetaDataAdapter;
import org.secuso.privacyfriendlypasswordgenerator.R;
import org.secuso.privacyfriendlypasswordgenerator.database.MetaData;
import org.secuso.privacyfriendlypasswordgenerator.database.MetaDataSQLiteHelper;
import org.secuso.privacyfriendlypasswordgenerator.dialogs.GeneratePasswordDialog;
import org.secuso.privacyfriendlypasswordgenerator.dialogs.UpdateMetadataDialog;
import org.secuso.privacyfriendlypasswordgenerator.helpers.SwipeableRecyclerViewTouchListener;

import java.util.List;

/**
 * Code for displaying cards according to the tutorial from https://code.tutsplus.com/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465
 * accessed on 20th June 2016
 */

public class MainActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private MetaDataAdapter adapter;
    private List<MetaData> metadatalist;
    MetaDataSQLiteHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new MetaDataSQLiteHelper(this);
        metadatalist = database.getAllmetaData();

        adapter = new MetaDataAdapter(metadatalist);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);

        //No screenshot
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getBaseContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Log.d("Main Activity", Integer.toString(position));
                        Bundle bundle = new Bundle();
                        bundle.putInt("position", position);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        GeneratePasswordDialog generatePasswordDialog = new GeneratePasswordDialog();
                        generatePasswordDialog.setArguments(bundle);
                        generatePasswordDialog.show(fragmentManager, "GeneratePasswordDialog");
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Log.d("Main Activity", Integer.toString(position));
                        Bundle bundle = new Bundle();
                        bundle.putInt("position", position);
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        UpdateMetadataDialog updateMetadataDialog = new UpdateMetadataDialog();
                        updateMetadataDialog.setArguments(bundle);
                        updateMetadataDialog.show(fragmentManager, "UpdateMetadataDialog");
                    }
                })
        );

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }
                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    metadatalist.remove(position);
                                    //MetaData tempData = database.getMetaData(position + 1 );
                                    //Log.d("DELETE IT",  tempData.getDOMAIN());
                                    database.deleteMetaData(position + 1);
                                    Toast toast = Toast.makeText(getBaseContext(), "Swipe left detected", Toast.LENGTH_SHORT);
                                    toast.show();
                                    adapter.notifyItemRemoved(position);
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    Toast toast = Toast.makeText(getBaseContext(), "Swipe right detected", Toast.LENGTH_SHORT);
                                    toast.show();
                                    adapter.notifyItemRemoved(position);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });

        recyclerView.addOnItemTouchListener(swipeTouchListener);



//        Log.d("Insert: ", "Inserting ..");
//        database.addMetaData(new MetaData(1, "first.de", 13, 1, 1, 1, 1));
//        database.addMetaData(new MetaData(1, "second.de", 14, 1, 1, 1, 1));
//        database.addMetaData(new MetaData(1, "third.de", 14, 1, 1, 1, 1));
//        database.addMetaData(new MetaData(1, "fourth.de", 16, 1, 1, 1, 1));

        Log.d("Reading: ", "Reading all data..");

//        for (MetaData cn : contacts) {
//            String log = "Id: "+cn.getID()+" ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber();
//            // Writing Contacts to log
//            Log.d("Name: ", log);



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
}
