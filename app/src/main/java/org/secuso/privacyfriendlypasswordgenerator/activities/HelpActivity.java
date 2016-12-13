package org.secuso.privacyfriendlypasswordgenerator.activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import org.secuso.privacyfriendlypasswordgenerator.R;

/**
 * @author Karola Marky
 * @version 20160617
 */
public class HelpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_help);
        //getFragmentManager().beginTransaction().replace(android.R.id.content, new HelpFragment()).commit();

        overridePendingTransition(0, 0);
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_help;
    }

    public static class HelpFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.help);
        }
    }

}

