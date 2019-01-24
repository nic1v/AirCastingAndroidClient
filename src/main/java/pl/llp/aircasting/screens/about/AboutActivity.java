/**
 AirCasting - Share your Air!
 Copyright (C) 2011-2012 HabitatMap, Inc.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.

 You can contact the authors by email at <info@habitatmap.org>
 */
package pl.llp.aircasting.screens.about;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import pl.llp.aircasting.R;
import pl.llp.aircasting.util.Logger;
import pl.llp.aircasting.sessionSync.SyncBroadcastReceiver;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pl.llp.aircasting.screens.about.TextViewHelper.stripUnderlines;

public class AboutActivity extends RoboActivity implements AppCompatCallback
{
  public static final String HEADING = "heading";

  @Inject LayoutInflater layoutInflater;

  @InjectView(R.id.about) ExpandableListView about;

  @Inject SyncBroadcastReceiver syncBroadcastReceiver;

  private AppCompatDelegate delegate;
  private String[] headings;
  private String[] contents;

  public void initializeSections() {
    headings = new String[]{
        getString(R.string.about_hardware_developers),
        getString(R.string.about_airbeam2),
        getString(R.string.about_phone_microphone),
        getString(R.string.about_connect_external_device),
        getString(R.string.about_record_mobile),
        getString(R.string.about_view_data),
        getString(R.string.about_note),
        getString(R.string.about_stop_mobile),
        getString(R.string.about_session_options),
        getString(R.string.about_disable_maps),
        getString(R.string.about_open_source),
        getString(R.string.about_privacy_policy),
        getString(R.string.about_thanks),
        getString(R.string.about_version)
    };
    contents = new String[]{
        getString(R.string.about_hardware_developers_content),
        getString(R.string.about_airbeam2_content),
        getString(R.string.about_phone_microphone_content),
        getString(R.string.about_connect_external_device_content),
        getString(R.string.about_record_mobile_content),
        getString(R.string.about_view_data_content),
        getString(R.string.about_note_content),
        getString(R.string.about_stop_mobile_content),
        getString(R.string.about_session_options_content),
        getString(R.string.about_disable_maps_content),
        getString(R.string.about_open_source_content),
        getString(R.string.about_privacy_policy_content),
        getString(R.string.about_thanks_content),
        getVersion()
    };
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    delegate = AppCompatDelegate.create(this, this);
    delegate.onCreate(savedInstanceState);

    setContentView(R.layout.about);

    initToolbar();
    initializeSections();
    initializeAbout();
  }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(syncBroadcastReceiver, SyncBroadcastReceiver.INTENT_FILTER);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(syncBroadcastReceiver);
    }

    public void initToolbar() {
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.abc_ic_ab_back_material));
      toolbar.setContentInsetStartWithNavigation(0);

      delegate.setSupportActionBar(toolbar);
      delegate.setTitle("About");
      toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
      });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

    private void initializeAbout() {
        ExpandableListAdapter adapter = new AboutAdapter();

        about.setAdapter(adapter);
    }

    private List<Map<String, String>> headings() {
        ArrayList<Map<String, String>> result = new ArrayList<Map<String, String>>();

        for (String heading : headings) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(HEADING, heading);
            result.add(map);
        }

        return result;
    }

    private String getVersion() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);

            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e("Error while fetching app version", e);
            return "?";
        }
    }

    @Override
    public void onSupportActionModeStarted(ActionMode mode) { }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) { }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    private class AboutAdapter extends SimpleExpandableListAdapter {
        public AboutAdapter() {
            super(AboutActivity.this, headings(), R.layout.about_heading, new String[]{HEADING}, new int[]{R.id.heading}, null, 0, null, null);
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.about_content, null);
            }

            String text = contents[groupPosition];
            Spanned spanned = Html.fromHtml(text);
            spanned = stripUnderlines(spanned);

            TextView view = (TextView) convertView.findViewById(R.id.content);
            view.setText(spanned);
            view.setMovementMethod(LinkMovementMethod.getInstance());

            return convertView;
        }
    }
}
