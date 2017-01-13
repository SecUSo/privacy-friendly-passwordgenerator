package org.secuso.privacyfriendlypasswordgenerator.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import org.secuso.privacyfriendlypasswordgenerator.BuildConfig;
import org.secuso.privacyfriendlypasswordgenerator.R;

/**
 * @author Karola Marky
 * @version 20170113
 */
public class AboutActivity extends BaseActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        View mainContent = findViewById(R.id.main_content);
        if (mainContent != null) {
            mainContent.setAlpha(0);
            mainContent.animate().alpha(1).setDuration(BaseActivity.MAIN_CONTENT_FADEIN_DURATION);
        }

        overridePendingTransition(0, 0);

        ((TextView)findViewById(R.id.secusoWebsite)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView)findViewById(R.id.githubURL)).setMovementMethod(LinkMovementMethod.getInstance());
        ((TextView)findViewById(R.id.textFieldVersionName)).setText(BuildConfig.VERSION_NAME);
    }

    @Override
    protected int getNavigationDrawerID() {
        return R.id.nav_about;
    }

}
