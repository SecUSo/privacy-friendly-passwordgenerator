package org.secuso.privacyfriendlypasswordgenerator.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.secuso.privacyfriendlypasswordgenerator.R;
import org.secuso.privacyfriendlypasswordgenerator.database.MetaData;
import org.secuso.privacyfriendlypasswordgenerator.database.MetaDataSQLiteHelper;
import org.secuso.privacyfriendlypasswordgenerator.dialogs.AddMetaDataDialog;
import org.secuso.privacyfriendlypasswordgenerator.dialogs.GeneratePasswordDialog;
import org.secuso.privacyfriendlypasswordgenerator.dialogs.UpdateMetadataDialog;
import org.secuso.privacyfriendlypasswordgenerator.helpers.MetaDataAdapter;
import org.secuso.privacyfriendlypasswordgenerator.helpers.RecyclerItemClickListener;
import org.secuso.privacyfriendlypasswordgenerator.helpers.SwipeableRecyclerViewTouchListener;

import java.util.List;

/**
 * Code for displaying cards according to the tutorial from https://code.tutsplus.com/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465
 * @author Karola Marky
 * @version 20170112
 */


public class MainActivity extends BaseActivity {

    private MetaDataAdapter adapter;
    private List<MetaData> metadatalist;
    MetaDataSQLiteHelper database;

    boolean clipboard_enabled;
    boolean bindToDevice_enabled;
    String hash_algorithm;
    int number_iterations;

    LinearLayout initialAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = MetaDataSQLiteHelper.getInstance(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        metadatalist = database.getAllMetaData();

        initialAlert = (LinearLayout) findViewById(R.id.insert_alert);
        hints(metadatalist.size());

        adapter = new MetaDataAdapter(metadatalist);
        recyclerView.setAdapter(adapter);

        loadPreferences();

        int current = 0;
        for (MetaData data : metadatalist) {
            data.setPOSITIONID(current);
            current++;
        }

        //No screenshot
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
//                WindowManager.LayoutParams.FLAG_SECURE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getBaseContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                        Bundle bundle = new Bundle();

                        //Gets ID for look up in DB
                        MetaData temp = metadatalist.get(position);

                        bundle.putInt("position", temp.getID());
                        bundle.putString("hash_algorithm", hash_algorithm);
                        bundle.putBoolean("clipboard_enabled", clipboard_enabled);
                        bundle.putBoolean("bindToDevice_enabled", bindToDevice_enabled);
                        bundle.putInt("number_iterations", number_iterations);

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        GeneratePasswordDialog generatePasswordDialog = new GeneratePasswordDialog();
                        generatePasswordDialog.setArguments(bundle);
                        generatePasswordDialog.show(fragmentManager, "GeneratePasswordDialog");

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Log.d("Main Activity", Integer.toString(position));
                        Bundle bundle = new Bundle();

                        //Gets ID for look up in DB
                        MetaData temp = metadatalist.get(position);

                        bundle.putInt("position", temp.getID());
                        bundle.putString("hash_algorithm", hash_algorithm);
                        bundle.putInt("number_iterations", number_iterations);
                        bundle.putBoolean("bindToDevice_enabled", bindToDevice_enabled);

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
                                    deleteItem(position);
                                }
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    deleteItem(position);
                                }
                            }
                        });

        recyclerView.addOnItemTouchListener(swipeTouchListener);


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

    public void deleteItem(int position) {

        MetaData toDeleteMetaData = metadatalist.get(position);
        final MetaData toDeleteMetaDataFinal = toDeleteMetaData;

        //Removes MetaData from DB
        database.deleteMetaData(toDeleteMetaData);

        //Removes MetaData from List in View
        metadatalist.remove(position);

        final int finalPosition = position;

        initialAlert.setVisibility(View.VISIBLE);
        hints(position);

        Snackbar.make(findViewById(android.R.id.content), getString(R.string.domain) + " " + toDeleteMetaData.getDOMAIN() + " " + getString(R.string.item_deleted), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        database.addMetaDataWithID(toDeleteMetaDataFinal);
                        metadatalist.add(finalPosition, toDeleteMetaDataFinal);
                        adapter.notifyItemInserted(finalPosition);
                        adapter.notifyDataSetChanged();
                        initialAlert.setVisibility(View.GONE);
                        hints(1);
                    }
                }).show();

        adapter.notifyItemRemoved(position);

    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_example;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (database.getAllMetaData().size() > 0) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_delete) {

            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.delete_dialog))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            database.deleteAllMetaData();
                            Toast.makeText(MainActivity.this, getString(R.string.delete_dialog_success), Toast.LENGTH_SHORT).show();
                            MainActivity.this.recreate();
                        }
                    })
                    .setNegativeButton(R.string.cancel, null).show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPreferences();
    }

    public void loadPreferences() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        clipboard_enabled = sharedPreferences.getBoolean("clipboard_enabled", false);
        bindToDevice_enabled = sharedPreferences.getBoolean("bindToDevice_enabled", false);
        hash_algorithm = sharedPreferences.getString("hash_algorithm", "SHA256");
        String tempIterations = sharedPreferences.getString("hash_iterations", "1000");
        number_iterations = Integer.parseInt(tempIterations);
    }

    public void hints(int position) {

        Animation anim = new AlphaAnimation(0.0f, 1.0f);

        if (metadatalist.size() == 0 || position == 0) {

            initialAlert.setVisibility(View.VISIBLE);
            anim.setDuration(1500);
            anim.setStartOffset(20);
            anim.setRepeatMode(Animation.REVERSE);
            anim.setRepeatCount(Animation.INFINITE);
            initialAlert.startAnimation(anim);

        } else {
            initialAlert.setVisibility(View.GONE);
            initialAlert.clearAnimation();
        }

    }

}
