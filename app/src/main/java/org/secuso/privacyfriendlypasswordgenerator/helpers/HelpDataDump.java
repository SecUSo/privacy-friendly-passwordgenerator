/**
 * This file is part of Privacy Friendly Password Generator.

 Privacy Friendly Password Generator is free software:
 you can redistribute it and/or modify it under the terms of the
 GNU General Public License as published by the Free Software Foundation,
 either version 3 of the License, or any later version.

 Privacy Friendly Password Generator is distributed in the hope
 that it will be useful, but WITHOUT ANY WARRANTY; without even
 the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 See the GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Privacy Friendly Password Generator. If not, see <http://www.gnu.org/licenses/>.
 */

package org.secuso.privacyfriendlypasswordgenerator.helpers;

import android.content.Context;

import org.secuso.privacyfriendlypasswordgenerator.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Class structure taken from tutorial at http://www.journaldev.com/9942/android-expandablelistview-example-tutorial
 * last access 27th October 2016
 */

public class HelpDataDump {

    private Context context;

    public HelpDataDump(Context context) {
        this.context = context;
    }

    public LinkedHashMap<String, List<String>> getDataGeneral() {
        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<String, List<String>>();

        List<String> overview = new ArrayList<String>();
        overview.add(context.getResources().getString(R.string.help_overview_answer));
        expandableListDetail.put(context.getResources().getString(R.string.help_overview), overview);

        List<String> generate = new ArrayList<String>();
        generate.add(context.getResources().getString(R.string.help_generation_answer));
        expandableListDetail.put(context.getResources().getString(R.string.help_generation), generate);

        List<String> general = new ArrayList<String>();
        general.add(context.getResources().getString(R.string.help_parameter_set_answer));
        expandableListDetail.put(context.getResources().getString(R.string.help_parameter_set_heading), general);

        List<String> features = new ArrayList<String>();
        features.add(context.getResources().getString(R.string.help_which_parameters_domain));
        features.add(context.getResources().getString(R.string.help_which_parameters_username));
        features.add(context.getResources().getString(R.string.help_which_parameters_characterset));
        features.add(context.getResources().getString(R.string.help_which_parameters_length));
        features.add(context.getResources().getString(R.string.help_which_parameters_password_counter));
        expandableListDetail.put(context.getResources().getString(R.string.help_which_parameters_heading), features);

        List<String> add = new ArrayList<String>();
        add.add(context.getResources().getString(R.string.help_manage_add_description));
        expandableListDetail.put(context.getResources().getString(R.string.help_manage_add_heading), add);

        List<String> update = new ArrayList<String>();
        update.add(context.getResources().getString(R.string.help_manage_update_description));
        expandableListDetail.put(context.getResources().getString(R.string.help_manage_update_heading), update);

        List<String> delete = new ArrayList<String>();
        delete.add(context.getResources().getString(R.string.help_manage_delete_description));
        expandableListDetail.put(context.getResources().getString(R.string.help_manage_delete_heading), delete);

        List<String> generation = new ArrayList<String>();
        generation.add(context.getResources().getString(R.string.help_generation_description));
        expandableListDetail.put(context.getResources().getString(R.string.help_generation_heading), generation);

        List<String> storage = new ArrayList<String>();
        storage.add(context.getResources().getString(R.string.help_password_storage_description));
        expandableListDetail.put(context.getResources().getString(R.string.help_password_storage_heading), storage);

        List<String> permissions = new ArrayList<String>();
        permissions.add(context.getResources().getString(R.string.help_permissions_description));
        expandableListDetail.put(context.getResources().getString(R.string.help_permissions_heading), permissions);

        return expandableListDetail;
    }

}
